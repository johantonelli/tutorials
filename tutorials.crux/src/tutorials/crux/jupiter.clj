(ns tutorials.crux.jupiter
  (:require [crux.api :as crux]
            [tutorials.crux.earth :refer [manifest]]))

;; Start node
(def node
  (crux/start-node
   {:crux.node/topology '[crux.standalone/topology]
    :crux.kv/db-dir "data/db-dir"}))

;; tag::submit[]
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
;;=> #:crux.tx{:tx-id 0, :tx-time #inst "2020-06-18T15:55:00.894-00:00"}
;; end::submit[]


;; tag::history[]
(crux/entity-history
 (crux/db node #inst "2116-01-01T09")
 :kaarlang/clients
 :desc
 {:with-docs? true})
#_#_
=> [{:crux.tx/tx-time #inst "2020-06-18T15:57:28.900-00:00",
    :crux.tx/tx-id 1,
    :crux.db/valid-time #inst "2114-01-01T09:00:00.000-00:00",
    :crux.db/content-hash #crux/id "d4bca6c78409d9d40ee42319a8aec32bffad9030",
    :crux.db/doc
    {:crux.db/id :kaarlang/clients,
     :clients [:blue-energy :gold-harmony :tombaugh-resources]}}
   {:crux.tx/tx-time #inst "2020-06-18T15:57:28.900-00:00",
    :crux.tx/tx-id 1,
    :crux.db/valid-time #inst "2113-01-01T09:00:00.000-00:00",
    :crux.db/content-hash #crux/id "000e5b775b55d06f0bddc77d736184284aa1e4e9",
    :crux.db/doc {:crux.db/id :kaarlang/clients, :clients [:blue-energy]}}
   {:crux.tx/tx-time #inst "2020-06-18T15:57:28.900-00:00",
    :crux.tx/tx-id 1,
    :crux.db/valid-time #inst "2111-01-01T09:00:00.000-00:00",
    :crux.db/content-hash #crux/id "cd71551fe21219db59067ce7483370fdebaae8b0",
    :crux.db/doc
    {:crux.db/id :kaarlang/clients, :clients [:encompass-trade :blue-energy]}}
   {:crux.tx/tx-time #inst "2020-06-18T15:57:28.900-00:00",
    :crux.tx/tx-id 1,
    :crux.db/valid-time #inst "2110-01-01T09:00:00.000-00:00",
    :crux.db/content-hash #crux/id "5ec42ea653288e01e1a9d7d2068b4658416177e0",
    :crux.db/doc {:crux.db/id :kaarlang/clients, :clients [:encompass-trade]}}]
;; end::history[]


;; tag::delete[]
(crux/submit-tx
 node [[:crux.tx/delete :kaarlang/clients #inst "2110-01-01" #inst "2116-01-01"]])
;;=> #:crux.tx{:tx-id 1, :tx-time #inst "2020-06-18T15:59:38.323-00:00"}
;; end::delete[]


;; tag::history-again[]
(crux/entity-history
 (crux/db node #inst "2116-01-01T09")
 :kaarlang/clients
 :desc
 {:with-docs? true})
#_#_
=> [{:crux.tx/tx-time #inst "2020-06-18T15:59:38.323-00:00",
    :crux.tx/tx-id 2,
    :crux.db/valid-time #inst "2115-01-01T09:00:00.000-00:00",
    :crux.db/content-hash #crux/id "0000000000000000000000000000000000000000",
    :crux.db/doc nil}
   {:crux.tx/tx-time #inst "2020-06-18T15:59:38.323-00:00",
    :crux.tx/tx-id 2,
    :crux.db/valid-time #inst "2114-01-01T09:00:00.000-00:00",
    :crux.db/content-hash #crux/id "0000000000000000000000000000000000000000",
    :crux.db/doc nil}
   {:crux.tx/tx-time #inst "2020-06-18T15:59:38.323-00:00",
    :crux.tx/tx-id 2,
    :crux.db/valid-time #inst "2113-01-01T09:00:00.000-00:00",
    :crux.db/content-hash #crux/id "0000000000000000000000000000000000000000",
    :crux.db/doc nil}
   {:crux.tx/tx-time #inst "2020-06-18T15:59:38.323-00:00",
    :crux.tx/tx-id 2,
    :crux.db/valid-time #inst "2111-01-01T09:00:00.000-00:00",
    :crux.db/content-hash #crux/id "0000000000000000000000000000000000000000",
    :crux.db/doc nil}
   {:crux.tx/tx-time #inst "2020-06-18T15:59:38.323-00:00",
    :crux.tx/tx-id 2,
    :crux.db/valid-time #inst "2110-01-01T09:00:00.000-00:00",
    :crux.db/content-hash #crux/id "0000000000000000000000000000000000000000",
    :crux.db/doc nil}
   {:crux.tx/tx-time #inst "2020-06-18T15:59:38.323-00:00",
    :crux.tx/tx-id 2,
    :crux.db/valid-time #inst "2110-01-01T00:00:00.000-00:00",
    :crux.db/content-hash #crux/id "0000000000000000000000000000000000000000",
    :crux.db/doc nil}]
;; end::history-again[]
