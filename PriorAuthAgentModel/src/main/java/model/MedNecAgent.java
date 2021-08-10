package model;


import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class MedNecAgent extends Agent implements DecisionAgent {
		
	private AID levelofcare, service;
	private Integer esi_level;
	private String cpt, icd10;
	
	private int demo_step = 0;
	private KieSession kSession;
	private FactHandle agentFH;
	
	public AID getLevelofcare() {
		return levelofcare;
	}

	public void setLevelofcare(AID levelofcare) {
		this.levelofcare = levelofcare;
	} 

	public AID getService() {
		return service;
	}

	public void setService(AID service) {
		this.service = service;
	}

	public Integer getEsi_level() {
		return esi_level;
	}

	public void setEsi_level(Integer esi_level) {
		this.esi_level = esi_level;
	}

	public String getCpt() {
		return cpt;
	}

	public void setCpt(String cpt) {
		this.cpt = cpt;
	}

	public String getIcd10() {
		return icd10;
	}

	public void setIcd10(String icd10) {
		this.icd10 = icd10;
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
	    this.kSession = kContainer.newKieSession("ksession-mednec");
	    
	    // Adding agent to controller session
    	DecisionAgent.kSession2.insert(this);
   	
    	// Register the eligibility agent in the yellow pages
    	registerAgent(this, getAID(), "mednec");
    	
    	// Try receiving message
    	addBehaviour(new Messaging(this.kSession, this));
	}
	
	public void parseContent(String str) {
		this.cpt = str.split("-")[0];
		this.icd10 = str.split("-")[1];
		quickMessage(getLevelofcare(), this, "report esi level", "esi-request");
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
			
			kSession = k;
			setAgentFH(kSession.insert(myAgent));
	    	kSession.fireAllRules();
	    	
	    	//Find level of care
	    	while (getLevelofcare() == null) {
	    		setLevelofcare(findAgent(myAgent, "levelofcare"));
	    	}
	    	
	    	//Find level of care
	    	while (getService() == null) {
	    		setService(findAgent(myAgent, "service"));
	    	}
		}
		
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
