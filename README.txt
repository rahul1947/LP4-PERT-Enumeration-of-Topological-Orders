# Long Project LP4: PERT, Enumeration of Topological Orders 

# Team: LP101 
 * Rahul Nalawade (https://github.com/rahul1947) - rsn170330@utdallas.edu
 * Prateek Sarna (https://github.com/prateek5795) - pxs180012
 * Bhavish Khanna Narayanan (https://github.com/bhavish14) - bxn170002

# End Date: 
 * Sunday, November 25, 2018
_______________________________________________________________________________

# DESCRIPTION: 

#1. Enumerate.java:
   ATTRIBUTES: 
   -----------
   - T[] arr: Array of elements on which enumeration is to be done.
     n = arr.length
      
   - k: number of elements to be selected (default = n). E.g nPk or nCk.  
   
   - count: counts the number of times visit has been called based on the 
     Approver. 
     
   APPROVER<T>: 
   ------------
   - Responsible for selection and de-selection (backtracking) of an 
   element based on certain precedence constraints coded in select() and 
   unselect() methods respectively.
   
   - visit() is called by non-static visit() to print the k elements in the
   arr[0...k-1]
     
   METHODS:
   --------
   A. permute(c): recursive method to visit all valid permutations, where c 
   is number of elements yet to visit.
    
   B. combine(i, c): recursive method to visit all valid combinations, where
   c is the number of element we need to choose from arr[i...n-1].
   
   C. heap(g): recursive method to visit all permutations by a single swap 
   from previous permutation, where first g elements are not frozen, i.e. 
   arr[g...n-1] are frozen, and arr[0..g-1] can only be changed.
   
   D. algorithmL(Comparator c): Knuth's L Algorithm based on Comparator c. 
   
-------------------------------------------------------------------------------
#2. EnumerateTopological.java:
   ATTRIBUTES:
   -----------
   - print: boolean if true prints all enumerations, else no printing.
   
   - selector: Approver of EnumerateTopological itself.
   
   - Vertex[] A: array of vertices in the Graph.
   
   SELECTOR extended from APPROVER<T>
   ----------------------------------
   - select(u): selects u, if it has inDegrees = 0.
   
   - unselect(u): do v.inDegrees++, where edge e = (u,v) exists. 
   
   METHODS:
   --------
   A. initialize(): initializes get(u).inDegrees = u.inDegree for all u.
   
   B. enumerateTopological(): based on selector, enumerates all 
   permutations using permute() from Enumerate.
   Returns count of enumerations from Enumerate itself.

-------------------------------------------------------------------------------
#3. PERT.java: 
   PERTVertex:
   -----------
   - duration: duration of this vertex
   - slack: slack available for this vertex
   - earliestCT: earliest completion time (EC) for this vertex
   - latestCT: latest completion time (LC) for this vertex
   
   METHODS:
   --------
   A. initialize(): add edges from source (first vertex) to all vertices, 
   and all vertices to the sink (last vertex).
   
   B. pert(): Implements PERT by computing slack, EC, LC for all vertices.
   Returns true if Graph is not a DAG, false otherwise.
   NOTE: it uses a topological order from DFS.java  
 _______________________________________________________________________________
 
# RESULTS: 

# Enumerate:
  $java rsn170330.Enumerate

Permutations: 4 3
1 2 3 
1 2 4 
1 3 2 
...
4 1 3 
4 1 2 
Count: 24
_________________________
Combinations: 4 3
1 2 3 
1 2 4 
1 3 4 
2 3 4 
Count: 4
_________________________
Heap Permutations: 4
1 2 3 4 
2 1 3 4 
3 1 2 4 
...
3 2 4 1 
2 3 4 1 
Count: 24
_________________________
Algorithm L Permutations: 
1 2 2 3 3 4 
1 2 2 3 4 3 
1 2 2 4 3 3 
...
4 3 3 2 1 2 
4 3 3 2 2 1 
Count: 180
_________________________


# Enumerate Topological: 
  $java rsn170330.LP4Driver 0 lp4-enumeratetopological/permute-dag-07.txt

+--------------------------------------------------------------------------+
| File          | Output          |   Time (mSec)     | Memory (used/avail)|
|--------------------------------------------------------------------------|
| dag-07.txt    | 105             | 3                 | 1 MB / 117 MB      |
|--------------------------------------------------------------------------|
| dag-08a.txt   | 118             | 2                 | 1 MB / 117 MB      |
|--------------------------------------------------------------------------|
| dag-08b.txt   | 280             | 3                 | 1 MB / 117 MB      |
|--------------------------------------------------------------------------|
| dag-10.txt    | 20              | 1                 | 1 MB / 117 MB      |
|--------------------------------------------------------------------------|
| dag-50-800.txt| 6193152         | 1426              | 7 MB / 117 MB      |
+--------------------------------------------------------------------------+

# PERT: 
  $ java rsn170330.LP4Driver true lp4-pert/lp4-pert4.txt

+--------------------------------------------------------------------------+
| File         | Output          |   Time (mSec)     | Memory (used/avail) |
|--------------------------------------------------------------------------|
| pert1.txt    | 98 52           | 61                | 6 MB / 245 MB       |
|--------------------------------------------------------------------------|
| pert2.txt    | 183 20          | 51                | 3 MB / 345 MB       |
|--------------------------------------------------------------------------|
| pert3.txt    | 596 57          | 157               | 23 MB / 245 MB      |
|--------------------------------------------------------------------------|
| pert4.txt    | 323 42          | 198               | 24 MB / 245 MB      |
+--------------------------------------------------------------------------+


NOTE: 
- Time and Memory might change, as you run the test the program on a 
  different system, but they could be comparable to the above values.
  
  Existing Processor: Intel® Core™ i5-8250U CPU @ 1.60GHz × 8
  Memory: 7.5 GiB
  
- EnumeratePath.java consist of extended version of this project where 
  all paths from source (first vertex) to sink (last vertex) could be 
  enumerated using two algorithms - with and without Selector/ Approver.
  
  NOTE: It uses VertexStack<T> of itself instead of java Stack.
_______________________________________________________________________________

# How to Run:

1. Extract the rsn170330.zip 

2. Compile: 
   $ javac rsn170330/*.java

3. Run: 
   
  [a] Enumerate.java:
  $ java rsn170330.Enumerate n k
  
  where combinations = nCk, permutations = nPk, i.e. 
  n :- number of distinct elements
  k :- number of elements to choose from n
  NOTE: by default n = 4, k = 4. 
  -----------------------------------------------------------------------------
  [b] EnumerateTopological:
  $ java rsn170330.EnumerateTopological [arg0] [arg1]
  $ java rsn170330.EnumerateTopological 0 lp4-enumeratetopological/permute-dag-07.txt
  
  [arg0] :- 1 for verbose i.e. to print all topological orders, otherwise no 
     enumeration of topological orders.
  [arg1] :- file containing the graph. 
  NOTE: by default, verbose = 0 and it has a simple graph in it's main()
  -----------------------------------------------------------------------------
  [c] EnumeratePath:
  $ java rsn170330.EnumeratePath [arg0] [arg1]
  $ java rsn170330.EnumeratePath 1 
  
  [arg0] :- 1 for verbose i.e. to print all paths, otherwise no enumeration of paths
  [arg1] :- file containing the graph. 
  NOTE: by default, verbose = 0 and it has a simple graph in it's main()
  -----------------------------------------------------------------------------
  [d] PERT:
  $ java rsn170330.LP4Driver [arg0] [arg1]
  $ java rsn170330.LP4Driver true lp4-pert/lp4-pert4.txt 
  
  [arg0] :- true for details i.e. to print the PERT chart, otherwise no chart
  [arg1] :- file containing the graph. 
  NOTE: by default, details = false and it has a simple graph in it's main()
  -----------------------------------------------------------------------------
  
NOTE: the current directory must contain rbk directory with rbk/Graph.java
_______________________________________________________________________________

