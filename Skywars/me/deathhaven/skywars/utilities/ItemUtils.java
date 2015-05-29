/*   1:    */ package me.deathhaven.skywars.utilities;
/*   2:    */ 
/*   3:    */ import com.earth2me.essentials.Enchantments;
/*   4:    */ import com.earth2me.essentials.api.IItemDb;
/*   5:    */ import com.flobi.WhatIsIt.WhatIsIt;
/*   6:    */ import com.sk89q.commandbook.util.item.ItemUtil;
/*   7:    */ import com.sk89q.minecraft.util.commands.CommandException;
/*   8:    */ import com.sk89q.worldedit.blocks.ItemType;
/*   9:    */ import java.util.ArrayList;
/*  10:    */ import java.util.List;
/*  11:    */ import java.util.Map;
/*  12:    */ import net.ess3.api.IEssentials;
/*  13:    */ import net.milkbowl.vault.item.ItemInfo;
/*  14:    */ import net.milkbowl.vault.item.Items;
/*  15:    */ import org.bukkit.Bukkit;
/*  16:    */ import org.bukkit.ChatColor;
/*  17:    */ import org.bukkit.Material;
/*  18:    */ import org.bukkit.Server;
/*  19:    */ import org.bukkit.enchantments.Enchantment;
/*  20:    */ import org.bukkit.inventory.ItemStack;
/*  21:    */ import org.bukkit.inventory.meta.ItemMeta;
/*  22:    */ import org.bukkit.plugin.PluginManager;
/*  23:    */ 
/*  24:    */ public class ItemUtils
/*  25:    */ {
/*  26:    */   public static ItemStack parseItem(String input)
/*  27:    */   {
/*  28: 25 */     String[] item = { input };
/*  29: 26 */     return parseItem(item);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public static ItemStack parseItem(String[] args)
/*  33:    */   {
/*  34: 37 */     if (args.length < 1) {
/*  35: 38 */       return null;
/*  36:    */     }
/*  37: 40 */     String[] type = args[0].split(":", 2);
/*  38: 41 */     String typeInput = type[0];
/*  39: 42 */     short durability = 0;
/*  40:    */     
/*  41:    */ 
/*  42: 45 */     Material mat = Material.matchMaterial(typeInput);
/*  43: 46 */     if ((mat == null) && 
/*  44: 47 */       (Bukkit.getPluginManager().getPlugin("Essentials") != null))
/*  45:    */     {
/*  46: 48 */       IEssentials ess = (IEssentials)Bukkit.getPluginManager().getPlugin("Essentials");
/*  47:    */       try
/*  48:    */       {
/*  49: 50 */         ItemStack essStack = ess.getItemDb().get(typeInput);
/*  50: 51 */         mat = essStack.getType();
/*  51: 52 */         durability = essStack.getDurability();
/*  52:    */       }
/*  53:    */       catch (Exception localException) {}
/*  54:    */     }
/*  55: 57 */     if ((mat == null) && 
/*  56: 58 */       (Bukkit.getServer().getPluginManager().getPlugin("WorldEdit") != null))
/*  57:    */     {
/*  58: 59 */       ItemType itemType = ItemType.lookup(typeInput);
/*  59: 60 */       if (itemType != null) {
/*  60: 61 */         mat = Material.getMaterial(itemType.getID());
/*  61:    */       }
/*  62:    */     }
/*  63: 65 */     if ((mat == null) && 
/*  64: 66 */       (Bukkit.getServer().getPluginManager().getPlugin("Vault") != null))
/*  65:    */     {
/*  66: 67 */       ItemInfo itemInfo = Items.itemByString(typeInput);
/*  67: 68 */       if (itemInfo != null)
/*  68:    */       {
/*  69: 69 */         mat = itemInfo.material;
/*  70: 70 */         durability = itemInfo.subTypeId;
/*  71:    */       }
/*  72:    */     }
/*  73: 74 */     if (mat == null) {
/*  74: 75 */       return null;
/*  75:    */     }
/*  76: 77 */     ItemStack itemStack = new ItemStack(mat);
/*  77: 80 */     if ((type.length > 1) && (type[1].length() > 0) && (durability == 0))
/*  78:    */     {
/*  79: 81 */       String durabilityInput = type[1];
/*  80:    */       try
/*  81:    */       {
/*  82: 83 */         durability = Short.parseShort(durabilityInput);
/*  83:    */       }
/*  84:    */       catch (NumberFormatException localNumberFormatException) {}
/*  85: 86 */       if ((durability == 0) && 
/*  86: 87 */         (Bukkit.getServer().getPluginManager().getPlugin("CommandBook") != null)) {
/*  87:    */         try
/*  88:    */         {
/*  89: 89 */           durability = (short)ItemUtil.matchItemData(mat.getId(), durabilityInput);
/*  90:    */         }
/*  91:    */         catch (CommandException localCommandException) {}
/*  92:    */       }
/*  93:    */     }
/*  94: 97 */     itemStack.setDurability(durability);
/*  95:    */     
/*  96:    */ 
/*  97:100 */     int stackSize = 1;
/*  98:101 */     int maxSize = itemStack.getMaxStackSize();
/*  99:102 */     if ((args.length > 1) && (args[1].length() > 0)) {
/* 100:    */       try
/* 101:    */       {
/* 102:104 */         stackSize = Integer.parseInt(args[1]);
/* 103:105 */         if (stackSize > maxSize) {
/* 104:106 */           stackSize = maxSize;
/* 105:    */         }
/* 106:    */       }
/* 107:    */       catch (NumberFormatException localNumberFormatException1) {}
/* 108:    */     }
/* 109:111 */     itemStack.setAmount(stackSize);
/* 110:114 */     if (args.length > 2) {
/* 111:115 */       for (String ench : args[2].split(","))
/* 112:    */       {
/* 113:116 */         String[] enchantment = ench.split(":", 2);
/* 114:117 */         Enchantment enchType = Enchantment.getByName(enchantment[0]);
/* 115:118 */         if ((enchType == null) && 
/* 116:119 */           (Bukkit.getPluginManager().getPlugin("Essentials") != null)) {
/* 117:120 */           enchType = Enchantments.getByName(enchantment[0]);
/* 118:    */         }
/* 119:123 */         if (enchType == null)
/* 120:    */         {
/* 121:124 */           String enchSearchString = enchantment[0].toLowerCase().replaceAll("[_\\-]", "");
/* 122:125 */           for (Enchantment possibleEnch : Enchantment.values()) {
/* 123:126 */             if (possibleEnch.getName().toLowerCase().replaceAll("[_\\-]", "").equals(enchSearchString))
/* 124:    */             {
/* 125:127 */               enchType = possibleEnch;
/* 126:128 */               break;
/* 127:    */             }
/* 128:    */           }
/* 129:    */         }
/* 130:134 */         if (enchType != null)
/* 131:    */         {
/* 132:135 */           Map<Enchantment, Integer> existingEnchantments = itemStack.getEnchantments();
/* 133:136 */           boolean enchant = true;
/* 134:137 */           if (enchType.canEnchantItem(itemStack)) {
/* 135:138 */             for (Enchantment existingEnchantment : existingEnchantments.keySet()) {
/* 136:139 */               if (enchType.conflictsWith(existingEnchantment))
/* 137:    */               {
/* 138:140 */                 enchant = false;
/* 139:141 */                 break;
/* 140:    */               }
/* 141:    */             }
/* 142:    */           } else {
/* 143:145 */             enchant = false;
/* 144:    */           }
/* 145:147 */           if (enchant)
/* 146:    */           {
/* 147:152 */             int enchLevel = enchType.getStartLevel();
/* 148:153 */             if ((enchantment.length > 1) && (enchantment[1].length() > 0))
/* 149:    */             {
/* 150:    */               try
/* 151:    */               {
/* 152:155 */                 enchLevel = Integer.parseInt(enchantment[1]);
/* 153:    */               }
/* 154:    */               catch (NumberFormatException localNumberFormatException3) {}
/* 155:158 */               if (enchLevel < enchType.getStartLevel()) {
/* 156:159 */                 enchLevel = enchType.getStartLevel();
/* 157:160 */               } else if (enchLevel > enchType.getMaxLevel()) {
/* 158:161 */                 enchLevel = enchType.getMaxLevel();
/* 159:    */               }
/* 160:    */             }
/* 161:164 */             itemStack.addEnchantment(enchType, enchLevel);
/* 162:    */           }
/* 163:    */         }
/* 164:    */       }
/* 165:    */     }
/* 166:169 */     return itemStack;
/* 167:    */   }
/* 168:    */   
/* 169:    */   public static ItemStack name(ItemStack itemStack, String name, String... lores)
/* 170:    */   {
/* 171:173 */     ItemMeta itemMeta = itemStack.getItemMeta();
/* 172:175 */     if (!name.isEmpty()) {
/* 173:176 */       itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
/* 174:    */     }
/* 175:179 */     if (lores.length > 0)
/* 176:    */     {
/* 177:180 */       List<String> loreList = new ArrayList(lores.length);
/* 178:182 */       for (String lore : lores) {
/* 179:183 */         loreList.add(ChatColor.translateAlternateColorCodes('&', lore));
/* 180:    */       }
/* 181:186 */       itemMeta.setLore(loreList);
/* 182:    */     }
/* 183:189 */     itemStack.setItemMeta(itemMeta);
/* 184:190 */     return itemStack;
/* 185:    */   }
/* 186:    */   
/* 187:    */   public static String itemName(ItemStack stack)
/* 188:    */   {
/* 189:201 */     String name = null;
/* 190:202 */     if (Bukkit.getPluginManager().getPlugin("WhatIsIt") != null) {
/* 191:203 */       name = WhatIsIt.itemName(stack);
/* 192:    */     }
/* 193:205 */     if ((name == null) && 
/* 194:206 */       (Bukkit.getPluginManager().getPlugin("Essentials") != null))
/* 195:    */     {
/* 196:207 */       IEssentials ess = (IEssentials)Bukkit.getPluginManager().getPlugin("Essentials");
/* 197:208 */       name = ess.getItemDb().name(stack);
/* 198:    */     }
/* 199:211 */     if ((name == null) && 
/* 200:212 */       (Bukkit.getServer().getPluginManager().getPlugin("Vault") != null)) {
/* 201:213 */       name = Items.itemByName(stack.toString()).getName();
/* 202:    */     }
/* 203:216 */     if ((name == null) && 
/* 204:217 */       (Bukkit.getServer().getPluginManager().getPlugin("WorldEdit") != null)) {
/* 205:218 */       name = ItemType.fromID(stack.getTypeId()).getName();
/* 206:    */     }
/* 207:221 */     if (name == null) {
/* 208:222 */       name = stack.getType().toString().toLowerCase();
/* 209:    */     }
/* 210:224 */     return name;
/* 211:    */   }
/* 212:    */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.utilities.ItemUtils
 * JD-Core Version:    0.7.0.1
 */