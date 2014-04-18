(ns jet-infosystems-test.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [clj-http.client :as client]
            [pl.danieljanus.tagsoup :as tagsoup]
            [cheshire.core :as json]))

(defn parse-rss-response [res]
  "Получаем список полей link из RSS"
  (let [is-item? (fn [x] (if (coll? x)
                           (= (first x) :item)
                           false))
        ;; По каждому слову ищем только первые 10 записей
        items (take 10
                    (filter is-item?
                            (get (tagsoup/parse-string (get res :body)) 2)))
        ;; Из каждого результата извлекаем основную ссылку (поле link)
        links (map #(loop [i-elements %]
                      (if (seq i-elements)
                        (let [element (first i-elements)]
                          (if (and (coll? element)
                                   (= (first element) :link))
                            (-> i-elements next first)
                            (recur (next i-elements))))))
                   items)]
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
  (json/generate-string (loop [ds domains
                               acc {}]
                          (if (seq ds)
                            (let [d (first ds)
                                  d-in-map (get acc d)]
                              (if d-in-map
                                ;; Считаем количество одноименных доменов
                                (recur (next ds) (assoc acc d (inc d-in-map)))
                                (recur (next ds) (assoc acc d 1))))
                            acc))
                        {:pretty true}))

;; Максимальное количество одновременных HTTP-соединений
(def ^:dynamic *max-threads* 3)

(defn run-queries [queries]
  "Многопоточное получение результатов HTTP-соединений, формирование json-ответа."
  (let [threads (loop [qs queries
                       result ()]
                  (if (seq qs)
                    (let [q (first qs)]
                      (recur
                       (next qs)
                       ;; Создается поток с отложенным запуском - получение
                       ;; и парсинг RSS для данного запроса.
                       (conj result (delay (-> q client/get parse-rss-response)))))
                    result))
        results (loop [ts threads
                       result ()]
                  (if (seq ts)
                    ;; Если количество запущенных, невыполненных потоков не
                    ;; превышает максимальное количество одновременных
                    ;; HTTP-соединений
                    (if (< (count (filter #(not (realized? %)) result)) *max-threads*)
                      (let [t (first ts)]
                        ;; Запуск нового потока.
                        @t
                        ;; Поток перемещается в список запущенных и/или выполненных
                        (recur (next ts) (conj result t)))
                      (do
                        ;; Достигнут лимит запущенных потоков - ждем завершения
                        ;; потоков 0,1 сек.
                        (Thread/sleep 100)
                        (recur ts result)))
                    result))]
    ;; Довычислять последние невычисленные потоки (если остались невычисленные,
    ;; их число всегда меньше *max-threads*) и получить закешированные
    ;; результаты
    (-> (map deref results)
        get-domains
        prepare-statistics-json)))

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
  (GET "/" [] (str "You should use \"search\" url, e.g. "
                   "\"http://localhost:8080/search?query=jet&query=smap\"."))
  (GET "/search" req-params (process-search req-params))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))

