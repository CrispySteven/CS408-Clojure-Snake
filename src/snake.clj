(ns snake-quil.core
  (:require [quil.core :as q]
            [quil.middleware :as m]))

;need game map, food generator, food remover, snake movement, snake consumption, snake growth, game loss, snake wrapping
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; DON'T FORGET THESE THINGS ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; '() for lists, [] for vectors
; let is a temporary bimding


;set up the world
(def game_info {:width 100 :height 100})
;define the snake and spawns it at x=50, y=50, food
(def snake_vector (atom [[50 50]]))

(def food (atom #{}))

(def direction (atom[]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; FOOD STUFF ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;this randomly spawns food around the map
;this colors the food red (used in the draw state)
(def food_color (atom '(255 0 0)))

;generates food 50% of the time every time this is checked by the update function and adds it to the food set
(defn food_gen[]
  (when (< (rand) 0.5 (swap! food conj [(rand-int (game_info :width)) (rand-int (game_info :width))]))))

;remove the food when eaten
(defn food_consumed[] (swap! food disj (first @snake_vector)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; SNAKE STUFF ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;sets initial direction to the right (x=1, y=0)
(swap! direction conj 1 0)
;this colors the snake green (used in the draw state)
(def snake_color (atom '(0 255 54)))


;this deals with turning the snake
(defn movement [state event] (case(:key event))
  (:up) (if (not= [0 1] @direction) (reset! direction [0, -1]) @direction)
  (:down) (if (not= [0 -1] @direction) (reset! direction [0, 1]) @direction)
  (:left) (if (not= [1 0] @direction) (reset! direction [-1, 0]) @direction)
  (:right) (if (not= [-1 0] @direction) (reset! direction [1, 0]) @direction)
  )
;snake wrapping around the map
(defn wrap [i m]
  (loop [x i] (cond (< x 0) (recur (+ x m)) (>= x m) (recur (- x m)) :else x)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; QUIL FUNCTIONS ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn setup []
  (q/smooth)
  (q/frame-rate 60))

(defn update-state [state]
  (food_gen)
  )

(defn draw-state [state]
  (let [w (/ (q/width) (game_info :width))
        h (/ (q/height) (game_info :height))]
    (q/background 0 0 0)
    ;this actually colors the food and the snake
    (doseq [[x y] @food]
      (q/fill (first @food_color) (second @food_color) (last @food_color))
      (q/stroke (first @food_color) (second @food_color) (last @food_color))
      (q/rect (* w x) (* h y) w h))

    (doseq [[x y] @snake_vector]
      (q/fill (first @snake_color) (second @snake_color) (last @snake_color))
      (q/stroke (first @snake_color) (second @snake_color) (last @snake_color))
      (q/rect (* w x) (* h y) w h)))
  (q/fill 100 255 100)
  (q/text-size 20))


(q/defsketch snake
  :title "Snake"
  :size [600 600]
  ; setup function called only once, during sketch initialization.
  :setup setup
  ; update-state is called on each iteration before draw-state.
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  ; This sketch uses functional-mode middleware.
  ; Check quil wiki for more info about middlewares and particularly
  ; fun-mode.
  :middleware [m/fun-mode])
