(ns todos.app
  (:require [reagent.core :as reagent]
  [re-frame.core :as rf]
  [clojure.string :as str])) 

(def value-a 1)
(defonce value-b 2)
(defn reload!  []
    (println "Code updated.")
      (println "Trying values:" value-a value-b))
(defn main!  []
    (println "App loaded!"))

(defn dispatch-timer-event
  "dispatch the timer"
  []
  (let [now (js/Date.)]
    (rf/dispatch [:timer now])))

(defonce do-timer (js/setInterval dispatch-timer-event 1000))

(rf/reg-event-db
 :initialize
 (fn [_ _]
   {:time (js/Date.)
    :time-color "#f88"}))

(rf/reg-event-db
 :timer
 (fn [db [_ new-time]]
   (assoc db :time new-time)))

(rf/reg-event-db
 :time-color-change
 (fn [db [_ new-color]]
   (assoc db :time-color-value new-color)))


(rf/reg-sub
 :time
 (fn [db _]
   (:time db)))

(rf/reg-sub
 :time-color
 (fn [db _]
   (:time-color db)))

(defn clock
  []
  [:div.example-clock
   {:style {:color @(rf/subscribe [:time-color])}}
   (-> @(rf/subscribe [:time])
       .toTimeString
       (str/split " ")
       first)])

(defn color-input
  []
  [:div.color-input
   "Time color: "
   [:input {:type "text"
            :value @(rf/subscribe [:time-color])
            :on-change #(rf/dispatch [:time-color-change (-> % .-target .-value)])}]])

(defn ui
  []
  [:div
   [:h1 "Hello world, it is"]
   [clock]
   [color-input]])

(defn mount-root
  []
  (let [root (js/document.getElementById "app")]
    (reagent/render [ui]
                    root)))
(defn ^:export run
  []
  (.log js/console "Running")
  (rf/dispatch-sync [:initialize])
  (mount-root))