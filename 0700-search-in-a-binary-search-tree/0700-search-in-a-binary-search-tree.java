/*
================================================================================
DICTIONARY IMPLEMENTATIONS & BST SEARCH
================================================================================
Search Trade-Offs:
--------------------------------------------------------------------------------
1. Unsorted Array: Search takes O(N) time.
2. Sorted Array: Search takes O(log N) via Binary Search, but updates (insert/delete)
   take O(N) due to element shifts.
3. Binary Search Tree (BST): Search, Insertion, and Deletion all run in 
   O(log N) average time.
4. B-Trees: Generalizes BSTs to higher degree branching (m-way) for disk blocks.
================================================================================
*/

/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */
class Solution {
    public TreeNode searchBST(TreeNode root, int val) {
        TreeNode actualNode = root;

        while(actualNode != null && actualNode.val != val){
            if (val < actualNode.val){
                actualNode = actualNode.left; //search the left subtree
            }else{
                actualNode = actualNode.right; //search the right subtree
            }
        }

        return actualNode; //returns matching node or null if not found
    }
}

/*--
Explanation:

1. BST Property:
   - For any node `x`, all keys in its left subtree are `< x.val`, and all keys 
     in its right subtree are `> x.val`.

2. Loop Mechanics:
   - `actualNode` tracks the current node during traversal.
   - If `val < actualNode.val`, update `actualNode` to `actualNode.left` to search deeper left.
   - If `val > actualNode.val`, update `actualNode` to `actualNode.right` to search deeper right.
   - The loop terminates when `actualNode.val == val` (target found) or `actualNode == null` (target missing).
--*/

/*--
Complexity Analysis:

- Time Complexity: O(H) where H is the height of the tree.
  * Balanced BST: O(log N) average time.
  * Unbalanced / Degenerate BST: O(N) worst-case time.
- Space Complexity: O(1) auxiliary space using iterative pointer traversal.
--*/

/**
WALKTHROUGH 1 (Tree Search Match: root = [4,2,7,1,3], val = 2):

          4
         / \
        2   7
       / \
      1   3

Initialization: actualNode = root (node 4)

Step 1: actualNode.val = 4, target val = 2
  - actualNode != null && 4 != 2 -> true
  - 2 < 4 -> actualNode moves to actualNode.left (node 2)

Step 2: actualNode.val = 2, target val = 2
  - actualNode.val == val -> loop terminates!

Result: Returns subtree starting at node 2 ([2,1,3]).
*/

/**
WALKTHROUGH 2 (Tree Search Miss: root = [4,2,7,1,3], val = 5):

Initialization: actualNode = root (node 4)

Step 1: actualNode.val = 4, target val = 5
  - 5 > 4 -> actualNode moves to actualNode.right (node 7)

Step 2: actualNode.val = 7, target val = 5
  - 5 < 7 -> actualNode moves to actualNode.left (null)

Step 3: actualNode == null
  - loop terminates!

Result: Returns null.
*/