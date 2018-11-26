# LP4-PERT-Enumeration-of-topological-orders 

/**
 * CS 5V81.001: Implementation of Data Structures and Algorithms 
 * Long Project LP4: PERT, Enumeration of topological orders
 * Team: LP101
 * @author Rahul Nalawade (rsn170330)
 * @author Prateek Sarna (pxs180012)
 * @author Bhavish Khanna Narayanan (bxn170002)
 * 
 */

1. Extract the rsn170330.zip 

2. Compile: 
   $javac rsn170330/*.java

3. Run: 
   $java rsn170330.LP4Driver [optional: arguments]

NOTE: the current directory must contain rbk directory with rbk/Graph.java

----------------------------------------------------------------------------
#DESCRIPTION: 

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
   
 
----------------------------------------------------------------------------
#RESULTS: 

#Enumerate

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


#Enumerate Topological

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

#PERT

+--------------------------------------------------------------------------+
| File         | Output          |   Time (mSec)     | Memory (used/avail) |
|--------------------------------------------------------------------------|
| Pert1.txt    | 98 52           | 61                | 6 MB / 245 MB       |
|--------------------------------------------------------------------------|
| Pert2.txt    | 183 20          | 51                | 3 MB / 345 MB       |
|--------------------------------------------------------------------------|
| Pert3.txt    | 596 57          | 157               | 23 MB / 245 MB      |
|--------------------------------------------------------------------------|
| Pert4.txt    | 323 42          | 198               | 24 MB / 245 MB      |
+--------------------------------------------------------------------------+


NOTE: 
- Time and Memory might change, as you run the test the program on a 
  different system, but they could be comparable to the above values.

- All necessary results are stored in the result directory for reference.

- EnumeratePath.java consist of extended version of this project where 
  all paths from source (first vertex) to sink (last vertex) could be 
  enumerated using algorithms from the class.
  
  NOTE: It uses VertexStack<T> of itself instead of java Stack.
