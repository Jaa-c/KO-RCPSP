package jrcpsp;

import java.util.BitSet;

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
    private int[] currentSchedule;
    
    
    private int[][] time;
    
    public static int x = 0;
    public static int y = 0;
    
    private BitSet end;

    public Rcpsp(Activity firstActivity, int activityCount, int[] resourcesLimit, int maxDuration) {
	
	this.rootNode = new Node(null, firstActivity, 0);
	
	this.resourcesLimit = resourcesLimit;
	this.activityCount = activityCount;
	
	this.currentBest = Integer.MAX_VALUE;
	
	this.time = new int[maxDuration + 1][resourcesLimit.length];
	
	end = new BitSet();
	for(int i = 1; i <= Main.activityList.length; i++) {
	    end.set(i);
	}
    }
    
    public void search() {
	search(rootNode);
    }
	
    private void search(Node node) {
	
	if(node.getMaxTime() > currentBest) {
	    y++;
	    return;
	}
	
	BitSet schedule = node.getActivities();
	int currentTime = node.getMaxTime();
	
	x++;
	if(x % 1000000 == 0) {
	    System.out.println(x + "/" + y + ": " + currentBest);
	}
	
//	for (int i = schedule.nextSetBit(0); i >= 0; i = schedule.nextSetBit(i+1)) {
//	    System.out.print(Main.activityList[i-1].getName());
//	}
//	System.out.println(" -> " + currentTime);
	
	//rozvrh je kompletni
	if(schedule.cardinality() == activityCount) {
	    if(currentTime < currentBest) {
		System.out.println("["  + x + "] Better solution: " + currentBest + " -> " + currentTime);
		currentBest = currentTime;
		currentSchedule = node.getActivityBeginings().clone();
	    }
	}
	
	//pridame vsechny mozne varianty - deti uzlu, ktere jsou v castecnem rozvrhu
	//for(Activity a : schedule.values()) {
	for (int i = schedule.nextSetBit(0); i >= 0; i = schedule.nextSetBit(i+1)) {
	    Activity a = Main.activityList[i-1];
	    
	    for(Activity next : a.getNext()) {
		if(!schedule.get(next.getName())) {

		    if( currentTime + next.getDuration() < next.geteStart()) { 
			continue; //asap
		    }
		    else {
			//y++;
		    }
		    
		    //muzu ji pridat ted hned, pokud je jeji predek driv
		    for(int startTime : node.getBeginings()) {
			if(startTime < next.geteStart() || startTime > next.getLatestStart()) {
			    continue;
			}
			else {
			    //y++;
			}
			
			if(x == 40) {
			    x = 40;
			}
			
			boolean add = true;
			//kontrola predku
			for(Activity test : next.getPrev()) { 
			    if(node.getActivityEnd(test) > startTime) {
				add = false;
				break;
			    }
			}
			if(add) {
			    //kontrola zda neprekracuje limit
			    if(startTime + next.getDuration()  < currentBest && //+ next.getMinTimeAfter()
				    checkPartialSchedule(node, next, startTime)) {
				
				search(new Node(node, next, startTime));
			    }
			    else {
			    	break;
			    }
			}
			else {
			    y++;
			}
		    }

		    //a nebo po ni, pokud je to syn libovolneho v rozvrhu
		    if(currentTime + next.getDuration()  < currentBest && //+ next.getMinTimeAfter()
			    checkPartialSchedule(node, next, currentTime)) {
			
			search(new Node(node, next, currentTime));
		    }
		    else {
			y++;
		    }
		}
	    }
	}
	
    }
    
    public boolean checkPartialSchedule(final Node node, final Activity last, final int lastActivityStartTime) {

	//vymazani starych hodnot 
	for(int i =0; i < time.length; i++) {
	    for(int j = 0; j < resourcesLimit.length; j++) {
		time[i][j] = 0;//last.getResources()[j];
	    }
	}
	
	final int resourcesSize = last.getResources().length;
	
	for (int next = node.getActivities().nextSetBit(0); next >= 0; next = node.getActivities().nextSetBit(next+1)) {
	    final Activity a = Main.activityList[next-1];
	    
	    if(node.getActivityEnd(a) > lastActivityStartTime) {
		for(int i = node.getActivityStart(a); i < node.getActivityEnd(a); i++) {
		    int index = i - lastActivityStartTime;
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
	for(int i = 0; i < Main.activityList.length; i++) {
	    res += "> " + (i+1) + ": start = " + currentSchedule[i] + "->" + (currentSchedule[i] + Main.activityList[i].getDuration()) + "\n";
	}
	res += "recursive calls: " + x;
	return res;
    }
    
    

}
