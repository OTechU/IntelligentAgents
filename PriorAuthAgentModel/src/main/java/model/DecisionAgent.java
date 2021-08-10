package model;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public interface DecisionAgent {
	
	public static final KieServices ks2 = KieServices.Factory.get();
	public static final KieContainer kContainer2 = ks2.getKieClasspathContainer();
	public static final KieSession kSession2 = kContainer2.newKieSession("ksession-controller");
	
	default void registerAgent(Agent agent, AID name, String service) {
		
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(name);
		ServiceDescription sd = new ServiceDescription();
		sd.setType(service);
		sd.setName("prior-auth");
		dfd.addServices(sd);
		try {
			DFService.register(agent, dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}
	
	default AID findAgent(Agent agent, String target) {
		
		AID aid = null;
		
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(target);
		template.addServices(sd);
		
		try {
			DFAgentDescription[] result = DFService.search(agent, template); 
			if (result.length > 0) {
				aid = result[0].getName();
			}
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		
		return aid;
	}
	
	default void quickMessage(AID to, Agent from, String content, String conversation) {
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.addReceiver(to);
		msg.setContent(content);
		msg.setConversationId(conversation);
		from.send(msg);
	}
	
}
