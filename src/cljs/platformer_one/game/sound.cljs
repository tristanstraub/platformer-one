(ns platformer-one.game.sound
  (:require [hum.core :as hum]))

(defonce ctx (atom nil))
(defonce nodes (atom []))

(defn- get-context! []
  (swap! ctx #(or % (hum/create-context))))

(defn start! []
  (let [ctx (get-context!)
        vco (hum/create-osc ctx :sawtooth)
        vcf (hum/create-biquad-filter ctx)
        output (hum/create-gain ctx)]

    (hum/connect vco vcf)
    (hum/connect vcf output)
    (hum/start-osc vco)

    (hum/connect-output output)

    (hum/note-on output vco 440)
    ;;(hum/note-off output)

    (swap! nodes #(concat % [vco vcf output]))))

(defn stop! []
  (swap! nodes (fn [nodes]
                 (doseq [node nodes]
                   (.disconnect node))
                 [])))




