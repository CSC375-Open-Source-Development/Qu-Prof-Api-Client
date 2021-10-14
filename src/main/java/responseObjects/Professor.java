package responseObjects;

public class Professor {
	private ProfessorGeneralInfo generalInfo;
	private ProfessorDetailedInfo detailedInfo;
	
	public Professor(ProfessorGeneralInfo generalInfo, ProfessorDetailedInfo detailedInfo) {
		this.generalInfo = generalInfo;
		this.detailedInfo = detailedInfo;
	}
	
	public ProfessorGeneralInfo getGeneralInfo() {
		return generalInfo;
	}

	public ProfessorDetailedInfo getDetailedInfo() {
		return detailedInfo;
	}

	@Override
	public String toString() {
		return "Professor [generalInfo=" + generalInfo + ", detailedInfo=" + detailedInfo + "]";
	}
}
