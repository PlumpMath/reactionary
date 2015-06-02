(ns reactionary.handlers
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require
   [reactionary.db :refer [initial-db]]
   [re-frame.core  :refer [dispatch path register-handler register-sub trim-v]]))


(enable-console-print!)

;; -- Init ---------------------------------------------------------------------

(register-handler
 :init-db
 (fn [_ _]
   initial-db))


(defn- register-top-sub
  [k]
  (register-sub k (fn [db] (reaction (k @db)))))

(register-handler  ; convenient when the db is shallow
 :init-subs
 (fn [db]
   (mapv register-top-sub (keys db))
   db))


(register-handler
 :init-todo-items
 (fn [db]
   (dispatch [:insert-into-todo-items "Do something"])
   (dispatch [:insert-into-todo-items "Take a break"])
   (dispatch [:insert-into-todo-items "Do something else"])
   (dispatch [:insert-into-todo-items "Take another break"])
   (dispatch [:insert-into-todo-items "And yet another break"])
   (dispatch [:delete-from-todo-items 5])
   db))


;; -- Helpers ------------------------------------------------------------------

(defn- db-path->event-id
  [prefix]
  (fn [db-path]
    (keyword (str prefix "-" (clojure.string/join "-" (map name db-path))))))

(def make-insert-event-id (db-path->event-id "insert"))

(def make-update-event-id (db-path->event-id "update"))

(def make-delete-event-id (db-path->event-id "delete"))


(defn next-key-value
  "Return the next value for a monotonically increasing integer map key."
  [m]
  ((fnil inc 0) (last (keys m))))


;; -- Middleware ---------------------------------------------------------------

(defn make-path-trim-middleware
  [db-path]
  (vector (path db-path) trim-v))

(def todo-middleware (make-path-trim-middleware [:todo]))

(def todo-items-middleware (make-path-trim-middleware [:todo :items]))


;; -- Update -------------------------------------------------------------------

(defn make-passthrough-update-handler
  []
  (fn
    [old [new]]
    new))

(defn make-callback-update-handler  ; or closed-over-handler???
  [f]
  (fn
    [old [new]]
    (f old)))

(defn register-update-handler
  ([db-path]
   (let [event-id   (make-update-event-id db-path)
         middleware (make-path-trim-middleware db-path)
         handler    (make-passthrough-update-handler)]
     (register-handler event-id middleware handler)))
  ([db-path f]
   (let [event-id   (make-update-event-id db-path)
         middleware (make-path-trim-middleware db-path)
         handler    (make-callback-update-handler f)]
     (register-handler event-id middleware handler))))


;; -- Register Handlers --------------------------------------------------------

(register-update-handler [:counter] inc)

(register-update-handler [:current-time :color])

(register-update-handler [:current-time :value] #(js/Date.))

(register-handler
 :insert-into-todo-items
 todo-items-middleware
 (fn [todo-items [text]]
   (let [id (next-key-value todo-items)]
     (assoc todo-items id {:id id :title text :done false}))))

(register-handler
 :delete-from-todo-items
 todo-items-middleware
 (fn [todo-items [id]]
   (dissoc todo-items id)))

;(register-update-handler [:todo :items some-id :done] not)
