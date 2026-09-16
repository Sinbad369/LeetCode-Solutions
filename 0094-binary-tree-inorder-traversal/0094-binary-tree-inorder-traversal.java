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


 /**
 SOLUTION USING THE RECURSION:

 class Solution {
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        traverse(root, result);
        return result;        
    }

    private void traverse(TreeNode node, List<Integer> result){
        if(node == null){
            return;
        }

        // traverse left subtree
        traverse(node.left, result);

        // go to the root
        result.add(node.val);

        // traverse right subtree
        traverse(node.right, result);
    }
}
*/

/*
================================================================================
IN-ORDER TRAVERSAL & TREE TRAVERSAL PARADIGMS
================================================================================
Traversal Strategy Trade-Offs:
--------------------------------------------------------------------------------
1. In-Order (Left -> Root -> Right):
   - Visits nodes in strictly non-decreasing (sorted) order for BSTs.
2. Pre-Order (Root -> Left -> Right):
   - Excellent for creating copies or serializing tree structures.
3. Post-Order (Left -> Right -> Root):
   - Essential for bottom-up operations like node deletions or tree destruction.
4. Iterative Stack vs. Recursion:
   - Recursive uses hidden call stack O(H) space; Iterative makes stack allocation
     explicit, preventing stack overflow on deep trees while maintaining O(H) space.
================================================================================
*/

// SOLUTION USING THE ITERATIVE STACK
class Solution {
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        TreeNode current = root;

        while(current != null || !stack.isEmpty()){
        // since it is INNNNORDER traversal, we push the left children to the stack
        while(current != null){
            stack.push(current);
            current = current.left;
        }

        // process the node on top of stack
        current = stack.pop();
        result.add(current.val);

        // move to the right child
        current = current.right;
    }
    return result;
    }    
}

/*--
Explanation:

1. In-Order Rule:
   - For any node `x`, process all nodes in its left subtree BEFORE `x`, 
     and all nodes in its right subtree AFTER `x`.

2. Iterative Stack Mechanics:
   - Outer loop runs as long as there are nodes to process or items on stack.
   - Inner `while` pushes current node and all its left descendants to explicitly
     mimic recursive call stack allocation down the left spine.
   - `stack.pop()` yields the next in-order node; its value is appended to `result`.
   - `current = current.right` redirects processing to examine the right subtree.
--*/

/*--
Complexity Analysis:

- Time Complexity: O(N) where N is the total number of nodes in the binary tree.
  * Each node is pushed and popped from the stack exactly once.
- Space Complexity: O(H) auxiliary space, where H is the height of the tree.
  * Balanced Tree: O(log N) stack depth.
  * Skewed Tree: O(N) stack depth in worst case.
--*/

/**
WALKTHROUGH 1 (Standard Tree: root = [1, null, 2, 3]):

       1
        \
         2
        /
       3

Initialization: result = [], stack = [], current = root (node 1)

Step 1:
  - Inner while pushes node 1. current becomes 1.left (null).
  - Stack: [1]
  - Pop node 1 -> result = [1]
  - Move current = 1.right (node 2)

Step 2:
  - Inner while pushes node 2, then moves left and pushes node 3.
  - Stack: [2, 3]
  - Pop node 3 -> result = [1, 3]
  - Move current = 3.right (null)

Step 3:
  - Inner while skipped (current is null).
  - Pop node 2 -> result = [1, 3, 2]
  - Move current = 2.right (null)

Step 4:
  - current == null && stack is empty -> loop terminates!

Result: [1, 3, 2]
*/