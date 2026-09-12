/*
================================================================================
WHY REDUCE ROTATION TO STRING MATCHING? (Problem Reduction + Input Enhancement)
================================================================================
The Insight:
- "goal" is a rotation of "s" IFF "goal" is a substring of "s + s".
- Rotating "s" by k positions = s.substring(k) + s.substring(0, k).
- Concatenating s with itself lays every possible rotation of "s" out as a
  contiguous window somewhere inside "s + s".
- Example: s = "abcde" -> s+s = "abcdeabcde"
  rotation by 2 ("cdeab") appears at index 2 of "abcdeabcde".

Once reduced, this becomes a plain strStr() problem, solved here with
Horspool's Algorithm (input enhancement via a precomputed shift table),
instead of scanning shift-by-shift like Brute Force.

--------------------------------------------------------------------------------
COMPARISON TABLE:
--------------------------------------------------------------------------------
Metric                  | Brute Force strStr       | Horspool strStr (this file)
------------------------+--------------------------+----------------------------
Preprocessing           | None                     | Build shift table O(m)
Comparison direction    | Left to right            | Right to left
Shift on mismatch       | Always +1                | t(text[i]), often skips several
Worst-case Time         | O(n*m)                   | O(n*m)
Typical-case Time       | O(n)                     | Sub-linear in practice
Auxiliary Space         | O(1)                     | O(alphabet size) for the table
================================================================================
*/
import java.util.HashMap;
import java.util.Map;

class Solution {
    public boolean rotateString(String s, String goal) {
        // Different lengths can never be rotations of one another
        if (s.length() != goal.length()) {
            return false;
        }

        // Every rotation of s is a contiguous substring of s+s
        String text = s + s;
        return strStr(text, goal) != -1;
    }

    // Horspool's Algorithm: search for "pattern" inside "haystack"
    private int strStr(String haystack, String pattern) {
        int n = haystack.length();
        int m = pattern.length();
        if (m == 0) return 0;

        Map<Character, Integer> table = shiftTable(pattern);

        // Align pattern's last character with haystack position i
        int i = m - 1;
        while (i <= n - 1) {
            int k = 0;
            // Compare right to left until mismatch or full match
            while (k <= m - 1 && pattern.charAt(m - 1 - k) == haystack.charAt(i - k)) {
                k++;
            }

            if (k == m) {
                return i - m + 1;
            } else {
                // Shift using the char currently aligned with the pattern's LAST position -- not the char where the mismatch happened
                i = i + table.getOrDefault(haystack.charAt(i), m);
            }
        }
        return -1;
    }

    // Preprocess "pattern" into a shift table (input enhancement)
    private Map<Character, Integer> shiftTable(String pattern) {
        Map<Character, Integer> table = new HashMap<>();
        int m = pattern.length();
        // Only the first m-1 characters matter; last char is excluded
        for (int j = 0; j < m - 1; j++) {
            table.put(pattern.charAt(j), m - 1 - j);
        }
        return table;
        // Any character not put into the table defaults to m at lookup time via table.getOrDefault(c, m)
    }
}

/*--
Explanation:

Rotation Reduction:
"s" can become "goal" via some number of left-rotations iff "goal" occurs
as a substring somewhere in "s + s". This turns an unfamiliar rotation
problem into the exact strStr() problem already solved earlier.

Horspool Matching:
Align the pattern's last character against the text, compare right to
left. On a full match (k == m), a rotation point was found. On mismatch,
shift the pattern using the shift-table entry for whichever text
character is CURRENTLY aligned with the pattern's last position -- this
is what lets Horspool skip multiple positions at once instead of always
shifting by 1 like Brute Force.
--*/

/*--
Complexity Analysis:

rotateString:
- String concatenation s + s: O(n) time and space, where n = s.length().
- Single call to strStr: dominates overall complexity.

strStr (Horspool):
- Shift table construction: O(m), where m = pattern.length() (here, m = n).
- Matching loop worst case: O(n*m) = O(n^2) (rare, needs many partial matches).
- Matching loop typical case: close to O(n), since mismatches are usually
  detected within the first couple of right-to-left comparisons.

Overall: O(n) extra space for "s+s" and the shift table; typical-case time
close to O(n), worst-case O(n^2).
--*/

/**
WALKTHROUGH:

Test Case:
s    = "abcde"
goal = "cdeab"

Step 1: Length check
s.length() == goal.length() == 5 -> proceed.

Step 2: Build text
text = s + s = "abcdeabcde"  (n = 10)

Step 3: Build shift table for pattern = "cdeab" (m = 5)
j=0: table['c'] = 5-1-0 = 4 // literally, how many lettrs a man should pass till b if he is at c : d e a b ==> so, 4 steps
j=1: table['d'] = 5-1-1 = 3
j=2: table['e'] = 5-1-2 = 2
j=3: table['a'] = 5-1-3 = 1
(last char 'b' excluded from the table -> defaults to m=5 if looked up)

Step 4: Horspool search, i starts at m-1 = 4
i=4: text[4]='e'. Compare pattern[4]='b' vs text[4]='e' -> mismatch, k=0.
     shift by table.getOrDefault('e', 5) = 2 -> i = 4+2 = 6
i=6: text[6]='b'. Compare pattern[4]='b' vs text[6]='b' -> match, k=1.
     pattern[3]='a' vs text[5]='a' -> match, k=2.
     pattern[2]='e' vs text[4]='e' -> match, k=3.
     pattern[1]='d' vs text[3]='d' -> match, k=4.
     pattern[0]='c' vs text[2]='c' -> match, k=5.
     k == m (5) -> MATCH FOUND, return i - m + 1 = 6 - 5 + 1 = 2

Step 5: strStr returns 2 (!= -1) -> rotateString returns true

Verification: text.substring(2, 7) = "cdeab" -- matches goal exactly.
This corresponds to rotating "abcde" left by 2 positions.
*/