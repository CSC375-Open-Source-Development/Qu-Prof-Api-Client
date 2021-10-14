package responseObjects;

import java.util.ArrayList;

public class ProfessorGeneralInfo {
	private String fullName;
	private ArrayList<String> positions;
	private String imageUrl;
	private String department;
	private String phoneNumber;
	private String emailAddress;
	private String profileUrl;
	
	public ProfessorGeneralInfo(String fullName, ArrayList<String> positions, String imageUrl, String department, String phoneNumber, String emailAddress, String profileUrl) {
		this.fullName = fullName;
		this.positions = positions;
		this.imageUrl = imageUrl;
		this.department = department;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
		this.profileUrl = profileUrl;
	}

	public String getFullName() {
		return fullName;
	}

	public ArrayList<String> getPositions() {
		return positions;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getDepartment() {
		return department;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getProfileUrl() {
		return profileUrl;
	}

	@Override
	public String toString() {
		return "ProfessorGeneralInfo [fullName=" + fullName + ", positions=" + positions + ", imageUrl=" + imageUrl
				+ ", department=" + department + ", phoneNumber=" + phoneNumber + ", emailAddress=" + emailAddress
				+ ", profileUrl=" + profileUrl + "]";
	}
}
