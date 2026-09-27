/*
================================================================================
LEETCODE 322: COIN CHANGE (UNBOUNDED MIN-COST DP)
================================================================================

PROBLEM DESCRIPTION:
You are given an integer array `coins` representing coins of different denominations 
and an integer `amount` representing a total amount of money.

Return the fewest number of coins that you need to make up that amount. If that 
amount of money cannot be made up by any combination of the coins, return -1.

You may assume that you have an infinite number of each kind of coin.

EXAMPLES:
1. Input: coins = [1, 2, 5], amount = 11      -->  Output: 3
   Explanation: 11 = 5 + 5 + 1

2. Input: coins = [2], amount = 3             -->  Output: -1

3. Input: coins = [1], amount = 0             -->  Output: 0

================================================================================
CORE DP IDEA & TRANSITIONS:
================================================================================
We build a 1D DP table `dp[i]` where `dp[i]` represents the MINIMUM number of 
coins needed to form target amount `i`.

For each target amount `i` from 1 to `amount`, we try subtracting each available 
coin denomination `c` in `coins`:

If `i - c >= 0` and `dp[i - c]` is reachable:
   dp[i] = Math.min(dp[i], dp[i - c] + 1)
================================================================================
*/
class Solution {
    public int coinChange(int[] coins, int amount) {
        if(amount == 0){
            return 0;
        }
        /*
        ================================================================================
        STEP 1: INITIALIZE THE DP TABLE WITH INFINITY SENTINEL
        ================================================================================
        1. Table Dimensions:
           Sized (amount + 1) to store answers from 0 up to amount.

        2. Base Cases & Sentinel:
           - Fill all cells with a pseudo-infinity value `amount + 1`.
           - Set dp[0] = 0 (0 coins required to form amount 0).
        ================================================================================
        */
        int[] dp = new int[amount + 1];
        int INF = amount + 1;
        Arrays.fill(dp, INF);
        dp[0] = 0;

        /*
        ================================================================================
        STEP 2: BOTTOM-UP TABULATION (NESTED LOOPS)
        ================================================================================
        Outer loop iterates over each sub-amount i from 1 to amount.
        Inner loop checks every coin denomination c in coins.
        ================================================================================
        */

        for(int i = 0; i <= amount; i++){
            for(int coin : coins){
                if(i - coin >= 0){
                    dp[i] = Math.min(dp[i], dp[i -  coin] + 1);
                }
            }
        }

        /*
        ================================================================================
        STEP 3: RETURN RESULT OR UNREACHABLE SENTINEL (-1)
        ================================================================================
        If dp[amount] is still INF, it means amount cannot be formed -> return -1.
        Otherwise, return dp[amount].
        ================================================================================
        */
        return dp[amount] > amount ? -1 : dp[amount];
        
    }
}

/*
================================================================================
EXPLANATION & ANALYSIS
================================================================================

1. State Definitions:
   - `dp[i]`: Minimum number of coins needed to make amount `i`.

2. State Transitions:
   - dp[i] = min_{c in coins}(dp[i - c] + 1) for all c <= i.

3. Complexity Analysis:
   - Time Complexity: O(Amount * M) where M = number of coin denominations.
   - Space Complexity: O(Amount) auxiliary space to store the DP array.

================================================================================
DETAILED WALKTHROUGH (coins = [1, 3, 4], amount = 6):
================================================================================

INF = 7
dp array initialized to [0, 7, 7, 7, 7, 7, 7]

--- i = 1 ---
  c = 1: dp[1] = min(7, dp[0] + 1) = min(7, 1) = 1
  dp[1] = 1

--- i = 2 ---
  c = 1: dp[2] = min(7, dp[1] + 1) = min(7, 2) = 2
  dp[2] = 2

--- i = 3 ---
  c = 1: dp[3] = min(7, dp[2] + 1) = min(7, 3) = 3
  c = 3: dp[3] = min(3, dp[0] + 1) = min(3, 1) = 1
  dp[3] = 1

--- i = 4 ---
  c = 1: dp[4] = min(7, dp[3] + 1) = min(7, 2) = 2
  c = 3: dp[4] = min(2, dp[1] + 1) = min(2, 2) = 2
  c = 4: dp[4] = min(2, dp[0] + 1) = min(2, 1) = 1
  dp[4] = 1

--- i = 5 ---
  c = 1: dp[5] = min(7, dp[4] + 1) = min(7, 2) = 2
  c = 3: dp[5] = min(2, dp[2] + 1) = min(2, 3) = 2
  c = 4: dp[5] = min(2, dp[1] + 1) = min(2, 2) = 2
  dp[5] = 2

--- i = 6 ---
  c = 1: dp[6] = min(7, dp[5] + 1) = min(7, 3) = 3
  c = 3: dp[6] = min(3, dp[3] + 1) = min(3, 2) = 2
  c = 4: dp[6] = min(2, dp[2] + 1) = min(2, 3) = 2
  dp[6] = 2

Result: dp[6] = 2 (formed by two 3-value coins: 3 + 3 = 6)

================================================================================
GRAPHICAL STEP-BY-STEP WALKTHROUGH (coins = [1, 3, 4], amount = 6)
================================================================================

--------------------------------------------------------------------------------
STEP 0: INITIAL ARRAY WITH SENTINELS
--------------------------------------------------------------------------------
   Amount i : [ 0 ] [ 1 ] [ 2 ] [ 3 ] [ 4 ] [ 5 ] [ 6 ]
   dp[i]    : [ 0 ] [ ∞ ] [ ∞ ] [ ∞ ] [ ∞ ] [ ∞ ] [ ∞ ]


--------------------------------------------------------------------------------
STEP 1: FILLING DP ARRAY FOR EACH AMOUNT i
--------------------------------------------------------------------------------
  i = 1 ---> coin 1               => dp[1] = 1
  i = 2 ---> coin 1               => dp[2] = 2
  i = 3 ---> coin 1, 3            => dp[3] = min(3, 1) = 1
  i = 4 ---> coin 1, 3, 4         => dp[4] = min(2, 2, 1) = 1
  i = 5 ---> coin 1, 3, 4         => dp[5] = min(2, 3, 2) = 2
  i = 6 ---> coin 1, 3, 4         => dp[6] = min(3, 2, 3) = 2


--------------------------------------------------------------------------------
STEP 2: FINAL DP ARRAY VISUALIZATION
--------------------------------------------------------------------------------
   Amount i : [ 0 ] [ 1 ] [ 2 ] [ 3 ] [ 4 ] [ 5 ] [ 6 ]
   dp[i]    : [ 0 ] [ 1 ] [ 2 ] [ 1 ] [ 1 ] [ 2 ] [ 2 ] <-- Final Answer: dp[6] = 2
================================================================================
*/