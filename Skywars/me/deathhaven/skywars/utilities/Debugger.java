package me.deathhaven.skywars.utilities;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Debugger {

	private static Debugger instance = null;
	
	//private List<Player> devPlayers = new ArrayList<Player>();
	
	public static Debugger get() {
		if(instance == null) {
			instance = new Debugger();
		}
		return instance;
	}
	
	public Debugger() {
		refreshDevs();
	}
	
	public void refreshDevs() {
		
	}
	
	public void sendDebbugMC(String msg) {
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			if(p.getName().startsWith("JJ")) {
				sendMsg(p, msg);
			}
		}
	}
	
	private void sendMsg(Player p, String msg) {
		p.sendMessage(msg);
	}
	
}
