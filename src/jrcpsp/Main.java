package jrcpsp;
/**
 *
 * @author Dan Princ
 * @date 17.4.2013
 */
public class Main {
    public static void main(String[] args) {
	
	Activity a1 = new Activity(1, 4, new int[] {2, 0});
	Activity a2 = new Activity(2, 3, new int[] {3, 0});
	Activity a3 = new Activity(3, 2, new int[] {1, 0});
	Activity a4 = new Activity(4, 5, new int[] {1, 0});
	Activity a5 = new Activity(5, 2, new int[] {4, 0});
	
	Activity a6 = new Activity(6, 2, new int[] {2, 0});
	Activity a7 = new Activity(7, 2, new int[] {0, 8});
	
	a2.addPrev(a1);
	a3.addPrev(a2);
	a4.addPrev(a1);
	a5.addPrev(a4);
	
	a6.addPrev(a1);
	a7.addPrev(a4);
	
	
	Rcpsp sheduling = new Rcpsp(a1, 7, new int[] {4, 8});
	sheduling.search();
	System.out.println(sheduling);

    }

}
