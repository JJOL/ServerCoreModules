/*  1:   */ package me.deathhaven.skywars.commands;
/*  2:   */ 
/*  3:   */ import me.deathhaven.skywars.controllers.PlayerController;
/*  4:   */ import me.deathhaven.skywars.game.Game;
/*  5:   */ import me.deathhaven.skywars.player.GamePlayer;
/*  6:   */ import me.deathhaven.skywars.utilities.Messaging.MessageFormatter;
/*  7:   */ import org.bukkit.command.Command;
/*  8:   */ import org.bukkit.command.CommandExecutor;
/*  9:   */ import org.bukkit.command.CommandSender;
/* 10:   */ import org.bukkit.entity.Player;
/* 11:   */ 
/* 12:   */ @CommandDescription("Leaves a SkyWars game")
/* 13:   */ public class LeaveCommand
/* 14:   */   implements CommandExecutor
/* 15:   */ {
/* 16:   */   public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
/* 17:   */   {
/* 18:17 */     GamePlayer gamePlayer = PlayerController.get().get((Player)sender);
/* 19:19 */     if (!gamePlayer.isPlaying()) {
/* 20:20 */       sender.sendMessage(new Messaging.MessageFormatter().format("error.not-in-game"));
/* 21:   */     } else {
/* 22:22 */       gamePlayer.getGame().onPlayerLeave(gamePlayer);
/* 23:   */     }
/* 24:25 */     return true;
/* 25:   */   }
/* 26:   */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.commands.LeaveCommand
 * JD-Core Version:    0.7.0.1
 */