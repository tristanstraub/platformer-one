(defproject platformer-one "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :source-paths ["src/clj" "src/cljs"]

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2371" :scope "provided"]
                 [ring "1.3.1"]
                 [compojure "1.2.0"]
                 [enlive "1.1.5"]
                 [om "0.7.3"]
                 [figwheel "0.1.4-SNAPSHOT"]
                 [environ "1.0.0"]
                 [com.cemerick/piggieback "0.1.3"]
                 [weasel "0.4.0-SNAPSHOT"]
                 [jayq "2.5.2"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [leiningen "2.5.0"]
                 [com.stuartsierra/component "0.2.2"]
                 [quile/component-cljs "0.2.2"]
                 [hum "0.3.0"]] 

  :plugins [[lein-cljsbuild "1.0.3"]
            [lein-environ "1.0.0"]]

  :min-lein-version "2.5.0"

  :uberjar-name "platformer-one.jar"

  :cljsbuild {:builds {:app {:source-paths ["src/cljs"]
                             :compiler {:output-to     "resources/public/js/app.js"
                                        :output-dir    "resources/public/js/out"
                                        :source-map    "resources/public/js/app.js.map"
                                        :preamble      ["react/react.min.js" "public/js/jquery-1.11.1.min.js" "public/js/snap.svg-min.js"]
                                        :externs       ["react/externs/react.js" "externs/jquery-1.9.js" "public/js/snap.svg.js"]
                                        :optimizations :none
                                        :pretty-print  true}}}}

  :profiles {:dev {:repl-options {:init-ns platformer-one.server
                                  :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}

                   :plugins [[lein-figwheel "0.1.4-SNAPSHOT"]]
                   :figwheel {:http-server-root "public"
                              :port 3449
                              :css-dirs ["resources/public/css"]}

                   :env {:is-dev true}

                   :cljsbuild {:builds {:app {:source-paths ["env/dev/cljs"]}}}}

             :uberjar {:hooks [leiningen.cljsbuild]
                       :env {:production true}
                       :omit-source true
                       :aot :all
                       :cljsbuild {:builds {:app
                                            {:source-paths ["env/prod/cljs"]
                                             :compiler
                                             {:optimizations :advanced
                                              :pretty-print false}}}}}

             :safe {:hooks [leiningen.cljsbuild]
                    :env {:production true}
                    :omit-source true
                    :aot :all
                    :cljsbuild {:builds {:app
                                         {:source-paths ["env/prod/cljs"]
                                          :compiler
                                          {:optimizations :advanced
                                           :pretty-print false}}}}}})
