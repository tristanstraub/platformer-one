(ns platformer-one.dev
  (:require [environ.core :refer [env]]
            [net.cgrand.enlive-html :refer [set-attr prepend append html]]
            [cemerick.piggieback :as piggieback]
            [weasel.repl.websocket :as weasel]
            [leiningen.core.main :as lein]))

(def is-dev? (env :is-dev))

(def inject-devmode-html
  (comp
     (set-attr :class "is-dev")
     (prepend (html [:script {:type "text/javascript" :src "/js/out/goog/base.js"}]))
     (prepend (html [:script {:type "text/javascript" :src "/react/react.js"}]))
     (prepend (html [:script {:type "text/javascript" :src "/js/jquery-1.11.1.min.js"}]))
     (prepend (html [:script {:type "text/javascript" :src "/js/snap.svg.js"}]))
     ;; (prepend (html [:script {:type "text/javascript" :src "//cdnjs.cloudflare.com/ajax/libs/jquery/2.1.1/jquery.min.js"}]))
     ;; (prepend (html [:script {:type "text/javascript" :src "//cdnjs.cloudflare.com/ajax/libs/snap.svg/0.3.0/snap.svg-min.js"}]))
     (append  (html [:script {:type "text/javascript"} "goog.require('platformer_one.dev')"]))))

(defn browser-repl []
  (piggieback/cljs-repl :repl-env (weasel/repl-env :ip "0.0.0.0" :port 9001)))

(defn start-figwheel []
  (future
    (print "Starting figwheel.\n")
    (lein/-main ["figwheel"])))
