import java.util.HashMap;
import java.util.Map;
/*
================================================================================
WHY BOYER-MOORE? (Combining Two Input-Enhancement Tables)
================================================================================
Horspool only uses ONE table: the bad-symbol shift t1(c), based purely on
whatever text character is aligned with the pattern's LAST position.
 
Boyer-Moore adds a SECOND table: the good-suffix shift d2(k), based on the
chunk of the pattern that was already successfully matched (suff(k)) before
the mismatch happened. At every mismatch, it computes BOTH shifts and takes
the larger one -- never wasting the information that k characters already
matched.
 
--------------------------------------------------------------------------------
COMPARISON TABLE:
--------------------------------------------------------------------------------
Metric                  | Horspool                 | Boyer-Moore (this file)
------------------------+--------------------------+----------------------------
Tables precomputed      | 1 (bad-symbol t1)        | 2 (bad-symbol t1 + good-suffix d2)
Shift uses              | only text[i]             | text[i] AND how many chars matched (k)
Shift formula           | i += t1(text[i])         | i += max(d1, d2[k]),  d1 = max(t1(c)-k, 1)
Worst-case Time         | O(n*m)                   | O(n*m)
Typical-case Time       | Sub-linear               | Usually fastest of the three algorithms
Auxiliary Space         | O(alphabet)              | O(alphabet) + O(m) for good-suffix table
================================================================================
*/
class Solution {
    public int repeatedStringMatch(String a, String b) {
    // Step 1: figure out the minimum number of copies of "a" needed just to
    // match (or exceed) the length of "b". Without at least this many
    // copies, "a" repeated could never be long enough to contain "b" at all.
    // Example: a="abcd" (len 4), b="cdabcdab" (len 8) -> 8/4 = 2 copies minimum.
    int minCount = (int) Math.ceil((double) b.length() / a.length());

    // Step 2: actually build that minimum-length repetition of "a" as a string.
    // StringBuilder is used instead of repeated "+" concatenation so we're not
    // creating a new String object on every loop iteration (O(n) vs O(n^2)).
    StringBuilder repeated = new StringBuilder();
    for (int i = 0; i < minCount; i++) repeated.append(a);
    // At this point, "repeated" = a+a (for minCount=2), e.g. "abcdabcd"

    // Step 3: check if "b" already shows up inside this minimum repetition,
    // using the Boyer-Moore search helper. If it's found anywhere (!= -1),
    // minCount copies is already enough -- return that count immediately.
    if (strStr(repeated.toString(), b) != -1) return minCount;

    // Step 4: it's possible "b" straddles the boundary where the minimum
    // repetition cuts off -- part of "b" would match at the very end of
    // "repeated", and the rest would only appear if there were ONE more
    // copy of "a" tacked on. So append exactly one more copy...
    repeated.append(a);
    // ...and search again with this longer version (minCount + 1 copies).
    if (strStr(repeated.toString(), b) != -1) return minCount + 1;

    // Step 5: if "b" isn't found even after that one extra copy, it will
    // never be found no matter how many more copies of "a" you add --
    // adding further copies just repeats the same cycle of characters,
    // so no new alignment possibility appears. Safe to give up here.
    return -1;
}

    private int strStr(String haystack, String needle) {
        int n = haystack.length(); // haystack length
        int m = needle.length(); // needle length
        if(m == 0) return 0;
        if (m > n) return -1;

        // shift tables are made using needle (aka pattern)
        Map<Character, Integer> badSymbolTable = buildBadSymbolTable(needle); 
        // goodSuffixTable is also a shift table, so needle is used
        int[] goodSuffixTable = buildGoodSuffixTable(needle);

        // Align the pattern's last char with text position i
        int i = m - 1;
        while(i <= n - 1){
            int k = 0;
            // compare right to left till the mismatch or the full match
            while(k <= m - 1 && needle.charAt(m - 1 - k) == haystack.charAt(i - k)){
                k++;
            }

            if(k == m){
                return i - m + 1;
            } else{
                char c = haystack.charAt(i);
                // d1: bad-symbol shift, adjusted for the k chars already matched
                int d1 = Math.max(badSymbolTable.getOrDefault(c, m) - k, 1);
                // If nothing matched yet (k = 0), good-suffix info doesn't apply
                int shift = (k == 0) ? d1 : Math.max(d1, goodSuffixTable[k]);
                i += shift;
            }
        }
        return -1;
    }


    // Bad-symbol shift table t1(c) -- identical to Horspool's shift table
    private Map<Character, Integer> buildBadSymbolTable(String pattern){
        Map<Character, Integer> table = new HashMap<>();
        int m = pattern.length();
        for(int j = 0; j < m - 1; j++){
            table.put(pattern.charAt(j), m - 1 -j);
        }
        return table;
    }

    // Good-suffix shift table d2, indexed by k (number of matched trailing chars).
    // Internally computed via the standard "strong good suffix" border-array technique
    // , then remapped from shift[q] (indexed by match-start position) 
    // to d2[k] (indexed by k)
    private int[] buildGoodSuffixTable(String pattern){
        int m = pattern.length();
        int[] shift = new int[m + 1];
        int[] borderPos = new int[m + 1];

        // Case I/III: find, for every suffix, its widest occurence elsewehere in the
        // pattern that is preceded by a different character.
        int i = m, j = m + 1;
        borderPos[i] = j;
        while(i > 0) {
            while(j <= m && pattern.charAt(i - 1) != pattern.charAt(j - 1)){
                if (shift[j] == 0){
                    shift[j] = j - i;
                }
                j = borderPos[j];
            }
            i--;
            j--;
            borderPos[i] = j;
        }

        // Case II: fill any remaining gaps using the widest border (prefix that is  
        // also a suffix) of the whole pattern {aka similar prefix overlap case}
        j = borderPos[0];
        for (i = 0; i <= m; i++){
            if (shift[i] == 0){
                shift[i] = j;
            }
            if (i == j){
                j = borderPos[j];
            }
        }

        // Remap: shift[q] (pattern[q..m-1] matched) -> d2[k] (k chars matched)
        int[] d2 = new int[m + 1];
        for(int k = 0; k <= m; k++){
            d2[k] = shift[m - k];
        }
        return d2;
    }

}


/*--
Explanation:
 
Two tables, two kinds of information:

- Bad-symbol (d1): "what text character caused the mismatch, and how far
  can I skip based on where that character last appears in the pattern?"

- Good-suffix (d2): "I already matched k characters -- does that exact
  matched chunk (or a self-overlapping piece of the pattern) appear
  anywhere else I could realign to?"
 
At every mismatch, Boyer-Moore computes both and takes the larger shift,
since either one could be the more informative choice depending on the
pattern and text.
 
Special case: if k=0 (mismatch on the very first right-to-left compare),
there is no matched suffix yet, so only d1 is used.
--*/
 
/*--
Complexity Analysis:
 
Preprocessing:
- Bad-symbol table: O(m)
- Good-suffix table: O(m) using the border-array technique
 
Matching:
- Worst case: O(n*m), same theoretical bound as Horspool/Brute Force

- Typical case: faster than Horspool in practice, since large shifts from
  either table are used, and pathological repeated patterns are handled
  correctly by the good-suffix table (avoiding Horspool's worst cases)
 
Space: O(alphabet size) for t1, O(m) for d2
--*/
 
/**
WALKTHROUGH:
 
Good-suffix table built for pattern "ABCBAB":
  k=1 -> d2=2
  k=2 -> d2=4
  k=3 -> d2=4
  k=4 -> d2=4
  k=5 -> d2=4
 
Functional checks:
  strStr("hello", "ll")                           -> 2
  strStr("aaaaa", "bba")                          -> -1
  strStr("a", "a")                                -> 0
  strStr("NOBODY_NOTICED_HIM", "NOT")             -> 7   (matches Horspool trace)
  strStr("mississippi", "issip")                  -> 4
  strStr("BESS_KNEW_ABOUT_BAOBABS", "BAOBAB")     -> 16  (matches Boyer-Moore trace)
*/