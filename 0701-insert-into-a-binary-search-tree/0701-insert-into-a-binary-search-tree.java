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
BINARY SEARCH TREE INSERTION & DYNAMIC REBALANCING
================================================================================
BST Insertion Dynamics:
--------------------------------------------------------------------------------
1. Unbalanced BST Insertion:
   - Always inserts a new key as a NEW LEAF node.
   - Preserves BST invariants without modifying existing tree connections.
   - Time complexity depends strictly on tree height: O(H).
2. Dynamic Rebalancing (AVL / Red-Black Trees):
   - Performs structural rotations after leaf insertion if balance factors break.
   - Guarantees worst-case O(log N) height at the cost of rotational overhead.
3. B-Trees (M-way Branching):
   - Inserts into leaf first; splits bottom-up when capacity exceeds m-1 keys.
   - Grows upward from root, keeping all leaf nodes at the same depth.
================================================================================
*/
class Solution {
    public TreeNode insertIntoBST(TreeNode root, int val) {
        // let's handle the base case first: empty tree or spot reached -->create and return new leaf node
        if(root == null){
            return new TreeNode(val);
        }

        TreeNode current = root;

        while(true){
            // 1. Target key belongs in the LEFT subtree
            if(val < current.val){
                if(current.left == null){
                    current.left = new TreeNode(val);
                    break;
                }
                current = current.left;
            }

            // 2. Target key belongs in the RIGHT subtree
            if(val > current.val){
                if(current.right == null){
                    current.right = new TreeNode(val);
                    break;
                }
                current = current.right;
            }
        }

        return root;
        
    }
}

/*--
Explanation:

1. Search for Insertion Point:
   - Traverse down the BST from `root` using standard BST comparisons.
   - Since all keys are distinct, `val` will either be smaller or larger than `current.val`.

2. Leaf Attachment Logic:
   - If `val < current.val`, inspect `current.left`. If `null`, attach `new TreeNode(val)` 
     immediately and break. Otherwise, update `current = current.left`.
   - If `val > current.val`, inspect `current.right`. If `null`, attach `new TreeNode(val)` 
     immediately and break. Otherwise, update `current = current.right`.

3. Return Root:
   - Modifies child pointers in-place during traversal, then returns original `root`.
--*/

/*--
Complexity Analysis:

- Time Complexity: O(H) where H is the height of the tree.
  * Balanced BST: O(log N) average time.
  * Degenerate BST: O(N) worst-case time (skewed linked-list structure).
- Space Complexity: O(1) auxiliary space using iterative pointer traversal.
--*/

/**
WALKTHROUGH 1 (Inserting into Leaf Position: root = [4, 2, 7, 1, 3], val = 5):

       4
     /   \
    2     7
   / \
  1   3

Initialization: root = node 4, val = 5, current = root (node 4)

Step 1: current.val = 4, val = 5
  - 5 > 4 -> right branch
  - current.right (node 7) != null -> move current = current.right (node 7)

Step 2: current.val = 7, val = 5
  - 5 < 7 -> left branch
  - current.left == null -> ATTACH NEW LEAF: current.left = new TreeNode(5)
  - Break loop!

Resulting Structure:

       4
     /   \
    2     7
   / \   /
  1   3 5

Returns original root pointer (node 4).
*/