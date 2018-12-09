(ns generative-artistry.cubic-disarray
  (:require [quil.core :as q]))

(def step 5)
(defn setup []
  (q/color-mode :hsb 360 100 100 1.0)
  (q/no-loop)
  (q/background 0 0 100)
  (q/stroke 0 0 0)
  (q/stroke-weight 4)
  (q/no-fill))

(defn w
  ([] (q/width))
  ([x] (* (q/width) (q/map-range x 0 100 0 1))))

(defn h
  ([] (q/height))
  ([x] (* (q/height) (q/map-range x 0 100 0 1))))

(defn val->color [v min max]
  (q/map-range v min max 180 250))

(defn draw []
  (q/no-stroke)
  (doseq [x (range step (- 100 step) step)
          y (range step (- 100 step) step)
          :let [t (q/map-range y step (- 100 step) 0 0.8)]]
    (q/with-translation [(w (+ x (q/random (- t) t))) (h y)]
      (q/with-rotation [(q/random (- t) t)]
        (q/fill (val->color y 0 100) 50 90 0.4)
        (q/rect 0 0 (w step) (h step)))))
  (q/save "cubic-disarray.png"))




(q/defsketch cubic-disarray
  :title "cubic disarray"
  :size [2000 2000]
  :setup setup
  :draw draw)
