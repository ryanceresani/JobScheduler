package scheduler;
import java.util.Comparator;

import scheduler.Scheduler.Algo;

public class PCB {

	private String jobName;
	private String status;
	private Timer timer;

	/**
	 * PCB Constructor
	 * @param jobName
	 * @param arrivalTime
	 * @param cpuCycles
	 */
	public PCB(String jobName, int arrivalTime, int cpuCycles){
		this.jobName = jobName;
		timer = new Timer(arrivalTime, cpuCycles);
		setStatus("HOLD");
	}
	
	/**
	 * For cloning a copy of a PCB
	 * @param PCB clone
	 */
	public PCB(PCB clone){
		this.jobName = clone.jobName;
		this.timer = new Timer(clone.getArrivalTime(), clone.getJobTime());
		setStatus(clone.getStatus());
	}

	/**
	 * Ticks the job by one time increment and checks for if it finishes
	 * @param systemTime
	 * @return TRUE if the job finishes after this tick, FALSE if it has time remaining
	 */
	public boolean tickJob(int systemTime){
		timer.decrementTimeRemaining();
		if(getTimeRemaining() <= 0){
			setStatus("FINISHED");
			timer.setEndTime(systemTime);
			timer.calculateTimes();
			return true;
		}
		return false;
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

	@Override
	public String toString() {
		return this.jobName + " " + this.getArrivalTime() + " " + this.getJobTime() + " TR:" +this.getTimeRemaining();
	}
	

	public void printStats() {
		String leftAlignFormat = "| %-9s | %-12s | %-10s | %-8s | %-15s | %-12s | %n";
		System.out.format(leftAlignFormat, jobName, timer.arrivalTime, timer.cpuCycles, timer.endTime, timer.turnAround, timer.waitingTime);
		System.out.format("+-----------+--------------+------------+----------+-----------------+--------------+%n");
		
	}
	
	/**
	 * @author ceresanr
	 * Custom comparator to enable PCB to exist in a priority queue
	 */
	static class PCBComparator implements Comparator<PCB>{
		private Algo algo;
		public PCBComparator(Algo algo){
			this.algo = algo;
		}
		@Override
		public int compare(PCB o1, PCB o2) {
			if(algo.equals(Algo.SJN)){
				return Integer.compare(o1.getJobTime(), o2.getJobTime());
			}
			else if(algo.equals(Algo.SRT)){
				return Integer.compare(o1.getTimeRemaining(), o2.getTimeRemaining());
			}
			else if(algo.equals(Algo.FCFS)){
				return Integer.compare(o1.getArrivalTime(), o2.getArrivalTime());
			}
			else {
				return Integer.compare(o1.getLastInterrupt(), o2.getLastInterrupt());
			}
		}
		
	}

	/**
	 * @author ceresanr
	 * Internal accounting of all the timing for each PCB
	 */
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
			this.lastInterrupt = arrivalTime;
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

