(ns platformer-one.game.robot)

(def descriptor
  {:svg

   "robot.svg"

   :order [
           :right-foot
           :left-foot
           :left-arm
           :right-arm
           :body
           :head
           :right-eye
           :left-eye
           :joint-left-arm
           :joint-right-arm
           :joint-body
           :joint-head
           :joint-left-foot
           :joint-right-foot
           ]

   :selectors

   {:root "#layer1"
    :joint-left-arm "#joint-left-arm"
    :joint-right-arm "#joint-right-arm"
    :joint-left-foot "#joint-left-foot"
    :joint-right-foot "#joint-right-foot"
    :joint-head "#joint-head"
    :joint-body "#joint-body"
    :right-foot "#right-foot"
    :left-foot "#left-foot"
    :left-arm "#left-arm"
    :right-arm "#right-arm"
    :body "#body"
    :head "#head"
    :right-eye "#right-eye"
    :left-eye "#left-eye"

    }

   :animations

   {:walking [{:pose :standing :duration 10}
              {:pose :raised-arms :duration 10}]}

   :poses

   {:joints {:parts [:joint-body :joint-left-arm :joint-right-arm :joint-left-foot :joint-right-foot :joint-head]}

    :standing {:inherit :joints
               :parts [:right-foot :head :left-eye :right-eye
                       :body :left-arm :right-arm
                       :left-foot]
               :transforms [ [ [:joint-body
                                :joint-left-arm :joint-right-arm
                                :joint-left-foot :joint-right-foot :joint-head
                                :right-foot :head :left-eye :right-eye
                                :body :left-arm :right-arm
                                :left-foot]
                               {}]]}

    :raised-arms {:inherit :standing
                  :parts [:left-foot :right-foot :left-eye :right-eye :left-arm :right-arm :head]
                  :transforms [[ [:right-arm] {:rotate -10 :around :joint-right-arm}]
                               [ [:left-arm] {:rotate 10 :around :joint-left-arm}]
                               [ [:left-foot] {:rotate -5 :around :joint-left-foot}]
                               [ [:right-foot] {:rotate 5 :around :joint-right-foot}]
                               [ [:head :left-eye :right-eye] {:rotate 5 :around :joint-head}]]}}})
