package me.jjservices.cccontroller.listeners;

import me.jjservices.cccontroller.api.players.CCPlayer;
import me.jjservices.cccontroller.api.players.PlayerManager;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class AccessListener implements Listener {
	
	private Location lobbySpawn;
	private boolean joinInLobby;
	
	public AccessListener(Location lobbySpawn) {
		this.lobbySpawn = lobbySpawn;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		
		if (joinInLobby) {
			event.getPlayer().teleport(lobbySpawn);
		} else {
			CCPlayer ccplayer = PlayerManager.loadPlayer(p);
			ccplayer.isFirstTime();
			event.getPlayer().teleport(lobbySpawn);
		}
	}

}
