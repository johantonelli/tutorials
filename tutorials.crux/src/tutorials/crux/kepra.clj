(ns tutorials.crux.kepra
  (:require [crux.api :as crux]
            [tutorials.crux.earth :refer [manifest]]))

;; Start node
(def node
  (crux/start-node
   {:crux.node/topology '[crux.standalone/topology]
    :crux.kv/db-dir "data/db-dir"}))

;; define stats
(def stats
  [{:body "Sun"
    :type "Star"
    :units {:radius "Earth Radius"
            :volume "Earth Volume"
            :mass "Earth Mass"
            :gravity "Standard gravity (g)"}
    :radius 109.3
    :volume 1305700
    :mass 33000
    :gravity 27.9
    :crux.db/id :Sun}
   {:body "Jupiter"
    :type "Gas Giant"
    :units {:radius "Earth Radius"
            :volume "Earth Volume"
            :mass "Earth Mass"
            :gravity "Standard gravity (g)"}
    :radius 10.97
    :volume 1321
    :mass 317.83
    :gravity 2.52
    :crux.db/id :Jupiter}
   {:body "Saturn"
    :type "Gas Giant"
    :units {:radius "Earth Radius"
            :volume "Earth Volume"
            :mass "Earth Mass"
            :gravity "Standard gravity (g)"}
    :radius :volume
    :mass :gravity
    :crux.db/id :Saturn}
   {:body "Saturn"
    :units {:radius "Earth Radius"
            :volume "Earth Volume"
            :mass "Earth Mass"
            :gravity "Standard gravity (g)"}
    :radius 9.14
    :volume 764
    :mass 95.162
    :gravity 1.065
    :type "planet"
    :crux.db/id :Saturn}
   {:body "Uranus"
    :units {:radius "Earth Radius"
            :volume "Earth Volume"
            :mass "Earth Mass"
            :gravity "Standard gravity (g)"}
    :radius 3.981
    :volume 63.1
    :mass 14.536
    :gravity 0.886
    :type "planet"
    :crux.db/id :Uranus}
   {:body "Neptune"
    :units {:radius "Earth Radius"
            :volume "Earth Volume"
            :mass "Earth Mass"
            :gravity "Standard gravity (g)"}
    :radius 3.865
    :volume 57.7
    :mass 17.147
    :gravity 1.137
    :type "planet"
    :crux.db/id :Neptune}
   {:body "Earth"
    :units {:radius "Earth Radius"
            :volume "Earth Volume"
            :mass "Earth Mass"
            :gravity "Standard gravity (g)"}
    :radius 1
    :volume 1
    :mass 1
    :gravity 1
    :type "planet"
    :crux.db/id :Earth}
   {:body "Venus"
    :units {:radius "Earth Radius"
            :volume "Earth Volume"
            :mass "Earth Mass"
            :gravity "Standard gravity (g)"}
    :radius 0.9499
    :volume 0.857
    :mass 0.815
    :gravity 0.905
    :type "planet"
    :crux.db/id :Venus}
   {:body "Mars"
    :units {:radius "Earth Radius"
            :volume "Earth Volume"
            :mass "Earth Mass"
            :gravity "Standard gravity (g)"}
    :radius 0.532
    :volume 0.151
    :mass 0.107
    :gravity 0.379
    :type "planet"
    :crux.db/id :Mars}
   {:body "Ganymede"
    :units {:radius "Earth Radius"
            :volume "Earth Volume"
            :mass "Earth Mass"
            :gravity "Standard gravity (g)"}
    :radius 0.4135
    :volume 0.0704
    :mass 0.0248
    :gravity 0.146
    :type "moon"
    :crux.db/id :Ganymede}
   {:body "Titan"
    :units {:radius "Earth Radius"
            :volume "Earth Volume"
            :mass "Earth Mass"
            :gravity "Standard gravity (g)"}
    :radius 0.4037
    :volume 0.0658
    :mass 0.0225
    :gravity 0.138
    :type "moon"
    :crux.db/id :Titan}
   {:body "Mercury"
    :units {:radius "Earth Radius"
            :volume "Earth Volume"
            :mass "Earth Mass"
            :gravity "Standard gravity (g)"}
    :radius 0.3829
    :volume 0.0562
    :mass 0.0553
    :gravity 0.377
    :type "planet"
    :crux.db/id :Mercury}])

;; Submit the data
(crux/submit-tx node (mapv (fn [stat] [:crux.tx/put stat]) stats))

(crux/submit-tx
 node
 [[:crux.tx/put
   {:body "Kepra-5"
    :units {:radius "Earth Radius"
            :volume "Earth Volume"
            :mass "Earth Mass"
            :gravity "Standard gravity (g)"}
    :radius 0.6729
    :volume 0.4562
    :mass 0.5653
    :gravity 1.4
    :type "planet"
    :crux.db/id :Kepra-5}]])

(sort
 (crux/q
  (crux/db node)
  '{:find [g planet]
    :where [[planet :gravity g]]}))

;; helper fn
(defn ingest-and-query
  [traveler-doc]
  (crux/submit-tx node [[:crux.tx/put traveler-doc]])
  (crux/q
   (crux/db node)
   {:find '[n]
    :where '[[e :crux.db/id id]
             [e :passport-number n]]
    :args [{'id (:crux.db/id traveler-doc)}]}))

;; Test it
(ingest-and-query
 {:crux.db/id :origin-planet/test-traveler
  :chosen-name "Test"
  :given-name "Test Traveler"
  :passport-number (java.util.UUID/randomUUID)
  :stamps []
  :penalties []})

;; better helper
(defn ingest-and-query
  "Ingests the given travelers document into Crux, returns the passport
  number once the transaction is complete."
  [traveler-doc]
  (crux/await-tx node (crux/submit-tx node [[:crux.tx/put traveler-doc]]))
  (crux/q
   (crux/db node)
   {:find '[n]
    :where '[[e :crux.db/id id]
             [e :passport-number n]]
    :args [{'id (:crux.db/id traveler-doc)}]}))

;; Test it
(ingest-and-query
 {:crux.db/id :origin-planet/new-test-traveler
  :chosen-name "Testy"
  :given-name "Test Traveler"
  :passport-number (java.util.UUID/randomUUID)
  :stamps []
  :penalties []})

;; Change your name
(ingest-and-query
 {:crux.db/id :earth/ioelena
  :chosen-name "Ioelena"
  :given-name "Johanna"
  :passport-number (java.util.UUID/randomUUID)
  :stamps []
  :penalties []})
