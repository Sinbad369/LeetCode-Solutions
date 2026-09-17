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
BINARY SEARCH TREE DELETION & STRUCTURAL RECONSTRUCTION
================================================================================
BST Deletion Scenarios & Mechanics:
--------------------------------------------------------------------------------
1. Case 1: Node is a Leaf (No Children)
   - Simply remove the node by returning null to the parent pointer.
2. Case 2: Node Has One Child
   - Bypass the target node by returning its non-null child directly to the parent.
3. Case 3: Node Has Two Children
   - Cannot simply delete without severing tree properties.
   - Find the In-Order Successor (smallest value in the right subtree).
   - Replace target node's value with successor's value.
   - Recursively delete the successor node from the right subtree.
================================================================================
*/
class Solution {
    public TreeNode deleteNode(TreeNode root, int key) {
        if(root == null){
            return null; // searched key is not found in the tree
        }
        
        // search for target node
        if(key < root.val){
            root.left = deleteNode(root.left, key);
        }else if(key > root.val){
            root.right = deleteNode(root.right, key);
        }else{
            // node to be deleted is found, so handle the deletion cases
            // case1 and case2 (no left or no right child)
            if(root.left == null){
                return root.right;
            } else if(root.right == null){
                return root.left;
            }

            // case3: node has two children
            // find the in-order successor (minimum node in the right-subtree)
            TreeNode minimumNode = findMin(root.right);

            // we overwrite current node's value with successor value
            root.val = minimumNode.val;

            // we delete the duplicate successor node from the right-subtree
            root.right = deleteNode(root.right, minimumNode.val);
        }

        return root;
        
    }

    private TreeNode findMin(TreeNode node){
        while(node.left != null){
            node = node.left;
        }
        return node;
    }
}

/*--
Explanation:

1. Recursive Search:
   - Navigates down left or right subtrees using BST properties until `root.val == key`.

2. Pointer Rebinding Trick:
   - Returning `root.right` or `root.left` when a node has 0 or 1 children allows the
     parent call `root.left = deleteNode(...)` to instantly rebind pointers without
     needing parent tracking pointers.

3. Two-Child Substitution:
   - Swapping values with the in-order successor reduces a complex 2-child deletion
     into a simple 0-child or 1-child deletion on the right branch.
--*/

/*--
Complexity Analysis:

- Time Complexity: O(H) where H is tree height.
  * Balanced BST: O(log N) average time.
  * Degenerate BST: O(N) worst-case time.
- Space Complexity: O(H) auxiliary space for recursive call stack.

--*/

/**
WALKTHROUGH 1 (Deleting Node with Two Children: root = [5, 3, 6, 2, 4, null, 7], key = 3):

       5
     /   \
    3     6
   / \     \
  2   4     7

Step 1: deleteNode(5, 3) -> 3 < 5 -> calls deleteNode(3, 3) on root.left.
Step 2: deleteNode(3, 3) -> Match found at node 3!
        - Node 3 has left (2) AND right (4) children -> Trigger Case 3.
Step 3: findMin(node 4) -> Returns node 4 (smallest key in right subtree).
Step 4: Overwrite root 3's value with 4 -> Tree temporarily has duplicate '4' values.
Step 5: Recursive call deleteNode(root.right, 4) removes the leaf node 4.
Step 6: Pointers rebind upward.

Resulting Structure:

       5
     /   \
    4     6
   /       \
  2         7

*/