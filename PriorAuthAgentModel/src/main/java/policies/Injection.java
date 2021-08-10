package policies;

// Default Injection is Flu Shot
public class Injection {
	// Other comments denote values for the IGIV
	String cpt_code = "90630"; // 90283
	String purpose = "immunization"; //
	String care_type = "preventative";
	double price = 40.0; // 10000.0
	
	public String getCpt_code() {
		return cpt_code;
	}

	public void setCpt_code(String cpt_code) {
		this.cpt_code = cpt_code;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getCare_type() {
		return care_type;
	}

	public void setCare_type(String care_type) {
		this.care_type = care_type;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Injection (String _cpt_code, String _purpose, String _care_type, double _price) {
		this.cpt_code = _cpt_code;
		this.purpose = _purpose;
		this.care_type = _care_type;
		this.price = _price;
	}
}
