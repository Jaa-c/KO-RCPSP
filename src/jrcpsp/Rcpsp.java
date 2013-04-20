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
	search(rootNode, 0);
    }
	
    private void search(Node node, int lastActivityStart) {
	
	if(node.getMaxTime() > currentBest) {
	    return;
	}
	
	BitSet schedule = node.getActivities();
	int currentTime = node.getMaxTime();
	
	x++;
	if(x % 10000 == 0)
	    System.out.println(x);
	
//	if(x  > 916300) {
//	    System.out.println(currentTime);
//	}
	
//	for (Activity a : schedule.values()) {
//	    System.out.print(a.getName());
//	}
//	System.out.println(" -> " + currentTime);
	
	
	//rozvrh je kompletni
	//if(schedule.size()== activityCount) {
	//if(schedule.get(Main.activityList.length)) {
	if(schedule.cardinality() == activityCount) {
	    if(currentTime < currentBest) {
		System.out.println("Better solution: " + currentBest + " -> " + currentTime);
		currentBest = currentTime;
		currentSchedule = node.getActivityBeginings().clone();
	    }
	}
	
	//pridame vsechny mozne varianty - deti uzlu, ktere jsou v castecnem rozvrhu
	//for(Activity a : schedule.values()) {
	for (int i = schedule.nextSetBit(0); i >= 0; i = schedule.nextSetBit(i+1)) {
	    Activity a = Main.activityList[i-1];
	    
	    for(Activity next : a.getNext()) {
		//if(!schedule.containsKey(next.getName())) {
		if(!schedule.get(next.getName())) {
		    Node added;	

		    if( currentTime + next.getDuration() < next.geteStart()) {
			//currentTime + next.getDuration() > next.getlStart()) { todo
			continue; //asap
		    }
		    
		    //muzu ji pridat ted hned, pokud je jeji predek driv
		    //for(int startTime : node.getBeginings()) {
		    for(int startTime = lastActivityStart; startTime < currentTime; startTime++) {
			if(startTime < next.geteStart() || startTime > next.getlStart()) {
			    continue;
			}
			
			boolean add = true;
			//kontrola predku
			for(Activity test : next.getPrev()) { //to do - prev only ID
			    if(node.getActivityEnd(test) > startTime) {
				add = false;
				break;
			    }
			}
			if(add) {
			    //kontrola zda neprekracuje limit
			    if(startTime + next.getDuration() < currentBest && 
				    checkPartialSchedule(node, next, startTime)) {
				added = new Node(node, next, startTime);
				node.addChild(added);
				search(added, startTime);
			    }
			}
		    }

		    //a nebo po ni, pokud je to syn libovolneho v rozvrhu
		    if(currentTime + next.getDuration() < currentBest) {
			if(checkPartialSchedule(node, next, currentTime)) {
			    added = new Node(node, next, currentTime);
			    node.addChild(added);
			    search(added, currentTime);
			}
		    }
		}
	    }
	}
	
//	for(Node n : node.getChildren()) {
//	    search(n);
//	}
    }
    
    public boolean checkPartialSchedule(Node node, Activity last, int lastActivityStartTime) {

	//vymazani starych hodnot 
	for(int i =0; i < time.length; i++) {
	    for(int j = 0; j < resourcesLimit.length; j++) {
		time[i][j] = last.getResources()[j];
	    }
	}
	
	final int resourcesSize = last.getResources().length;
	
	//for(Activity a : node.getActivities().values()) {
	for (int next = node.getActivities().nextSetBit(0); next >= 0; next = node.getActivities().nextSetBit(next+1)) {
	    Activity a = Main.activityList[next-1];
	    
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
