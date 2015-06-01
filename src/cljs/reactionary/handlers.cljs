(ns reactionary.handlers
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require
   [reactionary.db :refer [initial-db]]
   [re-frame.core  :refer [register-handler path trim-v register-sub]]))


(enable-console-print!)

;; -- Init ---------------------------------------------------------------------

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


;; -- Helpers ------------------------------------------------------------------

(defn db-path->event-id
  [db-path prefix]
  (keyword (str prefix "-" (clojure.string/join "-" (map name db-path)))))

(defn insert-event-id
  [db-path]
  (db-path->event-id db-path "insert"))

(defn update-event-id
  [db-path]
  (db-path->event-id db-path "update"))

(defn delete-event-id
  [db-path]
  (db-path->event-id db-path "delete"))

(defn next-key
  "Return the next key value for a monotonically increasing integer map key."
  [m]
  ((fnil inc 0) (last (keys m))))


;; -- Middleware ---------------------------------------------------------------

(defn path-trim-middleware
  [db-path]
  (vector (path db-path) trim-v))

(def todo-middleware (path-trim-middleware :todo))


;; -- Update -------------------------------------------------------------------

(defn path-trim-based-update-handler  ; or passthrough-handler???
  [old [new]]
  new)

(defn wrapped  ; or closed-over-handler???
  [f]
  (fn
    [old [new]]
    (f old)))

(defn register-update-handler
  ([db-path]
   (let [event-id   (update-event-id db-path)
         middleware (path-trim-middleware db-path)
         handler     path-trim-based-update-handler]
     (register-handler event-id middleware handler)))
  ([db-path f]
   (let [event-id   (update-event-id db-path)
         middleware (path-trim-middleware db-path)
         handler    (wrapped f)]
     (register-handler event-id middleware handler))))


;; -- Register Handlers --------------------------------------------------------

(register-update-handler [:counter] inc)

(register-update-handler [:current-time :color])

(register-update-handler [:current-time :value] #(js/Date.))
