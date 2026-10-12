/*
================================================================================
LEETCODE 1462: COURSE SCHEDULE IV (FLOYD-WARSHALL / TRANSITIVE CLOSURE)
================================================================================

PROBLEM DESCRIPTION:
There are a total of numCourses courses labeled from 0 to numCourses - 1.
You are given an array prerequisites where prerequisites[i] = [a_i, b_i] indicates 
that course a_i must be taken before course b_i (a_i is a direct prerequisite of b_i).

Prerequisites are transitive (indirect): if a is a prerequisite of b, and b is a 
prerequisite of c, then a is a prerequisite of c.

You are given queries where queries[j] = [u_j, v_j]. Return a boolean array answer 
where answer[j] is true if u_j is a prerequisite of v_j, and false otherwise.

================================================================================
DP / ALGORITHMIC FORMULATION (FLOYD-WARSHALL TRANSITIVE CLOSURE):
================================================================================
Let isPre[i][j] = boolean flag indicating whether course i is a prerequisite of course j.

Initial Setup:
- isPre[i][j] = true for all direct prerequisite pairs [i, j] in `prerequisites`.
- isPre[i][j] = false for all other pairs.

Transitive Closure Recurrence Relation:
We iterate through all possible intermediate nodes k:
For every pair of courses (i, j):
  isPre[i][j] = isPre[i][j] || (isPre[i][k] && isPre[k][j])

Meaning: Course i is a prerequisite of course j if it was already known to be a 
prerequisite OR if course i is a prerequisite of intermediate course k AND course k 
is a prerequisite of course j.

Time Complexity:  O(V^3 + Q) where V = numCourses, Q = queries.length
Space Complexity: O(V^2) to store the reachability matrix.
================================================================================
*/
class Solution {
    public List<Boolean> checkIfPrerequisite(int numCourses, int[][] prerequisites, int[][] queries) {
        // line1: create adjacency matrix to track reachability between any two courses
        boolean[][] isPrerequisite = new boolean[numCourses][numCourses];

        // line2: populate direct prerequisite dependencies
        for(int[] pre : prerequisites){
            isPrerequisite[pre[0]][pre[1]] = true;
        }

        // line3: Floyd-Warshall algorithm for transitive closure
        for(int k = 0; k < numCourses; k++){
            for(int i = 0; i < numCourses; i++){
                for(int j = 0; j < numCourses; j++){
                    // line4: update reachability if course k acts as a bridge between i and j
                    if(isPrerequisite[i][k] && isPrerequisite[k][j]){
                        isPrerequisite[i][j] = true;
                    }
                }
            }
        }

        // line5: answer each query in O(1) time using the computed matrix
        List<Boolean> ans = new ArrayList<>();
        for(int[] q : queries){
            ans.add(isPrerequisite[q[0]][q[1]]);
        }

        return ans;

    }
}

/*
================================================================================
FULL LINE-BY-LINE CODE WALKTHROUGH (numCourses = 3, pre = [[1,2],[2,0]])
================================================================================

--- INITIALIZATION PHASE ---
- Line 1: Creates `isPre` boolean grid of size 3x3 initialized to false.
- Line 2: Set direct edges:
  * pre = [1,2] -> isPre[1][2] = true
  * pre = [2,0] -> isPre[2][0] = true

  Initial isPre Matrix:
         j=0    j=1    j=2
  i=0  [ false  false  false ]
  i=1  [ false  false  true  ]
  i=2  [ true   false  false ]

--- FLOYD-WARSHALL TRACING ---

- k = 0: No updates since column 0 and row 0 have no common valid path pairs.

- k = 1: No updates.

- k = 2 (Bridge node = 2):
  * i = 1, j = 0:
    - Check: isPre[1][2] (true) && isPre[2][0] (true) -> TRUE
    - line4: updates isPre[1][0] = true

  Updated isPre Matrix:
         j=0    j=1    j=2
  i=0  [ false  false  false ]
  i=1  [ TRUE   false  true  ]  <-- Transitive dependency (1 -> 2 -> 0) established!
  i=2  [ true   false  false ]

--- QUERY PROCESSING PHASE ---
- Query [1, 0]: check isPre[1][0] -> true
- Query [1, 2]: check isPre[1][2] -> true
- Query [0, 1]: check isPre[0][1] -> false

- Line 5: Returns [true, true, false].

================================================================================
FULL TRANSITIVE MATRIX VISUALIZATION (numCourses = 3)
================================================================================

           j=0      j=1      j=2
i=0     [ false ] [ false ] [ false ]
i=1     [ TRUE  ] [ false ] [ true  ]  <-- isPre[1][0] = true via 1 -> 2 -> 0
i=2     [ true  ] [ false ] [ false ]
================================================================================
*/