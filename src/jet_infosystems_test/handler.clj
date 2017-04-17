(ns jet-infosystems-test.handler
  (:use compojure.core
        ring.adapter.jetty)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [clj-http.client :as client]
            [pl.danieljanus.tagsoup :as tagsoup]
            [cheshire.core :as json]
            [clojure.core.async :as async
             :refer [<! >! <!! >!! timeout chan alt! alts!! go buffer]]))

(defn pos [pred coll]
  (->> coll
       (map-indexed #(when (pred %2) %1))
       (remove nil?)
       (first)))

(defn is-item? [x]
  (if (coll? x)
    (= (first x) :item)
    false))

(defn is-link? [tag]
  (and (coll? tag)
       (= (first tag) :link)))

(defn get-link [item]
  (nth item (inc (pos is-link? item))))

(defn parse-rss-response [res]
  "Получаем список полей link из RSS"
  (let [;; По каждому слову ищем только первые 10 записей
        items (take 10
                    (filter is-item?
                            (get (tagsoup/parse-string (get res :body)) 2)))
        ;; Из каждого результата извлекаем основную ссылку (поле link)
        links (remove nil? (map get-link items))]
    links))

(defn get-domains [links]
  "Преобразуем список ссылок в список доменов"
  ;; В случае, если по разным ключевым словам было найдены полностью идентичные
  ;; ссылки, хост должен учитываться только один раз.
  (let [links (set (flatten links))
        ;; Из ссылки берем hostname, из которого берется только домен второго уровня
        parse-domain (fn [link]
                       (get (re-find #"http[s]?://(.*\.)?(\w+[\\.]\w+)/" link) 2))
        domains (map parse-domain links)]
    domains))

(defn prepare-statistics-json [domains]
  "Считаем количество одноименных доменов, формируем json."
  (json/generate-string (frequencies domains)
                        {:pretty true}))

;; Максимальное количество одновременных HTTP-соединений
(def ^:dynamic *max-threads* 3)
(def ^:dynamic *timeout* 1000)

;; Канал задач ограниченной длины
(def workers (chan (buffer *max-threads*)))
;; Канал результатов
(def results (chan))

(defn in?
  "True if seq contains elm."
  [seq elm]
  (some #(= elm %) seq))

(defn filter-timeouts [values]
  (filter #(not (in? [:timeout :exception] %)) values))

(defn wait [ms f & args]
  (let [r (chan)
        t (timeout ms)
        _ (go (>! r (apply f args)))
        [value channel] (alts!! [r t])]
    (if (= channel t)
      :timeout
      value)))

(defn pretend-log [result q]
  (println (if (not (in? [:timeout :exception] result))
             "+ success"
             (str "- " (name result)))
           "for" q))

(defn run-queries [queries]
  "Многопоточное получение результатов HTTP-соединений, формирование json-ответа."
  (go
    (doseq [q queries]
      ;; Синхронная запись задач в канал
      (>!! workers
           (go
             (let [result (wait *timeout*
                                (fn []
                                  (try
                                    (parse-rss-response
                                     (client/get q {:conn-timeout *timeout*}))
                                    (catch Throwable e
                                      :exception))))]
               (pretend-log result q)
               ;; Выполнившаяся задача записывает результат в канал результатов
               (>! results result))
             ;; Выполнившаяся задача освобождает канал задач
             (<! workers)))))
  ;; Чтение из канала результатов "довычислит" еще невычисленные потоки
  (-> (for [_ (range (count queries))]
        (<!! results))
      filter-timeouts
      get-domains
      prepare-statistics-json))

(defn process-search [req-params]
  ;; Фильтруем все параметры запроса "query"
  (let [queries (let [query (get (:query-params req-params) "query")]
                  (if (not (coll? query))
                    (vector query)
                    query))]
    ;; Возвращаем ответ в виде json
    {:status 200
     :headers {"Content-Type" "application/json"}
     :body (run-queries (map #(str "http://blogs.yandex.ru/search.rss?text=" %)
                             queries))}))

(defroutes app-routes
  ;; http://localhost:3000/search?query=jet&query=smap&query=text&query=info&query=data&query=home
  (GET "/" [] (str "You should use \"search\" url, e.g. "
                   "\"http://localhost:8080/search?query=jet&query=smap\"."))
  (GET "/search" req-params (process-search req-params))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))

(defn -main [& args]
  (run-jetty app {:port 8080}))
