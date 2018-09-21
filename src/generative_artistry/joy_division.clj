(ns generative-artistry.joy-division
  (:require [quil.core :as q]))

(defn setup []
  (q/color-mode :hsb 360 100 100 1.0)
  (q/no-loop))

(defn w
  ([] (q/width))
  ([x] (* (q/width) (q/map-range x 0 100 0 1))))

(defn h
  ([] (q/height))
  ([x] (* (q/height) (q/map-range x 0 100 0 1))))

(defn line [height]
  (for [x (range 0 102 2)
        :let [dist (Math/abs (- 50 x))
              variance (max (- 50 10 dist) 0)
              y (* (rand) variance 1/2 -1)]]
    [(w x) (h (+ height y))]))

(defn draw-line [line]
  (q/begin-shape)
  (doseq [[x y]line]
    (q/curve-vertex x y))
  (q/end-shape))

(defn draw []
  (q/background 0 0 100 1)
  ;(q/stroke 0 0 0 1)
  (q/no-stroke)
  (q/stroke-weight 6)
  (q/fill 0 0 100 1)
  (doseq [x (range 20 97 2)]
    (q/stroke (q/map-range x 20 97 160 200) x 100 1)
    (draw-line (line x)))
  (q/save "joy-division-blue.png")
  )


(defn -main [& args]
  (q/defsketch joy-division
               :title "joy division"
               :size [3000 2000]
               :setup setup
               :draw draw))