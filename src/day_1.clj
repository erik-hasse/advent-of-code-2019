(ns day-1
  (:require [clojure.string :as str])
  (:require [clojure.math :as math]))

(def input (slurp "inputs/day1"))

; part 1

(defn calc-fuel [x] (- (math/floor-div x 3) 2))
(->>
  input
  (str/split-lines)
  (map read-string)
  (map calc-fuel)
  (reduce +))

; part 2
(defn calc-fuel-tyranny [x]
  (->>
    (iterate calc-fuel x)
    (drop 1)
    (take-while pos?)
    (reduce +)
 ))

(->>
  input
  (str/split-lines)
  (map read-string)
  (map calc-fuel-tyranny)
  (reduce +)
  )