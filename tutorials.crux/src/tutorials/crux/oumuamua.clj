(ns tutorials.crux.oumuamua
  (:require [crux.api :as crux]
            [tutorials.crux.earth :refer [manifest]]))

;; Start node
(def node
  (crux/start-node
   {:crux.node/topology '[crux.standalone/topology]
    :crux.kv/db-dir "data/db-dir"}))

;; Submit some stuff
(crux/submit-tx node
                [[:crux.tx/put
                  {:crux.db/id :person/kaarlang
                   :full-name "Kaarlang"
                   :origin-planet "Mars"
                   :identity-tag :KA01299242093
                   :DOB #inst "2040-11-23"}]

                 [:crux.tx/put
                  {:crux.db/id :person/ilex
                   :full-name "Ilex Jefferson"
                   :origin-planet "Venus"
                   :identity-tag :IJ01222212454
                   :DOB #inst "2061-02-17"}]

                 [:crux.tx/put
                  {:crux.db/id :person/thadd
                   :full-name "Thad Christover"
                   :origin-moon "Titan"
                   :identity-tag :IJ01222212454
                   :DOB #inst "2101-01-01"}]

                 [:crux.tx/put
                  {:crux.db/id :person/johanna
                   :full-name "Johanna"
                   :origin-planet "Earth"
                   :identity-tag :JA012992129120
                   :DOB #inst "2090-12-07"}]])

(defn full-query
  [node]
  (crux/q
   (crux/db node)
   '{:find [id]
     :where [[e :crux.db/id id]]
     :full-results? true}))

(full-query node)

;; Evict Kaarlang
(crux/submit-tx node [[:crux.tx/evict :person/kaarlang]])

;; check history
(crux/entity-history
 (crux/db node)
 :person/clients
 :desc
 {:with-docs? true})
