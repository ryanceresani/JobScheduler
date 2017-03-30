package scheduler;

import java.util.ArrayDeque;

public class _phantomJobSender {
	ArrayDeque<PCB> jobRequest;
	private int nextJobTime;
	
	public _phantomJobSender(){
		populateRequests();
	}
	
	private void populateRequests() {
		// TODO Auto-generated method stub
		
	}

	public int getNextJobTime() {
		return nextJobTime;
	}
}
