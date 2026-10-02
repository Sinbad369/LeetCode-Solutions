/*
LEETCODE 416: PARTITION EQUAL SUBSET SUM
================================================================================

PROBLEM DESCRIPTION:
Given a non-empty array nums containing only positive integers, find if the
array can be partitioned into two subsets such that the sum of elements in
both subsets is equal.

MAPPING TO 0-1 KNAPSACK:

Total Sum = sum(nums)

If Total Sum is odd -> Impossible (return false)

Capacity W = Total Sum / 2

Items = array elements (each element's weight = value = nums[i])

Goal = Can we fill a knapsack of capacity W EXACTLY?

================================================================================
*/
class Solution {
    public boolean canPartition(int[] nums) {
        // Line 1: Calculate total sum of array elements
        int totalSum = 0;
        for (int num : nums) {
            totalSum += num;
        }

        // Line 2: Odd sum cannot be partitioned into two equal integer halves
        if (totalSum % 2 != 0) {
            return false;
        }

        // Line 3: Set target capacity W (half the sum) and total items n
        int target = totalSum / 2;
        int n = nums.length;

        // Line 4: Initialize 2D DP grid of size (n + 1) x (target + 1)
        boolean[][] dp = new boolean[n + 1][target + 1];

        // Line 5: Base Case - capacity 0 is always possible (empty subset)
        for (int i = 0; i <= n; i++) {
            dp[i][0] = true;
        }

        // Line 6: Fill inner DP grid row-by-row (Item-by-Item)
        for (int i = 1; i <= n; i++) {
            int currentNum = nums[i - 1]; // Translate 1-based loop index to 0-based array index

            for (int j = 1; j <= target; j++) {
                if (currentNum > j) {
                    // Line 7: Element too large -> EXCLUDE (Copy directly from ABOVE)
                    dp[i][j] = dp[i - 1][j];
                } else {
                    // Line 8: Element fits -> MAX(EXCLUDE, INCLUDE) via boolean OR
                    boolean exclude = dp[i - 1][j];
                    boolean include = dp[i - 1][j - currentNum];
                    dp[i][j] = exclude || include;
                }
            }
        }

        // Line 9: Answer is whether target capacity can be formed using any subset of n items
        return dp[n][target];
    }
}

/*
================================================================================
FULL LINE-BY-LINE CODE WALKTHROUGH (nums = [1, 5, 11, 5], Target W = 11)
================================================================================

--- INITIALIZATION PHASE ---
- Line 1 (totalSum calculation): Iterates nums -> totalSum = 1 + 5 + 11 + 5 = 22.
- Line 2 (odd check): 22 % 2 != 0 is false -> proceed.
- Line 3 (target setup): target = 22 / 2 = 11, n = 4.
- Line 4 (dp matrix creation): Creates dp[5][12] with all false values.
- Line 5 (base case): Sets Column 0 to true for all rows:
  dp[0][0] = T, dp[1][0] = T, dp[2][0] = T, dp[3][0] = T, dp[4][0] = T.

--- ROW 1: i = 1 (currentNum = nums[0] = 1) ---
Evaluating which target sums can be formed using ONLY the subset {1}:
- j = 1: currentNum <= j (1 <= 1) -> Line 8:
    exclude = dp[0][1] = F
    include = dp[0][1 - 1] = dp[0][0] = T
    dp[1][1] = F || T = T
- j = 2 to 11: currentNum <= j (1 <= j), but:
    exclude = dp[0][j] = F
    include = dp[0][j - 1] = F
    dp[1][j] = F || F = F
-> Row 1 State: [T, T, F, F, F, F, F, F, F, F, F, F]

--- ROW 2: i = 2 (currentNum = nums[1] = 5) ---
Evaluating which target sums can be formed using subsets from {1, 5}:
- j = 1 to 4: currentNum > j (5 > j) -> Line 7:
    Copy cell directly above: dp[2][j] = dp[1][j].
    So j=1 stays T, j=2,3,4 stay F.
- j = 5: currentNum <= j (5 <= 5) -> Line 8:
    exclude = dp[1][5] = F
    include = dp[1][5 - 5] = dp[1][0] = T
    dp[2][5] = F || T = T
- j = 6: currentNum <= j (5 <= 6) -> Line 8:
    exclude = dp[1][6] = F
    include = dp[1][6 - 5] = dp[1][1] = T
    dp[2][6] = F || T = T
- j = 7 to 11: Both exclude and include are F -> dp[2][j] = F.
-> Row 2 State: [T, T, F, F, F, T, T, F, F, F, F, F]

--- ROW 3: i = 3 (currentNum = nums[2] = 11) ---
Evaluating which target sums can be formed using subsets from {1, 5, 11}:
- j = 1 to 10:
    For j=1, 5, 6: exclude is T (from row 2 above) -> dp[3][j] = T.
    For other j < 11: currentNum > j (11 > j) -> Line 7: copy from dp[2][j] = F.
- j = 11: currentNum <= j (11 <= 11) -> Line 8:
    exclude = dp[2][11] = F
    include = dp[2][11 - 11] = dp[2][0] = T
    dp[3][11] = F || T = T  <-- TARGET REACHED!
-> Row 3 State: [T, T, F, F, F, T, T, F, F, F, F, T]

--- ROW 4: i = 4 (currentNum = nums[3] = 5) ---
Evaluating which target sums can be formed using all numbers {1, 5, 11, 5}:
- j = 10: currentNum <= j (5 <= 10) -> Line 8:
    exclude = dp[3][10] = F
    include = dp[3][10 - 5] = dp[3][5] = T
    dp[4][10] = F || T = T
- j = 11: currentNum <= j (5 <= 11) -> Line 8:
    exclude = dp[3][11] = T
    dp[4][11] = T || include = T
-> Row 4 State: [T, T, F, F, F, T, T, F, F, F, T, T]

--- RETURN PHASE ---
- Line 9 (return dp[n][target]): Reads dp[4][11] = true -> returns true.

================================================================================
FULL DP TABLE VISUALIZATION (nums = [1, 5, 11, 5], Target W = 11)
================================================================================

          j=0    j=1    j=2    j=3    j=4    j=5    j=6    j=7    j=8   j=9    j=10   j=11
i=0     [  T ] [  F ] [  F ] [  F ] [  F ] [  F ] [  F ] [  F ] [  F ] [  F ] [  F ] [  F ]
i=1 (1) [  T ] [  T ] [  F ] [  F ] [  F ] [  F ] [  F ] [  F ] [  F ] [  F ] [  F ] [  F ]
i=2 (5) [  T ] [  T ] [  F ] [  F ] [  F ] [  T ] [  T ] [  F ] [  F ] [  F ] [  F ] [  F ]
i=3 (11)[  T ] [  T ] [  F ] [  F ] [  F ] [  T ] [  T ] [  F ] [  F ] [  F ] [  F ] [  T ]
i=4 (5) [  T ] [  T ] [  F ] [  F ] [  F ] [  T ] [  T ] [  F ] [  F ] [  F ] [  T ] [  T ]
                                                                          Result: dp[4][11] = true
================================================================================
*/