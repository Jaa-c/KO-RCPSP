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
    
    //private int startTime;
    //private int endTime;
    
    //private int eFinish;
    private int eStart;
    //private int lFinish;
    private int lStart;
    
    
    public Activity(int name, int duration, int[] resources) {
	next = new ArrayList<>();
	prev = new ArrayList<>();
	
	this.name = name;
	this.duration = duration;
	this.resources = resources;
	
	//startTime = Integer.MAX_VALUE - duration;
    
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
//	prev.addNext(this);
//	return this;
    }

    public int getDuration() {
	return duration;
    }

    public int[] getResources() {
	return resources;
    }

//    public int getStartTime() {
//	return startTime;
//    }
    
//    public int getEndTime() {
//	return endTime;
//    }

//    public void setStartTime(int startTime) {
//	endTime = startTime + duration;
//	this.startTime = startTime;
//    }

    public int getName() {
	return name;
    }
//
//    public int geteFinish() {
//	return eFinish;
//    }
//
//    public void seteFinish(int eFinish) {
//	this.eFinish = eFinish;
//    }

    public int geteStart() {
	return eStart;
    }

    public void seteStart(int eStart) {
	this.eStart = eStart;
    }

//    public int getlFinish() {
//	return lFinish;
//    }
//
//    public void setlFinish(int lFinish) {
//	this.lFinish = lFinish;
//    }

    public int getlStart() {
	return lStart;
    }

    public void setlStart(int lStart) {
	this.lStart = lStart;
    }
    
//    @Override
//    protected Activity clone()  {
//	try {
//	    return (Activity) super.clone();
//	}
//	catch (CloneNotSupportedException e) {
//	    return null;
//	}
//    }

}
