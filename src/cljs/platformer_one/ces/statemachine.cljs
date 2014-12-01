(ns platformer-one.ces.statemachine)

(def *debug* false)

(defprotocol IStateMachine
  (do-action [this action]))

(defrecord StateMachine [states state]
  IStateMachine
  (do-action [this action]
    (let [result (assoc this :state (get (get states state) action))]
      (if *debug*
        (print state "->" (:state result) states))
      result)))

(defn state-machine [states state]
  (map->StateMachine {:states states :state state}))

