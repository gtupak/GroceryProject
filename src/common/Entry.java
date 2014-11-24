package common;

import java.io.Serializable;

public class Entry implements Serializable {
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

	public Entry(int int1, String string, String string2, String string3, boolean boolean1) {
		id = int1;
		description = string;
		creator = string2;
		checker = string3;
		checked = boolean1;
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
	
	public String toString(){
		return "ID: " + id + "; Description: " + description + "; Creator: " + creator + "; Checker: "
				+ checker + "; Checked: " + checked;
	}
}
