# Algorithms Part 1 (By Princeton University) Assignments
A repository containing the source code for the assignments in Algorithm Part 1 on Coursera.org.
Link of the course: https://www.coursera.org/learn/algorithms-part1/
This repository contains 5 assignments:
  - Percolation
  - Deque
  - Collinear Points
  - 8 Puzzle
  - Kd-Tree
  
 ## Detail of each assignments
 ### 1. Percolation
 - Using the Union Find Algorithm to determine the proportion of metal needed in a given material so that it could become a conducter
 (let the current flow through).
 Link to the assignment specification: https://coursera.cs.princeton.edu/algs4/assignments/percolation/specification.php 
 ### 2. Deque
 - Implementing the generic deque (double-ended queue) abstract data type. A double-ended queue or deque (pronounced “deck”) is a
 generalization of a stack and a queue that supports adding and removing items from either the front or the back of the data structure.
 - Implementing the randomized queue abstract data type that return a random element when calling dequeue().
 - Implementing Permutation class that uses the randomized queue just implemented to return a random permutation of the characters in a string.
 Link to the assignment specification: https://coursera.cs.princeton.edu/algs4/assignments/queues/specification.php
 ### 3. Collinear Points
 - Given a set of n distinct points in the plane, find every (maximal) line segment that connects a subset of 4 or more of the points.
 - Implementing the solution to the above problem using 2 different algorithms: brute-force and merge-sort
 Link to the assignment specification: https://coursera.cs.princeton.edu/algs4/assignments/collinear/specification.php
 ### 4. 8 Puzzle
 - The 8-puzzle is a sliding puzzle that is played on a 3-by-3 grid with 8 square tiles labeled 1 through 8, plus a blank square. The goal 
 is to rearrange the tiles so that they are in row-major order, using as few moves as possible. You are permitted to slide tiles either 
 horizontally or vertically into the blank square. The following diagram shows a sequence of moves from an initial board (left) to the goal 
 board (right).
 - Implementing the A* search algorithm to solve any 8-puzzle games (or n-puzzle games) using the Priority Queue abstract data type.
 - Link to the assignment specification: https://coursera.cs.princeton.edu/algs4/assignments/8puzzle/specification.php
 ### 5. Kd-Tree
 - Write a data type to represent a set of points in the unit square (all points have x- and y-coordinates between 0 and 1) using a 2d-tree 
 to support efficient range search (find all of the points contained in a query rectangle) and nearest-neighbor search (find a closest 
 point to a query point).
 - Link to the assignment specification: https://coursera.cs.princeton.edu/algs4/assignments/kdtree/specification.php
 
 ## How to run the solution
 ### 1. Download the third-party Java library provided by Princeton University
 The solutions in the assignments all use the library provided in the course so you need to download it first in order to run and test 
 the solutions.
 You can download each class needed for each solution or you can simply download the whole solution from the course's website: 
 https://algs4.cs.princeton.edu/code/
 ### 2. Download the project folder from the assignment specification webpage
 The project folder containing all the test data and the supporting classes (that not provided in the library above) can be downloaded in the specification webpage for each assignment.

