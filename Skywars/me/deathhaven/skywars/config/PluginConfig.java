/*   1:    */ package me.deathhaven.skywars.config;
/*   2:    */ 
/*   3:    */ import com.google.common.collect.Lists;
/*   4:    */ import java.io.File;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.util.List;
/*   7:    */ import java.util.Locale;
/*   8:    */ import me.deathhaven.skywars.SkyWars;
/*   9:    */ import me.deathhaven.skywars.utilities.LocationUtil;
/*  10:    */ import net.milkbowl.vault.permission.Permission;
/*  11:    */ import org.bukkit.Bukkit;
/*  12:    */ import org.bukkit.Location;
/*  13:    */ import org.bukkit.World;
/*  14:    */ import org.bukkit.configuration.file.FileConfiguration;
/*  15:    */ import org.bukkit.entity.Player;
/*  16:    */ 
/*  17:    */ public class PluginConfig
/*  18:    */ {
/*  19:    */   private static FileConfiguration storage;
/*  20:    */   private static Location lobbySpawn;
/*  21: 23 */   private static List<String> whitelistedCommands = ;
/*  22:    */   
/*  23:    */   static
/*  24:    */   {
/*  25: 26 */     storage = SkyWars.get().getConfig();
/*  26:    */     
/*  27: 28 */     lobbySpawn = LocationUtil.getLocation(Bukkit.getWorld(storage.getString("lobby.world")), storage.getString("lobby.spawn"));
/*  28: 29 */     if (storage.contains("whitelisted-commands")) {
/*  29: 30 */       whitelistedCommands = storage.getStringList("whitelisted-commands");
/*  30:    */     }
/*  31:    */   }
/*  32:    */   
/*  33:    */   public static Location getLobbySpawn()
/*  34:    */   {
/*  35: 35 */     return lobbySpawn;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public static void setLobbySpawn(Location location)
/*  39:    */   {
/*  40: 39 */     lobbySpawn = location.clone();
/*  41: 40 */     storage.set("lobby.world", lobbySpawn.getWorld().getName());
/*  42: 41 */     storage.set("lobby.spawn", String.format(Locale.US, "%.2f %.2f %.2f %.2f %.2f", new Object[] { Double.valueOf(location.getX()), Double.valueOf(location.getY()), Double.valueOf(location.getZ()), Float.valueOf(location.getYaw()), Float.valueOf(location.getPitch()) }));
/*  43: 42 */     saveConfig();
/*  44:    */   }
/*  45:    */   
/*  46:    */   public static int getLobbyRadius()
/*  47:    */   {
/*  48: 46 */     return storage.getInt("lobby.radius", 0);
/*  49:    */   }
/*  50:    */   
/*  51:    */   public static boolean isCommandWhitelisted(String command)
/*  52:    */   {
/*  53: 50 */     return whitelistedCommands.contains(command.replace("/", ""));
/*  54:    */   }
/*  55:    */   
/*  56:    */   public static int getIslandsPerWorld()
/*  57:    */   {
/*  58: 54 */     return storage.getInt("islands-per-row", 100);
/*  59:    */   }
/*  60:    */   
/*  61:    */   public static int getIslandBuffer()
/*  62:    */   {
/*  63: 58 */     return storage.getInt("island-buffer", 5);
/*  64:    */   }
/*  65:    */   
/*  66:    */   public static int getScorePerKill(Player player)
/*  67:    */   {
/*  68: 62 */     if (SkyWars.getPermission().hasGroupSupport())
/*  69:    */     {
/*  70: 63 */       String group = SkyWars.getPermission().getPrimaryGroup(player);
/*  71: 64 */       if (storage.contains("score.groups." + group + ".per-kill")) {
/*  72: 65 */         return storage.getInt("score.groups." + group + ".per-kill");
/*  73:    */       }
/*  74:    */     }
/*  75: 68 */     return storage.getInt("score.per-kill", 3);
/*  76:    */   }
/*  77:    */   
/*  78:    */   public static int getScorePerWin(Player player)
/*  79:    */   {
/*  80: 72 */     if (SkyWars.getPermission().hasGroupSupport())
/*  81:    */     {
/*  82: 73 */       String group = SkyWars.getPermission().getPrimaryGroup(player);
/*  83: 74 */       if (storage.contains("score.groups." + group + ".per-win")) {
/*  84: 75 */         return storage.getInt("score.groups." + group + ".per-win");
/*  85:    */       }
/*  86:    */     }
/*  87: 78 */     return storage.getInt("score.per-win", 10);
/*  88:    */   }
/*  89:    */   
/*  90:    */   public static int getScorePerDeath(Player player)
/*  91:    */   {
/*  92: 82 */     if (SkyWars.getPermission().hasGroupSupport())
/*  93:    */     {
/*  94: 83 */       String group = SkyWars.getPermission().getPrimaryGroup(player);
/*  95: 84 */       if (storage.contains("score.groups." + group + ".per-death")) {
/*  96: 85 */         return storage.getInt("score.groups." + group + ".per-death");
/*  97:    */       }
/*  98:    */     }
/*  99: 88 */     return storage.getInt("score.per-death", -1);
/* 100:    */   }
/* 101:    */   
/* 102:    */   public static int getScorePerLeave(Player player)
/* 103:    */   {
/* 104: 92 */     if (SkyWars.getPermission().hasGroupSupport())
/* 105:    */     {
/* 106: 93 */       String group = SkyWars.getPermission().getPrimaryGroup(player);
/* 107: 94 */       if (storage.contains("score.groups." + group + ".per-leave")) {
/* 108: 95 */         return storage.getInt("score.groups." + group + ".per-leave");
/* 109:    */       }
/* 110:    */     }
/* 111: 98 */     return storage.getInt("score.per-leave", -1);
/* 112:    */   }
/* 113:    */   
/* 114:    */   public static long getStatisticsUpdateInterval()
/* 115:    */   {
/* 116:102 */     return storage.getInt("statistics.update-interval", 600) * 20L;
/* 117:    */   }
/* 118:    */   
/* 119:    */   public static int getStatisticsTop()
/* 120:    */   {
/* 121:106 */     return storage.getInt("statistics.top", 30);
/* 122:    */   }
/* 123:    */   
/* 124:    */   public static boolean buildSchematic()
/* 125:    */   {
/* 126:110 */     return storage.getBoolean("island-building.enabled", false);
/* 127:    */   }
/* 128:    */   
/* 129:    */   public static int blocksPerTick()
/* 130:    */   {
/* 131:114 */     return storage.getInt("island-building.blocks-per-tick", 20);
/* 132:    */   }
/* 133:    */   
/* 134:    */   public static long buildInterval()
/* 135:    */   {
/* 136:118 */     return storage.getLong("island-building.interval", 1L);
/* 137:    */   }
/* 138:    */   
/* 139:    */   public static boolean buildCages()
/* 140:    */   {
/* 141:122 */     return storage.getBoolean("build-cages", true);
/* 142:    */   }
/* 143:    */   
/* 144:    */   public static boolean ignoreAir()
/* 145:    */   {
/* 146:126 */     return storage.getBoolean("ignore-air", false);
/* 147:    */   }
/* 148:    */   
/* 149:    */   public static boolean fillEmptyChests()
/* 150:    */   {
/* 151:130 */     return storage.getBoolean("fill-empty-chests", true);
/* 152:    */   }
/* 153:    */   
/* 154:    */   public static boolean fillPopulatedChests()
/* 155:    */   {
/* 156:134 */     return storage.getBoolean("fill-populated-chests", true);
/* 157:    */   }
/* 158:    */   
/* 159:    */   public static boolean useEconomy()
/* 160:    */   {
/* 161:138 */     return storage.getBoolean("use-economy", false);
/* 162:    */   }
/* 163:    */   
/* 164:    */   public static boolean disableKits()
/* 165:    */   {
/* 166:142 */     return storage.getBoolean("disable-kits", false);
/* 167:    */   }
/* 168:    */   
/* 169:    */   public static boolean enableSounds()
/* 170:    */   {
/* 171:146 */     return storage.getBoolean("enable-soundeffects", true);
/* 172:    */   }
/* 173:    */   
/* 174:    */   public static boolean chatHandledByOtherPlugin()
/* 175:    */   {
/* 176:150 */     return storage.getBoolean("chat-handled-by-other-plugin", false);
/* 177:    */   }
/* 178:    */   
/* 179:    */   public static boolean clearInventory()
/* 180:    */   {
/* 181:154 */     return storage.getBoolean("clear-inventory-on-join", true);
/* 182:    */   }
/* 183:    */   
/* 184:    */   public static boolean saveInventory()
/* 185:    */   {
/* 186:158 */     return storage.getBoolean("save-inventory", false);
/* 187:    */   }
/* 188:    */   
/* 189:    */   public static void setSchematicConfig(String schematicFile, int playerSize)
/* 190:    */   {
/* 191:162 */     String schematicPath = "schematics." + schematicFile.replace(".schematic", "");
/* 192:163 */     if (!storage.isSet(schematicPath))
/* 193:    */     {
/* 194:164 */       storage.set(schematicPath + ".min-players", Integer.valueOf(playerSize));
/* 195:165 */       storage.set(schematicPath + ".timer", Integer.valueOf(11));
/* 196:166 */       saveConfig();
/* 197:    */     }
/* 198:    */   }
/* 199:    */   
/* 200:    */   public static void migrateConfig()
/* 201:    */   {
/* 202:171 */     if (storage.isSet("fill-chests"))
/* 203:    */     {
/* 204:172 */       Boolean fill = Boolean.valueOf(storage.getBoolean("fill-chests"));
/* 205:173 */       storage.set("fill-empty-chests", fill);
/* 206:174 */       storage.set("fill-populated-chests", fill);
/* 207:175 */       storage.set("fill-chests", null);
/* 208:    */     }
/* 209:177 */     if (!storage.isSet("lobby.radius")) {
/* 210:178 */       storage.set("lobby.radius", Integer.valueOf(0));
/* 211:    */     }
/* 212:180 */     if (storage.isSet("island-size")) {
/* 213:181 */       storage.set("island-size", null);
/* 214:    */     }
/* 215:183 */     if (!storage.isSet("island-buffer")) {
/* 216:184 */       storage.set("island-buffer", Integer.valueOf(5));
/* 217:    */     }
/* 218:186 */     if (!storage.isSet("disable-kits")) {
/* 219:187 */       storage.set("disable-kits", Boolean.valueOf(false));
/* 220:    */     }
/* 221:189 */     if (!storage.isSet("enable-soundeffects")) {
/* 222:190 */       storage.set("enable-soundeffects", Boolean.valueOf(false));
/* 223:    */     }
/* 224:192 */     saveConfig();
/* 225:    */   }
/* 226:    */   
/* 227:    */   private static boolean saveConfig()
/* 228:    */   {
/* 229:196 */     File file = new File("./plugins/SkyWars/config.yml");
/* 230:    */     try
/* 231:    */     {
/* 232:198 */       storage.save(file);
/* 233:199 */       return true;
/* 234:    */     }
/* 235:    */     catch (IOException ignored) {}
/* 236:201 */     return false;
/* 237:    */   }
/* 238:    */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.config.PluginConfig
 * JD-Core Version:    0.7.0.1
 */