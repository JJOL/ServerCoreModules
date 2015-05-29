/*  1:   */ package me.deathhaven.skywars.listeners;
/*  2:   */ 
/*  3:   */ import me.deathhaven.skywars.SkyWars;
/*  4:   */ import me.deathhaven.skywars.controllers.PlayerController;
/*  5:   */ import me.deathhaven.skywars.game.Game;
/*  6:   */ import me.deathhaven.skywars.game.GameState;
/*  7:   */ import me.deathhaven.skywars.player.GamePlayer;
/*  8:   */ import org.bukkit.Bukkit;
/*  9:   */ import org.bukkit.entity.EntityType;
/* 10:   */ import org.bukkit.entity.Player;
/* 11:   */ import org.bukkit.event.EventHandler;
/* 12:   */ import org.bukkit.event.Listener;
/* 13:   */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/* 14:   */ import org.bukkit.event.entity.EntityDamageEvent;
/* 15:   */ import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
/* 16:   */ import org.bukkit.event.entity.PlayerDeathEvent;
/* 17:   */ import org.bukkit.scheduler.BukkitScheduler;
/* 18:   */ 
/* 19:   */ public class EntityListener
/* 20:   */   implements Listener
/* 21:   */ {
/* 22:   */   @EventHandler(ignoreCancelled=true)
/* 23:   */   public void onEntityDamage(EntityDamageEvent event)
/* 24:   */   {
/* 25:23 */     if (event.getEntityType() != EntityType.PLAYER) {
/* 26:24 */       return;
/* 27:   */     }
/* 28:27 */     Player player = (Player)event.getEntity();
/* 29:28 */     GamePlayer gamePlayer = PlayerController.get().get(player);
/* 30:30 */     if ((event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) && (gamePlayer.shouldSkipFireTicks()))
/* 31:   */     {
/* 32:31 */       player.setFireTicks(0);
/* 33:32 */       event.setCancelled(true);
/* 34:33 */       gamePlayer.setSkipFireTicks(false);
/* 35:   */     }
/* 36:36 */     if (!gamePlayer.isPlaying()) {
/* 37:37 */       return;
/* 38:   */     }
/* 39:40 */     Game game = gamePlayer.getGame();
/* 40:42 */     if (game.getState() == GameState.WAITING)
/* 41:   */     {
/* 42:43 */       event.setCancelled(true);
/* 43:   */     }
/* 44:44 */     else if ((event.getCause() == EntityDamageEvent.DamageCause.FALL) && (gamePlayer.shouldSkipFallDamage()))
/* 45:   */     {
/* 46:45 */       gamePlayer.setSkipFallDamage(false);
/* 47:46 */       event.setCancelled(true);
/* 48:   */     }
/* 49:47 */     else if (event.getCause() == EntityDamageEvent.DamageCause.VOID)
/* 50:   */     {
/* 51:48 */       player.setFallDistance(0.0F);
/* 52:49 */       event.setCancelled(true);
/* 53:50 */       gamePlayer.getGame().onPlayerDeath(gamePlayer, null);
/* 54:   */     }
/* 55:   */   }
/* 56:   */   
/* 57:   */   @EventHandler(ignoreCancelled=true)
/* 58:   */   public void onPlayerDeath(final PlayerDeathEvent event)
/* 59:   */   {
/* 60:56 */     Player player = event.getEntity();
/* 61:57 */     final GamePlayer gamePlayer = PlayerController.get().get(player);
/* 62:59 */     if (!gamePlayer.isPlaying()) {
/* 63:60 */       return;
/* 64:   */     }
/* 65:63 */     EntityDamageEvent.DamageCause damageCause = player.getLastDamageCause().getCause();
/* 66:64 */     if ((player.getLastDamageCause() instanceof EntityDamageByEntityEvent))
/* 67:   */     {
/* 68:65 */       Bukkit.getScheduler().runTaskLater(SkyWars.get(), new Runnable()
/* 69:   */       {
/* 70:   */         public void run()
/* 71:   */         {
/* 72:68 */           gamePlayer.getGame().onPlayerDeath(gamePlayer, event);
/* 73:   */         }
/* 74:70 */       }, 1L);
/* 75:   */     }
/* 76:71 */     else if ((damageCause == EntityDamageEvent.DamageCause.LAVA) || (damageCause == EntityDamageEvent.DamageCause.FIRE) || (damageCause == EntityDamageEvent.DamageCause.FIRE_TICK))
/* 77:   */     {
/* 78:72 */       gamePlayer.setSkipFireTicks(true);
/* 79:73 */       gamePlayer.getGame().onPlayerDeath(gamePlayer, event);
/* 80:   */     }
/* 81:   */     else
/* 82:   */     {
/* 83:75 */       gamePlayer.getGame().onPlayerDeath(gamePlayer, event);
/* 84:   */     }
/* 85:   */   }
/* 86:   */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.listeners.EntityListener
 * JD-Core Version:    0.7.0.1
 */