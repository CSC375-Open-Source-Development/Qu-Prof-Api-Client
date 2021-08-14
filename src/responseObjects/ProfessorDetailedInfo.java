package responseObjects;

import java.util.ArrayList;

public class ProfessorDetailedInfo {
    private ArrayList<String> educations;
    private ArrayList<String> organizations;
    private String officeLocation;
    private String mailDropLocation;
    
	public ProfessorDetailedInfo(ArrayList<String> educations, ArrayList<String> organizations, String officeLocation, String mailDropLocation) {
		this.educations = educations;
		this.organizations = organizations;
		this.officeLocation = officeLocation;
		this.mailDropLocation = mailDropLocation;
	}

	public ArrayList<String> getEducations() {
		return educations;
	}

	public ArrayList<String> getOrganizations() {
		return organizations;
	}

	public String getOfficeLocation() {
		return officeLocation;
	}

	public String getMailDropLocation() {
		return mailDropLocation;
	}

	@Override
	public String toString() {
		return "ProfessorDetailedInfo [educations=" + educations + ", organizations=" + organizations
				+ ", officeLocation=" + officeLocation + ", mailDropLocation=" + mailDropLocation + "]";
	}
}
