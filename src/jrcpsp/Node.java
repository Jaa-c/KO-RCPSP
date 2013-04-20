package jrcpsp;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Dan Princ
 * @since 17.4.2013
 */
public class Node {
    
    //private final HashMap<Integer, Activity> schedule;
    private final BitSet activities;
    private final Activity addedActivity;
    private final int maxTime;
    
    private int[] activityBeginings;
    
    private final List<Node> children;
    private final Node prev;
    
    private boolean feasible;
    
    private final Set<Integer> beginings; //possible beginnings of activities during this partial schedule
        
    
    public Node(Node prev, Activity activity, int addedActivityStart) {
	this.children = new ArrayList<>();
	
	this.prev = prev; 
	this.addedActivity = activity;
	
	
	if(prev != null) {
	    //schedule = new HashMap<>(prev.getActivities());
	    activities = (BitSet) prev.getActivities().clone();
	    activityBeginings = prev.getActivityBeginings().clone();
	    
	    this.maxTime = prev.getMaxTime() > addedActivityStart + addedActivity.getDuration() ?
		prev.getMaxTime() : addedActivityStart + addedActivity.getDuration();
	    
	}
	else {
	    //schedule = new HashMap<>();
	    activities = new BitSet();
	    activityBeginings = new int[Main.activityList.length];
	    for(int i = 0; i < activityBeginings.length; i++) {
		activityBeginings[i] = Main.maxFinish * 100;
	    }
	    this.maxTime = addedActivityStart + addedActivity.getDuration();
	}
	
	//schedule.put(activity.getName(), activity);
	activities.set(activity.getName());
	this.setActivityStart(activity, addedActivityStart);
	
	beginings = new HashSet<>();//todo
	beginings.add(addedActivityStart);
	
	for (int i = activities.nextSetBit(0); i >= 0; i = activities.nextSetBit(i+1)) {
	    if(getActivityEnd(Main.activityList[i-1]) > addedActivityStart) {
		beginings.add(getActivityEnd(Main.activityList[i-1]));
	    }
	}
	
//	for(Activity a : schedule.values()) {
//	    if(getActivityEnd(a) > addedActivityStart) {
//		beginings.add(getActivityEnd(a));
//	    }
//	}
	
	feasible = true;
    }
    
//    public List<Activity> getPrev(List<Activity> prev) {
//	for(Activity a : prev) {
//	    if(schedule.containsKey(a.getName())) {
//		a.setStartTime(schedule.get(a.getName()).getStartTime());
//	    }
//	}
//	return prev;
//    }
    
    public final void setActivityStart(Activity a, int start) {
	activityBeginings[a.getName()-1] = start;
    }
    
     public final int getActivityEnd(Activity a) {
	return activityBeginings[a.getName()-1] + a.getDuration();
    }
    
    public final int getActivityStart(Activity a) {
	return activityBeginings[a.getName()-1];
    }
    
    
    public void addChild(Node n) {
	children.add(n);
    }
    
    public void addChildren(List<Node> n) {
	children.addAll(n);
    }
    
    public List<Node> getChildren() {
	return children;
    }
    
    public void addActivity(Activity a) {
	activities.set(a.getName());
	//schedule.put(a.getName(), a);
    }
    
    public BitSet getActivities() {
	return activities;
	//return schedule;
    }
    

    public Node getPrev() {
	return prev;
    }

    public boolean isFeasible() {
	return feasible;
    }

    public void setFeasible(boolean notFeasible) {
	this.feasible = notFeasible;
    }

    public Activity getLastActivity() {
	return addedActivity;
    }

    public int getMaxTime() {
	return maxTime;
    }

    public Set<Integer> getBeginings() {
	return beginings;
    }

    public int[] getActivityBeginings() {
	return activityBeginings;
    }
    
    
    
    
    
    

}
