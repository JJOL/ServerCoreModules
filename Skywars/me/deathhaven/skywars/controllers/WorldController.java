/*   1:    */ package me.deathhaven.skywars.controllers;
/*   2:    */ 
/*   3:    */ import com.google.common.collect.Lists;
/*   4:    */ import com.onarandombox.MultiverseCore.MultiverseCore;
/*   5:    */ import com.onarandombox.MultiverseCore.api.MVWorldManager;
/*   6:    */ import com.onarandombox.MultiverseCore.api.MultiverseWorld;
/*   7:    */ import com.sk89q.worldedit.CuboidClipboard;
/*   8:    */ import com.sk89q.worldedit.Vector;
/*   9:    */ import java.util.Collection;
/*  10:    */ import java.util.Map;
/*  11:    */ import java.util.Map.Entry;
/*  12:    */ import java.util.Queue;
/*  13:    */ import java.util.logging.Level;
/*  14:    */ import me.deathhaven.skywars.SkyWars;
/*  15:    */ import me.deathhaven.skywars.config.PluginConfig;
/*  16:    */ import me.deathhaven.skywars.game.Game;
/*  17:    */ import me.deathhaven.skywars.utilities.LogUtils;
/*  18:    */ import me.deathhaven.skywars.utilities.WEUtils;
/*  19:    */ import org.bukkit.Bukkit;
/*  20:    */ import org.bukkit.Difficulty;
/*  21:    */ import org.bukkit.Location;
/*  22:    */ import org.bukkit.Material;
/*  23:    */ import org.bukkit.Server;
/*  24:    */ import org.bukkit.World;
/*  25:    */ import org.bukkit.World.Environment;
/*  26:    */ import org.bukkit.WorldCreator;
/*  27:    */ import org.bukkit.WorldType;
/*  28:    */ import org.bukkit.block.Block;
/*  29:    */ import org.bukkit.plugin.PluginManager;
/*  30:    */ 
/*  31:    */ public class WorldController
/*  32:    */ {
/*  33:    */   private static final int PASTE_HEIGHT = 75;
/*  34:    */   private static int islandSize;
/*  35:    */   private static WorldController worldController;
/*  36:    */   private World islandWorld;
/*  37: 34 */   private final Queue<int[]> islandReferences = Lists.newLinkedList();
/*  38:    */   private int nextId;
/*  39:    */   
/*  40:    */   public WorldController()
/*  41:    */   {
/*  42: 38 */     generateGridReferences();
/*  43: 39 */     this.islandWorld = createWorld();
/*  44:    */   }
/*  45:    */   
/*  46:    */   private void generateGridReferences()
/*  47:    */   {
/*  48: 43 */     for (int xxx = 0; xxx < PluginConfig.getIslandsPerWorld(); xxx++) {
/*  49: 44 */       for (int zzz = 0; zzz < PluginConfig.getIslandsPerWorld(); zzz++)
/*  50:    */       {
/*  51: 45 */         int[] coordinates = { xxx, zzz };
/*  52: 47 */         if (!this.islandReferences.contains(coordinates)) {
/*  53: 48 */           this.islandReferences.add(coordinates);
/*  54:    */         }
/*  55:    */       }
/*  56:    */     }
/*  57:    */   }
/*  58:    */   
/*  59:    */   public World create(Game game, CuboidClipboard schematic)
/*  60:    */   {
/*  61: 55 */     if (this.islandReferences.size() == 0)
/*  62:    */     {
/*  63: 56 */       LogUtils.log(Level.INFO, getClass(), "No more free islands left. Generating new world.", new Object[0]);
/*  64:    */       
/*  65: 58 */       generateGridReferences();
/*  66: 59 */       this.islandWorld = createWorld();
/*  67:    */     }
/*  68: 62 */     int[] gridReference = (int[])this.islandReferences.poll();
/*  69: 63 */     game.setGridReference(gridReference);
/*  70:    */     
/*  71: 65 */     int gridX = gridReference[0];
/*  72: 66 */     int gridZ = gridReference[1];
/*  73: 67 */     int buffer = PluginConfig.getIslandBuffer();
/*  74:    */     
/*  75: 69 */     int originX = gridX * (Bukkit.getViewDistance() * 16 + islandSize * 2 + buffer * 2);
/*  76: 70 */     int originZ = gridZ * (Bukkit.getViewDistance() * 16 + islandSize * 2 + buffer * 2);
/*  77:    */     
/*  78: 72 */     game.setLocation(originX, originZ);
/*  79: 74 */     if (PluginConfig.buildSchematic()) {
/*  80: 75 */       WEUtils.buildSchematic(game, new Location(this.islandWorld, originX, 75.0D, originZ), schematic);
/*  81:    */     } else {
/*  82: 77 */       WEUtils.pasteSchematic(new Location(this.islandWorld, originX, 75.0D, originZ), schematic);
/*  83:    */     }
/*  84: 80 */     Map<Integer, Vector> spawns = SchematicController.get().getCachedSpawns(schematic);
/*  85: 81 */     Vector isleLocation = new Vector(originX, 75, originZ);
/*  86:    */     Vector spawn;
/*  87: 83 */     for (Map.Entry<Integer, Vector> entry : spawns.entrySet())
/*  88:    */     {
/*  89: 84 */       spawn = ((Vector)entry.getValue()).add(isleLocation).add(schematic.getOffset());
/*  90: 85 */       Location location = new Location(this.islandWorld, spawn.getBlockX(), spawn.getBlockY(), spawn.getBlockZ());
/*  91:    */       
/*  92: 87 */       game.addSpawn(((Integer)entry.getKey()).intValue(), location);
/*  93: 89 */       if ((PluginConfig.buildSchematic()) || (PluginConfig.buildCages())) {
/*  94: 90 */         createSpawnHousing(location);
/*  95:    */       }
/*  96:    */     }
/*  97: 94 */     Collection<Vector> chests = SchematicController.get().getCachedChests(schematic);
/*  98: 96 */     if (chests != null) {
/*  99: 97 */       for (Vector location : chests)
/* 100:    */       {
/* 101: 98 */         Vector spawn = location.add(isleLocation).add(schematic.getOffset());
/* 102: 99 */         Location chest = new Location(this.islandWorld, spawn.getBlockX(), spawn.getBlockY(), spawn.getBlockZ());
/* 103:    */         
/* 104:101 */         game.addChest(chest);
/* 105:    */       }
/* 106:    */     }
/* 107:105 */     return this.islandWorld;
/* 108:    */   }
/* 109:    */   
/* 110:    */   private void createSpawnHousing(Location location)
/* 111:    */   {
/* 112:109 */     World world = location.getWorld();
/* 113:    */     
/* 114:111 */     int x = location.getBlockX();
/* 115:112 */     int y = location.getBlockY();
/* 116:113 */     int z = location.getBlockZ();
/* 117:    */     
/* 118:115 */     world.getBlockAt(x, y - 1, z).setType(Material.GLASS);
/* 119:116 */     world.getBlockAt(x, y + 3, z).setType(Material.GLASS);
/* 120:    */     
/* 121:118 */     world.getBlockAt(x + 1, y, z).setType(Material.GLASS);
/* 122:119 */     world.getBlockAt(x + 1, y + 1, z).setType(Material.GLASS);
/* 123:120 */     world.getBlockAt(x + 1, y + 2, z).setType(Material.GLASS);
/* 124:    */     
/* 125:122 */     world.getBlockAt(x - 1, y, z).setType(Material.GLASS);
/* 126:123 */     world.getBlockAt(x - 1, y + 1, z).setType(Material.GLASS);
/* 127:124 */     world.getBlockAt(x - 1, y + 2, z).setType(Material.GLASS);
/* 128:    */     
/* 129:126 */     world.getBlockAt(x, y, z + 1).setType(Material.GLASS);
/* 130:127 */     world.getBlockAt(x, y + 1, z + 1).setType(Material.GLASS);
/* 131:128 */     world.getBlockAt(x, y + 2, z + 1).setType(Material.GLASS);
/* 132:    */     
/* 133:130 */     world.getBlockAt(x, y, z - 1).setType(Material.GLASS);
/* 134:131 */     world.getBlockAt(x, y + 1, z - 1).setType(Material.GLASS);
/* 135:132 */     world.getBlockAt(x, y + 2, z - 1).setType(Material.GLASS);
/* 136:    */   }
/* 137:    */   
/* 138:    */   private World createWorld()
/* 139:    */   {
/* 140:136 */     String worldName = "island-" + getNextId();
/* 141:137 */     World world = null;
/* 142:138 */     MultiverseCore mV = (MultiverseCore)SkyWars.get().getServer().getPluginManager().getPlugin("Multiverse-Core");
/* 143:139 */     if (mV != null)
/* 144:    */     {
/* 145:140 */       if (mV.getMVWorldManager().loadWorld(worldName)) {
/* 146:141 */         return mV.getMVWorldManager().getMVWorld(worldName).getCBWorld();
/* 147:    */       }
/* 148:143 */       Boolean ret = 
/* 149:144 */         Boolean.valueOf(mV.getMVWorldManager().addWorld(worldName, World.Environment.NORMAL, null, WorldType.NORMAL, Boolean.valueOf(false), "SkyWars", false));
/* 150:145 */       if (ret.booleanValue())
/* 151:    */       {
/* 152:146 */         MultiverseWorld mvWorld = mV.getMVWorldManager().getMVWorld(worldName);
/* 153:147 */         world = mvWorld.getCBWorld();
/* 154:148 */         mvWorld.setDifficulty(Difficulty.NORMAL.toString());
/* 155:149 */         mvWorld.setPVPMode(true);
/* 156:150 */         mvWorld.setEnableWeather(false);
/* 157:151 */         mvWorld.setKeepSpawnInMemory(false);
/* 158:152 */         mvWorld.setAllowAnimalSpawn(false);
/* 159:153 */         mvWorld.setAllowMonsterSpawn(false);
/* 160:    */       }
/* 161:    */     }
/* 162:156 */     if (world == null)
/* 163:    */     {
/* 164:157 */       WorldCreator worldCreator = new WorldCreator(worldName);
/* 165:158 */       worldCreator.environment(World.Environment.NORMAL);
/* 166:159 */       worldCreator.generateStructures(false);
/* 167:160 */       worldCreator.generator("SkyWars");
/* 168:161 */       world = worldCreator.createWorld();
/* 169:162 */       world.setDifficulty(Difficulty.NORMAL);
/* 170:163 */       world.setSpawnFlags(false, false);
/* 171:164 */       world.setPVP(true);
/* 172:165 */       world.setStorm(false);
/* 173:166 */       world.setThundering(false);
/* 174:167 */       world.setWeatherDuration(2147483647);
/* 175:168 */       world.setKeepSpawnInMemory(false);
/* 176:169 */       world.setTicksPerAnimalSpawns(0);
/* 177:170 */       world.setTicksPerMonsterSpawns(0);
/* 178:    */     }
/* 179:172 */     world.setAutoSave(false);
/* 180:173 */     world.setGameRuleValue("doFireTick", "false");
/* 181:    */     
/* 182:175 */     return world;
/* 183:    */   }
/* 184:    */   
/* 185:    */   private int getNextId()
/* 186:    */   {
/* 187:179 */     int id = this.nextId++;
/* 188:181 */     if (this.nextId == 2147483647) {
/* 189:182 */       this.nextId = 0;
/* 190:    */     }
/* 191:185 */     return id;
/* 192:    */   }
/* 193:    */   
/* 194:    */   public static WorldController get()
/* 195:    */   {
/* 196:189 */     if (worldController == null) {
/* 197:190 */       worldController = new WorldController();
/* 198:    */     }
/* 199:193 */     return worldController;
/* 200:    */   }
/* 201:    */   
/* 202:    */   public static void setIslandSize(int size)
/* 203:    */   {
/* 204:197 */     if (size > islandSize) {
/* 205:198 */       islandSize = size;
/* 206:    */     }
/* 207:    */   }
/* 208:    */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.controllers.WorldController
 * JD-Core Version:    0.7.0.1
 */