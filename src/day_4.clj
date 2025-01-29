(ns day-4)

(defn digits [x]
  (->>
    (str x)
    (map #(Integer/parseInt (str %)))))

(defn digit-pairs [x]
    (partition 2 1 (digits x)))

(defn adjacent-digits-same [num]
  (some #(apply = %) (digit-pairs num)))

(defn digits-non-decreasing [num]
  (every? #(apply <= %) (digit-pairs num)))

(defn part-1 [start stop]
  (->>
    (range start stop)
    (filter (every-pred adjacent-digits-same digits-non-decreasing))
    count))

(part-1 178416 676461)

(defn exactly-2-adj-same [num]
  (contains?
    (->>
      (digits num)
      (partition-by identity)
      (map count)
      set)
    2))

(defn part-2 [start stop]
  (->>
    (range start stop)
    (filter (every-pred exactly-2-adj-same digits-non-decreasing))
    count))

(part-2 178416 676461)
