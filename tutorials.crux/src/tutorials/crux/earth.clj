;; tag::require[]
(ns tutorials.crux.earth
  (:require [crux.api :as crux]))
;; end::require[]

;; tag::node[]
(def node
  (crux/start-node
   {:crux.node/topology '[crux.standalone/topology]
    :crux.kv/db-dir "data/db-dir"}))
;; end::node[]

;; tag::manifest[]
(def manifest
  {:crux.db/id :manifest
   :pilot-name "Johanna"
   :id/rocket "SB002-sol"
   :id/employee "22910x2"
   :badges "SETUP"
   :cargo ["stereo" "gold fish" "slippers" "secret note"]})
;; end::manifest[]

;; tag::submit[]
(crux/submit-tx node [[:crux.tx/put manifest]])
;;=>#:crux.tx{:tx-id 0, :tx-time #inst "2020-06-18T13:54:08.375-00:00"}
;; end::submit[]

;; tag::check[]
(crux/entity-history (crux/db node) :manifest :asc)
;;=> {:crux.tx/tx-time #inst "2020-06-18T13:54:08.375-00:00",
;;    :crux.tx/tx-id 0,
;;    :crux.db/valid-time #inst "2020-06-18T13:54:08.375-00:00",
;;    :crux.db/content-hash #crux/id "0ab888b62775eea2eb2fffe10c9f6bfbf661a792"}
;; end::check[]
