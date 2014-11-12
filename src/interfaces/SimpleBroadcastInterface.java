/* 
 * Copyright 2010 Aalto University, ComNet
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package interfaces;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Random;
import core.CBRConnection;
import core.Connection;
import core.DTNHost;
import core.NetworkInterface;
import core.Settings;
import core.SimClock;

/**
 * A simple Network Interface that provides a constant bit-rate service, where
 * one transmission can be on at a time.
 */
public class SimpleBroadcastInterface extends NetworkInterface {
	/**
	 * Reads the interface settings from the Settings file
	 *  
	 */
	public SimpleBroadcastInterface(Settings s)	{
		super(s);
	}
		
	/**
	 * Copy constructor
	 * @param ni the copied network interface object
	 */
	public SimpleBroadcastInterface(SimpleBroadcastInterface ni) {
		super(ni);
	}

	public NetworkInterface replicate()	{
		return new SimpleBroadcastInterface(this);
	}
	
	//Rachit
	public double getTransmitSpeedMultiplyingFactorInRainyCondition() {
		Random randomNumberGenerator = new Random();
		int number = randomNumberGenerator.nextInt(1);
		// we have 50% probability of rain thats, why generation random number from 0...1
		// the transmission speed decreases to 30%of original value in case it rains.
		if(number == 0)
			return 0.3;
		return 1.0;
	}

	/**
	 * Tries to connect this host to another host. The other host must be
	 * active and within range of this host for the connection to succeed. 
	 * @param anotherInterface The interface to connect to
	 */
	public void connect(NetworkInterface anotherInterface) {

		if (isScanning()  
				&& anotherInterface.getHost().isActive() 
				&& isWithinRange(anotherInterface) 
				&& !isConnected(anotherInterface)
				&& (this != anotherInterface)) {
			// new contact within range
			// connection speed is the lower one of the two speeds 
			// RACHIT :
			if(!this.host.isConnected() && !anotherInterface.getHost().isConnected()) {
				if(anotherInterface.getHost().getName().startsWith("PBS") && !this.host.getName().startsWith("PBS")) {
					this.host.setConnectdToPBS(SimClock.getTime());
					this.host.setBreakdownState();
				}
				else if(!anotherInterface.getHost().getName().startsWith("PBS") && this.host.getName().startsWith("PBS")) {
					anotherInterface.getHost().setConnectdToPBS(SimClock.getTime());
					anotherInterface.getHost().setBreakdownState();
				}
				
			}
			int conSpeed = anotherInterface.getTransmitSpeed();
			if (conSpeed > this.transmitSpeed) {
				conSpeed = this.transmitSpeed;
			}
			
			conSpeed = (int)(conSpeed*getTransmitSpeedMultiplyingFactorInRainyCondition());
			
			
			
			//Rachit
			if(this.host.isConnectionBroken() || anotherInterface.getHost().isConnectionBroken()) {
				return;
			}
			Connection con = new CBRConnection(this.host, this, 
					anotherInterface.getHost(), anotherInterface, conSpeed);
			
			connect(con,anotherInterface);
			
			//Write New Connection in file here
			/*
			DTNHost To = con.ToNode();
			DTNHost From = con.FromNode();

			try {
				FileWriter fw = new FileWriter("C:\\connection.txt", true);
				fw.write(Integer.toString(To.getAddress()) + " " + Integer.toString(From.getAddress()) + " " + SimClock.getTime() + " Up\r\n");
	            fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
		}
	}

	/**
	 * Updates the state of current connections (ie tears down connections
	 * that are out of range).
	 */
	public void update() {
		// First break the old ones
		optimizer.updateLocation(this);
		for (int i=0; i<this.connections.size(); ) {
			Connection con = this.connections.get(i);
			NetworkInterface anotherInterface = con.getOtherInterface(this);

			// all connections should be up at this stage
			assert con.isUp() : "Connection " + con + " was down!";

			if (!isWithinRange(anotherInterface)) {
				
				//Write Connection Down into file here
				/*
				DTNHost To = con.ToNode();
				DTNHost From = con.FromNode();
				try {
					FileWriter fw = new FileWriter("C:\\connection.txt", true);
					fw.write(Integer.toString(To.getAddress()) + " " + Integer.toString(From.getAddress()) + " " + SimClock.getTime() + " Down\r\n");
		            fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				*/
				disconnect(con,anotherInterface);
				connections.remove(i);
			}
			else {
				i++;
			}
		}
		// Then find new possible connections
		Collection<NetworkInterface> interfaces =
			optimizer.getNearInterfaces(this);
		for (NetworkInterface i : interfaces) {
			connect(i);
		}
	}

	/** 
	 * Creates a connection to another host. This method does not do any checks
	 * on whether the other node is in range or active 
	 * @param anotherInterface The interface to create the connection to
	 */
	public void createConnection(NetworkInterface anotherInterface) {
		if (!isConnected(anotherInterface) && (this != anotherInterface)) {    			
			// connection speed is the lower one of the two speeds 
			int conSpeed = anotherInterface.getTransmitSpeed();
			if (conSpeed > this.transmitSpeed) {
				conSpeed = this.transmitSpeed; 
			}
			
			conSpeed = (int)(conSpeed*getTransmitSpeedMultiplyingFactorInRainyCondition());
			Connection con = new CBRConnection(this.host, this, 
					anotherInterface.getHost(), anotherInterface, conSpeed);
			connect(con,anotherInterface);
		}
	}

	/**
	 * Returns a string representation of the object.
	 * @return a string representation of the object.
	 */
	public String toString() {
		return "SimpleBroadcastInterface " + super.toString();
	}

}
