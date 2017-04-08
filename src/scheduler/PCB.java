package scheduler;
import java.util.Comparator;

public class PCB {

	private String jobName;
	private String status;
	private Timer timer;
	Comparator<PCB> comparator;

	public PCB(String jobName, int arrivalTime, int cpuCycles){
		this.jobName = jobName;
		timer = new Timer(arrivalTime, cpuCycles);
		setStatus("HOLD");
		
	}

	public void tickJob(int systemTime){
		timer.decrementTimeRemaining(1);
		if(getTimeRemaining() == 0){
			setStatus("FINISHED");
			timer.setEndTime(systemTime);
			timer.calculateTimes();
		}
	}
	
	public int getTurnAround() { return timer.turnAround;	}
	public int getWaitingTime() { return timer.waitingTime; }
	public int getArrivalTime(){ return this.timer.arrivalTime; }
	public int getTimeRemaining() { return this.timer.timeRemaining; }
	public String getStatus() { return status; }
	public String getJobName() { return jobName; }
	public void setStatus(String status) { this.status = status; }
	
	
	
}

class Timer {

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
	}
	
	public void setEndTime(int systemTime) {
		endTime = systemTime;
	}

	protected void decrementTimeRemaining(int time){
		timeRemaining-=time;
	}
	
	protected void calculateTimes(){
		turnAround = endTime - arrivalTime;
		waitingTime = turnAround - cpuCycles;
	}
}