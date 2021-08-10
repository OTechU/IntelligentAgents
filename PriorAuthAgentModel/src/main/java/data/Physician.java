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
public class Physician {

	private String firstName;
	private String lastName;
	private String credentials;
	private String address;
	private String city;
	private String state;
	private String zipCode;
	private String phoneNumber;
	private String fax;
	private String specialty;
	private String npiNumber;
	private String contactName;
	private String hospitalName;
	
	private boolean inNetwork = false; // This will be updated by the eligibility agent
	
	public Physician(String str_xml) {
		
		try {
	    	  
	         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	         InputSource inputData = new InputSource();
	         inputData.setCharacterStream(new StringReader(str_xml));
	         
	         Document doc = dBuilder.parse(inputData);
	         
	         doc.getDocumentElement().normalize();
	         
	         NodeList nList = doc.getElementsByTagName("physician_info");
	        
	         for (int temp = 0; temp < nList.getLength(); temp++) {
	            Node nNode = nList.item(temp);
	            
	            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	               Element eElement = (Element) nNode;
	                              
	               this.setFirstName(eElement
	                  .getElementsByTagName("first_name")
	                  .item(0)
	                  .getTextContent());            
	               
	               this.setLastName(eElement
                       .getElementsByTagName("last_name")
                       .item(0)
                       .getTextContent());
	               
	               this.setNpiNumber(eElement
            		   .getElementsByTagName("npi_num")
            		   .item(0)
            		   .getTextContent());
	               
	               this.setCredentials(eElement
            		   .getElementsByTagName("credential")
            		   .item(0)
            		   .getTextContent());
	               
	               this.setHospitalName(eElement
            		   .getElementsByTagName("hospital")
            		   .item(0)
            		   .getTextContent());
	               
	               this.setContactName(eElement
            		   .getElementsByTagName("contact_name")
            		   .item(0)
            		   .getTextContent());
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
	public String getCredentials() {
		return credentials;
	}
	public void setCredentials(String credentials) {
		this.credentials = credentials;
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
	public void setInNetwork(boolean inn) {
		this.inNetwork = inn;
	}
	public boolean getInNetwork() {
		return inNetwork;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getSpecialty() {
		return specialty;
	}
	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}
	public String getNpiNumber() {
		return npiNumber;
	}
	public void setNpiNumber(String npiNumber) {
		this.npiNumber = npiNumber;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	
}
