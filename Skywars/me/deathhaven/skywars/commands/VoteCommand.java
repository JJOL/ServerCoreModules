/*  1:   */ package me.deathhaven.skywars.commands;
/*  2:   */ 
/*  3:   */ import me.deathhaven.skywars.controllers.PlayerController;
/*  4:   */ import me.deathhaven.skywars.game.Game;
/*  5:   */ import me.deathhaven.skywars.game.GameState;
/*  6:   */ import me.deathhaven.skywars.player.GamePlayer;
/*  7:   */ import me.deathhaven.skywars.utilities.Messaging.MessageFormatter;
/*  8:   */ import org.bukkit.command.Command;
/*  9:   */ import org.bukkit.command.CommandExecutor;
/* 10:   */ import org.bukkit.command.CommandSender;
/* 11:   */ import org.bukkit.entity.Player;
/* 12:   */ 
/* 13:   */ @CommandDescription("Votes for a game to start faster.")
/* 14:   */ @CommandPermissions({"skywars.command.vote"})
/* 15:   */ public class VoteCommand
/* 16:   */   implements CommandExecutor
/* 17:   */ {
/* 18:   */   public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
/* 19:   */   {
/* 20:21 */     if ((sender instanceof Player))
/* 21:   */     {
/* 22:22 */       Player p = (Player)sender;
/* 23:23 */       GamePlayer gp = PlayerController.get().get(p);
/* 24:24 */       if (gp.isPlaying())
/* 25:   */       {
/* 26:25 */         if ((gp.getGame().getState() == GameState.WAITING) && (!gp.getGame().hasReachedMinimumPlayers()) && (!gp.getGame().canStart()))
/* 27:   */         {
/* 28:26 */           if (gp.getGame().addVote(p.getName())) {
/* 29:27 */             p.sendMessage(new Messaging.MessageFormatter().format(
/* 30:28 */               "game.voted"));
/* 31:   */           } else {
/* 32:30 */             p.sendMessage(new Messaging.MessageFormatter().format(
/* 33:31 */               "error.already-voted"));
/* 34:   */           }
/* 35:   */         }
/* 36:   */         else {
/* 37:34 */           p.sendMessage(new Messaging.MessageFormatter().format("error.no-valid-vote"));
/* 38:   */         }
/* 39:   */       }
/* 40:   */       else {
/* 41:37 */         p.sendMessage(new Messaging.MessageFormatter()
/* 42:38 */           .format("error.not-in-game"));
/* 43:   */       }
/* 44:   */     }
/* 45:   */     else
/* 46:   */     {
/* 47:41 */       sender.sendMessage("Only players can vote!");
/* 48:   */     }
/* 49:44 */     return true;
/* 50:   */   }
/* 51:   */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.commands.VoteCommand
 * JD-Core Version:    0.7.0.1
 */