(defproject jet-infosystems-test "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.5.2"]
                 [ring/ring-jetty-adapter "1.2.1"]
                 [ring/ring-json "0.1.2"]
                 [clj-tagsoup "0.3.0" :exclusions [org.clojure/clojure]]
                 [cheshire "5.3.1"]
                 [org.clojure/core.async "0.3.442"]]
  :plugins [[lein-ring "0.8.10"]]
  :ring {:handler jet-infosystems-test.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]
                        [clj-http "2.3.0"]]}}
  :main jet-infosystems-test.handler)
