(ns platformer-one.ces.component)

(defprotocol IComponent
  (create-state [component])
  (update-component-state [component world state])
  (synchronise! [component state]))

(defn component-entry? [[key component]]
  (satisfies? IComponent component))

(defn update-component [world entity key component]
  (update-in entity [key] #(update-component-state component world %)))

(defn update-components [world entities components]
  (reduce (fn [entities [key component]]
            (map #(update-component world % key component) entities))
          entities
          (filter component-entry? components)))

(defn synchronise-components! [entities components]
  (doseq [entity entities]
    (doseq [[key component] (filter component-entry? components)]
      (let [state (key entity)]
        (synchronise! component state)))))
