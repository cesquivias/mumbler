(ns truffler.list-form-test
  (:require [clojure.test :refer :all])
  (:import [truffler.simple.form ListForm NumberForm]))

(deftest toString-empty
  (is (= "()"
         (str ListForm/EMPTY))))

(deftest toString-list-of-nums
  (is (= "(1 23)"
         (str (.. ListForm/EMPTY
                  (cons (NumberForm. 23))
                  (cons (NumberForm. 1)))))))

(deftest toString-list-of-lists
  (is (= "((1) ())"
         (str (.. ListForm/EMPTY
                  (cons ListForm/EMPTY)
                  (cons (.. ListForm/EMPTY
                            (cons (NumberForm. 1)))))))))

(deftest equals-different-lengths
  (is (false? (= (ListForm/list (map #(NumberForm. %) [1 2]))
                 (ListForm/list [(NumberForm. 1)])))))
