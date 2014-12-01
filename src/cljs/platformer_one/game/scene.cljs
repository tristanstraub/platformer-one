(ns platformer-one.game.scene
  (:require [platformer-one.game.robot :as robot]))

(def descriptor
  {:robot1 {:descriptor robot/descriptor 
            :transforms [{:translate [300 100]} {:rotate 45}]}
   :robot2 {:descriptor robot/descriptor 
            :transforms [{:translate [0 0]} {:rotate 10}]}})
