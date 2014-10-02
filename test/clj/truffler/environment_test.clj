(ns truffler.environment-test
    (:require [clojure.test :refer :all])
    (:import [truffler.simple.env Environment]
             [truffler.simple.form SymbolForm]))

(deftest put-and-get
  (let [env (Environment.)]
    (.putValue env (SymbolForm. "foo") "bar")
    (is (= "bar"
           (.getValue env (SymbolForm. "foo"))))))
