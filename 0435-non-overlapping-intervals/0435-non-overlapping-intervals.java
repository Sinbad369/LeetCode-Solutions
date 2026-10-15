/*
================================================================================
LEETCODE 435: NON-OVERLAPPING INTERVALS (GREEDY / INTERVAL SCHEDULING)
================================================================================

PROBLEM DESCRIPTION:
Given an array of intervals `intervals` where intervals[i] = [start_i, end_i], 
return the minimum number of intervals you need to remove to make the rest of 
the intervals non-overlapping.

================================================================================
GREEDY STRATEGY & PROOF:
================================================================================
To minimize the number of intervals removed, we want to MAXIMIZE the number 
of non-overlapping intervals we keep.

Greedy Choice Property:
Always pick the interval that ENDS FIRST (smallest end time).
- Why? An interval that ends earlier leaves maximum room/capacity for subsequent 
  intervals to fit without overlapping!
- Once we choose an interval, we irrevocably discard any overlapping intervals 
  and move to the next valid interval.

Algorithm:
1. Sort intervals by their end times in ascending order.
2. Track the end time of the last kept interval (`lastEnd`).
3. Iterate through intervals:
   - If current interval's start >= lastEnd: Keep it, update `lastEnd`.
   - Else: Overlap detected -> Remove current interval (increment removal count).

Time Complexity:  O(N log N) due to sorting.
Space Complexity: O(1) auxiliary space (or O(N) depending on sort implementation).
================================================================================
*/
class Solution {
    public int eraseOverlapIntervals(int[][] intervals) {
        if(intervals.length == 0){
            return 0;
        }

        // line1: sort intervals by END time ascending (locally optimal greedy choice)
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[1], b[1]));

        int removedCount = 0;

        // line2: keep track of end time of the last non-overlapping interval
        int lastEnd = intervals[0][1];

        // line3: iterate through remaining intervals
        for(int i = 1; i < intervals.length; i++){
            if(intervals[i][0] >= lastEnd){
                // line4: no overlap -> keep interval, updatge current end time
                lastEnd = intervals[i][1];
            }else{
                // line5: overlap detected -> greedy choice requires removing this interval
                removedCount++;
            }
        }

        return removedCount;
        
    }
}

/*
================================================================================
FULL LINE-BY-LINE CODE WALKTHROUGH
intervals = [[1, 2], [2, 3], [3, 4], [1, 3]]
================================================================================

--- SORTING PHASE ---
- Line 1: Sort by end times (intervals[i][1]):
  Original: [[1, 2], [2, 3], [3, 4], [1, 3]]
  Sorted:   [[1, 2], [2, 3], [1, 3], [3, 4]]

--- INITIALIZATION PHASE ---
- removedCount = 0
- Line 2: Keep first interval [1, 2] -> lastEnd = 2

--- GREEDY TRACING ---

- Interval 1: [2, 3] (i = 1)
  * Check: start (2) >= lastEnd (2) -> TRUE
  * Action: Keep [2, 3], update lastEnd = 3
  * Status: removedCount = 0, lastEnd = 3

- Interval 2: [1, 3] (i = 2)
  * Check: start (1) >= lastEnd (3) -> FALSE (Overlap with kept intervals!)
  * Action: Remove [1, 3] (line 5)
  * Status: removedCount = 1, lastEnd = 3

- Interval 3: [3, 4] (i = 3)
  * Check: start (3) >= lastEnd (3) -> TRUE
  * Action: Keep [3, 4], update lastEnd = 4
  * Status: removedCount = 1, lastEnd = 4

--- RETURN PHASE ---
- Returns removedCount = 1.

================================================================================
INTERVAL TIMELINE VISUALIZATION
================================================================================

Time:       1    2    3    4
[1, 2]  :   |----|               <-- KEEP (Ends at 2)
[2, 3]  :        |----|          <-- KEEP (Ends at 3)
[1, 3]  :   |---------|          <-- REMOVE (Overlaps with [1,2] and [2,3])
[3, 4]  :             |----|     <-- KEEP (Ends at 4)

Total Kept: 3 intervals | Total Removed: 1 interval
================================================================================
*/