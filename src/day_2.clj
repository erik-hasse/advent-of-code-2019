(ns day-2
  (:require [clojure.string :as str]))

(defn parse [x]
  (->>
    (str/split x #",")
    (map #(Integer/parseInt %))
    vec))

(def input (parse (slurp "inputs/day2")))

; part 1
(defn get-args [memory ic]
  [(memory (memory (+ ic 1))) ; arg 1
   (memory (memory (+ ic 2))) ; arg 2
   (memory (+ ic 3))]) ; target location


(defn eval-and-store [memory ic func]
  (let [[arg-1 arg-2 target] (get-args memory ic)]
    (assoc memory target (func arg-1 arg-2))))

; Calls (memory ic) which accesses the opcode to determine which variant to dispatch.
; Each variant should return a new [memory ic] vec
(defmulti run-opcode (fn [memory ic] (memory ic)))
(defmethod run-opcode 1 [memory ic] [(eval-and-store memory ic +) (+ ic 4)])
(defmethod run-opcode 2 [memory ic] [(eval-and-store memory ic *) (+ ic 4)])
(defmethod run-opcode 99 [memory _] [memory :done])
(defmethod run-opcode :default [_ _] (throw (Exception. "Unknown opcode")))

(defn take-through [pred coll]
  (lazy-seq
    (when-let [s (seq coll)]
      (if (pred (first s))
        (list (first s))
        (cons (first s) (take-through pred (rest s)))))))

(defn done? [[_ ic]]
  (= ic :done))

; Returns a sequence containing the full state history so that it can be examined for debugging
(defn run [memory]
  (take-through done? (iterate (partial apply run-opcode) [memory 0])))

(defn init-with [memory noun verb]
  (assoc memory 1 noun 2 verb))

; Get the output from a sequence of steps
(defn parse-output [states]
  (->
    states
    last ; most recent state [memory ic]
    first ; memory vec
    first)) ; 0th value

(defn init-run-output [memory noun verb]
  (->
    memory
    (init-with noun verb)
    run
    parse-output))

(def part-1 (init-run-output input 12 2))

part-1

; part 2
(defn find-first [pred coll]
  (first (filter pred coll)))

(def part-2 (let [[noun verb] (find-first
                                (fn [[noun verb]] (= (init-run-output input noun verb) 19690720))
                                (for [x (range 100) y (range 100)] [x y]))]
              (+ (* 100 noun) verb)))

part-2