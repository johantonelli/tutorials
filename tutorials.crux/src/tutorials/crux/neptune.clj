(ns tutorials.crux.neptune
  (:require [crux.api :as crux]
            [tutorials.crux.earth :refer [manifest]]))

;; Start node
(def node
  (crux/start-node
   {:crux.node/topology '[crux.standalone/topology]
    :crux.kv/db-dir "data/db-dir"}))

;; tag::valid-start[]
(crux/submit-tx
 node
 [[:crux.tx/put
   {:crux.db/id :consumer/RJ29sUU
    :consumer-id :RJ29sUU
    :first-name "Jay"
    :last-name "Rose"
    :cover? true
    :cover-type :Full}
   #inst "2114-12-03"]])
;;=> #:crux.tx{:tx-id 0, :tx-time #inst "2020-06-18T14:38:29.240-00:00"}
;; end::valid-start[]

;; tag::valid-all[]
(crux/submit-tx
 node
 [[:crux.tx/put ;; <1>
   {:crux.db/id :consumer/RJ29sUU
    :consumer-id :RJ29sUU
    :first-name "Jay"
    :last-name "Rose"
    :cover? true
    :cover-type :Full}
   #inst "2113-12-03" ;; Valid time start
   #inst "2114-12-03"] ;; Valid time end

  [:crux.tx/put ;; <2>
   {:crux.db/id :consumer/RJ29sUU
    :consumer-id :RJ29sUU
    :first-name "Jay"
    :last-name "Rose"
    :cover? true
    :cover-type :Full}
   #inst "2112-12-03"
   #inst "2113-12-03"]

  [:crux.tx/put ;; <3>
   {:crux.db/id :consumer/RJ29sUU
    :consumer-id :RJ29sUU
    :first-name "Jay"
    :last-name "Rose"
    :cover? false}
   #inst "2112-06-03"
   #inst "2112-12-02"]

  [:crux.tx/put ;; <4>
   {:crux.db/id :consumer/RJ29sUU
    :consumer-id :RJ29sUU
    :first-name "Jay"
    :last-name "Rose"
    :cover? true
    :cover-type :Promotional}
   #inst "2111-06-03"
   #inst "2112-06-03"]])
;;=> #:crux.tx{:tx-id 1, :tx-time #inst "2020-06-18T14:39:11.189-00:00"}
;; end::valid-all[]

;; tag::query-2115[]
(crux/q (crux/db node #inst "2115-07-03")
        '{:find [cover type]
          :where [[e :consumer-id :RJ29sUU]
                  [e :cover? cover]
                  [e :cover-type type]]})
;;=> #{[true :Full]}
;; end::query-2115[]

;; tag::query-2111[]
(crux/q (crux/db node #inst "2111-07-03")
        '{:find [cover type]
          :where [[e :consumer-id :RJ29sUU]
                  [e :cover? cover]
                  [e :cover-type type]]})
;;=> #{[true :Promotional]}
;; end::query-2111[]

;; tag::query-2112[]
(crux/q (crux/db node #inst "2112-07-03")
        '{:find [cover type]
          :where [[e :consumer-id :RJ29sUU]
                  [e :cover? cover]
                  [e :cover-type type]]})
;;=> #{}
;; end::query-2112[]

;; tag::manifest[]
(crux/submit-tx
 node [[:crux.tx/put
        (assoc manifest
               :badges ["SETUP" "PUT" "DATALOG-QUERIES" "BITEMP"])]])
;;=> #:crux.tx{:tx-id 2, :tx-time #inst "2020-06-18T14:46:47.606-00:00"}
;; end::manifest[]
