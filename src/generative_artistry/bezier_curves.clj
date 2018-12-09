(ns generative-artistry.bezier-curves
  (:require [quil.core :as q]
            [generative-artistry.utils :refer :all]
            [clojure.pprint :as pp]
            [quil.middleware :as m]))

(defn control-points [mean-rad]
  (for [a (range 0 365 5)
        :let [radians (q/radians a)
              radius (gauss mean-rad (* 0.05 mean-rad))
              x (+ 50 (* radius (q/cos radians)))
              y (+ 50 (* radius (q/sin radians)))]]
    [x y]))

(defn curve [control-points]
  (interleave control-points
              (->> (partition 2 1 control-points)
                   (map #(interpolate-point (first %) (second %) 1/2)))))

(defn draw-bezier [control-points]
  (q/begin-shape)
  (let [[x y] (first control-points)]
    (q/vertex (w x) (h y)))
  (doseq [[[x1 y1] [x2 y2]] (partition-all 2 (curve (next control-points)))]
    (q/quadratic-vertex (w x1) (h y1) (w x2) (h y2)))
  (q/end-shape :close))

(defn setup []
  (q/color-mode :hsb 360 100 100 1.0)
  (q/background 0 0 100)
  (q/frame-rate 60)
  ;(q/no-loop)
  {:points (control-points 60)
   :times 0
   :mean-rad 55})

(defn update-state [{:keys [points times mean-rad] :as state}]
  (if (< times 100)
    (-> state
        (assoc :points (map #(+vec % [(random-range 0.3) (random-range 0.3)]) points))
        (update :times inc))
    (-> state
        (assoc :points (control-points mean-rad))
        (update :mean-rad - 5)
        (assoc :times 0))))

(defn draw [{:keys [points]}]
  ;(q/background 50 5 100)
  (q/no-fill)
  (q/stroke-weight 3)
  (q/stroke 0 0 0 0.05)
  (draw-bezier points))

(q/defsketch bezier-curves
  :title "bezier"
  :size [2000 2000]
  :setup setup
  :draw draw
  :mouse-clicked (fn [_ _] (q/save "results/going-down.png"))
  :update update-state
  :features []
  :middleware [m/fun-mode])


