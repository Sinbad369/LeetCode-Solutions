/*
================================================================================
LEETCODE 64: MINIMUM PATH SUM (2D GRID MATRIX DP)
================================================================================

PROBLEM DESCRIPTION:
Given a `m x n` grid filled with non-negative numbers, find a path from top left 
to bottom right, which minimizes the sum of all numbers along its path.

Note: You can only move either down or right at any point in time.

EXAMPLES:
1. Input: grid = [[1,3,1],
                  [1,5,1],
                  [4,2,1]]               -->  Output: 7
   Explanation: Because the path 1 -> 3 -> 1 -> 1 -> 1 minimizes the sum.

2. Input: grid = [[1,2,3],
                  [4,5,6]]               -->  Output: 12

================================================================================
CORE DP IDEA & TRANSITIONS (EXACT COIN COLLECTING RECIPROCAL):
================================================================================
We build a 2D table `dp[i][j]` representing the MINIMUM path sum from top-left (0, 0)
to cell (i, j).

1. TOP-LEFT CELL (0, 0):
      dp[0][0] = grid[0][0]

2. ROW 0 (Moving Right Only):
      dp[0][j] = dp[0][j - 1] + grid[0][j]

3. COLUMN 0 (Moving Down Only):
      dp[i][0] = dp[i - 1][0] + grid[i][0]

4. INNER GRID (Can come from Above or Left):
      dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]) + grid[i][j]
================================================================================
*/

class Solution {
    public int minPathSum(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0){
            return 0;
        }

        int m = grid.length;
        int n = grid[0].length;

        /*
        ================================================================================
        STEP 1: INITIALIZE THE 2D DP TABLE
        ================================================================================
        Table `dp` matches grid dimensions m x n.
        ================================================================================
        */
        int[][] dp = new int[m][n];

        /*
        ================================================================================
        STEP 2: BASE CASE - TOP-LEFT CELL
        ================================================================================
        */
        dp[0][0] = grid[0][0];

        /*
        ================================================================================
        STEP 3: FILL FIRST ROW (Moving Right)
        ================================================================================
        */
        for(int j = 1; j < n; j++){
            dp[0][j] = dp[0][j - 1] + grid[0][j];
        }

        /*
        ================================================================================
        STEP 4: FILL FIRST COLUMN & INNER GRID (TEXTBOOK 2-LOOP STRUCTURE)
        ================================================================================
        Outer loop moves row by row (i from 1 to m-1).
        Inner border cell (Column 0) is processed first, followed by inner columns.
        ================================================================================
        */

        for(int i = 1; i < m; i++){
            // First cell of row i (Column 0) - only comes from ABOVE
            dp[i][0] = dp[i - 1][0] + grid[i][0];

            // Remaining cells in row i - take min(ABOVE, LEFT)
            for(int j = 1; j < n; j++){
                int aboveCell = dp[i - 1][j];
                int leftCell = dp[i][j - 1];

                dp[i][j] = Math.min(aboveCell, leftCell) + grid[i][j];
            }
        }

        /*
        ================================================================================
        STEP 5: RETURN THE FINAL RESULT
        ================================================================================
        Bottom-right cell `dp[m-1][n-1]` holds the absolute minimum sum path!
        ================================================================================
        */
        return dp[m - 1][n - 1];
    }
}

/*
================================================================================
EXPLANATION & ANALYSIS
================================================================================

1. State Definitions:
   - `dp[i][j]`: Minimum cumulative path sum from (0,0) to cell (i, j).

2. State Transitions:
   - Base cell: dp[0][0] = grid[0][0]
   - Row 0: dp[0][j] = dp[0][j-1] + grid[0][j]
   - Col 0: dp[i][0] = dp[i-1][0] + grid[i][0]
   - Inner: dp[i][j] = min(dp[i-1][j], dp[i][j-1]) + grid[i][j]

3. Complexity Analysis:
   - Time Complexity: O(M * N) since every cell in the grid is visited once.
   - Space Complexity: O(M * N) auxiliary space to store the 2D DP matrix.

================================================================================
DETAILED WALKTHROUGH (grid = [[1, 3, 1], [1, 5, 1], [4, 2, 1]]):
================================================================================

Grid Dimensions: m = 3 rows, n = 3 columns

--- Base Case ---
dp[0][0] = grid[0][0] = 1

--- Row 0 Initialization ---
  dp[0][1] = dp[0][0] + grid[0][1] = 1 + 3 = 4
  dp[0][2] = dp[0][1] + grid[0][2] = 4 + 1 = 5

--- Row 1 ---
  Column 0: dp[1][0] = dp[0][0] + grid[1][0] = 1 + 1 = 2
  Column 1: dp[1][1] = min(dp[0][1], dp[1][0]) + grid[1][1] = min(4, 2) + 5 = 2 + 5 = 7
  Column 2: dp[1][2] = min(dp[0][2], dp[1][1]) + grid[1][2] = min(5, 7) + 1 = 5 + 1 = 6

--- Row 2 ---
  Column 0: dp[2][0] = dp[1][0] + grid[2][0] = 2 + 4 = 6
  Column 1: dp[2][1] = min(dp[1][1], dp[2][0]) + grid[2][1] = min(7, 6) + 2 = 6 + 2 = 8
  Column 2: dp[2][2] = min(dp[1][2], dp[2][1]) + grid[2][2] = min(6, 8) + 1 = 6 + 1 = 7

Result: dp[2][2] = 7

================================================================================
GRAPHICAL STEP-BY-STEP WALKTHROUGH (grid = [[1, 3, 1], [1, 5, 1], [4, 2, 1]])
================================================================================

--------------------------------------------------------------------------------
STEP 0: INPUT GRID
--------------------------------------------------------------------------------
          j=0   j=1   j=2
   i=0  [  1 ] [  3 ] [  1 ]
   i=1  [  1 ] [  5 ] [  1 ]
   i=2  [  4 ] [  2 ] [  1 ]


--------------------------------------------------------------------------------
STEP 1: FILL ROW 0 & COL 0 BORDERS
--------------------------------------------------------------------------------
          j=0   j=1   j=2
   i=0  [  1 ] [  4 ] [  5 ]
   i=1  [  2 ]
   i=2  [  6 ]


--------------------------------------------------------------------------------
STEP 2: FILL INNER GRID ROW-BY-ROW
--------------------------------------------------------------------------------

Row 1:
  [1, 1]: min(Above=4, Left=2) + grid[1][1]=5  => 2 + 5 = 7
  [1, 2]: min(Above=5, Left=7) + grid[1][2]=1  => 5 + 1 = 6

Row 2:
  [2, 1]: min(Above=7, Left=6) + grid[2][1]=2  => 6 + 2 = 8
  [2, 2]: min(Above=6, Left=8) + grid[2][2]=1  => 6 + 1 = 7


--------------------------------------------------------------------------------
STEP 3: FINAL DP TABLE VISUALIZATION
--------------------------------------------------------------------------------
          j=0   j=1   j=2
   i=0  [  1 ] [  4 ] [  5 ]
   i=1  [  2 ] [  7 ] [  6 ]
   i=2  [  6 ] [  8 ] [  7 ] <-- Final Answer: dp[2][2] = 7
================================================================================
*/