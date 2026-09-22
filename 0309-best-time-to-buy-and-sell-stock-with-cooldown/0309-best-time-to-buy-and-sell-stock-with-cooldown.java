/*
================================================================================
DYNAMIC PROGRAMMING: BEST TIME TO BUY & SELL STOCK WITH COOLDOWN (3-STATE DP)
================================================================================
*/
class Solution {
    public int maxProfit(int[] prices) {
        if(prices == null || prices.length == 0){
            return 0;
        }

        // base case for day 0

        //// bought stock on day 0
        int hold = -prices[0];

        //// cannot sell stock on day 0
        int sold = 0;

        //// nothing done on day 0
        int rest = 0;

        // transition through each day
        for (int i = 1; i < prices.length; i++){
            int prevHold = hold;
            int prevSold = sold;
            int prevRest = rest;

            //state1: hold stock today
            // two possibilities: A-kept holding from yesterday; B-bought today after resting yesterday
            hold = Math.max(prevHold, prevRest - prices[i]);

            //state2: sold stock today
            // must've hold stock yesterday and sold at today's price
            sold = prevHold + prices[i];

            //state3: rest today - cooldown or idle
            // either rested yesterday or just done with the obligatory cooldown from selling yesterday
            rest = Math.max(prevRest, prevSold);
        }

        // Maximum profit will be either resting or just sold on the last day
        return Math.max(sold, rest);
        
    }
}

/*--
Explanation:

1. State Definitions:
   - `hold[i]`: Max profit on day `i` while holding 1 share.
   - `sold[i]`: Max profit on day `i` right after selling a share (triggers forced cooldown).
   - `rest[i]`: Max profit on day `i` holding 0 shares and free to buy.

2. State Transitions:
   - `hold[i] = max(hold[i-1], rest[i-1] - prices[i])`
   - `sold[i] = hold[i-1] + prices[i]`
   - `rest[i] = max(rest[i-1], sold[i-1])`

3. O(1) Space Optimization:
   - Since day `i` transitions strictly depend on day `i-1`, we track the 3 states
     using scalar variables (`prevHold`, `prevSold`, `prevRest`).
--*/

/*--
Complexity Analysis:

- Time Complexity: O(N) where N is the number of days in prices array.
- Space Complexity: O(1) auxiliary space using scalar state variables.

--*/

/**
WALKTHROUGH 1 (prices = [1, 2, 3, 0, 2]):

Initialization (Day 0, price = 1):
  hold = -1, sold = 0, rest = 0

Day 1 (price = 2):
  prevHold = -1, prevSold = 0, prevRest = 0
  hold = max(-1, 0 - 2) = -1
  sold = -1 + 2 = 1
  rest = max(0, 0) = 0

Day 2 (price = 3):
  prevHold = -1, prevSold = 1, prevRest = 0
  hold = max(-1, 0 - 3) = -1
  sold = -1 + 3 = 2
  rest = max(0, 1) = 1

Day 3 (price = 0):
  prevHold = -1, prevSold = 2, prevRest = 1
  hold = max(-1, 1 - 0) = 1    <-- Bought stock using rest profit from Day 2!
  sold = -1 + 0 = -1
  rest = max(1, 2) = 2          <-- Cooldown active after Day 2 sale!

Day 4 (price = 2):
  prevHold = 1, prevSold = -1, prevRest = 2
  hold = max(1, 2 - 2) = 1
  sold = 1 + 2 = 3              <-- Sold stock bought on Day 3!
  rest = max(2, -1) = 2

Resulting Maximum Profit: max(sold, rest) = max(3, 2) = 3
*/