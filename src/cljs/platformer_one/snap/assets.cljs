(ns platformer-one.snap.assets
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [platformer-one.ces.assets :as assets]
            [cljs.core.async :refer [put! chan]]))

(defn snap-asset-manager []
  (reify
    assets/IAssetManager
    (load-svg [this name]
      (let [result (chan)]
        (go (. js/Snap 
               (load name
                     (fn [fragment] 
                       (put! result fragment)))))
        result))))
