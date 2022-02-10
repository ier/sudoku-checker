(ns sudoku-checker.core-test
  (:require [clojure.test :refer [deftest testing is]]
            [sudoku-checker.core :refer [check]]))


(deftest input-test
  (testing "parse"
    (let [input "1 2 3 4 5 6 7 8 9\n4 5 6 7 8 9 1 2 3\n7 8 9 1 2 3 4 5 6\n2 3 4 5 6 7 8 9 1\n5 6 7 8 9 1 2 3 4\n8 9 1 2 3 4 5 6 7\n3 4 5 6 7 8 9 1 2\n6 7 8 9 1 2 3 4 5\n9 1 2 3 4 5 6 7 8"
          box-side-size 3
          result (#'sudoku-checker.core/parse input box-side-size)]
      (is (= [[1 2 3 4 5 6 7 8 9]
              [4 5 6 7 8 9 1 2 3]
              [7 8 9 1 2 3 4 5 6]
              [2 3 4 5 6 7 8 9 1]
              [5 6 7 8 9 1 2 3 4]
              [8 9 1 2 3 4 5 6 7]
              [3 4 5 6 7 8 9 1 2]
              [6 7 8 9 1 2 3 4 5]
              [9 1 2 3 4 5 6 7 8]]
             (:rows result)))

      (is (= [[1 4 7 2 5 8 3 6 9]
              [2 5 8 3 6 9 4 7 1]
              [3 6 9 4 7 1 5 8 2]
              [4 7 1 5 8 2 6 9 3]
              [5 8 2 6 9 3 7 1 4]
              [6 9 3 7 1 4 8 2 5]
              [7 1 4 8 2 5 9 3 6]
              [8 2 5 9 3 6 1 4 7]
              [9 3 6 1 4 7 2 5 8]]
             (:cols result)))

      (is (= [[1 2 3 4 5 6 7 8 9]
              [4 5 6 7 8 9 1 2 3]
              [7 8 9 1 2 3 4 5 6]
              [2 3 4 5 6 7 8 9 1]
              [5 6 7 8 9 1 2 3 4]
              [8 9 1 2 3 4 5 6 7]
              [3 4 5 6 7 8 9 1 2]
              [6 7 8 9 1 2 3 4 5]
              [9 1 2 3 4 5 6 7 8]]
             (:boxes result)))))

  (testing "check"
    (let [correct-input "1 2 3 4 5 6 7 8 9\n4 5 6 7 8 9 1 2 3\n7 8 9 1 2 3 4 5 6\n2 3 4 5 6 7 8 9 1\n5 6 7 8 9 1 2 3 4\n8 9 1 2 3 4 5 6 7\n3 4 5 6 7 8 9 1 2\n6 7 8 9 1 2 3 4 5\n9 1 2 3 4 5 6 7 8"]
      (is (check correct-input)))
    
    (let [incorrect-input "2 2 3 4 5 6 7 8 9\n4 5 6 7 8 9 1 2 3\n7 8 9 1 2 3 4 5 6\n2 3 4 5 6 7 8 9 1\n5 6 7 8 9 1 2 3 4\n8 9 1 2 3 4 5 6 7\n3 4 5 6 7 8 9 1 2\n6 7 8 9 1 2 3 4 5\n9 1 2 3 4 5 6 7 8"]
      (is (not (check incorrect-input))))))