/*
================================================================================
LEETCODE 1049: LAST STONE WEIGHT II
================================================================================

PROBLEM DESCRIPTION:
You are given an array of integers `stones` where `stones[i]` is the weight of the ith stone.

We are playing a game with the stones. On each step, we choose any two stones and 
smash them together. Suppose the stones have weights x and y with x <= y. 
The result of this smash is:
- If x == y, both stones are destroyed.
- If x != y, the stone of weight x is destroyed, and the stone of weight y 
  has new weight y - x.

At the end of the game, there is at most one stone left.
Return the smallest possible weight of the left stone (or 0 if no stones are left).

EXAMPLE:
Input: stones = [2, 7, 4, 1, 8, 1]
Output: 1
Explanation:
- Smash 7 and 8 -> 1 remaining: stones = [2, 4, 1, 1, 1]
- Smash 2 and 4 -> 2 remaining: stones = [2, 1, 1, 1]
- Smash 2 and 1 -> 1 remaining: stones = [1, 1, 1]
- Smash 1 and 1 -> 0 remaining: stones = [1]
- Final weight = 1.

================================================================================
MATHEMATICAL TRANSFORMATION TO 0-1 KNAPSACK:
================================================================================
Smashing two stones `x` and `y` to get `y - x` is equivalent to dividing all stones 
into two groups (Subsets A and B) and subtracting their total sums:

          Minimal Leftover Weight = sum(Group A) - sum(Group B)

To make this difference as SMALL as possible, `sum(Group B)` should be as CLOSE 
to `totalSum / 2` as possible without exceeding it!

So the problem reduces to:
"Find the maximum weight we can pack into a 0-1 Knapsack of target capacity W = totalSum / 2."

Final Answer = totalSum - 2 * dp[n][W]
================================================================================
*/
class Solution {
    public int lastStoneWeightII(int[] stones) {
        // line1 - calc the total sum of all stones
        int totalSum = 0;
        for (int stone : stones){
            totalSum += stone;
        }

        // line2 - set knapsack target capacity W (half of totalSum) and total stones n
        int W = totalSum / 2;
        int n = stones.length;

        // line3 - initialize 2d dp grid of (n + 1) x (W + 1)
        int[][] dp = new int[n + 1][W + 1];

        // line4 - fill 2d grid row-by-row(stone-by-stone)
        for(int i = 1; i <= n; i++){
            int currentStone = stones[i - 1];

            for(int j = 1; j <= W; j++){
                if(currentStone > j){
                    // line5 - stone is too heavy ---> exclude (copy directly from above)
                    dp[i][j] = dp[i - 1][j];
                } else{
                    // line6 - stone fits ===> max(exclude, include)
                    int exclude = dp[i - 1][j];
                    int include = currentStone + dp[i - 1][j - currentStone];
                    dp[i][j] = Math.max(exclude, include);
                }
            }
        }

        // line7 - minimum remaining weight = totalSum - 2 * maxSubsetSum
        return totalSum - 2 * dp[n][W];
    }
}

/*
================================================================================
FULL LINE-BY-LINE CODE WALKTHROUGH (stones = [2, 7, 4, 1, 8, 1])
================================================================================

--- INITIALIZATION PHASE ---
- Line 1 (totalSum calculation): totalSum = 2 + 7 + 4 + 1 + 8 + 1 = 23.
- Line 2 (target setup): W = 23 / 2 = 11, n = 6.
- Line 3 (dp matrix creation): Creates dp[7][12] initialized with 0.

--- ROW-BY-ROW TRACING ---
- Row 1 (stone = 2):
    Can form max weight 2 for all capacities j >= 2.
    Row 1 State: [0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2]

- Row 2 (stone = 7):
    Combines with stone 2.
    j = 7 to 8: max(2, 7 + 0) = 7
    j = 9 to 11: max(2, 7 + dp[1][j-7]=2) = 9
    Row 2 State: [0, 0, 2, 2, 2, 2, 2, 7, 7, 9, 9, 9]

- Row 3 (stone = 4):
    j = 4 to 5: 4
    j = 6: 6 (4 + 2)
    j = 7 to 8: 7
    j = 9 to 10: 9
    j = 11: max(9, 4 + dp[2][7]=7) = 11  <-- TARGET CAPACITY 11 REACHED!
    Row 3 State: [0, 0, 2, 2, 4, 4, 6, 7, 7, 9, 9, 11]

- Row 4 (stone = 1):
    Increments available sums using stone 1.
    Row 4 State: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]

- Row 5 (stone = 8):
    Row 5 State: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]

- Row 6 (stone = 1):
    Row 6 State: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]

--- RETURN PHASE ---
- Line 7 (return formula):
    dp[6][11] = 11 (Max stone sum in group 1 <= 11)
    totalSum = 23
    Result = 23 - 2 * 11 = 23 - 22 = 1.

================================================================================
FULL DP TABLE VISUALIZATION (stones = [2, 7, 4, 1, 8, 1], Target W = 11)
================================================================================

          j=0    j=1    j=2    j=3    j=4    j=5    j=6    j=7    j=8    j=9   j=10   j=11
i=0     [  0 ] [  0 ] [  0 ] [  0 ] [  0 ] [  0 ] [  0 ] [  0 ] [  0 ] [  0 ] [  0 ] [  0 ]
i=1 (2) [  0 ] [  0 ] [  2 ] [  2 ] [  2 ] [  2 ] [  2 ] [  2 ] [  2 ] [  2 ] [  2 ] [  2 ]
i=2 (7) [  0 ] [  0 ] [  2 ] [  2 ] [  2 ] [  2 ] [  2 ] [  7 ] [  7 ] [  9 ] [  9 ] [  9 ]
i=3 (4) [  0 ] [  0 ] [  2 ] [  2 ] [  4 ] [  4 ] [  6 ] [  7 ] [  7 ] [  9 ] [  9 ] [ 11 ]
i=4 (1) [  0 ] [  1 ] [  2 ] [  3 ] [  4 ] [  5 ] [  6 ] [  7 ] [  8 ] [  9 ] [ 10 ] [ 11 ]
i=5 (8) [  0 ] [  1 ] [  2 ] [  3 ] [  4 ] [  5 ] [  6 ] [  7 ] [  8 ] [  9 ] [ 10 ] [ 11 ]
i=6 (1) [  0 ] [  1 ] [  2 ] [  3 ] [  4 ] [  5 ] [  6 ] [  7 ] [  8 ] [  9 ] [ 10 ] [ 11 ] <-- dp[6][11] = 11
================================================================================
*/