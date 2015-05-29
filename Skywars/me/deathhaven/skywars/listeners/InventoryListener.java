/*  1:   */ package me.deathhaven.skywars.listeners;
/*  2:   */ 
/*  3:   */ import me.deathhaven.skywars.config.PluginConfig;
/*  4:   */ import me.deathhaven.skywars.controllers.ChestController;
/*  5:   */ import me.deathhaven.skywars.controllers.PlayerController;
/*  6:   */ import me.deathhaven.skywars.game.Game;
/*  7:   */ import me.deathhaven.skywars.player.GamePlayer;
/*  8:   */ import org.bukkit.Location;
/*  9:   */ import org.bukkit.block.Chest;
/* 10:   */ import org.bukkit.entity.Player;
/* 11:   */ import org.bukkit.event.EventHandler;
/* 12:   */ import org.bukkit.event.Listener;
/* 13:   */ import org.bukkit.event.inventory.InventoryOpenEvent;
/* 14:   */ import org.bukkit.inventory.Inventory;
/* 15:   */ import org.bukkit.inventory.ItemStack;
/* 16:   */ 
/* 17:   */ public class InventoryListener
/* 18:   */   implements Listener
/* 19:   */ {
/* 20:   */   @EventHandler(ignoreCancelled=true)
/* 21:   */   public void onInventoryOpen(InventoryOpenEvent event)
/* 22:   */   {
/* 23:22 */     if (!(event.getInventory().getHolder() instanceof Chest)) {
/* 24:23 */       return;
/* 25:   */     }
/* 26:26 */     Player player = (Player)event.getPlayer();
/* 27:27 */     GamePlayer gamePlayer = PlayerController.get().get(player);
/* 28:29 */     if (!gamePlayer.isPlaying()) {
/* 29:30 */       return;
/* 30:   */     }
/* 31:33 */     Chest chest = (Chest)event.getInventory().getHolder();
/* 32:34 */     Location location = chest.getLocation();
/* 33:36 */     if (!gamePlayer.getGame().isChest(location)) {
/* 34:37 */       return;
/* 35:   */     }
/* 36:40 */     Inventory inv = chest.getInventory();
/* 37:41 */     boolean empty = true;
/* 38:42 */     for (ItemStack itemStack : inv.getContents()) {
/* 39:43 */       if (itemStack != null)
/* 40:   */       {
/* 41:44 */         empty = false;
/* 42:45 */         break;
/* 43:   */       }
/* 44:   */     }
/* 45:49 */     if ((!PluginConfig.fillEmptyChests()) && (empty)) {
/* 46:50 */       return;
/* 47:   */     }
/* 48:53 */     if ((!PluginConfig.fillPopulatedChests()) && (!empty)) {
/* 49:54 */       return;
/* 50:   */     }
/* 51:56 */     inv.clear();
/* 52:   */     
/* 53:   */ 
/* 54:59 */     gamePlayer.getGame().removeChest(location);
/* 55:60 */     ChestController.get().populateChest(chest);
/* 56:   */   }
/* 57:   */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.listeners.InventoryListener
 * JD-Core Version:    0.7.0.1
 */