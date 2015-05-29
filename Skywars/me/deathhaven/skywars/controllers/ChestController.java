/*  1:   */ package me.deathhaven.skywars.controllers;
/*  2:   */ 
/*  3:   */ import com.google.common.collect.Lists;
/*  4:   */ import java.io.File;
/*  5:   */ import java.util.List;
/*  6:   */ import java.util.Random;
/*  7:   */ import java.util.logging.Level;
/*  8:   */ import me.deathhaven.skywars.SkyWars;
/*  9:   */ import me.deathhaven.skywars.utilities.ItemUtils;
/* 10:   */ import me.deathhaven.skywars.utilities.LogUtils;
/* 11:   */ import org.bukkit.block.Chest;
/* 12:   */ import org.bukkit.configuration.file.FileConfiguration;
/* 13:   */ import org.bukkit.configuration.file.YamlConfiguration;
/* 14:   */ import org.bukkit.inventory.Inventory;
/* 15:   */ import org.bukkit.inventory.ItemStack;
/* 16:   */ 
/* 17:   */ public class ChestController
/* 18:   */ {
/* 19:   */   private static ChestController chestController;
/* 20:23 */   private final List<ChestItem> chestItemList = Lists.newArrayList();
/* 21:24 */   private final Random random = new Random();
/* 22:   */   
/* 23:   */   public ChestController()
/* 24:   */   {
/* 25:27 */     load();
/* 26:   */   }
/* 27:   */   
/* 28:   */   public void load()
/* 29:   */   {
/* 30:31 */     this.chestItemList.clear();
/* 31:32 */     File chestFile = new File(SkyWars.get().getDataFolder(), "chest.yml");
/* 32:34 */     if (!chestFile.exists()) {
/* 33:35 */       SkyWars.get().saveResource("chest.yml", false);
/* 34:   */     }
/* 35:38 */     if (chestFile.exists())
/* 36:   */     {
/* 37:39 */       FileConfiguration storage = YamlConfiguration.loadConfiguration(chestFile);
/* 38:41 */       if (storage.contains("items")) {
/* 39:42 */         for (String item : storage.getStringList("items"))
/* 40:   */         {
/* 41:43 */           String[] itemData = item.split(" ", 2);
/* 42:   */           
/* 43:45 */           int chance = Integer.parseInt(itemData[0]);
/* 44:46 */           ItemStack itemStack = ItemUtils.parseItem(itemData[1].split(" "));
/* 45:48 */           if (itemStack != null) {
/* 46:49 */             this.chestItemList.add(new ChestItem(itemStack, chance));
/* 47:   */           } else {
/* 48:51 */             LogUtils.log(Level.WARNING, getClass(), "Invalid item in chest: " + item, new Object[0]);
/* 49:   */           }
/* 50:   */         }
/* 51:   */       }
/* 52:   */     }
/* 53:57 */     LogUtils.log(Level.INFO, getClass(), "Registered %d chest items ...", new Object[] { Integer.valueOf(this.chestItemList.size()) });
/* 54:   */   }
/* 55:   */   
/* 56:   */   public void populateChest(Chest chest)
/* 57:   */   {
/* 58:61 */     Inventory inventory = chest.getBlockInventory();
/* 59:62 */     int added = 0;
/* 60:64 */     for (ChestItem chestItem : this.chestItemList) {
/* 61:65 */       if (this.random.nextInt(100) + 1 <= chestItem.getChance())
/* 62:   */       {
/* 63:66 */         inventory.addItem(new ItemStack[] { chestItem.getItem() });
/* 64:68 */         if (added++ > inventory.getSize()) {
/* 65:   */           break;
/* 66:   */         }
/* 67:   */       }
/* 68:   */     }
/* 69:   */   }
/* 70:   */   
/* 71:   */   public class ChestItem
/* 72:   */   {
/* 73:   */     private ItemStack item;
/* 74:   */     private int chance;
/* 75:   */     
/* 76:   */     public ChestItem(ItemStack item, int chance)
/* 77:   */     {
/* 78:81 */       this.item = item;
/* 79:82 */       this.chance = chance;
/* 80:   */     }
/* 81:   */     
/* 82:   */     public ItemStack getItem()
/* 83:   */     {
/* 84:86 */       return this.item;
/* 85:   */     }
/* 86:   */     
/* 87:   */     public int getChance()
/* 88:   */     {
/* 89:90 */       return this.chance;
/* 90:   */     }
/* 91:   */   }
/* 92:   */   
/* 93:   */   public static ChestController get()
/* 94:   */   {
/* 95:95 */     if (chestController == null) {
/* 96:96 */       chestController = new ChestController();
/* 97:   */     }
/* 98:99 */     return chestController;
/* 99:   */   }
/* :0:   */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.controllers.ChestController
 * JD-Core Version:    0.7.0.1
 */