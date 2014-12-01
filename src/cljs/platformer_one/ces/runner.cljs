(ns platformer-one.ces.runner
  (:require [quile.component :as component]
            [platformer-one.ces.animation :refer [each-animation-frame]]
            [platformer-one.ces.world :as world]))

(defrecord Runner [world pause]
  component/Lifecycle
  (start [this]
    (assoc this :stop-animation
      (each-animation-frame #(world/update! world))))

  (stop [this]
    ((:stop-animation this))))

(defn runner []
  (map->Runner {}))
