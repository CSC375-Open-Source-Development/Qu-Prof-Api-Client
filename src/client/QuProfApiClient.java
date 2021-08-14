package client;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import responseObjects.Professor;

public class QuProfApiClient {
	public ArrayList<Professor> getProfessors() {
        String endpoint = "https://www.qu.edu/FacultyAndStaffListingApi/GetProfiles/";
        QueryParams queryParams = new QueryParams() {{
        	addParam("page", "1");
        }};
        Response response = HttpRequest.get(endpoint, queryParams);
        JsonObject jsonResponse = (JsonObject)response.getBody();
        JsonArray staffListingJson = jsonResponse.get("StaffListing").getAsJsonArray(); 
        
        ArrayList<Professor> professors = new ArrayList<>();
        for (int i = 0; i < staffListingJson.size(); i++) {
        	JsonObject professorJson = staffListingJson.get(i).getAsJsonObject();
        	professors.add(new Professor(professorJson));
        }
        return professors;
	}
}
