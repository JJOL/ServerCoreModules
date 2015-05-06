package me.jjservices.cccontroller.api.players;

import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerManager {

	public static HashSet<CCPlayer> serverPlayers = new HashSet<CCPlayer>();
	
	public static void registerPlayer(Player player) {
		
	}
	
	public static CCPlayer loadPlayer(Player p) {
		return new CCPlayer(p);
	}
	
	public static CCPlayer loadPlayer(UUID id) {
		return new CCPlayer(Bukkit.getPlayer(id));
	}
	
}
