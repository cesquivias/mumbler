(ns truffler.reader-test
  (:require [clojure.test :refer :all])
  (:import [java.io ByteArrayInputStream]
           [truffler Reader]
           [truffler.form BooleanForm ListForm NumberForm SymbolForm]))

(defn str->istream [s]
  (ByteArrayInputStream. (.getBytes s)))

(deftest read-number
  (is (= (ListForm/list [(NumberForm. 1234)])
         (Reader/read (str->istream "1234")))))

(deftest read-empty-list
  (is (= (ListForm/list [ListForm/EMPTY])
         (Reader/read (str->istream "()")))))

(deftest read-list
  (is (= (ListForm/list [(ListForm/list [(NumberForm. 123) (NumberForm. 5)])])
         (Reader/read (str->istream "(123 5)")))))

(deftest read-whitespace
  (is (= (ListForm/list [ListForm/EMPTY])
         (Reader/read (str->istream "   (    )    ")))))

(deftest read-symbol
  (is (= (ListForm/list [(SymbolForm. "foo")])
         (Reader/read (str->istream "foo")))))

(deftest read-multiple-forms
  (is (= (ListForm/list [(SymbolForm. "foo")
                         (ListForm/list [(NumberForm. 12) (NumberForm. 3)])])
         (Reader/read (str->istream "foo (12 3)")))))

(deftest read-unmatched-end-paren
  (is (thrown? IllegalArgumentException
               (Reader/read (str->istream ")")))))

(deftest read-true-false
  (is (= (ListForm/list [(BooleanForm/TRUE)
                         (BooleanForm/FALSE)])
         (Reader/read (str->istream "#t #f")))))
