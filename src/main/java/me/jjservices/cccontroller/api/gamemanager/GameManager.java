package me.jjservices.cccontroller.api.gamemanager;

import me.jjservices.cccontroller.core.CCSCore;
import me.jjservices.cccontroller.listeners.AccessListener;

import org.bukkit.Bukkit;

public final class GameManager {
	
	
	public static void initializeGame(Game game) {
		
	Bukkit.getPluginManager().registerEvents(new AccessListener(game.getLobbyLocation()), CCSCore.INSTANCE);
		
		
	}

}
