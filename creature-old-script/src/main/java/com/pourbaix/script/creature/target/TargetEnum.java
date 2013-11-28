package com.pourbaix.script.creature.target;

import com.pourbaix.script.creature.generator.GeneratorException;

public enum TargetEnum {
	MYSELF("Myself", "Myself"), LASTSEEN("LastSeen", "LastSeenBy(Myself)"), LASTATTACKER("attacker", "LastAttackerOf(Myself)"), LASTHITTER("hitter",
			"LastHitter(Myself)"), LASTSUMMONER("summoner", "LastSummonerOf(Myself)"), LASTHEARD("heard", "LastHeardBy(Myself)"), PLAYER1("Player1", "Player1"), PLAYER2(
			"Player2", "Player2"), PLAYER3("Player3", "Player3"), PLAYER4("Player4", "Player4"), PLAYER5("Player5", "Player5"), PLAYER6("Player6", "Player6"), NEARESTENEMY(
			"nearest", "NearestEnemyOf(Myself)"), NEARESTENEMYOFTYPE("nearestType", "NearestEnemyOfType");

	private String shortcut;
	private String code;

	private TargetEnum(final String shortcut, final String code) {
		this.shortcut = shortcut;
		this.code = code;
	}

	public String getShortcut() {
		return this.shortcut;
	}

	public String getCode() {
		return this.code;
	}

	public static TargetEnum fromString(final String text) throws GeneratorException {
		if (text != null) {
			for (TargetEnum v : TargetEnum.values()) {
				if (text.equalsIgnoreCase(v.shortcut) || text.equalsIgnoreCase(v.code)) {
					return v;
				}
			}
		}
		throw new GeneratorException("Unknown target: " + text);
	}

}
