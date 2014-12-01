(ns platformer-one.components.svg
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require 
   [quile.component :as component]
   [platformer-one.ces.statemachine :as sm]
   [platformer-one.ces.component :refer [IComponent]]
   [platformer-one.ces.assets :as assets]
   [platformer-one.game.protocols :as p]
   [cljs.core.async :refer [<!]]
))

(defn center-of [ctx key]
  (let [bbox (.getBBox (.select ctx key))]
    [(.-cx bbox)
     (.-cy bbox)]))

(defn transform-matrix! 
  ([m transform]
     (transform-matrix! nil m transform))
  ([ctx m transform]
     (if-let [r (:rotate transform)]
       (if-let [around (:around transform)]
         (do
           (let [c (center-of ctx around)]
             (.translate m (c 0) (c 1))
             (.rotate m r)
             (.translate m (- (c 0)) (- (c 1)))))
         (.rotate m r)))

     (if-let [t (:translate transform)]
       (.translate m (t 0) (t 1)))
     (if-let [s (:scale transform)]
       (.scale m (s 0) (s 1)))))

(defn transforms->matrix 
  ([ctx matrix transforms]
     (doseq [transform transforms]
       (transform-matrix! ctx matrix transform))
     matrix)
  ([ctx transforms]
     (transforms->matrix ctx (js/Snap.Matrix.) transforms)))

(defn string->matrix [s]
  (js/Snap.Matrix. s))

(defn load-svg! [{:keys [assets]} state]
  (go 
   (reset! (:content state) (<! (assets/load-svg assets (:name state))))
   (swap! (:states state) #(sm/do-action % :loaded))))

(def ids (atom 0))

;; TODO this should be done via a translation using unique ids
;; of cloned elements
(defn apply-key-to-element! [element key]
  (cond (= \# (get key 0)) (.attr element #js {:id (subs key 1)})
        :else (print "don't know how to add key to element")))

(defn append-svg! [{:keys [assets canvas]} state]
  (let [content @(:content state)
        mask-keys (or (:mask state) [(str "#svg" (:id state))])
        mask (apply hash-set mask-keys)
        transforms (:transforms state)
        mask-snapshot (apply hash-set @(:mask-snapshot state))
        transforms-snapshot @(:transforms-snapshot state)
        root (swap! (:root state) #(or % (.g (:snap canvas))))]

    (.attr root #js {:id (str "#svg" (:id state))})

    ;; remove old parts
    (doseq [removed (clojure.set/difference mask-snapshot mask)]
      (.remove (.select root removed)))

    ;; add new parts
    (let [mask-selectors (clojure.set/difference mask mask-snapshot)]
      (loop [keys ;; order keys
             (filter #(get mask-selectors %) (:order state))
             previous nil]
        (when (< 0 (count keys))
          (let [key (first keys)
                part (.clone (.select content key))]
            (apply-key-to-element! part key)
            (if previous
              (.after previous part)
              (.append root part))          
            (recur (rest keys) part)))))

    ;; transform all parts
    (doseq [[key transforms] (group-by first transforms)]
      (let [transforms (map second transforms)]
        (cond (= :root key)
              (.transform root (transforms->matrix content transforms))
              :else
              (do
                (let [matrix (string->matrix (:localMatrix (.transform (.select content key))))]
                  (.transform (.select root key) (transforms->matrix content matrix transforms)))))))

    (reset! (:mask-snapshot state) mask)
    (reset! (:transforms-snapshot state) transforms)))

(defrecord Svg [assets canvas]
  ;; component/Lifecycle
  ;; (start [this]
  ;;   this)

  IComponent
  (create-state [this] {:id (swap! ids inc)
                        :mask nil
                        :transforms nil
                        ;; would be better to feed this to a stream 
                        ;; at the top, and then get rid of the atom
                        :content (atom nil)
                        :part-transforms (atom nil)
                        :root (atom nil)
                        :mask-snapshot (atom nil)
                        :transforms-snapshot (atom nil)
                        :states (atom (sm/state-machine
                                          {:init {:load :loading}
                                           :loading {:loaded :loaded}}
                                          :init))})

  (update-component-state [this world state]
    state)

  (synchronise! [this state]    
    (case (:state @(:states state))
      :init (do (swap! (:states state) #(sm/do-action % :load))
                (load-svg! this state))
      :loading nil
      :loaded (append-svg! this state)))

  p/IMaskable
  (apply-mask [this state selectors]
    (assoc state :mask selectors))

  p/ITransformable
  (apply-transforms [this state transforms]
    (assoc state :transforms transforms))

  p/IOrderable
  (apply-order [this state selectors]
    (assoc state :order selectors)))

(defn svg []
  (map->Svg. {}))
