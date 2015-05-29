/*  1:   */ package me.deathhaven.skywars.controllers;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.Iterator;
/*  5:   */ import java.util.List;
/*  6:   */ import me.deathhaven.skywars.SkyWars;
/*  7:   */ import me.deathhaven.skywars.utilities.IconMenu;
/*  8:   */ import me.deathhaven.skywars.utilities.IconMenu.OptionClickEvent;
/*  9:   */ import me.deathhaven.skywars.utilities.IconMenu.OptionClickEventHandler;
/* 10:   */ import org.bukkit.Material;
/* 11:   */ import org.bukkit.entity.Player;
/* 12:   */ import org.bukkit.event.EventHandler;
/* 13:   */ import org.bukkit.event.Listener;
/* 14:   */ import org.bukkit.inventory.ItemStack;
/* 15:   */ 
/* 16:   */ public class CustomController
/* 17:   */   implements Listener
/* 18:   */ {
/* 19:18 */   public static List<Player> activePlayers = new ArrayList();
/* 20:   */   private static CustomController instance;
/* 21:   */   
/* 22:   */   public static CustomController get()
/* 23:   */   {
/* 24:23 */     if (instance == null) {
/* 25:24 */       instance = new CustomController();
/* 26:   */     }
/* 27:27 */     return instance;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public CustomController()
/* 31:   */   {
/* 32:31 */     IconMenu menu = new IconMenu("InfoGUI", 27, new IconMenu.OptionClickEventHandler()
/* 33:   */     {
/* 34:   */       public void onOptionClick(IconMenu.OptionClickEvent event)
/* 35:   */       {
/* 36:35 */         switch (event.getPosition())
/* 37:   */         {
/* 38:   */         }
/* 39:   */       }
/* 40:43 */     }, SkyWars.get());
/* 41:   */     
/* 42:45 */     ItemStack stack = new ItemStack(Material.GRASS, 1);
/* 43:   */   }
/* 44:   */   
/* 45:   */   public static enum ChangeType
/* 46:   */   {
/* 47:50 */     PlayerLeft,  PlayerJoin,  SpectateLeft,  SpectateJoin;
/* 48:   */   }
/* 49:   */   
/* 50:   */   @EventHandler
/* 51:   */   public void onGameChange(GameChangeEvent e)
/* 52:   */   {
/* 53:55 */     switch (e.getChangeType())
/* 54:   */     {
/* 55:   */     case PlayerJoin: 
/* 56:   */       Player localPlayer;
/* 57:58 */       for (Iterator localIterator = activePlayers.iterator(); localIterator.hasNext(); localPlayer = (Player)localIterator.next()) {}
/* 58:61 */       break;
/* 59:   */     case PlayerLeft: 
/* 60:   */       break;
/* 61:   */     case SpectateLeft: 
/* 62:   */       break;
/* 63:   */     case SpectateJoin: 
/* 64:   */       break;
/* 65:   */     }
/* 66:   */   }
/* 67:   */   
/* 68:   */   public class GameChangeEvent
/* 69:   */   {
/* 70:   */     private CustomController.ChangeType type;
/* 71:   */     private Player player;
/* 72:   */     private String msg;
/* 73:   */     
/* 74:   */     public GameChangeEvent(CustomController.ChangeType type, Player player, String msg)
/* 75:   */     {
/* 76:83 */       this.type = type;
/* 77:84 */       this.player = player;
/* 78:85 */       this.msg = msg;
/* 79:   */     }
/* 80:   */     
/* 81:   */     public CustomController.ChangeType getChangeType()
/* 82:   */     {
/* 83:89 */       return this.type;
/* 84:   */     }
/* 85:   */     
/* 86:   */     public Player getPlayer()
/* 87:   */     {
/* 88:93 */       return this.player;
/* 89:   */     }
/* 90:   */     
/* 91:   */     public String getMessage()
/* 92:   */     {
/* 93:97 */       return this.msg;
/* 94:   */     }
/* 95:   */   }
/* 96:   */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.controllers.CustomController
 * JD-Core Version:    0.7.0.1
 */