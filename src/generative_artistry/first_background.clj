(ns generative-artistry.first-background
  (:require [quil.core :as q]
            [generative-artistry.utils :refer :all]))

(defn draw-paper-feel []
  (q/fill 0 0 50 0.1)
  (q/no-stroke)
  (dotimes [_ 500000]
    (let [x (q/random 0 100)
          y (q/random 0 100)]
      (q/ellipse (w x) (h y) 2 2))))

(defn draw-background-squares []
  (q/stroke-weight 1)
  (q/stroke 180 50 100 0.3)
  (q/no-fill)
  (doseq [x (range 0 100 2)
          y (range 0 100 2)]
    (when (odds 0.7)
      (q/rect (w x) (w y) (w 2) (h 2)))))

(defn random-circle-point [f radians center rad]
  (+ center (* (+ (random-range 0.5) rad) (f radians))))

(defn random-circle [x-center y-center]
  (let [radius (gauss 20 2)]
    (for [x (range 0 360 5)
          :let [rad (q/radians x)]]
      [(random-circle-point q/sin rad x-center radius)
       (random-circle-point q/cos rad y-center radius)])))

(defn draw-circle [[[fx fy] & fs]]
  (q/begin-shape)
  (q/vertex (w fx) (h fy))
  (doseq [[x y] fs]
    (q/curve-vertex(w x) (h y)))
  (q/vertex (w fx) (h fy))
  (q/end-shape))

(defn setup []
  (q/color-mode :hsb 360 100 100 1.0)
  (q/no-loop))

(defn draw []
  (q/background 0 0 100)
  (draw-paper-feel)
  (draw-background-squares)
  (q/stroke 0 0 0 0.3)
  (q/no-fill)
  (q/stroke-weight 2)
  (dotimes [_ 3]
    (draw-circle (random-circle (gauss 50 1) (gauss 50 1))))
  (q/save "results/the-back-ground.png"))

(q/defsketch first-background
  :title "first bacground"
  :size [2000 2000]
  :setup setup
  :draw draw
  :features [])

