/*  1:   */ package me.deathhaven.skywars.commands;
/*  2:   */ 
/*  3:   */ import me.deathhaven.skywars.controllers.IconMenuController;
/*  4:   */ import me.deathhaven.skywars.controllers.KitController;
/*  5:   */ import me.deathhaven.skywars.controllers.PlayerController;
/*  6:   */ import me.deathhaven.skywars.game.Game;
/*  7:   */ import me.deathhaven.skywars.game.GameState;
/*  8:   */ import me.deathhaven.skywars.player.GamePlayer;
/*  9:   */ import me.deathhaven.skywars.utilities.Messaging.MessageFormatter;
/* 10:   */ import org.bukkit.command.Command;
/* 11:   */ import org.bukkit.command.CommandExecutor;
/* 12:   */ import org.bukkit.command.CommandSender;
/* 13:   */ import org.bukkit.entity.Player;
/* 14:   */ 
/* 15:   */ @CommandDescription("Allows a player to pick kits")
/* 16:   */ public class KitCommand
/* 17:   */   implements CommandExecutor
/* 18:   */ {
/* 19:   */   public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
/* 20:   */   {
/* 21:20 */     Player player = (Player)sender;
/* 22:21 */     GamePlayer gamePlayer = PlayerController.get().get(player);
/* 23:23 */     if (!gamePlayer.isPlaying()) {
/* 24:24 */       sender.sendMessage(new Messaging.MessageFormatter().format("error.not-in-game"));
/* 25:25 */     } else if (gamePlayer.hasChosenKit()) {
/* 26:26 */       sender.sendMessage(new Messaging.MessageFormatter().format("error.already-has-kit"));
/* 27:27 */     } else if (gamePlayer.getGame().getState() != GameState.WAITING) {
/* 28:28 */       sender.sendMessage(new Messaging.MessageFormatter().format("error.can-not-pick-kit"));
/* 29:29 */     } else if (!IconMenuController.get().has(player)) {
/* 30:30 */       KitController.get().openKitMenu(gamePlayer);
/* 31:   */     }
/* 32:33 */     return true;
/* 33:   */   }
/* 34:   */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.commands.KitCommand
 * JD-Core Version:    0.7.0.1
 */