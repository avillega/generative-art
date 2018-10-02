(ns generative-artistry.first-fractal
  (:require [quil.core :as q]
            [generative-artistry.utils :refer :all]))

(defn line [level sx sy]
  (let [l (q/map-range level 0 10 0 20)
        ex (+ sx (range-random l))
        ey (+ sy (range-random l))
        children (when (< level 10)
                   (repeatedly 3 #(line (inc level) ex ey)))]
    {:start    [sx sy]
     :end      [ex ey]
     :children children}))

(defn draw-tree
  ([tree] (draw-tree tree 0))
  ([{:keys [start end children]} level]
   (q/fill 0 0 0)
   (q/stroke 0 0 0 0.1)
   (q/stroke-weight (- 5 (* 1/2 level)))
   (q/ellipse (w (first start)) (h (second start)) (- 9 level) (- 9 level))
   (q/line (scale-point start) (scale-point end))
   (doseq [c children]
     (draw-tree c (inc level)))))

(defn setup []
  (q/color-mode :hsb 360 100 100 1.0)
  (q/no-loop))

(defn draw []
  (q/background 45 10 100)
  (let [tree (line 0 50 50)]
    (draw-tree tree))
  (q/save "results/factal-tree.png"))

(defn -main [& args]
  (q/defsketch first-fractal
    :title "first-fractal"
    :size [2000 2000]
    :setup setup
    :draw draw
    :features []))
