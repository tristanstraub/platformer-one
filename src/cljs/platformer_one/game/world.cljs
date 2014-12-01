(ns platformer-one.game.world
  (:require [quile.component :as component]
            [platformer-one.ces.component :refer [create-state update-components synchronise-components!]]
            [platformer-one.ces.world :refer [IWorld]]
            [platformer-one.game.robot :as robot]
            [platformer-one.game.scene :as scene]
            [platformer-one.game.pose :as pose]
            [platformer-one.game.protocols :as p]
            [platformer-one.game.sound :as sound]))

(defn get-pose [{:keys [keys-down]}]
  (if (keys-down 39)
    :standing
    :raised-arms))

(defn update-robot [{:keys [components] :as world} robot game-state]

  (-> robot
      ;; keyboard pose
      (update-in [:pose :key] (fn [key] (:pose (:animation robot))))

      ;; animation
      (update-in [:svg] (fn [svg] 
                          (p/apply-mask (:svg components) svg (pose/get-selectors (:pose robot)))))
      (update-in [:svg] (fn [svg] 
                          (p/apply-order (:svg components) svg (pose/get-selector-order (:pose robot)))))
      (update-in [:svg] (fn [svg] 
                          (p/apply-transforms (:svg components) svg (concat (pose/get-transforms (:pose robot))
                                                                            (:transforms robot)))))))

(defn update-entities [world entities components]
  (let [game-state @(:game-state world) 
        update-components #(update-components world % components)
        update-robots (partial map (fn [entity]
                                     (if (:robot? entity)
                                       (update-robot world entity game-state)
                                       entity)))]
    (-> entities
        update-robots
        update-components
        )))

(defrecord World [components entities game-state]
  component/Lifecycle
  (start [this]
    (assoc this
      :ticks (atom 0)
      :entities
      (atom (map (fn [[key {:keys [descriptor transforms]}]] 
                   {:robot? true
                    :svg (assoc (create-state (:svg components)) :name "robot.svg")
                    :pose {:key :standing :descriptor descriptor}
                    :animation (assoc (create-state (:animation components))
                                 :frames (:walking (:animations descriptor)))
                    :transforms (map (fn [transform] [:root transform]) 
                                     transforms)})
                 scene/descriptor))))

  IWorld
  (update! [this]
    (-> (swap! entities #(update-entities this % components))
        (synchronise-components! components))
    (swap! (:ticks this) inc)))

(defn world []
  (map->World {}))
