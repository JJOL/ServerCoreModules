/*   1:    */ package me.deathhaven.skywars.listeners;
/*   2:    */ 
/*   3:    */ import java.util.Iterator;
/*   4:    */ import java.util.Set;
/*   5:    */ import me.deathhaven.skywars.SkyWars;
/*   6:    */ import me.deathhaven.skywars.config.PluginConfig;
/*   7:    */ import me.deathhaven.skywars.controllers.GameController;
/*   8:    */ import me.deathhaven.skywars.controllers.PlayerController;
/*   9:    */ import me.deathhaven.skywars.controllers.SchematicController;
/*  10:    */ import me.deathhaven.skywars.game.Game;
/*  11:    */ import me.deathhaven.skywars.game.GameState;
/*  12:    */ import me.deathhaven.skywars.player.GamePlayer;
/*  13:    */ import me.deathhaven.skywars.utilities.Messaging;
/*  14:    */ import me.deathhaven.skywars.utilities.Messaging.MessageFormatter;
/*  15:    */ import me.deathhaven.skywars.utilities.StringUtils;
/*  16:    */ import net.milkbowl.vault.chat.Chat;
/*  17:    */ import org.bukkit.Bukkit;
/*  18:    */ import org.bukkit.GameMode;
/*  19:    */ import org.bukkit.Location;
/*  20:    */ import org.bukkit.Material;
/*  21:    */ import org.bukkit.block.Block;
/*  22:    */ import org.bukkit.entity.Player;
/*  23:    */ import org.bukkit.event.EventHandler;
/*  24:    */ import org.bukkit.event.Listener;
/*  25:    */ import org.bukkit.event.block.Action;
/*  26:    */ import org.bukkit.event.player.AsyncPlayerChatEvent;
/*  27:    */ import org.bukkit.event.player.PlayerCommandPreprocessEvent;
/*  28:    */ import org.bukkit.event.player.PlayerGameModeChangeEvent;
/*  29:    */ import org.bukkit.event.player.PlayerInteractEvent;
/*  30:    */ import org.bukkit.event.player.PlayerJoinEvent;
/*  31:    */ import org.bukkit.event.player.PlayerMoveEvent;
/*  32:    */ import org.bukkit.event.player.PlayerQuitEvent;
/*  33:    */ import org.bukkit.event.player.PlayerRespawnEvent;
/*  34:    */ import org.bukkit.event.player.PlayerTeleportEvent;
/*  35:    */ import org.bukkit.event.player.PlayerToggleFlightEvent;
/*  36:    */ import org.bukkit.scheduler.BukkitScheduler;
/*  37:    */ import org.bukkit.util.Vector;
/*  38:    */ 
/*  39:    */ public class PlayerListener
/*  40:    */   implements Listener
/*  41:    */ {
/*  42:    */   @EventHandler
/*  43:    */   public void onPlayerJoin(PlayerJoinEvent event)
/*  44:    */   {
/*  45: 32 */     Player p = event.getPlayer();
/*  46:    */     
/*  47: 34 */     PlayerController.get().register(p);
/*  48: 35 */     p.getName().startsWith("Kiyame");
/*  49:    */   }
/*  50:    */   
/*  51:    */   @EventHandler
/*  52:    */   public void onPlayerQuit(PlayerQuitEvent event)
/*  53:    */   {
/*  54: 42 */     Player player = event.getPlayer();
/*  55: 43 */     GamePlayer gamePlayer = PlayerController.get().get(player);
/*  56: 45 */     if (gamePlayer.isPlaying()) {
/*  57: 46 */       gamePlayer.getGame().onPlayerLeave(gamePlayer);
/*  58:    */     }
/*  59: 49 */     gamePlayer.save();
/*  60: 50 */     PlayerController.get().unregister(player);
/*  61:    */   }
/*  62:    */   
/*  63:    */   @EventHandler
/*  64:    */   public void onPlayerRespawn(PlayerRespawnEvent event)
/*  65:    */   {
/*  66: 55 */     Player player = event.getPlayer();
/*  67: 56 */     final GamePlayer gamePlayer = PlayerController.get().get(player);
/*  68: 58 */     if (gamePlayer.isPlaying())
/*  69:    */     {
/*  70: 59 */       event.setRespawnLocation(PluginConfig.getLobbySpawn());
/*  71: 61 */       if (PluginConfig.saveInventory()) {
/*  72: 62 */         Bukkit.getScheduler().runTaskLater(SkyWars.get(), new Runnable()
/*  73:    */         {
/*  74:    */           public void run()
/*  75:    */           {
/*  76: 65 */             gamePlayer.restoreState();
/*  77:    */           }
/*  78: 67 */         }, 1L);
/*  79:    */       }
/*  80:    */     }
/*  81:    */   }
/*  82:    */   
/*  83:    */   @EventHandler
/*  84:    */   public void onPlayerInteract(PlayerInteractEvent event)
/*  85:    */   {
/*  86: 74 */     Player player = event.getPlayer();
/*  87: 75 */     GamePlayer gamePlayer = PlayerController.get().get(player);
/*  88: 77 */     if ((event.getAction() == Action.PHYSICAL) && (event.getClickedBlock().getType() == Material.STONE_PLATE))
/*  89:    */     {
/*  90: 78 */       if (player.getWorld() != PluginConfig.getLobbySpawn().getWorld()) {
/*  91: 79 */         return;
/*  92:    */       }
/*  93: 81 */       if ((PluginConfig.getLobbyRadius() != 0) && 
/*  94: 82 */         (player.getLocation().distance(PluginConfig.getLobbySpawn()) > PluginConfig.getLobbyRadius())) {
/*  95: 83 */         return;
/*  96:    */       }
/*  97: 86 */       if (!gamePlayer.isPlaying())
/*  98:    */       {
/*  99: 87 */         if (SchematicController.get().size() == 0)
/* 100:    */         {
/* 101: 88 */           player.sendMessage(new Messaging.MessageFormatter().format("error.no-schematics"));
/* 102: 89 */           return;
/* 103:    */         }
/* 104: 92 */         Game game = GameController.get().findEmpty();
/* 105: 93 */         game.onPlayerJoin(gamePlayer);
/* 106:    */       }
/* 107: 96 */       return;
/* 108:    */     }
/* 109: 99 */     if (((gamePlayer.isPlaying()) && (gamePlayer.getGame().getState() != GameState.PLAYING)) || (gamePlayer.isSpectating())) {
/* 110:100 */       event.setCancelled(true);
/* 111:    */     }
/* 112:    */   }
/* 113:    */   
/* 114:    */   @EventHandler
/* 115:    */   public void onPlayerChat(AsyncPlayerChatEvent event)
/* 116:    */   {
/* 117:106 */     Player player = event.getPlayer();
/* 118:107 */     GamePlayer gamePlayer = PlayerController.get().get(player);
/* 119:109 */     if (PluginConfig.chatHandledByOtherPlugin())
/* 120:    */     {
/* 121:110 */       event.setFormat(event.getFormat().replace("[score]", String.valueOf(gamePlayer.getScore())));
/* 122:112 */       if (gamePlayer.isPlaying()) {
/* 123:114 */         for (Iterator<Player> iterator = event.getRecipients().iterator(); iterator.hasNext();)
/* 124:    */         {
/* 125:115 */           GamePlayer gp = PlayerController.get().get((Player)iterator.next());
/* 126:117 */           if ((!gp.isPlaying()) || (!gp.getGame().equals(gamePlayer.getGame()))) {
/* 127:118 */             iterator.remove();
/* 128:    */           }
/* 129:    */         }
/* 130:    */       } else {
/* 131:123 */         for (Iterator<Player> iterator = event.getRecipients().iterator(); iterator.hasNext();)
/* 132:    */         {
/* 133:124 */           GamePlayer gp = PlayerController.get().get((Player)iterator.next());
/* 134:126 */           if (gp.isPlaying()) {
/* 135:127 */             iterator.remove();
/* 136:    */           }
/* 137:    */         }
/* 138:    */       }
/* 139:132 */       return;
/* 140:    */     }
/* 141:135 */     String prefix = null;
/* 142:136 */     if (SkyWars.getChat() != null) {
/* 143:137 */       prefix = SkyWars.getChat().getPlayerPrefix(player);
/* 144:    */     }
/* 145:139 */     if (prefix == null) {
/* 146:140 */       prefix = "";
/* 147:    */     }
/* 148:142 */     String message = new Messaging.MessageFormatter()
/* 149:143 */       .setVariable("score", StringUtils.formatScore(gamePlayer.getScore()))
/* 150:144 */       .setVariable("player", player.getDisplayName())
/* 151:145 */       .setVariable("message", Messaging.stripColor(event.getMessage()))
/* 152:    */       
/* 153:147 */       .format("chat.local");
/* 154:148 */     event.setCancelled(true);
/* 155:150 */     if (gamePlayer.isPlaying()) {
/* 156:151 */       gamePlayer.getGame().sendMessage(message);
/* 157:    */     } else {
/* 158:154 */       for (GamePlayer gp : PlayerController.get().getAll()) {
/* 159:155 */         if (!gp.isPlaying()) {
/* 160:156 */           gp.getBukkitPlayer().sendMessage(message);
/* 161:    */         }
/* 162:    */       }
/* 163:    */     }
/* 164:    */   }
/* 165:    */   
/* 166:    */   @EventHandler
/* 167:    */   public void onPlayerCommand(PlayerCommandPreprocessEvent event)
/* 168:    */   {
/* 169:164 */     Player player = event.getPlayer();
/* 170:165 */     GamePlayer gamePlayer = PlayerController.get().get(player);
/* 171:167 */     if (gamePlayer.isPlaying())
/* 172:    */     {
/* 173:168 */       String command = event.getMessage().split(" ")[0].toLowerCase();
/* 174:170 */       if ((!command.equals("/sw")) && (!PluginConfig.isCommandWhitelisted(command)))
/* 175:    */       {
/* 176:171 */         event.setCancelled(true);
/* 177:172 */         player.sendMessage(new Messaging.MessageFormatter().withPrefix().format("error.cmd-disabled"));
/* 178:    */       }
/* 179:    */     }
/* 180:    */   }
/* 181:    */   
/* 182:    */   @EventHandler
/* 183:    */   public void onPlayerMove(PlayerMoveEvent e)
/* 184:    */   {
/* 185:179 */     Location from = e.getFrom();
/* 186:180 */     Location to = e.getTo();
/* 187:181 */     if ((from.getBlockX() == to.getBlockX()) && (from.getBlockY() == to.getBlockY()) && (from.getBlockZ() == to.getBlockZ())) {
/* 188:182 */       return;
/* 189:    */     }
/* 190:184 */     Player p = e.getPlayer();
/* 191:185 */     GamePlayer gamePlayer = PlayerController.get().get(p);
/* 192:186 */     if (!gamePlayer.isPlaying()) {
/* 193:187 */       return;
/* 194:    */     }
/* 195:189 */     Vector minVec = gamePlayer.getGame().getMinLoc();
/* 196:190 */     Vector maxVec = gamePlayer.getGame().getMaxLoc();
/* 197:191 */     if (p.getLocation().getBlockY() < 0)
/* 198:    */     {
/* 199:192 */       p.setFallDistance(0.0F);
/* 200:193 */       gamePlayer.getGame().onPlayerDeath(gamePlayer, null);
/* 201:    */     }
/* 202:194 */     else if (!to.toVector().isInAABB(minVec, maxVec))
/* 203:    */     {
/* 204:195 */       p.sendMessage(new Messaging.MessageFormatter().withPrefix()
/* 205:196 */         .format("You cannot leave the arena."));
/* 206:197 */       p.teleport(from);
/* 207:    */     }
/* 208:    */   }
/* 209:    */   
/* 210:    */   @EventHandler
/* 211:    */   public void onPlayerTeleport(PlayerTeleportEvent e)
/* 212:    */   {
/* 213:203 */     Player p = e.getPlayer();
/* 214:204 */     GamePlayer gP = PlayerController.get().get(p);
/* 215:205 */     if (gP == null) {
/* 216:206 */       return;
/* 217:    */     }
/* 218:208 */     if (!gP.isPlaying()) {
/* 219:209 */       return;
/* 220:    */     }
/* 221:211 */     Vector minVec = gP.getGame().getMinLoc();
/* 222:212 */     Vector maxVec = gP.getGame().getMaxLoc();
/* 223:213 */     if (e.getTo().getWorld() != gP.getGame().getWorld())
/* 224:    */     {
/* 225:214 */       p.sendMessage(new Messaging.MessageFormatter().withPrefix().format("You left the arena."));
/* 226:215 */       gP.getGame().onPlayerLeave(gP);
/* 227:    */     }
/* 228:216 */     else if (!e.getTo().toVector().isInAABB(minVec, maxVec))
/* 229:    */     {
/* 230:217 */       p.sendMessage(new Messaging.MessageFormatter().withPrefix().format("You left the arena."));
/* 231:218 */       gP.getGame().onPlayerLeave(gP);
/* 232:    */     }
/* 233:    */   }
/* 234:    */   
/* 235:    */   @EventHandler
/* 236:    */   public void onPlayerFlight(PlayerToggleFlightEvent e)
/* 237:    */   {
/* 238:224 */     GamePlayer gP = PlayerController.get().get(e.getPlayer());
/* 239:225 */     if (gP == null) {
/* 240:226 */       return;
/* 241:    */     }
/* 242:228 */     if (!gP.isPlaying()) {
/* 243:229 */       return;
/* 244:    */     }
/* 245:231 */     if (e.isFlying())
/* 246:    */     {
/* 247:232 */       e.setCancelled(true);
/* 248:233 */       e.getPlayer().setAllowFlight(false);
/* 249:234 */       e.getPlayer().setFlying(false);
/* 250:235 */       e.getPlayer().sendMessage(new Messaging.MessageFormatter().withPrefix()
/* 251:236 */         .format("You're not allowed to fly while in-game."));
/* 252:    */     }
/* 253:    */   }
/* 254:    */   
/* 255:    */   @EventHandler
/* 256:    */   public void onGameModeChange(PlayerGameModeChangeEvent e)
/* 257:    */   {
/* 258:242 */     GamePlayer gP = PlayerController.get().get(e.getPlayer());
/* 259:243 */     if (gP == null) {
/* 260:244 */       return;
/* 261:    */     }
/* 262:246 */     if (!gP.isPlaying()) {
/* 263:247 */       return;
/* 264:    */     }
/* 265:249 */     if (!e.getNewGameMode().equals(GameMode.SURVIVAL))
/* 266:    */     {
/* 267:250 */       e.setCancelled(true);
/* 268:251 */       e.getPlayer().setGameMode(GameMode.SURVIVAL);
/* 269:252 */       e.getPlayer().sendMessage(new Messaging.MessageFormatter().withPrefix()
/* 270:253 */         .format("You're not allowed to change gamemode while in-game."));
/* 271:    */     }
/* 272:    */   }
/* 273:    */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.listeners.PlayerListener
 * JD-Core Version:    0.7.0.1
 */