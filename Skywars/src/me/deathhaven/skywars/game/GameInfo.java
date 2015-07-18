package me.deathhaven.skywars.game;

import java.util.List;

import me.deathhaven.skywars.player.GamePlayer;

public class GameInfo {
	
	public int gId;
	public GameState state;
	public String mapName;
	
	public int playerCount;
	public List<GamePlayer> players;
	public int spectatorCount;
	public List<GamePlayer> spectators;
	
	public GameInfo(int gId) {
		this.gId = gId;
	}
	
	public GameInfo(int gId, GameState state, List<GamePlayer> gPlayers, List<GamePlayer>gSpectators, String mapName) {
		this.gId = gId;
		this.state = state;
		players = gPlayers;
		spectators = gSpectators;
		playerCount = players.size();
		spectatorCount = spectators.size();
		this.mapName = mapName;
	}
	
	public void setPlayers(List<GamePlayer> players) {
		this.players = players;
		playerCount = players.size();
	}
	
	public void setSpectators(List<GamePlayer> spectators) {
		this.spectators = spectators;
		spectatorCount = spectators.size();
	}
	
	
	
	
	
	
	

}
