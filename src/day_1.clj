(ns day-1
  (:require [clojure.math :as math]
            [clojure.string :as str]))

(def input
  (->>
    (slurp "inputs/day1")
    (str/split-lines)
    (map Integer/parseInt)))


; part 1
(defn calc-fuel [x]
  (- (math/floor-div x 3) 2))

(defn total-fuel [func nums]
  (->>
    nums
    (map func)
    (apply +)))


(def part1
  (partial total-fuel calc-fuel))

(part1 input)


; part 2
(defn calc-fuel-tyranny [x]
  (->>
    (iterate calc-fuel x)
    (rest)
    (take-while pos?)
    (apply +)))

(def part2
  (partial total-fuel calc-fuel-tyranny))

(part2 input)