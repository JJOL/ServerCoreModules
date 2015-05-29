/*   1:    */ package me.deathhaven.skywars.utilities;
/*   2:    */ 
/*   3:    */ import org.bukkit.Bukkit;
/*   4:    */ import org.bukkit.entity.Player;
/*   5:    */ import org.bukkit.event.inventory.InventoryClickEvent;
/*   6:    */ import org.bukkit.inventory.Inventory;
/*   7:    */ import org.bukkit.inventory.ItemStack;
/*   8:    */ import org.bukkit.plugin.Plugin;
/*   9:    */ import org.bukkit.scheduler.BukkitScheduler;
/*  10:    */ 
/*  11:    */ public class IconMenu
/*  12:    */ {
/*  13:    */   private String name;
/*  14:    */   private int size;
/*  15:    */   private OptionClickEventHandler handler;
/*  16:    */   private Plugin plugin;
/*  17:    */   private String[] optionNames;
/*  18:    */   private ItemStack[] optionIcons;
/*  19:    */   
/*  20:    */   public IconMenu(String name, int size, OptionClickEventHandler handler, Plugin plugin)
/*  21:    */   {
/*  22: 21 */     this.name = name;
/*  23: 22 */     this.size = size;
/*  24: 23 */     this.handler = handler;
/*  25: 24 */     this.plugin = plugin;
/*  26: 25 */     this.optionNames = new String[size];
/*  27: 26 */     this.optionIcons = new ItemStack[size];
/*  28:    */   }
/*  29:    */   
/*  30:    */   public IconMenu setOption(int position, ItemStack icon, String name, String[] info)
/*  31:    */   {
/*  32: 30 */     this.optionNames[position] = name;
/*  33: 31 */     this.optionIcons[position] = ItemUtils.name(icon, name, info);
/*  34: 32 */     return this;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void open(Player player)
/*  38:    */   {
/*  39: 36 */     Inventory inventory = Bukkit.createInventory(player, this.size, this.name);
/*  40: 37 */     for (int iii = 0; iii < this.optionIcons.length; iii++) {
/*  41: 38 */       if (this.optionIcons[iii] != null) {
/*  42: 39 */         inventory.setItem(iii, this.optionIcons[iii]);
/*  43:    */       }
/*  44:    */     }
/*  45: 42 */     player.openInventory(inventory);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void destroy()
/*  49:    */   {
/*  50: 46 */     this.handler = null;
/*  51: 47 */     this.plugin = null;
/*  52: 48 */     this.optionNames = null;
/*  53: 49 */     this.optionIcons = null;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void onInventoryClick(InventoryClickEvent event)
/*  57:    */   {
/*  58: 53 */     if (!event.getInventory().getTitle().equals(this.name)) {
/*  59: 54 */       return;
/*  60:    */     }
/*  61: 57 */     event.setCancelled(true);
/*  62:    */     
/*  63: 59 */     int slot = event.getRawSlot();
/*  64: 60 */     if ((slot < 0) || (slot >= this.size) || (this.optionNames[slot] == null)) {
/*  65: 61 */       return;
/*  66:    */     }
/*  67: 64 */     OptionClickEvent clickEvent = new OptionClickEvent((Player)event.getWhoClicked(), slot, this.optionNames[slot]);
/*  68: 65 */     this.handler.onOptionClick(clickEvent);
/*  69: 67 */     if (clickEvent.willClose())
/*  70:    */     {
/*  71: 68 */       final Player player = (Player)event.getWhoClicked();
/*  72:    */       
/*  73: 70 */       Bukkit.getScheduler().runTaskLater(this.plugin, new Runnable()
/*  74:    */       {
/*  75:    */         public void run()
/*  76:    */         {
/*  77: 73 */           player.closeInventory();
/*  78:    */         }
/*  79: 75 */       }, 1L);
/*  80:    */     }
/*  81: 78 */     if (clickEvent.willDestroy()) {
/*  82: 79 */       destroy();
/*  83:    */     }
/*  84:    */   }
/*  85:    */   
/*  86:    */   public String getName()
/*  87:    */   {
/*  88: 84 */     return this.name;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public class OptionClickEvent
/*  92:    */   {
/*  93:    */     private Player player;
/*  94:    */     private int position;
/*  95:    */     private String name;
/*  96:    */     private boolean close;
/*  97:    */     private boolean destroy;
/*  98:    */     
/*  99:    */     public OptionClickEvent(Player player, int position, String name)
/* 100:    */     {
/* 101: 96 */       this.player = player;
/* 102: 97 */       this.position = position;
/* 103: 98 */       this.name = name;
/* 104: 99 */       this.close = false;
/* 105:100 */       this.destroy = false;
/* 106:    */     }
/* 107:    */     
/* 108:    */     public Player getPlayer()
/* 109:    */     {
/* 110:104 */       return this.player;
/* 111:    */     }
/* 112:    */     
/* 113:    */     public int getPosition()
/* 114:    */     {
/* 115:108 */       return this.position;
/* 116:    */     }
/* 117:    */     
/* 118:    */     public String getName()
/* 119:    */     {
/* 120:112 */       return this.name;
/* 121:    */     }
/* 122:    */     
/* 123:    */     public boolean willClose()
/* 124:    */     {
/* 125:116 */       return this.close;
/* 126:    */     }
/* 127:    */     
/* 128:    */     public boolean willDestroy()
/* 129:    */     {
/* 130:120 */       return this.destroy;
/* 131:    */     }
/* 132:    */     
/* 133:    */     public void setWillClose(boolean close)
/* 134:    */     {
/* 135:124 */       this.close = close;
/* 136:    */     }
/* 137:    */     
/* 138:    */     public void setWillDestroy(boolean destroy)
/* 139:    */     {
/* 140:128 */       this.destroy = destroy;
/* 141:    */     }
/* 142:    */   }
/* 143:    */   
/* 144:    */   public static abstract interface OptionClickEventHandler
/* 145:    */   {
/* 146:    */     public abstract void onOptionClick(IconMenu.OptionClickEvent paramOptionClickEvent);
/* 147:    */   }
/* 148:    */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.utilities.IconMenu
 * JD-Core Version:    0.7.0.1
 */