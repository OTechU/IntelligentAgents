package model;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


public class ManagerAgent extends Agent implements DecisionAgent {
		
	private AID eligibility, provider, service, facility;
	
	private boolean elig, prov, serv;
	private boolean denied;
	
	private int demo_step = 0;
	private KieSession kSession;
	private FactHandle agentFH;
	
	public AID getEligibility() {
		return eligibility;
	}

	public void setEligibility(AID eligibility) {
		this.eligibility = eligibility;
	}

	public AID getProvider() {
		return provider;
	}

	public void setProvider(AID provider) {
		this.provider = provider;
	}

	public AID getService() {
		return service;
	}

	public void setService(AID service) {
		this.service = service;
	}
	
	public AID getFacility() {
		return facility;
	}

	public void setFacility(AID facility) {
		this.facility = facility;
	}
	
	public boolean isElig() {
		return elig;
	}

	public void setElig(boolean elig) {
		this.elig = elig;
	}

	public boolean isProv() {
		return prov;
	}

	public void setProv(boolean prov) {
		this.prov = prov;
	}

	public boolean isServ() {
		return serv;
	}

	public void setServ(boolean serv) {
		this.serv = serv;
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

	public boolean isDenied() {
		return denied;
	}

	public void setDenied(boolean denied) {
		this.denied = denied;
	}

	public void deny() {
		System.out.println("Prior Authorization Denied. For request #" + "2");
		setElig(false);
		setProv(false);
		setServ(false);
		setDenied(true);
	}

	@SuppressWarnings("restriction")
	private static String nodeListToString(NodeList nodes) throws TransformerException {
	    DOMSource source = new DOMSource();
	    StringWriter writer = new StringWriter();
	    StreamResult result = new StreamResult(writer);
	    Transformer transformer = TransformerFactory.newInstance().newTransformer();
	    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

	    for (int i = 0; i < nodes.getLength(); ++i) {
	        source.setNode(nodes.item(i));
	        transformer.transform(source, result);
	    }

	    return writer.toString();
	}
	
	protected void setup() {
		
		// Start KieSession for drools
		KieServices ks = KieServices.Factory.get();
	    KieContainer kContainer = ks.getKieClasspathContainer();
    	this.kSession = kContainer.newKieSession("ksession-manager");
    	
    	// Adding agent to controller session
		DecisionAgent.kSession2.insert(this);
   	
    	// Register the manager in the yellow pages
		registerAgent(this, getAID(), "manager");
    	
    	// Try receiving message
    	addBehaviour(new Messaging(this.kSession, this));
	}
	
	// Drools calls this when facility sends form
	@SuppressWarnings("restriction")
	public void breakdownForm(String str_xml) {
		// Break apart form
		// Send parts to approriate agents
		// i.e. patient_info xml send to Elig. agent as string in ACL Message
		try {	  
	         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	         InputSource inputData = new InputSource();
	         inputData.setCharacterStream(new StringReader(str_xml));
	         Document doc = dBuilder.parse(inputData);
	         doc.getDocumentElement().normalize();
	         
	         NodeList patientInfo = doc.getElementsByTagName("patient_info");
	         NodeList physicianInfo = doc.getElementsByTagName("physician_info");
	         NodeList medicalInfo = doc.getElementsByTagName("medical_info");
	         NodeList insuranceInfo = doc.getElementsByTagName("insurance_info");

	 		 // This function will send 3 ACLMessage total
	         String eligibilityInfoMessage = "<info>\n" 
	        		 + nodeListToString(patientInfo) + "\n"
	        		 + nodeListToString(medicalInfo) + "\n"
	        		 + nodeListToString(physicianInfo) + "\n"
	        		 + "</info>";
	         String providerInfoMessage = "<info>\n" 
	        		 + nodeListToString(physicianInfo) + "\n"
	        		 + nodeListToString(medicalInfo) + "\n"
	        		 + "</info>";
	         String serviceInfoMessage = "<info>\n" 
	        		 + nodeListToString(patientInfo) + "\n"
	        		 + nodeListToString(medicalInfo) + "\n"
	        		 + "</info>";
	         
	         
	         quickMessage(getEligibility(),this,eligibilityInfoMessage,"initial-info");
	         quickMessage(getProvider(),this,providerInfoMessage,"initial-info");
	         quickMessage(getService(),this,serviceInfoMessage,"initial-info");
	         
	         
		 } catch (Exception e) {
	         e.printStackTrace();
	     }
		
	}
	
	@SuppressWarnings("restriction")
	public void breakdownClinicalDoc(String str_xml) {
		// Break apart form
		// Send parts to approriate agents
		// i.e. patient_info xml send to Elig. agent as string in ACL Message
		try {	  
	         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	         InputSource inputData = new InputSource();
	         inputData.setCharacterStream(new StringReader(str_xml));
	         Document doc = dBuilder.parse(inputData);
	         doc.getDocumentElement().normalize();
	         
	         NodeList medicalInfo = doc.getElementsByTagName("medical_info");

	         String serviceInfoMessage = nodeListToString(medicalInfo);
	        
	         quickMessage(getService(),this,serviceInfoMessage,"clinical-report");
	         
	         
		 } catch (Exception e) {
	         e.printStackTrace();
	     }
		
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
	    	
	    	// Find eligibility
	    	while (getEligibility() == null) {
	    		setEligibility(findAgent(myAgent, "eligibility"));
	    	}
	    	
	    	// Find provider
	    	while (getProvider() == null) {
	    		setProvider(findAgent(myAgent, "provider"));
	    	}
	    	
	    	// Find service
	    	while (getService() == null) {
	    		setService(findAgent(myAgent, "service"));
	    	}
	    	
	    	// Find facility
	    	while (getFacility() == null) {
	    		setFacility(findAgent(myAgent, "facility"));
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
