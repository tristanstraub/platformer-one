(ns platformer-one.ces.assets)

(defprotocol IAssetManager
  (load-svg [this name]))
