(ns generative-artistry.tiled-lines
  (:require [quil.core :as q]))

(defn setup []
  (q/color-mode :hsb 360 100 100 1.0)
  (q/no-loop))

(defn w
  ([] (q/width))
  ([x] (* (q/width) (q/map-range x 0 100 0 1))))

(defn h
  ([] (q/height))
  ([x] (* (q/height) (q/map-range x 0 100 0 1))))

(defn line [x y w h]
  (let [r (rand)]
    (if (> r 0.5)
      (q/line x y (+ x w) (+ y h))
      (q/line x (+ y h) (+ x w) y))))

(defn draw []
  (q/no-loop)
  (q/background 0 0 100 1)
  (q/stroke-weight 4)
  (q/stroke 0 0 100 1)
  ;(q/scale 2)
  (q/no-fill)
  (let [x-step 1
        y-step 2]
    (doseq [x (range 2 98 x-step)
            y (range 2 98 y-step)]
      (q/stroke (q/map-range x 2 98 180 250) 50 (q/map-range y 2 98 50 100) 1)
      (line (w x) (h y) (w x-step) (h y-step)))))

(q/defsketch tiled-lines
  :title "tilted-lines"
  :size [2000 1000]
  :setup setup
  :draw draw
  :features [:keep-on-top])

