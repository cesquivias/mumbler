(ns truffler.Reader-test
  (:require [clojure.test :refer :all])
  (:import [java.io ByteArrayInputStream]
           [truffler NumberForm Reader]))

(defn str->istream [s]
  (ByteArrayInputStream. (.getBytes s)))

(deftest read-number
  (is (= (NumberForm. 1234)
         (NumberForm/readNumber (str->istream "1234")))))
