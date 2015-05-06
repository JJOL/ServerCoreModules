package me.jjservices.cccontroller.api.gamemanager;

import org.bukkit.Bukkit;

public final class GameManager {
	
	
	public static void initializeGame(Game game) {
		
	Bukkit.getPluginManager().registerEvents(new AccessListener(), arg1);
		
		
	}

}
