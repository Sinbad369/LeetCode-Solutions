/*
================================================================================
LEETCODE 55: JUMP GAME (GREEDY / REACHABLE INDEX TRACKING)
================================================================================

PROBLEM DESCRIPTION:
You are given an integer array `nums`. You are initially positioned at the array's 
first index, and each element in the array represents your maximum jump length 
at that position.

Return `true` if you can reach the last index, or `false` otherwise.

================================================================================
GREEDY STRATEGY:
================================================================================
Instead of exploring all possible jump combinations (like DP/Backtracking), we 
greedily maintain the maximum reachable index (`maxReach`) at any given point.

Greedy Choice Property:
- As we iterate through index `i`, if `i` is within `maxReach` (meaning we can 
  physically get to index `i`), we greedily expand our reach:
  
      maxReach = max(maxReach, i + nums[i])

- If at any point `i > maxReach`, we are stuck at a 0-jump wall and can never 
  progress further -> Return `false`.
- If `maxReach >= nums.length - 1`, we can guarantee reaching the end -> Return `true`.

Time Complexity:  O(N) - single pass through the array.
Space Complexity: O(1) auxiliary space.
================================================================================
*/
class Solution {
    public boolean canJump(int[] nums) {
        // line1: maxReach tracks the furthest index reachable so far
        int maxReach = 0;

        // line2: iterate through each index in the array
        for(int i = 0; i < nums.length; i++){
            // line3: if current index is beyond furthest reachable position, we are stuck
            if(i > maxReach){
                return false;
            }

            // line4: greedily update maxReach with maximum jump potential from position i
            maxReach = Math.max(maxReach, i + nums[i]);

            // line5: early exit if we can already reach or cross the last index
            if(maxReach >= nums.length - 1){
                return true;
            }
        }

        return false;
        
    }
}

/*
================================================================================
FULL LINE-BY-LINE CODE WALKTHROUGH
nums = [2, 3, 1, 0, 4]
================================================================================

--- INITIALIZATION PHASE ---
- Line 1: maxReach = 0
- Target Index = 4 (nums.length - 1)

--- GREEDY TRACING ---

- Index i = 0 (nums[0] = 2):
  * Check: i (0) > maxReach (0) -> FALSE
  * Line 4: maxReach = max(0, 0 + 2) = 2
  * Line 5: maxReach (2) >= 4 -> FALSE

- Index i = 1 (nums[1] = 3):
  * Check: i (1) > maxReach (2) -> FALSE
  * Line 4: maxReach = max(2, 1 + 3) = 4
  * Line 5: maxReach (4) >= 4 -> TRUE! (Early exit)

--- RETURN PHASE ---
- Returns true (We can reach the last index!).

================================================================================
STEP-BY-STEP TRACE TABLE
================================================================================

 Index i | nums[i] | Jump Range (i + nums[i]) | maxReach Update       | Can Reach End?
---------+---------+--------------------------+-----------------------+----------------
 Start   | -       | -                        | maxReach = 0          | False
 0       | 2       | 0 + 2 = 2                | max(0, 2) = 2         | False
 1       | 3       | 1 + 3 = 4                | max(2, 4) = 4         | True (Return!)
================================================================================
*/