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

/*
================================================================================
TREE HEIGHT CALCULATIONS & BALANCE INVARIANTS
Formula: height = 1 + max(leftHeight, rightHeight)
================================================================================
Tree Balance Definitions & Mechanics:
--------------------------------------------------------------------------------
1. Height-Balanced Binary Tree:
   - A binary tree in which the depth of the two subtrees of EVERY node 
     never differs by more than 1 (|height(left) - height(right)| <= 1).
2. Naive Top-Down Approach:
   - Calculates height at every node recursively.
   - Result: O(N^2) time complexity due to redundant subtree height calculations.
3. Optimized Bottom-Up Approach (Post-Order / DFS):
   - Computes height from leaves upward.
   - Short-circuits immediately by returning -1 if any subtree is unbalanced.
   - Result: O(N) time complexity (visits each node exactly once).
================================================================================
*/
class Solution {
    public boolean isBalanced(TreeNode root) {
        // when did bottom-up check and did not encounter a failure in balance, return true (-1)
        return checkHeight(root) != -1;        
    }

    private int checkHeight(TreeNode node){
        // think about base case first: empty node has height 0
        if(node == null){
            return 0;
        }

        // check height of the left subtree
        int leftHeight = checkHeight(node.left);
        if(leftHeight == -1){
            return -1; // signal balance failure upward
        }

        // check height of the right subtree
        int rightHeight = checkHeight(node.right);
        if(rightHeight == -1){
            return -1; // signal balance failure upward
        }

        // check balance condition at current node
        if (Math.abs(rightHeight - leftHeight) > 1){
            return -1;
        }

        // return the actual height of the current subtree
        return Math.max(leftHeight, rightHeight) + 1;
    }
}

/*--
Explanation:

1. Post-Order Processing:
   - Evaluates child subtree heights (`leftHeight` and `rightHeight`) before evaluating
     the current node.

2. Short-Circuiting (-1 Flag):
   - Uses `-1` as a sentinel value representing an unbalanced subtree.
   - If `leftHeight == -1` or `rightHeight == -1`, the method skips further work 
     and immediately returns `-1` to terminate early.

3. Height Propagation:
   - If balanced, computes current node height as `1 + max(leftHeight, rightHeight)` 
     and passes it up to the parent caller.
--*/

/*--
Complexity Analysis:

- Time Complexity: O(N) where N is the total number of nodes in the binary tree.
  * Bottom-up approach visits each node at most once.
- Space Complexity: O(H) auxiliary call stack space, where H is tree height.
  * Balanced Tree: O(log N) stack depth.
  * Skewed Tree: O(N) stack depth in worst case.
--*/

/**
WALKTHROUGH 1 (Unbalanced Tree Check: root = [1, 2, 2, 3, 3, null, null, 4, 4]):

           1
          / \
         2   2
        / \
       3   3
      / \
     4   4

Step 1: Deepest call reaches node 4 -> left = 0, right = 0 -> returns 1.
Step 2: Parent node 3 (left child 4) -> leftHeight = 1, rightHeight = 0.
        |1 - 0| <= 1 -> returns height 2.
Step 3: Parent node 2 -> left child 3 has height 2, right child 3 has height 1.
        |2 - 1| <= 1 -> returns height 3.
Step 4: Parent node 1 (root) ->
        - left subtree height = 3
        - right subtree height = 1
        - Math.abs(3 - 1) = 2 > 1 -> BALANCE VIOLATION DETECTED!
Step 5: Node 1 returns -1.

Result: checkHeight(root) == -1 -> returns false.
*/