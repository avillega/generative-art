(ns generative-artistry.triangular-mesh
  (:require [quil.core :as q]))

(defn w
  ([] (q/width))
  ([x] (* (q/width) (q/map-range x 0 100 0 1))))

(defn h
  ([] (q/height))
  ([x] (* (q/height) (q/map-range x 0 100 0 1))))

(defn range-random [v]
  "returns a random value between -v and v"
  (q/random (- v) v))

(def colors [[309 43 69 0.5]
             [166 78 69 0.5]
             [355 54 96 0.5]])

(defn create-dot-row [height]
  (let [start (if (even? height) 3/2 3)]
    (for [x (range start 100 3)]
      (->> [x height]
           (map #(+ (range-random 3/2) %))))))

(def create-row (memoize create-dot-row))

(defn create-dot-grid [step]
  (for [x (range step (- 100 step) step)
        :let [next (+ x step)]]
    (cond
      (even? x) (interleave (create-row x) (create-row next))
      :else (interleave (create-row next) (create-row x)))))


(defn setup []
  (q/color-mode :hsb 360 100 100 1.0)
  (q/no-loop))

(defn draw-triangle [[x1 y1] [x2 y2] [x3 y3]]
  (q/triangle (w x1) (h y1) (w x2) (h y2) (w x3) (h y3)))

(defn draw []
  (q/background 55 10 100)
  (q/stroke 0 0 0)
  (q/no-fill)
  (q/stroke-weight 3)
  (doseq [row (create-dot-grid 3)]
    (loop [r row]
      (when (>= (count r) 3)
        (apply draw-triangle (take 3 r))
        (recur (rest r))))))

(defn -main [& args]
  (q/defsketch triangular-mesh
    :title "triangular-mesh"
    :size [2000 2000]
    :setup setup
    :draw draw
    :features []))
