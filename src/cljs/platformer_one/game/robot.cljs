(ns platformer-one.game.robot)

(def descriptor
  {:svg 
  
   "robot.svg"

   :order [:right-foot      
           :left-foot       
           :left-arm        
           :right-arm       
           :body            
           :head            
           :right-eye       
           :left-eye        
           :joint-left-arm  
           :joint-right-arm]

   :selectors 
   
   {:right-foot "#right-foot"
    :left-foot "#left-foot"
    :left-arm "#left-arm"
    :right-arm "#right-arm"
    :body "#body"
    :head "#head"
    :right-eye "#right-eye"
    :left-eye "#left-eye"
    :joint-left-arm "#joint-left-arm"
    :joint-right-arm "#joint-right-arm"
    :joint-left-foot "#joint-left-foot"
    :joint-right-foot "#joint-right-foot"
    :joint-head "#joint-head"}

   :animations

   {:walking [{:pose :standing :duration 10}
              {:pose :raised-arms :duration 10}]}

   :poses

   {:standing {
               :parts [:right-foot :head :left-eye :right-eye 
                       :body :left-arm :right-arm 
                       :left-foot ]
               :transforms [ [ [:right-foot :head :left-eye
                                :right-eye :body :left-arm :right-arm
                                :left-foot] {:scale [.5 .5]}]]}

    :raised-arms {:inherit :standing
                  :parts [:joint-left-arm :joint-right-arm :left-arm :right-arm]
                  :transforms [[ [:joint-right-arm :joint-left-arm] {:scale [.5 .5]}] 
                               [ [:right-arm] {:rotate -10 :around :joint-right-arm}]
                               [ [:left-arm] {:rotate 10 :around :joint-left-arm}]
                               [ [:left-foot] {:rotate -5 :around :joint-left-foot}]
                               [ [:right-foot] {:rotate 5 :around :joint-right-foot}]
                               [ [:head :left-eye :right-eye] {:rotate 5 :around :joint-head}]]}}})
