/*
================================================================================
LEETCODE 1334: FIND THE CITY WITH THE SMALLEST NUMBER OF NEIGHBORS AT A THRESHOLD DISTANCE
================================================================================

PROBLEM DESCRIPTION:
There are n cities numbered from 0 to n-1. Given an array `edges` where 
edges[i] = [fromi, toi, weighti] represents a bidirectional weighted edge between 
cities fromi and toi, and an integer `distanceThreshold`.

Return the city with the smallest number of cities that are reachable within 
distanceThreshold distance. If there are multiple such cities, return the city 
with the greatest numerical index (tie-breaker).

================================================================================
DP / ALGORITHMIC FORMULATION (FLOYD-WARSHALL ALL-PAIRS SHORTEST PATH):
================================================================================
Let dist[i][j] = shortest distance from city i to city j.

Initial Setup:
- Fill dist matrix with INF (where INF = 1e9 to avoid overflow).
- Set dist[i][i] = 0 for all cities i.
- For each edge [u, v, w]: set dist[u][v] = w and dist[v][u] = w.

Floyd-Warshall Recurrence Relation:
Iterate through every intermediate city k as a potential bridge:
For every pair of cities (i, j):
  dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j])

Tie-breaking & Selection Phase:
Count how many cities j (j != i) have dist[i][j] <= distanceThreshold.
Choose the city i with:
1. Minimum reachable city count.
2. Highest city index i in case of a tie (achieved via `<=`).

Time Complexity:  O(n^3 + E) where n <= 100
Space Complexity: O(n^2) for distance matrix storage.
================================================================================
*/
class Solution {
    public int findTheCity(int n, int[][] edges, int distanceThreshold) {
        // line1: sentinel infinity value to avoid integer overflow
        int INF = (int) 1e9;
        int[][] distance = new int[n][n];

        // line2: initialize distance matrix with INF and 0 on diagonal
        for(int i = 0; i < n; i++){
            Arrays.fill(distance[i], INF);
            distance[i][i] = 0;
        }
        
        // line3: populate bidirectional edge weights
        for(int[] edge : edges){
            int u = edge[0];
            int v = edge[1];
            int w = edge[2];
            distance[u][v] = w;
            distance[v][u] = w;
        }

        // line4: Floyd-Warshall algorithm for all-pairs shortest paths
        for(int k = 0; k < n; k++){
            for(int i = 0; i < n; i++){
                for(int j = 0; j < n; j++){
                    if(distance[i][k] + distance[k][j] < distance[i][j]){
                        distance[i][j] = distance[i][k] + distance[k][j];
                    }
                }
            }
        }

        int resultCity = -1;
        int minReachableCount = n;

        // line5: find city with smallest count of neighbors <= distanceThreshold
        for (int i = 0; i < n; i++){
            int reachableCount = 0;
            for(int j = 0; j < n; j++){
                if(i != j && distance[i][j] <= distanceThreshold){
                    reachableCount++;
                }
            }
            // <= handles tie-breaking automatically (preferring larger city index)
            if(reachableCount <= minReachableCount){
                minReachableCount = reachableCount;
                resultCity = i;
            }
        }
        return resultCity;

    }

}

/*
================================================================================
FULL LINE-BY-LINE CODE WALKTHROUGH (n = 4, threshold = 4)
edges = [[0,1,3],[1,2,1],[1,3,4],[2,3,1]]
================================================================================
Note: distance is dist for brevity

--- INITIALIZATION PHASE ---
- Line 1 & Line 2: Creates dist matrix 4x4 filled with INF, diagonal filled with 0.
- Line 3: Fills edge weights:
  dist[0][1] = dist[1][0] = 3
  dist[1][2] = dist[2][1] = 1
  dist[1][3] = dist[3][1] = 4
  dist[2][3] = dist[3][2] = 1

  Initial Matrix:
         j=0   j=1   j=2   j=3
  i=0  [  0     3    INF   INF ]
  i=1  [  3     0     1     4  ]
  i=2  [ INF    1     0     1  ]
  i=3  [ INF    4     1     0  ]

--- FLOYD-WARSHALL TRACING ---

- k = 1 (Bridge via City 1):
  * dist[0][2] = min(INF, dist[0][1] + dist[1][2]) = 3 + 1 = 4
  * dist[2][0] = 4

- k = 2 (Bridge via City 2):
  * dist[1][3] = min(4, dist[1][2] + dist[2][3]) = 1 + 1 = 2
  * dist[3][1] = 2
  * dist[0][3] = min(INF, dist[0][2] + dist[2][3]) = 4 + 1 = 5
  * dist[3][0] = 5

--- SHORTEST DISTANCE MATRIX RESULT ---
         j=0   j=1   j=2   j=3
  i=0  [  0     3     4     5  ]
  i=1  [  3     0     1     2  ]
  i=2  [  4     1     0     1  ]
  i=3  [  5     2     1     0  ]

--- NEIGHBOR COUNTING (threshold = 4) ---
- Line 5:
  * City 0: reachable <= 4 -> {City 1 (3), City 2 (4)} -> count = 2
    minReachableCount = 2, resultCity = 0

  * City 1: reachable <= 4 -> {City 0 (3), City 2 (1), City 3 (2)} -> count = 3

  * City 2: reachable <= 4 -> {City 0 (4), City 1 (1), City 3 (1)} -> count = 3

  * City 3: reachable <= 4 -> {City 1 (2), City 2 (1)} -> count = 2
    reachableCount (2) <= minReachableCount (2) -> minReachableCount = 2, resultCity = 3

- Returns resultCity = 3.

================================================================================
FULL DISTANCE MATRIX VISUALIZATION (n = 4)
================================================================================

         j=0   j=1   j=2   j=3
i=0    [  0  ] [  3  ] [  4  ] [ 5* ]   <-- 2 neighbors <= 4 (City 1, City 2)
i=1    [  3  ] [  0  ] [  1  ] [  2  ]   <-- 3 neighbors <= 4
i=2    [  4  ] [  1  ] [  0  ] [  1  ]   <-- 3 neighbors <= 4
i=3    [ 5*  ] [  2  ] [  1  ] [  0  ]   <-- 2 neighbors <= 4 (City 1, City 2) -> Win tie-break (i=3 > i=0)
================================================================================
*/