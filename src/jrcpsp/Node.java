package jrcpsp;

import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Dan Princ
 * @since 17.4.2013
 */
public class Node {
    
    private final BitSet activities;
    private int[] activityBeginings;
    
    //private final Activity addedActivity;
    private int maxTime;
    private int minPossibleFinish;
    
    private final Set<Integer> beginings;
    
    public Node(Node prev, Activity addedActivity, int addedActivityStart) {
	
	//this.addedActivity = addedActivity;
	
	
	if(prev != null) {
	    activities = (BitSet) prev.getActivities().clone();
	    activityBeginings = prev.getActivityBeginings().clone();
	    
	    int cmt = addedActivityStart + addedActivity.getDuration() ;
	    maxTime = prev.getMaxTime() > cmt ? prev.getMaxTime() : cmt;
	    
	    int cmpf = addedActivityStart + addedActivity.getDuration() + addedActivity.getMinTimeAfter();
	    minPossibleFinish = prev.getMinPossibleFinish() > cmpf ? prev.getMinPossibleFinish() : cmpf;
	
	}
	else {
	    activities = new BitSet();
	    activityBeginings = new int[Main.activityList.length];
	    for(int i = 0; i < activityBeginings.length; i++) {
		activityBeginings[i] = Main.maxFinish * 100;
	    }
	    
	    maxTime = addedActivity.getDuration();
	    minPossibleFinish = addedActivity.getDuration() + addedActivity.getMinTimeAfter();
	}
	
	
	activities.set(addedActivity.getName());
	this.setActivityStart(addedActivity, addedActivityStart);
	
	beginings = new HashSet<>();
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
    
//    public Activity getLastActivity() {
//	return addedActivity;
//    }

    
    public int getMaxTime() {
	return maxTime;
    }

    public Set<Integer> getBeginings() {
	return beginings;
    }

    public int[] getActivityBeginings() {
	return activityBeginings;
    }

    public int getMinPossibleFinish() {
	return minPossibleFinish;
    }
   
    

}
