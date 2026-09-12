/*
================================================================================
WHY BRUTE FORCE STRING MATCHING? (Space-for-Time Trade-Off baseline)
================================================================================
Brute Force (this solution):
- No preprocessing on the pattern or text at all.
- For every possible starting position 'i' in the text, re-check characters
  one by one against the pattern from scratch.
- Time: O(n*m)  where n = text length, m = pattern length.
- Space: O(1) extra space.
 
This is the baseline the lecture builds on before introducing "pre-structuring"
algorithms (Boyer-Moore, Horspool) that trade extra preprocessing space/time
for faster average-case matching:
 
--------------------------------------------------------------------------------
COMPARISON TABLE:
--------------------------------------------------------------------------------
Metric                  | Brute Force              | Boyer-Moore / Horspool
------------------------+--------------------------+----------------------------
Preprocessing           | None                     | Build shift table O(m + alphabet)
Comparisons per shift   | Up to m                  | Often << m (skips ahead)
Worst-case Time         | O(n*m)                   | O(n*m) worst, O(n) typical
Average-case Time       | O(n) for natural text    | Sub-linear in practice
Auxiliary Space         | O(1)                     | O(m + alphabet)
================================================================================
*/

class Solution {

    public int strStr(String haystack, String needle) {
        int haystackLength = haystack.length();
        int needleLength = needle.length();
        int difference = haystackLength - needleLength;
        
        // Align pattern against every valid starting position (0 .. n-m)
        for(int i = 0; i <= difference; i++){
            int j = 0;

            // Compare characters left to right until mismatch or full match
            while(j < needleLength && needle.charAt(j) == haystack.charAt(i+ j)){
                j++;
            }
            if (j == needleLength){
                return i;
            }
            // On mismatch, the outer loop shifts the pattern one position right
        }
        return -1;
    }
}


/*--
Explanation:
 
This is the classic Brute-Force String Matching algorithm: align the pattern
of 'm' characters against the text starting at index 0, compare left to right,
and on any mismatch shift the pattern one position to the right and restart
the comparison from the pattern's first character.
 
The last position at which the pattern can still fully fit inside the text is
'n - m' (text indices are 0-based), so the outer loop must run while
i <= n - m, not i < n - m -- this off-by-one is the most common bug.
--*/
 
/*--
Complexity Analysis:
 
Time Complexity:
- Outer loop: runs at most (n - m + 1) times.
- Inner loop: runs at most 'm' times per outer iteration.
- Worst case: O(m * (n - m + 1)) = θ(n*m).
 
Average Case:
- For natural language text, mismatches usually happen after just a few
  character comparisons, so average-case efficiency is θ(n).
 
Space Complexity: O(1) auxiliary space (only pointers i and j).
--*/
 
/**
WALKTHROUGH:
 
Test Case:
haystack = "NOBODY_NOTICED_HIM"   (n = 18)
needle   = "NOT"                  (m = 3)
difference = n - m = 15
 
Indices: 0:N 1:O 2:B 3:O 4:D 5:Y 6:_ 7:N 8:O 9:T 10:I 11:C 12:E 13:D 14:_ 15:H 16:I 17:M
 
i = 0: 
    j=0 'N'=='N' 
    j=1 'O'!='O'... compare 'O' vs haystack[1]='O' j=1 -> j=2 'T' vs haystack[2]='B' mismatch. Shift.

i = 1..6: 
    each mismatches within the first 1-2 characters. Shift each time.

i = 7: 
    j=0 'N'==haystack[7]='N' j=1
    j=1 'O'==haystack[8]='O' j=2
    j=2 'T'==haystack[9]='T' j=3
    j == needleLength (3) -> MATCH FOUND
 
Return i = 7.
 
Verification: haystack.substring(7, 10) = "NOT" -- matches needle exactly.
*/