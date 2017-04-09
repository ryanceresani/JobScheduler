package scheduler;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

import javax.swing.plaf.synth.SynthSplitPaneUI;

import scheduler.PCB.PCBComparator;


public class Scheduler {

	final static int TIME_QUANTUM = 4;
	static int systemTime = 0;
	boolean idle;
	PriorityQueue<PCB> jobQueue;
	HashMap<String, PCB> finishedJobs;
	PCB currentJob;
	Algo algo;
	Comparator<PCB> comparator;

	public Scheduler(Algo algo){
		this.algo = algo;
		this.currentJob = null;
		this.comparator = new PCBComparator(algo);
		jobQueue = new PriorityQueue<>(comparator);
		finishedJobs = new HashMap<>();
		idle = true;
	}

	//Allows an outside source to send a PCB to the scheduler
	public void requestJob(PCB job){
		if(currentJob == null){
			currentJob = job;
			job.setStatus("RUNNING");
		}
		jobQueue.add(job);
		job.setStatus("READY");
		idle = false;
	}

	/*
	 * Processor advances one cycle
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
			}
			System.out.println("Ticking " + currentJob.getJobName() + " Time Remaining: " + currentJob.getTimeRemaining());
			//We process the job for one tick, if it completes we move
			if(currentJob.tickJob(systemTime)){
				finishedJobs.put(currentJob.getJobName(), currentJob);
				currentJob = jobQueue.poll();
			}
		}
		else{
			idle = true;
		}
		//System Time is incremented by main method after every tick since system time is static amongst all schedulers
	}

	//Need to not use for SJN and FCFS
	private boolean interruptCheck() {
		//******Cannot run interrupt check for last job since jobQueue is empty therefor null. Need to add TRY CATCH BLOCK
		if(comparator.compare(currentJob, jobQueue.peek()) > 0){
			return true;
		}
		return false;
	}

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

	public static void incrementSystemTime() {
		systemTime++;
	}
}

