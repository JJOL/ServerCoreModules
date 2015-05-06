package me.jjservices.cccontroller.api.players;

import java.util.UUID;

import org.bukkit.entity.Player;

public class CCPlayer {

	UUID playerID;
	
	String customName;
	String playerName;
	
	public CCPlayer(Player p) {
		playerID = p.getUniqueId();
		playerName = p.getDisplayName();
	}
	
	
	
	public boolean isFirstTime() {
		return true;
	}
	
}
