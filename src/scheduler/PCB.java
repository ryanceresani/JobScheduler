package scheduler;

public class PCB {

	private String jobName;
	private int arrivalTime;
	private int timeRemaining;
	private String status;
	
	public PCB(String jobName, int arrivalTime, int timeRemaining){
		this.jobName = jobName;
		this.arrivalTime = arrivalTime;
		this.timeRemaining = timeRemaining;
		setStatus("HOLD");
	}

	
	public double getTurnAround(int systemTime) {	
		return systemTime - arrivalTime;
	}

	
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getStatus() { return status; }
	public String getJobName() { return jobName; }
	
}
