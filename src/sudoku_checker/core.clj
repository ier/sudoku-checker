(ns sudoku-checker.core
  (:require
   [clojure.string :as str]
   [clojure.set :as set])
  (:gen-class))


(defn- build-cols
  [rows size]
  (mapv
   (fn [pos]
     (mapv #(nth % pos) rows))
   (range size)))


(defn- build-boxes
  [rows size]
  (for [x (range size)
        y (range size)]
    (let [lines (->> rows
                     (drop (* x size))
                     (take size))
          fnx (fn [line]
                (take size (drop (* y size) line)))]
      (->> lines
           (map fnx)
           flatten))))


(defn- parse
  [s box-side-size]
  (let [line-size (* box-side-size box-side-size)
        numbers (-> s
                    (str/replace #"\n" " ")
                    (str/split #" "))
        rows (->> numbers
                  (map #(Integer/parseInt %))
                  (partition line-size)
                  (map vec)
                  vec)
        cols (build-cols rows line-size)
        boxes (build-boxes rows box-side-size)]
    {:rows rows
     :cols cols
     :boxes boxes}))


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


(defn -main
  [& args]
  (if (seq args)
    (let [correct? (->> args first check)
          output (if correct?
                   "Correct sudoku input"
                   "Incorrect sudoku input")]
      (prn output))
    (prn "Usage: java -jar sudoku-checker.jar \"81 digits from 1 to 9 separeated by space\"")))
