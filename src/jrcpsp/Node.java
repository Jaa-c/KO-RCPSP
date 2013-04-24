package jrcpsp;

import java.util.BitSet;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Dan Princ
 * @since 17.4.2013
 */
public class Node {
    
    private final BitSet activities;
    private int[] activityBeginings;
    private int[] activityEndings;
    
    private int maxTime;
    private int minPossibleFinish;
    
    private final Set<Integer> beginings;
    
    public Node(Node prev, Activity addedActivity, int addedActivityStart) {
	
	if(prev != null) {
	    activities = (BitSet) prev.getActivities().clone();
	    activityBeginings = prev.getActivityBeginings().clone();
	    activityEndings = prev.getActivityEndings().clone();
	    
	    int cmt = addedActivityStart + addedActivity.getDuration() ;
	    maxTime = prev.getMaxTime() > cmt ? prev.getMaxTime() : cmt;
	    
	    int cmpf = addedActivityStart + addedActivity.getDuration() + addedActivity.getMinTimeAfter();
	    minPossibleFinish = prev.getMinPossibleFinish() > cmpf ? prev.getMinPossibleFinish() : cmpf;
	}
	else { //root node
	    activities = new BitSet();
	    activityBeginings = new int[Main.activityList.length];
	    activityEndings = new int[Main.activityList.length];
	    for(int i = 0; i < activityBeginings.length; i++) {
		activityBeginings[i] = Main.maxFinish * 100;
		activityEndings[i] = -1;
	    }
	    maxTime = addedActivity.getDuration();
	    minPossibleFinish = addedActivity.getDuration() + addedActivity.getMinTimeAfter();
	}
	
	
	activities.set(addedActivity.getName());
	this.setActivityStart(addedActivity, addedActivityStart);
	
	beginings = new TreeSet<>();
	beginings.add(addedActivityStart);
	beginings.add(maxTime);//konec rozvrhu, mozna zbytecny
	for (int i = activities.nextSetBit(0); i >= 0; i = activities.nextSetBit(i+1)) {
	    Activity a = Main.activityList[i-1];
	    if(getActivityEnd(a) > addedActivityStart) {
		beginings.add(getActivityEnd(a));
	    }
	}
		
    }
    
    public final void setActivityStart(Activity a, int start) {
	activityBeginings[a.getName()-1] = start;
	activityEndings[a.getName()-1] = start + a.getDuration();
    }
    
    public final int getActivityEnd(Activity a) {
	return activityBeginings[a.getName()-1] + a.getDuration();
    }
    
    public final int getActivityStart(Activity a) {
	return activityBeginings[a.getName()-1];
    }
    
    public void addActivity(Activity a) {
	activities.set(a.getName());
    }
    
    public BitSet getActivities() {
	return activities;
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

    public int[] getActivityEndings() {
	return activityEndings;
    }

    public void setActivityEndings(int[] activityEndings) {
	this.activityEndings = activityEndings;
    }
    
    public int getMinPossibleFinish() {
	return minPossibleFinish;
    }

}