package common;

public class Entry {
	private int id;
	private String description;
	private boolean checked = false;
	private String creator;
	private String checker =  null;
	
	public Entry(int id, String description, String creator){
		this.id = id;
		this.creator = creator;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getChecker() {
		return checker;
	}

	public void setChecker(String checker) {
		this.checker = checker;
	}

	public int getId() {
		return id;
	}
	
	
}
