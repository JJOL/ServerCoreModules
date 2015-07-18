package me.deathhaven.skywars.game;

import com.sk89q.worldedit.CuboidClipboard;

public class GameConfig {

	private CuboidClipboard schematic;
	private String gameName;
	
	public GameConfig(String gameName) {
		this.gameName = gameName;
	}
	
	public GameConfig(String gameName, CuboidClipboard schematic) {
		this.gameName = gameName;
		this.schematic = schematic;
	}
	
	public void setSchematic(CuboidClipboard shematic) {
		this.schematic = shematic;
	}
	
	public CuboidClipboard getSchematic() {
		return schematic;
	}
	
	public String getGameName() {
		return gameName;
	}
	
}
