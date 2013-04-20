package jrcpsp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
    private Map<Integer, Activity> currentSchedule;
    
    public static int x = 0;

    public Rcpsp(Activity firstActivity, int activityCount, int[] resourcesLimit, int maxDuration) {
	
	firstActivity.setStartTime(0);
	this.rootNode = new Node(null, firstActivity);
	this.resourcesLimit = resourcesLimit;
	this.activityCount = activityCount;
	
	this.currentBest = Integer.MAX_VALUE;
	
	this.time = new int[maxDuration + 1][resourcesLimit.length];
    }
    
    public void search() {
	search(rootNode);
    }
	
    private void search(Node node) {
	
	if(!node.isFeasible() || node.getMaxTime() > currentBest) {
	    return;
	}
	
	x++;
	if(x % 10000 == 0 || x  > 916300)
	    System.out.println(x);
	
	
	Map<Integer, Activity> schedule = node.getActivities();
	int currentTime = node.getMaxTime();
	
	
	if(x  > 916300) {
	    System.out.println(currentTime);
	}
	
//	for (Activity a : schedule.values()) {
//	    System.out.print(a.getName());
//	}
//	System.out.println(" -> " + currentTime);
	
	//rozvrh je kompletni
	if(schedule.size() == activityCount) {
	    if(currentTime < currentBest) {
		System.out.println("Better solution: " + currentBest + " -> " + currentTime);
		currentBest = currentTime;
		currentSchedule = node.getActivities();
	    }
	}
	
	//pridame vsechny mozne varianty - deti uzlu, ktere jsou v castecnem rozvrhu
	for(Activity a : schedule.values()) {
	    for(Activity n : a.getNext()) {
		if(!schedule.containsKey(n.getName())) {
		    Node added;
		    Activity next = n.clone();	

		    if( currentTime + next.getDuration() < next.geteStart() ||
			currentTime + next.getDuration() > next.getlStart()) {
			continue; //asap
		    }
		    
		    //muzu ji pridat ted hned, pokud je jeji predek driv
		    for(int startTime : node.getBeginings()) {
			if(startTime < next.geteStart() || startTime > next.getlStart()) {
			    continue;
			}
			
			boolean add = true;
			next.setStartTime(startTime);
			//kontrola predku
			for(Activity test : node.getPrev(next.getPrev())) { //to do - prev only ID
			    if(test.getEndTime() > startTime) {
				add = false;
				break;
			    }
			}
			if(add) {
			    //kontrola zda neprekracuje limit
			    if(startTime + next.getDuration() < currentBest && 
				    checkPartialSchedule(schedule, next)) {
				added = new Node(node, next);
				node.addChild(added);
				//search(added);
			    }
			    next = n.clone();
			}
		    }

		    //a nebo po ni, pokud je to syn libovolneho v rozvrhu
		    if(currentTime + next.getDuration() < currentBest) {
			next.setStartTime(currentTime);
			if(checkPartialSchedule(schedule, next)) {
			    added = new Node(node, next);
			    node.addChild(added);
			    //search(added);
			}
		    }
		}
	    }
	}
	
	for(Node n : node.getChildren()) {
	    search(n);
	}
    }
    
    int[][] time;
    public boolean checkPartialSchedule(Map<Integer, Activity> partial, Activity last) {

	//vymazani starych hodnot 
	for(int i =0; i < time.length; i++) {
	    for(int j = 0; j < resourcesLimit.length; j++) {
		time[i][j] = last.getResources()[j];
	    }
	}
	
	final int resourcesSize = last.getResources().length;
	final int start = last.getStartTime();
	
	for(Activity a : partial.values()) {
	    if(a.getEndTime() > start) {
		for(int i = a.getStartTime(); i < a.getEndTime(); i++) {
		    int index = i - start;
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
	for(Activity a : currentSchedule.values()) {
	    res += "> " + a.getName() + ": start = " + a.getStartTime() + "->" + (a.getEndTime()) + "\n";
	}
	res += "recursive calls: " + x;
	return res;
    }
    
    

}
