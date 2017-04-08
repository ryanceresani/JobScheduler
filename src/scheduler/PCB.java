package scheduler;
import java.util.Comparator;

import scheduler.Scheduler.Algo;

public class PCB {

	private String jobName;
	private String status;
	private Timer timer;

	public PCB(String jobName, int arrivalTime, int cpuCycles){
		this.jobName = jobName;
		timer = new Timer(arrivalTime, cpuCycles);
		setStatus("HOLD");
	}

	public void tickJob(int systemTime){
		timer.decrementTimeRemaining();
		if(getTimeRemaining() == 0){
			setStatus("FINISHED");
			timer.setEndTime(systemTime);
			timer.calculateTimes();
		}
	}

	public int getTurnAround() { return timer.turnAround;	}
	public int getJobTime() { return timer.cpuCycles; } 
	public int getWaitingTime() { return timer.waitingTime; }
	public int getArrivalTime(){ return this.timer.arrivalTime; }
	public int getTimeRemaining() { return this.timer.timeRemaining; }
	public int getLastInterrupt() { return this.timer.lastInterrupt; }
	public String getStatus() { return status; }
	public String getJobName() { return jobName; }
	public void setStatus(String status) { this.status = status; }
	public void setInterrupt(int systemTime) { this.timer.lastInterrupt = systemTime; }

	
	static class PCBComparator implements Comparator<PCB>{
		private Algo algo;
		public PCBComparator(Algo algo){
			this.algo = algo;
		}
		@Override
		public int compare(PCB o1, PCB o2) {
			if(algo.equals(Algo.SJN)){
				return Integer.valueOf(o1.getJobTime()).compareTo(Integer.valueOf(o2.getJobTime()));
			}
			else if(algo.equals(Algo.SRT)){
				return Integer.valueOf(o1.getTimeRemaining()).compareTo(Integer.valueOf(o2.getTimeRemaining()));
			}
			else if(algo.equals(Algo.FCFS)){
				return Integer.valueOf(o1.getArrivalTime()).compareTo(Integer.valueOf(o2.getArrivalTime()));
			}
			else {
				return Integer.valueOf(o1.getLastInterrupt()).compareTo(Integer.valueOf(o2.getLastInterrupt()));
			}
		}
	}

	class Timer {

		protected int lastInterrupt;
		protected int arrivalTime;
		protected int cpuCycles;
		protected int timeRemaining;
		protected int endTime;
		protected int waitingTime;
		protected int turnAround;

		protected Timer(int arrivalTime, int cpuCycles) {
			this.arrivalTime = arrivalTime;
			this.cpuCycles = cpuCycles;
			this.timeRemaining = this.cpuCycles;
			this.lastInterrupt = 0;
		}

		public void setEndTime(int systemTime) {
			endTime = systemTime;
		}

		protected void decrementTimeRemaining(){
			timeRemaining--;
		}

		protected void calculateTimes(){
			turnAround = endTime - arrivalTime;
			waitingTime = turnAround - cpuCycles;
		}
	}

}

