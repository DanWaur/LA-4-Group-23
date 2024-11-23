package model;
/*
 * Juan Rogel Acedo (jarogelacedo)
 * Daniel (netid)
 * Marco (pena8)
 * Devin Dinh (devdinh)
 */

public enum DiceValue {
    ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6);

	private final int value;

	private DiceValue(int value) {
		this.value = value;
	}

	public int getVal() {
		return this.value;
	}
}
