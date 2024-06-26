(ns sudoku-checker.core
  (:require
   [clojure.java.io :as io]
   [clojure.set :as set]
   [clojure.string :as str])
  (:gen-class))

(defn- build-cols
  [rows size]
  (mapv
   (fn [pos]
     (mapv #(nth % pos) rows))
   (range size)))

(defn- window
  [line idx size]
  (->> line
       (drop (* idx size))
       (take size)))

(defn- build-boxes
  [rows size]
  (for [x (range size)
        y (range size)]
    (->> rows
         (drop (* x size))
         (take size)
         (map #(window % y size))
         flatten)))

(defn- build-rows
  [numbers line-size]
  (->> numbers
       (map #(Integer/parseInt %))
       (partition line-size)
       (map vec)
       vec))

(defn- parse
  [s box-side-size]
  (let [line-size (* box-side-size box-side-size)
        rows (-> s
                 (str/replace #"\n" " ")
                 (str/split #" ")
                 (build-rows line-size))]
    {:rows rows
     :cols (build-cols rows line-size)
     :boxes (build-boxes rows box-side-size)}))

(defn- validate
  [rows etalon]
  (and
   (every? #(= (count etalon) (count %)) (map set rows))
   (every? empty? (map #(set/difference (set %) etalon) rows))))

(defn check
  [s]
  (let [box-side-size 3
        line-size (* box-side-size box-side-size)
        {:keys [rows cols boxes]} (parse s box-side-size)]
    (every?
     #(validate % (set (range 1 (inc line-size))))
     [rows cols boxes])))

(defn- valid-input-size?
  [filename]
  (<= (->> filename io/file .length) 161))

(defn- valid-values?
  [s]
  (every?
   (fn [x]
     (and (Character/isDigit x)
          (not= (char x) \0)))
   s))

(defn -main
  [& args]
  (if (seq args)
    (if (and (valid-input-size? (first args))
             (valid-values? (first args)))
      (let [correct? (->> args first slurp check)
            output (if correct?
                     "Correct sudoku input"
                     "Incorrect sudoku input")]
        (prn output))
      (prn "File content should have only 81 digits from 1 to 9 separeted with space."))
    (prn "Usage: java -jar target/uberjar/sudoku-checker-0.1.0-SNAPSHOT-standalone.jar doc/sudoku-solution-example.txt")))
