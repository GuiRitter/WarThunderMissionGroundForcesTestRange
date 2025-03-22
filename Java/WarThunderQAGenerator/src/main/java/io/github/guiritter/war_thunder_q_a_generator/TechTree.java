package io.github.guiritter.war_thunder_q_a_generator;

import java.util.Objects;

public final class TechTree {

	public final String jsonName;
	public final String blk;

	public TechTree(String jsonName, String blk) {
		this.jsonName = jsonName;
		this.blk = blk;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TechTree)) {
			return false;
		}

		var other = (TechTree) obj;

		return ((blk.compareTo(other.blk) == 0) && jsonName.compareTo(other.jsonName) == 0);
	}

	@Override
	public int hashCode() {
		return Objects.hash(blk, jsonName);
	}
}
