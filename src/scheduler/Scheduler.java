package scheduler;

import java.util.PriorityQueue;

public class Scheduler {
	
	int systemTime;
	PriorityQueue<PCB> jobQueue;
	
	public Scheduler(Algo algo){
		this.systemTime = 0;
		
	}
	
	
	
	public enum Algo {
		FCFS, SJN, SRT, RR
	}
}
