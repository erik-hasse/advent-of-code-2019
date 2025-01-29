(ns int-code
  (:require [clojure.string :as str]))

(defn parse [x]
  (->>
    (str/split x #",")
    (map #(Integer/parseInt %))
    vec))

(defn get-args [{memory :memory ic :ic}]
  [(memory (memory (+ ic 1))) ; arg 1
   (memory (memory (+ ic 2))) ; arg 2
   (memory (+ ic 3))]) ; target location

(defn get-opcode [{memory :memory ic :ic}] (memory ic))

(defn eval-and-store [state func]
  (let [[arg-1 arg-2 target] (get-args state)]
    (assoc (:memory state) target (func arg-1 arg-2))))

; Calls (memory ic) which accesses the opcode to determine which variant to dispatch.
; Each variant should return a new [memory ic] vec
(defmulti run-opcode get-opcode)
(defmethod run-opcode 1 [state]
  {:memory (eval-and-store state +) :ic (+ (:ic state) 4)})
(defmethod run-opcode 2 [state]
  {:memory (eval-and-store state *) :ic (+ (:ic state) 4)})
(defmethod run-opcode 99 [state]
  (assoc state :ic :done))
(defmethod run-opcode :default [_] (throw (Exception. "Unknown opcode")))

(defn take-through [pred coll]
  (lazy-seq
    (when-let [s (seq coll)]
      (if (pred (first s))
        (list (first s))
        (cons (first s) (take-through pred (rest s)))))))

(defn done? [{ic :ic}]
  (= ic :done))

; Returns a sequence containing the full state history so that it can be examined for debugging
(defn run [memory]
  (take-through done? (iterate run-opcode {:memory memory :ic 0})))

(defn init-with [memory noun verb]
  (assoc memory 1 noun 2 verb))

; Get the output from a sequence of steps
(defn parse-output [states]
  (->
    states
    last ; most recent state [memory ic]
    :memory ; memory vec
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
