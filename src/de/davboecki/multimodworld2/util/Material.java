package de.davboecki.multimodworld2.util;

public enum Material {
	STONE(1),
	GRASS(2),
	DIRT(3),
	BEDROCK(7),
	GLASS(20),
	LAVA(11),
	GLOWSTONE(89),
	REDSTONEBLOCK(152),
	TORCH(50),
	LADDER(65),
	CARPET(171),
	CHEST(54);

	private int id;
	
	Material(int value) {
		this.id = value;
	}
	
	public int getId() {
		return id;
	}
}
