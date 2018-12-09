(ns generative-artistry.utils
  (:require [quil.core :as q]))

(defn w
  ([] (q/width))
  ([x] (* (q/width) (q/map-range x 0 100 0 1))))

(defn h
  ([] (q/height))
  ([x] (* (q/height) (q/map-range x 0 100 0 1))))

(defn random-range [v]
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

(defn distance [[x1 y1] [x2 y2]]
  (q/dist x1 y1 x2 y2))

(defn interpolate-point [[x1 y1] [x2 y2] amt]
  [(q/lerp x1 x2 amt) (q/lerp y1 y2 amt)])

(defn +vec [[x1 y1] [x2 y2]]
  [(+ x1 x2) (+ y1 y2)])

(defn polar->cartesian [radius angle]
  "gets the cartesian form of a centered polar coordinate. Angle must be in radians"
  (->> [(q/cos angle) (q/sin angle)]
       (mapv #(+ 50 (* radius %)))))
