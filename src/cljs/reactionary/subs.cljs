(ns reactionary.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require        [re-frame.core :refer [register-sub]]))

;; (defn register-top-sub
;;   [path]
;;   (register-sub path (fn [db] (reaction (path @db)))))

;; (mapv register-top-sub
;;       [:app-name
;;        :name
;;        :time
;;        :time-color])

;; (register :app-name)

;; (register :name)

;; (register :time)

;; (register :time-color)

;; (register-sub
;;  :app-name
;;  (fn [db]
;;    (reaction (:app-name @db))))

;; (register-sub
;;  :name
;;  (fn [db]
;;    (reaction (:name @db))))

;; (register-sub
;;  :time
;;  (fn [db]
;;    (reaction (:time @db))))

;; (register-sub
;;  :time-color
;;  (fn [db]
;;    (reaction (:time-color @db))))
