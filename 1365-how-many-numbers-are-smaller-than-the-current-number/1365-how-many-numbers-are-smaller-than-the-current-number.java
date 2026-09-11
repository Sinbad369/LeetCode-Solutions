class Solution {
    public int[] smallerNumbersThanCurrent(int[] nums) {
        int n = nums.length;
        int k = 100;

        // step1 frequency count
        int[] CountFreqArray = new int[k + 1];
        for(int i = 0; i < n; i++){
            CountFreqArray[nums[i]]++;
        }

        // step2 accumulation (prefix sums): CountFreqArray[j] stores the count of numbers <= j
        for(int j = 1; j <=k; j++){
            CountFreqArray[j] = CountFreqArray[j] + CountFreqArray[j - 1];
        }

        // step3 map values to the counts
        int[] outcome = new int[n];
        for(int i = 0; i < n; i++){
            if (nums[i] == 0){
                outcome[i] = 0;
            }else{
                outcome[i] = CountFreqArray[nums[i] - 1]; // count of elements <= (nums[i] - 1)
            }
        }
        return outcome;
    }
}

/*--
Explanation:

For this problem, we use Distribution Counting (Frequency + Prefix Sums) as an Input Enhancement technique to achieve linear time complexity.

Algorithm Steps:
Step 1 (Frequency Count): Count occurrences of each number in frequency array CountFreqArray of size k + 1.
Step 2 (Prefix Sums): Accumulate counts in CountFreqArray. After this, CountFreqArray[j] represents the number of elements <= j.
Step 3 (Lookup): For each element nums[i], the count of strictly smaller numbers is stored at CountFreqArray[nums[i] - 1]. If nums[i] is 0, the count is trivially 0.

NOTE: Comparison Counting Sort (Theoretical O(N^2))

Classic Comparison Counting explicitly compares every element against all other elements:

for (int i = 0; i < n; i++) {
    int count = 0;
    for (int j = 0; j < n; j++) {
        if (nums[j] < nums[i]) {
            count++;
        }
    }
    result[i] = count;
}

While Pure Comparison Counting runs in O(N^2) time with O(1) extra space, using Distribution Counting / Input Enhancement reduces the time to O(N + K) when key values are within a small, bounded range (K <= 100).
--*/


/*--
Complexity Analysis:

Time Complexity: O(N + K) where N is array length and K is max value range (100).

Step 1: O(N) pass to populate frequency array CountFreqArray.

Step 2: O(K) pass to compute prefix sums.

Step 3: O(N) pass to construct result array via O(1) lookups.

Total Time: O(2N + K) = O(N + K).

Space Complexity: O(K) auxiliary space for frequency array CountFreqArray (excluding output array of size N).
--*/

/**
WALKTHROUGH:

Test Case: nums = [8, 1, 2, 2, 3] (N = 5, k = 100)

Step 1: Frequency Counting
Populate array CountFreqArray of size 101:

nums[0] = 8 -> CountFreqArray[8] = 1

nums[1] = 1 -> CountFreqArray[1] = 1

nums[2] = 2 -> CountFreqArray[2] = 1

nums[3] = 2 -> CountFreqArray[2] = 2

nums[4] = 3 -> CountFreqArray[3] = 1

Step 2: Accumulation (Prefix Sums)
CountFreqArray[j] = CountFreqArray[j] + CountFreqArray[j - 1] (stores count of elements <= j):

CountFreqArray[1] = 1

CountFreqArray[2] = 3

CountFreqArray[3] = 4

CountFreqArray[4..7] = 4

CountFreqArray[8] = 5

Step 3: Direct Address Lookup (CountFreqArray[nums[i] - 1])

i = 0 (nums[0] = 8): CountFreqArray[7] = 4 -> result[0] = 4

i = 1 (nums[1] = 1): CountFreqArray[0] = 0 -> result[1] = 0

i = 2 (nums[2] = 2): CountFreqArray[1] = 1 -> result[2] = 1

i = 3 (nums[3] = 2): CountFreqArray[1] = 1 -> result[3] = 1

i = 4 (nums[4] = 3): CountFreqArray[2] = 3 -> result[4] = 3

Resulting Array: [4, 0, 1, 1, 3]

*/
