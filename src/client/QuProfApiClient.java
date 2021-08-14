package client;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import responseObjects.Course;
import responseObjects.Professor;
import responseObjects.ProfessorDetailedInfo;
import responseObjects.ProfessorGeneralInfo;

public class QuProfApiClient {
	public ArrayList<Professor> getProfessors() {
        String facultyListingeEndpoint = "https://www.qu.edu/FacultyAndStaffListingApi/GetProfiles/?page=1";
        Response response = HttpRequest.get(facultyListingeEndpoint);
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
	
	private ProfessorGeneralInfo mapProfessorGeneralInfoParams(JsonObject generalInfoJson) {
        String fullName = existsInJson(generalInfoJson, "FullName") ? generalInfoJson.get("FullName").getAsString() : "";
        ArrayList<String> positions = new ArrayList<>();
        if (existsInJson(generalInfoJson, "Position")) {
	        JsonArray positionsJson = generalInfoJson.get("Position").getAsJsonArray();
	        for (int i = 0; i < positionsJson.size(); i++) {
	        	positions.add(positionsJson.get(i).getAsString());
	        }
        }
        String imageUrl = existsInJson(generalInfoJson, "Image") ? generalInfoJson.get("Image").getAsString() : "";
        String department = existsInJson(generalInfoJson, "Department") ? generalInfoJson.get("Department").getAsString() : "";
        String phoneNumber = existsInJson(generalInfoJson, "PhoneNumber") ? generalInfoJson.get("PhoneNumber").getAsString() : "";
        String emailAddress = existsInJson(generalInfoJson, "EmailAddress") ? generalInfoJson.get("EmailAddress").getAsString() : "";
        String profileUrl = existsInJson(generalInfoJson, "ProfileUrl") ? generalInfoJson.get("ProfileUrl").getAsString() : "";
        return new ProfessorGeneralInfo(fullName, positions, imageUrl, department, phoneNumber, emailAddress, profileUrl);
	}
	
	// this was such a PITA
	// while I could grab quinnipiac's generic professor list in a nice json format,
	// the individual professor profiles are just gigantic blobs of html data that was never meant to be retrieved in this kind of way.
	// parsing html is exhausting
	// I don't recommend you touch this method unless you 100% know what you are doing, it's fragile
	private ProfessorDetailedInfo getProfessorDetailedInfo(String profileUrl) {
    	try {

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
	        Elements profileDetailsCoursesBlock = profileDetails.getElementsByClass("profile-details__courses");
	
	        ArrayList<String> educations = new ArrayList<>();
	        ArrayList<String> organizations = new ArrayList<>();
	        String officeLocation = "";
	        String mailDropLocation = "";
	        ArrayList<Course> courses = new ArrayList<>();
	
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
	        
	        try {
		        Elements courseListItems = profileDetailsCoursesBlock.get(0).getElementsByClass("profile-details__block-list-item");
		        for (int i = 0; i < courseListItems.size(); i++) {
		        	Element courseItem = courseListItems.get(i);
		        	Element courseItemDetails = courseItem.getElementsByTag("a").get(0);
		        	
		        	String name = courseItemDetails.html().trim();
		        	if (name.indexOf("<br>") != -1) {
		            	name = name.substring(0, name.indexOf("<br>")).trim();
		        	}
		        	if (name.indexOf("<span>") != -1) {
		        		name = name.substring(0, name.indexOf("<span>")).trim();
		        	}
		        	String semester = courseItemDetails.getElementsByTag("span").get(0).html().trim();
		        	String catalogUrl = courseItemDetails.attr("href");
		        	
		        	courses.add(new Course(name, semester, catalogUrl));
		        }
	        } catch (IndexOutOfBoundsException e) {
				// professor has no course info available :(
			}
	        return new ProfessorDetailedInfo(educations, organizations, officeLocation, mailDropLocation, courses);
    	}
    	catch (Exception e) {
    		// due to how uh ~unreliable~ html parsing is, if anything goes wrong trying to get the detailed info,
    		// this just returns an instantiated object full of empty lists/values to make it easier on the user,
    		// instead of just breaking the program or returning null.
    		return new ProfessorDetailedInfo(new ArrayList<String>(), new ArrayList<String>(), "", "", new ArrayList<Course>());
    	}
	}
	
	private boolean existsInJson(JsonObject json, String property) {
		return json.has(property) && !json.get(property).isJsonNull();
	}
}
