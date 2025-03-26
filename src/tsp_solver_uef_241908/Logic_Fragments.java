package tsp_solver_uef_241908;
import java.util.ArrayList;

/**
 * TSP Solver by Tuomas Hyvönen, Java file 6 of 11 
 * 
 * A class for logic fragments that are popped from a stack when moving the SOM neurons. 
 * 
 * If bad fragments are created here, then the TSP tours should be long, at least in some graphs. 
 * 
 * This class currently has mostly "bad" (or simple) fragments in order to show how they can affect the evolution negatively. 
 * Even the simpler SOM-CH-NN algorithm can sometimes find better solutions than 
 * SOM-CH-NN-EVO when the logic fragments are not good. Not even a good evolution can help if 
 * the Elastic Band Principle is totally ignored when trying to cluster the neuron vertices. 
 * 
 * Open source Java code, feel free to edit and try your own improvements. 
 * Tested with Windows 11 
 * Apache NetBeans 25 
 * Java JDK 17 64bit 
 * 
 * @author Tuomas Hyvönen 
 * @version 2.1
 */
public class Logic_Fragments {
    
    private static final int EXISTING_LOGIC_FRAGMENTS_IMPLEMENTED = 5; 
    // edit this int when programming more fragments (1 of 3 places) 
    
    /**
     * Get the amount of existing logic fragments.
     * 
     * @return EXISTING_LOGIC_FRAGMENTS_IMPLEMENTED int
     */
    public static int get_amount_of_existing_logic_fragments() {
        return EXISTING_LOGIC_FRAGMENTS_IMPLEMENTED;
    }
    
    /*
     * Old synopsis, idea of usage, pseudo code:
     * 
     * 
     * LogicStack s = new LogicStack(); // logic stack is a stack of integers that decide the movements
     * s.push(1);
     * s.push(2);
     * s.push(3); // and so on. When two TSP tours combine and mutate, values will be pushed for the child's stack.
     * 
     * int moveDecision = s.top(); s.pop();
     * 
     * switch(moveDecision) {
     *      case 1: nnhSprout3AndMoveTowards3rd(...)
     *      case 2: nnhSprout5AndMoveTowards5th(...)
     *      case 3: simpleTraverseMoveTowardsTheNearest(...)
     *      default: //...
     *  }
     * 
     * traverse();
     * makeSureThatThereAreNoEqualVertices(EVERY SINGLE VERTEX);
     * 
     */
    
    /**
     * Make a NNH sprout with a length of 3 edges, then move towards that point.
     * Bug needs to be fixed, possibly.
     * 
     * @param threshold double
     * @param startingNodeIndex_NO_ZERO int
     * @param coordinates_x ArrayList
     * @param coordinates_y ArrayList
     * @return traversed ArrayList
     */
    public static ArrayList nnhSprout3AndMoveTowards3rd(double threshold, int startingNodeIndex_NO_ZERO, 
                ArrayList coordinates_x, ArrayList coordinates_y) {
        
        double[][] coordinates;// = new double[3][2]; // the last one is "is Y?"
        
        if(coordinates_x.size() != coordinates_y.size()) {
            System.out.println("Xs and Ys have inequal sizes.");
            return null;
        }
                                            // sprout length, start index, then coordinates
        coordinates = Sub_algorithms.NNHsprout(3, startingNodeIndex_NO_ZERO, coordinates_x, coordinates_y);
        
        //double[] traversed = new double[2];
        ArrayList traversed;// = new ArrayList();
        
        traversed = KohonenSOM.traverse((double)coordinates_x.get(startingNodeIndex_NO_ZERO -1), coordinates[2][0], 
                                (double)coordinates_y.get(startingNodeIndex_NO_ZERO -1), coordinates[2][1], 
                                threshold);
        
        System.out.println("nnhSprout3AndMoveTowards3rd, begin from: " + (startingNodeIndex_NO_ZERO));
        System.out.println((double)coordinates_x.get(startingNodeIndex_NO_ZERO -1) + ", " + 
                (double)coordinates_y.get(startingNodeIndex_NO_ZERO -1));
        System.out.println(traversed.get(0) + ", " + traversed.get(1) + " when moved this much: " + threshold);
        
        return traversed;
    }
    
    /**
     * Checks what is the closest vertex, moves towards that.
     * 
     * @param threshold double
     * @param startingNodeIndex_NO_ZERO int
     * @param coordinates_x ArrayList
     * @param coordinates_y ArrayList
     * @return traversed ArrayList
     */
    public static ArrayList moveTowardsTheClosest(double threshold, int startingNodeIndex_NO_ZERO, 
                ArrayList coordinates_x, ArrayList coordinates_y) {
        
        double[][] coordinates;// = new double[2][2]; // the last one is "is Y?"
        
        if(coordinates_x.size() != coordinates_y.size()) {
            System.out.println("Xs and Ys have inequal sizes.");
            return null;
        }
                                            // sprout length, start index, then coordinates
        coordinates = Sub_algorithms.NNHsprout(1, startingNodeIndex_NO_ZERO, coordinates_x, coordinates_y);
        
        ArrayList traversed;
        traversed = KohonenSOM.traverse((double)coordinates_x.get(startingNodeIndex_NO_ZERO -1), coordinates[1][0], 
                                (double)coordinates_y.get(startingNodeIndex_NO_ZERO -1), coordinates[1][1], 
                                threshold);
        
        System.out.println("moveTowardsTheClosest, begin from: " + (startingNodeIndex_NO_ZERO));
        System.out.println((double)coordinates_x.get(startingNodeIndex_NO_ZERO -1) + ", " + 
                (double)coordinates_y.get(startingNodeIndex_NO_ZERO -1));
        System.out.println(traversed.get(0) + ", " + traversed.get(1) + " when moved this much: " + threshold);
        
        return traversed;
    }
    
    /**
     * Move towards a random vertex. Can even move towards itself, in other words does not move then.
     * 
     * @param threshold double
     * @param startingNodeIndex_NO_ZERO int
     * @param coordinates_x ArrayList
     * @param coordinates_y ArrayList
     * @param boostedMovingTowards boolean
     * @return traversed ArrayList
     */
    public static ArrayList towardsRandom(double threshold, int startingNodeIndex_NO_ZERO, 
                ArrayList coordinates_x, ArrayList coordinates_y, boolean boostedMovingTowards) {
        
        if(boostedMovingTowards) {
            System.out.println("Boosted traversing, by a logic fragment, *= 1.2 ");
            threshold *= 1.2;
            if(threshold >= 1) {
                threshold = 1;
            }
        }
        
        if(coordinates_x.size() != coordinates_y.size()) {
            System.out.println("Xs and Ys have inequal sizes.");
            return null;
        }
        int random = (int) ((Math.random() * (coordinates_x.size() - 1)) + 1);
        ArrayList traversed;// = new ArrayList();
        traversed = KohonenSOM.traverse((double)coordinates_x.get(startingNodeIndex_NO_ZERO -1), (double)coordinates_x.get(random -1), 
                                (double)coordinates_y.get(startingNodeIndex_NO_ZERO -1), (double)coordinates_y.get(random -1), 
                                threshold);
        
        System.out.println("towards a random vertex, begin from: " + (startingNodeIndex_NO_ZERO));
        System.out.println((double)coordinates_x.get(startingNodeIndex_NO_ZERO -1) + ", " + 
                (double)coordinates_y.get(startingNodeIndex_NO_ZERO -1));
        System.out.println(traversed.get(0) + ", " + traversed.get(1) + " when moved this much: " + threshold);
        
        return traversed;
    }
    
    /**
     * Checks what is the closest vertex, moves away from that to the opposite direction. 
     * Finally, moves a bit to up, down, left or right (random of these 4). 
     * 
     * @param threshold double
     * @param startingNodeIndex_NO_ZERO int
     * @param coordinates_x ArrayList
     * @param coordinates_y ArrayList
     * @return traversed ArrayList
     */
    public static ArrayList moveAwayFromTheClosestThenUDLR(double threshold, int startingNodeIndex_NO_ZERO, 
                ArrayList coordinates_x, ArrayList coordinates_y) {
        threshold *= -1; // change the moving percent into a negative number, additive inverse number 
        
        double[][] coordinates;// = new double[2][2]; // the last one is "is Y?"
        
        if(coordinates_x.size() != coordinates_y.size()) {
            System.out.println("Xs and Ys have inequal sizes.");
            return null;
        }
        
        coordinates = Sub_algorithms.NNHsprout(1, startingNodeIndex_NO_ZERO, coordinates_x, coordinates_y);
        
        ArrayList traversed;// = new ArrayList();
        traversed = KohonenSOM.traverse((double)coordinates_x.get(startingNodeIndex_NO_ZERO -1), coordinates[1][0], 
                                (double)coordinates_y.get(startingNodeIndex_NO_ZERO -1), coordinates[1][1], 
                                threshold);
        
        int random = (int) ((Math.random() * (4 - 0)) + 0);
        if(random <= 0) { // up
            traversed.set(1, (double)(traversed.get(1))*1.1);
            System.out.println("Up");
        }
        if(random == 1) { // down
            traversed.set(1, (double)(traversed.get(1))*0.9);
            System.out.println("Down");
        }
        if(random == 2) { // left
            traversed.set(0, (double)(traversed.get(0))*0.9);
            System.out.println("Left");
        }
        if(random >= 3) { // right
            traversed.set(0, (double)(traversed.get(0))*1.1);
            System.out.println("Right");
        }
        
        System.out.println("moveAwayFromTheClosest, begin from: " + (startingNodeIndex_NO_ZERO));
        System.out.println((double)coordinates_x.get(startingNodeIndex_NO_ZERO -1) + ", " + 
                (double)coordinates_y.get(startingNodeIndex_NO_ZERO -1));
        System.out.println(traversed.get(0) + ", " + traversed.get(1) + " when moved this much: " + threshold);
        
        return traversed;
    }
    
    /**
     * Programmer's own logic fragment method that they can program.
     * 
     * @param threshold double
     * @param startingNodeIndex_NO_ZERO int
     * @param coordinates_x ArrayList
     * @param coordinates_y ArrayList
     * @return traversed ArrayList
     */
    public static ArrayList myOwnImplementationJavaMethodCall(double threshold, int startingNodeIndex_NO_ZERO, 
                ArrayList coordinates_x, ArrayList coordinates_y) {     // (2 of 3 places) 
        // Write your own logic here. 
        // Do not forget to edit kohonenSom.java's switch-case "integer" so the method will be called! 
        // Also edit "private static final int existing_logic_fragments_implemented = ... ;" at the top of this .java file 
        // That's a total of 3 places (in 2 classes) to edit, including this method here.
        
        // How you would like the neuron point to move when it does not move towards the SOM goal? 
        // Towards the origo? Towards a random mean (vector) between some points? You decide with the logic fragments you program.
        
        // In the evolution phase when pressing F8 or F9, some chromosomes (= TSP tours) will acquire your logic fragment randomly. 
        // Check the logic stack pushes if they get a "integer" or whatever your fragment's int ID number is. 
        // If they get that "integer", they move like you ordered at some phase when popping the stack. 
        // Do not expect any huge movements if the "double threshold" variable is small! 
        
        ArrayList traversed = new ArrayList();
        return traversed;
    }
} 