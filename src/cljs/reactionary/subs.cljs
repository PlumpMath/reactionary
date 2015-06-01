(ns reactionary.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require        [re-frame.core :refer [register-sub]]))


;; The :init-subs handler automatically registers subscriptions to all
;; the top level keys in the database.  See handlers.cljs for details.


(register-sub
 :current-time-color
 (fn [db]
   (reaction (get-in @db [:current-time :color]))))

(register-sub
 :current-time-value
 (fn [db]
   (reaction (get-in @db [:current-time :value]))))
