(ns generative-artistry.10-print
  (:require [quil.core :as q]
            [generative-artistry.utils :refer :all]
            [generative-artistry.draw-utils :refer :all]))
(def step 1)

(defn n-closest-elements [n p points]
  (->> points
       (sort #(compare (distance p %1) (distance p %2)))
       (take n)))

(def points
  (for [y (range 0 100 step)
        x (range 0 100 step)
        :when (odds 0.3)]
    [x y]))

(defn setup []
  (q/color-mode :hsb 360 100 100 1.0)
  (q/no-loop))

(defn draw []
  (q/background 45 5 100)
  (q/stroke-weight 3)
  (q/no-fill)
  (doseq [[i p] (map-indexed vector points)
          :let [hue (q/map-range i 0 10000 180 360)]]
    (q/stroke hue 100 100)
    (point p)
    (apply triangle (n-closest-elements 3 p points))))

(q/defsketch print-10
  :title "10-print-hsl"
  :size [2000 2000]
  :setup setup
  :draw draw
  :features [])

