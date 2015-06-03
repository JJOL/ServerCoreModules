package me.deathhaven.skywars.game;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import me.deathhaven.skywars.SkyWars;
import me.deathhaven.skywars.config.PluginConfig;
import me.deathhaven.skywars.controllers.DataController;
import me.deathhaven.skywars.controllers.GameController;
import me.deathhaven.skywars.controllers.IconMenuController;
import me.deathhaven.skywars.controllers.KitController;
import me.deathhaven.skywars.controllers.PlayerController;
import me.deathhaven.skywars.controllers.SchematicController;
import me.deathhaven.skywars.controllers.WorldController;
import me.deathhaven.skywars.player.GamePlayer;
import me.deathhaven.skywars.utilities.CraftBukkitUtil;
import me.deathhaven.skywars.utilities.Debugger;
import me.deathhaven.skywars.utilities.Messaging;
import me.deathhaven.skywars.utilities.PlayerUtil;
import me.deathhaven.skywars.utilities.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sk89q.worldedit.CuboidClipboard;

public class Game {

    private GameState gameState;
    private Map<Integer, GamePlayer> idPlayerMap = Maps.newLinkedHashMap();
    private Map<GamePlayer, Integer> playerIdMap = Maps.newHashMap();
    private List<GamePlayer> spectators = Lists.newArrayList(); 
    private int spectatorsCount = 0;
    private int playerCount = 0;
    private int playerVotes = 0;
    private HashSet<String> voted = new HashSet<String>();
    private int slots;
    private Map<Integer, Location> spawnPlaces = Maps.newHashMap();
    private int timer;
    private boolean canStart;
    private Scoreboard scoreboard;
    private Objective objective;
    private boolean built;

    private CuboidClipboard schematic;
    private World world;
    private int[] islandReference;
    private Vector minLoc;
    private Vector maxLoc;
    private List<Location> chestList = Lists.newArrayList();

    public Game(CuboidClipboard schematic) {
        this.schematic = schematic;
        world = WorldController.get().create(this, schematic);
        slots = spawnPlaces.size();
        gameState = GameState.WAITING;
        canStart = false;

        for (int iii = 0; iii < slots; iii++) {
            idPlayerMap.put(iii, null);
        }
        
        //scoreboard = null;
    }

    public boolean isBuilt() {
        return built;
    }

    public void setBuilt(boolean built) {
        this.built = built;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }
    
    public void setCanStart(boolean b) {
    	canStart = b;
    }

    public void addChest(Location location) {
        chestList.add(location);
    }

    public void removeChest(Location location) {
        chestList.remove(location);
    }

    public boolean isChest(Location location) {
        return chestList.contains(location);
    }

    public boolean isReady() {
        return slots >= 2;
    }

    public World getWorld() {
        return world;
    }
    
    public CuboidClipboard getSchematic() {
    	return schematic;
    }

    public void setGridReference(int[] gridReference) {
        islandReference = gridReference;
    }

    public int[] getGridReference() {
        return islandReference;
    }

    public void setLocation(int originX, int originZ) {
        int minX = originX + schematic.getOffset().getBlockX() + 1;
        int minZ = originZ + schematic.getOffset().getBlockZ() + 1;
        int maxX = minX + schematic.getWidth() - 2;
        int maxZ = minZ + schematic.getLength() - 2;
        int buffer = PluginConfig.getIslandBuffer();
        minLoc = new Vector(minX - buffer, Integer.MIN_VALUE, minZ - buffer);
        maxLoc = new Vector(maxX + buffer, Integer.MAX_VALUE, maxZ + buffer);
    }

    public Vector getMinLoc() {
        return minLoc;
    }

    public Vector getMaxLoc() {
        return maxLoc;
    }

	public void onPlayerJoin(GamePlayer gamePlayer) {
        Player player = gamePlayer.getBukkitPlayer();

        int id = getFistEmpty();
        playerCount++;
        idPlayerMap.put(getFistEmpty(), gamePlayer);
        playerIdMap.put(gamePlayer, id);

        sendMessage(new Messaging.MessageFormatter()
                .withPrefix()
                .setVariable("player", player.getDisplayName())
                .setVariable("amount", String.valueOf(getPlayerCount()))
                .setVariable("slots", String.valueOf(slots))
                .format("game.join"));

        if (getMinimumPlayers() - playerCount > 0) {
            sendMessage(new Messaging.MessageFormatter()
                    .withPrefix()
                    .setVariable("amount", String.valueOf(getMinimumPlayers() - playerCount))
                    .format("game.required"));
        }

        PlayerUtil.refreshPlayer(player);

        if (PluginConfig.saveInventory()) {
            gamePlayer.saveCurrentState();
        }

        if (PluginConfig.clearInventory()) {
            PlayerUtil.clearInventory(player);
        }

        if (player.getGameMode() != GameMode.SURVIVAL) {
            player.setGameMode(GameMode.SURVIVAL);
        }

        gamePlayer.setChosenKit(false);
        gamePlayer.setSkipFallDamage(true);
        player.teleport(getSpawn(id).clone().add(0.5, 0.5, 0.5));
        gamePlayer.setGame(this);

        // Make sure GodMode is disabled. This should cover CommandBook and WorldGuard
        Plugin commandBook = SkyWars.get().getServer().getPluginManager().getPlugin("CommandBook");
        Plugin worldGuard = SkyWars.get().getServer().getPluginManager().getPlugin("WorldGuard");
        if (player.hasMetadata("god")) {
            if (commandBook != null && commandBook instanceof com.sk89q.commandbook.CommandBook) {
                player.removeMetadata("god", commandBook);
            }
            if (worldGuard != null && worldGuard instanceof com.sk89q.worldguard.bukkit.WorldGuardPlugin) {
                player.removeMetadata("god", worldGuard);
            }
        }

        if (!PluginConfig.disableKits()) {
            KitController.get().openKitMenu(gamePlayer);
        }

        if (!PluginConfig.buildSchematic()) {
            timer = getTimer();
        }
    }
    
    public void onSpectatorJoin(GamePlayer player) {
    	
    	spectators.add(player);
    	//bPlayer.setFlying(true);
    	player.setGame(this);
    	player.setSpectating(true);
    	spawnSpectator(player);
    }
    
    public void spawnSpectator(GamePlayer gPlayer) {
    	Player player = gPlayer.getBukkitPlayer();
    	player.setGameMode(GameMode.SPECTATOR);
    	player.teleport(getSpawn(0).clone().add(0.5, 0.5, 0.5));
    }
    
    public void onSpectatorLeave(GamePlayer player) {
    	
    	spectators.remove(player);
    	Player p = player.getBukkitPlayer();
    	PlayerUtil.refreshPlayer(p);
        PlayerUtil.clearInventory(p);

        player.setSpectating(false);
        player.setGame(null);
        p.teleport(PluginConfig.getLobbySpawn());
        p.setGameMode(GameMode.SURVIVAL);
    }

    public void onPlayerLeave(GamePlayer gamePlayer) {
        onPlayerLeave(gamePlayer, true, true, true);
    }

    public void onPlayerLeave(final GamePlayer gamePlayer, boolean displayText, boolean process, boolean left) {
        Player player = gamePlayer.getBukkitPlayer();
        playerCount--;
        IconMenuController.get().destroy(player);

        if (displayText) {
            if (left && gameState == GameState.PLAYING) {
                int scorePerLeave = PluginConfig.getScorePerLeave(player);
                gamePlayer.addScore(scorePerLeave);

                sendMessage(new Messaging.MessageFormatter()
                        .withPrefix()
                        .setVariable("player", player.getDisplayName())
                        .setVariable("score", StringUtils.formatScore(scorePerLeave, Messaging.getInstance().getMessage("score.naming")))
                        .format("game.quit.playing"));

            } else {
                sendMessage(new Messaging.MessageFormatter()
                        .withPrefix()
                        .setVariable("player", player.getDisplayName())
                        .setVariable("total", String.valueOf(getPlayerCount()))
                        .setVariable("slots", String.valueOf(slots))
                        .format("game.quit.other"));
            }
        }

        if (scoreboard != null) {
            objective.getScore(player).setScore(-playerCount);
            try {
                player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
            } catch (IllegalStateException ignored) {

            }
        }

        if (player.isDead()) {
            CraftBukkitUtil.forceRespawn(player);
            gamePlayer.setGame(null);
        } else {
            PlayerUtil.refreshPlayer(player);
            PlayerUtil.clearInventory(player);

            gamePlayer.setGame(null);
            player.teleport(PluginConfig.getLobbySpawn());

            if (PluginConfig.saveInventory()) {
                gamePlayer.restoreState();
            }
        }
        idPlayerMap.put(playerIdMap.remove(gamePlayer), null);
        gamePlayer.setChosenKit(false);

        if (process && gameState == GameState.PLAYING && playerCount == 1) {
            onGameEnd(getWinner());
        }
    }

    public void onPlayerDeath(final GamePlayer gamePlayer, PlayerDeathEvent event) {
        final Player player = gamePlayer.getBukkitPlayer();
        Player killer = player.getKiller();

        int scorePerDeath = PluginConfig.getScorePerDeath(player);
        gamePlayer.addScore(scorePerDeath);
        gamePlayer.setDeaths(gamePlayer.getDeaths() + 1);
        
        GamePlayer gameKiller = null;
        if (killer != null) {
            gameKiller = PlayerController.get().get(killer);
        }

        if (gameKiller != null) {
            int scorePerKill = PluginConfig.getScorePerKill(killer);
            gameKiller.addScore(scorePerKill);
            gameKiller.setKills(gameKiller.getKills() + 1);

            sendMessage(new Messaging.MessageFormatter()
                    .withPrefix()
                    .setVariable("player", player.getDisplayName())
                    .setVariable("killer", killer.getDisplayName())
                    .setVariable("player_score", StringUtils.formatScore(scorePerDeath, Messaging.getInstance().getMessage("score.naming")))
                    .setVariable("killer_score", StringUtils.formatScore(scorePerKill, Messaging.getInstance().getMessage("score.naming")))
                    .format("game.kill"));

        } else {
            sendMessage(new Messaging.MessageFormatter()
                    .withPrefix()
                    .setVariable("player", player.getDisplayName())
                    .setVariable("score", StringUtils.formatScore(scorePerDeath, Messaging.getInstance().getMessage("score.naming")))
                    .format("game.death"));
        }

        sendMessage(new Messaging.MessageFormatter()
                .withPrefix()
                .setVariable("remaining", String.valueOf(playerCount - 1))
                .format("game.remaining"));

        for (GamePlayer gp : getPlayers()) {
            if (gp.equals(gamePlayer)) {
                gp.getBukkitPlayer().sendMessage(new Messaging.MessageFormatter().withPrefix().format("game.eliminated.self"));

            } else {
                gp.getBukkitPlayer().sendMessage(new Messaging.MessageFormatter()
                        .withPrefix()
                        .setVariable("player", player.getDisplayName())
                        .format("game.eliminated.others"));
            }
        }

        if (event != null) {
            Location location = player.getLocation().clone();
            World world = location.getWorld();

            for (ItemStack itemStack : event.getDrops()) {
                world.dropItemNaturally(location, itemStack);
            }

            world.spawn(location, ExperienceOrb.class).setExperience(event.getDroppedExp());

            event.setDeathMessage(null);
            event.getDrops().clear();
            event.setDroppedExp(0);

            onPlayerLeave(gamePlayer, false, true, false);

        } else {
            onPlayerLeave(gamePlayer, false, true, false);
        }
    }

    public void onGameStart() {
        registerScoreboard();
        gameState = GameState.PLAYING;

        for (Map.Entry<Integer, GamePlayer> playerEntry : idPlayerMap.entrySet()) {
            GamePlayer gamePlayer = playerEntry.getValue();

            if (gamePlayer != null) {
                objective.getScore(gamePlayer.getBukkitPlayer()).setScore(0);
                IconMenuController.get().destroy(gamePlayer.getBukkitPlayer());
                getSpawn(playerEntry.getKey()).clone().add(0, -1D, 0).getBlock().setType(Material.AIR);
                gamePlayer.setGamesPlayed(gamePlayer.getGamesPlayed() + 1);
            }
        }

        for (GamePlayer gamePlayer : getPlayers()) {
            gamePlayer.getBukkitPlayer().setHealth(20D);
            gamePlayer.getBukkitPlayer().setFoodLevel(20);
            gamePlayer.getBukkitPlayer().setSaturation(20f);
            
            gamePlayer.getBukkitPlayer().setScoreboard(scoreboard);
            gamePlayer.getBukkitPlayer().sendMessage(new Messaging.MessageFormatter().withPrefix().format("game.start"));
        }
    }

    public void onGameEnd() {
        onGameEnd(null);
    }

    public void onGameEnd(GamePlayer gamePlayer) {
        if (gamePlayer != null) {
            Player player = gamePlayer.getBukkitPlayer();
            int score = PluginConfig.getScorePerWin(player);
            gamePlayer.addScore(score);
            gamePlayer.setGamesWon(gamePlayer.getGamesWon() + 1);

            Bukkit.broadcastMessage(new Messaging.MessageFormatter()
                    .withPrefix()
                    .setVariable("player", player.getDisplayName())
                    .setVariable("score", String.valueOf(score))
                    .setVariable("map", SchematicController.get().getName(schematic))
                    .format("game.win"));
        }

        for (GamePlayer player : getPlayers()) {
            onPlayerLeave(player, false, false, false);
        }
        for(GamePlayer player : getAllSpectators()) {
        	onSpectatorLeave(player);
        }

        gameState = GameState.ENDING;
        unregisterScoreboard();

        Debugger.get().sendDebbugMC("[Game:onGameEnd] Game Is Ending with Id: "+DataController.getGameInfo(this, -1).gId);
        if(gamePlayer != null)
        	Debugger.get().sendDebbugMC("[Game:onGameEnd] Player Winner: "+gamePlayer.getBukkitPlayer().getName());
        GameController.get().remove(this);
        
    }

    public void onTick() {
    	// 20 - 2 players
        /*if (timer <= 0 || 
        		(gameState == GameState.WAITING && !hasReachedMinimumPlayers())) {
            return;
        }*/
    	
    	if (timer <= 0 || 
    			(gameState==GameState.WAITING && !hasReachedMinimumPlayers() && !canStart)) {
    		return;
    	}

        timer--;

        switch (gameState) {
            case WAITING:
                if (timer == 0) {
                	if(getPlayers().size() < 2)
                		return;
                    onGameStart();
                } else if (timer % 10 == 0 || timer <= 5) {
                    if (PluginConfig.enableSounds() && timer <= 3) {
                        for (GamePlayer gamePlayer : idPlayerMap.values()) {
                            if (gamePlayer == null) {
                                continue;
                            }
                            Player player = gamePlayer.getBukkitPlayer();
                            if (player == null) {
                                continue;
                            }
                            player.getWorld().playSound(player.getLocation(),
                                    Sound.SUCCESSFUL_HIT, 10, 1);
                        }
                    }
                    sendMessage(new Messaging.MessageFormatter()
                            .withPrefix()
                            .setVariable("timer", String.valueOf(timer))
                            .format("game.countdown"));
                }
                break;

            default:
                break;
        }
    }

    public GameState getState() {
        return gameState;
    }
    public int getPlayersCount() {
    	return playerCount;
    }
    public Map<Integer, GamePlayer> getAllPlayers() {
    	return idPlayerMap;
    }
    public int getSpectarorCount() {
    	return spectatorsCount;
    }
    public List<GamePlayer> getAllSpectators() {
    	return spectators;
    }
    
    public boolean canStart() {
    	return canStart;
    }

    public boolean isFull() {
        return getPlayerCount() == slots;
    }

    public int getMinimumPlayers() {
        FileConfiguration config = SkyWars.get().getConfig();
        String schematicName = SchematicController.get().getName(schematic);

        return config.getInt("schematics." + schematicName + ".min-players", slots);
    }

    private int getTimer() {
        FileConfiguration config = SkyWars.get().getConfig();
        String schematicName = SchematicController.get().getName(schematic);

        return config.getInt("schematics." + schematicName + ".timer", 11);
    }

    public boolean addVote(String name){
        if(!voted.contains(name)){
            playerVotes++;
            checkVotes();
            return true;
        }else{
            return false;
        }
    }
    
    public void checkVotes(){
        if(playerVotes/playerCount*100 >= 75){
            if(getPlayerCount()>1){
            	if (!PluginConfig.buildSchematic()) {
            		canStart = true;
                    timer = getTimer();
                    
                }
            }
        }
    }
    
    public boolean hasReachedMinimumPlayers() {
        return getPlayerCount() >= getMinimumPlayers();
    }

    public void sendMessage(String message) {
        for (GamePlayer gamePlayer : getPlayers()) {
            gamePlayer.getBukkitPlayer().sendMessage(message);
        }
        for(GamePlayer gamePlayer : getAllSpectators()) {
        	gamePlayer.getBukkitPlayer().sendMessage(message);
        }
    }
    
    public void sendSpectatorMessage(String message) {
    	for(GamePlayer gamePlayer : getAllSpectators()) {
        	gamePlayer.getBukkitPlayer().sendMessage(message);
        }
    }

    private GamePlayer getWinner() {
        for (GamePlayer gamePlayer : idPlayerMap.values()) {
            if (gamePlayer == null) {
                continue;
            }
            
            

            return gamePlayer;
        }

        return null;
    }

    private int getFistEmpty() {
        for (Map.Entry<Integer, GamePlayer> playerEntry : idPlayerMap.entrySet()) {
            if (playerEntry.getValue() == null) {
                return playerEntry.getKey();
            }
        }

        return -1;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public Collection<GamePlayer> getPlayers() {
        List<GamePlayer> playerList = Lists.newArrayList();

        for (GamePlayer gamePlayer : idPlayerMap.values()) {
            if (gamePlayer != null) {
                playerList.add(gamePlayer);
            }
        }

        return playerList;
    }

    private Location getSpawn(int id) {
        return spawnPlaces.get(id);
    }

    public void addSpawn(int id, Location location) {
        spawnPlaces.put(id, location);
    }

    private void registerScoreboard() {
        if (scoreboard != null) {
            unregisterScoreboard();
        }

        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = scoreboard.registerNewObjective("info", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("\247c\247lLeaderBoard");
    }

    private void unregisterScoreboard() {
        if (objective != null) {
            objective.unregister();
        }

        if (scoreboard != null) {
            scoreboard = null;
        }
    }
}
