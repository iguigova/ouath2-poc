(defproject clj-oauth2-poc "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [stuarth/clj-oauth2 "0.3.2"]
                 [compojure "1.6.1"]
                 [ring/ring-core "1.9.0"]
                 [ring/ring-jetty-adapter "1.9.6"]]
  :main ^:skip-aot clj-oauth2-poc.core
  :profiles {:uberjar {:aot :all :uberjar-name clj-oauth2-poc.jar}}
  :repl-options {:init-ns clj-oauth2-poc.core})
