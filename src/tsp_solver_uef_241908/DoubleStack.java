package tsp_solver_uef_241908;

/** 
 * TSP Solver by Tuomas Hyvönen, Java file 2 of 11 
 * 
 * Implements a stack of decimal real numbers (doubles). 
 * The maximum number of elements allowed in the stack is given by MAX_SIZE. 
 * 
 * Partly inspired by https://courses.cs.washington.edu/courses/cse326/03su/homework/hw1/DoubleStack.java 
 * by the Paul G. Allen School of Computer Science and Engineering, University of Washington 
 * (visited on 21.10.2016), created years ago for another TSP solver when I had less knowledge about programming 
 * and did not want to "reinvent the wheel". 
 * 
 * Open source Java code, feel free to edit and try your own improvements. 
 * Tested with Windows 11 
 * Apache NetBeans 25 
 * Java JDK 17 64bit 
 * 
 * @author Tuomas Hyvönen 
 * @version 2.1
 */
public class DoubleStack {
    private final double[] DATA;
    private int topIndex;
    private static final int MAX_SIZE = 5000000;
    
    /**
     * The constructor.
     */
    public DoubleStack() {
        DATA = new double[MAX_SIZE];
        topIndex = -1;
    } 
    
    /**
     * Return the top index.
     * @return topIndex integer
     */
    public int getTopIndex() {
        return topIndex;
    }

    /**
     * Checks if the stack is empty (is the top index -1?)
     * @return boolean
     */
    public boolean isEmpty() {
        return topIndex <= -1;
    }

    /**
     * Checks if the stack is full (is the top index more or equal than MAX_SIZE - 1?)
     * @return boolean
     */
    public boolean isFull() {
        return topIndex >= MAX_SIZE - 1;
    }

    /**
     * Mark the stack empty (set the top index -1), the values may still exist in memory.
     */
    public void empty() {
        topIndex = -1;
    }
    
    /**
     * Push the number "double x" onto the top of the stack. 
     * @param x double
     */
    public void push(double x) {
        if(isFull() == false) {
            topIndex++;
            DATA[topIndex] = x;
            //System.out.println("push(double): Pushes " + data[topIndex] + 
            //        " to the stack. ");
        }
        else{
            //System.out.println("\t\t\t push(): Tried to push an "
            //        + "element but the stack is full");
        }
    }
    
    /**
     * Return the top element of the stack.
     * @return double
     */
    public double top() {
        if(isEmpty() == false) {
            //System.out.println("top(): Returns " + data[topIndex] + ". ");
            return DATA[topIndex];
        }
        else{
            //System.out.println("\t\t top(): Tried to return the top "
            //        + "element but the stack is empty");
            return Double.MAX_VALUE; 
        }
    }
    
    /**
     * Remove the top element from the stack.
     */
    public void pop() {
        if(isEmpty() == false) {
            //System.out.println("pop(): Removes " + data[topIndex] + ". ");
            DATA[topIndex] = Double.MAX_VALUE; 
            topIndex--;
        }
        else{
            //System.out.println("\t\t pop(): Tried to remove the top element "
            //        + "but the stack is empty");
        }
    }
} 