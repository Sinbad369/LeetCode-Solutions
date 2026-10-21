/*
================================================================================
LEETCODE 1489: FIND CRITICAL AND PSEUDO-CRITICAL EDGES IN MST
================================================================================
HOW TO READ THE PROBLEM DIAGRAM & INPUT NOTATION:
--------------------------------------------------------------------------------
1. NUMBERS INSIDE CIRCLES: (0), (1), (2), (3), (4)
   - These are simply the NODE/VERTEX IDs in the graph.

2. LABELS ON EDGES: <i, weight_i>
   - The values inside the angle brackets `<...>` represent two things:
       * `i`        : The ORIGINAL INDEX of that edge in the input `edges` array.
       * `weight_i` : The COST / WEIGHT of that edge.
   
   - EXAMPLE: An edge labeled `<4, 3>` between node 0 and node 4 means:
       * It is `edges[4]` from the input array.
       * Its weight is `3`.

3. WHY DOES THE DIAGRAM SHOW THE ORIGINAL INDEX `i`?
   - The problem asks you to return the ORIGINAL INDEX NUMBERS of the edges that 
     are critical and pseudo-critical (e.g., return `[0, 1]`, not the node pairs).

--------------------------------------------------------------------------------
WHAT ARE CRITICAL VS. PSEUDO-CRITICAL EDGES? (PLAIN ENGLISH)
--------------------------------------------------------------------------------
- CRITICAL EDGE:
  A "must-have" edge. If you REMOVE this edge from the graph, it becomes impossible 
  to connect all nodes at the cheapest possible total cost.

- PSEUDO-CRITICAL EDGE:
  An "optional / alternative" edge. You don't HAVE to use it, but if you FORCE it 
  to be included first, you can STILL build a full tree with the cheapest total cost.

- USELESS EDGE:
  An edge that can NEVER be used in any cheapest spanning tree (ignored in output).


ALGORITHM OVERVIEW (KRUSKAL'S APPROACH):
1. Store original edge indices and sort edges globally by weight.
2. Find baseline MST weight using standard Kruskal's algorithm.
3. For each edge:
   a. Force-exclude it. If MST weight increases or graph disconnects -> CRITICAL.
   b. Force-include it. If MST weight matches baseline weight -> PSEUDO-CRITICAL.

================================================================================
*/

import java.util.*;

class Solution {

    public List<List<Integer>> findCriticalAndPseudoCriticalEdges(int n, int[][] edges) {
        int m = edges.length;

        // Preserve original indices: [u, v, weight, original_index]
        int[][] sortedEdges = new int[m][4];
        for (int i = 0; i < m; i++) {
            sortedEdges[i][0] = edges[i][0];
            sortedEdges[i][1] = edges[i][1];
            sortedEdges[i][2] = edges[i][2];
            sortedEdges[i][3] = i; // Save original index before sorting
        }

        // line 1: sort E in nondecreasing order of edge weights
        Arrays.sort(sortedEdges, (a, b) -> Integer.compare(a[2], b[2]));

        // calculate baseline MST weight using exact Kruskal's algorithm
        int baseWeight = kruskal(n, sortedEdges, -1, -1);

        List<Integer> critical = new ArrayList<>();
        List<Integer> pseudoCritical = new ArrayList<>();

        // Test each edge independently
        for (int i = 0; i < m; i++) {
            int originalIdx = sortedEdges[i][3];

            // 1. force EXCLUDE edge i: If MST weight goes up or graph disconnects -> CRITICAL
            int weightWithout = kruskal(n, sortedEdges, i, -1);
            if (weightWithout > baseWeight) {
                critical.add(originalIdx);
                continue; // cannot be both critical and pseudo-critical
            }

            // 2. force INCLUDE edge i: If MST weight equals baseWeight -> PSEUDO-CRITICAL
            int weightWith = kruskal(n, sortedEdges, -1, i);
            if (weightWith == baseWeight) {
                pseudoCritical.add(originalIdx);
            }
        }

        return Arrays.asList(critical, pseudoCritical);
    }

    /*
    ============================================================================
    KRUSKAL'S ALGORITHM
    ============================================================================
    ALGORITHM Kruskal(G)
        sort E in nondecreasing order of edge weights
        E_T <- Ø; ecounter <- 0
        k <- 0
        while ecounter < |V| - 1 do
            k <- k + 1
            if E_T U {e_i_k} is acyclic
                E_T <- E_T U {e_i_k}
                ecounter <- ecounter + 1
        return E_T
    ============================================================================
    */
    private int kruskal(int n, int[][] sortedEdges, int skipEdgeIdx, int forceEdgeIdx) {
        DSU dsu = new DSU(n);
        int totalWeight = 0;

        // Line 2: E_T <- Ø, ecounter <- 0
        int encounter = 0;

        // Force-include specified edge first (for pseudo-critical test)
        if (forceEdgeIdx != -1) {
            int[] edge = sortedEdges[forceEdgeIdx];
            dsu.union(edge[0], edge[1]);
            totalWeight += edge[2];
            encounter++;
        }

        // line 5: while encounter < |V| - 1 do
        for (int k = 0; k < sortedEdges.length && encounter < n - 1; k++) {
            if (k == skipEdgeIdx) continue; // skip force-excluded edge

            int u = sortedEdges[k][0];
            int v = sortedEdges[k][1];
            int weight = sortedEdges[k][2];

            // line 7: if E_T U {e_i_k} is acyclic
            if (dsu.union(u, v)) {
                // line 8: E_T <- E_T U {e_i_k}
                totalWeight += weight;
                // line 9: encounter <- encounter + 1
                encounter++;
            }
        }

        // line 10: return E_T (or infinity if graph cannot be connected)
        return dsu.components == 1 ? totalWeight : Integer.MAX_VALUE;
    }

    // Helper Class: Disjoint Set Union (DSU / Union-Find)
    // Used by Kruskal's algorithm to greedily merge components and detect cycles.
    static class DSU {
        int[] parent;
        int components; // Tracks number of connected sub-graphs

        public DSU(int n) {
            parent = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i; // Each node starts in its own set
            }
            components = n;
        }

        // find root of node i with path compression
        public int find(int i) {
            if (parent[i] == i) {
                return i;
            }
            return parent[i] = find(parent[i]);
        }

        // union two components. returns true if successfully connected (no cycle)
        public boolean union(int i, int j) {
            int rootI = find(i);
            int rootJ = find(j);
            if (rootI != rootJ) {
                parent[rootI] = rootJ;
                components--; // merged two components into one
                return true;
            }
            return false;
        }
    }
}

/*
================================================================================
FULL LINE-BY-LINE CODE WALKTHROUGH
Example 1: n = 5, edges = [[0,1,1],[1,2,1],[2,3,2],[0,3,2],[0,4,3],[3,4,3],[1,4,6]]
================================================================================

--- INITIALIZATION PHASE ---
1. `sortedEdges` array created with `[u, v, weight, original_index]`:
   - Index 0: <0, 1> -> [0, 1, 1, 0]
   - Index 1: <1, 1> -> [1, 2, 1, 1]
   - Index 2: <2, 2> -> [2, 3, 2, 2]
   - Index 3: <3, 2> -> [0, 3, 2, 3]
   - Index 4: <4, 3> -> [0, 4, 3, 4]
   - Index 5: <5, 3> -> [3, 4, 3, 5]
   - Index 6: <6, 6> -> [1, 4, 6, 6]

2. `Arrays.sort(sortedEdges)` sorts globally by weight.
3. `baseWeight = kruskal(n, sortedEdges, -1, -1)` runs standard Kruskal:
   - Picks edge <0,1> (w=1), edge <1,1> (w=1), edge <2,2> (w=2), edge <4,3> (w=3).
   - All 5 nodes connected. Total `baseWeight` = 1 + 1 + 2 + 3 = 7.

--- TESTING EACH EDGE ---

- Testing Edge <0,1> (Original Index 0, Weight 1):
  * Force EXCLUDE <0,1> (`kruskal(5, sortedEdges, 0, -1)`):
    - Without edge 0, MST weight increases to 8 > baseWeight (7).
    - Edge 0 is CRITICAL -> `critical.add(0)`.

- Testing Edge <1,1> (Original Index 1, Weight 1):
  * Force EXCLUDE <1,1> (`kruskal(5, sortedEdges, 1, -1)`):
    - Without edge 1, MST weight increases to 8 > baseWeight (7).
    - Edge 1 is CRITICAL -> `critical.add(1)`.

- Testing Edge <2,2> (Original Index 2, Weight 2):
  * Force EXCLUDE <2,2> (`kruskal(5, sortedEdges, 2, -1)`):
    - Without edge 2, edge <3,2> can be used instead! Weight remains 7 == baseWeight.
    - Not critical. Move to Force INCLUDE.
  * Force INCLUDE <2,2> (`kruskal(5, sortedEdges, -1, 2)`):
    - Forcing edge 2 gives total weight 7 == baseWeight.
    - Edge 2 is PSEUDO-CRITICAL -> `pseudoCritical.add(2)`.

- Testing Edges 3, 4, 5:
  * Similar tests show they are optional tie-breakers with weight remaining 7.
  * All added to `pseudoCritical` -> `[2, 3, 4, 5]`.

- Testing Edge <6,6> (Original Index 6, Weight 6):
  * Force INCLUDE <6,6> gives weight 10 > baseWeight (7).
  * Edge 6 is USELESS -> Ignored.

--- RETURN PHASE ---
- Returns `[[0, 1], [2, 3, 4, 5]]`.

================================================================================
TIME & SPACE COMPLEXITY ANALYSIS
================================================================================
1. TIME COMPLEXITY: O(E^2 * α(V))
   - Sorting edges: O(E log E).
   - Baseline Kruskal's: O(E * α(V)), where α is the Inverse Ackermann function 
     (nearly O(1) in practice due to Union-Find path compression).
   - Testing loop: We run Kruskal's algorithm 2 times for each of the E edges.
     That gives 2 * E * O(E * α(V)) = O(E^2 * α(V)).
   - Total Time: O(E^2 * α(V)) — optimal for this problem constraints (E <= 200).

2. SPACE COMPLEXITY: O(V + E)
   - `sortedEdges` array: O(E) to store the copy of edges with original indices.
   - `DSU` parent array: O(V) space to track sets.
   - Result lists: O(E) to hold critical/pseudo-critical indices.
   - Total Space: O(V + E).
================================================================================
*/