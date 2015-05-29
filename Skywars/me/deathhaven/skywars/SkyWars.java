/*   1:    */ 
/*   2:    */ 
/*   3:    */ com.onarandombox.MultiverseCore.MultiverseCore
/*   4:    */ com.onarandombox.MultiverseCore.api.MVWorldManager
/*   5:    */ java.io.File
/*   6:    */ java.io.IOException
/*   7:    */ java.sql.SQLException
/*   8:    */ java.util.logging.Level
/*   9:    */ java.util.logging.Logger
/*  10:    */ me.deathhaven.skywars.commands.MainCommand
/*  11:    */ me.deathhaven.skywars.config.PluginConfig
/*  12:    */ me.deathhaven.skywars.controllers.ChestController
/*  13:    */ me.deathhaven.skywars.controllers.GameController
/*  14:    */ me.deathhaven.skywars.controllers.IconMenuController
/*  15:    */ me.deathhaven.skywars.controllers.KitController
/*  16:    */ me.deathhaven.skywars.controllers.PlayerController
/*  17:    */ me.deathhaven.skywars.controllers.SchematicController
/*  18:    */ me.deathhaven.skywars.controllers.WorldController
/*  19:    */ me.deathhaven.skywars.database.Database
/*  20:    */ me.deathhaven.skywars.listeners.BlockListener
/*  21:    */ me.deathhaven.skywars.listeners.EntityListener
/*  22:    */ me.deathhaven.skywars.listeners.InventoryListener
/*  23:    */ me.deathhaven.skywars.listeners.PlayerListener
/*  24:    */ me.deathhaven.skywars.metrics.MetricsLite
/*  25:    */ me.deathhaven.skywars.player.GamePlayer
/*  26:    */ me.deathhaven.skywars.storage.DataStorage
/*  27:    */ me.deathhaven.skywars.storage.DataStorage.DataStorageType
/*  28:    */ me.deathhaven.skywars.storage.SQLStorage
/*  29:    */ me.deathhaven.skywars.storage.SQLStorage.SaveProcessor
/*  30:    */ me.deathhaven.skywars.tasks.SyncTask
/*  31:    */ me.deathhaven.skywars.utilities.CraftBukkitUtil
/*  32:    */ me.deathhaven.skywars.utilities.FileUtils
/*  33:    */ me.deathhaven.skywars.utilities.Messaging
/*  34:    */ me.deathhaven.skywars.utilities.Messaging.MessageFormatter
/*  35:    */ me.deathhaven.skywars.utilities.StringUtils
/*  36:    */ me.deathhaven.skywars.utilities.WorldGenerator
/*  37:    */ net.milkbowl.vault.chat.Chat
/*  38:    */ net.milkbowl.vault.economy.Economy
/*  39:    */ net.milkbowl.vault.permission.Permission
/*  40:    */ org.bukkit.Bukkit
/*  41:    */ org.bukkit.Server
/*  42:    */ org.bukkit.World
/*  43:    */ org.bukkit.command.Command
/*  44:    */ org.bukkit.command.CommandExecutor
/*  45:    */ org.bukkit.command.CommandSender
/*  46:    */ org.bukkit.command.PluginCommand
/*  47:    */ org.bukkit.configuration.file.FileConfiguration
/*  48:    */ org.bukkit.configuration.file.FileConfigurationOptions
/*  49:    */ org.bukkit.entity.Player
/*  50:    */ org.bukkit.plugin.PluginManager
/*  51:    */ org.bukkit.plugin.RegisteredServiceProvider
/*  52:    */ org.bukkit.plugin.ServicesManager
/*  53:    */ org.bukkit.plugin.java.JavaPlugin
/*  54:    */ org.bukkit.scheduler.BukkitScheduler
/*  55:    */ 
/*  56:    */ SkyWars
/*  57:    */   
/*  58:    */ 
/*  59:    */   instance
/*  60:    */   permission
/*  61:    */   economy
/*  62:    */   chat
/*  63:    */   database
/*  64:    */   
/*  65:    */   onEnable
/*  66:    */   
/*  67: 57 */     instance = 
/*  68:    */     
/*  69: 59 */     deleteIslandWorlds()
/*  70:    */     
/*  71: 61 */     getConfig()options()copyDefaults
/*  72: 62 */     saveDefaultConfig()
/*  73: 63 */     migrateConfig()
/*  74: 64 */     reloadConfig()
/*  75:    */     
/*  76: 66 */     
/*  77:    */     
/*  78: 68 */     getCommand"skywars"setExecutor()
/*  79: 69 */     getCommand"global"setExecutor()
/*  80:    */     
/*  81:    */       onCommand, , , []
/*  82:    */       
/*  83: 72 */          ( {
/*  84: 73 */           
/*  85:    */         
/*  86: 76 */          (length==0
/*  87:    */         
/*  88: 77 */           sendMessage"§cUsage: /"" <message>"
/*  89: 78 */           
/*  90:    */         
/*  91: 81 */          = ()
/*  92: 82 */          ( : 
/*  93:    */         
/*  94: 83 */           append
/*  95: 84 */           append" "
/*  96:    */         
/*  97: 87 */          = get()get
/*  98: 88 */          = formatScoregetScore()
/*  99:    */         
/* 100: 90 */          = 
/* 101: 91 */          (getChat()!= {
/* 102: 92 */            = getChat()getPlayerPrefixgetBukkitPlayer()
/* 103:    */         
/* 104: 94 */          (== {
/* 105: 95 */            = ""
/* 106:    */         
/* 107: 97 */         broadcastMessage()
/* 108: 98 */           setVariable"player", getBukkitPlayer()getDisplayName()
/* 109: 99 */           setVariable"score", 
/* 110:100 */           setVariable"message", stripColortoString()
/* 111:    */           
/* 112:102 */           format"chat.global"
/* 113:    */         
/* 114:104 */         
/* 115:    */       
/* 116:    */     
/* 117:    */     
/* 118:    */     
/* 119:109 */        = valueOfgetConfig()getString"data-storage", "FILE"
/* 120:110 */        (==SQLsetupDatabase()
/* 121:    */       
/* 122:111 */         getLogger()logINFO, "Couldn't setup database, now using file storage."
/* 123:112 */         setInstanceFILE
/* 124:    */       
/* 125:    */       
/* 126:    */       
/* 127:115 */         setInstance
/* 128:    */       
/* 129:    */     
/* 130:    */      (
/* 131:    */     
/* 132:119 */       setInstanceFILE
/* 133:    */     
/* 134:122 */     setupPermission()
/* 135:123 */     setupEconomy()
/* 136:124 */     setupChat()
/* 137:    */     
/* 138:126 */     get()
/* 139:127 */     get()
/* 140:128 */     get()
/* 141:129 */     get()
/* 142:130 */     get()
/* 143:131 */      (disableKits() {
/* 144:132 */       get()
/* 145:    */     
/* 146:134 */     get()
/* 147:    */     
/* 148:    */     
/* 149:137 */        = 
/* 150:138 */       start()
/* 151:    */     
/* 152:    */      ( {}
/* 153:147 */     getPluginManager()registerEvents(), 
/* 154:148 */     getPluginManager()registerEvents(), 
/* 155:149 */     getPluginManager()registerEvents(), 
/* 156:150 */     getPluginManager()registerEvents(), 
/* 157:    */     
/* 158:152 */     getScheduler()scheduleSyncRepeatingTask, (), 20L, 20L
/* 159:    */   
/* 160:    */   
/* 161:    */   onDisable
/* 162:    */   
/* 163:159 */     get()shutdown()
/* 164:160 */     get()shutdown()
/* 165:162 */      (get()isRunning()
/* 166:    */     
/* 167:163 */        = get()
/* 168:164 */        (saveProcessor.isEmpty()) {}
/* 169:165 */       long currentTime = System.currentTimeMillis();
/* 170:166 */       while (System.currentTimeMillis() - currentTime < 1000L) {}
/* 171:167 */       sqlStorage.saveProcessor.stop();
/* 172:    */     }
/* 173:170 */     if (this.database != null) {
/* 174:171 */       this.database.close();
/* 175:    */     }
/* 176:174 */     deleteIslandWorlds();
/* 177:    */   }
/* 178:    */   
/* 179:    */   private void deleteIslandWorlds()
/* 180:    */   {
/* 181:179 */     File workingDirectory = new File(".");
/* 182:180 */     File[] contents = workingDirectory.listFiles();
/* 183:182 */     if (contents != null) {
/* 184:183 */       for (File file : contents) {
/* 185:184 */         if ((file.isDirectory()) && (file.getName().matches("island-\\d+")))
/* 186:    */         {
/* 187:187 */           World world = getServer().getWorld(file.getName());
/* 188:188 */           Boolean result = Boolean.valueOf(false);
/* 189:189 */           if (Bukkit.getPluginManager().getPlugin("Multiverse-Core") != null)
/* 190:    */           {
/* 191:190 */             MultiverseCore multiVerse = (MultiverseCore)Bukkit.getPluginManager().getPlugin("Multiverse-Core");
/* 192:191 */             if (world != null) {
/* 193:    */               try
/* 194:    */               {
/* 195:193 */                 result = Boolean.valueOf(multiVerse.getMVWorldManager().deleteWorld(world.getName()));
/* 196:    */               }
/* 197:    */               catch (IllegalArgumentException ignored)
/* 198:    */               {
/* 199:195 */                 result = Boolean.valueOf(false);
/* 200:    */               }
/* 201:    */             } else {
/* 202:198 */               result = Boolean.valueOf(multiVerse.getMVWorldManager().removeWorldFromConfig(file.getName()));
/* 203:    */             }
/* 204:    */           }
/* 205:201 */           if (!result.booleanValue())
/* 206:    */           {
/* 207:202 */             if (world != null)
/* 208:    */             {
/* 209:203 */               result = Boolean.valueOf(getServer().unloadWorld(world, true));
/* 210:204 */               if (result.booleanValue()) {
/* 211:205 */                 getLogger().log(Level.INFO, "World ''{0}'' was unloaded from memory.", file.getName());
/* 212:    */               } else {
/* 213:207 */                 getLogger().log(Level.SEVERE, "World ''{0}'' could not be unloaded.", file.getName());
/* 214:    */               }
/* 215:    */             }
/* 216:210 */             result = Boolean.valueOf(FileUtils.deleteFolder(file));
/* 217:211 */             if (result.booleanValue())
/* 218:    */             {
/* 219:212 */               getLogger().log(Level.INFO, "World ''{0}'' was deleted.", file.getName());
/* 220:    */             }
/* 221:    */             else
/* 222:    */             {
/* 223:214 */               getLogger().log(Level.SEVERE, "World ''{0}'' was NOT deleted.", file.getName());
/* 224:215 */               getLogger().log(Level.SEVERE, "Are you sure the folder {0} exists?", file.getName());
/* 225:216 */               getLogger().log(Level.SEVERE, "Please check your file permissions on ''{0}''", file.getName());
/* 226:    */             }
/* 227:    */           }
/* 228:    */         }
/* 229:    */       }
/* 230:    */     }
/* 231:223 */     workingDirectory = new File("./plugins/WorldGuard/worlds/");
/* 232:224 */     contents = workingDirectory.listFiles();
/* 233:226 */     if (contents != null) {
/* 234:227 */       for (File file : contents) {
/* 235:228 */         if ((file.isDirectory()) && (file.getName().matches("island-\\d+"))) {
/* 236:232 */           FileUtils.deleteFolder(file);
/* 237:    */         }
/* 238:    */       }
/* 239:    */     }
/* 240:    */   }
/* 241:    */   
/* 242:    */   private boolean setupDatabase()
/* 243:    */   {
/* 244:    */     try
/* 245:    */     {
/* 246:239 */       this.database = new Database(getConfig().getConfigurationSection("database"));
/* 247:    */     }
/* 248:    */     catch (ClassNotFoundException exception)
/* 249:    */     {
/* 250:242 */       getLogger().log(Level.SEVERE, String.format("Unable to register JDCB driver: %s", new Object[] { exception.getMessage() }));
/* 251:243 */       return false;
/* 252:    */     }
/* 253:    */     catch (SQLException exception)
/* 254:    */     {
/* 255:246 */       getLogger().log(Level.SEVERE, String.format("Unable to connect to SQL server: %s", new Object[] { exception.getMessage() }));
/* 256:247 */       return false;
/* 257:    */     }
/* 258:    */     try
/* 259:    */     {
/* 260:251 */       this.database.createTables();
/* 261:    */     }
/* 262:    */     catch (Exception exception)
/* 263:    */     {
/* 264:253 */       getLogger().log(Level.SEVERE, String.format("An exception was thrown while attempting to create tables: %s", new Object[] { exception.getMessage() }));
/* 265:254 */       return false;
/* 266:    */     }
/* 267:257 */     return true;
/* 268:    */   }
/* 269:    */   
/* 270:    */   private void setupPermission()
/* 271:    */   {
/* 272:261 */     RegisteredServiceProvider<Permission> chatProvider = getServer().getServicesManager().getRegistration(Permission.class);
/* 273:262 */     if (chatProvider != null) {
/* 274:263 */       permission = (Permission)chatProvider.getProvider();
/* 275:    */     }
/* 276:    */   }
/* 277:    */   
/* 278:    */   private void setupEconomy()
/* 279:    */   {
/* 280:268 */     RegisteredServiceProvider<Economy> chatProvider = getServer().getServicesManager().getRegistration(Economy.class);
/* 281:269 */     if (chatProvider != null) {
/* 282:270 */       economy = (Economy)chatProvider.getProvider();
/* 283:    */     }
/* 284:    */   }
/* 285:    */   
/* 286:    */   private void setupChat()
/* 287:    */   {
/* 288:275 */     RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(Chat.class);
/* 289:276 */     if (chatProvider != null) {
/* 290:277 */       chat = (Chat)chatProvider.getProvider();
/* 291:    */     }
/* 292:    */   }
/* 293:    */   
/* 294:    */   public static SkyWars get()
/* 295:    */   {
/* 296:282 */     return instance;
/* 297:    */   }
/* 298:    */   
/* 299:    */   public static Permission getPermission()
/* 300:    */   {
/* 301:286 */     return permission;
/* 302:    */   }
/* 303:    */   
/* 304:    */   public static Economy getEconomy()
/* 305:    */   {
/* 306:290 */     return economy;
/* 307:    */   }
/* 308:    */   
/* 309:    */   public static Chat getChat()
/* 310:    */   {
/* 311:294 */     return chat;
/* 312:    */   }
/* 313:    */   
/* 314:    */   public static Database getDB()
/* 315:    */   {
/* 316:298 */     return instance.database;
/* 317:    */   }
/* 318:    */   
/* 319:    */   public WorldGenerator getDefaultWorldGenerator(String worldName, String id)
/* 320:    */   {
/* 321:303 */     return new WorldGenerator();
/* 322:    */   }
/* 323:    */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.SkyWars
 * JD-Core Version:    0.7.0.1
 */