package tsp_solver_uef_241908;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Random;

/**
 * TSP Solver by Tuomas Hyvönen, Java file 1 of 11 
 * 
 * The Chromosome class. 
 * A chromosome is in other words a Hamiltonian circuit instance, also known as 
 * a TSP tour, each chromosome has (a) logic stack(s) for the inner vertices (movable neurons).
 * 
 * Open source Java code, feel free to edit and try your own improvements. 
 * Tested with Windows 11 
 * Apache NetBeans 
 * Java JDK 17 64bit 
 * 
 * @author Tuomas Hyvönen 
 * @version 2.2
 */
public class Chromosome {
    // private final String nameID;
    private Logic_Stack logicstack;
    private final ArrayList<Double> points; // odds = Xs, evens = Ys  // class "Point.java" might turn out to be unnecessary 
    // note: "ArrayList points" can and must change coordinates when needed, copying the chromosome instances all the time is 
    // another story, delete the "final" if necessary 
    
    // https://stackoverflow.com/questions/10750791/what-is-the-sense-of-final-arraylist
    // visited on 22/March/2025 
    
    //      AN IDEA: if permanent IDs or names wanted for the chromosomes, not implemented though 
    //      https://stackoverflow.com/questions/19961844/creating-multiple-objects-with-different-names-in-a-loop-to-store-in-an-array-li
    //      visited on 22/March/2025 
    
    /**
     * The constructor
     * 
     * @param nameID String 
     * @param points ArrayList 
     */
    public Chromosome(String nameID, ArrayList points) {
        //this.nameID = nameID;
        this.points = points;
    }
    
    /**
     * Get chromosome (TSP tour, Hamiltonian circuit)
     * 
     * @return points ArrayList
     */
    public ArrayList getChromosomePoints() {
        return this.points;
    }
    
    /**
     * Sets points to a chromosome.
     * 
     * @param solutionArray double[]
     */
    public void setChromosomePoints(double[] solutionArray) {
        this.points.clear();
        for(int i = 0; i < solutionArray.length; i++) {
            this.points.add(solutionArray[i]);
        }
    }
    
    /**
     * Get the logic stack of a chromosome. The stack gives moving instructions 
     * after moving towards the regular SOM goal.
     * 
     * @return logicstack Logic_Stack
     */
    public Logic_Stack getStack() {
        return this.logicstack;
    }
    
    /**
     * Sets logic stacks for the chromosome so each of its neurons know how to move after SOM.
     * 
     * @param logicstack Logic_Stack
     */
    public void setLogicStacks(Logic_Stack logicstack) {
        this.logicstack = logicstack;
        // improvement: set for each point individually?
    }
    
    /**
     * A getter method for a chromosome instance.
     * 
     * @param nameID String
     * @return this 
     */
    public Chromosome getChromosome(String nameID) {
        return this;
    }
    
    /**
     * Returns the length of the TSP tour of a chromosome.
     * 
     * @return length double
     */
    public double getLength() {
        double length = 0;
        ArrayList c_coordinates = this.getChromosomePoints();
        for(int j = 0; j < this.getChromosomePoints().size()-2; j+=2) {
            length += Sub_algorithms.Euclidean_distance((double)c_coordinates.get(j), 
                                         (double)c_coordinates.get(j+1), 
                                         (double)c_coordinates.get(j+2), 
                                         (double)c_coordinates.get(j+3));
        }
        //System.out.println("-- getLength -- Returned length: " + length);
        return length;
    }
    
    /**
     * Sets a Chromosome instance to null. Calls the garbage collector.
     * 
     * @param c Chromosome
     */
    public static void terminateChromosome(Chromosome c) {
        c = null;
        System.gc();
    }
    
    /**
     * Form a chromosome, making a TSP tour in other words.Called when creating chromosomes. Using the SOM-CH-NN algorithm.
     * 
     * @param somResultArray ArrayList
     * @param maxSomIterations int
     * @param maxPossibleDistanceInHullAvgs double
     * @param inputCoordinateXs ArrayList
     * @param inputCoordinateYs ArrayList
     * @param neuronCoordinateXs ArrayList
     * @param neuronCoordinateYs ArrayList
     * @param amountOfHullVertices int
     * @param edges double[][][]
     * @param neuronCoordinateXsCopy ArrayList
     * @param neuronCoordinateYsCopy ArrayList
     * @param max int
     * @param min int
     * @param solutionArray double[]
     * @param learningRate double
     * @param maxNeighbourhoodRadius
     * @return arr ArrayList
     */
    public static ArrayList formChromosome_SOM_CH_NN(
            ArrayList somResultArray, 
            int maxSomIterations, 
            double maxPossibleDistanceInHullAvgs, 
            ArrayList inputCoordinateXs, 
            ArrayList inputCoordinateYs,
            ArrayList neuronCoordinateXs, 
            ArrayList neuronCoordinateYs, 
            int amountOfHullVertices,
            double[][][] edges, 
            ArrayList neuronCoordinateXsCopy, 
            ArrayList neuronCoordinateYsCopy,
            int max, 
            int min, 
            double[] solutionArray,
            double learningRate,
            double maxNeighbourhoodRadius) {
        
            somResultArray = KohonenSOM.performSOM(maxSomIterations, maxPossibleDistanceInHullAvgs, 
            inputCoordinateXs, inputCoordinateYs, 
            neuronCoordinateXs, neuronCoordinateYs, false, null, learningRate,
            maxNeighbourhoodRadius); // not yet using logic stacks and their fragments 

        ArrayList nnh_purpose_coordinates = new ArrayList();
        ArrayList nnh_purpose_coordinates_x = new ArrayList();
        ArrayList nnh_purpose_coordinates_y = new ArrayList();
        ArrayList nnh_purpose_coordinates_clust = new ArrayList();

        for(int j = 0; j < amountOfHullVertices/2; j++) {           // changed to "/2"
            nnh_purpose_coordinates.add((double)edges[j][0][0]);
            nnh_purpose_coordinates.add((double)edges[j][1][0]);
            nnh_purpose_coordinates.add(j);
            //System.out.println("Hull: " + (double)edges[j][0][0] + ", " + (double)edges[j][1][0] + ", " + j);
        }
        int helpindex = 2;
        for(int j = 0; j < neuronCoordinateXsCopy.size(); j++) {
            nnh_purpose_coordinates.add(neuronCoordinateXsCopy.get(j));
            nnh_purpose_coordinates.add(neuronCoordinateYsCopy.get(j));
            nnh_purpose_coordinates.add(somResultArray.get(helpindex));
            //System.out.println("Neurons and clusters: " + neuronCoordinateXsCopy.get(j) + ", " + 
            //        neuronCoordinateYsCopy.get(j) + ", " + somResultArray.get(helpindex));
            helpindex +=3;
        }
        for(int j = 0; j < nnh_purpose_coordinates.size()-2; j+=3) {
                nnh_purpose_coordinates_x.add(nnh_purpose_coordinates.get(j));
                nnh_purpose_coordinates_y.add(nnh_purpose_coordinates.get(j+1));
                nnh_purpose_coordinates_clust.add(nnh_purpose_coordinates.get(j+2));
        }
        // amountOfHullVertices and cluster count are equal 

        Random rand = new Random();
        int start = rand.nextInt((max - min) + 1) + min; // min 1 
        boolean[] booltable = new boolean[max];
        boolean booltable_has_false = true;
        int pointer = 1; 
        int pointer_start = start -1;
        int clusterInTurnNow = (int)nnh_purpose_coordinates_clust.get(pointer_start);

        //solution = new StringBuilder(solution).append(String.valueOf(start)).toString();
        solutionArray = new double[(max +1)*2];
        int solutionArrayInsertIndex = 0;
        for(int j = 0; j < max +1; j++) {
                    solutionArray[j] = Double.MAX_VALUE;
        }
        solutionArray[solutionArrayInsertIndex] = (double) nnh_purpose_coordinates_x.get(pointer_start);
        solutionArrayInsertIndex++;
        solutionArray[solutionArrayInsertIndex] = (double) nnh_purpose_coordinates_y.get(pointer_start);
        solutionArrayInsertIndex++;
        boolean verticeadded;// = true;
        booltable[pointer_start] = true;
        while(booltable_has_false) {
            //System.out.println("Pointer start is " + pointer_start + 
            //        ", pointer is (begin of while loop): " + pointer + 
            //        ", the wanted cluster is " + clusterInTurnNow);
            double min_distance = Double.MAX_VALUE;
            double temp_distance;
            double x1;
            double y1;
            x1 = (double) nnh_purpose_coordinates_x.get(pointer_start);
            y1 = (double) nnh_purpose_coordinates_y.get(pointer_start);
            double x2;
            double y2;

            for(int j = 0; j < booltable.length; j++) {
                if((booltable[j] == false) 
                         && (clusterInTurnNow == (int)nnh_purpose_coordinates_clust.get(j))
                        ) {
                    x2 = (double) nnh_purpose_coordinates_x.get(j);
                    y2 = (double) nnh_purpose_coordinates_y.get(j);
                    temp_distance = Sub_algorithms.
                            Euclidean_distance_squared(x1, y1, x2, y2);
                    if((temp_distance < min_distance)) {
                        min_distance = temp_distance;
                        pointer = j;
                        //System.out.println("min distance is " + min_distance);
                    }
                }
            }

            // new vertice to the result: 
            if((booltable[pointer] == false) 
                    // && (clusterInTurnNow == (int)nnh_purpose_coordinates_clust.get(pointer)) 
            ) {
                booltable[pointer] = true;
                //solution = new StringBuilder(solution).append("-").toString();
                //solution = new StringBuilder(solution).append(pointer + 1).toString();
                solutionArray[solutionArrayInsertIndex] = (double) nnh_purpose_coordinates_x.get(pointer);
                solutionArrayInsertIndex++;
                solutionArray[solutionArrayInsertIndex] = (double) nnh_purpose_coordinates_y.get(pointer);
                solutionArrayInsertIndex++;
                //System.out.println("ADDED with pointer " + pointer + ", cluster wanted: " + clusterInTurnNow);
                verticeadded = true;
            }
            else {
                //System.out.println("DID NOT ADD ANYTHING, pointer is " + pointer);
                verticeadded = false;
            }

            boolean all_in_this_cluster_taken = true;
            for(int j = 0; j < nnh_purpose_coordinates_clust.size(); j++) {
                if((booltable[j] == false) && (
                        clusterInTurnNow == (int)nnh_purpose_coordinates_clust.get(j))) {
                            all_in_this_cluster_taken = false;
                            //System.out.println("NOT TAKEN YET: " + j + " in cluster " + clusterInTurnNow);
                            j = nnh_purpose_coordinates_clust.size(); // end the loop 
                }
            }
            //System.out.println("Booltable: " + Arrays.toString(booltable));
            if(all_in_this_cluster_taken) {
                //if(verticeadded) {
                    clusterInTurnNow++;
                    if(clusterInTurnNow >= amountOfHullVertices) {
                        clusterInTurnNow = 0;
                    }
                    //System.out.println("CLUSTER HANDLED, clusterInTurnNow changed to " + clusterInTurnNow);
                //}
            }

            booltable_has_false = false;
            for(int j = 0; j < booltable.length; j++) {
                if(booltable[j] == false) {
                    booltable_has_false = true;
                }
            }
            if(verticeadded) {
                pointer_start = pointer;
                //verticeadded = false;
            }
        }
        // link back to the start node:
        solutionArray[solutionArrayInsertIndex] = (double) nnh_purpose_coordinates_x.get(start-1);
        solutionArrayInsertIndex++;
        solutionArray[solutionArrayInsertIndex] = (double) nnh_purpose_coordinates_y.get(start-1);
        solutionArrayInsertIndex++;
        //System.out.println("ADDED index (final) is " + (start-1));

        ArrayList arr = new ArrayList();
        for(int j = 0; j < solutionArray.length; j++) {
            arr.add(solutionArray[j]);
        }
        return arr;
    }
    
    /**
     * Another way to form Chromosomes: using the OPT-NNH-CHH-CHRI algorithm.
     * 
     * @param text String
     * @param ERRORMSG String
     * @param i int
     * @param rows String[]
     * @param max int
     * @param coordinate_ids ArrayList
     * @param coordinates_x ArrayList
     * @param coordinates_y ArrayList
     * @return edges double[][]
     */
    public static double[][] formChromosome_OPT_NNH_CHH_CHRI(String text, String ERRORMSG,
                                                int i, String rows[], int max, ArrayList coordinate_ids,
                                                ArrayList coordinates_x, ArrayList coordinates_y){
        String result1; // Strings after just reading all there is 
        String result2;
        String result3;
        String resultE;
        String tour1 = ""; // Strings after reading the tour, 1-2-3-4-1 for example 
        String tour2 = "";
        String tour3 = "";
        String tourE = "";
        ArrayList nnh_coordinates = new ArrayList(); // Strings containing the TSP coordinates, xy xy xy...
        ArrayList chh_coordinates = new ArrayList();
        ArrayList chri_coordinates = new ArrayList();
        String length1 = ""; // variables after reading a length of a TSP tour, 10.12345...
        String length2 = "";
        String length3 = "";
        String lengthE = "";
        double length1d;
        double length2d;
        double length3d;
        double lengthEd;
        int bestOneOfTheTours;// = -1; // will be 1, 2 or 3 
        //double bestTourLength = Double.MAX_VALUE;
        //String bestTour = "";

        result1 = TSP_Solver_UEF.NearestNeighbour_Algorithm(text); //    call NNH   (1) 
        result2 = TSP_Solver_UEF.ConvexHull_Algorithm(text);       //    call CHH   (2) 
        result3 = TSP_Solver_UEF.Christofides_Algorithm(text);     //    call CHRI  (3) 
        resultE = TSP_Solver_UEF.NearestNeighbour_Algorithm(text); //    call NNH again
        // the results must never contain words like "terror" etc. -> misunderstands the string 
        if(result1.contains("error") || result1.contains("Error") || result1.contains("ERROR")) { 
            System.out.println(ERRORMSG); return null;
        }
        if(result2.contains("error") || result2.contains("Error") || result2.contains("ERROR")) { 
            System.out.println(ERRORMSG); return null;
        }
        if(result3.contains("error") || result3.contains("Error") || result3.contains("ERROR")) { 
            System.out.println(ERRORMSG); return null;
        }
        if(resultE.contains("error") || resultE.contains("Error") || resultE.contains("ERROR")) { 
            System.out.println(ERRORMSG); return null;
        }
        // read all 3(4) results, compare with the original text input, take the lengths and tours 
        // so the OPT phase can be called (I. Length, II. Tour and III. Then find coordinates) 
        i = 0;
        String str1;
        BufferedReader reader1 = new BufferedReader(new StringReader(result1));
        try {
            while ((str1 = reader1.readLine()) != null) {
                if (str1.length() > 0) {
                    rows[i] = str1; 
                    if(rows[i].contains("Tour length:")) {
                        try {
                            int endIndex;
                            for (int beginIndex = 2; beginIndex < 
                                    rows[i].length(); 
                                    beginIndex = endIndex + 1) {
                                endIndex = rows[i].indexOf(" ", beginIndex);
                                if (endIndex == -1) {
                                    endIndex = rows[i].length();
                                }
                                String numberString = rows[i].substring(
                                        beginIndex, endIndex);
                                try {
                                    length1 = numberString;
                                } 
                                catch (Exception e) {
                                    System.err.println(e);
                                }
                            }
                        }
                        catch(Exception e) {
                            System.err.println(e);
                        }
                    }
                    if(Character.isDigit(rows[i].charAt(0)) && (
                       rows[i].charAt(1) == '-' || 
                       rows[i].charAt(2) == '-' || 
                       rows[i].charAt(3) == '-' || 
                       rows[i].charAt(4) == '-' || 
                       rows[i].charAt(5) == '-' || 
                       rows[i].charAt(6) == '-')) {
                        try {
                            int endIndex;
                            for (int beginIndex = 0; beginIndex < 
                                    rows[i].length(); 
                                    beginIndex = endIndex + 1) {
                                endIndex = rows[i].indexOf(" ", beginIndex);
                                if (endIndex == -1) {
                                    endIndex = rows[i].length();
                                }
                                String numberString = rows[i].substring(
                                        beginIndex, endIndex);
                                try {
                                    tour1 = numberString;
                                } 
                                catch (Exception e) {
                                    System.err.println(e);
                                }
                            }
                        }
                        catch(Exception e) {
                            System.err.println(e);
                        }
                    }
                }
            }
        } 
        catch(IOException e) {
            System.err.println(e);
        }
        i = 0;
        String strE;
        BufferedReader readerE = new BufferedReader(new StringReader(resultE));
        try {
            while ((strE = readerE.readLine()) != null) {
                if (strE.length() > 0) {
                    rows[i] = strE; 
                    if(rows[i].contains("Tour length:")) {
                        try {
                            int endIndex;
                            for (int beginIndex = 2; beginIndex < 
                                    rows[i].length(); 
                                    beginIndex = endIndex + 1) {
                                endIndex = rows[i].indexOf(" ", beginIndex);
                                if (endIndex == -1) {
                                    endIndex = rows[i].length();
                                }
                                String numberString = rows[i].substring(
                                        beginIndex, endIndex);
                                try {
                                    lengthE = numberString;
                                } 
                                catch (Exception e) {
                                    System.err.println(e);
                                }
                            }
                        }
                        catch(Exception e) {
                            System.err.println(e);
                        }
                    }
                    if(Character.isDigit(rows[i].charAt(0)) && (
                       rows[i].charAt(1) == '-' || 
                       rows[i].charAt(2) == '-' || 
                       rows[i].charAt(3) == '-' || 
                       rows[i].charAt(4) == '-' || 
                       rows[i].charAt(5) == '-' || 
                       rows[i].charAt(6) == '-')) {
                        try {
                            int endIndex;
                            for (int beginIndex = 0; beginIndex < 
                                    rows[i].length(); 
                                    beginIndex = endIndex + 1) {
                                endIndex = rows[i].indexOf(" ", beginIndex);
                                if (endIndex == -1) {
                                    endIndex = rows[i].length();
                                }
                                String numberString = rows[i].substring(
                                        beginIndex, endIndex);
                                try {
                                    tourE = numberString;
                                } 
                                catch (Exception e) {
                                    System.err.println(e);
                                }
                            }
                        }
                        catch(Exception e) {
                            System.err.println(e);
                        }
                    }
                }
            }
        } 
        catch(IOException e) {
            System.err.println(e);
        }
        length1d = Double.parseDouble(length1);
        //System.out.println("TOUR 1 LENGTH: " + length1d);
        //System.out.println(tour1);
        
        lengthEd = Double.parseDouble(lengthE);
        //System.out.println("TOUR EXTRA1 LENGTH: " + lengthEd);
        //System.out.println(tourE);
        if(lengthEd < length1d) { // choose the best of the 2 NNHs 
            length1d = lengthEd;
            tour1 = tourE;
        }

        // same for the others:
        i = 0;
        String str2;
        BufferedReader reader2 = new BufferedReader(new StringReader(result2));
        try {
            while ((str2 = reader2.readLine()) != null) {
                if (str2.length() > 0) {
                    rows[i] = str2; 
                    if(rows[i].contains("Tour length:")) {
                        try {
                            int endIndex;
                            for (int beginIndex = 2; beginIndex < 
                                    rows[i].length(); 
                                    beginIndex = endIndex + 1) {
                                endIndex = rows[i].indexOf(" ", beginIndex);
                                if (endIndex == -1) {
                                    endIndex = rows[i].length();
                                }
                                String numberString = rows[i].substring(
                                        beginIndex, endIndex);
                                try {
                                    length2 = numberString;
                                } 
                                catch (Exception e) {
                                    System.err.println(e);
                                }
                            }
                        }
                        catch(Exception e) {
                            System.err.println(e);
                        }
                    }
                    if(Character.isDigit(rows[i].charAt(0)) && (
                       rows[i].charAt(1) == '-' || 
                       rows[i].charAt(2) == '-' || 
                       rows[i].charAt(3) == '-' || 
                       rows[i].charAt(4) == '-' || 
                       rows[i].charAt(5) == '-' || 
                       rows[i].charAt(6) == '-')) {
                        try {
                            int endIndex;
                            for (int beginIndex = 0; beginIndex < 
                                    rows[i].length(); 
                                    beginIndex = endIndex + 1) {
                                endIndex = rows[i].indexOf(" ", beginIndex);
                                if (endIndex == -1) {
                                    endIndex = rows[i].length();
                                }
                                String numberString = rows[i].substring(
                                        beginIndex, endIndex);
                                try {
                                    tour2 = numberString;
                                } 
                                catch (Exception e) {
                                    System.err.println(e);
                                }
                            }
                        }
                        catch(Exception e) {
                            System.err.println(e);
                        }
                    }
                }
            }
        } 
        catch(IOException e) {
            System.err.println(e);
        }
        length2d = Double.parseDouble(length2);
        //System.out.println("TOUR 2 LENGTH: " + length2d);
        //System.out.println(tour2);

        // one more time, the last is CHRI:
        i = 0;
        String str3;
        BufferedReader reader3 = new BufferedReader(new StringReader(result3));
        try {
            while ((str3 = reader3.readLine()) != null) {
                if (str3.length() > 0) {
                    rows[i] = str3; 
                    if(rows[i].contains("Tour length:")) {
                        try {
                            int endIndex;
                            for (int beginIndex = 2; beginIndex < 
                                    rows[i].length(); 
                                    beginIndex = endIndex + 1) {
                                endIndex = rows[i].indexOf(" ", beginIndex);
                                if (endIndex == -1) {
                                    endIndex = rows[i].length();
                                }
                                String numberString = rows[i].substring(
                                        beginIndex, endIndex);
                                try {
                                    length3 = numberString;
                                } 
                                catch (Exception e) {
                                    System.err.println(e);
                                }
                            }
                        }
                        catch(Exception e) {
                            System.err.println(e);
                        }
                    }
                    if(Character.isDigit(rows[i].charAt(0)) && (
                       rows[i].charAt(1) == '-' || 
                       rows[i].charAt(2) == '-' || 
                       rows[i].charAt(3) == '-' || 
                       rows[i].charAt(4) == '-' || 
                       rows[i].charAt(5) == '-' || 
                       rows[i].charAt(6) == '-')) {
                        try {
                            int endIndex;
                            for (int beginIndex = 0; beginIndex < 
                                    rows[i].length(); 
                                    beginIndex = endIndex + 1) {
                                endIndex = rows[i].indexOf(" ", beginIndex);
                                if (endIndex == -1) {
                                    endIndex = rows[i].length();
                                }
                                String numberString = rows[i].substring(
                                        beginIndex, endIndex);
                                try {
                                    tour3 = numberString;
                                } 
                                catch (Exception e) {
                                    System.err.println(e);
                                }
                            }
                        }
                        catch(Exception e) {
                            System.err.println(e);
                        }
                    }
                }
            }
        } 
        catch(IOException e) {
            System.err.println(e);
        }
        length3d = Double.parseDouble(length3);
        //System.out.println("TOUR 3 LENGTH: " + length3d);
        //System.out.println(tour3);
        
        TSP_Solver_UEF.setOPT3preResult("\tNNH for a couple of times:  " + tour1 + "\n\t     " + length1d + "\n" + 
                           "\tCHH for 1 time:  " + tour2 + "\n\t     " + length2d + "\n" + 
                           "\tCHRI for 1 time:  " + tour3 + "\n\t     " + length3d + "\n");

        if(length1d < length2d) {
            if(length1d < length3d) {
                // length1d is the minimum,      2nd and 3rd both remain unknown
                // choose NNH
                bestOneOfTheTours = 1;
                    if((length1d == length2d) && (length1d == length3d)) {// even if unfair, NNH
                        bestOneOfTheTours = 1;
                    }
                    if(length1d == length2d) {// even if unfair, choose NNH
                        bestOneOfTheTours = 1;
                    }
                    if(length1d == length3d) {// even if unfair, choose NNH
                        bestOneOfTheTours = 1;
                    }
            }
            else {
                // length3d is the minimum,      3rd < 1st < 2nd
                // choose CHRI
                bestOneOfTheTours = 3;
                    if((length1d == length2d) && (length1d == length3d)) {// even if unfair, NNH
                        bestOneOfTheTours = 1;
                    }
                    if(length2d == length3d) {// even if unfair, choose CHH
                        bestOneOfTheTours = 2;
                    }
                    if(length1d == length3d) {// even if unfair, choose NNH
                        bestOneOfTheTours = 1;
                    }
            }
        }
        else {
            if(length2d < length3d) {
                // length2d is the minimum,      1st and 3rd both remain unknown
                // choose CHH
                bestOneOfTheTours = 2;
                    if((length1d == length2d) && (length1d == length3d)) {// even if unfair, NNH
                        bestOneOfTheTours = 1;
                    }
                    if(length1d == length2d) {// even if unfair, choose NNH
                        bestOneOfTheTours = 1;
                    }
                    if(length2d == length3d) {// even if unfair, choose CHH
                        bestOneOfTheTours = 2;
                    }
            }
            else {
                // length3d is the minimum,      3rd < 2nd < 1st
                // choose CHRI
                bestOneOfTheTours = 3;
                    if((length1d == length2d) && (length1d == length3d)) {// even if unfair, NNH
                        bestOneOfTheTours = 1;
                    }
                    if(length2d == length3d) {// even if unfair, choose CHH
                        bestOneOfTheTours = 2;
                    }
                    if(length1d == length3d) {// even if unfair, choose NNH
                        bestOneOfTheTours = 1;
                    }
            }
        }

        String[] tourArr1 = tour1.split("-", max+1);
        String[] tourArr2 = tour2.split("-", max+1);
        String[] tourArr3 = tour3.split("-", max+1);

        
        for (String tourArr1a : tourArr1) { 
            for (int j = 0; j < coordinate_ids.size(); j++) {
                Double d = (double)coordinate_ids.get(j);
                int value = d.intValue();
                if (value == Integer.parseInt(tourArr1a)) {
                    nnh_coordinates.add(coordinates_x.get(j));
                    nnh_coordinates.add(coordinates_y.get(j));
                }
            }
        }
        for (String tourArr2a : tourArr2) {
            for (int j = 0; j < coordinate_ids.size(); j++) {
                Double d = (double)coordinate_ids.get(j);
                int value = d.intValue();
                if (value == Integer.parseInt(tourArr2a)) {
                    chh_coordinates.add(coordinates_x.get(j));
                    chh_coordinates.add(coordinates_y.get(j));
                }
            }
        }
        for (String tourArr3a : tourArr3) {
            for (int j = 0; j < coordinate_ids.size(); j++) {
                Double d = (double)coordinate_ids.get(j);
                int value = d.intValue();
                if (value == Integer.parseInt(tourArr3a)) {
                    chri_coordinates.add(coordinates_x.get(j));
                    chri_coordinates.add(coordinates_y.get(j));
                }
            }
        }
        //System.out.println(nnh_coordinates);
        double[][] edges = new double[max][2]; // the improved edges 

        switch(bestOneOfTheTours) {
            case(1): {
                TSP_Solver_UEF.addToOPT3preResult("NNH was chosen for improving purposes."
                        + " Please note that not always the solution will be improved!");
                edges = Sub_algorithms.MakeOptMoves(max, length1d, nnh_coordinates);
                    TSP_Solver_UEF.setWantedTourLength(length1d * TSP_Solver_UEF.getTerminationPercentRequirement()); 
                    TSP_Solver_UEF.setShortestLengthOfTheOriginalChromosomes(length1d);
                break;
            }
            case(2): {
                TSP_Solver_UEF.addToOPT3preResult("CHH was chosen for improving purposes."
                        + " Please note that not always the solution will be improved!");
                edges = Sub_algorithms.MakeOptMoves(max, length2d, chh_coordinates);
                    TSP_Solver_UEF.setWantedTourLength(length2d * TSP_Solver_UEF.getTerminationPercentRequirement()); 
                    TSP_Solver_UEF.setShortestLengthOfTheOriginalChromosomes(length2d);
                break;
            }
            case(3): {
                TSP_Solver_UEF.addToOPT3preResult("CHRI was chosen for improving purposes."
                        + " Please note that not always the solution will be improved!");
                edges = Sub_algorithms.MakeOptMoves(max, length3d, chri_coordinates);
                    TSP_Solver_UEF.setWantedTourLength(length3d * TSP_Solver_UEF.getTerminationPercentRequirement()); 
                    TSP_Solver_UEF.setShortestLengthOfTheOriginalChromosomes(length3d);
                break;
            }
        }
        return edges;
    }
} 