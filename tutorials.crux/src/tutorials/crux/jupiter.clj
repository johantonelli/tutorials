(ns tutorials.crux.jupiter
  (:require [crux.api :as crux]
            [tutorials.crux.earth :refer [manifest]]))

;; Start node
(def node
  (crux/start-node
   {:crux.node/topology '[crux.standalone/topology]
    :crux.kv/db-dir "data/db-dir"}))


(crux/submit-tx
 node [[:crux.tx/put {:crux.db/id :kaarlang/clients
                      :clients [:encompass-trade]}
        #inst "2110-01-01T09"
        #inst "2111-01-01T09"]

       [:crux.tx/put {:crux.db/id :kaarlang/clients
                      :clients [:encompass-trade :blue-energy]}
        #inst "2111-01-01T09"
        #inst "2113-01-01T09"]

       [:crux.tx/put {:crux.db/id :kaarlang/clients
                      :clients [:blue-energy]}
        #inst "2113-01-01T09"
        #inst "2114-01-01T09"]

       [:crux.tx/put {:crux.db/id :kaarlang/clients
                      :clients [:blue-energy :gold-harmony :tombaugh-resources]}
        #inst "2114-01-01T09"
        #inst "2115-01-01T09"]])

;; Check history
(crux/entity-history
 (crux/db node #inst "2116-01-01T09")
 :kaarlang/clients
 :desc
 {:with-docs? true})

;; Delete
(crux/submit-tx
 node [[:crux.tx/delete :kaarlang/clients #inst "2110-01-01" #inst "2116-01-01"]])

;; Check again
(crux/entity-history
 (crux/db node #inst "2116-01-01T09")
 :kaarlang/clients
 :desc
 {:with-docs? true})
