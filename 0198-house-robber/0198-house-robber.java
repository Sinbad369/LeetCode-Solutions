/*
================================================================================
DYNAMIC PROGRAMMING: HOUSE ROBBER (1D ALTERNATING CHOICE)
================================================================================
*/
class Solution {
    public int rob(int[] nums) {
        if(nums == null || nums.length == 0){
            return 0;
        }

        if(nums.length == 1){
            return nums[0];
        }

        // DP STATE: track the maximum loot at the house i - 2 and i - 1
        int prev2 = 0;
        int prev1 = 0;

        for(int num : nums){
            // way1: skip current house -> keep prev1
            // way2: rob current house -> prev2 + current value
            int current = Math.max(prev1, prev2 + num);

            // shift the pointers for the next iteration
            prev2 = prev1;
            prev1 = current;
        }

        return prev1;

    }
}

/*--
Explanation:

1. State Transition:
   - At each house `i`, you face a binary choice:
     * Skip house `i`: Maximum money remains `dp[i-1]`.
     * Rob house `i`: Add its value `nums[i]` to `dp[i-2]`.
   - Formula: `dp[i] = Math.max(dp[i-1], dp[i-2] + nums[i])`.

2. O(1) Space Optimization:
   - Since calculating state `i` only requires states `i-1` and `i-2`, we replace
     the full DP array with two integer registers (`prev1`, `prev2`).
--*/

/*--
Complexity Analysis:

- Time Complexity: O(N) where N is the number of houses.
- Space Complexity: O(1) auxiliary space.

--*/

/**
WALKTHROUGH 1 (nums = [2, 7, 9, 3, 1]):

Initialization:
  prev2 = 0, prev1 = 0

Iteration 1 (num = 2):
  current = Math.max(0, 0 + 2) = 2
  prev2 = 0, prev1 = 2

Iteration 2 (num = 7):
  current = Math.max(2, 0 + 7) = 7
  prev2 = 2, prev1 = 7

Iteration 3 (num = 9):
  current = Math.max(7, 2 + 9) = 11
  prev2 = 7, prev1 = 11

Iteration 4 (num = 3):
  current = Math.max(11, 7 + 3) = 11
  prev2 = 11, prev1 = 11

Iteration 5 (num = 1):
  current = Math.max(11, 11 + 1) = 12
  prev2 = 11, prev1 = 12

Resulting Maximum Loot: 12
*/