package movement;

import core.SimClock;

// RACHIT
public class HostBreakdown {
	private double start;
	private double end;
			
	public HostBreakdown() {
		this.start = SimClock.getTime();
		double temp = durationOfBreakdown();
		this.end = start + temp*60.0*60.0;
	}
	
	public void setBreakdownState() {
		if( SimClock.getTime() > this.end) {
			this.start = SimClock.getTime();
			this.end = start + durationOfBreakdown()*60*60;
		}
	}
	
	public boolean isBroken() {
		return isInRange(SimClock.getTime());
	}
	
	private boolean isInRange(double time) {
		if (time >= start && time < end ) {
			return true;
		}
		return false;			
	}
	
	private double probabilityOfBreakdown() {
		return 1;
	}
	
	private double durationOfBreakdown() {
		if(probabilityOfBreakdown() > 0) {
			return 1.0/30.0;
		}
		return 0;
	}
	
}
