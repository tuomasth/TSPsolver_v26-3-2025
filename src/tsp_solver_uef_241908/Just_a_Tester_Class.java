package tsp_solver_uef_241908;
import java.util.*;

/**
 * TSP Solver by Tuomas Hyvönen, Java file 4 of 11 (unnecessary tester class) 
 * 
 * Extra Java main method for testing purposes. Does not create a new User Interface window. 
 * If used, disable the main method in "TSP_Solver_UEF_241908.java" first. That creates 
 * a new UI every single time unlike this Java file here. 
 * 
 * Open source Java code, feel free to edit and try your own improvements. 
 * Tested with Windows 11 
 * Apache NetBeans 
 * Java JDK 17 64bit 
 * 
 * @author Tuomas Hyvönen 
 * @version 2.1
 */
public class Just_a_Tester_Class {
    
    /**
     * Possible main method when testing single details, rename to "main", 
     * call whatever methods you want and test them!
     * 
     * @param args String[]
     */
    public static void previousmain(String args[]) {
        // change the name to "main" if used to test single methods without the user interface 
        // the other one can be "previousmain", for example 
        
        Point p1 = new Point(1.0, 1.1);  // 
        Point p2 = new Point(1.2, 9.1);  // 
        Point p3 = new Point(8.0, 9.1);  // 
        Point p4 = new Point(8.2, 1.1);  // 
        Point p5 = new Point(2.0, 3.1);  // 
        Point p6 = new Point(3.2, 6.1);  // 
        Point p7 = new Point(2.0, 4.1);  // 
        Point p8 = new Point(5.2, 5.1);  // 
        //Point p9 = new Point(6.0, 3.1);  // 
        //Point p10 = new Point(6.2, 7.1); // 
        
        ArrayList<Double> a_list = new ArrayList<>(Arrays.asList(
                p1.getX(), p1.getY(), 
                p2.getX(), p2.getY(), p2.getX(), p2.getY(),
                p3.getX(), p3.getY(), p3.getX(), p3.getY(), 
                p4.getX(), p4.getY(), p4.getX(), p4.getY(),
                p5.getX(), p5.getY(), p5.getX(), p5.getY(), 
                p6.getX(), p6.getY(), p6.getX(), p6.getY(),
                p7.getX(), p7.getY(), p7.getX(), p7.getY(), 
                p8.getX(), p8.getY(), p8.getX(), p8.getY(),
                p1.getX(), p1.getY())
        );
        double euc = 0.0;
        for(int i = 0; i < a_list.size()-2; i+=2) {
            euc += Sub_algorithms.Euclidean_distance((double)a_list.get(i), 
                                         (double)a_list.get(i+1), 
                                         (double)a_list.get(i+2), 
                                         (double)a_list.get(i+3));
        }
        
        System.out.println(a_list.toString());
        
        ArrayList newlist = Sub_algorithms.threeOpt(a_list, euc);
        
        System.out.println(newlist.toString());
        
        /*
        
        ArrayList<Double> inputXs = new ArrayList<>(Arrays.asList(
                p1.getX(), p2.getX(), p3.getX(), p4.getX()));
        ArrayList<Double> inputYs = new ArrayList<>(Arrays.asList(
                p1.getY(), p2.getY(), p3.getY(), p4.getY()));
        ArrayList<Double> neuronXs = new ArrayList<>(Arrays.asList(
                p5.getX(), p6.getX(), p7.getX(), p8.getX(), p9.getX(), p10.getX()));
        ArrayList<Double> neuronYs = new ArrayList<>(Arrays.asList(
                p5.getY(), p6.getY(), p7.getY(), p8.getY(), p9.getY(), p10.getY()));
        
        KohonenSOM.performSOM(20, 10.0, inputXs,
                inputYs, neuronXs, neuronYs, false, null, 0.02); 
        // false & null means "no logic stacks & no chromosomes" 

        System.out.println("\n");
        
        ArrayList test3opt = new ArrayList();
        test3opt.add(1.0); test3opt.add(1.0);
        
        test3opt.add(2.0); test3opt.add(2.0); // 1
        test3opt.add(2.0); test3opt.add(2.0);
        
        test3opt.add(4.0); test3opt.add(2.0); // 2
        test3opt.add(4.0); test3opt.add(2.0);
        
        test3opt.add(5.0); test3opt.add(3.0); // 3
        test3opt.add(5.0); test3opt.add(3.0);
        
        test3opt.add(5.0); test3opt.add(5.0); // 4
        test3opt.add(5.0); test3opt.add(5.0);
        
        test3opt.add(6.0); test3opt.add(6.0); // 5
        test3opt.add(6.0); test3opt.add(6.0);
        
        test3opt.add(7.0); test3opt.add(5.0); // 6
        test3opt.add(7.0); test3opt.add(5.0);
        
        test3opt.add(4.0); test3opt.add(4.0); // 7
        test3opt.add(4.0); test3opt.add(4.0);
        
        test3opt.add(1.0); test3opt.add(1.0); // 8
        
        double euc = 0;
        for(int i = 0; i < test3opt.size()-2; i+=2) {
            euc += Sub_algorithms.Euclidean_distance((double)test3opt.get(i), 
                                         (double)test3opt.get(i+1), 
                                         (double)test3opt.get(i+2), 
                                         (double)test3opt.get(i+3));
        }
        System.out.println(euc);
        Sub_algorithms.threeOpt(test3opt, euc);
        
        System.out.println(test3opt);
        
        */
    }
} 