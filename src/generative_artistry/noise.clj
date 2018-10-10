(ns generative-artistry.noise
  (:require [quil.core :as q]
            [generative-artistry.utils :refer :all]
            [quil.middleware :as m]))

(def title "noise2")

(defn save-image []
  (q/save (str "results/" title ".png")))

(defn setup []
  (q/color-mode :hsb 360 100 100 1.0)
  (q/no-loop))

(defn draw []
  (q/background 0 0 100)
  (q/stroke-weight 3)
  (q/fill 210 100 100 0.8)
  (q/no-stroke)
  (dotimes [degree 360]
    (let [angle (q/radians degree)
          x (+ 50 (* 20 (q/cos angle)))
          y (+ 50 (* 20 (q/sin angle)))]
      (q/fill 210 100 100 0.05)
      (q/ellipse (w x) (h y) (w 2) (h 2))
      (q/fill 210 100 100 0.07)
      (loop [t 1000
             nx x
             ny y]
        (when (< t 2000)
          (let [mr #(q/map-range % 0 1 -0.3 0.3)
                sy (mr (q/noise (/ ny 10) (/ nx 10) (/ t 100)))
                sx (mr (q/noise (/ nx 10) (/ t 100) (/ ny 10)))]
            (q/ellipse (w (+ sx nx)) (h (+ sy ny)) 5 5)
            (recur (inc t) (+ sx nx) (+ sy ny)))))))
  (q/no-fill)
  (q/stroke-weight 5)
  (q/stroke 210 100 100)
  (q/rect (w 10) (h 10) (w 80) (h 80))
  (q/save (str "results/" title ".png")))

(defn -main [& args]
  (q/defsketch noise
    :title title
    :renderer :p2d
    :size [2000 2000]
    :setup setup
    :draw draw
    :mouse-clicked save-image
    :features []
    :middleware []))
