(ns tutorials.crux.core
  (:require
    [yada.yada :as yada]
    [integrant.core :as ig]
    [crux.api :as crux]
    [tutorials.crux.earth :as earth]))

(defn string-resource
  [x]
  (yada/as-resource x))

(defmethod ig/init-key ::string
  [_ x]
  (string-resource x))

(defn query-all
  [node]
  (let [ids (crux/q (crux/db node)
                    '{:find [e]
                      :where [[e :crux.db/id]]})]
    (mapv #(crux/entity (crux/db node) (first %)) ids)))

(defn pprint-data
  [data]
  (with-out-str (clojure.pprint/pprint data)))

(defmethod ig/init-key ::earth
  [_ _]
  (some-> earth/node
          query-all
          pprint-data
          string-resource))
