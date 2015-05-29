/*  1:   */ package me.deathhaven.skywars.commands;
/*  2:   */ 
/*  3:   */ import me.deathhaven.skywars.config.PluginConfig;
/*  4:   */ import me.deathhaven.skywars.controllers.GameController;
/*  5:   */ import me.deathhaven.skywars.controllers.PlayerController;
/*  6:   */ import me.deathhaven.skywars.controllers.SchematicController;
/*  7:   */ import me.deathhaven.skywars.game.Game;
/*  8:   */ import me.deathhaven.skywars.player.GamePlayer;
/*  9:   */ import me.deathhaven.skywars.utilities.Messaging.MessageFormatter;
/* 10:   */ import org.bukkit.Location;
/* 11:   */ import org.bukkit.command.Command;
/* 12:   */ import org.bukkit.command.CommandExecutor;
/* 13:   */ import org.bukkit.command.CommandSender;
/* 14:   */ import org.bukkit.entity.Player;
/* 15:   */ 
/* 16:   */ @CommandDescription("Joins a Game.")
/* 17:   */ public class JoinCommand
/* 18:   */   implements CommandExecutor
/* 19:   */ {
/* 20:   */   public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
/* 21:   */   {
/* 22:21 */     Player player = (Player)sender;
/* 23:22 */     GamePlayer gamePlayer = PlayerController.get().get(player);
/* 24:24 */     if ((!gamePlayer.isPlaying()) && (player.getLocation().getWorld().equals(PluginConfig.getLobbySpawn().getWorld())))
/* 25:   */     {
/* 26:25 */       if (SchematicController.get().size() == 0)
/* 27:   */       {
/* 28:26 */         player.sendMessage(new Messaging.MessageFormatter().format("error.no-schematics"));
/* 29:27 */         return true;
/* 30:   */       }
/* 31:29 */       Game game = GameController.get().findEmpty();
/* 32:30 */       game.onPlayerJoin(gamePlayer);
/* 33:   */     }
/* 34:   */     else
/* 35:   */     {
/* 36:33 */       player.sendMessage(new Messaging.MessageFormatter().format("error.in-game"));
/* 37:   */     }
/* 38:35 */     return true;
/* 39:   */   }
/* 40:   */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.commands.JoinCommand
 * JD-Core Version:    0.7.0.1
 */