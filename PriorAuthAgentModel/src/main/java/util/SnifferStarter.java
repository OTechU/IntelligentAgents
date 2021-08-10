package util;

import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.SniffOn;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREInitiator;
import model.DecisionAgent;

public class SnifferStarter extends Agent implements DecisionAgent {
	@Override
	protected void setup() {
		getContentManager().registerLanguage(new SLCodec());
		getContentManager().registerOntology(JADEManagementOntology.getInstance());

		addBehaviour(new OneShotBehaviour(this) {
			public void action() {

				//delay to make sure all other agents have started
				//Probably should find a better way to do this
				
				while (findAgent(myAgent, "Facility") == null) {}
				while (findAgent(myAgent, "Manager") == null) {}
				while (findAgent(myAgent, "Eligibility") == null) {}
				while (findAgent(myAgent, "Provider") == null) {}
				while (findAgent(myAgent, "Service") == null) {}
				while (findAgent(myAgent, "LevelOfCare") == null) {}
				while (findAgent(myAgent, "MedNec") == null) {}
				
				try
				{
				    Thread.sleep(500);
				}
				catch(InterruptedException ex)
				{
				    Thread.currentThread().interrupt();
				}
				
			
				// Prepare the request for the sniffer - add all agents in order
				SniffOn snif = new SniffOn();
				AID sniffer = new AID("sniffer", AID.ISLOCALNAME);
				snif.addSniffedAgents(new AID("Facility", AID.ISLOCALNAME));
				snif.addSniffedAgents(new AID("Manager", AID.ISLOCALNAME));
				snif.addSniffedAgents(new AID("Eligibility", AID.ISLOCALNAME));
				snif.addSniffedAgents(new AID("Providers", AID.ISLOCALNAME));
				snif.addSniffedAgents(new AID("Service", AID.ISLOCALNAME));
				snif.addSniffedAgents(new AID("LevelOfCare", AID.ISLOCALNAME));
				snif.addSniffedAgents(new AID("MedNec", AID.ISLOCALNAME));
				snif.setSniffer(sniffer);

				Action action = new Action();
				action.setAction(snif);
				action.setActor(sniffer);

				ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
				request.setOntology(JADEManagementOntology.NAME);
				request.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
				request.addReceiver(sniffer);
				try {
					myAgent.getContentManager().fillContent(request, action);
					addBehaviour(new AchieveREInitiator(myAgent, request) {
						public void handleInform(ACLMessage inform) {
							//System.out.println("INFORM received");
						}
					} );
				}
				catch (Exception e) {
					e.printStackTrace();
				}

			}
		} );
	}
}
