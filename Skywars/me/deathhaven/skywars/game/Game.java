package me.deathhaven.skywars.game;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sk89q.commandbook.CommandBook;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import me.deathhaven.skywars.SkyWars;
import me.deathhaven.skywars.config.PluginConfig;
import me.deathhaven.skywars.controllers.GameController;
import me.deathhaven.skywars.controllers.IconMenuController;
import me.deathhaven.skywars.controllers.KitController;
import me.deathhaven.skywars.controllers.PlayerController;
import me.deathhaven.skywars.controllers.SchematicController;
import me.deathhaven.skywars.controllers.WorldController;
import me.deathhaven.skywars.player.GamePlayer;
import me.deathhaven.skywars.utilities.CraftBukkitUtil;
import me.deathhaven.skywars.utilities.Messaging;
import me.deathhaven.skywars.utilities.Messaging.MessageFormatter;
import me.deathhaven.skywars.utilities.PlayerUtil;
import me.deathhaven.skywars.utilities.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
public class Game
  private GameState gameState;
  private Map<Integer, GamePlayer> idPlayerMap = Maps.newLinkedHashMap();
  private Map<GamePlayer, Integer> playerIdMap = Maps.newHashMap();
  private List<GamePlayer> spectators = Lists.newArrayList();
  private int spectatorsCount = 0;
  private int playerCount = 0;
  private int playerVotes = 0;
  private HashSet<String> voted = new HashSet();
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
  private org.bukkit.util.Vector minLoc;
  private org.bukkit.util.Vector maxLoc;
  private List<Location> chestList = Lists.newArrayList();
  
  public Game(CuboidClipboard schematic)
  {
    this.schematic = schematic;
    this.world = WorldController.get().create(this, schematic);
    this.slots = this.spawnPlaces.size();
    this.gameState = GameState.WAITING;
    this.canStart = false;
    for (int iii = 0; iii < this.slots; iii++) {
      this.idPlayerMap.put(Integer.valueOf(iii), null);
    }
  }
  
  public boolean isBuilt()
  {
    return this.built;
  }
  
  public void setBuilt(boolean built)
  {
    this.built = built;
  }
  
  public void setTimer(int timer)
  {
    this.timer = timer;
  }
  
  public void setCanStart(boolean b)
  {
    this.canStart = b;
  }
  
  public void addChest(Location location)
  {
    this.chestList.add(location);
  }
  
  public void removeChest(Location location)
  {
    this.chestList.remove(location);
  }
  
  public boolean isChest(Location location)
  {
    return this.chestList.contains(location);
  }
  
  public boolean isReady()
  {
    return this.slots >= 2;
  }
  
  public World getWorld()
  {
    return this.world;
  }
  
  public void setGridReference(int[] gridReference)
  {
    this.islandReference = gridReference;
  }
  
  public int[] getGridReference()
  {
    return this.islandReference;
  }
  
  public void setLocation(int originX, int originZ)
  {
    int minX = originX + this.schematic.getOffset().getBlockX() + 1;
    int minZ = originZ + this.schematic.getOffset().getBlockZ() + 1;
    int maxX = minX + this.schematic.getWidth() - 2;
    int maxZ = minZ + this.schematic.getLength() - 2;
    int buffer = PluginConfig.getIslandBuffer();
    this.minLoc = new org.bukkit.util.Vector(minX - buffer, -2147483648, minZ - buffer);
    this.maxLoc = new org.bukkit.util.Vector(maxX + buffer, 2147483647, maxZ + buffer);
  }
  
  public org.bukkit.util.Vector getMinLoc()
  {
    return this.minLoc;
  }
  
  public org.bukkit.util.Vector getMaxLoc()
  {
    return this.maxLoc;
  }
  
  public void onPlayerJoin(GamePlayer gamePlayer)
  {
    Player player = gamePlayer.getBukkitPlayer();
    
    int id = getFistEmpty();
    this.playerCount += 1;
    this.idPlayerMap.put(Integer.valueOf(getFistEmpty()), gamePlayer);
    this.playerIdMap.put(gamePlayer, Integer.valueOf(id));
    
    sendMessage(new Messaging.MessageFormatter()
      .withPrefix()
      .setVariable("player", player.getDisplayName())
      .setVariable("amount", String.valueOf(getPlayerCount()))
      .setVariable("slots", String.valueOf(this.slots))
      .format("game.join"));
    if (getMinimumPlayers() - this.playerCount > 0) {
      sendMessage(new Messaging.MessageFormatter()
        .withPrefix()
        .setVariable("amount", String.valueOf(getMinimumPlayers() - this.playerCount))
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
    player.teleport(getSpawn(id).clone().add(0.5D, 0.5D, 0.5D));
    gamePlayer.setGame(this);
    
    Plugin commandBook = SkyWars.get().getServer().getPluginManager().getPlugin("CommandBook");
    Plugin worldGuard = SkyWars.get().getServer().getPluginManager().getPlugin("WorldGuard");
    if (player.hasMetadata("god"))
    {
      if ((commandBook != null) && ((commandBook instanceof CommandBook))) {
        player.removeMetadata("god", commandBook);
      }
      if ((worldGuard != null) && ((worldGuard instanceof WorldGuardPlugin))) {
        player.removeMetadata("god", worldGuard);
      }
    }
    if (!PluginConfig.disableKits()) {
      KitController.get().openKitMenu(gamePlayer);
    }
    if (!PluginConfig.buildSchematic()) {
      this.timer = getTimer();
    }
  }
  
  public void onSpectatorJoin(GamePlayer player)
  {
    this.spectators.add(player);
    Player bPlayer = player.getBukkitPlayer();
    bPlayer.setFlying(true);
    player.setGame(this);
    player.setSpectating(true);
  }
  
  public void onSpectatorLeave(GamePlayer player)
  {
    this.spectators.remove(player);
  }
  
  public void onPlayerLeave(GamePlayer gamePlayer)
  {
    onPlayerLeave(gamePlayer, true, true, true);
  }
  
  public void onPlayerLeave(GamePlayer gamePlayer, boolean displayText, boolean process, boolean left)
  {
    Player player = gamePlayer.getBukkitPlayer();
    this.playerCount -= 1;
    IconMenuController.get().destroy(player);
    if (displayText) {
      if ((left) && (this.gameState == GameState.PLAYING))
      {
        int scorePerLeave = PluginConfig.getScorePerLeave(player);
        gamePlayer.addScore(scorePerLeave);
        
        sendMessage(new Messaging.MessageFormatter()
          .withPrefix()
          .setVariable("player", player.getDisplayName())
          .setVariable("score", StringUtils.formatScore(scorePerLeave, Messaging.getInstance().getMessage("score.naming")))
          .format("game.quit.playing"));
      }
      else
      {
        sendMessage(new Messaging.MessageFormatter()
          .withPrefix()
          .setVariable("player", player.getDisplayName())
          .setVariable("total", String.valueOf(getPlayerCount()))
          .setVariable("slots", String.valueOf(this.slots))
          .format("game.quit.other"));
      }
    }
    if (this.scoreboard != null)
    {
      this.objective.getScore(player).setScore(-this.playerCount);
      try
      {
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
      }
      catch (IllegalStateException localIllegalStateException) {}
    }
    if (player.isDead())
    {
      CraftBukkitUtil.forceRespawn(player);
      gamePlayer.setGame(null);
    }
    else
    {
      PlayerUtil.refreshPlayer(player);
      PlayerUtil.clearInventory(player);
      
      gamePlayer.setGame(null);
      player.teleport(PluginConfig.getLobbySpawn());
      if (PluginConfig.saveInventory()) {
        gamePlayer.restoreState();
      }
    }
    this.idPlayerMap.put((Integer)this.playerIdMap.remove(gamePlayer), null);
    gamePlayer.setChosenKit(false);
    if ((process) && (this.gameState == GameState.PLAYING) && (this.playerCount == 1)) {
      onGameEnd(getWinner());
    }
  }
  
  public void onPlayerDeath(GamePlayer gamePlayer, PlayerDeathEvent event)
  {
    Player player = gamePlayer.getBukkitPlayer();
    Player killer = player.getKiller();
    
    int scorePerDeath = PluginConfig.getScorePerDeath(player);
    gamePlayer.addScore(scorePerDeath);
    gamePlayer.setDeaths(gamePlayer.getDeaths() + 1);
    
    GamePlayer gameKiller = null;
    if (killer != null) {
      gameKiller = PlayerController.get().get(killer);
    }
    if (gameKiller != null)
    {
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
    }
    else
    {
      sendMessage(new Messaging.MessageFormatter()
        .withPrefix()
        .setVariable("player", player.getDisplayName())
        .setVariable("score", StringUtils.formatScore(scorePerDeath, Messaging.getInstance().getMessage("score.naming")))
        .format("game.death"));
    }
    sendMessage(new Messaging.MessageFormatter()
      .withPrefix()
      .setVariable("remaining", String.valueOf(this.playerCount - 1))
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
    if (event != null)
    {
      Location location = player.getLocation().clone();
      World world = location.getWorld();
      for (ItemStack itemStack : event.getDrops()) {
        world.dropItemNaturally(location, itemStack);
      }
      ((ExperienceOrb)world.spawn(location, ExperienceOrb.class)).setExperience(event.getDroppedExp());
      
      event.setDeathMessage(null);
      event.getDrops().clear();
      event.setDroppedExp(0);
      
      onPlayerLeave(gamePlayer, false, true, false);
    }
    else
    {
      onPlayerLeave(gamePlayer, false, true, false);
    }
  }
  
  public void onGameStart()
  {
    registerScoreboard();
    this.gameState = GameState.PLAYING;
    for (Map.Entry<Integer, GamePlayer> playerEntry : this.idPlayerMap.entrySet())
    {
      GamePlayer gamePlayer = (GamePlayer)playerEntry.getValue();
      if (gamePlayer != null)
      {
        this.objective.getScore(gamePlayer.getBukkitPlayer()).setScore(0);
        IconMenuController.get().destroy(gamePlayer.getBukkitPlayer());
        getSpawn(((Integer)playerEntry.getKey()).intValue()).clone().add(0.0D, -1.0D, 0.0D).getBlock().setType(Material.AIR);
        gamePlayer.setGamesPlayed(gamePlayer.getGamesPlayed() + 1);
      }
    }
    for (GamePlayer gamePlayer : getPlayers())
    {
      gamePlayer.getBukkitPlayer().setHealth(20.0D);
      gamePlayer.getBukkitPlayer().setFoodLevel(20);
      gamePlayer.getBukkitPlayer().setSaturation(20.0F);
      
      gamePlayer.getBukkitPlayer().setScoreboard(this.scoreboard);
      gamePlayer.getBukkitPlayer().sendMessage(new Messaging.MessageFormatter().withPrefix().format("game.start"));
    }
  }
  
  public void onGameEnd()
  {
    onGameEnd(null);
  }
  
  public void onGameEnd(GamePlayer gamePlayer)
  {
    int score;
    if (gamePlayer != null)
    {
      Player player = gamePlayer.getBukkitPlayer();
      score = PluginConfig.getScorePerWin(player);
      gamePlayer.addScore(score);
      gamePlayer.setGamesWon(gamePlayer.getGamesWon() + 1);
      
      Bukkit.broadcastMessage(new Messaging.MessageFormatter()
        .withPrefix()
        .setVariable("player", player.getDisplayName())
        .setVariable("score", String.valueOf(score))
        .setVariable("map", SchematicController.get().getName(this.schematic))
        .format("game.win"));
    }
    for (GamePlayer player : getPlayers()) {
      onPlayerLeave(player, false, false, false);
    }
    this.gameState = GameState.ENDING;
    unregisterScoreboard();
    
    GameController.get().remove(this);
  }
  
  public void onTick()
  {
    if ((this.timer <= 0) || (
      (this.gameState == GameState.WAITING) && (!hasReachedMinimumPlayers()) && (!this.canStart))) {
      return;
    }
    this.timer -= 1;
    switch (this.gameState)
    {
    case ENDING: 
      if (this.timer == 0)
      {
        if (getPlayers().size() < 2) {
          return;
        }
        onGameStart();
      }
      else if ((this.timer % 10 == 0) || (this.timer <= 5))
      {
        if ((PluginConfig.enableSounds()) && (this.timer <= 3)) {
          for (GamePlayer gamePlayer : this.idPlayerMap.values()) {
            if (gamePlayer != null)
            {
              Player player = gamePlayer.getBukkitPlayer();
              if (player != null) {
                player.getWorld().playSound(player.getLocation(), 
                  Sound.SUCCESSFUL_HIT, 10.0F, 1.0F);
              }
            }
          }
        }
        sendMessage(new Messaging.MessageFormatter()
          .withPrefix()
          .setVariable("timer", String.valueOf(this.timer))
          .format("game.countdown"));
      }
      break;
    }
  }
  
  public GameState getState()
  {
    return this.gameState;
  }
  
  public int getPlayersCount()
  {
    return this.playerCount;
  }
  
  public Map<Integer, GamePlayer> getAllPlayers()
  {
    return this.idPlayerMap;
  }
  
  public int getSpectarorCount()
  {
    return this.spectatorsCount;
  }
  
  public List<GamePlayer> getAllSpectators()
  {
    return this.spectators;
  }
  
  public boolean canStart()
  {
    return this.canStart;
  }
  
  public boolean isFull()
  {
    return getPlayerCount() == this.slots;
  }
  
  public int getMinimumPlayers()
  {
    FileConfiguration config = SkyWars.get().getConfig();
    String schematicName = SchematicController.get().getName(this.schematic);
    
    return config.getInt("schematics." + schematicName + ".min-players", this.slots);
  }
  
  private int getTimer()
  {
    FileConfiguration config = SkyWars.get().getConfig();
    String schematicName = SchematicController.get().getName(this.schematic);
    
    return config.getInt("schematics." + schematicName + ".timer", 11);
  }
  
  public boolean addVote(String name)
  {
    if (!this.voted.contains(name))
    {
      this.playerVotes += 1;
      checkVotes();
      return true;
    }
    return false;
  }
  
  public void checkVotes()
  {
    if ((this.playerVotes / this.playerCount * 100 >= 75) && 
      (getPlayerCount() > 1) && 
      (!PluginConfig.buildSchematic()))
    {
      this.canStart = true;
      this.timer = getTimer();
    }
  }
  
  public boolean hasReachedMinimumPlayers()
  {
    return getPlayerCount() >= getMinimumPlayers();
  }
  
  public void sendMessage(String message)
  {
    for (GamePlayer gamePlayer : getPlayers()) {
      gamePlayer.getBukkitPlayer().sendMessage(message);
    }
  }
  
  private GamePlayer getWinner()
  {
    for (GamePlayer gamePlayer : this.idPlayerMap.values()) {
      if (gamePlayer != null) {
        return gamePlayer;
      }
    }
    return null;
  }
  
  private int getFistEmpty()
  {
    for (Map.Entry<Integer, GamePlayer> playerEntry : this.idPlayerMap.entrySet()) {
      if (playerEntry.getValue() == null) {
        return ((Integer)playerEntry.getKey()).intValue();
      }
    }
    return -1;
  }
  
  public int getPlayerCount()
  {
    return this.playerCount;
  }
  
  public Collection<GamePlayer> getPlayers()
  {
    List<GamePlayer> playerList = Lists.newArrayList();
    for (GamePlayer gamePlayer : this.idPlayerMap.values()) {
      if (gamePlayer != null) {
        playerList.add(gamePlayer);
      }
    }
    return playerList;
  }
  
  private Location getSpawn(int id)
  {
    return (Location)this.spawnPlaces.get(Integer.valueOf(id));
  }
  
  public void addSpawn(int id, Location location)
  {
    this.spawnPlaces.put(Integer.valueOf(id), location);
  }
  
  private void registerScoreboard()
  {
    if (this.scoreboard != null) {
      unregisterScoreboard();
    }
    this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    this.objective = this.scoreboard.registerNewObjective("info", "dummy");
    this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    this.objective.setDisplayName("§c§lLeaderBoard");
  }
  
  private void unregisterScoreboard()
  {
    if (this.objective != null) {
      this.objective.unregister();
    }
    if (this.scoreboard != null) {
      this.scoreboard = null;
    }
  }
}