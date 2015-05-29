package me.deathhaven.skywars.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.function.BiConsumer;

import me.deathhaven.skywars.game.Game;
import me.deathhaven.skywars.player.GamePlayer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.Maps;
import com.google.common.collect.Queues;

public class CustomController implements Listener{
/*
	
	public static CustomController instance; 
	public static List<Player> openPlayers = new ArrayList<Player>();
	private Inventory infoMenuInv;
	private Queue<GameChangeEvent> queue = Queues.newLinkedBlockingQueue();
	private HashMap<Integer, String> gamePlayers = Maps.newHashMap();
	private HashMap<Integer, String> gameStates = Maps.newHashMap();
	
	
	public static CustomController get() {
		if(instance == null) {
			instance = new CustomController();
		}
		return instance;
	}
	
	public CustomController() {
        
		
		
        ItemStack stack = new ItemStack(Material.GRASS,1);
       // menu.setOption(0, new It, name, info)
	}
	
	public Queue<GameChangeEvent> getEventsQueue() {
		return queue;
	}
	
	public void addEvent(GameChangeEvent e) {
		queue.add(e);
	}
	
	
	public enum ChangeType {
		PlayerLeft, PlayerJoin, SpectateLeft, SpectateJoin, GameStarts, GameEnds
		
	}

	public void setIndicator(int pos, ItemStack item) {
    	infoMenuInv.setItem(pos, item);
    }
	
	@EventHandler
	public void onGameChange(GameChangeEvent e) {
		
		final Game game = e.getGame();
		int id = e.getData();
		
		switch (e.getChangeType()) 
		{
		case GameStarts:
		case GameEnds:
			
			ItemStack[] shiftingGames = new ItemStack[GameController.get().getAll().size()-1];
			ItemStack[] icons = infoMenuInv.getContents();
			for(int ii = id; ii < infoMenuInv.getContents().length; ) {
				infoMenuInv.setItem(ii, infoMenuInv.getItem(++ii));
			}
				
			// Add or Remove Game
			// moves Items By One
			
			
			
			break;
		default:
			// Change Indicator
			
			setIndicator(key, pos, item);
		}
	}
	
	public ItemStack getItemDescription(int n, Game g) {
		ItemStack showItem = new ItemStack(Material.BOW);
		ItemMeta im = showItem.getItemMeta();
		im.setDisplayName("Skywars Game #"+n);
		im.setLore(getIconGameLore(g));
				
		showItem.setItemMeta(im);
		return showItem;
	}
	
	public List<String> getIconGameLore(Game g) {
		
		List<String> lore = new ArrayList<>();
		
		lore.add("§4§lState §7>>  §c"+g.getState().toString());
		// Add Players
		lore.add("§4§l(Players)");
		for(GamePlayer p : g.getPlayers()) {
			lore.add(p.getBukkitPlayer().getName());
		}
		// Add Spectators		
		lore.add("§4§l(Spectators)");
		for(GamePlayer p : g.getAllSpectators()) {
			lore.add(p.getBukkitPlayer().getName());
		}
		
		return lore;
	}
	
	private void updateInfoIconMenu() {
		
		int gameIndicator;
		
		ItemStack stack = getItemDescription(game);
		
		setIndicator()
		
	}
	
	public class GameChangeEvent {

		
		private ChangeType type;
		private Player player;
		private String msg;
		private int data;
		private Game game;
		
		public GameChangeEvent(ChangeType type, Player player, String  msg, Game game, int data) {
			this.type   = type;
			this.player = player;
			this.msg    = msg;
			this.data   = data;
			this.game   = game;
		}
		
		public ChangeType getChangeType() {
			return type;
		}
		
		public Player getPlayer() {
			return player;
		}
		
		public String getMessage() {
			return msg;
		}
		
		public int getData() {
			return data;
		}
		
		public Game getGame() {
			return game;
		}
		
	}*/
	
	
}
