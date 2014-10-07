package movement;

import core.SimClock;
import java.util.Random;

// RACHIT
public class HostBreakdown {
	static int[] duration = {1,2,4,8,16,32};
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
	
	// returns true if boats break down else false
	private boolean probabilityOfBreakdown() {
		Random randomNumberGenerator = new Random();
		int number = randomNumberGenerator.nextInt(44);
		// marking '0' as a number which results in breakdown of a host
		// since number can take any value from 0 to 44 (both inclusive) with equal probability
		// hence probability of getting 0 is 1/45
		// use -1 in order to enforce no breakdown
		if(number == 0)
			return true;
		else 
			return false;
	}
	
	// return time duration of breakdown in hours
	// returns a random value from array "duration" if probability of breakdown is true
	private double durationOfBreakdown() {
		if(probabilityOfBreakdown()) {
			Random randomNumberGenerator = new Random();
			int number = randomNumberGenerator.nextInt(5);
			return duration[number];
		}
		return -1;
	}
}
