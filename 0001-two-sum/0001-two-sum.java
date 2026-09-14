import java.util.HashMap;
import java.util.Map;
/*
================================================================================
INPUT ENHANCEMENT & HASH MAP LOOKUP (O(N) SPACE-FOR-TIME)
================================================================================
Brute Force vs. Hash Map Trade-Off:
--------------------------------------------------------------------------------
1. Brute Force (Nested Loops): 
   Compares every pair (i, j). Time Complexity = O(N^2), Space = O(1).
   Matches slow array search.

2. Hash Map (Pre-Structuring / Input Enhancement):
   Stores previously seen elements in a Hash Map (value -> index).
   As we traverse, we check if (target - current_val) exists in O(1) time.
   Time Complexity = O(N), Space = O(N).
================================================================================
*/
class Solution {
    public int[] twoSum(int[] nums, int target) {
        // map to store: key = number val, value = array index
        Map<Integer, Integer> map = new HashMap<>();

        for(int i = 0; i < nums.length; i++){
            int complement = target - nums[i];

            // check if complement was already processed in O(1) average time
            if(map.containsKey(complement)){
                return new int[] {map.get(complement), i};
            }

            // store the current number and its index into the hash map
            map.put(nums[i], i);
        }

        return new int[] {}; // fallback return
    }
}

/*--
Explanation:

1. Initialization:
   - A `HashMap` is initialized to keep track of elements seen so far and their 
     corresponding array indices.

2. Single Pass Execution:
   - For each element `nums[i]`, we calculate `complement = target - nums[i]`.
   - If `map.containsKey(complement)` returns true, we immediately return the array 
     `[map.get(complement), i]`.
   - Otherwise, we store `map.put(nums[i], i)` so future elements can reference it.
--*/

/*--
Complexity Analysis:

- Time Complexity: O(N) single pass through the array. Hash Map lookups take 
  O(1) average time.
- Space Complexity: O(N) to store up to N elements in the Hash Map.
--*/

/**
WALKTHROUGH (Tracing Example 1: nums = [2, 7, 11, 15], target = 9):

Initialization: map = {}

Step 1: i = 0, nums[0] = 2
  - complement = 9 - 2 = 7
  - map.containsKey(7) -> false
  - map.put(2, 0)      -> map = {2: 0}

Step 2: i = 1, nums[1] = 7
  - complement = 9 - 7 = 2
  - map.containsKey(2) -> true! Found key 2 at index map.get(2) = 0
  - return new int[] { 0, 1 }

Result: [0, 1] returned in O(N) time without secondary loop.

WALKTHROUGH 2 (Handling Duplicate Numbers: nums = [3, 3], target = 6):

Initialization: map = {}

Step 1: i = 0, nums[0] = 3
  - complement = 6 - 3 = 3
  - map.containsKey(3) -> false (map is empty)
  - map.put(3, 0)      -> map = {3: 0}

Step 2: i = 1, nums[1] = 3
  - complement = 6 - 3 = 3
  - map.containsKey(3) -> true! Found key 3 at index map.get(3) = 0
  - return new int[] { 0, 1 }

Result: [0, 1] returned cleanly. (Checking BEFORE putting avoids overwriting index 0).

WALKTHROUGH 3 (Match at the End of Array: nums = [3, 2, 4], target = 6):

Initialization: map = {}

Step 1: i = 0, nums[0] = 3
  - complement = 6 - 3 = 3
  - map.containsKey(3) -> false
  - map.put(3, 0)      -> map = {3: 0}

Step 2: i = 1, nums[1] = 2
  - complement = 6 - 2 = 4
  - map.containsKey(4) -> false
  - map.put(2, 1)      -> map = {3: 0, 2: 1}

Step 3: i = 2, nums[2] = 4
  - complement = 6 - 4 = 2
  - map.containsKey(2) -> true! Found key 2 at index map.get(2) = 1
  - return new int[] { 1, 2 }

Result: [1, 2] returned in a single pass!

WALKTHROUGH 4 (Longer Array Trace: nums = [11, 15, 3, 8, 2, 7], target = 9):

Initialization: map = {}

Step 1: i = 0, nums[0] = 11
  - complement = 9 - 11 = -2
  - map.containsKey(-2) -> false
  - map.put(11, 0)      -> map = {11: 0}

Step 2: i = 1, nums[1] = 15
  - complement = 9 - 15 = -6
  - map.containsKey(-6) -> false
  - map.put(15, 1)      -> map = {11: 0, 15: 1}

Step 3: i = 2, nums[2] = 3
  - complement = 9 - 3 = 6
  - map.containsKey(6)  -> false
  - map.put(3, 2)       -> map = {11: 0, 15: 1, 3: 2}

Step 4: i = 3, nums[3] = 8
  - complement = 9 - 8 = 1
  - map.containsKey(1)  -> false
  - map.put(8, 3)       -> map = {11: 0, 15: 1, 3: 2, 8: 3}

Step 5: i = 4, nums[4] = 2
  - complement = 9 - 2 = 7
  - map.containsKey(7)  -> false
  - map.put(2, 4)       -> map = {11: 0, 15: 1, 3: 2, 8: 3, 2: 4}

Step 6: i = 5, nums[5] = 7
  - complement = 9 - 7 = 2
  - map.containsKey(2)  -> true! Found key 2 at index map.get(2) = 4
  - return new int[] { 4, 5 }

Result: Returns [4, 5] in 6 steps (O(N)) instead of 15 comparisons (O(N^2)).
*/