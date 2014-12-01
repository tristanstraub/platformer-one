(ns platformer-one.game.system
  (:require [quile.component :as component]
            [platformer-one.ces.runner :refer [runner]]

            [platformer-one.components.svg :as svg]
            [platformer-one.components.animation :as animation]
            [platformer-one.app :as app]

            [platformer-one.snap.assets :refer [snap-asset-manager]]
            [platformer-one.snap.canvas :refer [snap-canvas]]

            [platformer-one.game.world :refer [world]]))

(defn components []
  (component/system-map
             :svg 
             (component/using
                        (svg/svg)
                        [:assets :canvas])

             :animation
             (animation/animation)))

(defn game-system [game-state]
  (component/system-map
             :game-state game-state
             :assets (snap-asset-manager)
             :canvas (snap-canvas)
             :components (component/using (components) [:assets :canvas])
             :world (component/using (world) [:components :game-state])
             :game-runner (component/using (runner) [:world])))

