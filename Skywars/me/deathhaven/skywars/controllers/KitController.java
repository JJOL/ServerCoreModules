/*   1:    */ package me.deathhaven.skywars.controllers;
/*   2:    */ 
/*   3:    */ import com.google.common.collect.Lists;
/*   4:    */ import com.google.common.collect.Maps;
/*   5:    */ import java.io.File;
/*   6:    */ import java.util.Collection;
/*   7:    */ import java.util.Iterator;
/*   8:    */ import java.util.List;
/*   9:    */ import java.util.Map;
/*  10:    */ import java.util.logging.Level;
/*  11:    */ import me.deathhaven.skywars.SkyWars;
/*  12:    */ import me.deathhaven.skywars.game.Game;
/*  13:    */ import me.deathhaven.skywars.game.GameState;
/*  14:    */ import me.deathhaven.skywars.player.GamePlayer;
/*  15:    */ import me.deathhaven.skywars.utilities.FileUtils;
/*  16:    */ import me.deathhaven.skywars.utilities.IconMenu.OptionClickEvent;
/*  17:    */ import me.deathhaven.skywars.utilities.IconMenu.OptionClickEventHandler;
/*  18:    */ import me.deathhaven.skywars.utilities.ItemUtils;
/*  19:    */ import me.deathhaven.skywars.utilities.LogUtils;
/*  20:    */ import me.deathhaven.skywars.utilities.Messaging.MessageFormatter;
/*  21:    */ import org.bukkit.ChatColor;
/*  22:    */ import org.bukkit.Material;
/*  23:    */ import org.bukkit.configuration.file.FileConfiguration;
/*  24:    */ import org.bukkit.configuration.file.YamlConfiguration;
/*  25:    */ import org.bukkit.entity.Player;
/*  26:    */ import org.bukkit.inventory.Inventory;
/*  27:    */ import org.bukkit.inventory.ItemStack;
/*  28:    */ 
/*  29:    */ public class KitController
/*  30:    */ {
/*  31:    */   private static final String PERMISSION_NODE = "skywars.kit.";
/*  32:    */   private static final int INVENTORY_SLOTS_PER_ROW = 9;
/*  33:    */   private static final int MAX_INVENTORY_SIZE = 54;
/*  34: 30 */   private static final String KIT_MENU_NAME = new Messaging.MessageFormatter().format("kit.window-title");
/*  35:    */   private static KitController instance;
/*  36: 33 */   private final Map<String, Kit> kitMap = Maps.newHashMap();
/*  37:    */   
/*  38:    */   public KitController()
/*  39:    */   {
/*  40: 36 */     load();
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void load()
/*  44:    */   {
/*  45: 40 */     this.kitMap.clear();
/*  46: 41 */     File dataDirectory = SkyWars.get().getDataFolder();
/*  47: 42 */     File kitsDirectory = new File(dataDirectory, "kits");
/*  48: 44 */     if (!kitsDirectory.exists())
/*  49:    */     {
/*  50: 45 */       if (!kitsDirectory.mkdirs()) {
/*  51: 46 */         return;
/*  52:    */       }
/*  53: 50 */       FileUtils.saveResource(SkyWars.get(), "example.yml", new File(kitsDirectory, "Example.yml"), false);
/*  54:    */     }
/*  55: 53 */     File[] kits = kitsDirectory.listFiles();
/*  56: 54 */     if (kits == null) {
/*  57: 55 */       return;
/*  58:    */     }
/*  59: 58 */     for (File kit : kits) {
/*  60: 59 */       if (kit.getName().endsWith(".yml"))
/*  61:    */       {
/*  62: 63 */         String name = kit.getName().replace(".yml", "");
/*  63: 65 */         if ((!name.isEmpty()) && (!this.kitMap.containsKey(name))) {
/*  64: 66 */           this.kitMap.put(name, new Kit(name, YamlConfiguration.loadConfiguration(kit)));
/*  65:    */         }
/*  66:    */       }
/*  67:    */     }
/*  68: 70 */     LogUtils.log(Level.INFO, getClass(), "Registered %d kits ...", new Object[] { Integer.valueOf(this.kitMap.size()) });
/*  69:    */   }
/*  70:    */   
/*  71:    */   public boolean hasPermission(Player player, Kit kit)
/*  72:    */   {
/*  73: 74 */     return (player.isOp()) || (player.hasPermission("skywars.kit." + kit.getName().toLowerCase()));
/*  74:    */   }
/*  75:    */   
/*  76:    */   public boolean isPurchaseAble(Kit kit)
/*  77:    */   {
/*  78: 78 */     return kit.getPoints() > 0;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public boolean canPurchase(GamePlayer gamePlayer, Kit kit)
/*  82:    */   {
/*  83: 82 */     return (kit.getPoints() > 0) && (gamePlayer.getScore() >= kit.getPoints());
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void populateInventory(Inventory inventory, Kit kit)
/*  87:    */   {
/*  88: 86 */     for (ItemStack itemStack : kit.getItems()) {
/*  89: 87 */       inventory.addItem(new ItemStack[] { itemStack });
/*  90:    */     }
/*  91:    */   }
/*  92:    */   
/*  93:    */   public Kit getByName(String name)
/*  94:    */   {
/*  95: 92 */     return (Kit)this.kitMap.get(name);
/*  96:    */   }
/*  97:    */   
/*  98:    */   public void openKitMenu(final GamePlayer gamePlayer)
/*  99:    */   {
/* 100: 96 */     List<Kit> availableKits = Lists.newArrayList(this.kitMap.values());
/* 101:    */     
/* 102: 98 */     int rowCount = 9;
/* 103: 99 */     while ((rowCount < availableKits.size()) && (rowCount < 54)) {
/* 104:100 */       rowCount += 9;
/* 105:    */     }
/* 106:103 */     IconMenuController.get().create(gamePlayer.getBukkitPlayer(), KIT_MENU_NAME, rowCount, new IconMenu.OptionClickEventHandler()
/* 107:    */     {
/* 108:    */       public void onOptionClick(IconMenu.OptionClickEvent event)
/* 109:    */       {
/* 110:106 */         if (!gamePlayer.isPlaying())
/* 111:    */         {
/* 112:107 */           event.getPlayer().sendMessage(new Messaging.MessageFormatter().format("error.not-in-game"));
/* 113:108 */           return;
/* 114:    */         }
/* 115:111 */         if (gamePlayer.getGame().getState() != GameState.WAITING)
/* 116:    */         {
/* 117:112 */           event.getPlayer().sendMessage(new Messaging.MessageFormatter().format("error.can-not-pick-kit"));
/* 118:113 */           return;
/* 119:    */         }
/* 120:116 */         KitController.Kit kit = KitController.get().getByName(ChatColor.stripColor(event.getName()));
/* 121:117 */         if (kit == null) {
/* 122:118 */           return;
/* 123:    */         }
/* 124:125 */         if ((KitController.this.isPurchaseAble(kit)) && (!KitController.this.hasPermission(event.getPlayer(), kit)))
/* 125:    */         {
/* 126:126 */           if (!KitController.this.canPurchase(gamePlayer, kit))
/* 127:    */           {
/* 128:127 */             event.getPlayer().sendMessage(new Messaging.MessageFormatter().format("error.not-enough-score"));
/* 129:128 */             return;
/* 130:    */           }
/* 131:131 */           gamePlayer.setScore(gamePlayer.getScore() - kit.getPoints());
/* 132:    */         }
/* 133:133 */         else if (!KitController.this.hasPermission(event.getPlayer(), kit))
/* 134:    */         {
/* 135:134 */           event.getPlayer().sendMessage(new Messaging.MessageFormatter().format("error.no-permission-kit"));
/* 136:135 */           return;
/* 137:    */         }
/* 138:138 */         event.setWillClose(true);
/* 139:139 */         event.setWillDestroy(true);
/* 140:    */         
/* 141:141 */         KitController.this.populateInventory(event.getPlayer().getInventory(), kit);
/* 142:142 */         gamePlayer.setChosenKit(true);
/* 143:    */         
/* 144:144 */         event.getPlayer().sendMessage(new Messaging.MessageFormatter().format("success.enjoy-kit"));
/* 145:    */       }
/* 146:    */     });
/* 147:148 */     for (int iii = 0; iii < availableKits.size(); iii++)
/* 148:    */     {
/* 149:149 */       if (iii >= 54) {
/* 150:    */         break;
/* 151:    */       }
/* 152:153 */       Kit kit = (Kit)availableKits.get(iii);
/* 153:154 */       List<String> loreList = Lists.newLinkedList();
/* 154:155 */       boolean canPurchase = false;
/* 155:157 */       if (isPurchaseAble(kit))
/* 156:    */       {
/* 157:158 */         loreList.add("§r§6Price§f: §" + (gamePlayer.getScore() >= kit.getPoints() ? 'a' : 'c') + kit.getPoints());
/* 158:159 */         loreList.add(" ");
/* 159:161 */         if (canPurchase(gamePlayer, kit)) {
/* 160:162 */           canPurchase = true;
/* 161:    */         }
/* 162:    */       }
/* 163:165 */       else if (!hasPermission(gamePlayer.getBukkitPlayer(), kit))
/* 164:    */       {
/* 165:166 */         loreList.add(new Messaging.MessageFormatter().format("kit.lores.no-permission"));
/* 166:167 */         loreList.add(" ");
/* 167:    */       }
/* 168:    */       else
/* 169:    */       {
/* 170:170 */         canPurchase = true;
/* 171:    */       }
/* 172:173 */       loreList.addAll(kit.getLores());
/* 173:    */       
/* 174:175 */       IconMenuController.get().setOption(
/* 175:176 */         gamePlayer.getBukkitPlayer(), 
/* 176:177 */         iii, 
/* 177:178 */         kit.getIcon(), 
/* 178:179 */         "§r§" + (canPurchase ? 'a' : 'c') + kit.getName(), 
/* 179:180 */         (String[])loreList.toArray(new String[loreList.size()]));
/* 180:    */     }
/* 181:183 */     IconMenuController.get().show(gamePlayer.getBukkitPlayer());
/* 182:    */   }
/* 183:    */   
/* 184:    */   public class Kit
/* 185:    */   {
/* 186:    */     private String name;
/* 187:    */     private int points;
/* 188:190 */     private List<ItemStack> items = Lists.newArrayList();
/* 189:    */     private ItemStack icon;
/* 190:    */     private List<String> lores;
/* 191:    */     
/* 192:    */     public Kit(String name, FileConfiguration storage)
/* 193:    */     {
/* 194:196 */       this.name = name;
/* 195:    */       String item;
/* 196:198 */       for (Iterator localIterator1 = storage.getStringList("items").iterator(); localIterator1.hasNext();)
/* 197:    */       {
/* 198:198 */         item = (String)localIterator1.next();
/* 199:199 */         ItemStack itemStack = ItemUtils.parseItem(item.split(" "));
/* 200:201 */         if (itemStack != null) {
/* 201:202 */           this.items.add(itemStack);
/* 202:    */         } else {
/* 203:204 */           LogUtils.log(Level.WARNING, getClass(), "Invalid item in kit: " + item, new Object[0]);
/* 204:    */         }
/* 205:    */       }
/* 206:208 */       this.points = storage.getInt("points", 0);
/* 207:    */       
/* 208:210 */       String icon = storage.getString("icon.material", "STONE");
/* 209:211 */       short data = (short)storage.getInt("icon.data", 0);
/* 210:212 */       ItemStack stack = ItemUtils.parseItem(icon + ":" + data);
/* 211:213 */       if (stack == null) {
/* 212:214 */         stack = new ItemStack(Material.STONE, 1, data);
/* 213:    */       }
/* 214:216 */       this.icon = stack;
/* 215:    */       
/* 216:218 */       this.lores = Lists.newLinkedList();
/* 217:219 */       if (storage.contains("details")) {
/* 218:220 */         for (String string : storage.getStringList("details")) {
/* 219:221 */           this.lores.add("§r" + ChatColor.translateAlternateColorCodes('&', string));
/* 220:    */         }
/* 221:    */       }
/* 222:225 */       this.lores.add("§r§eContents§f:");
/* 223:226 */       for (ItemStack itemStack : this.items) {
/* 224:227 */         this.lores.add("§r§c" + ItemUtils.itemName(itemStack));
/* 225:    */       }
/* 226:    */     }
/* 227:    */     
/* 228:    */     public Collection<ItemStack> getItems()
/* 229:    */     {
/* 230:232 */       return this.items;
/* 231:    */     }
/* 232:    */     
/* 233:    */     public String getName()
/* 234:    */     {
/* 235:236 */       return this.name;
/* 236:    */     }
/* 237:    */     
/* 238:    */     public int getPoints()
/* 239:    */     {
/* 240:240 */       return this.points;
/* 241:    */     }
/* 242:    */     
/* 243:    */     public ItemStack getIcon()
/* 244:    */     {
/* 245:244 */       return this.icon;
/* 246:    */     }
/* 247:    */     
/* 248:    */     public List<String> getLores()
/* 249:    */     {
/* 250:248 */       return this.lores;
/* 251:    */     }
/* 252:    */   }
/* 253:    */   
/* 254:    */   public static KitController get()
/* 255:    */   {
/* 256:253 */     if (instance == null) {
/* 257:254 */       instance = new KitController();
/* 258:    */     }
/* 259:257 */     return instance;
/* 260:    */   }
/* 261:    */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.controllers.KitController
 * JD-Core Version:    0.7.0.1
 */