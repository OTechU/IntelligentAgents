package model;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class LevelOfCareAgent extends Agent implements DecisionAgent {
	
	private AID service, mednec;
	
	private String icd10 = "";
	private boolean hospitalized;
	private Integer esi_level;
	
	private int demo_step = 0;
	private KieSession kSession;
	private FactHandle agentFH;
	
	public AID getService() {
		return service;
	}

	public void setService(AID service) {
		this.service = service;
	}

	public AID getMednec() {
		return mednec;
	}

	public void setMednec(AID mednec) {
		this.mednec = mednec;
	}

	public String getIcd10() {
		return icd10;
	}

	public void setIcd10(String icd10) {
		this.icd10 = icd10;
	}

	public Integer getEsi_level() {
		return esi_level;
	}

	public void setEsi_level(Integer esi_level) {
		this.esi_level = esi_level;
	}

	public boolean isHospitalized() {
		return hospitalized;
	}

	public void setHospitalized(boolean hospitalized) {
		this.hospitalized = hospitalized;
	}

	public int getDemo_step() {
		return demo_step;
	}

	public void setDemo_step(int demo_step) {
		this.demo_step = demo_step;
	}

	public FactHandle getAgentFH() {
		return agentFH;
	}

	public void setAgentFH(FactHandle agentFH) {
		this.agentFH = agentFH;
	}

	protected void setup() {
		
		KieServices ks = KieServices.Factory.get();
	    KieContainer kContainer = ks.getKieClasspathContainer();
	    this.kSession = kContainer.newKieSession("ksession-levelofcare");
	    
	    // Adding agent to controller session
	    DecisionAgent.kSession2.insert(this);
   	
    	// Register the eligibility agent in the yellow pages
    	registerAgent(this, getAID(), "levelofcare");
    	
    	// Try receiving message
    	addBehaviour(new Messaging(this.kSession, this));
	}
	
	public void parseContent(String str) {
		this.icd10 = str.split("-")[1];
		this.hospitalized = str.split("-")[2].equals("true");
	}
	
	public void nextStep() {
		demo_step++;
		this.kSession.update(agentFH, this);
		this.kSession.fireAllRules();
	}
	
	private class Messaging extends CyclicBehaviour {
		
		private KieSession kSession;
		
		public Messaging(KieSession k, Agent a) {
			super(a);
			
			// Get KieSession
			kSession = k;
			setAgentFH(kSession.insert(myAgent));
			kSession.fireAllRules();
			
			//Find service
			while (getService() == null) {
	    		setService(findAgent(myAgent, "service"));
	    	}
	    	
	    	//Find med nec
	    	while (getMednec() == null) {
	    		setMednec(findAgent(myAgent, "mednec"));
	    	}			
		}
		
		// Cycles forever
		public void action() {
	    	
			// Wait for message
	    	ACLMessage msg = myAgent.blockingReceive();
	    	if (msg != null) {
				kSession.insert(msg);
				kSession.fireAllRules();
			}
			else {
				block();
			}
	    	
	    	
		}
	}

}
