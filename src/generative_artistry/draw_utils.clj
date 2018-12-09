(ns generative-artistry.draw-utils
  (:require [quil.core :as q]
            [generative-artistry.utils :refer :all]))

(defn line [[x1 y1] [x2 y2]]
  (q/line (w x1) (h y1) (w x2) (h y2)))

(defn point [[x y]]
  (q/point (w x) (h y)))

(defn triangle [[x1 y1] [x2 y2] [x3 y3]]
  (q/triangle (w x1) (h y1) (w x2) (h y2) (w x3) (h y3)))

