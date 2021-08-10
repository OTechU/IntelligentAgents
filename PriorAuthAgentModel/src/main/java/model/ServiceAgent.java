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

@SuppressWarnings("restriction")
public class ServiceAgent extends Agent implements DecisionAgent {
		
	private AID manager, levelofcare, mednec;
	
	private MedicalInfo medicalInfo;
	private Patient patient;
	
	private int demo_step = 0;
	private KieSession kSession;
	private FactHandle agentFH;
	
	public void missingData() {
		String missingData = "";
		
		if (medicalInfo.getCptCode().equals("") || medicalInfo.getCptCode() == null) {
			missingData += "CPT code, ";
		} if (medicalInfo.getIcd10Codes().equals("") || medicalInfo.getIcd10Codes() == null) {
			missingData += "ICD10 codes, ";
		} if (medicalInfo.getDiagnosis().equals("") || medicalInfo.getDiagnosis() == null) {
			missingData += "Diagnosis";
		}
		
		if (missingData.equals("")) {
			// Do nothing
		} else {
			quickMessage(getManager(), this, missingData, "clinical-doc-request");
		}
	}
	
	public AID getManager() {
		return manager;
	}

	public void setManager(AID manager) {
		this.manager = manager;
	}

	public AID getLevelofcare() {
		return levelofcare;
	}

	public void setLevelofcare(AID levelofcare) {
		this.levelofcare = levelofcare;
	}
	
	public AID getMednec() {
		return mednec;
	}

	public void setMednec(AID mednec) {
		this.mednec = mednec;
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
    	this.kSession = kContainer.newKieSession("ksession-service");
    	
    	// Adding agent to controller session
    	DecisionAgent.kSession2.insert(this);
   
    	// Register the eligibility agent in the yellow pages
    	registerAgent(this, getAID(), "service");
    	
    	// Try receiving message
    	addBehaviour(new Messaging(kSession, this));
	}
	
	// Drools calls this when agent receives form piece from manager
	public void parseForm(String str_xml) {
		//Parse string to xml if needed
		//Save data into patient class or other structure
		this.medicalInfo = new MedicalInfo(str_xml);
		this.patient = new Patient(str_xml);

		//Insert this patient, medicalinfo into Drools
		kSession.insert(this.patient);
		kSession.insert(this.medicalInfo);      
		
		//Fire rules to see if more info is needed
		//If good, drools will call other method
		kSession.fireAllRules();
	}
	
	public void parseClinical(String str_xml) {
		//Parse string to xml if needed
		//Save data into patient class or other structure
		this.medicalInfo = new MedicalInfo(str_xml);

		//Insert new medicalinfo into Drools
		kSession.insert(this.medicalInfo);
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
			
			kSession = k;
			setAgentFH(kSession.insert(myAgent));
	    	kSession.fireAllRules();
	    	
	    	//Find manager
	    	while (getManager() == null) {
	    		setManager(findAgent(myAgent, "manager"));
	    	}
	    	
	    	//Find level of care
	    	while (getLevelofcare() == null) {
	    		setLevelofcare(findAgent(myAgent, "levelofcare"));
	    	}
	    	
	    	//Find med nec
	    	while (getMednec() == null) {
	    		setMednec(findAgent(myAgent, "mednec"));
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
