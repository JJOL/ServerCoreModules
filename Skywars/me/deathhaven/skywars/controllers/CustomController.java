package me.deathhaven.skywars.controllers;

import java.util.List;

import me.deathhaven.skywars.game.Game;
import me.deathhaven.skywars.game.GameInfo;
import me.deathhaven.skywars.player.GamePlayer;

public class CustomController{
	/*
	 * 
	 * public static CustomController instance; public static List<Player>
	 * openPlayers = new ArrayList<Player>(); private Inventory infoMenuInv;
	 * private Queue<GameChangeEvent> queue = Queues.newLinkedBlockingQueue();
	 * private HashMap<Integer, String> gamePlayers = Maps.newHashMap(); private
	 * HashMap<Integer, String> gameStates = Maps.newHashMap();
	 * 
	 * 
	 * public static CustomController get() { if(instance == null) { instance =
	 * new CustomController(); } return instance; }
	 * 
	 * public CustomController() {
	 * 
	 * 
	 * 
	 * ItemStack stack = new ItemStack(Material.GRASS,1); // menu.setOption(0,
	 * new It, name, info) }
	 * 
	 * public Queue<GameChangeEvent> getEventsQueue() { return queue; }
	 * 
	 * public void addEvent(GameChangeEvent e) { queue.add(e); }
	 * 
	 * 
	 * public enum ChangeType { PlayerLeft, PlayerJoin, SpectateLeft,
	 * SpectateJoin, GameStarts, GameEnds
	 * 
	 * }
	 * 
	 * public void setIndicator(int pos, ItemStack item) {
	 * infoMenuInv.setItem(pos, item); }
	 * 
	 * @EventHandler public void onGameChange(GameChangeEvent e) {
	 * 
	 * final Game game = e.getGame(); int id = e.getData();
	 * 
	 * switch (e.getChangeType()) { case GameStarts: case GameEnds:
	 * 
	 * ItemStack[] shiftingGames = new
	 * ItemStack[GameController.get().getAll().size()-1]; ItemStack[] icons =
	 * infoMenuInv.getContents(); for(int ii = id; ii <
	 * infoMenuInv.getContents().length; ) { infoMenuInv.setItem(ii,
	 * infoMenuInv.getItem(++ii)); }
	 * 
	 * // Add or Remove Game // moves Items By One
	 * 
	 * 
	 * 
	 * break; default: // Change Indicator
	 * 
	 * setIndicator(key, pos, item); } }
	 * 
	 * public ItemStack getItemDescription(int n, Game g) { ItemStack showItem =
	 * new ItemStack(Material.BOW); ItemMeta im = showItem.getItemMeta();
	 * im.setDisplayName("Skywars Game #"+n); im.setLore(getIconGameLore(g));
	 * 
	 * showItem.setItemMeta(im); return showItem; }
	 * 
	 * public List<String> getIconGameLore(Game g) {
	 * 
	 * List<String> lore = new ArrayList<>();
	 * 
	 * lore.add("§4§lState §7>>  §c"+g.getState().toString()); // Add Players
	 * lore.add("§4§l(Players)"); for(GamePlayer p : g.getPlayers()) {
	 * lore.add(p.getBukkitPlayer().getName()); } // Add Spectators
	 * lore.add("§4§l(Spectators)"); for(GamePlayer p : g.getAllSpectators()) {
	 * lore.add(p.getBukkitPlayer().getName()); }
	 * 
	 * return lore; }
	 * 
	 * private void updateInfoIconMenu() {
	 * 
	 * int gameIndicator;
	 * 
	 * ItemStack stack = getItemDescription(game);
	 * 
	 * setIndicator()
	 * 
	 * }
	 * 
	 * public class GameChangeEvent {
	 * 
	 * 
	 * private ChangeType type; private Player player; private String msg;
	 * private int data; private Game game;
	 * 
	 * public GameChangeEvent(ChangeType type, Player player, String msg, Game
	 * game, int data) { this.type = type; this.player = player; this.msg = msg;
	 * this.data = data; this.game = game; }
	 * 
	 * public ChangeType getChangeType() { return type; }
	 * 
	 * public Player getPlayer() { return player; }
	 * 
	 * public String getMessage() { return msg; }
	 * 
	 * public int getData() { return data; }
	 * 
	 * public Game getGame() { return game; }
	 * 
	 * }
	 */
	
	static public boolean checkValidInt(String s) {
		try {
			Integer.parseInt(s);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public static List<Game> getGames() {
		return (List<Game>) GameController.get().getAll();
	}
	

	static public GameInfo getAllGameInfo(Game game, int id) {
		if (id == -1) {
			id = GameController.get().getGameId(game);
		}
		String mName = SchematicController.get().getName(game.getSchematic());
		return new GameInfo(id, game.getState(),
				(List<GamePlayer>) game.getPlayers(), game.getAllSpectators(),
				mName);

	}

	static public GameInfo getAllGameInfo(int gId) {
		return getAllGameInfo(
				((List<Game>) GameController.get().getAll()).get(gId), gId);
	}

	static public GameInfo getAllGameInfo(GamePlayer gPlayer) {
		if(gPlayer.isPlaying())
			return getAllGameInfo(gPlayer.getGame(), -1);
		else 
			return new GameInfo(-1);
	}

	static public GameInfo getGameInfo(GamePlayer player) {
		if(player.isPlaying())
			return getGameInfo(player.getGame(), -1);
		else 
			return new GameInfo(-1);
	}

	static public GameInfo getGameInfo(int gId) {
		return getGameInfo(
				((List<Game>) GameController.get().getAll()).get(gId), gId);
	}

	static public GameInfo getGameInfo(Game game, int id) {
		if (id == -1) {
			id = GameController.get().getGameId(game);
		}
		return new GameInfo(id);
	}

	static public GameInfo getPlayersFromGame(int id) {
		return getPlayersFromGame(
				((List<Game>) GameController.get().getAll()).get(id), id);
	}

	static public GameInfo getPlayersFromGame(GamePlayer player) {
		if(player.isPlaying())
			return getPlayersFromGame(player.getGame(), -1);
		else
			return new GameInfo(-1);
	}

	static public GameInfo getPlayersFromGame(Game game, int id) {
		if (id == -1) {
			id = GameController.get().getGameId(game);
		}
		GameInfo info = new GameInfo(id);
		info.setPlayers((List<GamePlayer>)game.getPlayers());
		return info;
	}
	
	static public GameInfo getSpectatorsFromGame(int id) {
		return getSpectatorsFromGame(
				((List<Game>) GameController.get().getAll()).get(id), id);
	}

	static public GameInfo getSpectatorsFromGame(GamePlayer player) {
		if(player.isPlaying())
			return getSpectatorsFromGame(player.getGame(), -1);
		else
			return new GameInfo(-1);
	}

	static public GameInfo getSpectatorsFromGame(Game game, int id) {
		if (id == -1) {
			id = GameController.get().getGameId(game);
		}
		GameInfo info = new GameInfo(id);
		info.setSpectators(game.getAllSpectators());
		return info;
	}
	
	static public boolean validMap(String mapname) {
		
		return SchematicController.get().schematicExists(mapname);
	}
}
