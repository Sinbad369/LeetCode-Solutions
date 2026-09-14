import java.util.LinkedList;

/*
================================================================================
SEPARATE CHAINING HASH SET (OPEN HASHING)
================================================================================
Why Separate Chaining? (Restructuring Approach)
--------------------------------------------------------------------------------
Unlike Open Addressing techniques (Linear Probing, Quadratic Probing, Double
Hashing) which store colliding items directly inside alternative array slots:

1. Separate Chaining allocates a dynamic Linked List (a chain) at each index 
   of the main hash table array.
2. When a collision occurs (h(key1) == h(key2)), the new key is simply appended 
   to the end of the existing linked list at table[index].
3. Prevents table saturation: The hash table never truly gets "full".
4. Avoids Clustering: Prevents primary and secondary clustering issues seen in 
   probing techniques.
5. Simple Deletion: No need for "Lazy Deletion" or tombstone markers ("DELETED"), 
   since items are cleanly unlinked from the bucket's list.

------------------------------------------------------------------------------------------
COMPARISON TABLE (Collision Resolution Methods):
------------------------------------------------------------------------------------------
Technique         | Collision Mechanism       | Table Full Limit   | Deletion Strategy
------------------+---------------------------+--------------------+----------------------
Linear Probing    | Checks table[i+1, i+2...] | Fixed (M slots)    | Requires Tombstones
Quadratic Probing | Checks table[i+1^2, 2^2]  | Fixed (M slots)    | Requires Tombstones
Double Hashing    | Uses second hash h2(key)  | Fixed (M slots)    | Requires Tombstones
Separate Chaining | Appends to LinkedList     | Unlimited (Dynamic)| Direct Node Unlinking
==========================================================================================
*/

class MyHashSet {
    // Choice of M = 769: A medium-sized prime number to minimize collisions 
    // and distribute keys uniformly according to slide hashing rules.
    private static final int BASE = 769;
    private LinkedList<Integer>[] bucket;

    @SuppressWarnings("unchecked")
    public MyHashSet() {        
        // Step 1: Initialize array of size M (BASE)
        bucket = new LinkedList[BASE];
        
        // Step 2: Instantiate an empty linked list at every bucket index
        for (int i = 0; i < BASE; i++) {
            bucket[i] = new LinkedList<>();
        }
    }

    // Hash Function: h(key) = key mod M     ---> M here is the BASE - the size - the length
    // Maps any incoming integer key to a valid array index in [0, BASE - 1]
    private int hash(int key) {
        return key % BASE;
    }

    // Insertion: O(1) average time
    public void add(int key) {
        int index = hash(key);
        // Check if key is already in the chain to maintain Set uniqueness
        if (!bucket[index].contains(key)) {
            bucket[index].add(key); // Append key to end of chain
        }
    }
    
    // Deletion: O(1) average time
    public void remove(int key) {
        int index = hash(key);
        // Explicit cast (Integer) ensures removing by VALUE, not array index!
        bucket[index].remove((Integer) key);
    }
    
    // Search: O(1) average time
    public boolean contains(int key) {
        int index = hash(key);
        // Traverses the linked list at bucket[index] to locate key
        return bucket[index].contains(key);
    }
}

/*--
Explanation:

1. Initialization:
   - An array of linked lists (`bucket`) of prime size M = 769 is created.

2. Primary Operations:
   - add(key): Computes `index = key % 769`. If `key` is not present in 
     `bucket[index]`, it is appended.
   - remove(key): Computes `index = key % 769`. Searches the chain at 
     `bucket[index]` and unlinks `key` if found.
   - contains(key): Computes `index = key % 769` and scans the corresponding 
     chain returning true if present.
--*/

/*--
Complexity Analysis:

- Primary Hash Time: O(1)
- Operations (Add, Remove, Contains):
  * Average Case: O(1 + alpha), where Load Factor alpha = n / m. 
    If keys are uniformly distributed, list lengths remain small.
  * Worst Case: O(N), occurs if all N keys hash to the exact same index bucket.

- Space Complexity: O(M + N) where M is table capacity (769) and N is the 
  total number of unique keys inserted into the set.
--*/

/**
WALKTHROUGH (Tracing Example 1 from LeetCode):

Input Operations:
  MyHashSet set = new MyHashSet();
  set.add(1);        -> hash(1) = 1 % 769 = 1. Bucket[1] = [1]
  set.add(2);        -> hash(2) = 2 % 769 = 2. Bucket[2] = [2]
  set.contains(1);   -> hash(1) = 1. Checks Bucket[1], finds 1 -> returns true
  set.contains(3);   -> hash(3) = 3. Checks Bucket[3], empty  -> returns false
  set.add(2);        -> hash(2) = 2. Bucket[2] already contains 2 -> ignored
  set.contains(2);   -> hash(2) = 2. Checks Bucket[2], finds 2 -> returns true
  set.remove(2);     -> hash(2) = 2. Unlinks 2 from Bucket[2] -> Bucket[2] = []
  set.contains(2);   -> hash(2) = 2. Checks Bucket[2], empty  -> returns false
*/