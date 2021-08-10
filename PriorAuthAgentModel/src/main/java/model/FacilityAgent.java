package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.kie.api.runtime.rule.FactHandle;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import util.FacilityGui;

public class FacilityAgent extends Agent implements DecisionAgent {
		
	private AID manager;
	
	private FactHandle agentFH;
	private int demo_step;

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


	private FacilityGui myGui;
	
	
	protected void setup() {

		
		// Adding agent to controller session
		this.agentFH = DecisionAgent.kSession2.insert(this);
		
		// Create and show the GUI 
		myGui = new FacilityGui(this);
		myGui.showGui();
    	
    	// Register the eligibility agent in the yellow pages
    	registerAgent(this, getAID(), "facility");
    	
    	while (getManager() == null) {
    		setManager(findAgent(this, "manager"));
    	}
    	
    	addBehaviour(new RequestHandler());
	}
	
	protected void takeDown() {
		myGui.dispose();
	}
	
	public void stepDemo() {
		demo_step++;
		DecisionAgent.kSession2.update(agentFH, this);
		DecisionAgent.kSession2.fireAllRules();
	}
	
	public void resetDemo() {
		demo_step = 0;
		DecisionAgent.kSession2.update(agentFH, this);
		DecisionAgent.kSession2.fireAllRules();
	}
	
	
	public String readFile(String string) throws IOException {
	  File f = new File(string);
  	  Reader fileReader = new FileReader(f); 
  	  BufferedReader bufReader = new BufferedReader(fileReader); 
  	  StringBuilder sb = new StringBuilder(); 
  	  String line = bufReader.readLine(); 
  	  while( line != null){ 
  		  sb.append(line).append("\n"); 
  		  line = bufReader.readLine(); 
  		  } 
  	  String inputString = sb.toString(); 
  	  bufReader.close();
  	  return inputString;
	}
	
	public void startDemo(String file) {
		resetDemo();
		addBehaviour(new OneShotBehaviour() {
			public void action() {
				System.out.println("Start demo");
				ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
				msg.addReceiver(getManager());
				
				//Content will be string xml of pa_form
				try {
					String inputString = readFile(file);
					msg.setContent(inputString);
				} catch (IOException e) {
					e.printStackTrace();
				}
				msg.setConversationId("facility-pa-request");
				myAgent.send(msg);
			}
		} );
	}
	
	private class RequestHandler extends CyclicBehaviour {

		public void action() {
			
			// Receive message from manager
			ACLMessage msg = myAgent.receive();
			if (msg != null && msg.getConversationId().equals("clinical-doc-request")) {
				System.out.println("Clinical doc request completed");
				ACLMessage rpl = msg.createReply();
				try {
                    String inputString = readFile("src/main/resources/forms/PAform.xml");
                    rpl.setContent(inputString);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                rpl.setConversationId("clinical-doc-recieved");
                myAgent.send(rpl);
			}
				     
		}

	}
	
	
}
