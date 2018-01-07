package nl.androidappfactory.recipe.models;

public enum Difficulty {

	EASY("Easy to do"), MODERATE("Moderate"), KIND_OF_HARD("Not as easy as you might think"), HARD("Pretty hard");

	private String value;

	private Difficulty(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
