(ns day-2
  (:require [int-code :as ic]))

(def input (ic/parse (slurp "inputs/day2")))

; part 1
(def part-1 (ic/init-run-output input 12 2))

part-1

; part 2
(def part-2 (let [[noun verb] (ic/find-first
                                (fn [[noun verb]] (= (ic/init-run-output input noun verb) 19690720))
                                (for [x (range 100) y (range 100)] [x y]))]
              (+ (* 100 noun) verb)))

part-2