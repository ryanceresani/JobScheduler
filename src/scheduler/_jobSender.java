package scheduler;

import java.util.ArrayDeque;

public class _jobSender {
	ArrayDeque<PCB> jobRequest;
	private int nextJobTime;
	
	public _jobSender(){
		populateRequests();
	}
	
	private void populateRequests() {
		// TODO Auto-generated method stub
		
	}

	public int getNextJobTime() {
		return nextJobTime;
	}
}
