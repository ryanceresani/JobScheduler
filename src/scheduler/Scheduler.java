package scheduler;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import scheduler.PCB.PCBComparator;


/**
 * Job Scheduler
 * @author Ryan Ceresani
 *
 */
public class Scheduler {

	final static int TIME_QUANTUM = 4;
	static int systemTime = 0;
	static ArrayList<Scheduler> schedulers = new ArrayList<>();

	boolean idle;
	PriorityQueue<PCB> jobQueue;
	HashMap<String, PCB> finishedJobs;
	PCB currentJob;
	Algo algo;
	Comparator<PCB> comparator;
	int currentSlice = 0;

	/**
	 * Scheduler Constructor
	 * @param algo: Enum that determines the scheduling algorithm used by this instance of scheduler
	 */
	public Scheduler(Algo algo){
		this.algo = algo;
		this.currentJob = null;
		this.comparator = new PCBComparator(algo);
		jobQueue = new PriorityQueue<>(comparator);
		finishedJobs = new HashMap<>();
		idle = true;
	}

	/**
	 * Accepts a job request from an outside source and handles appropriately
	 * @param job
	 */
	public void requestJob(PCB job){
		if(currentJob == null){
			currentJob = job;
			job.setStatus("RUNNING");
		}
		else{
			jobQueue.add(job);
			job.setStatus("READY");
		}
		idle = false;
	}

	/**
	 * Internally advances processor one tick. Checks if a job needs to be interrupted based on algorithm
	 */
	public void tick(){
		//if there is a current job
		if(currentJob != null){
			//Check to see if any job in queue has better priority, switch new job in and put old one back in ready queue
			//Only interrupts if the current algorithm is preemptive
			if(algo.getPreempt() && interruptCheck()){
				currentJob.setInterrupt(systemTime);
				currentJob.setStatus("READY");
				jobQueue.add(currentJob);
				currentJob = jobQueue.poll();
				currentSlice = 0;
			}

			//For Round Robin only, we increment the current time slice by one. 
			currentSlice++;
			//We process the job for one tick, if it completes we move on
			//For consistency, the system time is incremented after the tick for all schedulers
			if(currentJob.tickJob(systemTime+1)){
				finishedJobs.put(currentJob.getJobName(), currentJob);
				currentJob = jobQueue.poll();
				//Job done, reset the slice time to 0
				currentSlice = 0;
			}
		}
		else{
			idle = true;
		}
		//System Time is incremented by main method after every tick since system time is static amongst all schedulers
	}

	/**
	 * Checks if the priority of the current job is higher than the priority of the first job in the queue.
	 * Signals for an interrupt if we need to do a context switch
	 * @return true if an interrupt will occur
	 */
	private boolean interruptCheck() {
		if(!jobQueue.isEmpty()){
			if(algo.equals(Algo.RR) && currentSlice >= TIME_QUANTUM){
				return true;
			}
			else if(comparator.compare(currentJob, jobQueue.peek()) > 0){
				return true;
			}
		}
		return false;
	}

	public boolean isIdle(){
		return idle;
	}

	/**
	 * Calculates and prints the stats for every scheduler that exists
	 * 
	 */
	public void printAverageStats(){
		double avgTurnAround = 0;
		double avgWaitingTime = 0; 
		Iterator<Entry<String, PCB>> it = finishedJobs.entrySet().iterator();
		while(it.hasNext()){
			PCB next = it.next().getValue();
			avgTurnAround += next.getTurnAround();
			avgWaitingTime += next.getWaitingTime();
		}
		avgTurnAround = avgTurnAround / finishedJobs.size();
		avgWaitingTime = avgWaitingTime / finishedJobs.size();
		String leftAlignFormat = "| %-10s | %-14.1f | %-11.1f |%n";
		System.out.format(leftAlignFormat, algo.toString(), avgTurnAround, avgWaitingTime);
		System.out.format("+------------+----------------+-------------+%n");
	}

	/**
	 * Prints out the stats for an individual schedulers finishes jobs
	 */
	public void printIndividualStats(){
		Iterator<Entry<String, PCB>> it = finishedJobs.entrySet().iterator();
		System.out.println();
		System.out.println("+ " + algo.toString() +" Finished PCB Stats");
		System.out.format("+-----------+--------------+------------+----------+-----------------+--------------+%n");
		System.out.format("| Job Name  | Arrival Time | CPU Cycles | End Time | Turnaround Time | Waiting Time |%n");
		System.out.format("+-----------+--------------+------------+----------+-----------------+--------------+%n");
		while(it.hasNext()){
			PCB next = it.next().getValue();
			next.printStats();
		}
	}
	/*
	 * Enum for the algorithm type
	 * determines ordering for Priority Queue and if the algorithm is preemptive 
	 */
	public enum Algo {
		FCFS(false), SJN(false), SRT(true), RR(true);

		private boolean preempt;

		Algo(boolean preempt) {
			this.preempt = preempt;
		}

		public boolean getPreempt() {
			return this.preempt;
		}
	}

	/*
	 * System time is incremented by one for all schedulers at once.
	 */
	public static void incrementSystemTime() {
		systemTime++;
	}


	/**
	 * Check if all schedulers are sitting idle.
	 * @return TRUE if all the schedulers are sitting idle
	 */
	public static boolean allIdle() {
		for (Scheduler scheduler : schedulers) {
			if(!scheduler.isIdle()){
				return false;
			}
		}
		return true;
	}

	/**
	 * Adds a scheduler to the static list of instantiated schedulers
	 * @param an Algo enum that defines the algorithm used for scheduling
	 */
	public static void addScheduler(Algo algo) {
		schedulers.add(new Scheduler(algo));
	}
}

