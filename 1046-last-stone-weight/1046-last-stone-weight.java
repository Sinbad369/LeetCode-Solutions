/*
================================================================================
LEETCODE 1046: LAST STONE WEIGHT
================================================================================
PROBLEM SUMMARY:
- Input: Array of integers `stones` where `stones[i]` is the weight of the i-th stone.
- Rules: On each turn, choose the TWO HEAVIEST stones (x and y, where x <= y) 
  and smash them together:
  1. If x == y, both stones are completely destroyed.
  2. If x != y, stone x is destroyed, and stone y becomes (y - x).
- Objective: Return the weight of the last remaining stone, or 0 if no stones remain.

WHY A MAX-HEAP (PRIORITY QUEUE)?
- Extreme Value Retrieval: At each turn, we need the TWO LARGEST elements.
- Dynamic Re-insertion: When x != y, the remaining stone (y - x) must be inserted 
  back into the heap in O(log N) time to maintain sorted order for future turns.
- Huffman Connection: This is the dual/opposite of Huffman Coding! Huffman uses a 
  MIN-HEAP to extract the 2 smallest elements and ADD them (x + y). Last Stone 
  Weight uses a MAX-HEAP to extract the 2 largest elements and SUBTRACT them (y - x).
================================================================================
*/
class Solution {
    public int lastStoneWeight(int[] stones) {

        // step1: initialize a max-heap using a custom comparator (b-a)
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> Integer.compare(b, a));

        // step2: offer all initial stone weights into the Max-heap
        for(int stone : stones){
            maxHeap.offer(stone);
        }
        
        // step3: simulation loop - continue while at least 2 stones remain
        while (maxHeap.size() > 1){
            // extract two heaviest stones
            int y = maxHeap.poll();
            int x = maxHeap.poll();

            // if the stones are not equal, push the remaining smashed weight back
            if (x != y){
                maxHeap.offer(y - x);
            }
        }

        // step4: return the weight of the last steon, or 0 if the heap is empty
        return maxHeap.isEmpty() ? 0 : maxHeap.poll();
    }
}

/*
================================================================================
COMPLEXITY ANALYSIS
================================================================================
1. TIME COMPLEXITY: O(N log N)
   - Heap Construction: Inserting N elements into the Max-Heap takes O(N log N).
   - Smashing Loop: In each turn, 2 elements are popped and at most 1 element is 
     pushed back. The loop runs at most N - 1 times.
   - Each poll/offer operation takes O(log N) time.
   - Total Time: O(N log N).

2. SPACE COMPLEXITY: O(N)
   - Max-Heap stores up to N elements simultaneously.
   - Total Space: O(N).
================================================================================
*/

/*
--------------------------------------------------------------------------------
STEP-BY-STEP WALKTHROUGH EXAMPLE:
--------------------------------------------------------------------------------
Given: stones = [2, 7, 4, 1, 8, 1]

1. INITIALIZATION:
   - Max-Heap = [8, 7, 4, 2, 1, 1]

2. STEP 1: Pop 8 and 7
   - y = 8, x = 7
   - x != y (8 - 7 = 1) -> push 1 back into heap
   - Max-Heap state: [4, 2, 1, 1, 1]

3. STEP 2: Pop 4 and 2
   - y = 4, x = 2
   - x != y (4 - 2 = 2) -> push 2 back into heap
   - Max-Heap state: [2, 1, 1, 1]

4. STEP 3: Pop 2 and 1
   - y = 2, x = 1
   - x != y (2 - 1 = 1) -> push 1 back into heap
   - Max-Heap state: [1, 1, 1]

5. STEP 4: Pop 1 and 1
   - y = 1, x = 1
   - x == y -> Both destroyed! Nothing pushed back.
   - Max-Heap state: [1]

6. FINAL RESULT:
   - Loop terminates (heap size = 1 <= 1).
   - Return maxHeap.poll() = 1.
*/