(ns generative-artistry.triangle-splitting
  (:require [quil.core :as q]
            [clojure.pprint :as pp]))

(defn w
  ([] (q/width))
  ([x] (* (q/width) (q/map-range x 0 100 0 1))))

(defn h
  ([] (q/height))
  ([x] (* (q/height) (q/map-range x 0 100 0 1))))

(defn gauss [mean variance]
  (+ mean (* variance (q/random-gaussian))))

(defn find-new-mid-point [[x1 y1 :as p1] [x2 y2 :as p2] [x3 y3 :as p3]]
  (let [A (q/dist x1 y1 x2 y2)
        B (q/dist x2 y2 x3 y3)
        C (q/dist x1 y1 x3 y3)]
    (cond
      (and (>= A B) (>= A C)) p3
      (and (>= B A) (>= B C)) p1
      :else p2)))

(defn odds [v]
  (< (rand) v))

(defn interpolate [[x1 y1] [x2 y2] amt]
  [(q/lerp x1 x2 amt) (q/lerp y1 y2 amt)])

(defn divide-triangle [p1 p2 p3]
  (let [contrary (find-new-mid-point p1 p2 p3)
        [n1 n2] (remove #{contrary} [p1 p2 p3])
        new-p (interpolate n1 n2 (q/random 1/3 2/3))]
    [[contrary new-p n1] [contrary new-p n2]]))

(defn divide-triangle-recur [[p1 p2 p3 :as t] depth]
  (if (or (odds (q/map-range depth 0 14 0 1/100)) (>= depth 14))
    [t]
    (let [new-ts (divide-triangle p1 p2 p3)]
      (concat (divide-triangle-recur (first new-ts) (inc depth))
              (divide-triangle-recur (second new-ts) (inc depth))))))

(defn draw-triangle [[x1 y1] [x2 y2] [x3 y3]]
  (q/begin-shape)
  (q/vertex (w x1) (h y1))
  (q/vertex (w x2) (h y2))
  (q/vertex (w x3) (h y3))
  (q/vertex (w x1) (h y1))
  (q/end-shape))

(defn setup []
  (q/smooth 2)
  (q/color-mode :hsb 360 100 100 1.0)
  (q/no-loop))

(defn draw []
  (q/background 45 10 100)
  (q/no-fill)
  (q/stroke-weight 1)
  (q/stroke 0 0 0)
  (let
    [triangles (mapcat #(divide-triangle-recur % 0) [[[2 2] [2 98] [98 98]] [[2 2] [98 2] [98 98]]])]
    (doseq [t triangles]
      (apply draw-triangle t)))
  (q/save "results/aesthetically-pleasing-recursion.png"))


(q/defsketch triangular-mesh
  :title "triangular-mesh"
  :size [2000 2000]
  :setup setup
  :draw draw
  :features [])
