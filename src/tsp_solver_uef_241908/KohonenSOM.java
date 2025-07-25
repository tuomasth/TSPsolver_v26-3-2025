package tsp_solver_uef_241908;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.Random;

/**
 * TSP Solver by Tuomas Hyvönen, Java file 5 of 11 
 * A class for the PhD Teuvo Kalevi Kohonen's (11 Jul, 1934 – 13 Dec, 2021) Self-Organizing Map (SOM). 
 * Keep choosing a random input node and move the neuron vertices towards that Best Matching Unit node. 
 * Finally, move with own logic if logic stacks are used. 
 * Also, checking that no points are on the tops of each other. 
 * 
 * Open source Java code, feel free to edit and try your own improvements. 
 * Tested with Windows 11 
 * Apache NetBeans 25 
 * Java JDK 17 64bit 
 * 
 * @author Tuomas Hyvönen 
 * @version 2.1
 */
public class KohonenSOM {
    // https://www.youtube.com/watch?v=H9H6s-x-0YE 
    // tutorial uploaded by Thales Sehn Körting, 8/June/2013 
    // watched the video on 25/March/2025 
    
    /**
     * Generates a new regular SOM, in other words a Kohonen Self-Organizing Map.
     * The map's purpose is to get some target points and move all the 
     * movable "neurons" towards the targets like a net. Then the neurons are clustered.
     * A good Gif animation: https://en.wikipedia.org/wiki/Self-organizing_map#/media/File:TrainSOM.gif 
     * (by "Chompinha", visited on 22/March/2025) 
     * 
     * Returns both updated XY neuron coordinate parameters with a classification 
     * set for them, cluster(classification) count = input point count.
     * 
     * The "maxPossibleDistanceInHull" should be calculated beforehand so that 
     * all the hull vertices (=inputs) are iterated with 2 for loops and the max 
     * exact Euclidean distance between any of them is in the variable.
     * (The distances can be designed to be orthogonal only.)
     * 
     * @param maxIterations int
     * @param maxPossibleDistanceInHull double
     * @param inputCoordinateXs ArrayList
     * @param inputCoordinateYs ArrayList
     * @param neuronCoordinateXs ArrayList
     * @param neuronCoordinateYs ArrayList
     * @param useLogicStacks boolean
     * @param c Chromosome
     * @param learningRate double
     * @return answer ArrayList
     */
    public static ArrayList performSOM(int maxIterations, double maxPossibleDistanceInHull, 
            ArrayList inputCoordinateXs, ArrayList inputCoordinateYs, 
            ArrayList neuronCoordinateXs, ArrayList neuronCoordinateYs, 
            boolean useLogicStacks, Chromosome c, double learningRate) {          
        
        int inputVerticeCount = inputCoordinateXs.size();    // number of rows or columns
        int neuronCount = neuronCoordinateXs.size();         // number of neurons (inner vertices when the convex hull is the input)
        //double maxNR = -1;               // max radius of grid if wanted (like a grid of squares or hexagons)
        double neighborhoodRadius = maxPossibleDistanceInHull/1.5;  // radius with neighborhood
                                           // learningRate is how intensively the shape is learned, should keep decreasing 
        //int numberOfDimensions = -1;     // dimensions 
        double distance;                   // Euclidean distance between two neurons, see Sub_algorithms.java for distance
        int winnerBMU = -1;                // Index of the winner Best Matching Unit
        
        // THE IDEA: 
        // 1) Selecting a random input point all the time from the input points, then
        // 2) finding the closest Best Matching Unit neuron with the Euclidean distance (squared), 
        // 3) traversing all the neurons so that the closest BMU moves the most, 
        //    others move only a little and less and even less, depending on the distance.
        // 4) Check that no points are on each other with the exact same coordinates.
        // 5) Repeat the steps from 1 to 4 (for maxIterations), 
        // 6) finally, classify the data into clusters, Euclidean distance with inputs is the measurement.
        
        if(inputCoordinateXs.size() != inputCoordinateYs.size()) {
            System.out.println("inputCoordinateXs.size() != inputCoordinateYs.size()");
            return null;
        }
        if(neuronCoordinateXs.size() != neuronCoordinateYs.size()) {
            System.out.println("neuronCoordinateXs.size() != neuronCoordinateYs.size()");
            return null;
        }
        /**     // just a casual try I once had in mind 
        for(int a = 0; a < 2; a++) { // for extra 2 times 
            if(a == 2) {
                learningRate = (learningRate+learningRate)/4;
            }
            **/
            for(int i = 0; i < maxIterations; i++) {

                int max = inputCoordinateXs.size()-1;
                int min = 0;
                int range = (max - min) + 1;     
                int randomIndex = (int)(Math.random() * range) + min;
                //System.out.println("\nRandom's index is " + randomIndex + "\n");

                double bestKnownDistance = Double.MAX_VALUE;
                double[] weights = new double[neuronCount];

                for(int j = 0; j < neuronCount; j++) {
                    // finding out the Best Matching Unit (has nothing to do with Christofides heuristic matching)
                    distance = Sub_algorithms.Euclidean_distance_squared(
                            (double)neuronCoordinateXs.get(j), (double)neuronCoordinateYs.get(j),
                            (double)inputCoordinateXs.get(randomIndex), (double)inputCoordinateYs.get(randomIndex));

                    if(distance < bestKnownDistance) {
                        bestKnownDistance = distance;
                        winnerBMU = j;
                        //System.out.println("Winner BMU index set to " + winnerBMU);
                    }
                }
                
                for(int j = 0; j < neuronCount; j++) {
                    distance = Sub_algorithms.Euclidean_distance_squared(
                            (double)neuronCoordinateXs.get(j), (double)neuronCoordinateYs.get(j),
                            (double)neuronCoordinateXs.get(winnerBMU), (double)neuronCoordinateYs.get(winnerBMU));
                    
                    if(distance < maxPossibleDistanceInHull) { 
                        weights[j] -= (sqrt(distance));
                    }
                    else {
                        weights[j] = maxPossibleDistanceInHull;
                    }
                }

                learningRate *= 0.99; // decreasing the learning rate, can be edited how fast 
                
                ArrayList movedCoord= new ArrayList();
                for(int j = 0; j < neuronCount; j++) {
                    
                    // adjusting the weights (in Euclidean cartesian coordinate space, there are Xs and Ys) 
                    // traverse the neurons normally (percentage * weights/100) --- max weight should be 1 
                    
                    double neuronWithInputDist = Sub_algorithms.Euclidean_distance((double)neuronCoordinateXs.get(j),
                                                                                   (double)neuronCoordinateYs.get(j), 
                                                                                   (double)inputCoordinateXs.get(randomIndex), 
                                                                                   (double)inputCoordinateYs.get(randomIndex));
                    if(neuronWithInputDist < neighborhoodRadius) {
                        double movingPercentage = learningRate * ((weights[j])/100); 

                        movedCoord = traverse(       // The important traversing phase 
                                (double)neuronCoordinateXs.get(j),  // movingPercentage calculation is not the official recommendation
                                (double)inputCoordinateXs.get(randomIndex), // W(s) + theta(u,v,s) *alpha(s)*(D(t)-W(s)) is the official 
                                (double)neuronCoordinateYs.get(j), 
                                (double)inputCoordinateYs.get(randomIndex), 
                                movingPercentage);
                        neuronCoordinateXs.set(j, movedCoord.get(0));
                        neuronCoordinateYs.set(j, movedCoord.get(1));
                    }
                    
                    //System.out.println("X " + neuronCoordinateXs.toString());
                    //System.out.println("Y " + neuronCoordinateYs.toString());
                    ArrayList helpList;
                    helpList = makeSureThatThereAreNoEqualVertices(neuronCoordinateXs, neuronCoordinateYs);
                    int insertIndex = 0;
                    for(int k = 0; k < helpList.size()/2; k+=2) {
                        neuronCoordinateXs.set(insertIndex, helpList.get(k));
                        insertIndex++;
                    }
                    insertIndex = 0;
                    for(int k = 1; k < helpList.size()/2; k+=2) {
                        neuronCoordinateYs.set(insertIndex, helpList.get(k));
                        insertIndex++;
                    }
                    //System.out.println("x " + neuronCoordinateXs.toString());
                    //System.out.println("y " + neuronCoordinateYs.toString());
                    //Sub_algorithms.makeSureThatThereAreNoEqualVertices(inputCoordinateXs, inputCoordinateYs);
                }
                
                if(useLogicStacks) {
                    // make the neurons have a feeling of "on a second thought, I'll also move as I want"
                    for(int j = 0; j < neuronCount; j++) {
                        // j should point to a chromosome that has logic stacks 
                        int moveDecision = c.getStack().top();
                        c.getStack().pop();
                        
                        double movingPercentageForFragments = TSP_Solver_UEF_241908.getMovingPercentageForFragments();
                        
                        ArrayList x_coord = new ArrayList();
                        ArrayList y_coord = new ArrayList();
                        for(int k = 0; k < neuronCoordinateXs.size(); k++) {
                            x_coord.add(neuronCoordinateXs.get(k));
                            y_coord.add(neuronCoordinateYs.get(k));
                        }
                        
                        switch(moveDecision) {  // 0 = sprout start index, NO ZERO ALLOWED SO 1 
                             case 1: {
                                 // when commented, do nothing 
                                 // movedCoord = Logic_Fragments.nnhSprout3AndMoveTowards3rd(movingPercentage, 1, x_coord, y_coord); 
                                 break;
                             }
                             case 2: {
                                 movedCoord = Logic_Fragments.towardsRandom(movingPercentageForFragments, 1, x_coord, y_coord, true);
                                 //traverse boost 
                                 break;
                             }
                             case 3: {
                                 movedCoord = Logic_Fragments.towardsRandom(movingPercentageForFragments, 1, x_coord, y_coord, false);
                                 // no boost 
                                 break;
                             }
                             case 4: {
                                 movedCoord = Logic_Fragments.moveTowardsTheClosest(movingPercentageForFragments, 1, x_coord, y_coord); 
                                 break;
                             }
                             case 5: {
                                 movedCoord = Logic_Fragments.moveAwayFromTheClosestThenUDLR(movingPercentageForFragments, 1, x_coord, y_coord); 
                                 break;
                             } // Comment (//) the method calls above in order to ignore them. 
                             // Write your own cases 6, 7, 8 etc. here if you make more logic fragments. 
                             // The other option: overwrite the existing fragments 1-5 and possibly rename them. 
                             case 6: { 
                                 // (3 of 3 places) 
                                 //movedCoord = Logic_fragments.myOwnImplementationJavaMethodCall(movingPercentage, 1, x_coord, y_coord); 
                                 break;
                             }
                        } // when adding new logic fragment methods into "Logic_fragments.java", remember to add the calls here too^! 
                        
                        if(!movedCoord.isEmpty()) {
                            neuronCoordinateXs.set(j, movedCoord.get(0));
                            neuronCoordinateYs.set(j, movedCoord.get(1));
                        }
                            ArrayList helpList;
                            helpList = makeSureThatThereAreNoEqualVertices(neuronCoordinateXs, neuronCoordinateYs);
                            int insertIndex = 0;
                            for(int k = 0; k < helpList.size()/2; k+=2) {
                                neuronCoordinateXs.set(insertIndex, helpList.get(k));
                                insertIndex++;
                            }
                            insertIndex = 0;
                            for(int k = 1; k < helpList.size()/2; k+=2) {
                                neuronCoordinateYs.set(insertIndex, helpList.get(k));
                                insertIndex++;
                            }
                        
                    }
                }
            }
        //}
        
        //System.out.println("performSOM - " + neuronCoordinateXs.toString() + ", " + neuronCoordinateYs.toString());
        
        ArrayList answer = new ArrayList(); // xyC xyC xyC xyC xyC xyC ... where C = cluster, 0 is included
        // clusterings for the neurons:
        int[] clusters = new int[neuronCount];
        
        for(int i = 0; i < neuronCount; i++) {
            double bestKnownDistance = Double.MAX_VALUE;
            for(int j = 0; j < inputVerticeCount; j++) {
                distance = Sub_algorithms.Euclidean_distance_squared(
                        (double)neuronCoordinateXs.get(i), (double)neuronCoordinateYs.get(i),
                        (double)inputCoordinateXs.get(j), (double)inputCoordinateYs.get(j));
                if(distance < bestKnownDistance) {
                    bestKnownDistance = distance;
                    clusters[i] = j;
                }
            }
        }
        
        for(int i = 0; i < neuronCount; i++) {
            answer.add(neuronCoordinateXs.get(i));
            answer.add(neuronCoordinateYs.get(i));
            answer.add(clusters[i]);
        }
        //System.out.println(answer.toString() + ", size is: " + answer.size() + " after performSOM\n");
        return answer;
    }
    
    /**
     * Traversing (moving) points to a direction.
     * Threshold value means "going towards the target point this much per cent".
     * 0 = no movement
     * 0.5 = go half the way
     * 0.75 = go 3/4 closer
     * 1 = go on the target point
     * 
     * (2 would be "mirror yourself over the target point" (which becomes the central in 50:50 scale))
     * 
     * @param moving_point_s_X double
     * @param target_point_s_X double
     * @param moving_point_s_Y double
     * @param target_point_s_Y double
     * @param threshold double
     * @return ret_values ArrayList
     */
    public static ArrayList traverse(double moving_point_s_X, double target_point_s_X, 
                                    double moving_point_s_Y, double target_point_s_Y, 
                                    double threshold) {
        if(threshold <= -1) {
            System.out.println("Threshold <= 100% in traverse method.");
            return null;
        }
        if(threshold >= 1) {
            ArrayList ret_values = new ArrayList();
            ret_values.add(moving_point_s_X);
            ret_values.add(moving_point_s_Y);
            return ret_values;
        }
        if((moving_point_s_X >= Double.MAX_VALUE /2) || 
           (moving_point_s_Y >= Double.MAX_VALUE /2) || 
           (target_point_s_X >= Double.MAX_VALUE /2) || 
           (target_point_s_Y >= Double.MAX_VALUE /2)) {
            System.out.println("Traverse method - too high risk of getting a double overflow");
            return null;
        }
        else {
            // everything is ok, do the actual computation:
            double travelled_X = moving_point_s_X;
            double travelled_Y = moving_point_s_Y;
            travelled_X += (target_point_s_X - moving_point_s_X) * threshold; // if threshold is negative, moving away 
            travelled_Y += (target_point_s_Y - moving_point_s_Y) * threshold; // if positive, moving towards 
            ArrayList ret_values = new ArrayList();
            ret_values.add(travelled_X);
            ret_values.add(travelled_Y);
            return ret_values;
        }
    }
    
    /**
     * This method will make sure that there are no vertices with equal coordinates.
     * If equal coordinates exist, one will be changed a bit like for example:
     * 6.0 -> 6.0000000003,    14.5 -> 14.5000000011     etc. 
     * 
     * Should be called always after traversing because the coordinates might
     * get on each other by accident. If 2 equal vertices are acceptable on purpose, then never mind this.
     * 
     * @param Xs ArrayList
     * @param Ys ArrayList
     * @return answer ArrayList
     */
    public static ArrayList makeSureThatThereAreNoEqualVertices(ArrayList Xs, ArrayList Ys) {
        //System.out.println("\n\n" + Xs.toString() + " \n" + Ys.toString());
        
        if(Xs.size() != Ys.size()) {
            System.out.println("Xs and Ys have inequal sizes in makeSureThatThereAreNoEqualVertices");
            return null;
        }
        if((Xs.size() < 1) || (Ys.size() < 1)) {
            System.out.println("Lists of Xs and Ys are < 1 in makeSureThatThereAreNoEqualVertices");
            return null;
        }
        for(int i = 0; i < Xs.size(); i++) {
            double value1 = Double.parseDouble(String.valueOf(Xs.get(i)));
            double value2 = Double.parseDouble(String.valueOf(Ys.get(i)));
            Xs.set(i, value1);
            Ys.set(i, value2);
            if(((double)Xs.get(i) > (double)5000000.0) || 
               ((double)Xs.get(i) < (double)(-5000000.0)) || 
               ((double)Ys.get(i) > (double)5000000.0) || 
               ((double)Ys.get(i) < (double)(-5000000.0))) {
                    System.out.println("Some coordinates > 5 000 000 or < -5 000 000 in makeSureThatThereAreNoEqualVertices");
                    return null;
            }
        }
        
        boolean ok = false;
        while(ok == false) {
            ok = true;
            for(int k = 0; k < Xs.size()-1; k++) {
                for(int l = k+1; l < Xs.size(); l++) {

                    double x1 = (double)Xs.get(k);
                    double y1 = (double)Ys.get(k);
                    double x2 = (double)Xs.get(l);
                    double y2 = (double)Ys.get(l);

                    if((x1 == x2) && (y1 == y2)) {
                        //System.out.println("Equal coordinates found, changing: " + x1 + ", " + y1 + " & " + x2 + ", " + y2);
                        ok = false;
                        
                        // https://stackoverflow.com/questions/3680637/generate-a-random-double-in-a-range 
                        // visited on 22/March/2025 
                        Random r = new Random();
                        double rangeMin = 0.00000001;
                        double rangeMax = 0.00000002;

                        double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
                        x1 += randomValue;   
                        Xs.set(k, x1);

                        randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
                        y1 -= randomValue;   
                        Ys.set(k, y1);

                        randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
                        x2 += randomValue;   
                        Xs.set(l, x2);

                        randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
                        y2 -= randomValue;   
                        Ys.set(l, y2);
                    }
                }
            }
        }
        
        ArrayList answer = new ArrayList();
        for(int i = 0; i < Xs.size(); i++) {
            for(int j = 0; j < 2; j++) {
                if(j==0) {
                    answer.add(Xs.get(i));
                }
                else {
                    answer.add(Ys.get(i));
                }
            }
        }
        //System.out.println("\n\n" + Xs.toString() + " \n" + Ys.toString());
        return answer;
    }
} 