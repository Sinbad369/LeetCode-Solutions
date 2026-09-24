/*
================================================================================
LEETCODE 312: BURST BALLOONS (INTERVAL / RANGE DYNAMIC PROGRAMMING)
================================================================================

PROBLEM DESCRIPTION:
You are given n balloons, indexed from 0 to n - 1. Each balloon is painted 
with a number on it represented by an array 'nums'. You are asked to burst all 
the balloons.

If you burst the i-th balloon, you will get:
    coins = nums[i - 1] * nums[i] * nums[i + 1]

If i - 1 or i + 1 goes out of bounds, treat it as if there is a balloon with 
a 1 painted on it. Return the maximum coins you can collect.

EXAMPLES:
1. Input: nums = [3, 1, 5, 8]  -->  Output: 167
   Explanation:
   nums = [3, 1, 5, 8]  --> [3, 5, 8]  --> [3, 8]  --> [8]  --> []
   coins = (3 * 1 * 5)   +  (3 * 5 * 8) + (1 * 3 * 8) + (1 * 8 * 1) = 167

2. Input: nums = [1, 5]        -->  Output: 10
   Explanation:
   Burst 1 first (1*1*5=5), then 5 (1*5*1=5) -> Total = 10.

================================================================================
MCM CONNECTION & KEY IDEA:
================================================================================
Instead of choosing which balloon to burst FIRST (which dynamically changes 
neighbors), we think IN REVERSE:
    "Which balloon k is the LAST balloon to burst in interval (i, j)?"

If balloon k is the LAST one standing between i and j:
1. All other balloons between i and j are already burst.
2. The remaining standing neighbors of k are padded[i] and padded[j].
3. Coins earned from popping k last = padded[i] * padded[k] * padded[j].

This mirrors Matrix Chain Multiplication (Lecture 9):
    dp[i][j] = max(dp[i][k] + dp[k][j] + padded[i] * padded[k] * padded[j])
    (where split point k ranges from i + 1 to j - 1)
================================================================================
*/

class Solution {
    public int maxCoins(int[] nums) {
        if(nums == null || nums.length == 0){
            return 0;
        }
        
        /*
        ================================================================================
        WHY WE PAD THE ARRAY WITH 1s (STEP 1):
        ================================================================================
        1. Enforces Problem Rules:
        The problem specifies that if a balloon lacks a left or right neighbor 
        (out of bounds), it must be treated as a balloon with value 1.

        2. Serves as Fixed Boundaries:
        Index 0 and Index (n + 1) act as permanent anchors for the DP interval (i, j). 
        When balloon 'k' is the LAST balloon burst in range (i, j), its remaining 
        standing neighbors are guaranteed to be padded[i] and padded[j].

        3. Eliminates Out-of-Bounds Checks:
        Prevents writing repetitive conditional checks like:
        `int left = (k - 1 < 0) ? 1 : nums[k - 1];`
        Instead, we cleanly compute:
        `padded[i] * padded[k] * padded[j]`
        ================================================================================
        */ 

        // Step 1: pad the original array with 1 at both boundaries
        int n = nums.length;
        int[] padded = new int[n + 2];
        padded[0] = 1;// left boundary anchor
        padded[n+1] = 1;// right boundary anchor
        for(int i = 0; i < n; i++){
            padded[i+1] = nums[i];// copying original values into indices 1 to n
        }

        /*
        ================================================================================
        STEP 2: INITIALIZE THE 2D DP TABLE
        ================================================================================
        1. Table Dimensions:
        The table is sized (numBalloons x numBalloons), where numBalloons = n + 2.
        This covers all indices from 0 (padded left anchor) to n + 1 (padded right anchor).

        2. Value Meaning (`dp[i][j]`):
        `dp[i][j]` represents the MAXIMUM coins collected by bursting ALL balloons 
        STRICTLY BETWEEN index `i` and index `j`. 

        3. Boundaries are Excluded:
        The balloons at index `i` and index `j` are NOT burst in `dp[i][j]`. 
        They act as the surrounding wall/neighbors for the inner range (i, j).

        4. Base Case (Implicit Initialization):
        In Java, `new int[numBalloons][numBalloons]` automatically initializes all cells to 0.
        - `dp[i][i] = 0` (an empty interval has 0 coins).
        - `dp[i][i + 1] = 0` (an interval with 0 inner balloons yields 0 coins).
        ================================================================================
        */
        int numBallons = padded.length;
        int[][] dp = new int[numBallons][numBallons];

        /*
        ================================================================================
        STEP 3: BOTTOM-UP TABLE FILLING BY INTERVAL LENGTH (3 NESTED LOOPS)
        ================================================================================
        To solve larger ranges, we MUST solve smaller sub-ranges first!
        This is why the loops are ordered by interval length (len) from smallest to largest.

        1. Outer Loop (`len`): 
        Controls the distance between left boundary `i` and right boundary `j`.
        - `len = 2` means 1 inner balloon to consider (e.g., range (0, 2) has k = 1).
        - `len` increases up to `numBalloons - 1` (the full padded range).

        2. Middle Loop (`i`): 
        Moves the left boundary `i` from index 0 across the array.
        - Right boundary `j` is automatically calculated as `j = i + len`.

        3. Inner Loop (`k`): 
        Tries every possible balloon `k` between `i` and `j` as the LAST balloon to burst!
        - Thinking in REVERSE: If `k` is the LAST balloon burst in range (i, j), then:
            a) All balloons between `i` and `k` are already burst (`dp[i][k]`).
            b) All balloons between `k` and `j` are already burst (`dp[k][j]`).
            c) The only standing neighbors remaining for `k` are `padded[i]` and `padded[j]`.
        - Coins gained from bursting `k` last = `padded[i] * padded[k] * padded[j]`.

        4. Transition Formula:
        `dp[i][j] = max(dp[i][j], dp[i][k] + dp[k][j] + (padded[i] * padded[k] * padded[j]))`
        ================================================================================
        */

        // Outer loop: interval length from 2 up to numBallons - 1
        for(int len = 2; len < numBallons; len++){
            
            // Middle loop: left boundary index i
            for(int i = 0; i < numBallons - len; i++){
                int j = i + len; // Right boundary index j

                // Inner loop: try every inner ballon k as the LAST balloon to burst
                for(int k = i + 1; k < j; k++){
                    // 1. coins from bursting left sub-interval (i to k)
                    int leftSubproblem = dp[i][k];

                    // 2. coins from bursting right sub-interval (k to j)
                    int rightSubproblem = dp[k][j];

                    // 3. coins from bursting k LAST (surrounding by remaining boundaries i and j)
                    int lastPopCoins = padded[i] * padded[k] * padded[j];

                    // total coins for this split choice k
                    int totalCoins = leftSubproblem + rightSubproblem + lastPopCoins;

                    // keep the maximum results accross all choices for k
                    dp[i][j] = Math.max(dp[i][j], totalCoins);
                }
            }
            
        }

        /*
        ================================================================================
        STEP 4: RETURN THE FINAL RESULT
        ================================================================================
        The problem asks for the maximum coins collected by bursting ALL original balloons.
        - Original balloons sit between index 1 and index n.
        - Including padded boundary anchors, our range is from index 0 to index (numBalloons - 1).
        - Therefore, `dp[0][numBalloons - 1]` contains the optimal solution for the whole array!
        ================================================================================
        */
        return dp[0][numBallons - 1];
    }
}

/*--
================================================================================
EXPLANATION & ANALYSIS
================================================================================

1. State Definitions:
   - `dp[i][j]`: Maximum coins collected by bursting all balloons strictly 
     between index `i` and index `j` (excluding boundaries `i` and `j`).

2. State Transitions:
   - `dp[i][j] = max(dp[i][k] + dp[k][j] + padded[i] * padded[k] * padded[j])`
     for all `k` where `i < k < j`.

3. Complexity Analysis:
   - Time Complexity: O(N^3) where N is the length of original array 
     (3 nested loops: range length, left index i, split choice k).
   - Space Complexity: O(N^2) auxiliary space for the 2D DP table.

================================================================================
DETAILED WALKTHROUGH (nums = [3, 1, 5]):
================================================================================

Padded Array: padded = [1, 3, 1, 5, 1]  (indices 0 to 4)

--- Length 2 (No inner balloons to burst) ---
dp[0][2] = 0,  dp[1][3] = 0,  dp[2][4] = 0

--- Length 3 (1 inner balloon) ---
Range (0, 2), k = 1 (burst 3 last):
  dp[0][2] = dp[0][1] + dp[1][2] + (1 * 3 * 1) = 0 + 0 + 3 = 3

Range (1, 3), k = 2 (burst 1 last):
  dp[1][3] = dp[1][2] + dp[2][3] + (3 * 1 * 5) = 0 + 0 + 15 = 15

Range (2, 4), k = 3 (burst 5 last):
  dp[2][4] = dp[2][3] + dp[3][4] + (1 * 5 * 1) = 0 + 0 + 5 = 5

--- Length 4 (2 inner balloons) ---
Range (0, 3):
  Try k = 1: dp[0][1] + dp[1][3] + (1 * 3 * 5) = 0 + 15 + 15 = 30
  Try k = 2: dp[0][2] + dp[2][3] + (1 * 1 * 5) = 3 + 0 + 5 = 8
  dp[0][3] = max(30, 8) = 30

Range (1, 4):
  Try k = 2: dp[1][2] + dp[2][4] + (3 * 1 * 1) = 0 + 5 + 3 = 8
  Try k = 3: dp[1][3] + dp[3][4] + (3 * 5 * 1) = 15 + 0 + 15 = 30
  dp[1][4] = max(8, 30) = 30

--- Length 5 (Full Range 0 to 4, 3 inner balloons) ---
Range (0, 4):
  Try k = 1: dp[0][1] + dp[1][4] + (1 * 3 * 1) = 0 + 30 + 3 = 33
  Try k = 2: dp[0][2] + dp[2][4] + (1 * 1 * 1) = 3 + 5 + 1 = 9
  Try k = 3: dp[0][3] + dp[3][4] + (1 * 5 * 1) = 30 + 0 + 5 = 35

Result: dp[0][4] = 35
--*/

/*
================================================================================
GRAPHICAL STEP-BY-STEP WALKTHROUGH (nums = [3, 1, 5])
================================================================================

--------------------------------------------------------------------------------
STEP 0: PADDED ARRAY SETUP
--------------------------------------------------------------------------------
Index:    [0]   [1]   [2]   [3]   [4]
Value:   | 1 | | 3 | | 1 | | 5 | | 1 |
          ^                       ^
     Left Anchor             Right Anchor


--------------------------------------------------------------------------------
STEP 1: BASE CASE — LENGTH 2 (len = 2)
Ranges contain 0 inner balloons to burst.
--------------------------------------------------------------------------------
Sub-range (0, 2): [1, 3, 1] ----> No inner balloons to burst ----> dp[0][2] = 0
Sub-range (1, 3): [3, 1, 5] ----> No inner balloons to burst ----> dp[1][3] = 0
Sub-range (2, 4): [1, 5, 1] ----> No inner balloons to burst ----> dp[2][4] = 0


--------------------------------------------------------------------------------
STEP 2: LENGTH 3 (len = 3)
Ranges contain 1 inner balloon (k).
--------------------------------------------------------------------------------

Range (0, 2) — Inner Balloon k = 1 (value 3):
  [1]     (3)     [1]
  i=0     k=1     j=2

  Coins = dp[0][1] + dp[1][2] + (1 * 3 * 1)
        =     0    +    0     +     3       = 3

  RESULT: dp[0][2] = 3

Range (1, 3) — Inner Balloon k = 2 (value 1):
  [3]     (1)     [5]
  i=1     k=2     j=3

  Coins = dp[1][2] + dp[2][3] + (3 * 1 * 5)
        =     0    +    0     +     15      = 15

  RESULT: dp[1][3] = 15

Range (2, 4) — Inner Balloon k = 3 (value 5):
  [1]     (5)     [1]
  i=2     k=3     j=4

  Coins = dp[2][3] + dp[3][4] + (1 * 5 * 1)
        =     0    +    0     +     5       = 5

  RESULT: dp[2][4] = 5


--------------------------------------------------------------------------------
STEP 3: LENGTH 4 (len = 4)
Ranges contain 2 inner balloons. Test both choices for k.
--------------------------------------------------------------------------------

Range (0, 3) — Inner Balloons {1, 2} (values 3, 1):

Choice A: Burst k=1 (value 3) LAST
  [1]     (3)     . . .     [5]
  i=0     k=1               j=3
           |                 |
     (bursts last)   (already burst: dp[1][3]=15)

  Coins = dp[0][1] + dp[1][3] + (1 * 3 * 5)
        =     0    +    15    +     15      = 30

Choice B: Burst k=2 (value 1) LAST
  [1]     . . .     (1)     [5]
  i=0               k=2     j=3
   |                 |
(already burst:     (bursts last)
 dp[0][2]=3)

  Coins = dp[0][2] + dp[2][3] + (1 * 1 * 5)
        =     3    +    0     +      5      = 8

  BEST CHOICE: max(30, 8) ---> dp[0][3] = 30


Range (1, 4) — Inner Balloons {2, 3} (values 1, 5):

Choice A: Burst k=2 (value 1) LAST
  [3]     (1)     . . .     [1]
  i=1     k=2               j=4

  Coins = dp[1][2] + dp[2][4] + (3 * 1 * 1)
        =     0    +    5     +      3      = 8

Choice B: Burst k=3 (value 5) LAST
  [3]     . . .     (5)     [1]
  i=1               k=3     j=4

  Coins = dp[1][3] + dp[3][4] + (3 * 5 * 1)
        =     15   +    0     +     15      = 30

  BEST CHOICE: max(8, 30) ---> dp[1][4] = 30


--------------------------------------------------------------------------------
STEP 4: LENGTH 5 (len = 5) — Full Range (0, 4)
Ranges contain all 3 original balloons {1, 2, 3} (values 3, 1, 5).
--------------------------------------------------------------------------------

Choice A: Burst k=1 (value 3) LAST
  [1]     (3)     . . . . . . . .     [1]
  i=0     k=1                         j=4
           |                           |
     (bursts last)             (already burst sub-range:
                                dp[1][4] = 30)

  Coins = dp[0][1] + dp[1][4] + (1 * 3 * 1)
        =     0    +    30    +      3      = 33


Choice B: Burst k=2 (value 1) LAST
  [1]     . . . . .     (1)     . . . . .     [1]
  i=0                   k=2                   j=4
   |                     |                     |
(already burst:     (bursts last)       (already burst:
 dp[0][2] = 3)                           dp[2][4] = 5)

  Coins = dp[0][2] + dp[2][4] + (1 * 1 * 1)
        =     3    +    5     +      1      = 9


Choice C: Burst k=3 (value 5) LAST
  [1]     . . . . . . . .     (5)     [1]
  i=0                         k=3     j=4
   |                           |
(already burst sub-range:     (bursts last)
 dp[0][3] = 30)

  Coins = dp[0][3] + dp[3][4] + (1 * 5 * 1)
        =     30   +    0     +      5      = 35

  BEST CHOICE: max(33, 9, 35) ---> dp[0][4] = 35


--------------------------------------------------------------------------------
STEP 5: FINAL DP TABLE VISUALIZATION
--------------------------------------------------------------------------------
      j=0    j=1    j=2    j=3    j=4
i=0  [ 0 ][  0  ][  3  ][  30 ][  35 ]  <-- Final Answer: dp[0][4] = 35
i=1  [ - ][  0  ][  0  ][  15 ][  30 ]
i=2  [ - ][  -  ][  0  ][  0  ][  5  ]
i=3  [ - ][  -  ][  -  ][  0  ][  0  ]
i=4  [ - ][  -  ][  -  ][  -  ][  0  ]
================================================================================
*/