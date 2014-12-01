(ns platformer-one.components.animation
  (:require 
   [platformer-one.ces.component :refer [IComponent]]
   [platformer-one.game.protocols :as p]
   [platformer-one.ces.statemachine :as sm]))

(def *default-duration* 500)

(defn get-pose [state ticks]
  (cond (< 30 (mod ticks 60)) (get (:frames state) 0)
        :else (get (:frames state) 1)))

(defn set-pose [state]
  (assoc state :pose  (let [frame (get (:frames state) 0)]
                        ;; support delays?
                        (:pose frame))))

(defn transition-states [state {:keys [ticks]}]
  (loop [state state 
         previous-states nil]
    (if (= (:states state) previous-states)
      state
      (recur (case (get-in state [:states :state])
               ;; TODO fix ticks deref
               :init 
               (-> state
                   (update-in [:states] #(sm/do-action % :next-frame)))

               :next-frame
               (-> state
                   (update-in [:frame] #(mod (inc (or % -1)) 
                                             (count (:frames state))))
                   (as-> state
                         (update-in state [:pose] (fn [pose]
                                                    ;; maintain previous pose if the current frame has none
                                                    (or (get-in state [:frames (:frame state) :pose])
                                                        pose))))
                   ;; TODO duration
                   (assoc :frame-tick-begin @ticks)
                   (update-in [:states] #(sm/do-action % :wait)))

               :wait
               (let [duration (or (get-in state [:frames (:frame state) :duration])
                                  *default-duration*)
                     delta-ticks (- @ticks (:frame-tick-begin state))]
                 (if (<= duration delta-ticks)
                   ;; next frame
                   (update-in state [:states] #(sm/do-action % :next-frame))
                   state)))

             (:states state)))))

(defrecord Animation []
  IComponent
  (create-state [this] {:frames [] :pose nil 
                        :states (sm/state-machine {:init {:next-frame :next-frame}
                                                   :next-frame {:wait :wait}
                                                   :wait {:next-frame :next-frame}}
                                    :init)})

  (update-component-state [this world state]
    (-> state
        ;; update state-machine state
        (transition-states world)))
 
  (synchronise! [this state]))

(defn animation []
  (Animation.))
