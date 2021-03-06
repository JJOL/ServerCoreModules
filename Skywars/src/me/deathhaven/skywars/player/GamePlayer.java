package me.deathhaven.skywars.player;

import me.deathhaven.skywars.SkyWars;
import me.deathhaven.skywars.config.PluginConfig;
import me.deathhaven.skywars.game.Game;
import me.deathhaven.skywars.storage.DataStorage;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GamePlayer {

    private final Player bukkitPlayer;
    private final String playerName;
    private Game game;
    private boolean chosenKit;
    private boolean spectating;
    private int score;
    private int gamesPlayed;
    private int gamesWon;
    private int kills;
    private int deaths;
    private boolean skipFallDamage;
    private boolean skipFireTicks;
    private ItemStack[] savedInventoryContents = null;
    private ItemStack[] savedArmorContents = null;
    private boolean hasSpectatingAccess;

    public GamePlayer(Player bukkitPlayer) {
        this.bukkitPlayer = bukkitPlayer;
        this.playerName = bukkitPlayer.getName();
        this.spectating = false;
        this.hasSpectatingAccess = false;
        DataStorage.get().loadPlayer(this);
    }

    public GamePlayer(String playerName) {
        this.bukkitPlayer = null;
        this.playerName = playerName;
        DataStorage.get().loadPlayer(this);
    }

    public void save() {
        DataStorage.get().savePlayer(this);
    }

    public Player getBukkitPlayer() {
        return bukkitPlayer;
    }

    public boolean isPlaying() {
        return (game != null );
    }
    
    public boolean isSpectating() {
    	return spectating;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public boolean hasChosenKit() {
        return chosenKit;
    }

    public void setChosenKit(boolean yes) {
        chosenKit = yes;
    }
    
    public void setSpectating(boolean s) {
    	spectating = s;
    }

    public int getScore() {
        if (PluginConfig.useEconomy() && SkyWars.getEconomy() != null) {
            return (int) SkyWars.getEconomy().getBalance(playerName);
        }

        return score;
    }

    public void setScore(int score) {
        if (PluginConfig.useEconomy() && SkyWars.getEconomy() != null) {
            double balance = SkyWars.getEconomy().getBalance(playerName);
            if (balance < 0) {
                SkyWars.getEconomy().depositPlayer(playerName, -balance);
            } else {
                SkyWars.getEconomy().withdrawPlayer(playerName, balance);
            }
            addScore(score);

        } else {
            this.score = score;
        }
    }

    public void addScore(int score) {
        if (PluginConfig.useEconomy() && SkyWars.getEconomy() != null) {
            if (score < 0) {
                SkyWars.getEconomy().withdrawPlayer(playerName, -score);
            } else {
                SkyWars.getEconomy().depositPlayer(playerName, score);
            }

        } else {
            this.score += score;
        }
    }

    @Override
    public String toString() {
        return playerName;
    }

    public String getName() {
        return playerName;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setSkipFallDamage(boolean skipFallDamage) {
        this.skipFallDamage = skipFallDamage;
    }

    public void setSkipFireTicks(boolean skipFireTicks) {
        this.skipFireTicks = skipFireTicks;
    }

    public boolean shouldSkipFallDamage() {
        return skipFallDamage;
    }

    public boolean shouldSkipFireTicks() {
        return skipFireTicks;
    }

    public void saveCurrentState() {
        savedArmorContents = bukkitPlayer.getInventory().getArmorContents().clone();
        savedInventoryContents = bukkitPlayer.getInventory().getContents().clone();
    }

    @SuppressWarnings("deprecation")
    public void restoreState() {
        boolean shouldUpdateInventory = false;

        if (savedArmorContents != null) {
            bukkitPlayer.getInventory().setArmorContents(savedArmorContents);
            savedArmorContents = null;
            shouldUpdateInventory = true;
        }

        if (savedInventoryContents != null) {
            bukkitPlayer.getInventory().setContents(savedInventoryContents);
            savedInventoryContents = null;
            shouldUpdateInventory = true;
        }

        if (shouldUpdateInventory) {
            bukkitPlayer.updateInventory();
        }
    }
    
    public boolean hasSpectatingAccess() {
    	return hasSpectatingAccess;
    }
    
    public void setHasSpectatingAccess(boolean b) {
    	hasSpectatingAccess = b;
    }
    
    public void toggleSpectatingAccess() {
    	setHasSpectatingAccess(!hasSpectatingAccess());
    }
    
    
}
