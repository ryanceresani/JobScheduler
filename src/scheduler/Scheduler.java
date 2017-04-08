package scheduler;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

import scheduler.PCB.PCBComparator;


public class Scheduler {

	final static int TIME_QUANTUM = 4;
	int systemTime;
	PriorityQueue<PCB> jobQueue;
	HashMap<String, PCB> finishedJobs;
	PCB currentJob;
	Algo algo;
	Comparator<PCB> comparator;

	public Scheduler(Algo algo){
		this.systemTime = 0;
		this.algo = algo;
		this.currentJob = null;
		this.comparator = new PCBComparator(algo);
		jobQueue = new PriorityQueue<>(comparator);
	}

	public void requestJob(PCB job){
		if(currentJob == null){
			currentJob = job;
			job.setStatus("RUNNING");
		}
		jobQueue.add(job);
		job.setStatus("READY");
	}

	/*
	 * Processor advances one cycle
	 */
	public void tick(){
		//if there is a current job
		if(currentJob != null){
			//Check to see if any job in queue has better priority, switch new job in and put old one back in ready queue
			if(interruptCheck()){
				currentJob.setInterrupt(systemTime);
				currentJob.setStatus("READY");
				jobQueue.add(currentJob);
				currentJob = jobQueue.poll();
			}
			//We process the job for one tick, if it completes we move
			if(currentJob.tickJob(systemTime)){
				finishedJobs.put(currentJob.getJobName(), currentJob);
				currentJob = jobQueue.poll();
			}
		}
		systemTime++;
	}

	//Need to not use for SJN and FCFS
	private boolean interruptCheck() {
		if(comparator.compare(currentJob, jobQueue.peek()) > 0){
			return true;
		}
		return false;
	}

	public enum Algo {
		FCFS, SJN, SRT, RR
	}
}

