(ns reactionary.db)

(def initial-db
  {:app-name "Reactionary"
   :counter 0
   :current-time {:color "#ccc"
                  :value (js/Date.)}
   :todo {:items (sorted-map)
          :showing :all}})
