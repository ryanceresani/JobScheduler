package scheduler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;

import scheduler.PCB.PCBComparator;
import scheduler.Scheduler.Algo;

/**
 * @author Ryan Ceresani
 * 
 * Tester class to provide job requests to a scheduler class.
 * Generates a job request queue that is sent to the Scheduler at the appropriate arrival times for that job
 */
public class _jobSender {
	PriorityQueue<PCB> jobRequest;

	public static void main(String[] args) throws IOException{
		_jobSender js = new _jobSender();
		int nextArrival = js.jobRequest.peek().getArrivalTime();
		Scheduler.schedulers.add(new Scheduler(Algo.FCFS));
		Scheduler.schedulers.add(new Scheduler(Algo.SJN));
		Scheduler.schedulers.add(new Scheduler(Algo.SRT));
		Scheduler.schedulers.add(new Scheduler(Algo.RR));
		
		while(!js.jobRequest.isEmpty() || !Scheduler.allIdle()){
			try {
				nextArrival = js.jobRequest.peek().getArrivalTime();
			} catch (Exception e) {
				nextArrival = -1;
			}
			if(Scheduler.systemTime == nextArrival){
				PCB newJob = js.jobRequest.poll();
				//Copy details of the newJob so it can be tested in 4 schedulers simultaneously
				int newJobArrival = newJob.getArrivalTime();
				int newJobCPU = newJob.getJobTime();
				String newJobName = newJob.getJobName();
				
				for (Scheduler scheduler : Scheduler.schedulers) {
					scheduler.requestJob(new PCB(newJobName, newJobArrival, newJobCPU));
				}
			}
			for (Scheduler scheduler : Scheduler.schedulers) {
				scheduler.tick();
			}
			Scheduler.incrementSystemTime();
		}
		printResults();
	}


	public _jobSender() throws IOException{
		Comparator<PCB> comp = new PCBComparator(Algo.FCFS);
		jobRequest = new PriorityQueue<PCB>(comp);
		populateRequests();
	}

	/**
	 * Reads a sequence of simulated job requests from a file
	 * @throws IOException
	 */
	private void populateRequests() throws IOException {
		String[] tokens = new String[3];
		BufferedReader in = new BufferedReader(new FileReader("JobInfo"));
		String line;
		PCB newPCB = null;
		while ((line = in.readLine()) != null) {
			tokens = line.split(" ");
			newPCB = new PCB(tokens[0], Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
			if(!newPCB.equals(null)){
				jobRequest.add(newPCB);
			}
		}	
		in.close();
	}
	
	public static void printResults(){
		for (Scheduler s : Scheduler.schedulers) {
			s.printIndividualStats();
		}
		System.out.println();
		System.out.println("+Average Times Per Scheduling Algorithm");
		System.out.format("+------------+----------------+-------------+%n");
		System.out.format("| Algorithm  | Avg Turnaround | Avg Waiting |%n");
		System.out.format("+------------+----------------+-------------+%n");
		for (Scheduler s : Scheduler.schedulers) {
			s.printAverageStats();
		}

	}
}
