package jrcpsp;

import java.util.ArrayList;
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
    
    private final HashMap<Integer, Activity> schedule;
    private final Activity lastActivity;
    private final int maxTime;
    
    private final List<Node> children;
    private final Node prev;
    
    private boolean feasible;
    
    private final Set<Integer> beginings; //possible beginnings of activities during this partial schedule
    
    
    public Node(Node prev, Activity activity) {
	this.children = new ArrayList<>();
	
	this.prev = prev; 
	this.lastActivity = activity;
	
	if(prev  != null) {
	    schedule = new HashMap<>(prev.getActivities());
	    this.maxTime = prev.getMaxTime() > lastActivity.getStartTime() + lastActivity.getDuration() ?
		prev.getMaxTime() : lastActivity.getStartTime() + lastActivity.getDuration();
	}
	else {
	    schedule = new HashMap<>();
	    this.maxTime = lastActivity.getStartTime() + lastActivity.getDuration();
	}
	
	schedule.put(activity.getName(), activity);
	
	
	beginings = new HashSet<>();
	beginings.add(lastActivity.getStartTime());
	for(Activity a : schedule.values()) {
	    if(a.getEndTime() > lastActivity.getStartTime()) {
		beginings.add(a.getEndTime());
	    }
	}
	
	feasible = true;
    }
    
    public List<Activity> getPrev(List<Activity> prev) {
	for(Activity a : prev) {
	    if(schedule.containsKey(a.getName())) {
		a .setStartTime(schedule.get(a.getName()).getStartTime());
	    }
	    else {
		System.out.println("no");
	    }
	}
	return prev;
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
	schedule.put(a.getName(), a);
    }
    
    public Map<Integer, Activity> getActivities() {
	return schedule;
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
	return lastActivity;
    }

    public int getMaxTime() {
	return maxTime;
    }

    public Set<Integer> getBeginings() {
	return beginings;
    }
    
    
    
    

}
