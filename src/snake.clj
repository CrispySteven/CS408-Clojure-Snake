(ns snake
  (:require [quil.core :as q]
            [quil.middleware :as m]))
;need game map, food generator, food refiller, snake movement, snake consumption, snake growth, game loss
;def is evaluated only once, so game size and amount is defined and stays the same
(def game_info
  {:width 100 :height 100 :food_amount 20})

(def snake (atom []))
(def direction (atom[]))

;defn is evaluated every time its called
;this randomly spawns food around the map
(defn generate_food[] [(rand-int (game_info :width)) (rand-int (game_info :width))])

(defn refill_food)


;this deals with turning the snake
(defn movement [state event] (case(:key event))
  (:up) (if (not= [0 1] @direction) (reset! direction [0, -1]) @direction)
  (:down) (if (not= [0 -1] @direction) (reset! direction [0, 1]) @direction)
  (:left) (if (not= [1 0] @direction) (reset! direction [-1, 0]) @direction)
  (:right) (if (not= [-1 0] @direction) (reset! direction [1, 0]) @direction)
  )