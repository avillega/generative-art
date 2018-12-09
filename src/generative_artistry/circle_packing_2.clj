(ns generative-artistry.circle-packing-2
  (:require [quil.core :as q]
            [generative-artistry.utils :refer :all]
            [quil.middleware :as m]))

(def margin -1)
(def sizes [[1 1000]])

(defn collide? [{x1 :x y1 :y r1 :r stroke1 :stroke}
                {x2 :x y2 :y r2 :r stroke2 :stroke}]
  (let [x (- x1 x2)
        y (- y1 y2)
        a (+ r1 r2 stroke2 stroke1 margin)]
    (> a (Math/sqrt (+ (* x x) (* y y))))))

(defn create-circle [r tries others]
  (let [x (q/random r (- 100 r))
        y (q/random r (- 100 r))
        candidate {:x x :y y :r r :stroke 1/5}]
    (cond
      (> tries 2000) nil
      (not-any? #(collide? candidate %) others) candidate
      :else (recur r (inc tries) others))))


(defn setup []
  (q/color-mode :hsb 360 100 100 1.0)
  (q/ellipse-mode :radius)
  ;(q/no-loop)
  {:circles      []
   :current-size (first sizes)
   :rest-sizes   (rest sizes)})

(defn update-state [{:keys [circles current-size rest-sizes] :as state}]
  (if (= (second current-size) 0)
    (update-state (-> state
                      (assoc :current-size (first rest-sizes))
                      (assoc :rest-sizes (rest rest-sizes))))
    (if-let [circle (create-circle (first current-size) 0 circles)]
      (-> state
          (update-in [:current-size 1] dec)
          (update :circles conj circle))
      (-> state
          (assoc :current-size (first rest-sizes))
          (assoc :rest-sizes (rest rest-sizes))))))

(defn draw [state]
  (if state
    (doseq [{:keys [x y r stroke]} (:circles state)]
      (q/stroke (w stroke))
      (q/no-fill)
      (q/ellipse (w x) (h y) (w r) (h r)))
    (q/no-loop)))

(q/defsketch circle-packing-2
  :title "circle packing 2"
  :size [2000 2000]
  :setup setup
  :draw draw
  :update update-state
  :features []
  :middleware [m/fun-mode])
