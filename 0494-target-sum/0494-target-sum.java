/*
================================================================================
LEETCODE 494: TARGET SUM
================================================================================

PROBLEM DESCRIPTION:
You are given an integer array `nums` and an integer `target`.
You want to build an expression by adding one of the symbols '+' and '-' 
before each integer in `nums` and then concatenate all the integers.

Return the number of different expressions that you can build, which evaluates to `target`.

EXAMPLE:
Input: nums = [1, 1, 1, 1, 1], target = 3
Output: 5
Explanation: There are 5 ways to assign symbols to make the sum of nums be 3:
-1 + 1 + 1 + 1 + 1 = 3
+1 - 1 + 1 + 1 + 1 = 3
+1 + 1 - 1 + 1 + 1 = 3
+1 + 1 + 1 - 1 + 1 = 3
+1 + 1 + 1 + 1 - 1 = 3

================================================================================
MATHEMATICAL TRANSFORMATION TO 0-1 KNAPSACK:
================================================================================
Let P be the subset of numbers with '+' signs and N be the subset with '-' signs.

1. sum(P) - sum(N) = target
2. sum(P) + sum(N) = totalSum

Adding both equations together:
2 * sum(P) = target + totalSum
sum(P) = (target + totalSum) / 2

So the problem becomes: "How many subsets of `nums` sum up to W = (target + totalSum) / 2?"

VALIDITY CHECKS:
1. If (target + totalSum) is ODD, or if target > totalSum or target < -totalSum, 
   return 0 (impossible to form).
================================================================================
*/
class Solution {
    public int findTargetSumWays(int[] nums, int target) {

        // line1 - calc the total sum of array
        int totalSum = 0;
        for(int num : nums){
            totalSum += num;
        }

        // line2 - check math validity constraints
        if(Math.abs(target) > totalSum || (target + totalSum) % 2 != 0){
            return 0;
        }

        // line3 - calc subset target w and array length n
        int W = (target + totalSum) / 2;
        int n = nums.length;

        // line4 - initialize 2d dp grid(const number of ways to form capacity j)
        int[][] dp = new int[n + 1][W + 1];

        // line5 - base case - 1  way to form sum 0 using 0 elements (empty subset)
        dp[0][0] = 1;

        // line6 - fill dp grid row-by-row
        for(int i = 1; i <= n; i++){
            int currentNum = nums[i - 1];
            
            for(int j = 0; j <= W; j++){
                if(currentNum > j){
                    // line7 - number is too large -> copy from above
                    dp[i][j] = dp[i - 1][j];
                } else{
                    // line8 - total ways = exclude(above) + include(above shifted left)
                    int exclude = dp[i - 1][j];
                    int include = dp[i - 1][j - currentNum];
                    dp[i][j] = exclude + include;
                }
            }
        }

        // line9 - return total number of ways to reach capacity W using all n items
        return dp[n][W];
        
    }
}

/*
================================================================================
FULL LINE-BY-LINE CODE WALKTHROUGH (nums = [1, 1, 1, 1, 1], target = 3)
================================================================================

--- INITIALIZATION PHASE ---
- Line 1 (totalSum calculation): totalSum = 1 + 1 + 1 + 1 + 1 = 5.
- Line 2 (validity check):
    target + totalSum = 3 + 5 = 8 (Even number -> valid).
    abs(3) <= 5 -> valid.
- Line 3 (subset target W): W = (3 + 5) / 2 = 4, n = 5.
- Line 4 (dp matrix creation): Creates dp[6][5] filled with 0.
- Line 5 (base case): dp[0][0] = 1 (1 way to make sum 0 with 0 elements).

--- ROW 1: i = 1 (currentNum = nums[0] = 1) ---
- j = 0: currentNum > 0 (1 > 0) -> Line 7: dp[1][0] = dp[0][0] = 1
- j = 1: currentNum <= 1 (1 <= 1) -> Line 8:
    exclude = dp[0][1] = 0
    include = dp[0][1 - 1] = dp[0][0] = 1
    dp[1][1] = 0 + 1 = 1
- j = 2 to 4: dp[1][j] = 0
-> Row 1 State: [1, 1, 0, 0, 0]

--- ROW 2: i = 2 (currentNum = nums[1] = 1) ---
- j = 0: dp[2][0] = dp[1][0] = 1
- j = 1: dp[2][1] = dp[1][1] + dp[1][0] = 1 + 1 = 2
- j = 2: dp[2][2] = dp[1][2] + dp[1][1] = 0 + 1 = 1
- j = 3 to 4: dp[2][j] = 0
-> Row 2 State: [1, 2, 1, 0, 0]

--- ROW 3: i = 3 (currentNum = nums[2] = 1) ---
- j = 0: dp[3][0] = 1
- j = 1: dp[3][1] = dp[2][1] + dp[2][0] = 2 + 1 = 3
- j = 2: dp[3][2] = dp[2][2] + dp[2][1] = 1 + 2 = 3
- j = 3: dp[3][3] = dp[2][3] + dp[2][2] = 0 + 1 = 1
- j = 4: dp[3][4] = 0
-> Row 3 State: [1, 3, 3, 1, 0]

--- ROW 4: i = 4 (currentNum = nums[3] = 1) ---
- j = 0: dp[4][0] = 1
- j = 1: dp[4][1] = dp[3][1] + dp[3][0] = 3 + 1 = 4
- j = 2: dp[4][2] = dp[3][2] + dp[3][1] = 3 + 3 = 6
- j = 3: dp[4][3] = dp[3][3] + dp[3][2] = 1 + 3 = 4
- j = 4: dp[4][4] = dp[3][4] + dp[3][3] = 0 + 1 = 1
-> Row 4 State: [1, 4, 6, 4, 1]

--- ROW 5: i = 5 (currentNum = nums[4] = 1) ---
- j = 0: dp[5][0] = 1
- j = 1: dp[5][1] = dp[4][1] + dp[4][0] = 4 + 1 = 5
- j = 2: dp[5][2] = dp[4][2] + dp[4][1] = 6 + 4 = 10
- j = 3: dp[5][3] = dp[4][3] + dp[4][2] = 4 + 6 = 10
- j = 4: dp[5][4] = dp[4][4] + dp[4][3] = 1 + 4 = 5
-> Row 5 State: [1, 5, 10, 10, 5]

--- RETURN PHASE ---
- Line 9 (return dp[n][W]): Reads dp[5][4] = 5 -> returns 5.

================================================================================
FULL DP TABLE VISUALIZATION (nums = [1, 1, 1, 1, 1], Target W = 4)
================================================================================

          j=0    j=1    j=2    j=3    j=4
i=0     [  1 ] [  0 ] [  0 ] [  0 ] [  0 ]
i=1 (1) [  1 ] [  1 ] [  0 ] [  0 ] [  0 ]
i=2 (1) [  1 ] [  2 ] [  1 ] [  0 ] [  0 ]
i=3 (1) [  1 ] [  3 ] [  3 ] [  1 ] [  0 ]
i=4 (1) [  1 ] [  4 ] [  6 ] [  4 ] [  1 ]
i=5 (1) [  1 ] [  5 ] [ 10 ] [ 10 ] [  5 ] <-- Final Result: dp[5][4] = 5
================================================================================
*/