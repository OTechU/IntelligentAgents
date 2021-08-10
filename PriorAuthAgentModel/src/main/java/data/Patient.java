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
public class Patient {
	
	private String firstName;
	private String lastName;
	private String memberId;
	private String address;
	private String city;
	private String state;
	private String zipCode;
	private String phoneNumber;
	private String DOB;
	private String allergies;
	private String primaryInsurance;
	private String policyNumber;
	private String groupNumber;
	private boolean newMedication;
	private String medicationContinued;
	private boolean hospitalized;
	
	public Patient(String str_xml) {
		
		try {
	    	 
	         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	         InputSource inputData = new InputSource();
	         inputData.setCharacterStream(new StringReader(str_xml));
	         
	         Document doc = dBuilder.parse(inputData);
	         
	         doc.getDocumentElement().normalize();
	         
	         // Select element
	         NodeList nList = doc.getElementsByTagName("patient_info");
	        
	         for (int temp = 0; temp < nList.getLength(); temp++) {
	            Node nNode = nList.item(temp);
	            
	            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	               Element eElement = (Element) nNode;
	                              
	               // Grab whatever data you want here
	               this.setFirstName(eElement
	                  .getElementsByTagName("first_name")
	                  .item(0)
	                  .getTextContent());
	                 
	               this.setLastName(eElement
                       .getElementsByTagName("last_name")
                       .item(0)
                       .getTextContent());
	               
	               this.setHospitalized(eElement
                       .getElementsByTagName("hospitalized")
                       .item(0)
                       .getTextContent().equals("true"));
	               
	            }
	         }
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getDOB() {
		return DOB;
	}
	public void setDOB(String dOB) {
		DOB = dOB;
	}
	public String getAllergies() {
		return allergies;
	}
	public void setAllergies(String allergies) {
		this.allergies = allergies;
	}
	public String getPrimaryInsurance() {
		return primaryInsurance;
	}
	public void setPrimaryInsurance(String primaryInsurance) {
		this.primaryInsurance = primaryInsurance;
	}
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	public String getGroupNumber() {
		return groupNumber;
	}
	public void setGroupNumber(String groupNumber) {
		this.groupNumber = groupNumber;
	}
	public boolean isNewMedication() {
		return newMedication;
	}
	public void setNewMedication(boolean newMedication) {
		this.newMedication = newMedication;
	}
	public String getMedicationContinued() {
		return medicationContinued;
	}
	public void setMedicationContinued(String medicationContinued) {
		this.medicationContinued = medicationContinued;
	}
	public boolean isHospitalized() {
		return hospitalized;
	}
	public void setHospitalized(boolean hospitalized) {
		this.hospitalized = hospitalized;
	}
	
	
}
