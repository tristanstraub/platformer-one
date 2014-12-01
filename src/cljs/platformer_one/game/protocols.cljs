(ns platformer-one.game.protocols)

(defprotocol IMaskable
  (apply-mask [component state selectors]))

(defprotocol ITransformable
  (apply-transforms [component state transforms]))

(defprotocol IOrderable
  (apply-order [component state selectors]))
