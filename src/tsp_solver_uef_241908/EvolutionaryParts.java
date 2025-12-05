package tsp_solver_uef_241908;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TSP Solver by Tuomas Hyvönen, Java file 3 of 11 
 * 
 * A class of the evolutionary computing parts, the genetic algorithm methods. 
 * The original idea was to include all of the evolution codes here but it 
 * turned out that when a List has the chromosome instances, most functions can be 
 * done to the object List directly, for example removing a chromosome (individual TSP tour). 
 * 
 * Open source Java code, feel free to edit and try your own improvements. 
 * Tested with Windows 11 
 * Apache NetBeans 
 * Java JDK 17 64bit 
 * 
 * @author Tuomas Hyvönen 
 * @version 2.1
 */
public class EvolutionaryParts {

    /**
     * The programmer is supposed to edit also this method bravely like they want. The edits might
     * make the TSP tours much better.
     * 
     * Natural selection (after the selection, there are still the crossing-over and mutation parts): 
     * the input List will be reordered so that when pairs are later combined, the couple can be 
     * selected when reading the List just "2 by 2" in order. If the input is odd instead of even, then the 
     * last one just does not reproduce if decided so.
     * For evolution, it is not wise to reproduce chromosomes (their stacks) that are exactly equal.
     * Also includes crossing-overs (reproducing) and mutations.
     * 
     * @param instances List
     * @param logicStackCopiesOfOriginals List
     * @param probabilityOfCrossingOver double
     * @param wantedMutationRate double
     * @return instances List
     */
    public static List selectWhatWillBePairedThenCrossingOverAndMutation(List instances, List logicStackCopiesOfOriginals,
                                                            double probabilityOfCrossingOver, double wantedMutationRate) {
        if(Math.random() < probabilityOfCrossingOver) {
            System.out.println("Crossing-over phase.");
            for(int i = 0; i < instances.size()-1; i++) {   // partner searching: 
                for(int j = 1; j < instances.size(); j++) {
                    if(i != j) {
                        Logic_Stack ls1 = (Logic_Stack) logicStackCopiesOfOriginals.get(i);
                        Logic_Stack ls2 = (Logic_Stack) logicStackCopiesOfOriginals.get(j);
                        if(ls1.top() != ls2.top()) { // if different tops, move 
                            if(ls1.getTopIndex() == ls2.getTopIndex()) { // same stack sizes 
                                Chromosome helpVar = (Chromosome) instances.get(i+1);   // i+1 to helpv 
                                instances.set(i+1, (Chromosome) instances.get(j));      // j to i+1 
                                instances.set(j, helpVar);                              // helpv to j 
                            }
                        }
                        else if(j < i) {
                            if(ls1.getTopIndex() > 2 && ls2.getTopIndex() > 2) {
                                ls1.pop();
                                ls2.pop();
                            }
                        }
                        if(ls1.isEmpty() && ls2.isEmpty()) {
                            i = instances.size()+1;
                            j = instances.size()+1;
                        }
                    }
                }
            }

            int originalInstanceSize = instances.size();
            // next, adding the children to the population "instances": 
            // the child of i and i+1 is added to the end of the list, then i+=2 and the next children 

            for(int i = 0; i < originalInstanceSize -1; i+=2) {
                // idea: even 0, 2, 4, 6 ... are "females", odds are "males" and male's purpose is to affect the stack of the child, 
                // trade childrens' stack data: (took a copy before popping all out into logicStackCopiesOfOriginals) 
                Chromosome newC = (Chromosome) instances.get(i);

                newC = possiblySwapTwoRandomNodes(newC);

                Logic_Stack newL = new Logic_Stack();
                Logic_Stack motherL = (Logic_Stack)logicStackCopiesOfOriginals.get(i);
                Logic_Stack fatherL = (Logic_Stack)logicStackCopiesOfOriginals.get(i+1);
                while(!motherL.isEmpty() && !fatherL.isEmpty()) {
                    newL.push(motherL.top());
                    motherL.pop();
                    newL.push(fatherL.top());
                    fatherL.pop();
                } // the stack will have changes 
                
                // mutations (mostly deletion types), allows the child to be something 
                // way more than just a combination of its parents 
                // some randoms are deleted so the new stack is not so long: 

                System.gc();
                Logic_Stack newerL = new Logic_Stack();
                while(!newL.isEmpty()) {
                    boolean switc = Math.random() < wantedMutationRate; // switch variable, can set the probability of adding 
                    if(switc) {                          // when the stack is reversed again (switch cannot be a variable name in Java)
                        newerL.push(newL.top());
                    }
                    switc = Math.random() < wantedMutationRate;
                    if(switc && (newL.top() > 2)) {
                        newerL.push(newL.top()-2);  // this kind of code allows completely new genes not present in parents 
                    }                               // assuming there are at least 3 logic fragments present 
                    newL.pop();
                }

                newC.setLogicStacks(newerL);
                if(newC.getLength() < TSP_Solver_UEF_241908.getShortestLengthOfTheOriginalChromosomes() * 
                        TSP_Solver_UEF_241908.getElasticBandLimit()) {
                // do nothing if not in the approved range 
                    instances.add(newC);
                }
            }
        }
        return instances;
    }
    
    /**
     * Calculating the fitness value.
     * Those who respect the elastic band principle, should survive.
     * 
     * 1.0 requirement would mean "we want the best that is known currently and that's it".
     * 3.0 requirement means "at maximum, we allow a tour that is 300% the known optimum tour".
     * 
     * @param instances List
     * @param wantedPercentsAddedTo100_Min1_Max3 double
     * @return double
     */
    public static double calculateFitness(List instances, double wantedPercentsAddedTo100_Min1_Max3) {
        if(wantedPercentsAddedTo100_Min1_Max3 < 1 || 
           wantedPercentsAddedTo100_Min1_Max3 > 3) {
                System.out.println("The percent requirement is ridiculous in calculateFitness. Should be from 1 to 3");
                // 3.0 means 300% 
                return Double.MAX_VALUE;
        }
        double bestFitness = Double.MAX_VALUE;
        for(int chro = 0; chro < instances.size(); chro++) {
            Chromosome c = (Chromosome) instances.get(chro);
            ArrayList coordinates = c.getChromosomePoints();
            double eucNew = 0;
            for(int i = 0; i < c.getChromosomePoints().size()-2; i+=2) {
                eucNew += Sub_algorithms.Euclidean_distance((double)coordinates.get(i), 
                                             (double)coordinates.get(i+1), 
                                             (double)coordinates.get(i+2), 
                                             (double)coordinates.get(i+3));
            }
            if(eucNew < bestFitness) {
                bestFitness = eucNew;
            }
        }
        
        if(TSP_Solver_UEF_241908.getWantedTourLength() > bestFitness * wantedPercentsAddedTo100_Min1_Max3) {
            TSP_Solver_UEF_241908.setWantedTourLength(bestFitness * wantedPercentsAddedTo100_Min1_Max3);
            System.out.println("----- NEW FITNESS REQUIREMENT: " + TSP_Solver_UEF_241908.getWantedTourLength());
            return TSP_Solver_UEF_241908.getWantedTourLength(); // tour length in other words, less than that means terminating 
        }
        else {
            return TSP_Solver_UEF_241908.getWantedTourLength();
        }
    }
    
    /**
     * Possibly swap 2 random vertices/points/nodes when the points are stored 
     * like xy xy xy xy... 
     * Sort of mutation that affects the TSP tour of the child when this is called for the child. 
     * 
     * @param c Chromosome
     * @return c Chromosome
     */
    public static Chromosome possiblySwapTwoRandomNodes(Chromosome c) {
        int i = (c.getChromosomePoints().size()/2)-4; // maximum 
        int random1x = (int) ((Math.random() * (i - 1)) + 1);
        int random1y = random1x +1;
        int random2x = (int) ((Math.random() * (i - 1)) + 1);
        int random2y = random2x +1;
        
        if((Math.random() < 0.5) && (random1x%2 == 0) && (random2x%2 == 0) && (i > 0) && 
                (random1x != random2x) && (random1x > 1) && (random2x > random1x)) {
            Collections.swap(c.getChromosomePoints(), (int)(random1x), (int)(random2x));
            Collections.swap(c.getChromosomePoints(), (int)(random1y), (int)(random2y));
        }
        return c;
    }
}

/**
 * OLD NOTES, DESIGN THOUGHTS WHEN PROGRAMMING THIS: 
 * 
 * set population (size x), add to the population one by one (for loop), push fragments to stack
 * 
 * growth, som neuron movements, tsp tours are made in a loop that has terminating
 * 
 * select the best ones that have good fitness, terminate others
 * 
 * reproduce, crossing-overs to the stacks
 * 
 * new chromosomes are created
 * 
 * also do random mutations for children (shuffle the stacks for some parts)
 * 
 * ...until only 1 remains - if multiple, then just select the best one.
 */