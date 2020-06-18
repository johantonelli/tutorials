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

;; tag::simple[]
(crux/q (crux/db node)
        '{:find [element]
          :where [[element :type :element/metal]]})
;;=> #{[:commodity/Pu] [:commodity/Au]}
;; end::simple[]

;; tag::quote[]
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
;;=> true
;; end::quote[]


;; tag::metal[]
(crux/q (crux/db node)
        '{:find [name]
          :where [[e :type :element/metal]
                  [e :common-name name]]})
;;=> #{["Gold"] ["Plutonium"]}
;; end::metal[]

;; tag::more[]
(crux/q (crux/db node)
        '{:find [name rho]
          :where [[e :density rho]
                  [e :common-name name]]})
;;=> #{["Nitrogen" 1.2506] ["Carbon" 2.267] ["Methane" 0.717] ["Borax" 1.73] ["Gold" 19.3] ["Plutonium" 19.816]}
;; end::more[]

;; tag::args[]
(crux/q (crux/db node)
        {:find '[name]
         :where '[[e :type t]
                  [e :common-name name]]
         :args [{'t :element/metal}]})
;;=> #{["Gold"] ["Plutonium"]}
;; end::args[]

;; tag::daily[]
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
;;=> #{["Gold"] ["Plutonium"]}

(filter-appearance "white solid")
;;=> #{["Borax" "Sodium tetraborate decahydrate"]}
;; end::daily[]

;; tag::manifest[]
(crux/submit-tx
 node [[:crux.tx/put (assoc manifest
                            :badges ["SETUP" "PUT" "DATALOG-QUERIES"])]])
;;=> #:crux.tx{:tx-id 1, :tx-time #inst "2020-06-18T14:31:46.148-00:00"}
;; end::manifest[]
