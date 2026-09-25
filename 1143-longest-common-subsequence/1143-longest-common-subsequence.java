/*
================================================================================
LEETCODE 1143: LONGEST COMMON SUBSEQUENCE (2D MATRIX DP)
================================================================================

PROBLEM DESCRIPTION:
Given two strings 'text1' and 'text2', return the length of their longest common 
subsequence. If there is no common subsequence, return 0.

A subsequence of a string is a new string generated from the original string 
with some characters (can be none) deleted without changing the relative order 
of the remaining characters.

EXAMPLES:
1. Input: text1 = "abcde", text2 = "ace"  -->  Output: 3
   Explanation: The longest common subsequence is "ace" and its length is 3.

2. Input: text1 = "abc", text2 = "abc"    -->  Output: 3
   Explanation: The longest common subsequence is "abc" and its length is 3.

3. Input: text1 = "abc", text2 = "def"    -->  Output: 0
   Explanation: There is no such common subsequence, so the result is 0.

================================================================================
CORE DP IDEA & TRANSITIONS:
================================================================================
We build a 2D table `dp[i][j]` representing the length of the Longest Common 
Subsequence between the prefix text1[0...i-1] and text2[0...j-1].

At each cell (i, j), we compare character text1[i - 1] with text2[j - 1]:

1. MATCH (`text1[i - 1] == text2[j - 1]`):
   The character is shared! We extend the best sequence from the diagonal:
      dp[i][j] = dp[i - 1][j - 1] + 1

2. MISMATCH (`text1[i - 1] != text2[j - 1]`):
   The characters don't match. We take the maximum of skipping either 
   character (Upper vs. Left cell):
      dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1])
================================================================================
*/

class Solution {
    public int longestCommonSubsequence(String text1, String text2) {
        if (text1 == null || text1.length() == 0 || text2 == null || text2.length() == 0){
            return 0;
        }

        int m = text1.length();
        int n = text2.length();

        /*
        ================================================================================
        STEP 1: INITIALIZE THE 2D DP TABLE
        ================================================================================
        1. Table Dimensions:
           The table is sized (m + 1) x (n + 1).
           Rows represent prefixes of text1 (length 0 to m).
           Columns represent prefixes of text2 (length 0 to n).

        2. Value Meaning (`dp[i][j]`):
           `dp[i][j]` stores the length of the LCS for text1[0...i-1] and text2[0...j-1].

        3. Base Cases:
           Row 0 (`dp[0][j]`) and Column 0 (`dp[i][0]`) are automatically initialized to 0.
           An empty string compared against any string yields an LCS of length 0.
        ================================================================================
        */
        int[][] dp = new int[m+1][n+1];

        /*
        ================================================================================
        STEP 2: BOTTOM-UP TABLE FILLING (NESTED LOOPS)
        ================================================================================
        We process row by row (i) and column by column (j).

        1. Outer Loop (`i`): 
           Processes characters of text1 from index 1 to m.

        2. Inner Loop (`j`): 
           Processes characters of text2 from index 1 to n.

        3. Decision Rule:
           - If characters match:  dp[i][j] = dp[i-1][j-1] + 1   (Diag + 1)
           - If mismatch:          dp[i][j] = max(dp[i-1][j], dp[i][j-1]) (Upper vs Left)
        ================================================================================
        */
        for(int i = 1; i <= m; i++){
            for(int j = 1; j <= n; j++){
                //char comparison (1-indexed dp corresponds to 0-indexed String)
                if(text1.charAt(i-1) == text2.charAt(j-1)){
                    // case: match? then add 1 to diagonal subproblem
                    dp[i][j] = dp[i-1][j-1] + 1;
                }else{
                    // case: mismatch? then carry over the max from the upper cell or lower cell
                    int upperSubproblem = dp[i-1][j];
                    int leftSubproblem = dp[i][j-1];

                    dp[i][j] = Math.max(upperSubproblem, leftSubproblem);
                }
            }
        }
        /*
        ================================================================================
        STEP 3: RETURN THE FINAL RESULT
        ================================================================================
        The bottom-right cell `dp[m][n]` evaluates the full length of text1 against 
        the full length of text2, holding the final maximum LCS length!
        ================================================================================
        */
        return dp[m][n];
    }
}

/*
================================================================================
EXPLANATION & ANALYSIS
================================================================================

1. State Definitions:
   - `dp[i][j]`: Length of the Longest Common Subsequence of text1[0...i-1] 
     and text2[0...j-1].

2. State Transitions:
   - If text1[i-1] == text2[j-1]: dp[i][j] = dp[i-1][j-1] + 1
   - If text1[i-1] != text2[j-1]: dp[i][j] = max(dp[i-1][j], dp[i][j-1])

3. Complexity Analysis:
   - Time Complexity: O(M * N) where M = len(text1) and N = len(text2).
     (We iterate through an (M+1) x (N+1) matrix performing O(1) operations per cell).
   - Space Complexity: O(M * N) auxiliary space to store the 2D DP matrix.

================================================================================
DETAILED WALKTHROUGH (text1 = "abcde", text2 = "ace"):
================================================================================

String 1 (m = 5): "abcde"
String 2 (n = 3): "ace"

--- Row 0 & Column 0 Initialized to 0 ---

--- Row 1: text1[0] = 'a' ---
  j=1 ('a'): 'a' == 'a' -> MATCH    -> dp[1][1] = dp[0][0] + 1 = 0 + 1 = 1
  j=2 ('c'): 'a' != 'c' -> MISMATCH -> dp[1][2] = max(dp[0][2], dp[1][1]) = max(0, 1) = 1
  j=3 ('e'): 'a' != 'e' -> MISMATCH -> dp[1][3] = max(dp[0][3], dp[1][2]) = max(0, 1) = 1

--- Row 2: text1[1] = 'b' ---
  j=1 ('a'): 'b' != 'a' -> MISMATCH -> dp[2][1] = max(dp[1][1], dp[2][0]) = max(1, 0) = 1
  j=2 ('c'): 'b' != 'c' -> MISMATCH -> dp[2][2] = max(dp[1][2], dp[2][1]) = max(1, 1) = 1
  j=3 ('e'): 'b' != 'e' -> MISMATCH -> dp[2][3] = max(dp[1][3], dp[2][2]) = max(1, 1) = 1

--- Row 3: text1[2] = 'c' ---
  j=1 ('a'): 'c' != 'a' -> MISMATCH -> dp[3][1] = max(dp[2][1], dp[3][0]) = max(1, 0) = 1
  j=2 ('c'): 'c' == 'c' -> MATCH    -> dp[3][2] = dp[2][1] + 1 = 1 + 1 = 2
  j=3 ('e'): 'c' != 'e' -> MISMATCH -> dp[3][3] = max(dp[2][3], dp[3][2]) = max(1, 2) = 2

--- Row 4: text1[3] = 'd' ---
  j=1 ('a'): 'd' != 'a' -> MISMATCH -> dp[4][1] = max(dp[3][1], dp[4][0]) = max(1, 0) = 1
  j=2 ('c'): 'd' != 'c' -> MISMATCH -> dp[4][2] = max(dp[3][2], dp[4][1]) = max(2, 1) = 2
  j=3 ('e'): 'd' != 'e' -> MISMATCH -> dp[4][3] = max(dp[3][3], dp[4][2]) = max(2, 2) = 2

--- Row 5: text1[4] = 'e' ---
  j=1 ('a'): 'e' != 'a' -> MISMATCH -> dp[5][1] = max(dp[4][1], dp[5][0]) = max(1, 0) = 1
  j=2 ('c'): 'e' != 'c' -> MISMATCH -> dp[5][2] = max(dp[4][2], dp[5][1]) = max(2, 1) = 2
  j=3 ('e'): 'e' == 'e' -> MATCH    -> dp[5][3] = dp[4][2] + 1 = 2 + 1 = 3

Result: dp[5][3] = 3

================================================================================
GRAPHICAL STEP-BY-STEP WALKTHROUGH (text1 = "abcde", text2 = "ace")
================================================================================

--------------------------------------------------------------------------------
STEP 0: TABLE SETUP & BASE CASE INITIALIZATION
--------------------------------------------------------------------------------
          Ø     a     c     e
        j=0   j=1   j=2   j=3
  Ø i=0 [ 0 ] [ 0 ] [ 0 ] [ 0 ]
  a i=1 [ 0 ]
  b i=2 [ 0 ]
  c i=3 [ 0 ]
  d i=4 [ 0 ]
  e i=5 [ 0 ]


--------------------------------------------------------------------------------
STEP 1: FILLING THE DP MATRIX CELL-BY-CELL
--------------------------------------------------------------------------------

Row 1 ('a'):
  [a vs a]: MATCH    ---> Diag + 1 = dp[0][0] + 1 = 1
  [a vs c]: MISMATCH ---> max(Upper, Left) = max(0, 1) = 1
  [a vs e]: MISMATCH ---> max(Upper, Left) = max(0, 1) = 1

Row 2 ('b'):
  [b vs a]: MISMATCH ---> max(Upper, Left) = max(1, 0) = 1
  [b vs c]: MISMATCH ---> max(Upper, Left) = max(1, 1) = 1
  [b vs e]: MISMATCH ---> max(Upper, Left) = max(1, 1) = 1

Row 3 ('c'):
  [c vs a]: MISMATCH ---> max(Upper, Left) = max(1, 0) = 1
  [c vs c]: MATCH    ---> Diag + 1 = dp[2][1] + 1 = 2
  [c vs e]: MISMATCH ---> max(Upper, Left) = max(1, 2) = 2

Row 4 ('d'):
  [d vs a]: MISMATCH ---> max(Upper, Left) = max(1, 0) = 1
  [d vs c]: MISMATCH ---> max(Upper, Left) = max(2, 1) = 2
  [d vs e]: MISMATCH ---> max(Upper, Left) = max(2, 2) = 2

Row 5 ('e'):
  [e vs a]: MISMATCH ---> max(Upper, Left) = max(1, 0) = 1
  [e vs c]: MISMATCH ---> max(Upper, Left) = max(2, 1) = 2
  [e vs e]: MATCH    ---> Diag + 1 = dp[4][2] + 1 = 3


--------------------------------------------------------------------------------
STEP 2: FINAL DP TABLE VISUALIZATION
--------------------------------------------------------------------------------
          Ø     a     c     e
        j=0   j=1   j=2   j=3
  Ø i=0 [ 0 ] [ 0 ] [ 0 ] [ 0 ]
  a i=1 [ 0 ] [ 1 ] [ 1 ] [ 1 ]
  b i=2 [ 0 ] [ 1 ] [ 1 ] [ 1 ]
  c i=3 [ 0 ] [ 1 ] [ 2 ] [ 2 ]
  d i=4 [ 0 ] [ 1 ] [ 2 ] [ 2 ]
  e i=5 [ 0 ] [ 1 ] [ 2 ] [ 3 ] <-- Final Answer: dp[5][3] = 3
================================================================================
*/