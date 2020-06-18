(ns tutorials.crux.earth
  (:require [crux.api :as crux]))

;; Start node
(def node
  (crux/start-node
   {:crux.node/topology '[crux.standalone/topology]
    :crux.kv/db-dir "data/db-dir"}))

;; Define first manifest
(def manifest
  {:crux.db/id :manifest
   :pilot-name "Johanna"
   :id/rocket "SB002-sol"
   :id/employee "22910x2"
   :badges "SETUP"
   :cargo ["stereo" "gold fish" "slippers" "secret note"]})

;; Submit it
(crux/submit-tx node [[:crux.tx/put manifest]])

;; Check
(crux/entity-history (crux/db node) :manifest :asc)
