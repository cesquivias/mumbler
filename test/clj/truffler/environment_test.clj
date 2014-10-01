(ns truffler.environment-test
    (:require [clojure.test :refer :all])
    (:import [truffler.env Environment]
             [truffler.form SymbolForm]))

(deftest put-and-get
  (let [env (Environment.)]
    (.putValue env (SymbolForm. "foo") "bar")
    (is (= "bar"
           (.getValue env (SymbolForm. "foo"))))))
