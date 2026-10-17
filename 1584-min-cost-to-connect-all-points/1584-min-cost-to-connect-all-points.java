/*
================================================================================
LEETCODE 1584: MIN COST TO CONNECT ALL POINTS (PRIM'S ALGORITHM / MST)
================================================================================

PROBLEM DESCRIPTION:
You are given an array `points` representing integer coordinates of some points on a 2D-plane, 
where points[i] = [x_i, y_i].

The cost of connecting two points [x_i, y_i] and [x_j, y_j] is the Manhattan distance 
between them: |x_i - x_j| + |y_i - y_j|.

Return the minimum cost to make all points connected (i.e., build a Minimum Spanning Tree 
that connects all n points).

================================================================================
PRIM'S ALGORITHM FORMULATION:
================================================================================
We treat the points as vertices V in a complete graph where the edge weight between 
any pair of points is their Manhattan distance.

Algorithm Steps:
1. Start with an arbitrary starting vertex (v0 = point 0) in V_T.
2. Maintain a set of visited vertices V_T and a Priority Queue storing candidate edges.
3. At each step, greedily select the edge crossing the cut (V_T -> V - V_T) with the 
   ABSOLUTE MINIMUM WEIGHT.
4. Add the newly reached point to V_T and accumulate its edge weight into the total cost.
5. Repeat for exactly |V| - 1 iterations until all points are included in V_T.

Time Complexity:  O(N^2 log N) using Priority Queue, or O(N^2) using dense-graph Prim's.
Space Complexity: O(N) to store visited state and distances.
================================================================================
*/
class Solution {
    public int minCostConnectPoints(int[][] points) {
        int n = points.length;
        if (n == 0){
            return 0;
        }

        // line1: track visited vertices (V_T set)
        boolean[] inMinimumSearchTree = new boolean[n];

        // Priority Queue stores pairs: [edge_weight, target_vertex_u]
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));

        // line2: start at arbitrary root vertex v0 = 0 with weight 0
        pq.add(new int[]{0,0});

        int totalCost = 0;
        int edgesAdded = 0;

        // line3: repeat until all N vertices are included in MinimumSearchTree
        while(!pq.isEmpty() && edgesAdded < n){
            int[] curr = pq.poll();
            int weight = curr[0];
            int u = curr[1];

            // line4: if vertex u is already in V_T, skip to avoid cycles
            if (inMinimumSearchTree[u]) {
                continue;
            }

            // line5: add vertex u to V_T and accumulate MinimumSearchTree cost
            inMinimumSearchTree[u] = true;
            totalCost += weight;
            edgesAdded++;

            // line6: evaluate all outgoing edges from u to unvisited vertices (V - V_T)
            for(int v = 0; v < n; v++){
                if(!inMinimumSearchTree[v]){
                    int dist = Math.abs(points[u][0] - points[v][0]) 
                        + Math.abs(points[u][1] - points[v][1]);
                    pq.add(new int[]{dist, v});
                }
            }
        }
        
        return totalCost;
    }
}

/*
================================================================================
FULL LINE-BY-LINE CODE WALKTHROUGH
points = [[0,0], [2,2], [3,10], [5,2]]
================================================================================

--- INITIALIZATION PHASE ---
- n = 4 points:
  * Point 0: [0,0]
  * Point 1: [2,2]
  * Point 2: [3,10]
  * Point 3: [5,2]
- inMinimumSearchTree = [false, false, false, false]
- Push starting node [weight=0, vertex=0] to Priority Queue.

--- PRIM'S ITERATIONS ---

- Iteration 1 (Pop vertex 0, weight 0):
  * inMinimumSearchTree[0] = true, totalCost = 0
  * Add edges from 0 to all other points:
    - 0 -> 1: dist(|0-2| + |0-2|) = 4 -> Push [4, 1]
    - 0 -> 2: dist(|0-3| + |0-10|) = 13 -> Push [13, 2]
    - 0 -> 3: dist(|0-5| + |0-2|) = 7 -> Push [7, 3]
  * PQ candidates: {[4, 1], [7, 3], [13, 2]}

- Iteration 2 (Pop minimum candidate [4, 1] -> vertex 1):
  * inMinimumSearchTree[1] = true, totalCost = 0 + 4 = 4
  * Add edges from 1 to unvisited points {2, 3}:
    - 1 -> 2: dist(|2-3| + |2-10|) = 9 -> Push [9, 2]
    - 1 -> 3: dist(|2-5| + |2-2|) = 3 -> Push [3, 3]
  * PQ candidates: {[3, 3], [7, 3], [9, 2], [13, 2]}

- inMinimumSearchTree 3 (Pop minimum candidate [3, 3] -> vertex 3):
  * inMinimumSearchTree[3] = true, totalCost = 4 + 3 = 7
  * Add edges from 3 to unvisited point {2}:
    - 3 -> 2: dist(|5-3| + |2-10|) = 10 -> Push [10, 2]
  * PQ candidates: {[7, 3] (skip), [9, 2], [10, 2], [13, 2]}

- Iteration 4 (Pop [7, 3] -> already inMinimumSearchTree, skip. Pop [9, 2] -> vertex 2):
  * inMinimumSearchTree[2] = true, totalCost = 7 + 9 = 16
  * All 4 points added to MinimumSearchTree -> Terminate!

--- RETURN PHASE ---
- Returns totalCost = 16.

================================================================================
MINIMUM SPANNING TREE VISUALIZATION
================================================================================

  Point 0 [0,0] --(weight 4)--> Point 1 [2,2]
                                   |
                               (weight 3)
                                   |
                                   v
  Point 2 [3,10] <--(weight 9)-- Point 3 [5,2]

Total MST Weight = 4 + 3 + 9 = 16
================================================================================
*/