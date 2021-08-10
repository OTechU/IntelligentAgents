package model;


import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import data.MedicalInfo;
import data.Patient;
import data.Physician;
import policies.Policy;

@SuppressWarnings("restriction")
public class EligibilityAgent extends Agent implements DecisionAgent {
		
	private AID manager;
	private Patient patient;
	private MedicalInfo medicalInfo;
	private Policy policy;
	private Physician physician;
	
	private int demo_step = 0;
	private KieSession kSession;
	private FactHandle agentFH;

	public AID getManager() {
		return manager;
	}

	public void setManager(AID manager) {
		this.manager = manager;
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
    	this.kSession = kContainer.newKieSession("ksession-eligibility");
    	
    	// Adding agent to controller session
    	DecisionAgent.kSession2.insert(this);
    	
    	// Register the eligibility agent in the yellow pages
    	registerAgent(this, getAID(), "eligibility");
    	
    	// Try receiving message
    	addBehaviour(new Messaging(kSession, this));
	}
	
	// Drools calls this when agent receives form piece from manager
	public void parseForm(String str_xml) {
		//Save data into patient class or other structure
	    this.patient = new Patient(str_xml);
	    this.medicalInfo = new MedicalInfo(str_xml);
	    this.physician = new Physician(str_xml);
	    this.policy = new Policy();
	    
	    //insert patient, medicalinfo, physician, policy into ksession
        kSession.insert(this.patient);
        kSession.insert(this.medicalInfo);
        kSession.insert(this.physician);
        kSession.insert(this.policy);
        
        //Fire rules to see if more info is needed
      	//If good, drools will call other method
        kSession.fireAllRules();		
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
			
			//get KieSession
			kSession = k;
			setAgentFH(kSession.insert(myAgent));
			kSession.fireAllRules();
			
			//Find manager
			while (getManager() == null) {
	    		setManager(findAgent(myAgent, "manager"));
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
