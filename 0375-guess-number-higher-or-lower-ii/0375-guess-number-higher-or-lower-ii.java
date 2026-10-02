/*
================================================================================
LEETCODE 375: GUESS NUMBER HIGHER OR LOWER II (OBST / DP MATRIX)
================================================================================

PROBLEM DESCRIPTION:
We are playing the Guessing Game. The game will focus on numbers from 1 to n.
Every time you guess wrong, I will tell you whether the target number is higher 
or lower, and you pay an amount of money equal to the number you guessed.

Given a particular n, return the minimum amount of money you need to guarantee 
a win regardless of what number I pick.

================================================================================
DP FORMULATION & RECURRENCE RELATION:
================================================================================
Let dp[i][j] = minimum money needed to guarantee a win in range [i, j].

If we pick number k (where i <= k <= j) as our guess:
- If k is wrong, we pay k.
- The target could be in range [i, k - 1] (cost: dp[i][k - 1]) 
  OR range [k + 1, j] (cost: dp[k + 1][j]).
- To guarantee a win in the WORST case, we take MAX(dp[i][k - 1], dp[k + 1][j]).
- Total cost for picking k = k + max(dp[i][k - 1], dp[k + 1][j]).

To find the OPTIMAL initial guess, we take the MINIMUM over all choices of k:

  dp[i][j] = min_{i <= k <= j} { k + max(dp[i][k - 1], dp[k + 1][j]) }

Base Cases:
- dp[i][i] = 0 (1 number left -> guess is guaranteed correct, cost = 0)
- dp[i][j] = 0 for i > j (invalid/empty range)
================================================================================
*/
class Solution {
    public int getMoneyAmount(int n) {
        // line1: create a dp grid of size (n+2)x(n+2) to avoid out-of-bounds exception
        int[][] dp = new int[n+2][n+2];

        // line2: build table diagonal-by-diagonal (len is length of range)
        for(int len = 2; len <= n; len++){
            for(int i = 1; i <= n - len + 1; i++){
                int j = i + len - 1;

                int minCost = Integer.MAX_VALUE;

                // line3: try all possible roots/guesses k in range [i, j]
                for(int k = i; k <= j; k++){
                    int cost = k + Math.max(dp[i][k - 1], dp[k + 1][j]);
                    minCost = Math.min(minCost, cost);
                }

                // line4: store min cost for subproblem range [i, j]
                dp[i][j] = minCost;
            }
        }
        
        // line5: answer the full range [1, n] sits at top-right corner
        return dp[1][n];
        
    }
}

/*
================================================================================
FULL LINE-BY-LINE CODE WALKTHROUGH (n = 4)
================================================================================

--- INITIALIZATION PHASE ---
- Line 1: Creates dp matrix of size 6 x 6 filled with 0s.
  All diagonal entries dp[i][i] are 0 (ranges of size 1 cost 0).

--- DIAGONAL TRACING ---

- Length 2 ranges (len = 2):
  * Range [1, 2]: i=1, j=2
    - k=1: 1 + max(dp[1][0], dp[2][2]) = 1 + 0 = 1
    - k=2: 2 + max(dp[1][1], dp[3][2]) = 2 + 0 = 2
    - dp[1][2] = min(1, 2) = 1

  * Range [2, 3]: i=2, j=3 -> dp[2][3] = 2
  * Range [3, 4]: i=3, j=4 -> dp[3][4] = 3

- Length 3 ranges (len = 3):
  * Range [1, 3]: i=1, j=3
    - k=1: 1 + max(0, dp[2][3]=2) = 3
    - k=2: 2 + max(dp[1][1]=0, dp[3][3]=0) = 2
    - k=3: 3 + max(dp[1][2]=1, 0) = 4
    - dp[1][3] = min(3, 2, 4) = 2

  * Range [2, 4]: i=2, j=4 -> dp[2][4] = 4

- Length 4 range (len = 4):
  * Range [1, 4]: i=1, j=4
    - k=1: 1 + max(0, dp[2][4]=4) = 5
    - k=2: 2 + max(dp[1][1]=0, dp[3][4]=3) = 5
    - k=3: 3 + max(dp[1][2]=1, dp[4][4]=0) = 4
    - k=4: 4 + max(dp[1][3]=2, 0) = 6
    - dp[1][4] = min(5, 5, 4, 6) = 4

--- RETURN PHASE ---
- Line 5: Returns dp[1][4] = 4.

================================================================================
FULL DP TABLE VISUALIZATION (n = 4)
================================================================================

          j=1   j=2   j=3   j=4
i=1     [  0 ] [  1 ] [  2 ] [  4 ]  <-- Top-Right Corner dp[1][4] = 4
i=2     [  - ] [  0 ] [  2 ] [  4 ]
i=3     [  - ] [  - ] [  0 ] [  3 ]
i=4     [  - ] [  - ] [  - ] [  0 ]
================================================================================
*/