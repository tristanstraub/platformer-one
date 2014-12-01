(ns platformer-one.ces.animation)

(def request-animation-frame
  (or (.-requestAnimationFrame js/window)
      (.-webkitRequestAnimationFrame js/window)
      (.-mozRequestAnimationFrame js/window)
      (.-msRequestAnimationFrame js/window)
      (fn [callback] (js/setTimeout callback 17))))

(defn each-animation-frame [f]
  (let [continue-running? (atom true)]
    (request-animation-frame
     (fn [] 
       (f)
       (if @continue-running?
         (each-animation-frame f))))
    (fn []
      (reset! continue-running? false))))
