package jrcpsp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Dan Princ
 * @date 17.4.2013
 */
public class Main {
    public static void main(String[] args) throws Exception {
	
	
	activityList = new Activity[9];
	activityList[0] = new Activity(1, 4, new int[] {2, 0});
	activityList[1] = new Activity(2, 3, new int[] {3, 0});
	activityList[2] = new Activity(3, 2, new int[] {1, 0});
	activityList[3] = new Activity(4, 5, new int[] {1, 0});
	activityList[4] = new Activity(5, 2, new int[] {4, 0});
	activityList[5] = new Activity(6, 2, new int[] {2, 0});
	activityList[6] = new Activity(7, 2, new int[] {0, 8});//20
	
	
	activityList[7] = new Activity(8, 4, new int[] {0, 4});
	activityList[8] = new Activity(9, 4, new int[] {3, 0});
	
	activityList[0].addNext(activityList[1]).addNext(activityList[3]).addNext(activityList[5]);
	activityList[1].addNext(activityList[2]);
	activityList[3].addNext(activityList[4]).addNext(activityList[6]);
	activityList[6].addNext(activityList[4]).addNext(activityList[7]);
	
	activityList[6].addNext(activityList[8]);
	
	Activity start = activityList[0];
	resources = new int[] {4, 8};
	maxDuration = 5;
	maxFinish = 28;
	findEStart(activityList[0], 0);
	
	start = parsePSPlibData("data/j301_7.sm");
	
	Rcpsp sheduling = new Rcpsp(start, activityList.length, resources, maxDuration);
	sheduling.search();
	System.out.println(sheduling);

    }
    
    public static Activity[] activityList;
    public static int maxDuration = 0;
    public static int maxFinish = 0;
    private static int[] resources;
    
    public static Activity parsePSPlibData(String file) throws Exception {
	
	int activities = -1;
	
	BufferedReader br = new BufferedReader(new FileReader(file));
	skip(br, 5);
	
	Pattern p = Pattern.compile("-?\\d+");
	Matcher m = p.matcher(br.readLine());
	if (m.find()) {
	  activities = Integer.parseInt(m.group());
	}
	skip(br, 48);
	
	//nacteme duration a resources
	activityList = new Activity[activities];
	String[] line;
	for (int i = 0; i < activities; i++) {
	    line = br.readLine().split("\\s+");
	    int[] res = new int[] {Integer.parseInt(line[4]), Integer.parseInt(line[5]), Integer.parseInt(line[6]), Integer.parseInt(line[7])};
	    activityList[i] = new Activity(
		    Integer.parseInt(line[1]),
		    Integer.parseInt(line[3]), 
		    res);
	    
	    maxFinish += activityList[i].getDuration();
	    
	    if(activityList[i].getDuration() > maxDuration) {
		maxDuration = activityList[i].getDuration();
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
		activityList[i].addNext(activityList[a - 1]);	    
	    }
	}
	
	//nastavime max a min moznej start a konec

	
	findEStart(activityList[0], 0);
	
	
	return activityList[0];
    }
    
    private static void findEStart(Activity a, int start) {
	a.setEarliestStart(start);
	for(Activity n : a.getNext()) {
	    findEStart(n, start + a.getDuration());
	}
	if(a.getNext().isEmpty()) {
	    findLStart(a, maxFinish - a.getDuration());
	}
    }
    
    private static void findLStart(Activity a, int start) {
	a.setLatestStart(start);
	if(a.getMinTimeAfter() < maxFinish - start) {
	    a.setMinTimeAfter(maxFinish - start);
	}
	for(Activity n : a.getPrev()) {
	    findLStart(n, start - a.getDuration());
	}
    }
    
    
    private static void skip(BufferedReader br, int lines) throws IOException {
    for (int i = 0; i < lines; i++) {
	    br.readLine(); //skip
	}
    }
    

}
