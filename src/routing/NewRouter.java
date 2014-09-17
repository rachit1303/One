
package routing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import routing.maxprop.MaxPropDijkstra;
import routing.maxprop.MeetingProbabilitySet;
import core.DTNHost;
import core.Settings;

/**
 * Epidemic message router with drop-oldest buffer and only single transferring
 * connections at a time.
 */
public class NewRouter extends ActiveRouter {
	
	/** Router's setting namespace ({@value})*/
	public static final String NEWROUTER_NS = "Boat";

	public static final int NumOfGroups = 27;
	
	public static ArrayList<String[]> BoatDP = new ArrayList<String[]>();
	public static ArrayList<String> PBS_Sequence = new ArrayList<String>();
	
	/**
	 * Constructor. Creates a new message router based on the settings in
	 * the given Settings object.
	 * @param s The settings object
	 */
	public NewRouter(Settings s) {
		super(s);
		//TODO: read&use epidemic router specific settings (if any)
		ArrayList<String[]> TEMP = new ArrayList<String[]>();
		for (int i = 1; i <= NumOfGroups; i++) {
			if ( i <= 13 || i>=23)
			{
				Settings s2 = new Settings(NEWROUTER_NS+i);
				String[] PBS = s2.getCsvSetting("DP");
				TEMP.add(PBS);
			}
			else 
			{
				
				String Tmp[] = null;
				TEMP.add(Tmp);
			}
				
			
		}
		
		BoatDP = TEMP;
		
		// If the next DP is nearer Belem
		PBS_Sequence.add("PBS_Belem13");
		PBS_Sequence.add("PBS_SRE20");
		PBS_Sequence.add("PBS_PPD18");
		PBS_Sequence.add("PBS_MNA17");
		PBS_Sequence.add("PBS_SST21");
		PBS_Sequence.add("PBS_Curralinho15");
		PBS_Sequence.add("PBS_Breves14");
		PBS_Sequence.add("PBS_MGC16");
		PBS_Sequence.add("PBS_PRT19");
		PBS_Sequence.add("PBS_RIV23");
	}
	
	/**
	 * Copy constructor.
	 * @param r The router prototype where setting values are copied from
	 */
	protected NewRouter(NewRouter r) {
		super(r);
		//TODO: copy epidemic settings here (if any)
	}
			
	@Override
	public void update() {
		super.update();
		if (isTransferring() || !canStartTransfer()) {
			return; // transferring, don't try other connections yet
		}
		
		// Try first the messages that can be delivered to final recipient
		if (exchangeDeliverableMessages() != null) {
			return; // started a transfer, don't try others (yet)
		}
		
		// then try any/all message to any/all connection
		this.tryAllMessagesToAllConnections_withsameD(BoatDP, PBS_Sequence);

	}
	
	
	@Override
	public MessageRouter replicate() {
		return new NewRouter(this);
	}

}