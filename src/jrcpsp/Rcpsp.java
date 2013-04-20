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

    public Rcpsp(Activity firstActivity, int activityCount, int[] resourcesLimit) {
	
	firstActivity.setStartTime(0);
	this.rootNode = new Node(null, firstActivity);
	this.resourcesLimit = resourcesLimit;
	this.activityCount = activityCount;
	
	this.currentBest = Integer.MAX_VALUE;
    }
    
    public void search() {
	search(rootNode);
    }
	
    private void search(Node node) {
	
	if(!node.isFeasible() || node.getMaxTime() > currentBest) {
	    return;
	}
	
	x++;
	
	Map<Integer, Activity> schedule = node.getActivities();
	int time = node.getMaxTime();
	
//	for (Activity a : schedule.values()) {
//	    System.out.print(a.getName());
//	}
//	System.out.println(" -> " + time);
	
	//rozvrh je kompletni
	if(schedule.size() == activityCount) {
	    if(time < currentBest) { //tohle by melo byt opet zbytecny a projit vzdy
		//System.out.println("Better solution: " + currentBest + " -> " + time);
		currentBest = time;
		currentSchedule = node.getActivities();
	    }
	}
	
	//pridame vsechny mozne varianty - deti uzlu, ktere jsou v castecnem rozvrhu
	for(Activity a : schedule.values()) {
	    for(Activity n : a.getNext()) {
		if(!schedule.containsKey(n.getName())) {
		    Node added;
		    Activity next = n.clone();		    
		    
		    //muzu ji pridat ted hned, pokud je jeji predek driv
		    for(int startTime : node.getBeginings()) {
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
				search(added);
			    }
			    next = n.clone();
			}
		    }

		    //a nebo po ni, pokud je to syn libovolneho v rozvrhu
		    if(time + next.getDuration() < currentBest) {
			next.setStartTime(time);
			if(checkPartialSchedule(schedule, next)) {
			    added = new Node(node, next);
			    node.addChild(added);
			    search(added);
			}
		    }
		}
	    }
	}
	
//	for(Node n : node.getChildren()) {
//	    search(n);
//	}
    }
    
    
    public boolean checkPartialSchedule(Map<Integer, Activity> partial, Activity last) {
	
	List<Activity> list = new ArrayList<>(partial.values());
	list.add(last);
	
	final int resourcesSize = last.getResources().length;
	
	int[][] time = new int[last.getDuration() + 1][resourcesSize];
	final int start = last.getStartTime();
	
	for(Activity a : list) {
	    if(a.getEndTime() > start) {
		for(int i = a.getStartTime(); i < a.getEndTime(); i++) {
		    int index = i-start;
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
