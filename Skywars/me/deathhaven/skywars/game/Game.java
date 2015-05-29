/*   1:    */ package me.deathhaven.skywars.game;
/*   2:    */ 
/*   3:    */ import com.google.common.collect.Lists;
/*   4:    */ import com.google.common.collect.Maps;
/*   5:    */ import com.sk89q.commandbook.CommandBook;
/*   6:    */ import com.sk89q.worldedit.CuboidClipboard;
/*   7:    */ import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
/*   8:    */ import java.util.Collection;
/*   9:    */ import java.util.HashSet;
/*  10:    */ import java.util.List;
/*  11:    */ import java.util.Map;
/*  12:    */ import java.util.Map.Entry;
/*  13:    */ import me.deathhaven.skywars.SkyWars;
/*  14:    */ import me.deathhaven.skywars.config.PluginConfig;
/*  15:    */ import me.deathhaven.skywars.controllers.GameController;
/*  16:    */ import me.deathhaven.skywars.controllers.IconMenuController;
/*  17:    */ import me.deathhaven.skywars.controllers.KitController;
/*  18:    */ import me.deathhaven.skywars.controllers.PlayerController;
/*  19:    */ import me.deathhaven.skywars.controllers.SchematicController;
/*  20:    */ import me.deathhaven.skywars.controllers.WorldController;
/*  21:    */ import me.deathhaven.skywars.player.GamePlayer;
/*  22:    */ import me.deathhaven.skywars.utilities.CraftBukkitUtil;
/*  23:    */ import me.deathhaven.skywars.utilities.Messaging;
/*  24:    */ import me.deathhaven.skywars.utilities.Messaging.MessageFormatter;
/*  25:    */ import me.deathhaven.skywars.utilities.PlayerUtil;
/*  26:    */ import me.deathhaven.skywars.utilities.StringUtils;
/*  27:    */ import org.bukkit.Bukkit;
/*  28:    */ import org.bukkit.GameMode;
/*  29:    */ import org.bukkit.Location;
/*  30:    */ import org.bukkit.Material;
/*  31:    */ import org.bukkit.Server;
/*  32:    */ import org.bukkit.Sound;
/*  33:    */ import org.bukkit.World;
/*  34:    */ import org.bukkit.block.Block;
/*  35:    */ import org.bukkit.configuration.file.FileConfiguration;
/*  36:    */ import org.bukkit.entity.ExperienceOrb;
/*  37:    */ import org.bukkit.entity.Player;
/*  38:    */ import org.bukkit.event.entity.PlayerDeathEvent;
/*  39:    */ import org.bukkit.inventory.ItemStack;
/*  40:    */ import org.bukkit.plugin.Plugin;
/*  41:    */ import org.bukkit.plugin.PluginManager;
/*  42:    */ import org.bukkit.scoreboard.DisplaySlot;
/*  43:    */ import org.bukkit.scoreboard.Objective;
/*  44:    */ import org.bukkit.scoreboard.Score;
/*  45:    */ import org.bukkit.scoreboard.Scoreboard;
/*  46:    */ import org.bukkit.scoreboard.ScoreboardManager;
/*  47:    */ 
/*  48:    */ public class Game
/*  49:    */ {
/*  50:    */   private GameState gameState;
/*  51: 47 */   private Map<Integer, GamePlayer> idPlayerMap = Maps.newLinkedHashMap();
/*  52: 48 */   private Map<GamePlayer, Integer> playerIdMap = Maps.newHashMap();
/*  53: 49 */   private List<GamePlayer> spectators = Lists.newArrayList();
/*  54: 50 */   private int spectatorsCount = 0;
/*  55: 51 */   private int playerCount = 0;
/*  56: 52 */   private int playerVotes = 0;
/*  57: 53 */   private HashSet<String> voted = new HashSet();
/*  58:    */   private int slots;
/*  59: 55 */   private Map<Integer, Location> spawnPlaces = Maps.newHashMap();
/*  60:    */   private int timer;
/*  61:    */   private boolean canStart;
/*  62:    */   private Scoreboard scoreboard;
/*  63:    */   private Objective objective;
/*  64:    */   private boolean built;
/*  65:    */   private CuboidClipboard schematic;
/*  66:    */   private World world;
/*  67:    */   private int[] islandReference;
/*  68:    */   private org.bukkit.util.Vector minLoc;
/*  69:    */   private org.bukkit.util.Vector maxLoc;
/*  70: 67 */   private List<Location> chestList = Lists.newArrayList();
/*  71:    */   
/*  72:    */   public Game(CuboidClipboard schematic)
/*  73:    */   {
/*  74: 70 */     this.schematic = schematic;
/*  75: 71 */     this.world = WorldController.get().create(this, schematic);
/*  76: 72 */     this.slots = this.spawnPlaces.size();
/*  77: 73 */     this.gameState = GameState.WAITING;
/*  78: 74 */     this.canStart = false;
/*  79: 76 */     for (int iii = 0; iii < this.slots; iii++) {
/*  80: 77 */       this.idPlayerMap.put(Integer.valueOf(iii), null);
/*  81:    */     }
/*  82:    */   }
/*  83:    */   
/*  84:    */   public boolean isBuilt()
/*  85:    */   {
/*  86: 84 */     return this.built;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public void setBuilt(boolean built)
/*  90:    */   {
/*  91: 88 */     this.built = built;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public void setTimer(int timer)
/*  95:    */   {
/*  96: 92 */     this.timer = timer;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public void setCanStart(boolean b)
/* 100:    */   {
/* 101: 96 */     this.canStart = b;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public void addChest(Location location)
/* 105:    */   {
/* 106:100 */     this.chestList.add(location);
/* 107:    */   }
/* 108:    */   
/* 109:    */   public void removeChest(Location location)
/* 110:    */   {
/* 111:104 */     this.chestList.remove(location);
/* 112:    */   }
/* 113:    */   
/* 114:    */   public boolean isChest(Location location)
/* 115:    */   {
/* 116:108 */     return this.chestList.contains(location);
/* 117:    */   }
/* 118:    */   
/* 119:    */   public boolean isReady()
/* 120:    */   {
/* 121:112 */     return this.slots >= 2;
/* 122:    */   }
/* 123:    */   
/* 124:    */   public World getWorld()
/* 125:    */   {
/* 126:116 */     return this.world;
/* 127:    */   }
/* 128:    */   
/* 129:    */   public void setGridReference(int[] gridReference)
/* 130:    */   {
/* 131:120 */     this.islandReference = gridReference;
/* 132:    */   }
/* 133:    */   
/* 134:    */   public int[] getGridReference()
/* 135:    */   {
/* 136:124 */     return this.islandReference;
/* 137:    */   }
/* 138:    */   
/* 139:    */   public void setLocation(int originX, int originZ)
/* 140:    */   {
/* 141:128 */     int minX = originX + this.schematic.getOffset().getBlockX() + 1;
/* 142:129 */     int minZ = originZ + this.schematic.getOffset().getBlockZ() + 1;
/* 143:130 */     int maxX = minX + this.schematic.getWidth() - 2;
/* 144:131 */     int maxZ = minZ + this.schematic.getLength() - 2;
/* 145:132 */     int buffer = PluginConfig.getIslandBuffer();
/* 146:133 */     this.minLoc = new org.bukkit.util.Vector(minX - buffer, -2147483648, minZ - buffer);
/* 147:134 */     this.maxLoc = new org.bukkit.util.Vector(maxX + buffer, 2147483647, maxZ + buffer);
/* 148:    */   }
/* 149:    */   
/* 150:    */   public org.bukkit.util.Vector getMinLoc()
/* 151:    */   {
/* 152:138 */     return this.minLoc;
/* 153:    */   }
/* 154:    */   
/* 155:    */   public org.bukkit.util.Vector getMaxLoc()
/* 156:    */   {
/* 157:142 */     return this.maxLoc;
/* 158:    */   }
/* 159:    */   
/* 160:    */   public void onPlayerJoin(GamePlayer gamePlayer)
/* 161:    */   {
/* 162:146 */     Player player = gamePlayer.getBukkitPlayer();
/* 163:    */     
/* 164:148 */     int id = getFistEmpty();
/* 165:149 */     this.playerCount += 1;
/* 166:150 */     this.idPlayerMap.put(Integer.valueOf(getFistEmpty()), gamePlayer);
/* 167:151 */     this.playerIdMap.put(gamePlayer, Integer.valueOf(id));
/* 168:    */     
/* 169:153 */     sendMessage(new Messaging.MessageFormatter()
/* 170:154 */       .withPrefix()
/* 171:155 */       .setVariable("player", player.getDisplayName())
/* 172:156 */       .setVariable("amount", String.valueOf(getPlayerCount()))
/* 173:157 */       .setVariable("slots", String.valueOf(this.slots))
/* 174:158 */       .format("game.join"));
/* 175:160 */     if (getMinimumPlayers() - this.playerCount > 0) {
/* 176:161 */       sendMessage(new Messaging.MessageFormatter()
/* 177:162 */         .withPrefix()
/* 178:163 */         .setVariable("amount", String.valueOf(getMinimumPlayers() - this.playerCount))
/* 179:164 */         .format("game.required"));
/* 180:    */     }
/* 181:167 */     PlayerUtil.refreshPlayer(player);
/* 182:169 */     if (PluginConfig.saveInventory()) {
/* 183:170 */       gamePlayer.saveCurrentState();
/* 184:    */     }
/* 185:173 */     if (PluginConfig.clearInventory()) {
/* 186:174 */       PlayerUtil.clearInventory(player);
/* 187:    */     }
/* 188:177 */     if (player.getGameMode() != GameMode.SURVIVAL) {
/* 189:178 */       player.setGameMode(GameMode.SURVIVAL);
/* 190:    */     }
/* 191:181 */     gamePlayer.setChosenKit(false);
/* 192:182 */     gamePlayer.setSkipFallDamage(true);
/* 193:183 */     player.teleport(getSpawn(id).clone().add(0.5D, 0.5D, 0.5D));
/* 194:184 */     gamePlayer.setGame(this);
/* 195:    */     
/* 196:    */ 
/* 197:187 */     Plugin commandBook = SkyWars.get().getServer().getPluginManager().getPlugin("CommandBook");
/* 198:188 */     Plugin worldGuard = SkyWars.get().getServer().getPluginManager().getPlugin("WorldGuard");
/* 199:189 */     if (player.hasMetadata("god"))
/* 200:    */     {
/* 201:190 */       if ((commandBook != null) && ((commandBook instanceof CommandBook))) {
/* 202:191 */         player.removeMetadata("god", commandBook);
/* 203:    */       }
/* 204:193 */       if ((worldGuard != null) && ((worldGuard instanceof WorldGuardPlugin))) {
/* 205:194 */         player.removeMetadata("god", worldGuard);
/* 206:    */       }
/* 207:    */     }
/* 208:198 */     if (!PluginConfig.disableKits()) {
/* 209:199 */       KitController.get().openKitMenu(gamePlayer);
/* 210:    */     }
/* 211:202 */     if (!PluginConfig.buildSchematic()) {
/* 212:203 */       this.timer = getTimer();
/* 213:    */     }
/* 214:    */   }
/* 215:    */   
/* 216:    */   public void onSpectatorJoin(GamePlayer player)
/* 217:    */   {
/* 218:208 */     this.spectators.add(player);
/* 219:209 */     Player bPlayer = player.getBukkitPlayer();
/* 220:210 */     bPlayer.setFlying(true);
/* 221:211 */     player.setGame(this);
/* 222:212 */     player.setSpectating(true);
/* 223:    */   }
/* 224:    */   
/* 225:    */   public void onSpectatorLeave(GamePlayer player)
/* 226:    */   {
/* 227:216 */     this.spectators.remove(player);
/* 228:    */   }
/* 229:    */   
/* 230:    */   public void onPlayerLeave(GamePlayer gamePlayer)
/* 231:    */   {
/* 232:220 */     onPlayerLeave(gamePlayer, true, true, true);
/* 233:    */   }
/* 234:    */   
/* 235:    */   public void onPlayerLeave(GamePlayer gamePlayer, boolean displayText, boolean process, boolean left)
/* 236:    */   {
/* 237:224 */     Player player = gamePlayer.getBukkitPlayer();
/* 238:225 */     this.playerCount -= 1;
/* 239:226 */     IconMenuController.get().destroy(player);
/* 240:228 */     if (displayText) {
/* 241:229 */       if ((left) && (this.gameState == GameState.PLAYING))
/* 242:    */       {
/* 243:230 */         int scorePerLeave = PluginConfig.getScorePerLeave(player);
/* 244:231 */         gamePlayer.addScore(scorePerLeave);
/* 245:    */         
/* 246:233 */         sendMessage(new Messaging.MessageFormatter()
/* 247:234 */           .withPrefix()
/* 248:235 */           .setVariable("player", player.getDisplayName())
/* 249:236 */           .setVariable("score", StringUtils.formatScore(scorePerLeave, Messaging.getInstance().getMessage("score.naming")))
/* 250:237 */           .format("game.quit.playing"));
/* 251:    */       }
/* 252:    */       else
/* 253:    */       {
/* 254:240 */         sendMessage(new Messaging.MessageFormatter()
/* 255:241 */           .withPrefix()
/* 256:242 */           .setVariable("player", player.getDisplayName())
/* 257:243 */           .setVariable("total", String.valueOf(getPlayerCount()))
/* 258:244 */           .setVariable("slots", String.valueOf(this.slots))
/* 259:245 */           .format("game.quit.other"));
/* 260:    */       }
/* 261:    */     }
/* 262:249 */     if (this.scoreboard != null)
/* 263:    */     {
/* 264:250 */       this.objective.getScore(player).setScore(-this.playerCount);
/* 265:    */       try
/* 266:    */       {
/* 267:252 */         player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
/* 268:    */       }
/* 269:    */       catch (IllegalStateException localIllegalStateException) {}
/* 270:    */     }
/* 271:258 */     if (player.isDead())
/* 272:    */     {
/* 273:259 */       CraftBukkitUtil.forceRespawn(player);
/* 274:260 */       gamePlayer.setGame(null);
/* 275:    */     }
/* 276:    */     else
/* 277:    */     {
/* 278:262 */       PlayerUtil.refreshPlayer(player);
/* 279:263 */       PlayerUtil.clearInventory(player);
/* 280:    */       
/* 281:265 */       gamePlayer.setGame(null);
/* 282:266 */       player.teleport(PluginConfig.getLobbySpawn());
/* 283:268 */       if (PluginConfig.saveInventory()) {
/* 284:269 */         gamePlayer.restoreState();
/* 285:    */       }
/* 286:    */     }
/* 287:272 */     this.idPlayerMap.put((Integer)this.playerIdMap.remove(gamePlayer), null);
/* 288:273 */     gamePlayer.setChosenKit(false);
/* 289:275 */     if ((process) && (this.gameState == GameState.PLAYING) && (this.playerCount == 1)) {
/* 290:276 */       onGameEnd(getWinner());
/* 291:    */     }
/* 292:    */   }
/* 293:    */   
/* 294:    */   public void onPlayerDeath(GamePlayer gamePlayer, PlayerDeathEvent event)
/* 295:    */   {
/* 296:281 */     Player player = gamePlayer.getBukkitPlayer();
/* 297:282 */     Player killer = player.getKiller();
/* 298:    */     
/* 299:284 */     int scorePerDeath = PluginConfig.getScorePerDeath(player);
/* 300:285 */     gamePlayer.addScore(scorePerDeath);
/* 301:286 */     gamePlayer.setDeaths(gamePlayer.getDeaths() + 1);
/* 302:    */     
/* 303:288 */     GamePlayer gameKiller = null;
/* 304:289 */     if (killer != null) {
/* 305:290 */       gameKiller = PlayerController.get().get(killer);
/* 306:    */     }
/* 307:293 */     if (gameKiller != null)
/* 308:    */     {
/* 309:294 */       int scorePerKill = PluginConfig.getScorePerKill(killer);
/* 310:295 */       gameKiller.addScore(scorePerKill);
/* 311:296 */       gameKiller.setKills(gameKiller.getKills() + 1);
/* 312:    */       
/* 313:298 */       sendMessage(new Messaging.MessageFormatter()
/* 314:299 */         .withPrefix()
/* 315:300 */         .setVariable("player", player.getDisplayName())
/* 316:301 */         .setVariable("killer", killer.getDisplayName())
/* 317:302 */         .setVariable("player_score", StringUtils.formatScore(scorePerDeath, Messaging.getInstance().getMessage("score.naming")))
/* 318:303 */         .setVariable("killer_score", StringUtils.formatScore(scorePerKill, Messaging.getInstance().getMessage("score.naming")))
/* 319:304 */         .format("game.kill"));
/* 320:    */     }
/* 321:    */     else
/* 322:    */     {
/* 323:307 */       sendMessage(new Messaging.MessageFormatter()
/* 324:308 */         .withPrefix()
/* 325:309 */         .setVariable("player", player.getDisplayName())
/* 326:310 */         .setVariable("score", StringUtils.formatScore(scorePerDeath, Messaging.getInstance().getMessage("score.naming")))
/* 327:311 */         .format("game.death"));
/* 328:    */     }
/* 329:314 */     sendMessage(new Messaging.MessageFormatter()
/* 330:315 */       .withPrefix()
/* 331:316 */       .setVariable("remaining", String.valueOf(this.playerCount - 1))
/* 332:317 */       .format("game.remaining"));
/* 333:319 */     for (GamePlayer gp : getPlayers()) {
/* 334:320 */       if (gp.equals(gamePlayer)) {
/* 335:321 */         gp.getBukkitPlayer().sendMessage(new Messaging.MessageFormatter().withPrefix().format("game.eliminated.self"));
/* 336:    */       } else {
/* 337:324 */         gp.getBukkitPlayer().sendMessage(new Messaging.MessageFormatter()
/* 338:325 */           .withPrefix()
/* 339:326 */           .setVariable("player", player.getDisplayName())
/* 340:327 */           .format("game.eliminated.others"));
/* 341:    */       }
/* 342:    */     }
/* 343:331 */     if (event != null)
/* 344:    */     {
/* 345:332 */       Location location = player.getLocation().clone();
/* 346:333 */       World world = location.getWorld();
/* 347:335 */       for (ItemStack itemStack : event.getDrops()) {
/* 348:336 */         world.dropItemNaturally(location, itemStack);
/* 349:    */       }
/* 350:339 */       ((ExperienceOrb)world.spawn(location, ExperienceOrb.class)).setExperience(event.getDroppedExp());
/* 351:    */       
/* 352:341 */       event.setDeathMessage(null);
/* 353:342 */       event.getDrops().clear();
/* 354:343 */       event.setDroppedExp(0);
/* 355:    */       
/* 356:345 */       onPlayerLeave(gamePlayer, false, true, false);
/* 357:    */     }
/* 358:    */     else
/* 359:    */     {
/* 360:348 */       onPlayerLeave(gamePlayer, false, true, false);
/* 361:    */     }
/* 362:    */   }
/* 363:    */   
/* 364:    */   public void onGameStart()
/* 365:    */   {
/* 366:353 */     registerScoreboard();
/* 367:354 */     this.gameState = GameState.PLAYING;
/* 368:356 */     for (Map.Entry<Integer, GamePlayer> playerEntry : this.idPlayerMap.entrySet())
/* 369:    */     {
/* 370:357 */       GamePlayer gamePlayer = (GamePlayer)playerEntry.getValue();
/* 371:359 */       if (gamePlayer != null)
/* 372:    */       {
/* 373:360 */         this.objective.getScore(gamePlayer.getBukkitPlayer()).setScore(0);
/* 374:361 */         IconMenuController.get().destroy(gamePlayer.getBukkitPlayer());
/* 375:362 */         getSpawn(((Integer)playerEntry.getKey()).intValue()).clone().add(0.0D, -1.0D, 0.0D).getBlock().setType(Material.AIR);
/* 376:363 */         gamePlayer.setGamesPlayed(gamePlayer.getGamesPlayed() + 1);
/* 377:    */       }
/* 378:    */     }
/* 379:367 */     for (GamePlayer gamePlayer : getPlayers())
/* 380:    */     {
/* 381:368 */       gamePlayer.getBukkitPlayer().setHealth(20.0D);
/* 382:369 */       gamePlayer.getBukkitPlayer().setFoodLevel(20);
/* 383:370 */       gamePlayer.getBukkitPlayer().setSaturation(20.0F);
/* 384:    */       
/* 385:372 */       gamePlayer.getBukkitPlayer().setScoreboard(this.scoreboard);
/* 386:373 */       gamePlayer.getBukkitPlayer().sendMessage(new Messaging.MessageFormatter().withPrefix().format("game.start"));
/* 387:    */     }
/* 388:    */   }
/* 389:    */   
/* 390:    */   public void onGameEnd()
/* 391:    */   {
/* 392:378 */     onGameEnd(null);
/* 393:    */   }
/* 394:    */   
/* 395:    */   public void onGameEnd(GamePlayer gamePlayer)
/* 396:    */   {
/* 397:    */     int score;
/* 398:382 */     if (gamePlayer != null)
/* 399:    */     {
/* 400:383 */       Player player = gamePlayer.getBukkitPlayer();
/* 401:384 */       score = PluginConfig.getScorePerWin(player);
/* 402:385 */       gamePlayer.addScore(score);
/* 403:386 */       gamePlayer.setGamesWon(gamePlayer.getGamesWon() + 1);
/* 404:    */       
/* 405:388 */       Bukkit.broadcastMessage(new Messaging.MessageFormatter()
/* 406:389 */         .withPrefix()
/* 407:390 */         .setVariable("player", player.getDisplayName())
/* 408:391 */         .setVariable("score", String.valueOf(score))
/* 409:392 */         .setVariable("map", SchematicController.get().getName(this.schematic))
/* 410:393 */         .format("game.win"));
/* 411:    */     }
/* 412:396 */     for (GamePlayer player : getPlayers()) {
/* 413:397 */       onPlayerLeave(player, false, false, false);
/* 414:    */     }
/* 415:400 */     this.gameState = GameState.ENDING;
/* 416:401 */     unregisterScoreboard();
/* 417:    */     
/* 418:403 */     GameController.get().remove(this);
/* 419:    */   }
/* 420:    */   
/* 421:    */   public void onTick()
/* 422:    */   {
/* 423:413 */     if ((this.timer <= 0) || (
/* 424:414 */       (this.gameState == GameState.WAITING) && (!hasReachedMinimumPlayers()) && (!this.canStart))) {
/* 425:415 */       return;
/* 426:    */     }
/* 427:418 */     this.timer -= 1;
/* 428:420 */     switch (this.gameState)
/* 429:    */     {
/* 430:    */     case ENDING: 
/* 431:422 */       if (this.timer == 0)
/* 432:    */       {
/* 433:423 */         if (getPlayers().size() < 2) {
/* 434:424 */           return;
/* 435:    */         }
/* 436:425 */         onGameStart();
/* 437:    */       }
/* 438:426 */       else if ((this.timer % 10 == 0) || (this.timer <= 5))
/* 439:    */       {
/* 440:427 */         if ((PluginConfig.enableSounds()) && (this.timer <= 3)) {
/* 441:428 */           for (GamePlayer gamePlayer : this.idPlayerMap.values()) {
/* 442:429 */             if (gamePlayer != null)
/* 443:    */             {
/* 444:432 */               Player player = gamePlayer.getBukkitPlayer();
/* 445:433 */               if (player != null) {
/* 446:436 */                 player.getWorld().playSound(player.getLocation(), 
/* 447:437 */                   Sound.SUCCESSFUL_HIT, 10.0F, 1.0F);
/* 448:    */               }
/* 449:    */             }
/* 450:    */           }
/* 451:    */         }
/* 452:440 */         sendMessage(new Messaging.MessageFormatter()
/* 453:441 */           .withPrefix()
/* 454:442 */           .setVariable("timer", String.valueOf(this.timer))
/* 455:443 */           .format("game.countdown"));
/* 456:    */       }
/* 457:445 */       break;
/* 458:    */     }
/* 459:    */   }
/* 460:    */   
/* 461:    */   public GameState getState()
/* 462:    */   {
/* 463:453 */     return this.gameState;
/* 464:    */   }
/* 465:    */   
/* 466:    */   public int getPlayersCount()
/* 467:    */   {
/* 468:456 */     return this.playerCount;
/* 469:    */   }
/* 470:    */   
/* 471:    */   public Map<Integer, GamePlayer> getAllPlayers()
/* 472:    */   {
/* 473:459 */     return this.idPlayerMap;
/* 474:    */   }
/* 475:    */   
/* 476:    */   public int getSpectarorCount()
/* 477:    */   {
/* 478:462 */     return this.spectatorsCount;
/* 479:    */   }
/* 480:    */   
/* 481:    */   public List<GamePlayer> getAllSpectators()
/* 482:    */   {
/* 483:465 */     return this.spectators;
/* 484:    */   }
/* 485:    */   
/* 486:    */   public boolean canStart()
/* 487:    */   {
/* 488:469 */     return this.canStart;
/* 489:    */   }
/* 490:    */   
/* 491:    */   public boolean isFull()
/* 492:    */   {
/* 493:473 */     return getPlayerCount() == this.slots;
/* 494:    */   }
/* 495:    */   
/* 496:    */   public int getMinimumPlayers()
/* 497:    */   {
/* 498:477 */     FileConfiguration config = SkyWars.get().getConfig();
/* 499:478 */     String schematicName = SchematicController.get().getName(this.schematic);
/* 500:    */     
/* 501:480 */     return config.getInt("schematics." + schematicName + ".min-players", this.slots);
/* 502:    */   }
/* 503:    */   
/* 504:    */   private int getTimer()
/* 505:    */   {
/* 506:484 */     FileConfiguration config = SkyWars.get().getConfig();
/* 507:485 */     String schematicName = SchematicController.get().getName(this.schematic);
/* 508:    */     
/* 509:487 */     return config.getInt("schematics." + schematicName + ".timer", 11);
/* 510:    */   }
/* 511:    */   
/* 512:    */   public boolean addVote(String name)
/* 513:    */   {
/* 514:491 */     if (!this.voted.contains(name))
/* 515:    */     {
/* 516:492 */       this.playerVotes += 1;
/* 517:493 */       checkVotes();
/* 518:494 */       return true;
/* 519:    */     }
/* 520:496 */     return false;
/* 521:    */   }
/* 522:    */   
/* 523:    */   public void checkVotes()
/* 524:    */   {
/* 525:501 */     if ((this.playerVotes / this.playerCount * 100 >= 75) && 
/* 526:502 */       (getPlayerCount() > 1) && 
/* 527:503 */       (!PluginConfig.buildSchematic()))
/* 528:    */     {
/* 529:504 */       this.canStart = true;
/* 530:505 */       this.timer = getTimer();
/* 531:    */     }
/* 532:    */   }
/* 533:    */   
/* 534:    */   public boolean hasReachedMinimumPlayers()
/* 535:    */   {
/* 536:513 */     return getPlayerCount() >= getMinimumPlayers();
/* 537:    */   }
/* 538:    */   
/* 539:    */   public void sendMessage(String message)
/* 540:    */   {
/* 541:517 */     for (GamePlayer gamePlayer : getPlayers()) {
/* 542:518 */       gamePlayer.getBukkitPlayer().sendMessage(message);
/* 543:    */     }
/* 544:    */   }
/* 545:    */   
/* 546:    */   private GamePlayer getWinner()
/* 547:    */   {
/* 548:523 */     for (GamePlayer gamePlayer : this.idPlayerMap.values()) {
/* 549:524 */       if (gamePlayer != null) {
/* 550:528 */         return gamePlayer;
/* 551:    */       }
/* 552:    */     }
/* 553:531 */     return null;
/* 554:    */   }
/* 555:    */   
/* 556:    */   private int getFistEmpty()
/* 557:    */   {
/* 558:535 */     for (Map.Entry<Integer, GamePlayer> playerEntry : this.idPlayerMap.entrySet()) {
/* 559:536 */       if (playerEntry.getValue() == null) {
/* 560:537 */         return ((Integer)playerEntry.getKey()).intValue();
/* 561:    */       }
/* 562:    */     }
/* 563:541 */     return -1;
/* 564:    */   }
/* 565:    */   
/* 566:    */   public int getPlayerCount()
/* 567:    */   {
/* 568:545 */     return this.playerCount;
/* 569:    */   }
/* 570:    */   
/* 571:    */   public Collection<GamePlayer> getPlayers()
/* 572:    */   {
/* 573:549 */     List<GamePlayer> playerList = Lists.newArrayList();
/* 574:551 */     for (GamePlayer gamePlayer : this.idPlayerMap.values()) {
/* 575:552 */       if (gamePlayer != null) {
/* 576:553 */         playerList.add(gamePlayer);
/* 577:    */       }
/* 578:    */     }
/* 579:557 */     return playerList;
/* 580:    */   }
/* 581:    */   
/* 582:    */   private Location getSpawn(int id)
/* 583:    */   {
/* 584:561 */     return (Location)this.spawnPlaces.get(Integer.valueOf(id));
/* 585:    */   }
/* 586:    */   
/* 587:    */   public void addSpawn(int id, Location location)
/* 588:    */   {
/* 589:565 */     this.spawnPlaces.put(Integer.valueOf(id), location);
/* 590:    */   }
/* 591:    */   
/* 592:    */   private void registerScoreboard()
/* 593:    */   {
/* 594:569 */     if (this.scoreboard != null) {
/* 595:570 */       unregisterScoreboard();
/* 596:    */     }
/* 597:573 */     this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
/* 598:574 */     this.objective = this.scoreboard.registerNewObjective("info", "dummy");
/* 599:575 */     this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
/* 600:576 */     this.objective.setDisplayName("§c§lLeaderBoard");
/* 601:    */   }
/* 602:    */   
/* 603:    */   private void unregisterScoreboard()
/* 604:    */   {
/* 605:580 */     if (this.objective != null) {
/* 606:581 */       this.objective.unregister();
/* 607:    */     }
/* 608:584 */     if (this.scoreboard != null) {
/* 609:585 */       this.scoreboard = null;
/* 610:    */     }
/* 611:    */   }
/* 612:    */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.game.Game
 * JD-Core Version:    0.7.0.1
 */