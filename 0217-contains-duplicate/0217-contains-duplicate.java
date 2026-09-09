// Notes to handle this problem
/*-- 
1. Simple way is to compare every element and it is the worst way,
but the solution is guaranteed:
Time complexity: O(N^2) bcz it compares each one element, thus N elements in total with the remaining (N-1) elements
Space complexity: O(1) bcz you are not creating anything of set, just a primitive pointer, that's it

2. Better approach is sorting and then having a counter on elements or just comparing like brute force. But in this case, sorting is prerequisite
Time complexity: sorting(O(logN)) * comparing(O(N)) = thus, O(N*log(N))
Space complexity: O(N) if created set or another array, O(1) if done in its own place

3. The best approach is using HashMap:
I create an empty HashSet of and start taking elements from array and compare if i had it before or not. If I have seen it, that is it, I am done I return true- the array has duplicate. Else, false bcz it is unique array
Time complexity: O(N) bcz i compare elements anyway
Space complexity: O(N) simply bcz i have used a set
--*/
// import the HashSet or just import java.util.*
import java.util.HashSet;
import java.util.Set;

class Solution {
    public boolean containsDuplicate(int[] nums) {
        // well, what if the array is empty or consists of only one number?
        if (nums == null || nums.length < 2){
            return false;
        }
        // create the HashSet
        Set<Integer> foundBefore = new HashSet<>();

        // check the numbers if you have seen before or not
        for(int num : nums){
            // if yes, say i have seen it and report that there is sth at least duplicate
            if (foundBefore.contains(num)){
                return true;
            // if not, just add it to your HashSet
            } else{
                foundBefore.add(num);
            }
        }
        // just return false in case every element is unique
        return false;        
    }
}