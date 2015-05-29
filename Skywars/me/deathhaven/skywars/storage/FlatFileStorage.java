/*  1:   */ package me.deathhaven.skywars.storage;
/*  2:   */ 
/*  3:   */ import java.io.File;
/*  4:   */ import java.io.IOException;
/*  5:   */ import java.io.PrintStream;
/*  6:   */ import javax.annotation.Nonnull;
/*  7:   */ import me.deathhaven.skywars.SkyWars;
/*  8:   */ import me.deathhaven.skywars.config.PluginConfig;
/*  9:   */ import me.deathhaven.skywars.player.GamePlayer;
/* 10:   */ import org.bukkit.configuration.file.FileConfiguration;
/* 11:   */ import org.bukkit.configuration.file.YamlConfiguration;
/* 12:   */ 
/* 13:   */ public class FlatFileStorage
/* 14:   */   extends DataStorage
/* 15:   */ {
/* 16:   */   public void loadPlayer(@Nonnull GamePlayer player)
/* 17:   */   {
/* 18:   */     try
/* 19:   */     {
/* 20:20 */       File dataDirectory = SkyWars.get().getDataFolder();
/* 21:21 */       File playerDataDirectory = new File(dataDirectory, "player_data");
/* 22:23 */       if ((!playerDataDirectory.exists()) && (!playerDataDirectory.mkdirs()))
/* 23:   */       {
/* 24:24 */         System.out.println("Failed to load player " + player + ": Could not create player_data directory.");
/* 25:25 */         return;
/* 26:   */       }
/* 27:28 */       File playerFile = new File(playerDataDirectory, player + ".yml");
/* 28:29 */       if ((!playerFile.exists()) && (!playerFile.createNewFile()))
/* 29:   */       {
/* 30:30 */         System.out.println("Failed to load player " + player + ": Could not create player file.");
/* 31:31 */         return;
/* 32:   */       }
/* 33:34 */       FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(playerFile);
/* 34:36 */       if ((!PluginConfig.useEconomy()) || (SkyWars.getEconomy() == null)) {
/* 35:37 */         player.setScore(fileConfiguration.getInt("score", 0));
/* 36:   */       }
/* 37:39 */       player.setGamesWon(fileConfiguration.getInt("wins", 0));
/* 38:40 */       player.setGamesPlayed(fileConfiguration.getInt("played", 0));
/* 39:41 */       player.setKills(fileConfiguration.getInt("kills", 0));
/* 40:42 */       player.setDeaths(fileConfiguration.getInt("deaths", 0));
/* 41:   */     }
/* 42:   */     catch (IOException ioException)
/* 43:   */     {
/* 44:45 */       System.out.println("Failed to load player " + player + ": " + ioException.getMessage());
/* 45:   */     }
/* 46:   */   }
/* 47:   */   
/* 48:   */   public void savePlayer(@Nonnull GamePlayer player)
/* 49:   */   {
/* 50:   */     try
/* 51:   */     {
/* 52:52 */       File dataDirectory = SkyWars.get().getDataFolder();
/* 53:53 */       File playerDataDirectory = new File(dataDirectory, "player_data");
/* 54:55 */       if ((!playerDataDirectory.exists()) && (!playerDataDirectory.mkdirs()))
/* 55:   */       {
/* 56:56 */         System.out.println("Failed to save player " + player + ": Could not create player_data directory.");
/* 57:57 */         return;
/* 58:   */       }
/* 59:60 */       File playerFile = new File(playerDataDirectory, player + ".yml");
/* 60:61 */       if ((!playerFile.exists()) && (!playerFile.createNewFile()))
/* 61:   */       {
/* 62:62 */         System.out.println("Failed to save player " + player + ": Could not create player file.");
/* 63:63 */         return;
/* 64:   */       }
/* 65:66 */       FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(playerFile);
/* 66:67 */       fileConfiguration.set("score", Integer.valueOf(player.getScore()));
/* 67:68 */       fileConfiguration.set("wins", Integer.valueOf(player.getGamesWon()));
/* 68:69 */       fileConfiguration.set("played", Integer.valueOf(player.getGamesPlayed()));
/* 69:70 */       fileConfiguration.set("deaths", Integer.valueOf(player.getDeaths()));
/* 70:71 */       fileConfiguration.set("kills", Integer.valueOf(player.getKills()));
/* 71:72 */       fileConfiguration.save(playerFile);
/* 72:   */     }
/* 73:   */     catch (IOException ioException)
/* 74:   */     {
/* 75:75 */       System.out.println("Failed to save player " + player + ": " + ioException.getMessage());
/* 76:   */     }
/* 77:   */   }
/* 78:   */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.storage.FlatFileStorage
 * JD-Core Version:    0.7.0.1
 */