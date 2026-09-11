import java.util.*;

class Solution {
    public int heightChecker(int[] heights) {
        int n = heights.length;
        int k = 100;

        int[] C = new int[k + 1];

        // count the frequency - step1        
        for(int i = 0; i < n; i++){
            C[heights[i]] = C[heights[i]] + 1;
        }

        // accumulation(prefix sums) - step2
        for(int j = 1; j <= k; j++){
            C[j] = C[j] + C[j - 1];
        }

        // backward placement into B - step3 
        int[] B = new int[n];
        for(int i = n - 1; i >= 0; i--){
            B[C[heights[i]] - 1] = heights[i];
            C[heights[i]] = C[heights[i]] - 1;
        }

        // compare heights vs. B - step4
        int mismatches = 0;
        for(int i = 0; i < n; i++){
            if(heights[i] != B[i]) {
                mismatches++;
            }
        }
        
        return mismatches;        
    }
}

/*--
Explanation:

For this problem, I used Distribution Counting Sort. 

The algorithm relies on three key structures:

Original Array (A / heights): Holds the input values (size N).

Frequency/Distribution Array (C): Size k + 1, where k is the maximum value in the array. Indices represent values, initialized to 0.

Result Array (B): Output array of size N to store elements in sorted order. 

Algorithm Steps:

Step 1 (Frequency Count): Iterate through the original array and increment C[heights[i]] for each element.

Step 2 (Prefix Sums): Transform C into an accumulated count array using C[j] = C[j] + C[j-1]. Now, C[j] indicates the exact number of elements <= j.

Step 3 (Placement): Iterate backward through the original array. Use C[heights[i]] - 1 to place each element into its sorted position in B, then decrement C[heights[i]]. 

Finally, to solve the specific LeetCode problem, compare heights[i] with B[i] across all indices and increment mismatches whenever they differ.

--*/

/**
Step-by-Step Code Walkthrough

Input: heights = [1, 1, 4, 2, 1, 3] (N = 6, k = 100)
####################Code##################
int n = heights.length;
int k = 100;
int[] C = new int[k + 1];
##########################################


Step 1: Frequency Counting
####################Code##################
for(int i = 0; i < n; i++){
    C[heights[i]] = C[heights[i]] + 1;
}
##########################################
C is initialized to size 101 with all zeros.

i = 0, heights[0] = 1 -> C[1] becomes 1
i = 1, heights[1] = 1 -> C[1] becomes 2
i = 2, heights[2] = 4 -> C[4] becomes 1
i = 3, heights[3] = 2 -> C[2] becomes 1
i = 4, heights[4] = 1 -> C[1] becomes 3
i = 5, heights[5] = 3 -> C[3] becomes 1

Non-zero values in C: C[1] = 3, C[2] = 1, C[3] = 1, C[4] = 1.

Step 2: Accumulation (Prefix Sums)
####################Code##################
for(int j = 1; j <= k; j++){
    C[j] = C[j] + C[j - 1];
}
##########################################
Modifies C so C[j] stores the total elements <= j.

j = 1: C[1] = C[1] + C[0] -> 3 + 0 = 3
j = 2: C[2] = C[2] + C[1] -> 1 + 3 = 4
j = 3: C[3] = C[3] + C[2] -> 1 + 4 = 5
j = 4: C[4] = C[4] + C[3] -> 1 + 5 = 6
j = 5..100: values remain 6

Accumulated counts in C: C[1] = 3, C[2] = 4, C[3] = 5, C[4] = 6.

Step 3: Backward Placement into B
####################Code##################
int[] B = new int[n];
for(int i = n - 1; i >= 0; i--){
    B[C[heights[i]] - 1] = heights[i];
    C[heights[i]] = C[heights[i]] - 1;
}
##########################################
Iterates backwards from index 5 down to 0 to preserve stability and construct array B.

i = 5 (heights[5] = 3): Index in B = C[3] - 1 (5 - 1 = 4) -> B[4] = 3. Decrement C[3] to 4.
i = 4 (heights[4] = 1): Index in B = C[1] - 1 (3 - 1 = 2) -> B[2] = 1. Decrement C[1] to 2.
i = 3 (heights[3] = 2): Index in B = C[2] - 1 (4 - 1 = 3) -> B[3] = 2. Decrement C[2] to 3.
i = 2 (heights[2] = 4): Index in B = C[4] - 1 (6 - 1 = 5) -> B[5] = 4. Decrement C[4] to 5.
i = 1 (heights[1] = 1): Index in B = C[1] - 1 (2 - 1 = 1) -> B[1] = 1. Decrement C[1] to 1.
i = 0 (heights[0] = 1): Index in B = C[1] - 1 (1 - 1 = 0) -> B[0] = 1. Decrement C[1] to 0.

Resulting Array B: [1, 1, 1, 2, 3, 4]

Step 4: Compare heights vs. B

i = 0: heights[0] (1) == B[0] (1) -> Match
i = 1: heights[1] (1) == B[1] (1) -> Match
i = 2: heights[2] (4) != B[2] (1) -> Mismatch 1
i = 3: heights[3] (2) == B[3] (2) -> Match
i = 4: heights[4] (1) != B[4] (3) -> Mismatch 2
i = 5: heights[5] (3) != B[5] (4) -> Mismatch 3

Output: 3
 */
