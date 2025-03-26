Recommended Java JDK version: 17.0.12 or newer.

This TSPsolver is an updated version of "https://github.com/tuomasth/TSPsolver_v14-3-2017".

 F2 NNH                   Nearest neighbour as the simplest heuristic there exists. 
 
 F3 2MST                  Using minimum spanning tree's doubled edges and Euler tour. 
 
 F4 CHH                   Convex hull around everything and connect the (closest) inner nodes one by one. 
 
 F5 CHRI                  Christofides heuristic, using minimum spanning tree and its odd degree node (min) matching and Euler tour. 
 
 F6 OPT-NNH-CHH-CHRI      F2 for a couple of times, F4 once, F5 once, choose the best and try to improve with quick opts. 
 
 F7 SOM-CH-NN             Calculate the convex hull so its nodes (or edge centroids) can be the input nodes and clusters, then 
                          the inner nodes are movable neurons that perform the Kohonen Self-Organizing Map algorithm, finally 
                          each cluster performs the NNH which chains everything and creates the Hamiltonian circuit. 
                          
 F8 SOM-CH-NN-EVO         Same as F7 but with 2 times more clusters and evolution is used, the population consists 
                          of chromosomes (multiple F7 results), the movable neurons also have logic stacks that tell what to do 
                          after moving towards the SOM goal, the programmer can make their own logic fragments to the stacks. 
                          
 F9 OPT-SOM-CH-NN-EVO     Same as F8 but the F6's quick opts are used in the end, parameters can be changed before the run. 

 F1                       (About the Java application.) 
