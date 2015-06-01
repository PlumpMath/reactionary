(ns reactionary.handlers
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require
   [re-frame.core  :refer [register-handler path trim-v register-sub]]
   [reactionary.db :refer [initial-db]]))


;; -- init ---------------------------------------------------------------------

(register-handler
 :init
 (fn [_ _]
   initial-db))

(defn register-top-sub
  [k]
  (register-sub k (fn [db] (reaction (k @db)))))

(register-handler
 :init-subs
 (fn [db]
   (mapv register-top-sub (keys db))
   db))


;; -- set add del upd ----------------------------------------------------------

(register-handler
 :set-time
 [trim-v]
 (fn [db [value]]
   (assoc db :time value)))

(register-handler
 :set-time-color
 [(path [:time-color]) trim-v]
 (fn [old [new]] new))
