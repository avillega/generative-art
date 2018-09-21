(ns generative-artistry.playing-squares
  (:require [quil.core :as q]))

(defn setup []
  (q/color-mode :hsb 360 100 100 1.0)
  (q/background 0 0 100)
  (q/no-loop))

(defn w
  ([] (q/width))
  ([x] (* (q/width) (q/map-range x 0 100 0 1))))

(defn h
  ([] (q/height))
  ([x] (* (q/height) (q/map-range x 0 100 0 1))))

(defn gauss [mean variance]
  (+ mean (* variance (q/random-gaussian))))

(defn draw []
  (q/stroke 0 0 0)
  (q/stroke-weight 3)
  (q/no-fill)
    (q/rect-mode :center)
  (doseq [x (range 5 100 5)
          y (range 5 100 5)
          :let [r (max 0.3 (gauss 1 0.2))]]
    (q/with-translation [(w x) (h y)]
      (loop [size 5]
        (when (> size 0)
          (q/rect (* 2 (q/random-gaussian)) (* 2 (q/random-gaussian))  (w size) (h size))
          (recur (- size r))))))
  (q/save "playing-squares.png"))




(defn -main [& args]
  (q/defsketch playing-squares
               :title "playing squares"
               :size [2000 2000]
               :setup setup
               :draw draw))
