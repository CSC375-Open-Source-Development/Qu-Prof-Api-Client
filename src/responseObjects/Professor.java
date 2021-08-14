package responseObjects;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Professor {
	private String fullName;
	private ArrayList<String> positions;
	private String imageUrl;
	private String department;
	private String phoneNumber;
	private String emailAddress;
	private String profileUrl;
	
	
	public Professor(String fullName, ArrayList<String> positions, String imageUrl, String department, String phoneNumber, String emailAddress, String profileUrl) {
		super();
		this.fullName = fullName;
		this.positions = positions;
		this.imageUrl = imageUrl;
		this.department = department;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
		this.profileUrl = profileUrl;
	}

	public Professor(JsonObject json) {
		mapParams(json);
	}
	
	private void mapParams(JsonObject json) {
        fullName = !isJsonNull(json, "FullName") ? json.get("FullName").getAsString() : "";
        positions = new ArrayList<>();
        if (!isJsonNull(json, "Position")) {
        JsonArray positionsJson = json.get("Position").getAsJsonArray();
	        for (int i = 0; i < positionsJson.size(); i++) {
	        	positions.add(positionsJson.get(i).getAsString());
	        }
        }
        imageUrl = !isJsonNull(json, "Image") ?json.get("Image").getAsString() : "";
        department = !isJsonNull(json, "Department") ? json.get("Department").getAsString() : "";
        phoneNumber = !isJsonNull(json, "PhoneNumber") ? json.get("PhoneNumber").getAsString() : "";
        emailAddress = !isJsonNull(json, "EmailAddress") ? json.get("EmailAddress").getAsString() : "";
        profileUrl = !isJsonNull(json, "ProfileUrl") ? json.get("ProfileUrl").getAsString() : "";
	}
	
	private boolean isJsonNull(JsonObject json, String property) {
		return !json.has(property) || json.get(property).isJsonNull();
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public ArrayList<String> getPositions() {
		return positions;
	}

	public void setPositions(ArrayList<String> positions) {
		this.positions = positions;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}

	@Override
	public String toString() {
		return "Professor [fullName=" + fullName + ", positions=" + positions + ", imageUrl=" + imageUrl
				+ ", department=" + department + ", phoneNumber=" + phoneNumber + ", emailAddress=" + emailAddress
				+ ", profileUrl=" + profileUrl + "]";
	}
}
