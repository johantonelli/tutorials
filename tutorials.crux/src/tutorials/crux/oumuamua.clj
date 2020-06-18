(ns tutorials.crux.oumuamua
  (:require [crux.api :as crux]
            [tutorials.crux.earth :refer [manifest]]))

;; Start node
(def node
  (crux/start-node
   {:crux.node/topology '[crux.standalone/topology]
    :crux.kv/db-dir "data/db-dir"}))


;; tag::submit[]
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
;;=> #:crux.tx{:tx-id 0, :tx-time #inst "2020-06-18T16:11:03.410-00:00"}
;; end::submit[]

;; tag::full-query[]
(defn full-query
  [node]
  (crux/q
   (crux/db node)
   '{:find [id]
     :where [[e :crux.db/id id]]
     :full-results? true}))
;; end::full-query[]


;; tag::result[]
(full-query node)
#_#_
=> #{[{:crux.db/id :person/ilex,
    :full-name "Ilex Jefferson",
    :origin-planet "Venus",
    :identity-tag :IJ01222212454,
    :DOB #inst "2061-02-17T00:00:00.000-00:00"}]
  [{:crux.db/id :person/thadd,
    :full-name "Thad Christover",
    :origin-moon "Titan",
    :identity-tag :IJ01222212454,
    :DOB #inst "2101-01-01T00:00:00.000-00:00"}]
  [{:crux.db/id :person/kaarlang,
    :full-name "Kaarlang",
    :origin-planet "Mars",
    :identity-tag :KA01299242093,
    :DOB #inst "2040-11-23T00:00:00.000-00:00"}]
  [{:crux.db/id :person/johanna,
    :full-name "Johanna",
    :origin-planet "Earth",
    :identity-tag :JA012992129120,
    :DOB #inst "2090-12-07T00:00:00.000-00:00"}]}
;; end::result[]


;; tag::evict[]
(crux/submit-tx node [[:crux.tx/evict :person/kaarlang]])
;;=> #:crux.tx{:tx-id 3, :tx-time #inst "2020-06-18T16:13:03.276-00:00"}
;; end::evict[]

;; tag::post-query[]
(full-query node)
#_#_
=> #{[{:crux.db/id :person/ilex,
       :full-name "Ilex Jefferson",
       :origin-planet "Venus",
       :identity-tag :IJ01222212454,
       :DOB #inst "2061-02-17T00:00:00.000-00:00"}]
     [{:crux.db/id :person/thadd,
       :full-name "Thad Christover",
       :origin-moon "Titan",
       :identity-tag :IJ01222212454,
       :DOB #inst "2101-01-01T00:00:00.000-00:00"}]
     [{:crux.db/id :person/johanna,
       :full-name "Johanna",
       :origin-planet "Earth",
       :identity-tag :JA012992129120,
       :DOB #inst "2090-12-07T00:00:00.000-00:00"}]}
;; end::post-query[]

;; tag::history[]
(crux/entity-history
 (crux/db node)
 :person/kaarlang
 :desc
 {:with-docs? true})
#_#_
=> [{:crux.tx/tx-time #inst "2020-06-18T16:11:03.410-00:00",
     :crux.tx/tx-id 2,
     :crux.db/valid-time #inst "2020-06-18T16:11:03.410-00:00",
     :crux.db/content-hash #crux/id "c3ad3191fff06083fedf3640b625566c02033a6b",
     :crux.db/doc
     #:crux.db{:id #crux/id "efe634523d6867a3c6e4089074adf29b07b45f43",
               :evicted? true}}]
;; end::history[]

