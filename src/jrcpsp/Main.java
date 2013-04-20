package jrcpsp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Dan Princ
 * @date 17.4.2013
 */
public class Main {
    public static void main(String[] args) throws Exception {
	
//	Activity a1 = new Activity(1, 4, new int[] {2, 0});
//	Activity a2 = new Activity(2, 3, new int[] {3, 0});
//	Activity a3 = new Activity(3, 2, new int[] {1, 0});
//	Activity a4 = new Activity(4, 5, new int[] {1, 0});
//	Activity a5 = new Activity(5, 2, new int[] {4, 0});
//	
//	Activity a6 = new Activity(6, 2, new int[] {2, 0});
//	Activity a7 = new Activity(7, 2, new int[] {0, 8});
//	
//	a1.addNext(a2).addNext(a4).addNext(a6);
//	a2.addNext(a3);
//	a4.addNext(a5).addNext(a7);
//	
//	Activity start = a1;
//	activities = 7;
//	resources = new int[] {4, 8};
//	maxDuration = 5;
	
	Activity start = parsePSPlibData("data/j301_7.sm");
	
	Rcpsp sheduling = new Rcpsp(start, activities, resources, maxDuration);
	sheduling.search();
	System.out.println(sheduling);

    }
    
    private static int maxDuration = 0;
    private static int activities;
    private static int[] resources;
    
    public static Activity parsePSPlibData(String file) throws Exception {
	BufferedReader br = new BufferedReader(new FileReader(file));
	skip(br, 5);
	
	Pattern p = Pattern.compile("-?\\d+");
	Matcher m = p.matcher(br.readLine());
	if (m.find()) {
	  activities = Integer.parseInt(m.group());
	}
	skip(br, 48);
	
	//nacteme duration a resources
	Activity[] act = new Activity[activities];
	String[] line;
	for (int i = 0; i < activities; i++) {
	    line = br.readLine().split("\\s+");
	    act[i] = new Activity(
		    Integer.parseInt(line[1]),
		    Integer.parseInt(line[3]), 
		    new int[] {Integer.parseInt(line[4]), Integer.parseInt(line[5]), Integer.parseInt(line[6]), Integer.parseInt(line[7])});
	    
	    if(act[i].getDuration() > maxDuration) {
		maxDuration = act[i].getDuration();
	    }
	
	}
	
	skip(br, 3);
	
	//nacteme resource constraint
	resources = new int[4];
	line = br.readLine().split("\\s+");
	resources[0] = Integer.parseInt(line[1]);
	resources[1] = Integer.parseInt(line[2]);
	resources[2] = Integer.parseInt(line[3]);
	resources[3] = Integer.parseInt(line[4]);
	
	br.close();
	br = new BufferedReader(new FileReader(file));
	skip(br, 18);
	//nacteme zavislosti na aktivitach
	for (int i = 0; i < activities; i++) {
	    line = br.readLine().split("\\s+");
	    for(int n = 1; n <= Integer.parseInt(line[3]); n++) {
		int a = Integer.parseInt(line[3+n]);
		act[i].addNext(act[a - 1]);	    
	    }
	}
	
	return act[0];
    }
    
    private static void skip(BufferedReader br, int lines) throws IOException {
    for (int i = 0; i < lines; i++) {
	    br.readLine(); //skip
	}
    }
    

}
