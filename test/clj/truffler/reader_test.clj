(ns truffler.reader-test
  (:require [clojure.test :refer :all])
  (:import [java.io ByteArrayInputStream]
           [truffler ListForm NumberForm Reader]))

(defn str->istream [s]
  (ByteArrayInputStream. (.getBytes s)))

(deftest read-number
  (is (= (NumberForm. 1234)
         (Reader/read (str->istream "1234")))))

(deftest read-empty-list
  (is (= ListForm/EMPTY
         (Reader/read (str->istream "()")))))

(deftest read-list
  (is (= (.cons ListForm/EMPTY (NumberForm. 123))
         (Reader/read (str->istream "(123)")))))

(deftest read-whitespace
  (is (= ListForm/EMPTY
         (Reader/read (str->istream "   (    )    ")))))
