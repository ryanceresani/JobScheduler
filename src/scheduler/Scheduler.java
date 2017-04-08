package scheduler;
import java.util.Comparator;
import java.util.PriorityQueue;

import scheduler.PCB.PCBComparator;


public class Scheduler {
	
	final static int TIME_QUANTUM = 4;
	int systemTime;
	PriorityQueue<PCB> jobQueue;
	PCB currentJob;
	Algo algo;
	Comparator<PCB> comparator;
			
	public Scheduler(Algo algo){
		this.systemTime = 0;
		this.algo = algo;
		this.comparator = new PCBComparator(algo);
		jobQueue = new PriorityQueue<>(comparator);
	}
	
	public void requestJob(PCB job){
		
	}
	
	public enum Algo {
		FCFS, SJN, SRT, RR
	}
}
