(ns platformer-one.game.scene
  (:require [platformer-one.game.robot :as robot]))

(def descriptor
  {:robot1 {:descriptor robot/descriptor
            :transforms [{:translate [100 100]} {:rotate -90 :around "#joint-body"}
                         ]}
   :robot2 {:descriptor robot/descriptor
            ;; TODO before after transforms (translate after, rotate before)
            :transforms [{:translate [100 20]} {:rotate 45 :around "#joint-body"}
                         ]}
   })
