/* 
 * Copyright 2010 Aalto University, ComNet
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package input;

import core.DTNHost;
import core.Message;
import core.World;


/**
 * External event for creating a message.
 */
public class MessageCreateEvent extends MessageEvent {
	private int size;
	private int responseSize;
	private int generateNumN = 161;
	
	/**
	 * Creates a message creation event with a optional response request
	 * @param from The creator of the message
	 * @param to Where the message is destined to
	 * @param id ID of the message
	 * @param size Size of the message
	 * @param responseSize Size of the requested response message or 0 if
	 * no response is requested
	 * @param time Time, when the message is created
	 */
	public MessageCreateEvent(int from, int to, String id, int size,
			int responseSize, double time) {
		
		super(from,to, id, time);
		this.size = size;
		this.responseSize = responseSize;
	}
	
	/**
	 * Creates the message this event represents. 
	 */
	@Override
	public void processEvent(World world) {
		//Messages are always trasfered to Belem, PBS ID = 13
		//DTNHost to = world.getNodeByAddress(this.toAddr);
		DTNHost to = world.getNodeByAddress(13);
		
		//from address are continuous from 14
		//DTNHost from = world.getNodeByAddress(this.fromAddr);		
		int tempfrom = Integer.parseInt(this.id.substring(1)) % 8;
		DTNHost from = world.getNodeByAddress(tempfrom + 14);
		
		for ( int i = 0; i != this.generateNumN; i++)
		{
			String Newid = this.id + "_" + Integer.toString(i);
			Message m = new Message(from, to, Newid, this.size);
			
			m.setResponseSize(this.responseSize);
			from.createNewMessage(m);
		}
	}	
	
	@Override
	public String toString() {
		return super.toString() + " [" + fromAddr + "->" + toAddr + "] " +
		"size:" + size + " CREATE";
	}
}
