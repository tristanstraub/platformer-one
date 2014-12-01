(ns platformer-one.game.pose
  (:require [platformer-one.game.protocols :as p]))

(defn get-selectors [pose]
  (let [{:keys [key descriptor]} pose
        {:keys [selectors poses]} descriptor]
    (let [{:keys [inherit parts]} (get poses key)
          inherited-selectors (if inherit
                                (get-selectors {:key inherit :descriptor descriptor}))]
      (apply hash-set (concat inherited-selectors (map selectors parts))))))

(defn get-selector-order [{:keys [descriptor]}]
  (let [{:keys [order selectors]} descriptor]
    (map #(get selectors %) order)))

(defn flatten-transforms [selectors transforms]
  (reduce concat (map (fn [[transform-selectors transform]]
                        (map (fn [selector]
                               [(get selectors selector) (if-let [around (:around transform)]
                                                           (assoc transform :around (get selectors around))
                                                           transform)])
                             transform-selectors))
                      transforms)))

(defn get-transforms [pose]
  (let [{:keys [key descriptor]} pose
        {:keys [selectors poses]} descriptor]
    (let [{:keys [inherit transforms]} (get poses key)
          inherited-transforms
          (if inherit
            (get-transforms {:key inherit :descriptor descriptor}))
          mapped-transforms (flatten-transforms selectors transforms)]
      (concat inherited-transforms mapped-transforms))))

