/*  1:   */ package me.deathhaven.skywars.commands;
/*  2:   */ 
/*  3:   */ import me.deathhaven.skywars.config.PluginConfig;
/*  4:   */ import me.deathhaven.skywars.controllers.PlayerController;
/*  5:   */ import me.deathhaven.skywars.game.Game;
/*  6:   */ import me.deathhaven.skywars.game.GameState;
/*  7:   */ import me.deathhaven.skywars.player.GamePlayer;
/*  8:   */ import me.deathhaven.skywars.utilities.Messaging.MessageFormatter;
/*  9:   */ import org.bukkit.command.Command;
/* 10:   */ import org.bukkit.command.CommandExecutor;
/* 11:   */ import org.bukkit.command.CommandSender;
/* 12:   */ import org.bukkit.entity.Player;
/* 13:   */ 
/* 14:   */ @CommandDescription("Starts a SkyWars game")
/* 15:   */ @CommandPermissions({"skywars.command.start"})
/* 16:   */ public class StartCommand
/* 17:   */   implements CommandExecutor
/* 18:   */ {
/* 19:   */   public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
/* 20:   */   {
/* 21:20 */     GamePlayer gamePlayer = PlayerController.get().get((Player)sender);
/* 22:22 */     if (!gamePlayer.isPlaying()) {
/* 23:23 */       sender.sendMessage(new Messaging.MessageFormatter().format("error.not-in-game"));
/* 24:24 */     } else if (gamePlayer.getGame().getState() != GameState.WAITING) {
/* 25:25 */       sender.sendMessage(new Messaging.MessageFormatter().format("error.already-started"));
/* 26:26 */     } else if (gamePlayer.getGame().getPlayerCount() < 2) {
/* 27:27 */       sender.sendMessage(new Messaging.MessageFormatter().format("error.not-enough-players"));
/* 28:28 */     } else if ((PluginConfig.buildSchematic()) && (!gamePlayer.getGame().isBuilt())) {
/* 29:29 */       sender.sendMessage(new Messaging.MessageFormatter().format("error.arena-under-construction"));
/* 30:   */     } else {
/* 31:31 */       gamePlayer.getGame().onGameStart();
/* 32:   */     }
/* 33:34 */     return true;
/* 34:   */   }
/* 35:   */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.commands.StartCommand
 * JD-Core Version:    0.7.0.1
 */