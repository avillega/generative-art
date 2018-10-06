(ns generative-artistry.circle-packing
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-artistry.utils :refer :all]))

(def ratio 0.3)

(defn collide? [{x1 :x y1 :y r1 :r stroke1 :stroke}
                {x2 :x y2 :y r2 :r stroke2 :stroke}]
  (let [x (- x1 x2)
        y (- y1 y2)
        a (+ r1 r2 stroke2 stroke1)]
    (> a (Math/sqrt (+ (* x x) (* y y))))))

(defn update-growing [{:keys [x y r growing stroke max-r] :as circle} others]
  (let [should-grow (and (not-any? #(collide? circle %) others)
                         (< 0 (- x r stroke) (+ x r stroke) 100)
                         (< 0 (- y r stroke) (+ y r stroke) 100)
                         (< r max-r)
                         growing)]
    (assoc circle :growing should-grow)))

(defn grow-circle [circle circles]
  (loop [c (update-growing circle circles)]
    (let [new-c (update c :r + ratio)]
      (if (:growing c)
        (recur (update-growing new-c circles))
        c))))

(defn create-circle [tries others]
  (let [x (q/random 0.5 99.5)
        y (q/random 0.5 99.5)
        new-c {:x x :y y :r 0.03 :stroke 1/5 :growing true :max-r (q/random 10 20)}]
    (cond
      (> tries 2000) nil
      (not-any? #(collide? new-c %) others) new-c
      :else (recur (inc tries) others))))

(defn create-circles []
  (loop [circles []]
    (let [c (some-> (create-circle 0 circles)
                    (update-growing circles)
                    (grow-circle circles))]
      (if c
        (recur (conj circles c))
        circles))))

(defn setup []
  (q/ellipse-mode :radius)
  (q/color-mode :hsb 360 100 100 1.0)
  (q/no-loop))

(defn draw []
  (q/background 45 10 100)
  (let [circles (create-circles)]
    (doseq [{:keys [x y r stroke]} circles]
      (q/stroke-weight (w stroke))
      (q/no-fill)
      (q/ellipse (w x) (h y) (w r) (h r))
      (q/point (w x) (h y)))))

(defn -main [& args]
  (q/defsketch circle-packing
    :title "Circle packing"
    :size [2000 2000]
    :setup setup
    :draw draw
    :features []))
