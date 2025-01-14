(ns day-3
  (:require [clojure.string :as str]))

(defn parse-step [s] [(subs s 0 1) (#(Integer/parseInt %) (subs s 1))])
(defn parse-wire [s] (map parse-step (str/split s #",")))
(defn parse [s] (map parse-wire (str/split-lines s)))

(def input (parse (slurp "inputs/day3")))

(defn vec-add [[l1 l2] [r1 r2]] [(+ l1 r1) (+ l2 r2)])
(defn move-dir [dir]
  (let [dirs {"U" [0 1] "D" [0 -1] "R" [1 0] "L" [-1 0]}]
    (fn [from] (vec-add from (dirs dir)))))

(defn locs-from [prev [dir dist]]
  (->>
    (last prev)
    (iterate (move-dir dir))
    (rest)
    (take dist)))

(defn build-path [seq]
  (apply concat (reductions locs-from [[0 0]] seq)))

(defn find-intersections [directions]
  (->>
    directions
    (map #(set (build-path %)))
    (apply clojure.set/intersection)
    (remove #(= [0 0] %))))

; part 1
(defn dist [[x y]]
  (+ (abs x) (abs y)))

(defn part-1 [input] (apply min (map dist (find-intersections input))))
(part-1 input)

; part 2
(defn index-maps [wires]
  (map #(zipmap (reverse %) (reverse (range (count %)))) wires))

(defn score [wires x]
  (apply + (map #(get % x) wires)))

(defn part-2 [input]
  (let [maps (index-maps (map build-path input))
        intersections (find-intersections input)]
    (apply min (map (partial score maps) intersections))))

(part-2 input)