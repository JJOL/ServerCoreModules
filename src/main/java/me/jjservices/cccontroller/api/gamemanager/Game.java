package me.jjservices.cccontroller.api.gamemanager;

import java.util.List;

import me.jjservices.cccontroller.api.utils.PStat;

import org.bukkit.ChatColor;

public class Game {

	private final String name;
	private final ChatColor nameColor;
	
	private List<PStat> gameStats;
	
	public Game(String name, ChatColor nameColor) {
		this.name = name;
		this.nameColor = nameColor;
	}
	
	
	public String getName() {
		return name;
	}
	public ChatColor getNameColor() {
		return nameColor;
	}
}
