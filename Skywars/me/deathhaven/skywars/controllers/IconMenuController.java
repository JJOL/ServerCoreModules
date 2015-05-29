/*  1:   */ package me.deathhaven.skywars.controllers;
/*  2:   */ 
/*  3:   */ import com.google.common.collect.Maps;
/*  4:   */ import java.util.HashSet;
/*  5:   */ import java.util.Map;
/*  6:   */ import javax.annotation.Nonnull;
/*  7:   */ import me.deathhaven.skywars.SkyWars;
/*  8:   */ import me.deathhaven.skywars.utilities.IconMenu;
/*  9:   */ import me.deathhaven.skywars.utilities.IconMenu.OptionClickEventHandler;
/* 10:   */ import org.bukkit.Bukkit;
/* 11:   */ import org.bukkit.entity.Player;
/* 12:   */ import org.bukkit.event.EventHandler;
/* 13:   */ import org.bukkit.event.EventPriority;
/* 14:   */ import org.bukkit.event.Listener;
/* 15:   */ import org.bukkit.event.inventory.InventoryClickEvent;
/* 16:   */ import org.bukkit.event.inventory.InventoryCloseEvent;
/* 17:   */ import org.bukkit.inventory.InventoryView;
/* 18:   */ import org.bukkit.inventory.ItemStack;
/* 19:   */ import org.bukkit.plugin.PluginManager;
/* 20:   */ 
/* 21:   */ public class IconMenuController
/* 22:   */   implements Listener
/* 23:   */ {
/* 24:   */   private static IconMenuController instance;
/* 25:25 */   private final Map<Player, IconMenu> menuMap = Maps.newHashMap();
/* 26:   */   
/* 27:   */   public IconMenuController()
/* 28:   */   {
/* 29:28 */     Bukkit.getPluginManager().registerEvents(this, SkyWars.get());
/* 30:   */   }
/* 31:   */   
/* 32:   */   public void create(Player player, String name, int size, IconMenu.OptionClickEventHandler handler)
/* 33:   */   {
/* 34:32 */     destroy(player);
/* 35:33 */     this.menuMap.put(player, new IconMenu(name, size, handler, SkyWars.get()));
/* 36:   */   }
/* 37:   */   
/* 38:   */   public void show(@Nonnull Player player)
/* 39:   */   {
/* 40:37 */     if (this.menuMap.containsKey(player)) {
/* 41:38 */       ((IconMenu)this.menuMap.get(player)).open(player);
/* 42:   */     }
/* 43:   */   }
/* 44:   */   
/* 45:   */   public void setOption(Player player, int position, ItemStack icon, String name, String... info)
/* 46:   */   {
/* 47:43 */     if (this.menuMap.containsKey(player)) {
/* 48:44 */       ((IconMenu)this.menuMap.get(player)).setOption(position, icon, name, info);
/* 49:   */     }
/* 50:   */   }
/* 51:   */   
/* 52:   */   public void destroy(Player player)
/* 53:   */   {
/* 54:49 */     if (this.menuMap.containsKey(player))
/* 55:   */     {
/* 56:50 */       ((IconMenu)this.menuMap.remove(player)).destroy();
/* 57:51 */       player.getOpenInventory().close();
/* 58:   */     }
/* 59:   */   }
/* 60:   */   
/* 61:   */   public void destroyAll()
/* 62:   */   {
/* 63:56 */     for (Player player : new HashSet(this.menuMap.keySet())) {
/* 64:57 */       destroy(player);
/* 65:   */     }
/* 66:   */   }
/* 67:   */   
/* 68:   */   public boolean has(Player player)
/* 69:   */   {
/* 70:62 */     return this.menuMap.containsKey(player);
/* 71:   */   }
/* 72:   */   
/* 73:   */   @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
/* 74:   */   public void onInventoryClick(InventoryClickEvent event)
/* 75:   */   {
/* 76:67 */     if (((event.getWhoClicked() instanceof Player)) && (this.menuMap.containsKey(event.getWhoClicked()))) {
/* 77:68 */       ((IconMenu)this.menuMap.get(event.getWhoClicked())).onInventoryClick(event);
/* 78:   */     }
/* 79:   */   }
/* 80:   */   
/* 81:   */   @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
/* 82:   */   public void onInventoryClose(InventoryCloseEvent event)
/* 83:   */   {
/* 84:74 */     if (((event.getPlayer() instanceof Player)) && (this.menuMap.containsKey(event.getPlayer()))) {
/* 85:75 */       destroy((Player)event.getPlayer());
/* 86:   */     }
/* 87:   */   }
/* 88:   */   
/* 89:   */   public static IconMenuController get()
/* 90:   */   {
/* 91:80 */     if (instance == null) {
/* 92:81 */       instance = new IconMenuController();
/* 93:   */     }
/* 94:84 */     return instance;
/* 95:   */   }
/* 96:   */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.controllers.IconMenuController
 * JD-Core Version:    0.7.0.1
 */