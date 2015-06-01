(ns reactionary.views
  (:require [re-com.core   :as rc]
            [re-frame.core :refer [dispatch subscribe]]))


;; -- Timers -------------------------------------------------------------------


(defonce update-counter
  (js/setInterval
   #(dispatch [:update-counter])
   (* 4 1000)))  ; every so often

(defonce update-current-time-value
  (js/setInterval
   #(dispatch [:update-current-time-value])
   1000))  ; every second (1000 ms)


;; -- Content ------------------------------------------------------------------

(defn app-name []
  (let [app-name (subscribe [:app-name])]
    (fn []
      [rc/title
       :label (str "Welcome to " @app-name)
       :level :level1])))

(defn counter []
  (let [counter (subscribe [:counter])]
    (fn []
      [rc/p "Every so often this value will change --> " (str @counter)])))

(defn current-time []
  (let [current-time-value (subscribe [:current-time-value])
        current-time-color (subscribe [:current-time-color])]
    (fn []
      (let [time-str (-> @current-time-value
                         .toTimeString
                         (clojure.string/split " ")
                         first)]
        [rc/p "According to my clock it is " time-str ".  "
         "Time to do some todos."]))))

(defn todo-items []
  [rc/p "Nothing to do yet..."])

;; -- Main App -----------------------------------------------------------------

(defn app []
  (fn []
    [rc/v-box
     :height "100%"
     :children [[app-name]
                [counter]
                [current-time]
                [todo-items]]]))
