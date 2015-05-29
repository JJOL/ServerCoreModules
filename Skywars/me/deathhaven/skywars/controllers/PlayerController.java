/*  1:   */ package me.deathhaven.skywars.controllers;
/*  2:   */ 
/*  3:   */ import com.google.common.collect.Maps;
/*  4:   */ import java.util.Collection;
/*  5:   */ import java.util.Map;
/*  6:   */ import javax.annotation.Nonnull;
/*  7:   */ import me.deathhaven.skywars.player.GamePlayer;
/*  8:   */ import me.deathhaven.skywars.storage.DataStorage;
/*  9:   */ import org.bukkit.Bukkit;
/* 10:   */ import org.bukkit.entity.Player;
/* 11:   */ 
/* 12:   */ public class PlayerController
/* 13:   */ {
/* 14:18 */   private final Map<Player, GamePlayer> playerRegistry = Maps.newHashMap();
/* 15:   */   private static PlayerController instance;
/* 16:   */   
/* 17:   */   private PlayerController()
/* 18:   */   {
/* 19:21 */     for (Player player : Bukkit.getOnlinePlayers()) {
/* 20:22 */       register(player);
/* 21:   */     }
/* 22:   */   }
/* 23:   */   
/* 24:   */   public GamePlayer register(@Nonnull Player bukkitPlayer)
/* 25:   */   {
/* 26:27 */     GamePlayer gamePlayer = null;
/* 27:29 */     if (!this.playerRegistry.containsKey(bukkitPlayer))
/* 28:   */     {
/* 29:30 */       gamePlayer = new GamePlayer(bukkitPlayer);
/* 30:31 */       this.playerRegistry.put(bukkitPlayer, gamePlayer);
/* 31:   */     }
/* 32:35 */     return gamePlayer;
/* 33:   */   }
/* 34:   */   
/* 35:   */   public GamePlayer unregister(@Nonnull Player bukkitPlayer)
/* 36:   */   {
/* 37:39 */     return (GamePlayer)this.playerRegistry.remove(bukkitPlayer);
/* 38:   */   }
/* 39:   */   
/* 40:   */   public GamePlayer get(@Nonnull Player bukkitPlayer)
/* 41:   */   {
/* 42:43 */     return (GamePlayer)this.playerRegistry.get(bukkitPlayer);
/* 43:   */   }
/* 44:   */   
/* 45:   */   public Collection<GamePlayer> getAll()
/* 46:   */   {
/* 47:47 */     return this.playerRegistry.values();
/* 48:   */   }
/* 49:   */   
/* 50:   */   public void shutdown()
/* 51:   */   {
/* 52:51 */     for (GamePlayer gamePlayer : this.playerRegistry.values()) {
/* 53:52 */       DataStorage.get().savePlayer(gamePlayer);
/* 54:   */     }
/* 55:55 */     this.playerRegistry.clear();
/* 56:   */   }
/* 57:   */   
/* 58:   */   public static PlayerController get()
/* 59:   */   {
/* 60:61 */     if (instance == null) {
/* 61:62 */       instance = new PlayerController();
/* 62:   */     }
/* 63:65 */     return instance;
/* 64:   */   }
/* 65:   */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.controllers.PlayerController
 * JD-Core Version:    0.7.0.1
 */