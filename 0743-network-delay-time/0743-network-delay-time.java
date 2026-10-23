/*
================================================================================
LEETCODE 743: NETWORK DELAY TIME
================================================================================
PROBLEM SUMMARY:
- Nodes: 1 to n (1-indexed).
- Directed Edges: times[i] = [u, v, w] (u -> v with travel time w).
- Source Node: k.
- Objective: Find the minimum time for ALL n nodes to receive the signal.
  If any node is unreachable, return -1.

WHY DIJKSTRA'S ALGORITHM?
- Single-Source Shortest Path: We start from source node k.
- Signal Propagation: Signals travel along all paths in parallel. The time it 
  takes for ALL nodes to get the signal is the MAXIMUM of all shortest path 
  distances from node k.
================================================================================
*/
class Solution {
    public int networkDelayTime(int[][] times, int n, int k) {
        
        // step1: build adjacency list for the directed graph
        // map: u -> list of [v, w]
        Map<Integer, List<int[]>> graph = new HashMap<>();
        for(int[] time: times) {
            int u = time[0];
            int v = time[1];
            int w = time[2];
            graph.computeIfAbsent(u, x -> new ArrayList<>()).add(new int[]{v, w});
        }

        // step2: initialize distances array with infinity (1-indexed nodes: 1 to n)
        int[] dist = new int[n + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);

        // Distance to source node k is 0
        dist[k] = 0;

        // step3: priority queue storing [node, current_total_distance]
        // min-heap ordered by total distance
        PriorityQueue<int[]> pq = new PriorityQueue<>((a,b) -> Integer.compare(a[1], b[1]));
        pq.offer(new int[]{k, 0});

        // step4: dijkstra's relaxation loop
        while(!pq.isEmpty()) {
            int[] current = pq.poll();
            int u = current[0];
            int d = current[1];

            // optimization: if extracted distance is larger than known shortest distance, skip
            if(d > dist[u]) continue;

            // check all outgoing neighbors
            if(graph.containsKey(u)){
                for(int[] neighbor : graph.get(u)){
                    int v = neighbor[0]; 
                    int weight = neighbor[1]; 

                    // relaxation check: d_u + w(u, v) < d_v
                    if(dist[u] + weight < dist[v]){
                        dist[v] = dist[u] + weight;
                        pq.offer(new int[]{v, dist[v]}); // DecreaseKey operation
                    }
                }
            }
        }

        // step5: find the maximum time among all notes 1 to n
        int maxDelay = 0;
        for(int i = 1; i <= n; i++){
            if(dist[i] == Integer.MAX_VALUE){
                return -1; // Node i was never reached!
            }
            maxDelay = Math.max(maxDelay, dist[i]);
        }

        return maxDelay;
    }
}

/*
================================================================================
COMPLEXITY ANALYSIS
================================================================================
1. TIME COMPLEXITY: O(|E| log |V|)
   - Building the graph takes O(|E|) time.
   - Each node is popped from the Priority Queue once: O(|V| log |V|).
   - Every edge is relaxed at most once, inserting into the heap: O(|E| log |V|).
   - Total Time: O((|V| + |E|) log |V|) = O(|E| log |V|) for connected graphs.

2. SPACE COMPLEXITY: O(|V| + |E|)
   - Adjacency List: O(|V| + |E|) space to store all edges and neighbors.
   - Distance Array: O(|V|) space to track shortest distances for 1..n nodes.
   - Priority Queue: O(|E|) space in the worst case to hold pending nodes.
   - Total Space: O(|V| + |E|).
================================================================================
*/

/*
--------------------------------------------------------------------------------
STEP-BY-STEP WALKTHROUGH EXAMPLE:
--------------------------------------------------------------------------------
Given: n = 4, k = 2, times = [[2,1,1], [2,3,1], [3,4,1]]

Graph Visualized:
  (1) ◄─── 1 ─── (2) ─── 1 ───► (3) ─── 1 ───► (4)
                  ▲
            Signal Starts Here (k = 2, t = 0)

1. INITIALIZATION:
   - dist = [∞, ∞, 0, ∞, ∞] (Node 2 starts at cost 0)
   - pq = { node: 2, distance: 0 }

2. STEP 1: Pop Node 2 (t = 0)
   - Neighbor 1: 0 + 1 = 1 < ∞ -> update dist[1] = 1, push node 1
   - Neighbor 3: 0 + 1 = 1 < ∞ -> update dist[3] = 1, push node 3
   - pq state: { node: 1 (dist: 1), node: 3 (dist: 1) }

3. STEP 2: Pop Node 1 (t = 1)
   - Node 1 has no outgoing edges -> Stop branch.
   - pq state: { node: 3 (dist: 1) }

4. STEP 3: Pop Node 3 (t = 1)
   - Neighbor 4: 1 + 1 = 2 < ∞ -> update dist[4] = 2, push node 4
   - pq state: { node: 4 (dist: 2) }

5. STEP 4: Pop Node 4 (t = 2)
   - Node 4 has no outgoing edges -> Stop branch.
   - pq state: {} (Empty!)

6. FINAL RESULT:
   - dist array = [1, 0, 1, 2] (for nodes 1, 2, 3, 4)
   - maxDelay = max(1, 0, 1, 2) = 2
*/