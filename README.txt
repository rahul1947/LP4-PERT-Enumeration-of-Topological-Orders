# Long Project LP4: PERT, Enumeration of Topological Orders 

# Team: LP101 
 * Rahul Nalawade (https://github.com/rahul1947) 
   rsn170330@utdallas.edu 
 * Prateek Sarna (https://github.com/prateek5795)  
   pxs180012@utdallas.edu 
 * Bhavish Khanna Narayanan (https://github.com/bhavish14) 
   bxn170002@utdallas.edu 
 
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
   
   NOTE: 
   Counting and Enumerating Topological Orders uses the same algorithm.

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
$ java rsn170330.EnumerateTopological 0 rsn170330/lp4-test/enumtop-t08.txt 

+-------------------------------------------------------------------------+
| File        |   |V| |E|  |  Output  | Time (mSec) | Memory (used/avail) |
|-------------------------------------------------------------------------|
| enumtop-t01 |        7 6 |      105 |           2 |       1 MB / 117 MB |
|-------------------------------------------------------------------------|
| enumtop-t02 |        8 9 |      280 |           4 |       1 MB / 117 MB |
|-------------------------------------------------------------------------|
| enumtop-t03 |       8 11 |      118 |           3 |       1 MB / 117 MB |
|-------------------------------------------------------------------------|
| enumtop-t04 |      10 30 |       20 |           2 |       1 MB / 117 MB |
|-------------------------------------------------------------------------|
| enumtop-t05 |     20 100 |     3864 |          14 |       3 MB / 117 MB |
|-------------------------------------------------------------------------|
| enumtop-t06 |     30 300 |   107136 |          60 |       7 MB / 117 MB |
|-------------------------------------------------------------------------|
| enumtop-t07 |     40 500 |    38052 |          31 |       5 MB / 117 MB |
|-------------------------------------------------------------------------|
| enumtop-t08 |     50 800 |  6193152 |        1390 |       7 MB / 117 MB |
|-------------------------------------------------------------------------|
| enumtop-t09 |     50 900 |   552960 |         653 |       4 MB / 117 MB |
|-------------------------------------------------------------------------|
| enumtop-t10 |   100 4000 |    29160 |         612 |      12 MB / 117 MB |
|-------------------------------------------------------------------------|
| enumtop-t11 |  200 18000 |   768000 |        2512 |      22 MB / 147 MB |
+-------------------------------------------------------------------------+

NOTE: 
  |V|: Number of Vertices in the Graph
  |E|: Number of Edges in the Graph
  Output: Total number of all valid permutations of Topological Orderings

Refer lp4-enumtop.txt as obtained from 
$ ./lp4-enumtop.sh > lp4-enumtop.txt
_______________________________________________________________________________

# PERT: 
$ java rsn170330.PERT false rsn170330/lp4-test/pert-t04.txt

+-------------------------------------------------------------------------+
| File      |   |V| |E|   |   Output  | Time (mSec) | Memory (used/avail) |
|-------------------------------------------------------------------------|
| pert-t01  |     102 300 |    183 20 |           5 |       2 MB / 117 MB |
|-------------------------------------------------------------------------|
| pert-t02  |     52 1200 |     98 52 |           6 |       4 MB / 117 MB |
|-------------------------------------------------------------------------|
| pert-t03  |    102 1000 |     97 34 |           5 |       4 MB / 117 MB |
|-------------------------------------------------------------------------|
| pert-t04  |     502 675 |     89 64 |           9 |       3 MB / 117 MB |
|-------------------------------------------------------------------------|
| pert-t05  |   1002 1166 |     61 46 |          12 |       5 MB / 117 MB |
|-------------------------------------------------------------------------|
| pert-t06  |    502 6000 |    596 57 |          12 |      16 MB / 117 MB |
|-------------------------------------------------------------------------|
| pert-t07  |    502 6000 |     84 65 |          11 |      16 MB / 117 MB |
|-------------------------------------------------------------------------|
| pert-t08  |   1002 6000 |     99 26 |          12 |      17 MB / 117 MB |
|-------------------------------------------------------------------------|
| pert-t09  |   1002 6000 |    323 42 |          14 |      17 MB / 117 MB |
+-------------------------------------------------------------------------+
NOTE:
  Output: x y:
  x: Minimum Time needed to complete the Project/ Critical Path Length
  y: Number of Critical Nodes in the Graph

Refer lp4-pert.txt as obtained from 
$ ./lp4-pert.sh > lp4-pert.txt

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
  NOTE: by default n = 4, k = 3 
  -----------------------------------------------------------------------------
  [b] EnumerateTopological:
  $ java rsn170330.EnumerateTopological [arg0] [arg1]
  $ java rsn170330.EnumerateTopological 0 rsn170330/lp4-test/enumtop-t08.txt 
  
  [arg0] :- true for verbose i.e. to print all topological orders, otherwise no 
     enumeration of topological orders
  [arg1] :- file containing the graph 
  NOTE: by default, verbose = false and it has a simple graph in it's main()
  -----------------------------------------------------------------------------
  [c] EnumeratePath:
  $ java rsn170330.EnumeratePath [arg0] [arg1]
  $ java rsn170330.EnumeratePath 1 
  
  [arg0] :- true for verbose i.e. to print all paths, otherwise no enumeration of 
     paths
  [arg1] :- file containing the graph. 
  NOTE: by default, verbose = false and it has a simple graph in it's main()
  -----------------------------------------------------------------------------
  [d] PERT:
  $ java rsn170330.PERT [arg0] [arg1]
  $ java rsn170330.PERT false rsn170330/lp4-test/pert-t04.txt 
  
  [arg0] :- true for details i.e. to print the PERT chart, otherwise no chart
  [arg1] :- file containing the graph. 
  NOTE: by default, details = false and it has a simple graph in it's main()
  -----------------------------------------------------------------------------
  
NOTE: the current directory must contain rbk directory with rbk/Graph.java
_______________________________________________________________________________