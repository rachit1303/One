/* 
 * Copyright 2010 Aalto University, ComNet
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Hashtable;

import core.DTNHost;
import core.Message;
import core.MessageListener;

/**
 * Reports delivered messages' delays (one line per delivered message) and
 * cumulative delivery probability sorted by message delays. Ignores the
 * messages that were created during the warm up period.
 */
public class MessageDelayReport extends Report implements MessageListener {
	//public static final String HEADER = "# messageDelay  cumulativeProbability";
	public static final String HEADER = "Message Name	Delay Time(s)	# of Forwards ";
	/** all message delays */
	private LinkedHashMap<String, Double> delaytable;
	private LinkedHashMap<String, List<DTNHost>> hoptable;
	private LinkedHashMap<String, Integer> messagetable;
	private int nrofCreated;
	public int count;
	public double avgDelay;
	public double avgForward;

	/**
	 * Constructor.
	 */
	public MessageDelayReport() {
		init();
	}

	@Override
	public void init() {
		super.init();
		write(HEADER);
		this.delaytable = new LinkedHashMap<String, Double>();
		this.hoptable = new LinkedHashMap<String, List<DTNHost>>();
		this.nrofCreated = 0;
		this.messagetable = new LinkedHashMap<String, Integer>();
		this.avgDelay = 0;
		this.avgForward = 0;
	}

	public void newMessage(Message m) {
		if (isWarmup()) {
			addWarmupID(m.getId());
		} else {
			this.nrofCreated++;
		}
	}

	/*
	 * Original Code public void messageTransferred(Message m, DTNHost from,
	 * DTNHost to, boolean firstDelivery) { if (firstDelivery &&
	 * !isWarmupID(m.getId())) { this.delays.add(getSimTime() -
	 * m.getCreationTime()); }
	 * 
	 * }
	 */

	public void messageTransferred(Message m, DTNHost from, DTNHost to,
			boolean firstDelivery) {

		if (this.messagetable.containsKey(m.getId()) && !isWarmupID(m.getId())) {
			int count = messagetable.get(m.getId());
			count++;
			messagetable.put(m.getId(), count);
		}

		else
			messagetable.put(m.getId(), 1);

		if (firstDelivery && !isWarmupID(m.getId())) {
			double time = getSimTime() - m.getCreationTime();
			delaytable.put(m.getId(), time);
			hoptable.put(m.getId(), m.getHops());
			
			//decode
			int[] messageEncode = m.getEncode();
			int[] messageGVector = m.getGVector();
			
			
			//System.out.println(m.getId());
			
			String[] temp = m.getId().split("_");
			String MessageName = temp[0];
			
//			File dir = new File("C:\\OneSimulator\\" + MessageName); 
//			if (!dir.exists()) dir.mkdirs();
//			String WriteenCode = "C:\\OneSimulator\\" + MessageName + "\\encodeCode.txt";
//			String WriteGVector = "C:\\OneSimulator\\" + MessageName + "\\GVector.txt";
//			try {
//				BufferedWriter bw = new BufferedWriter(new FileWriter(WriteenCode,true));
//				for (int i = 0; i < messageEncode.length; i++)
//				{
//					bw.write(messageEncode[i] + " ");
//				}
//				bw.write("\r\n");
//				bw.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			try {
//				BufferedWriter bw = new BufferedWriter(new FileWriter(WriteGVector,true));
//				for (int i = 0; i < messageGVector.length; i++)
//				{
//					bw.write(messageGVector[i] + " ");
//				}
//				bw.write("\r\n");
//				bw.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} 
			
		}

	}

	@SuppressWarnings("rawtypes")
	@Override
	public void done() {
		if (delaytable.size() == 0) {
			write("# no messages delivered in sim time " + format(getSimTime()));
			super.done();
			return;
		}

		for (Iterator it = delaytable.keySet().iterator(); it.hasNext();) {
			String msgName = (String) it.next();
			Double msgDelay = delaytable.get(msgName);
			if (messagetable.containsKey(msgName)) {
				avgDelay += msgDelay;
				double msgFwd = messagetable.get(msgName);
				avgForward += msgFwd;
				write(msgName + "\t" + msgDelay + "\t" + msgFwd);
				
				List<DTNHost> tmp = hoptable.get(msgName);
				String hops = "";
				for (int i = 0; i != tmp.size(); i++)
				{
					if ( i < tmp.size()-1 )
						hops = hops + tmp.get(i).getName() + " -> ";
					else hops = hops + tmp.get(i).getName();
				}
				write(hops);
				
			}
		}
		avgDelay = avgDelay / delaytable.size();
		avgForward = avgForward / delaytable.size();
		write("average delay : " + avgDelay);
		write("average forward : " + avgForward);
		super.done();
	}

	/*
	 * Original Code
	 * 
	 * @Override public void done() { if (delays.size() == 0) {
	 * write("# no messages delivered in sim time "+format(getSimTime()));
	 * super.done(); return; } double cumProb = 0; // cumulative probability
	 * 
	 * java.util.Collections.sort(delays);
	 * 
	 * for (int i=0; i < delays.size(); i++) { cumProb += 1.0/nrofCreated;
	 * write(format(delays.get(i)) + " " + format(cumProb)); } super.done(); }
	 */
	// nothing to implement for the rest
	public void messageDeleted(Message m, DTNHost where, boolean dropped) {
	}

	public void messageTransferAborted(Message m, DTNHost from, DTNHost to) {
	}

	public void messageTransferStarted(Message m, DTNHost from, DTNHost to) {
	}

}
