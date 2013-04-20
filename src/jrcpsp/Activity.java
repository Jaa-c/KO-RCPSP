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
    
    private int startTime;
    
    
    public Activity(int name, int duration, int[] resources) {
	next = new ArrayList<>();
	prev = new ArrayList<>();
	
	this.name = name;
	this.duration = duration;
	this.resources = resources;
	
	startTime = Integer.MAX_VALUE - duration;
    
    }

    public List<Activity> getNext() {
	return next;
    }

    protected void addNext(Activity next) {
	this.next.add(next);
    }

    public List<Activity> getPrev() {
	return prev;
    }

    public Activity addPrev(Activity prev) {
	this.prev.add(prev);
	prev.addNext(this);
	return this;
    }

    public int getDuration() {
	return duration;
    }

    public int[] getResources() {
	return resources;
    }

    public int getStartTime() {
	return startTime;
    }
    
    public int getEndTime() {
	return startTime + duration;
    }

    public void setStartTime(int startTime) {
	this.startTime = startTime;
    }

    public int getName() {
	return name;
    }

    @Override
    protected Activity clone()  {
	try {
	    return (Activity) super.clone();
	}
	catch (CloneNotSupportedException e) {
	    return null;
	}
    }

}
