package jrcpsp;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 *
 * @author Dan Princ
 * @since 17.4.2013
 */
public class Node {
    
    private final BitSet activities;
    private int[] activityBeginings;
    
    //private final Activity addedActivity;
    private final int maxTime;
    
    private final List<Node> children;
    //private final Node prev;
        
    
    public Node(Node prev, Activity addedActivity, int addedActivityStart) {
	this.children = new ArrayList<>();
	
	//this.addedActivity = activity;
	
	
	if(prev != null) {
	    activities = (BitSet) prev.getActivities().clone();
	    activityBeginings = prev.getActivityBeginings().clone();
	    
	    this.maxTime = prev.getMaxTime() > addedActivityStart + addedActivity.getDuration() ?
		prev.getMaxTime() : addedActivityStart + addedActivity.getDuration();
	    
	}
	else {
	    activities = new BitSet();
	    activityBeginings = new int[Main.activityList.length];
	    for(int i = 0; i < activityBeginings.length; i++) {
		activityBeginings[i] = Main.maxFinish * 100;
	    }
	    this.maxTime = addedActivityStart + addedActivity.getDuration();
	}
	
	activities.set(addedActivity.getName());
	this.setActivityStart(addedActivity, addedActivityStart);
	
    }
    
    
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
    }
    
    public BitSet getActivities() {
	return activities;
    }
    
//    public Node getPrev() {
//	return prev;
//    }

//    public Activity getLastActivity() {
//	return addedActivity;
//    }

    public int getMaxTime() {
	return maxTime;
    }

//    public Set<Integer> getBeginings() {
//	return beginings;
//    }

    public int[] getActivityBeginings() {
	return activityBeginings;
    }
    
    
    
    
    
    

}
