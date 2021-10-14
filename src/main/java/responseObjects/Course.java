package responseObjects;

public class Course {
	private String name;
	private String semester;
	private String catalogUrl;
	
	public Course(String name, String semester, String catalogUrl) {
		this.name = name;
		this.semester = semester;
		this.catalogUrl = catalogUrl;
	}

	public String getName() {
		return name;
	}

	public String getSemester() {
		return semester;
	}

	public String getCatalogUrl() {
		return catalogUrl;
	}

	@Override
	public String toString() {
		return "Course [name=" + name + ", semester=" + semester + ", catalogUrl=" + catalogUrl + "]";
	}
}
