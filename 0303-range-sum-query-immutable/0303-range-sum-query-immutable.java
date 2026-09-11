/*
================================================================================
WHY USE PREFIX SUMS? (Input Enhancement Trade-Off)
================================================================================
Without Prefix Sums (Brute Force):
- Every time sumRange(left, right) is called, we must run a loop from left to right.
- Time per query: O(N)
- If we have Q queries, Total Time = O(Q * N).
- Example: N = 1,000,000 and Q = 100,000 -> 100 BILLION operations (Time Limit Exceeded).

With Prefix Sums (Input Enhancement):
- We pay an upfront cost of O(N) time and O(N) space during initialization.
- Every subsequent query is calculated in O(1) time using: prefixSum[right + 1] - prefixSum[left].
- Total Time = O(N + Q) -> Only 1.1 MILLION operations!

--------------------------------------------------------------------------------
COMPARISON TABLE:
--------------------------------------------------------------------------------
Metric                  | Brute Force Loop         | Prefix Sum Preprocessing
------------------------+--------------------------+----------------------------
Query Strategy          | Loop from left to right  | Single subtraction: C[R+1] - C[L]
Time per Query          | O(N)                     | O(1)
Total Operations (N, Q) | Q * N                    | N + Q
Execution Speed         | Very Slow (Causes TLE)   | Milliseconds
Auxiliary Space         | O(1)                     | O(N)
================================================================================
*/
class NumArray {
    private int[] prefixSum;

    public NumArray(int[] nums) {
        int n = nums.length;

        // n+1 is to simplify the 0-index boundary conditions
        prefixSum = new int[n + 1];

        // Build prefix sum array where prefixSum[i] = sum of nums[0 ... i-1]
        for(int i = 0; i < n; i++){
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }
        
    }
    
    // Range sum [left, right] = prefixSum[right + 1] - prefixSum[left]
    public int sumRange(int left, int right) {
        return prefixSum[right + 1] - prefixSum[left];   
    }
}

/**
 * Your NumArray object will be instantiated and called as such:
 * NumArray obj = new NumArray(nums);
 * int param_1 = obj.sumRange(left,right);
 */

 /*--
Explanation:

This algorithm uses Input Enhancement (Preprocessing) via a Prefix Sum array to reduce range query time from O(N) to O(1).

Preprocessing (Constructor):

We construct an auxiliary array 'prefixSum' of size (N + 1).

'prefixSum[k]' stores the cumulative sum of the first 'k' elements of 'nums'.

Using size (N + 1) with 'prefixSum[0] = 0' acts as a dummy base case, avoiding edge-case checks when 'left = 0'.

Query Execution (sumRange):

The sum of elements between indices 'left' and 'right' inclusive is computed in O(1) time using:
Sum(left, right) = prefixSum[right + 1] - prefixSum[left]
--*/

/*--
Complexity Analysis:

Constructor Time Complexity: O(N)

Requires a single pass of length N to compute cumulative sums.

sumRange Time Complexity: O(1)

Performs constant-time direct array accesses and a single subtraction.

Space Complexity: O(N) auxiliary space

Allocates an array 'prefixSum' of size N + 1 to store preprocessed data.
--*/

/**
WALKTHROUGH:

Test Case:
nums = [-2, 0, 3, -5, 2, -1] (N = 6)

Step 1: Constructor Execution (Building prefixSum)

prefixSum initialized with size 7: [0, 0, 0, 0, 0, 0, 0]

i = 0 (nums[0] = -2): prefixSum[1] = prefixSum[0] + (-2) = 0 - 2 = -2

i = 1 (nums[1] = 0): prefixSum[2] = prefixSum[1] + 0 = -2 + 0 = -2

i = 2 (nums[2] = 3): prefixSum[3] = prefixSum[2] + 3 = -2 + 3 = 1

i = 3 (nums[3] = -5): prefixSum[4] = prefixSum[3] + (-5) = 1 - 5 = -4

i = 4 (nums[4] = 2): prefixSum[5] = prefixSum[4] + 2 = -4 + 2 = -2

i = 5 (nums[5] = -1): prefixSum[6] = prefixSum[5] + (-1) = -2 - 1 = -3

Final prefixSum array: [0, -2, -2, 1, -4, -2, -3]


Step 2: Query Executions (sumRange)

Query A: sumRange(0, 2)

Calculation: prefixSum[2 + 1] - prefixSum[0]
= prefixSum[3] - prefixSum[0]
= 1 - 0 = 1

Verification: nums[0] + nums[1] + nums[2] = -2 + 0 + 3 = 1

Query B: sumRange(2, 5)

Calculation: prefixSum[5 + 1] - prefixSum[2]
= prefixSum[6] - prefixSum[2]
= -3 - (-2) = -1

Verification: nums[2] + nums[3] + nums[4] + nums[5] = 3 + (-5) + 2 + (-1) = -1

Query C: sumRange(0, 5)

Calculation: prefixSum[5 + 1] - prefixSum[0]
= prefixSum[6] - prefixSum[0]
= -3 - 0 = -3

Verification: Sum of entire array = -3
*/