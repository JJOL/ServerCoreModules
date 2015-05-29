/*   1:    */ package me.deathhaven.skywars.controllers;
/*   2:    */ 
/*   3:    */ import com.google.common.collect.Lists;
/*   4:    */ import com.google.common.collect.Maps;
/*   5:    */ import com.sk89q.worldedit.CuboidClipboard;
/*   6:    */ import com.sk89q.worldedit.Vector;
/*   7:    */ import com.sk89q.worldedit.blocks.BaseBlock;
/*   8:    */ import com.sk89q.worldedit.data.DataException;
/*   9:    */ import com.sk89q.worldedit.schematic.SchematicFormat;
/*  10:    */ import java.io.File;
/*  11:    */ import java.io.IOException;
/*  12:    */ import java.util.Collection;
/*  13:    */ import java.util.List;
/*  14:    */ import java.util.Map;
/*  15:    */ import java.util.Map.Entry;
/*  16:    */ import java.util.Random;
/*  17:    */ import java.util.logging.Level;
/*  18:    */ import me.deathhaven.skywars.SkyWars;
/*  19:    */ import me.deathhaven.skywars.config.PluginConfig;
/*  20:    */ import me.deathhaven.skywars.utilities.LogUtils;
/*  21:    */ import org.bukkit.Bukkit;
/*  22:    */ import org.bukkit.Material;
/*  23:    */ import org.bukkit.scheduler.BukkitScheduler;
/*  24:    */ 
/*  25:    */ public class SchematicController
/*  26:    */ {
/*  27:    */   private static SchematicController instance;
/*  28: 29 */   private final Random random = new Random();
/*  29: 30 */   private final Map<String, CuboidClipboard> schematicMap = Maps.newHashMap();
/*  30: 31 */   private final Map<CuboidClipboard, Map<Integer, Vector>> spawnCache = Maps.newHashMap();
/*  31: 32 */   private final Map<CuboidClipboard, List<Vector>> chestCache = Maps.newHashMap();
/*  32: 33 */   private int schematicSize = 0;
/*  33:    */   
/*  34:    */   public SchematicController()
/*  35:    */   {
/*  36: 36 */     File dataDirectory = SkyWars.get().getDataFolder();
/*  37: 37 */     File schematicsDirectory = new File(dataDirectory, "schematics");
/*  38: 39 */     if ((!schematicsDirectory.exists()) && (!schematicsDirectory.mkdirs())) {
/*  39: 40 */       return;
/*  40:    */     }
/*  41: 43 */     File[] schematics = schematicsDirectory.listFiles();
/*  42: 44 */     if (schematics == null) {
/*  43: 45 */       return;
/*  44:    */     }
/*  45: 48 */     for (File schematic : schematics) {
/*  46: 49 */       if (schematic.getName().endsWith(".schematic")) {
/*  47: 53 */         if (!schematic.isFile())
/*  48:    */         {
/*  49: 54 */           LogUtils.log(Level.INFO, getClass(), "Could not load schematic %s: Not a file", new Object[] { schematic.getName() });
/*  50:    */         }
/*  51:    */         else
/*  52:    */         {
/*  53: 58 */           SchematicFormat schematicFormat = SchematicFormat.getFormat(schematic);
/*  54: 59 */           if (schematicFormat == null) {
/*  55: 60 */             LogUtils.log(Level.INFO, getClass(), "Could not load schematic %s: Unable to determine schematic format", new Object[] { schematic.getName() });
/*  56:    */           } else {
/*  57:    */             try
/*  58:    */             {
/*  59: 65 */               registerSchematic(schematic.getName().replace(".schematic", ""), schematicFormat.load(schematic));
/*  60:    */             }
/*  61:    */             catch (DataException e)
/*  62:    */             {
/*  63: 67 */               LogUtils.log(Level.INFO, getClass(), "Could not load schematic %s: %s", new Object[] { schematic.getName(), e.getMessage() });
/*  64:    */             }
/*  65:    */             catch (IOException e)
/*  66:    */             {
/*  67: 69 */               LogUtils.log(Level.INFO, getClass(), "Could not load schematic %s: %s", new Object[] { schematic.getName(), e.getMessage() });
/*  68:    */             }
/*  69:    */           }
/*  70:    */         }
/*  71:    */       }
/*  72:    */     }
/*  73: 73 */     LogUtils.log(Level.INFO, getClass(), "Registered %d schematics ...", new Object[] { Integer.valueOf(this.schematicSize) });
/*  74:    */   }
/*  75:    */   
/*  76:    */   private void registerSchematic(final String name, final CuboidClipboard schematic)
/*  77:    */   {
/*  78: 78 */     Bukkit.getScheduler().runTaskAsynchronously(SkyWars.get(), new Runnable()
/*  79:    */     {
/*  80:    */       public void run()
/*  81:    */       {
/*  82: 83 */         int spawnId = 0;
/*  83: 85 */         for (int y = 0; y < schematic.getSize().getBlockY(); y++) {
/*  84: 86 */           for (int x = 0; x < schematic.getSize().getBlockX(); x++) {
/*  85: 87 */             for (int z = 0; z < schematic.getSize().getBlockZ(); z++)
/*  86:    */             {
/*  87: 88 */               Vector currentPoint = new Vector(x, y, z);
/*  88: 89 */               int currentBlock = schematic.getPoint(currentPoint).getType();
/*  89: 91 */               if (currentBlock != 0) {
/*  90: 95 */                 if ((currentBlock == Material.SIGN_POST.getId()) || (currentBlock == Material.BEACON.getId()))
/*  91:    */                 {
/*  92: 96 */                   SchematicController.this.cacheSpawn(schematic, spawnId++, currentPoint);
/*  93: 97 */                   schematic.setBlock(currentPoint, new BaseBlock(0));
/*  94:    */                 }
/*  95: 99 */                 else if (currentBlock == Material.CHEST.getId())
/*  96:    */                 {
/*  97:100 */                   SchematicController.this.cacheChest(schematic, currentPoint);
/*  98:    */                 }
/*  99:    */               }
/* 100:    */             }
/* 101:    */           }
/* 102:    */         }
/* 103:106 */         if (spawnId <= 1)
/* 104:    */         {
/* 105:107 */           SchematicController.this.noSpawnsNotifier(name);
/* 106:108 */           return;
/* 107:    */         }
/* 108:110 */         SchematicController.this.schematicMap.put(name, schematic);
/* 109:111 */         PluginConfig.setSchematicConfig(name, spawnId);
/* 110:112 */         int length = schematic.getLength();
/* 111:113 */         int width = schematic.getWidth();
/* 112:114 */         WorldController.setIslandSize(length > width ? length : width);
/* 113:    */       }
/* 114:116 */     });
/* 115:117 */     this.schematicSize += 1;
/* 116:    */   }
/* 117:    */   
/* 118:    */   public CuboidClipboard getRandom()
/* 119:    */   {
/* 120:121 */     List<CuboidClipboard> schematics = Lists.newArrayList(this.schematicMap.values());
/* 121:122 */     return (CuboidClipboard)schematics.get(this.random.nextInt(schematics.size()));
/* 122:    */   }
/* 123:    */   
/* 124:    */   public String getName(CuboidClipboard cuboidClipboard)
/* 125:    */   {
/* 126:126 */     for (Map.Entry<String, CuboidClipboard> entry : this.schematicMap.entrySet()) {
/* 127:127 */       if (((CuboidClipboard)entry.getValue()).equals(cuboidClipboard)) {
/* 128:128 */         return (String)entry.getKey();
/* 129:    */       }
/* 130:    */     }
/* 131:132 */     return null;
/* 132:    */   }
/* 133:    */   
/* 134:    */   public void cacheSpawn(CuboidClipboard schematic, int position, Vector location)
/* 135:    */   {
/* 136:    */     Map<Integer, Vector> spawnPlaces;
/* 137:    */     Map<Integer, Vector> spawnPlaces;
/* 138:138 */     if (this.spawnCache.containsKey(schematic)) {
/* 139:139 */       spawnPlaces = (Map)this.spawnCache.get(schematic);
/* 140:    */     } else {
/* 141:142 */       spawnPlaces = Maps.newHashMap();
/* 142:    */     }
/* 143:145 */     spawnPlaces.put(Integer.valueOf(position), location);
/* 144:146 */     this.spawnCache.put(schematic, spawnPlaces);
/* 145:    */   }
/* 146:    */   
/* 147:    */   public void noSpawnsNotifier(String name)
/* 148:    */   {
/* 149:150 */     LogUtils.log(Level.SEVERE, getClass(), String.format("Schematic '" + name + "' does not have any spawns set!", new Object[0]), new Object[0]);
/* 150:    */   }
/* 151:    */   
/* 152:    */   public Map<Integer, Vector> getCachedSpawns(CuboidClipboard schematic)
/* 153:    */   {
/* 154:154 */     return (Map)this.spawnCache.get(schematic);
/* 155:    */   }
/* 156:    */   
/* 157:    */   private void cacheChest(CuboidClipboard schematic, Vector location)
/* 158:    */   {
/* 159:    */     List<Vector> chestList;
/* 160:    */     List<Vector> chestList;
/* 161:160 */     if (this.chestCache.containsKey(schematic)) {
/* 162:161 */       chestList = (List)this.chestCache.get(schematic);
/* 163:    */     } else {
/* 164:163 */       chestList = Lists.newArrayList();
/* 165:    */     }
/* 166:166 */     chestList.add(location);
/* 167:167 */     this.chestCache.put(schematic, chestList);
/* 168:    */   }
/* 169:    */   
/* 170:    */   public Collection<Vector> getCachedChests(CuboidClipboard schematic)
/* 171:    */   {
/* 172:171 */     return (Collection)this.chestCache.get(schematic);
/* 173:    */   }
/* 174:    */   
/* 175:    */   public int size()
/* 176:    */   {
/* 177:175 */     return this.schematicMap.size();
/* 178:    */   }
/* 179:    */   
/* 180:    */   public void remove(String schematic)
/* 181:    */   {
/* 182:179 */     this.schematicMap.remove(schematic);
/* 183:    */   }
/* 184:    */   
/* 185:    */   public static SchematicController get()
/* 186:    */   {
/* 187:183 */     if (instance == null) {
/* 188:184 */       instance = new SchematicController();
/* 189:    */     }
/* 190:187 */     return instance;
/* 191:    */   }
/* 192:    */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.controllers.SchematicController
 * JD-Core Version:    0.7.0.1
 */