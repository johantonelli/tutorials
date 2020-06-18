(ns tutorials.crux.mercury
  (:require [crux.api :as crux]
            [tutorials.crux.earth :refer [manifest]]
            [tutorials.crux.pluto :refer [easy-ingest]]))

;; Start node
(def node
  (crux/start-node
   {:crux.node/topology '[crux.standalone/topology]
    :crux.kv/db-dir "data/db-dir"}))

;; define data
(def data
  [{:crux.db/id :commodity/Pu
    :common-name "Plutonium"
    :type :element/metal
    :density 19.816
    :radioactive true}

   {:crux.db/id :commodity/N
    :common-name "Nitrogen"
    :type :element/gas
    :density 1.2506
    :radioactive false}

   {:crux.db/id :commodity/CH4
    :common-name "Methane"
    :type :molecule/gas
    :density 0.717
    :radioactive false}

   {:crux.db/id :commodity/Au
    :common-name "Gold"
    :type :element/metal
    :density 19.300
    :radioactive false}

   {:crux.db/id :commodity/C
    :common-name "Carbon"
    :type :element/non-metal
    :density 2.267
    :radioactive false}

   {:crux.db/id :commodity/borax
    :common-name "Borax"
    :IUPAC-name "Sodium tetraborate decahydrate"
    :other-names ["Borax decahydrate" "sodium borate"
                  "sodium tetraborate" "disodium tetraborate"]
    :type :mineral/solid
    :appearance "white solid"
    :density 1.73
    :radioactive false}])

;; ingest data
(easy-ingest node data)

;; Basic query
(crux/q (crux/db node)
        '{:find [element]
          :where [[element :type :element/metal]]})

;; Quoting
(=
 (crux/q (crux/db node)
         '{:find [element]
           :where [[element :type :element/metal]]})

 (crux/q (crux/db node)
         {:find '[element]
          :where '[[element :type :element/metal]]})

 (crux/q (crux/db node)
         (quote
          {:find [element]
           :where [[element :type :element/metal]]})))

;; Metal elements
(crux/q (crux/db node)
        '{:find [name]
          :where [[e :type :element/metal]
                  [e :common-name name]]})

;; More info
(crux/q (crux/db node)
        '{:find [name rho]
          :where [[e :density rho]
                  [e :common-name name]]})

;; Args
(crux/q (crux/db node)
        {:find '[name]
         :where '[[e :type t]
                  [e :common-name name]]
         :args [{'t :element/metal}]})

;; Daily fns
(defn filter-type
  [type]
  (crux/q (crux/db node)
        {:find '[name]
         :where '[[e :type t]
                  [e :common-name name]]
         :args [{'t type}]}))

(defn filter-appearance
  [description]
  (crux/q (crux/db node)
        {:find '[name IUPAC]
         :where '[[e :common-name name]
                  [e :IUPAC-name IUPAC]
                  [e :appearance appearance]]
         :args [{'appearance description}]}))

(filter-type :element/metal)

(filter-appearance "white solid")

;; Update manifest
(crux/submit-tx
 node [[:crux.tx/put (assoc manifest
                            :badges ["SETUP" "PUT" "DATALOG-QUERIES"])]])
