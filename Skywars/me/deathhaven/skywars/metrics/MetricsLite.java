/*   1:    */ package me.deathhaven.skywars.metrics;
/*   2:    */ 
/*   3:    */ import java.io.BufferedReader;
/*   4:    */ import java.io.ByteArrayOutputStream;
/*   5:    */ import java.io.File;
/*   6:    */ import java.io.IOException;
/*   7:    */ import java.io.InputStreamReader;
/*   8:    */ import java.io.OutputStream;
/*   9:    */ import java.io.PrintStream;
/*  10:    */ import java.io.UnsupportedEncodingException;
/*  11:    */ import java.net.Proxy;
/*  12:    */ import java.net.URL;
/*  13:    */ import java.net.URLConnection;
/*  14:    */ import java.net.URLEncoder;
/*  15:    */ import java.util.Collection;
/*  16:    */ import java.util.UUID;
/*  17:    */ import java.util.logging.Level;
/*  18:    */ import java.util.logging.Logger;
/*  19:    */ import java.util.zip.GZIPOutputStream;
/*  20:    */ import org.bukkit.Bukkit;
/*  21:    */ import org.bukkit.Server;
/*  22:    */ import org.bukkit.configuration.InvalidConfigurationException;
/*  23:    */ import org.bukkit.configuration.file.YamlConfiguration;
/*  24:    */ import org.bukkit.configuration.file.YamlConfigurationOptions;
/*  25:    */ import org.bukkit.plugin.Plugin;
/*  26:    */ import org.bukkit.plugin.PluginDescriptionFile;
/*  27:    */ import org.bukkit.scheduler.BukkitScheduler;
/*  28:    */ import org.bukkit.scheduler.BukkitTask;
/*  29:    */ 
/*  30:    */ public class MetricsLite
/*  31:    */ {
/*  32:    */   private static final int REVISION = 7;
/*  33:    */   private static final String BASE_URL = "http://report.mcstats.org";
/*  34:    */   private static final String REPORT_URL = "/plugin/%s";
/*  35:    */   private static final int PING_INTERVAL = 15;
/*  36:    */   private final Plugin plugin;
/*  37:    */   private final YamlConfiguration configuration;
/*  38:    */   private final File configurationFile;
/*  39:    */   private final String guid;
/*  40:    */   private final boolean debug;
/*  41:103 */   private final Object optOutLock = new Object();
/*  42:108 */   private volatile BukkitTask task = null;
/*  43:    */   
/*  44:    */   public MetricsLite(Plugin plugin)
/*  45:    */     throws IOException
/*  46:    */   {
/*  47:111 */     if (plugin == null) {
/*  48:112 */       throw new IllegalArgumentException("Plugin cannot be null");
/*  49:    */     }
/*  50:115 */     this.plugin = plugin;
/*  51:    */     
/*  52:    */ 
/*  53:118 */     this.configurationFile = getConfigFile();
/*  54:119 */     this.configuration = YamlConfiguration.loadConfiguration(this.configurationFile);
/*  55:    */     
/*  56:    */ 
/*  57:122 */     this.configuration.addDefault("opt-out", Boolean.valueOf(false));
/*  58:123 */     this.configuration.addDefault("guid", UUID.randomUUID().toString());
/*  59:124 */     this.configuration.addDefault("debug", Boolean.valueOf(false));
/*  60:127 */     if (this.configuration.get("guid", null) == null)
/*  61:    */     {
/*  62:128 */       this.configuration.options().header("http://mcstats.org").copyDefaults(true);
/*  63:129 */       this.configuration.save(this.configurationFile);
/*  64:    */     }
/*  65:133 */     this.guid = this.configuration.getString("guid");
/*  66:134 */     this.debug = this.configuration.getBoolean("debug", false);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public boolean start()
/*  70:    */   {
/*  71:145 */     synchronized (this.optOutLock)
/*  72:    */     {
/*  73:147 */       if (isOptOut()) {
/*  74:148 */         return false;
/*  75:    */       }
/*  76:152 */       if (this.task != null) {
/*  77:153 */         return true;
/*  78:    */       }
/*  79:157 */       this.task = this.plugin.getServer().getScheduler().runTaskTimerAsynchronously(this.plugin, new Runnable()
/*  80:    */       {
/*  81:159 */         private boolean firstPost = true;
/*  82:    */         
/*  83:    */         public void run()
/*  84:    */         {
/*  85:    */           try
/*  86:    */           {
/*  87:164 */             synchronized (MetricsLite.this.optOutLock)
/*  88:    */             {
/*  89:166 */               if ((MetricsLite.this.isOptOut()) && (MetricsLite.this.task != null))
/*  90:    */               {
/*  91:167 */                 MetricsLite.this.task.cancel();
/*  92:168 */                 MetricsLite.this.task = null;
/*  93:    */               }
/*  94:    */             }
/*  95:175 */             MetricsLite.this.postPlugin(!this.firstPost);
/*  96:    */             
/*  97:    */ 
/*  98:    */ 
/*  99:179 */             this.firstPost = false;
/* 100:    */           }
/* 101:    */           catch (IOException e)
/* 102:    */           {
/* 103:181 */             if (MetricsLite.this.debug) {
/* 104:182 */               Bukkit.getLogger().log(Level.INFO, "[Metrics] " + e.getMessage());
/* 105:    */             }
/* 106:    */           }
/* 107:    */         }
/* 108:186 */       }, 0L, 18000L);
/* 109:    */       
/* 110:188 */       return true;
/* 111:    */     }
/* 112:    */   }
/* 113:    */   
/* 114:    */   public boolean isOptOut()
/* 115:    */   {
/* 116:198 */     synchronized (this.optOutLock)
/* 117:    */     {
/* 118:    */       try
/* 119:    */       {
/* 120:201 */         this.configuration.load(getConfigFile());
/* 121:    */       }
/* 122:    */       catch (IOException ex)
/* 123:    */       {
/* 124:203 */         if (this.debug) {
/* 125:204 */           Bukkit.getLogger().log(Level.INFO, "[Metrics] " + ex.getMessage());
/* 126:    */         }
/* 127:206 */         return true;
/* 128:    */       }
/* 129:    */       catch (InvalidConfigurationException ex)
/* 130:    */       {
/* 131:208 */         if (this.debug) {
/* 132:209 */           Bukkit.getLogger().log(Level.INFO, "[Metrics] " + ex.getMessage());
/* 133:    */         }
/* 134:211 */         return true;
/* 135:    */       }
/* 136:213 */       return this.configuration.getBoolean("opt-out", false);
/* 137:    */     }
/* 138:    */   }
/* 139:    */   
/* 140:    */   public void enable()
/* 141:    */     throws IOException
/* 142:    */   {
/* 143:224 */     synchronized (this.optOutLock)
/* 144:    */     {
/* 145:226 */       if (isOptOut())
/* 146:    */       {
/* 147:227 */         this.configuration.set("opt-out", Boolean.valueOf(false));
/* 148:228 */         this.configuration.save(this.configurationFile);
/* 149:    */       }
/* 150:232 */       if (this.task == null) {
/* 151:233 */         start();
/* 152:    */       }
/* 153:    */     }
/* 154:    */   }
/* 155:    */   
/* 156:    */   public void disable()
/* 157:    */     throws IOException
/* 158:    */   {
/* 159:245 */     synchronized (this.optOutLock)
/* 160:    */     {
/* 161:247 */       if (!isOptOut())
/* 162:    */       {
/* 163:248 */         this.configuration.set("opt-out", Boolean.valueOf(true));
/* 164:249 */         this.configuration.save(this.configurationFile);
/* 165:    */       }
/* 166:253 */       if (this.task != null)
/* 167:    */       {
/* 168:254 */         this.task.cancel();
/* 169:255 */         this.task = null;
/* 170:    */       }
/* 171:    */     }
/* 172:    */   }
/* 173:    */   
/* 174:    */   public File getConfigFile()
/* 175:    */   {
/* 176:271 */     File pluginsFolder = this.plugin.getDataFolder().getParentFile();
/* 177:    */     
/* 178:    */ 
/* 179:274 */     return new File(new File(pluginsFolder, "PluginMetrics"), "config.yml");
/* 180:    */   }
/* 181:    */   
/* 182:    */   private void postPlugin(boolean isPing)
/* 183:    */     throws IOException
/* 184:    */   {
/* 185:282 */     PluginDescriptionFile description = this.plugin.getDescription();
/* 186:283 */     String pluginName = description.getName();
/* 187:284 */     boolean onlineMode = Bukkit.getServer().getOnlineMode();
/* 188:285 */     String pluginVersion = description.getVersion();
/* 189:286 */     String serverVersion = Bukkit.getVersion();
/* 190:    */     
/* 191:    */ 
/* 192:    */ 
/* 193:290 */     int playersOnline = Bukkit.getServer().getOnlinePlayers().size();
/* 194:    */     
/* 195:    */ 
/* 196:    */ 
/* 197:    */ 
/* 198:295 */     StringBuilder json = new StringBuilder(1024);
/* 199:296 */     json.append('{');
/* 200:    */     
/* 201:    */ 
/* 202:299 */     appendJSONPair(json, "guid", this.guid);
/* 203:300 */     appendJSONPair(json, "plugin_version", pluginVersion);
/* 204:301 */     appendJSONPair(json, "server_version", serverVersion);
/* 205:302 */     appendJSONPair(json, "players_online", Integer.toString(playersOnline));
/* 206:    */     
/* 207:    */ 
/* 208:305 */     String osname = System.getProperty("os.name");
/* 209:306 */     String osarch = System.getProperty("os.arch");
/* 210:307 */     String osversion = System.getProperty("os.version");
/* 211:308 */     String java_version = System.getProperty("java.version");
/* 212:309 */     int coreCount = Runtime.getRuntime().availableProcessors();
/* 213:312 */     if (osarch.equals("amd64")) {
/* 214:313 */       osarch = "x86_64";
/* 215:    */     }
/* 216:316 */     appendJSONPair(json, "osname", osname);
/* 217:317 */     appendJSONPair(json, "osarch", osarch);
/* 218:318 */     appendJSONPair(json, "osversion", osversion);
/* 219:319 */     appendJSONPair(json, "cores", Integer.toString(coreCount));
/* 220:320 */     appendJSONPair(json, "auth_mode", onlineMode ? "1" : "0");
/* 221:321 */     appendJSONPair(json, "java_version", java_version);
/* 222:324 */     if (isPing) {
/* 223:325 */       appendJSONPair(json, "ping", "1");
/* 224:    */     }
/* 225:329 */     json.append('}');
/* 226:    */     
/* 227:    */ 
/* 228:332 */     URL url = new URL("http://report.mcstats.org" + String.format("/plugin/%s", new Object[] { urlEncode(pluginName) }));
/* 229:    */     URLConnection connection;
/* 230:    */     URLConnection connection;
/* 231:339 */     if (isMineshafterPresent()) {
/* 232:340 */       connection = url.openConnection(Proxy.NO_PROXY);
/* 233:    */     } else {
/* 234:342 */       connection = url.openConnection();
/* 235:    */     }
/* 236:346 */     byte[] uncompressed = json.toString().getBytes();
/* 237:347 */     byte[] compressed = gzip(json.toString());
/* 238:    */     
/* 239:    */ 
/* 240:350 */     connection.addRequestProperty("User-Agent", "MCStats/7");
/* 241:351 */     connection.addRequestProperty("Content-Type", "application/json");
/* 242:352 */     connection.addRequestProperty("Content-Encoding", "gzip");
/* 243:353 */     connection.addRequestProperty("Content-Length", Integer.toString(compressed.length));
/* 244:354 */     connection.addRequestProperty("Accept", "application/json");
/* 245:355 */     connection.addRequestProperty("Connection", "close");
/* 246:    */     
/* 247:357 */     connection.setDoOutput(true);
/* 248:359 */     if (this.debug) {
/* 249:360 */       System.out.println("[Metrics] Prepared request for " + pluginName + " uncompressed=" + uncompressed.length + " compressed=" + compressed.length);
/* 250:    */     }
/* 251:364 */     OutputStream os = connection.getOutputStream();
/* 252:365 */     os.write(compressed);
/* 253:366 */     os.flush();
/* 254:    */     
/* 255:    */ 
/* 256:369 */     BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
/* 257:370 */     String response = reader.readLine();
/* 258:    */     
/* 259:    */ 
/* 260:373 */     os.close();
/* 261:374 */     reader.close();
/* 262:376 */     if ((response == null) || (response.startsWith("ERR")) || (response.startsWith("7")))
/* 263:    */     {
/* 264:377 */       if (response == null) {
/* 265:378 */         response = "null";
/* 266:379 */       } else if (response.startsWith("7")) {
/* 267:380 */         response = response.substring(response.startsWith("7,") ? 2 : 1);
/* 268:    */       }
/* 269:383 */       throw new IOException(response);
/* 270:    */     }
/* 271:    */   }
/* 272:    */   
/* 273:    */   public static byte[] gzip(String input)
/* 274:    */   {
/* 275:394 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 276:395 */     GZIPOutputStream gzos = null;
/* 277:    */     try
/* 278:    */     {
/* 279:398 */       gzos = new GZIPOutputStream(baos);
/* 280:399 */       gzos.write(input.getBytes("UTF-8"));
/* 281:    */     }
/* 282:    */     catch (IOException e)
/* 283:    */     {
/* 284:401 */       e.printStackTrace();
/* 285:403 */       if (gzos != null) {
/* 286:    */         try
/* 287:    */         {
/* 288:404 */           gzos.close();
/* 289:    */         }
/* 290:    */         catch (IOException localIOException1) {}
/* 291:    */       }
/* 292:    */     }
/* 293:    */     finally
/* 294:    */     {
/* 295:403 */       if (gzos != null) {
/* 296:    */         try
/* 297:    */         {
/* 298:404 */           gzos.close();
/* 299:    */         }
/* 300:    */         catch (IOException localIOException2) {}
/* 301:    */       }
/* 302:    */     }
/* 303:409 */     return baos.toByteArray();
/* 304:    */   }
/* 305:    */   
/* 306:    */   private boolean isMineshafterPresent()
/* 307:    */   {
/* 308:    */     try
/* 309:    */     {
/* 310:419 */       Class.forName("mineshafter.MineServer");
/* 311:420 */       return true;
/* 312:    */     }
/* 313:    */     catch (Exception e) {}
/* 314:422 */     return false;
/* 315:    */   }
/* 316:    */   
/* 317:    */   private static void appendJSONPair(StringBuilder json, String key, String value)
/* 318:    */     throws UnsupportedEncodingException
/* 319:    */   {
/* 320:435 */     boolean isValueNumeric = false;
/* 321:    */     try
/* 322:    */     {
/* 323:438 */       if ((value.equals("0")) || (!value.endsWith("0")))
/* 324:    */       {
/* 325:439 */         Double.parseDouble(value);
/* 326:440 */         isValueNumeric = true;
/* 327:    */       }
/* 328:    */     }
/* 329:    */     catch (NumberFormatException e)
/* 330:    */     {
/* 331:443 */       isValueNumeric = false;
/* 332:    */     }
/* 333:446 */     if (json.charAt(json.length() - 1) != '{') {
/* 334:447 */       json.append(',');
/* 335:    */     }
/* 336:450 */     json.append(escapeJSON(key));
/* 337:451 */     json.append(':');
/* 338:453 */     if (isValueNumeric) {
/* 339:454 */       json.append(value);
/* 340:    */     } else {
/* 341:456 */       json.append(escapeJSON(value));
/* 342:    */     }
/* 343:    */   }
/* 344:    */   
/* 345:    */   private static String escapeJSON(String text)
/* 346:    */   {
/* 347:467 */     StringBuilder builder = new StringBuilder();
/* 348:    */     
/* 349:469 */     builder.append('"');
/* 350:470 */     for (int index = 0; index < text.length(); index++)
/* 351:    */     {
/* 352:471 */       char chr = text.charAt(index);
/* 353:473 */       switch (chr)
/* 354:    */       {
/* 355:    */       case '"': 
/* 356:    */       case '\\': 
/* 357:476 */         builder.append('\\');
/* 358:477 */         builder.append(chr);
/* 359:478 */         break;
/* 360:    */       case '\b': 
/* 361:480 */         builder.append("\\b");
/* 362:481 */         break;
/* 363:    */       case '\t': 
/* 364:483 */         builder.append("\\t");
/* 365:484 */         break;
/* 366:    */       case '\n': 
/* 367:486 */         builder.append("\\n");
/* 368:487 */         break;
/* 369:    */       case '\r': 
/* 370:489 */         builder.append("\\r");
/* 371:490 */         break;
/* 372:    */       default: 
/* 373:492 */         if (chr < ' ')
/* 374:    */         {
/* 375:493 */           String t = "000" + Integer.toHexString(chr);
/* 376:494 */           builder.append("\\u" + t.substring(t.length() - 4));
/* 377:    */         }
/* 378:    */         else
/* 379:    */         {
/* 380:496 */           builder.append(chr);
/* 381:    */         }
/* 382:    */         break;
/* 383:    */       }
/* 384:    */     }
/* 385:501 */     builder.append('"');
/* 386:    */     
/* 387:503 */     return builder.toString();
/* 388:    */   }
/* 389:    */   
/* 390:    */   private static String urlEncode(String text)
/* 391:    */     throws UnsupportedEncodingException
/* 392:    */   {
/* 393:513 */     return URLEncoder.encode(text, "UTF-8");
/* 394:    */   }
/* 395:    */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.metrics.MetricsLite
 * JD-Core Version:    0.7.0.1
 */