(ns generative-artistry.variations-1
  (:require [quil.core :as q]
            [generative-artistry.utils :refer :all]
            [quil.middleware :as m]))

(def abcd [0.1 1.9 -0.8 -1.2])
(def abcd2 [1.0111 -1.011 2.08 10.2])
(def step 0.004)
(def n-steps 100)

(defn- addf
  ([f] f)
  ([f g] (fn [v amount] (mapv + (f v amount) (g v amount))))
  ([f g & fs] (reduce addf (addf f g) fs)))

(defn composef
  ([f] f)
  ([f g] (fn [v amount] (f (g v amount) amount)))
  ([f g & fs] (reduce composef (composef f g) fs)))

(defn mulf
  ([f] f)
  ([f g] (fn [v amount] (mapv * (f v amount) (g v amount))))
  ([f g & fs] (reduce mulf (mulf f g) fs)))

(defn subf
  ([f] f)
  ([f g] (fn [v amount] (mapv - (f v amount) (g v amount))))
  ([f g & fs] (reduce subf (subf f g) fs)))

(defn powf [f n]
  (apply composef (repeat n f)))

(defn julia [[x y] amount]
  (let [r (q/sqrt (q/mag x y))
        theta (+ (rand-nth [0 Math/PI]) (* (q/atan2 x y) 0.5))]
    [(* amount r (q/cos theta)) (* amount r (q/sin theta))]))

(defn sinosoidal [[x y] amount]
  [(* amount (q/sin x)) (* amount (q/sin y))])

(defn circular [[x y] amount]
  (let [r (q/sqrt (q/mag x y))
        theta (* (q/atan2 y x))]
    [(* amount r (q/cos theta)) (* amount r (q/sin theta))]))

(defn hyperbolic [[x y] amount]
  (let [r (q/mag x y)
        theta (q/atan2 x y)
        xx (/ (* amount (q/sin theta)) r)
        yy (* amount (q/cos theta) r)]
    [xx yy]))

(defn pdj [[x y] amount]
  (let [[a b c d] abcd]
    [(* amount (- (q/sin (* a y)) (q/cos (* b x))))
     (* amount (- (q/sin (* c x)) (q/cos (* d y))))]))

(def f (composef circular circular circular pdj pdj))

(defn update-state [actual]
  (+ actual (* step n-steps)))

(defn setup []
  (q/color-mode :hsb 360 100 100 1.0)
  (q/background 45 10 100)
  (q/stroke 0 0 20 0.1)
  -3)

(defn draw-variation [[x y] amount]
  (let [[sx sy] (f [x y] amount)
        xx (q/map-range (gauss sx 0.003) -3 3 2 98)
        yy (q/map-range (gauss sy 0.003) -3 3 2 98)]
    (q/point (w xx) (h yy))))

(defn draw [actual]
  (if (<= actual 3)
    (do
      (q/stroke 0 0 20 0.1)
      (doseq [y (range actual (+ actual (* step n-steps)) step)
              x (range -3 3 step)]
        (draw-variation [x y] 1.5))
      (q/stroke 120 39 98 0.1)
      (doseq [y (range actual (+ actual (* step n-steps)) step)
              x (range -3 3 step)]
        (draw-variation [x y] 1))
      (q/stroke 219 87 74 0.1)
      (doseq [y (range actual (+ actual (* step n-steps)) step)
              x (range -3 3 step)]
        (draw-variation [x y] 0.7)))
    (do
      (q/save (str "results/variations-" (System/currentTimeMillis) ".png"))
      (println "done")
      (q/no-loop))))

(defn -main [& args]
  (q/defsketch variations-1
    :title "variations 1"
    :renderer :p2d
    :size [2000 2000]
    :setup setup
    :draw draw
    :update update-state
    :settings #(q/smooth 8)
    :features []
    :middleware [m/fun-mode]))
