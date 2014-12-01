(ns platformer-one.snap.canvas
  (:require [quile.component :as component]))

(defonce snap-instance (atom nil))

(defrecord SnapCanvas [snap]
  component/Lifecycle
  (start [this]
    (assoc this :snap (doto (swap! snap-instance #(or % (js/Snap 1000 1000)))
                        .clear))))

(defn snap-canvas []
  (map->SnapCanvas {}))
