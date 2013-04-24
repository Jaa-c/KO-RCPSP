package jrcpsp;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 *
 * @author Dan Princ
 * @since 17.4.2013
 */
public class Rcpsp {
    
    private Node rootNode;
    
    private final int[] resourcesLimit;
    private final int activityCount;
    
    private int currentBest;
    private int[] currentSchedule;
    
    private int[][] time;
    
    public static long x = 0;
    public static long y = 0;
    
    private BitSet end;
    
    private final static boolean VERBOSE = false;

    public Rcpsp(Activity firstActivity, int activityCount, int[] resourcesLimit, int maxDuration) {
	
	this.rootNode = new Node(null, firstActivity, 0);
	
	this.resourcesLimit = resourcesLimit;
	this.activityCount = activityCount;
	
	this.currentBest = Integer.MAX_VALUE;
	
	this.time = new int[maxDuration + 1][resourcesLimit.length];
	
	end = new BitSet();
	for(int i = 1; i <= Main.activityList.length; i++) {
	    end.set(i);
	}
    }
        
    public void search() {
	search(rootNode);
    }  
    
    private void search(Node node) {
		
	BitSet schedule = (BitSet) node.getActivities();
	int currentTime = node.getMaxTime();
	
	if(++x % 1000000 == 0) {
	    System.out.println(x + "/" + y + ": " + currentBest);
	}
	if(VERBOSE) {
	    System.out.print("[" + x + "] ");
	    for (int i = schedule.nextSetBit(0); i >= 0; i = schedule.nextSetBit(i+1)) {
		System.out.print(Main.activityList[i-1].getName() + ",");
	    }
	    System.out.println(" -> " + currentTime);
	}
	
	//rozvrh je kompletni
	if(schedule.cardinality() == activityCount) {
	    if(currentTime < currentBest) {
		System.out.println("["  + x + "] Better solution: " + currentBest + " -> " + currentTime);
		currentBest = currentTime;
		currentSchedule = node.getActivityBeginings().clone();
	    }
	    return;
	}
	
	//pridame vsechny mozne varianty - deti uzlu, ktere jsou v castecnem rozvrhu
	for (int i = schedule.nextSetBit(0); i >= 0; i = schedule.nextSetBit(i+1)) {
	    Activity a = Main.activityList[i-1];
	    
	    for(Activity next : a.getNext()) {
		if(!schedule.get(next.getName())) {

		    if( currentTime + next.getDuration() < next.setEarliestStart()) { 
			continue; //asap
		    }
		    else {
			y++;
		    }
		    
		    //muzu ji pridat ted hned, pokud je jeji predek driv
		    for(int startTime : node.getBeginings()) {
			if(startTime < next.setEarliestStart() || startTime > next.getLatestStart()) {
			    continue;
			}
			if(startTime + next.getDuration()+ next.getMinTimeAfter() >= currentBest) { //starTime is ordered, any other will be only longer!
			    break;
			}
			else {
			    y++;
			}
						
			boolean add = true;
			//kontrola predku
			for(Activity test : next.getPrev()) { 
			    if(node.getActivityEnd(test) > startTime) {
				add = false;
				break;
			    }
			}
			if(add) {
			    //kontrola zda neprekracuje limit
			    if(startTime + next.getDuration() + next.getMinTimeAfter() < currentBest && // + next.getMinTimeAfter() 
				    checkPartialSchedule(node, next, startTime)) {
				if(VERBOSE) System.out.print("o" + next.getName() + " ");
				Node n = new Node(node, next, startTime);
				if(n.getMinPossibleFinish() < currentBest) { //jestli neni minimalni mozny rozvrh delsi nez nejelpsi nalezeny
				    search(n);
				}
			    }
			    else {
				y++;
			    }
			}
			else {
			    y++;
			}
		    }
		}
	    }
	}
    }
    
    public boolean checkPartialSchedule(final Node node, final Activity last, final int lastActivityStartTime) {

	//vymazani starych hodnot 
	for(int i =0; i < time.length; i++) {
	    for(int j = 0; j < resourcesLimit.length; j++) {
		time[i][j] = last.getResources()[j];
	    }
	}
	
	final int resourcesSize = last.getResources().length;
	
	for (int next = node.getActivities().nextSetBit(0); next >= 0; next = node.getActivities().nextSetBit(next+1)) {
	    final Activity a = Main.activityList[next-1];
	    
	    if(node.getActivityEnd(a) > lastActivityStartTime) {
		for(int i = node.getActivityStart(a); i < node.getActivityEnd(a); i++) {
		    int index = i - lastActivityStartTime;
		    if(index >= 0 && index <= last.getDuration()) {
			for(int r = 0; r < resourcesSize; r++) {
			    time[index][r] += a.getResources()[r];
			    if(time[index][r] > resourcesLimit[r]) {
				return false;
			    }
			}
		    }
		}
	    }
	}
	return true;
    }
    
    @Override
    public String toString() {
	String res = "> Optimal solution: " + currentBest + "\n";
	for(int i = 0; i < Main.activityList.length; i++) {
	    res += "> " + (i+1) + ": time=" + currentSchedule[i] + "->" + (currentSchedule[i] + Main.activityList[i].getDuration()) + "\n";
	}
	float perc = (float)y/(float)x;
	res += "recursive calls: " + x + " pruned " + perc +"x more";// + y;
	return res;
    }
    
}
