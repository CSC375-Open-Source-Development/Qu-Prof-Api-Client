import java.util.ArrayList;

import client.QuProfApiClient;
import responseObjects.Professor;

public class Main {
	public static void main(String[] args) {
		QuProfApiClient client = new QuProfApiClient();
		ArrayList<Professor> professors = client.getProfessors();
		System.out.println(professors);
	}

}