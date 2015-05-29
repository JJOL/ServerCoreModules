/*  1:   */ package me.deathhaven.skywars.listeners;
/*  2:   */ 
/*  3:   */ import me.deathhaven.skywars.controllers.PlayerController;
/*  4:   */ import me.deathhaven.skywars.game.Game;
/*  5:   */ import me.deathhaven.skywars.game.GameState;
/*  6:   */ import me.deathhaven.skywars.player.GamePlayer;
/*  7:   */ import org.bukkit.entity.Player;
/*  8:   */ import org.bukkit.event.EventHandler;
/*  9:   */ import org.bukkit.event.Listener;
/* 10:   */ import org.bukkit.event.block.BlockBreakEvent;
/* 11:   */ import org.bukkit.event.block.BlockPlaceEvent;
/* 12:   */ 
/* 13:   */ public class BlockListener
/* 14:   */   implements Listener
/* 15:   */ {
/* 16:   */   @EventHandler(ignoreCancelled=true)
/* 17:   */   public void onBlockPlace(BlockPlaceEvent event)
/* 18:   */   {
/* 19:17 */     Player player = event.getPlayer();
/* 20:18 */     GamePlayer gamePlayer = PlayerController.get().get(player);
/* 21:20 */     if ((gamePlayer.isPlaying()) && (gamePlayer.getGame().getState() == GameState.WAITING)) {
/* 22:21 */       event.setCancelled(true);
/* 23:   */     }
/* 24:   */   }
/* 25:   */   
/* 26:   */   @EventHandler(ignoreCancelled=true)
/* 27:   */   public void onBlockBreak(BlockBreakEvent event)
/* 28:   */   {
/* 29:27 */     Player player = event.getPlayer();
/* 30:28 */     GamePlayer gamePlayer = PlayerController.get().get(player);
/* 31:30 */     if ((gamePlayer.isPlaying()) && (gamePlayer.getGame().getState() == GameState.WAITING)) {
/* 32:31 */       event.setCancelled(true);
/* 33:   */     }
/* 34:   */   }
/* 35:   */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.listeners.BlockListener
 * JD-Core Version:    0.7.0.1
 */