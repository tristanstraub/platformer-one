(ns platformer-one.ces.keyboard
  (:use [jayq.core :only [$]]))

(def keys-down (atom #{}))

(defn right-arrow [] (@keys-down 39))
(defn left-arrow [] (@keys-down 37))

(defn get-key [e]
  (.-which e))

(defn key-down-handler [e]
  (let [key (get-key e)]
    ;; prevent scrolling when arrow keys are pressed
    (when (#{37 38 39 40} key)
      (.preventDefault e))
    (swap! keys-down #(conj % key))))

(defn key-up-handler [e]
  (let [key (get-key e)]
    (swap! keys-down #(disj % key))))

(defn init []
  ($ #(-> ($ "body")
          (.keydown (fn [e] (key-down-handler e)))
          (.keyup (fn [e] (key-up-handler e))))))
