class Solution {
    public void sortColors(int[] nums) {

        // let's count the frequency of the colors 0=red, 1=white, 2=blue NOTE: ALL INITIALIZED TO 0 bcz we do not know the count yet
        int redCount = 0, whiteCount = 0, blueCount = 0;

        // now, we find the frequency of each color
        for(int num : nums){
            if (num == 0) redCount++;
            else if (num == 1) whiteCount++;
            else if (num == 2) blueCount++;
        }

        // to IN-PLACE sorting, we need index initialized to 0 for starters
        int indexColor = 0;

        // place the reds first, after each placing of single red, decrease the redCount
        while (redCount > 0){
            nums[indexColor++] = 0;
            redCount--;
        }

        // place the whites after reds, after each placing of single white, decrease the whiteCount
        while (whiteCount > 0){
            nums[indexColor++] = 1;
            whiteCount--;
        }

        // place the blues after whites, after each placing of single blue, decrease the blueCount
        while (blueCount > 0){
            nums[indexColor++] = 2;
            blueCount--;
        }
        
    }
}

/*--
Complexity Analysis:
- Time Complexity: O(N) where N is the length of array nums.
  - Pass 1 (Counting): O(N) to count frequencies of 0, 1, and 2.
  - Pass 2 (Overwrite): O(N) total across all while loops to write values back in-place.
  - Total Time: O(2N) = O(N) linear time.

- Space Complexity: O(1) auxiliary space.
  - Uses only primitive integer variables (redCount, whiteCount, blueCount, indexColor).
  - No additional arrays or data structures are allocated, satisfying the in-place constraint.


########################################################
WALKTHROUGH: 
########################################################

Input: nums = [2, 0, 2, 1, 1, 0] (N = 6)

Step 1: Frequency Counting

Counters are initialized to zero: redCount = 0, whiteCount = 0, blueCount = 0.

num = 2 -> blueCount becomes 1
num = 0 -> redCount becomes 1
num = 2 -> blueCount becomes 2
num = 1 -> whiteCount becomes 1
num = 1 -> whiteCount becomes 2
num = 0 -> redCount becomes 2

Final Counters: redCount = 2, whiteCount = 2, blueCount = 2.

Step 2: In-Place Overwrite

Initialize indexColor = 0.

Red Loop (redCount = 2):
nums[0] = 0, indexColor becomes 1, redCount becomes 1
nums[1] = 0, indexColor becomes 2, redCount becomes 0

White Loop (whiteCount = 2):
nums[2] = 1, indexColor becomes 3, whiteCount becomes 1
nums[3] = 1, indexColor becomes 4, whiteCount becomes 0

Blue Loop (blueCount = 2):
nums[4] = 2, indexColor becomes 5, blueCount becomes 1
nums[5] = 2, indexColor becomes 6, blueCount becomes 0

Resulting Array: [0, 0, 1, 1, 2, 2]


$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
EXTRA NOTE ABOUT THE ALGORITHM USED AND COMPARISON
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
In-Place Counting (Sort Colors)
Core Concept

A 2-pass variant of Counting Sort designed for arrays with a very small range of values (0, 1, 2). It trades algorithm stability to achieve O(1) extra space, satisfying in-place requirements without allocating an output array B.

How it Works

Pass 1 (Frequency Count): Loop through nums once and count the occurrences of each distinct value using primitive variables (redCount, whiteCount, blueCount).

Pass 2 (In-Place Overwrite): Sequentially write values back into nums using a pointer (indexColor), overwriting the original elements based on the exact counts gathered in Pass 1.

Key Differences from Distribution Counting-->

No Prefix Sums: Fixed values (0, 1, 2) allow direct sequential writing without calculating dynamic lookup boundaries.

In-Place (O(1) Space): Overwrites the input array directly instead of placing elements backward into an auxiliary array B.

Complexity

Time Complexity: O(N) (One pass to count, one pass to write).

Space Complexity: O(1) auxiliary space.

--*/