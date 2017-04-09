package scheduler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;

import scheduler.Scheduler.Algo;

public class _jobSender {
	ArrayDeque<PCB> jobRequest;

	
	public static void main(String[] args) throws IOException{
		int nextArrival = -1;
		_jobSender js = new _jobSender();
		Scheduler srt = new Scheduler(Algo.SRT);
		while(!js.jobRequest.isEmpty() || !srt.idle){
			try {
				nextArrival = js.jobRequest.peek().getArrivalTime();
			} catch (Exception e) {
				nextArrival = -1;
			}
			if(Scheduler.systemTime == nextArrival){
				PCB newJob = js.jobRequest.poll();
				srt.requestJob(newJob);
			}
			srt.tick();
			Scheduler.incrementSystemTime();
			System.out.println(Scheduler.systemTime);
		}
		System.out.println(srt.finishedJobs.toString());
	}
	
	public _jobSender() throws IOException{	
		jobRequest = new ArrayDeque<PCB>();
		populateRequests();
	}
	
	private void populateRequests() throws IOException {
		String[] tokens = new String[3];
		BufferedReader in = new BufferedReader(new FileReader("JobInfo"));
		String line;
		PCB newPCB = null;
		while ((line = in.readLine()) != null) {
			  tokens = line.split(" ");
			  newPCB = new PCB(tokens[0], Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
			  System.out.println(newPCB.toString());
			  jobRequest.add(newPCB);
			}	
		in.close();
	}
	
}
