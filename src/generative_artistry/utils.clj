(ns generative-artistry.utils
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

(defn gauss [mean dev]
  (+ mean (* dev (q/random-gaussian))))

(defn odds [v]
  "returns true if a random [0,1) number is less than v"
  (< (rand) v))

(defn scale-point [[x y]]
  "scales a point with values betwwen 0 and 100 to width and heigth"
  [(w x) (h y)])

(defn interpolate-point [[x1 y1] [x2 y2] amt]
  [(q/lerp x1 x2 amt) (q/lerp y1 y2 amt)])
