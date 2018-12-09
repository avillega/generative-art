(ns generative-artistry.variations-1
  (:require [quil.core :as q]
            [generative-artistry.utils :refer :all]
            [quil.middleware :as m]))

(def abcd [0.1 1.9 -0.8 -1.2])
(def abcd2 [1.0111 -1.011 2.08 10.2])
(def abcd3 [0.369 2.892 1.939 1.687])
(def abcd4 [1.011 2.892 -0.8 -1.2])

(def step 0.004)
(def n-steps 20)
(def min-r -3)
(def max-r 3)

(defn scalef [f s]
  (fn [[x v]] (mapv #(* s %) (f [x v]))))

(defn addf
  ([f] f)
  ([f g] (fn [v] (mapv + (f v) (g v))))
  ([f g & fs] (reduce addf (addf f g) fs)))

(defn mulf
  ([f] f)
  ([f g] (fn [v] (mapv * (f v) (g v))))
  ([f g & fs] (reduce mulf (mulf f g) fs)))

(defn subf
  ([f] f)
  ([f g] (fn [v amount] (mapv - (f v amount) (g v amount))))
  ([f g & fs] (reduce subf (subf f g) fs)))

(defn powf [f n]
  (apply comp (repeat n f)))

(defn julia [[x y]]
  (let [r (q/sqrt (q/mag x y))
        theta (+ (rand-nth [0 Math/PI]) (* (q/atan2 x y) 0.5))]
    [(* r (q/cos theta)) (* r (q/sin theta))]))

(defn sinosoidal [[x y]]
  [(q/sin x) (q/sin y)])

(defn circular [[x y]]
  (let [r (q/sqrt (q/mag x y))
        theta (* (q/atan2 y x))]
    [(* r (q/cos theta)) (* r (q/sin theta))]))

(defn hyperbolic [[x y]]
  (let [r (q/mag x y)
        theta (q/atan2 x y)
        xx (/ (q/sin theta) r)
        yy (* (q/cos theta) r)]
    [xx yy]))

(defn pdj [[x y]]
  (let [[a b c d] abcd4]
    [(- (q/sin (* a y)) (q/cos (* b x)))
     (- (q/sin (* c x)) (q/cos (* d y)))]))

(defn update-state [actual]
  (+ actual (* step n-steps)))

(defn setup []
  (q/color-mode :hsb 360 100 100 1.0)
  (q/background 50 5 100)
  (q/stroke 0 0 20 0.1)
  min-r)

(def f pdj)

(defn draw-variation [[x y] amount]
  (let [[sx sy] ((scalef f amount) [x y])
        xx (q/map-range sx min-r max-r 2 98)
        yy (q/map-range sy min-r max-r 2 98)]
    (q/point (w xx) (h yy))))

(defn draw [actual]
  (if (<= actual max-r)
    (do
      (q/stroke 0 0 20 0.1)
      (doseq [y (range actual (+ actual (* step n-steps)) step)
              x (range min-r max-r step)]
        (draw-variation [x y] 1)))
    (do
      ;(q/save (str "results/variations-" (System/currentTimeMillis) ".png"))
      (println "done")
      (println abcd4)
      (q/no-loop))))

(q/defsketch variations-1
  :title "variations 1"
  :renderer :p2d
  :size [2000 2000]
  :setup setup
  :draw draw
  :update update-state
  :settings #(q/smooth 8)
  :features []
  :middleware [m/fun-mode])
