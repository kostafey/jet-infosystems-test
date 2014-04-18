(defproject jet-infosystems-test "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.6"]
                 [ring/ring-json "0.1.2"]
                 [clj-tagsoup "0.3.0"]
                 [cheshire "5.3.1"]]
  :plugins [[lein-ring "0.8.10"]]
  :ring {:handler jet-infosystems-test.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]
                        [clj-http "0.9.1"]]}})
