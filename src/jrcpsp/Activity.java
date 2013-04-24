package jrcpsp;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dan Princ
 * @since 17.4.2013
 */
public class Activity implements Cloneable {
    
    private List<Activity> next;
    private List<Activity> prev;
    
    private final int name;
    
    private final int duration;
    private final int[] resources;
    
    private int eStart;
    private int lStart;
    private int minTimeAfter;

    public Activity(int name, int duration, int[] resources) {
	next = new ArrayList<>();
	prev = new ArrayList<>();
	
	this.name = name;
	this.duration = duration;
	this.resources = resources;
	    
    }
    
    public int getMinTimeAfter() {
	return minTimeAfter;
    }

    public void setMinTimeAfter(int minTimeAfter) {
	this.minTimeAfter = minTimeAfter;
    }

    public List<Activity> getNext() {
	return next;
    }

    protected Activity addNext(Activity next) {
	this.next.add(next);
	next.addPrev(this);
	return this;
    }

    public List<Activity> getPrev() {
	return prev;
    }

    public void addPrev(Activity prev) {
	this.prev.add(prev);
    }

    public int getDuration() {
	return duration;
    }

    public int[] getResources() {
	return resources;
    }

    public int getName() {
	return name;
    } 
    
    public int setEarliestStart() {
	return eStart;
    }

    public void setEarliestStart(int eStart) {
	this.eStart = eStart;
    }

    public int getLatestStart() {
	return lStart;
    }

    public void setLatestStart(int lStart) {
	this.lStart = lStart;
    }

}
