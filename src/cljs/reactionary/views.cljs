(ns reactionary.views
  (:require [re-com.core   :as rc]
            [re-frame.core :refer [dispatch subscribe]]))

(defonce time-updater
  (js/setInterval
   #(dispatch [:set-time (js/Date.)])
   1000))  ; every second (1000 ms)

(defn app-name-title []
  (let [app-name (subscribe [:app-name])]
    (fn []
      [rc/title
       :label (str "Welcome to " @app-name)
       :level :level1])))

(defn hello-title []
  (let [name (subscribe [:name])]
    (fn []
      [rc/title
       :label (str "Hello from " @name)
       :level :level2])))

(defn time-title []
  (let [time       (subscribe [:time])
        time-color (subscribe [:time-color])]
    (fn []
      (let [time-str (-> @time
                         .toTimeString
                         (clojure.string/split " ")
                         first)]
        [rc/title
         :label (str "According to my clock it is " time-str)
         :level :level3]))))

(defn app []
  (fn []
    [rc/v-box
     :height "100%"
     :children [[app-name-title]
                [hello-title]
                [time-title]]]))
