(ns platformer-one.core
  (:require 
   [quile.component :as component]
   [platformer-one.app :as app]
   [platformer-one.ces.keyboard :as kbd]
   [platformer-one.game.system :refer [game-system]]
   [platformer-one.ces.statemachine :as sm])
  
  (:use [jayq.core :only [$ css html]]))

(enable-console-print!)

(defn stop-system! []
  (component/stop @system)
  (reset! system nil))

(defn start-system! []
  (swap! system #(component/start (game-system game-state))))

(defn on-ready []
  (defonce app-state (atom {:text "Hello!"}))
  (defonce game-state (atom {}))
  (defonce system (atom nil))

  (kbd/init)
  (swap! game-state (fn [state] (assoc state :keys-down #{} :pose :wave)))

  (add-watch kbd/keys-down nil (fn [key reference old-keys-down keys-down]
                                 (swap! game-state (fn [state] (assoc state :keys-down keys-down)))))

  (app/start! app-state)
  (start-system!))

(defn main []
  (defonce loader (atom (sm/state-machine
                            {:init {:start :started}
                             :started nil}
                            :init)))

  ;; called from env/[prod/dev]/platformer-one/main
  (swap! loader (fn [{:keys [state] :as loader}]
                  (case state
                    :init (do ($ on-ready)
                              (sm/do-action loader :start))
                    :started (do (stop-system!)
                                 (start-system!)
                               loader)))))

