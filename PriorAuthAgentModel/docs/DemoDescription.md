# Applying a Multi-Agent Framework to Prior Auth. 
The Prior Authorization process is best modeled with a multi-agent framework that can mimic the conversations between multiple working agents involved in isolated segments of the PA process. 

The following demos use XML files and Java objects to mimic clinical data being transmitted across the network to various actors in the Prior Auth. process.

The following demos are used to showcase the *need* for multiple agents and the importance of multi-threaded processing of different points in the Prior Auth. process. The benefits of the multi-agent framework are shown by providing three different, potential cases for information in the Prior Auth. process. They represent a successful run, an error-prone run, and a run which can be fixed by providing more data. Together, they show the power and capabilities of the multi-agent framework.

## Table of Contents
- [Prior Auth. Agent Descriptions](#agents)
- [Simple Multi-Agent Showcase](#demo1)
- [Invalid Information Showcase](#demo2)
- [Incomplete Information Showcase](#demo3)

---

<a id="agents"></a>
## Prior Auth Agents 
The following are descriptions of the various agents involved in the Prior Auth. process. Each agent has unique goals, behaviors, and capabilities. Organized together in this multi-agent system, they can take advantage of each others' abilities.

### Facility
The facility agent acts as an end point for our model. It represents the medical facility that will communicate with our system. The facility streams data into the model through XML files that mimic actual PA request forms. The only agent that communicates with the facility is the Manager Agent since the Manager Agent is tasked with handling the distribution of information to other agents in the model.

### Manager
The goal of the Manager Agent is to communicate with the facility regarding the PA request and to distribute appropriate information to the worker agents. The Manager is equipped with the ability to parse information and a rule set to determine what information needs to be passed on for eligibility, providers, and service decisions. Additionally, the Manager checks the results from those three decisions to determine if the PA request is approved, denied, or requires more information from the facility. 

### Eligibility
The goal of the Eligibility Agent is to ensure that a patient is eligible for coverage of a treatment at a specific facility. This requires data from the patient's healthcare policy, namely what services are covered and which medical facilities are in network. Additionally, this agent must know the treatment being requested as well as the purpose of the treatment. Policy information is pulled directly from a mock policy represented as a data object for the purposes of this demo. Information regarding the treatment and purpose will be gained from the PA form which is sent to the Eligibility Agent by the Manager Agent. Once the Eligibility Agent has come to a decision on the eligibility of treatment for the patient, its decision is sent back to the Manager Agent.

### Providers
The goal of the Providers Agent is to ensure that the healthcare provider is covered as in network for the patient's specific treatment at the expert physician's hospital. This requires data from the patient's healthcare policy, the physician's information along with their hospital information, and the patient's medical history. The patient's healthcare policy is pulled from a sample policy represented as a Java class (Policy.java). The physician, hospital, and patient's medical information is received from the PA form sent from the Manager. Using the information, the Providers Agent will determine whether the provider is covered for a specific treatment at the physician's hospital. This agent will send its decision on the providers' coverage back to the manager agent.

### Service
The goal of the Service Agent is to determine which requested service(s) are approved. This decision requires information on patient medical history and the services being requested. This information all comes in from the facility. It is the responsibility of this agent to make sure the facility has supplied enough information. In some cases, additional clinical documents need to be requested from the facility in order to gather more case information on patient history, patient condition, and other data that is needed to determine if the services are appropriate. The final decision of the Service Agent take into account the decisions of the Level of Care Agent and Medical Necessity Agent. Once a final decision has been compiled, the Service Agent send it back to the Manager Agent.

### Level of Care
The goal of the Level of Care agent is to determine the condition of the patient. The patient condition is quantified through the Emergency Severity Index (ESI) Level. ESI is a five-level scale based on the severity of the patient's condition and the amount of resources needed to stabilize the patient. The Level of Care agent determines this level based on a set of facts including the patient's diagnosis and if the patient is hospitalized. This is a simplification of all the factors that truly determine the ESI level. Once determined, the ESI level is sent to the Medical Necessity Agent to determine if the treatment is appropriate.

### Medical Necessity (Med Nec)
The goal of the Med Nec Agent is to come to a Y/N conclusion on whether or not the service(s) requested is medically appropriate. This agent needs service and diagnosis codes from the Service Agent along with the calculated ESI from the Level of Care Agent. This agent does some basic reasoning to determine if the service is a valid treatment for the diagnosis. Additionally, the agent take the ESI level into account for determining medical necessity. Once a decision has been reached, it is sent back to the Service Agent to be compiled into the final service decision.

---

## Demos

<a id="demo1"></a>
### Simple Multi-Agent Showcase
This demo case is meant to show a very simple example of a treatment gaining prior authorization approval from the multi-agent system. In this example, a patient with a standard healthcare plan requests approval for an IGIV (immune globulin intravenous) injection with the purpose of preventative healthcare. Preventative care is covered under the patient's healthcare policy and the physician administering the injection is in network. Additionally, the patient experiences Primary Immunodeficiency, which is a condition that can typically be treated with IGIV injections. All of these facts lead the agents to approve of this request. 

By showing a successful demonstration of a complete PA case, we can highlight the flow and transmission of information across agents in the system. 

<a id="demo2"></a>
### Invalid Information Showcase
This demo case is meant to show an example of a treatment being denied by the multi-agent system. Like the previous example, there is a request for an IGIV injection for a patient. However, much of the case information is different. The case is requesting an out of network provider, which happens to not be approved for this treatment. Additionally, the patient's condition isn't as severe as the first demo case since the patient only has proof of being diagnosed with Type 2 Diabetes. This set of facts leads all the agents based on their reasoning capabilities to deny the request. 

By providing a case where the agents deny the prior authorization request, we can showcase what the result would be and how to avoid falling into such pitfalls. This will showcase how the communication between several intelligent agents can find faults in the data payload.

<a id="demo3"></a>
### Incomplete Information Showcase
This final demo case is a more complex scenario where the agents need more information from the facility to come to their decisions. This case is very common is actuality as many prior authorization cases require one or more clinical doc requests. In this demo scenario, the same request is given to the agents as the Simple Multi-Agent Showcase demo, but a clinical doc request is necessary to determine to patient experiences Primary Immunodeficiency. The Service Agent recognizes a need for clinical evidence, so a request is sent all the way back to the facility and once the facility fulfills this with clinical documents, the information is sent back to the Service Agent to continue working towards a decision. Ultimately, the additional information is enough to approve the prior authorization request in the same fashion as the first demo case.

This case shows the ability for the framework to determine what additional information is needed and to request that information from the facility. Note that there are multiple other ways to approach a solution to this problem, but that this demonstration highlights a self-contained, agent-based solution to requests for more data. 
