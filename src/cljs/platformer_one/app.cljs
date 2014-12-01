(ns platformer-one.app
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cljs.core.async :refer [put! <! >! chan timeout]]))

(defn start! [app-state]
  (om/root
    (fn [app owner]
      (reify
        om/IRender
        (render [_]
          (dom/h1 nil "State:" (:text app)))))
    app-state
    {:target (. js/document (getElementById "app"))}))

;; (defn show-text! [text]
;;   (swap! app-state #(assoc % :text text)))
