package jrcpsp;
/**
 *
 * @author Dan Princ
 * @date 17.4.2013
 */
public class Main {
    public static void main(String[] args) {
	
	Activity a1 = new Activity(1, 4, 2);
	Activity a2 = new Activity(2, 3, 3);
	Activity a3 = new Activity(3, 2, 1);
	Activity a4 = new Activity(4, 5, 1);
	Activity a5 = new Activity(5, 2, 4);
	
	Activity a6 = new Activity(6, 2, 2);
	Activity a7 = new Activity(7, 2, 1);
	
	a2.addPrev(a1);
	a3.addPrev(a2);
	a4.addPrev(a1);
	a5.addPrev(a4);
	
	a6.addPrev(a1);
	a7.addPrev(a4);
	
	Rcpsp sheduling = new Rcpsp(a1, 7, 4);
	sheduling.search();
	System.out.println(sheduling);

    }

}
