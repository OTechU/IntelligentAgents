package data;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

@SuppressWarnings("restriction")
public class MedicalInfo {
	
	private String medication;
	private String strength;
	private String directions;
	private String diagnosis;
	private String HIVorAIDS;
	private String cptCode;
	private String icd10Codes;
	private String isPregnant;
	private String explanation;
	
	private PreviousMeds previousMeds;
	
	public MedicalInfo(String str_xml) {
		
		try {
			 
	         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	         InputSource inputData = new InputSource();
	         inputData.setCharacterStream(new StringReader(str_xml));
	         
	         Document doc = dBuilder.parse(inputData);
	         
	         doc.getDocumentElement().normalize();
	         
	         // Select element
	         NodeList nList = doc.getElementsByTagName("medical_info");
	        
	         for (int temp = 0; temp < nList.getLength(); temp++) {
	            Node nNode = nList.item(temp);
	            
	            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	               Element eElement = (Element) nNode;
	                            
	               // Grab whatever data you want here
	               this.setMedication(eElement
	                  .getElementsByTagName("medication")
	                  .item(0)
	                  .getTextContent());
	            
	               this.setDiagnosis(eElement
                       .getElementsByTagName("diagnosis")
                       .item(0)
                       .getTextContent());
	               
	               this.setCptCode(eElement
                       .getElementsByTagName("cpt_code")
                       .item(0)
                       .getTextContent());
	               
	               this.setIcd10Codes(eElement
                       .getElementsByTagName("icd_10")
                       .item(0)
                       .getTextContent());
	               
	               this.setExplanation(eElement
            		   .getElementsByTagName("explanation")
            		   .item(0)
            		   .getTextContent());
	            }
	         }
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
		
	}
	
	public String getMedication() {
		return medication;
	}
	public void setMedication(String medication) {
		this.medication = medication;
	}
	public String getStrength() {
		return strength;
	}
	public void setStrength(String strength) {
		this.strength = strength;
	}
	public String getDirections() {
		return directions;
	}
	public void setDirections(String directions) {
		this.directions = directions;
	}
	public String getDiagnosis() {
		return diagnosis;
	}
	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
	public String getHIVorAIDS() {
		return HIVorAIDS;
	}
	public void setHIVorAIDS(String hIVorAIDS) {
		HIVorAIDS = hIVorAIDS;
	}
	public String getCptCode() {
		return cptCode;
	}
	public void setCptCode(String cptCode) {
		this.cptCode = cptCode;
	}
	public String getIcd10Codes() {
		return icd10Codes;
	}
	public void setIcd10Codes(String icd10Codes) {
		this.icd10Codes = icd10Codes;
	}
	public String getIsPregnant() {
		return isPregnant;
	}
	public void setIsPregnant(String isPregnant) {
		this.isPregnant = isPregnant;
	}
	public String getExplanation() {
		return explanation;
	}
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}
	public PreviousMeds getPreviousMeds() {
		return previousMeds;
	}
	public void setPreviousMeds(PreviousMeds previousMeds) {
		this.previousMeds = previousMeds;
	}
	
	
}

class PreviousMeds {
	
	private String name;
	private String strength;
	private String dateRange;
	private String description;
	private String reason;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStrength() {
		return strength;
	}
	public void setStrength(String strength) {
		this.strength = strength;
	}
	public String getDateRange() {
		return dateRange;
	}
	public void setDateRange(String dateRange) {
		this.dateRange = dateRange;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
}