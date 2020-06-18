(ns tutorials.crux.saturn
  (:require [crux.api :as crux]
            [tutorials.crux.earth :refer [manifest]]
            [tutorials.crux.pluto :refer [easy-ingest]]))

;; Start node
(def node
  (crux/start-node
   {:crux.node/topology '[crux.standalone/topology]
    :crux.kv/db-dir "data/db-dir"}))

;; Define data
(def data [{:crux.db/id :gold-harmony
            :company-name "Gold Harmony"
            :seller? true
            :buyer? false
            :units/Au 10211
            :credits 51}

           {:crux.db/id :tombaugh-resources
            :company-name "Tombaugh Resources Ltd."
            :seller? true
            :buyer? false
            :units/Pu 50
            :units/N 3
            :units/CH4 92
            :credits 51}

           {:crux.db/id :encompass-trade
            :company-name "Encompass Trade"
            :seller? true
            :buyer? true
            :units/Au 10
            :units/Pu 5
            :units/CH4 211
            :credits 1002}

           {:crux.db/id :blue-energy
            :seller? false
            :buyer? true
            :company-name "Blue Energy"
            :credits 1000}])

;; Ingest
(easy-ingest node data)

;; Helper fns
(defn stock-check
  [company-id item]
  {:result (crux/q (crux/db node)
                   {:find '[name funds stock]
                    :where ['[e :company-name name]
                            '[e :credits funds]
                            ['e item 'stock]]
                    :args [{'e company-id}]})
   :item item})

(defn format-stock-check
  [{:keys [result item] :as stock-check}]
  (for [[name funds commod] result]
    (str "Name: " name ", Funds: " funds ", " item " " commod)))

;; use match

(crux/submit-tx
 node
 [[:crux.tx/match
   :blue-energy
   {:crux.db/id :blue-energy
    :seller? false
    :buyer? true
    :company-name "Blue Energy"
    :credits 1000}]
  [:crux.tx/put
   {:crux.db/id :blue-energy
    :seller? false
    :buyer? true
    :company-name "Blue Energy"
    :credits 900
    :units/CH4 10}]

  [:crux.tx/match
   :tombaugh-resources
   {:crux.db/id :tombaugh-resources
    :company-name "Tombaugh Resources Ltd."
    :seller? true
    :buyer? false
    :units/Pu 50
    :units/N 3
    :units/CH4 92
    :credits 51}]
  [:crux.tx/put
   {:crux.db/id :tombaugh-resources
    :company-name "Tombaugh Resources Ltd."
    :seller? true
    :buyer? false
    :units/Pu 50
    :units/N 3
    :units/CH4 82
    :credits 151}]])

;; check
(format-stock-check (stock-check :tombaugh-resources :units/CH4))

(format-stock-check (stock-check :blue-energy :units/CH4))

;; Another match
(crux/submit-tx
 node
 [[:crux.tx/match
   :gold-harmony
   {:crux.db/id :gold-harmony
    :company-name "Gold Harmony"
    :seller? true
    :buyer? false
    :units/Au 10211
    :credits 51}]
  [:crux.tx/put
   {:crux.db/id :gold-harmony
    :company-name "Gold Harmony"
    :seller? true
    :buyer? false
    :units/Au 211
    :credits 51}]

  [:crux.tx/match
   :encompass-trade
   {:crux.db/id :encompass-trade
    :company-name "Encompass Trade"
    :seller? true
    :buyer? true
    :units/Au 10
    :units/Pu 5
    :units/CH4 211
    :credits 100002}]
  [:crux.tx/put
   {:crux.db/id :encompass-trade
    :company-name "Encompass Trade"
    :seller? true
    :buyer? true
    :units/Au 10010
    :units/Pu 5
    :units/CH4 211
    :credits 1002}]])

;; Another check
(format-stock-check (stock-check :gold-harmony :units/Au))

(format-stock-check (stock-check :encompass-trade :units/Au))

;; Update manifest
(crux/submit-tx
 node [[:crux.tx/put
        (assoc manifest
               :badges ["SETUP" "PUT" "DATALOG-QUERIES" "BITEMP" "MATCH"])]])

;; Check for note
(crux/q (crux/db node)
        {:find '[belongings]
         :where '[[e :cargo belongings]]
         :args [{'belongings "secret note"}]})
