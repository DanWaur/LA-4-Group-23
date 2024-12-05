/*
* Juan Rogel Acedo (jarogelacedo)
* Daniel (netid)
* Marco (pena8)
* Devin Dinh (devdinh)
*/

package model;

public enum DiceValue {
    ONE(1, "dice1.png"), TWO(2, "dice2.png"), 
	THREE(3, "dice3.png"), FOUR(4, "dice4.png"), 
	FIVE(5, "dice5.png"), SIX(6, "dice6.png");

	private final int value;
	private final String fileName;

	private DiceValue(int value, String fileName) {
		this.value = value;
		this.fileName = fileName;
	}

	public int getVal() {
		return this.value;
	}

	public String getFileName() {
		return this.fileName;
	}
}
