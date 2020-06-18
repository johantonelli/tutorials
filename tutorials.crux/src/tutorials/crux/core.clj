(ns tutorials.crux.core
  (:require
    [yada.yada :as yada]
    [integrant.core :as ig]
    [crux.api :as crux]
    [tutorials.crux.earth :as earth]
    [tutorials.crux.pluto :as pluto]
    [tutorials.crux.mercury :as mercury]
    [tutorials.crux.neptune :as neptune]
    [tutorials.crux.saturn :as saturn]
    [tutorials.crux.jupiter :as jupiter]
    [tutorials.crux.oumuamua :as oumuamua]))

(defn string-resource
  [x]
  (yada/as-resource x))

(defmethod ig/init-key ::string
  [_ x]
  (string-resource x))

(defn query-all
  [node]
  (crux/q
   (crux/db node)
   '{:find [id]
     :where [[e :crux.db/id id]]
     :full-results? true}))

(defn pprint-data
  [data]
  (with-out-str (clojure.pprint/pprint data)))

(defmethod ig/init-key ::earth
  [_ _]
  (some-> earth/node
          query-all
          pprint-data
          string-resource))

(defmethod ig/init-key ::pluto
  [_ _]
  (some-> pluto/node
          query-all
          pprint-data
          string-resource))

(defmethod ig/init-key ::mercury
  [_ _]
  (some-> mercury/node
          query-all
          pprint-data
          string-resource))

(defmethod ig/init-key ::neptune
  [_ _]
  (some-> neptune/node
          query-all
          pprint-data
          string-resource))

(defmethod ig/init-key ::saturn
  [_ _]
  (some-> saturn/node
          query-all
          pprint-data
          string-resource))

(defmethod ig/init-key ::jupiter
  [_ _]
  (some-> jupiter/node
          query-all
          pprint-data
          string-resource))

(defmethod ig/init-key ::oumuamua
  [_ _]
  (some-> oumuamua/node
          query-all
          pprint-data
          string-resource))
