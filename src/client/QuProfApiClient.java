package client;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.management.RuntimeErrorException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import responseObjects.Professor;
import responseObjects.ProfessorDetailedInfo;
import responseObjects.ProfessorGeneralInfo;

public class QuProfApiClient {
	public ArrayList<Professor> getProfessors() {
        String facultyListingeEndpoint = "https://www.qu.edu/FacultyAndStaffListingApi/GetProfiles/";
        QueryParams queryParams = new QueryParams() {{
        	addParam("page", "1");
        }};
        Response response = HttpRequest.get(facultyListingeEndpoint, queryParams);
        JsonObject jsonResponse = (JsonObject)response.getBody();
        JsonArray staffListingJson = jsonResponse.get("StaffListing").getAsJsonArray(); 
        
        ArrayList<Professor> professors = new ArrayList<>();
        for (int i = 0; i < staffListingJson.size(); i++) {
        	JsonObject professorGeneralInfoJson = staffListingJson.get(i).getAsJsonObject();
        	ProfessorGeneralInfo generalInfo = mapProfessorGeneralInfoParams(professorGeneralInfoJson);
        	ProfessorDetailedInfo detailedInfo = getProfessorDetailedInfo(generalInfo.getProfileUrl());
        	professors.add(new Professor(generalInfo, detailedInfo));
        }
        return professors;
	}
	
	private ProfessorDetailedInfo getProfessorDetailedInfo(String profileUrl) {
    	String professorProfileEndpoint = "https://www.qu.edu" +  profileUrl;
    	Document document = null;
    	try {
    		document = Jsoup.connect(professorProfileEndpoint).get();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}

        Element main = document.body().getElementById("main");
        Element profileDetails = main.getElementsByClass("profile-details").get(0);
        Elements profileDetailsBlocks = profileDetails.getElementsByClass("profile-details__block");
        
        ArrayList<String> educations = new ArrayList<>();
        ArrayList<String> organizations = new ArrayList<>();
        String officeLocation = "";
        String mailDropLocation = "";

        for (int i = 0; i < profileDetailsBlocks.size(); i++) {
        	Element profileDetailsBlock = profileDetailsBlocks.get(i);
        	Elements heading = profileDetailsBlock.getElementsByClass("profile-details__block-heading");
        	if (heading.get(0).html().contains("Education")) {
        		Elements educationsListItems = profileDetailsBlock.getElementsByClass("profile-details__block-list-item");
        		for (int j = 0; j < educationsListItems.size(); j++) {
        			educations.add(educationsListItems.get(j).html().trim());
        		}
        	}
        	else if (heading.get(0).html().contains("Organization")) {
        		Elements organizationsListItems = profileDetailsBlock.getElementsByClass("profile-details__block-list-item");
        		for (int j = 0; j < organizationsListItems.size(); j++) {
        			organizations.add(organizationsListItems.get(j).html().trim());
        		}
        	}
        	else if (heading.get(0).html().contains("Office Location")) {
        		Elements officeLocationItem = profileDetailsBlock.getElementsByClass("profile-details__block-list-item");
        		officeLocation = officeLocationItem.html().trim();
        	}
        	else if (heading.get(0).html().contains("Mail Drop")) {
        		Elements mailDropItem = profileDetailsBlock.getElementsByClass("profile-details__block-list-item");
        		mailDropLocation = mailDropItem.html().trim();
        	}
        }
        return new ProfessorDetailedInfo(educations, organizations, officeLocation, mailDropLocation);
	}
	
	private ProfessorGeneralInfo mapProfessorGeneralInfoParams(JsonObject generalInfoJson) {
        String fullName = !isJsonNull(generalInfoJson, "FullName") ? generalInfoJson.get("FullName").getAsString() : "";
        ArrayList<String> positions = new ArrayList<>();
        if (!isJsonNull(generalInfoJson, "Position")) {
        JsonArray positionsJson = generalInfoJson.get("Position").getAsJsonArray();
	        for (int i = 0; i < positionsJson.size(); i++) {
	        	positions.add(positionsJson.get(i).getAsString());
	        }
        }
        String imageUrl = !isJsonNull(generalInfoJson, "Image") ? generalInfoJson.get("Image").getAsString() : "";
        String department = !isJsonNull(generalInfoJson, "Department") ? generalInfoJson.get("Department").getAsString() : "";
        String phoneNumber = !isJsonNull(generalInfoJson, "PhoneNumber") ? generalInfoJson.get("PhoneNumber").getAsString() : "";
        String emailAddress = !isJsonNull(generalInfoJson, "EmailAddress") ? generalInfoJson.get("EmailAddress").getAsString() : "";
        String profileUrl = !isJsonNull(generalInfoJson, "ProfileUrl") ? generalInfoJson.get("ProfileUrl").getAsString() : "";
        return new ProfessorGeneralInfo(fullName, positions, imageUrl, department, phoneNumber, emailAddress, profileUrl);
	}
	
	/*
	private ProfessorDetailedInfo mapProfessorDetailedInfoParams(JsonObject detailedInfoJson) {
		
	}
	*/
	
	private boolean isJsonNull(JsonObject json, String property) {
		return !json.has(property) || json.get(property).isJsonNull();
	}
}
