/*  1:   */ package me.deathhaven.skywars.commands;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.List;
/*  5:   */ import me.deathhaven.skywars.controllers.GameController;
/*  6:   */ import me.deathhaven.skywars.game.Game;
/*  7:   */ import me.deathhaven.skywars.utilities.Messaging.MessageFormatter;
/*  8:   */ import org.bukkit.command.Command;
/*  9:   */ import org.bukkit.command.CommandExecutor;
/* 10:   */ import org.bukkit.command.CommandSender;
/* 11:   */ 
/* 12:   */ @CommandPermissions({"skywars.command.vote"})
/* 13:   */ @CommandDescription("Lets you Join a Game as a Spectator")
/* 14:   */ public class SpectateCommand
/* 15:   */   implements CommandExecutor
/* 16:   */ {
/* 17:   */   public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
/* 18:   */   {
/* 19:25 */     sender.sendMessage(new Messaging.MessageFormatter().withPrefix()
/* 20:26 */       .format("&lComing Soon!"));
/* 21:30 */     if (isId(args[1]))
/* 22:   */     {
/* 23:31 */       int gId = Integer.parseInt(args[1]);
/* 24:   */       
/* 25:33 */       List<Game> games = (ArrayList)GameController.get().getAll();
/* 26:34 */       if ((gId >= 0) && (gId < games.size())) {
/* 27:35 */         Game localGame = (Game)games.get(gId);
/* 28:   */       }
/* 29:   */     }
/* 30:42 */     return true;
/* 31:   */   }
/* 32:   */   
/* 33:   */   private boolean isId(String s)
/* 34:   */   {
/* 35:   */     try
/* 36:   */     {
/* 37:47 */       int i = Integer.parseInt(s);
/* 38:48 */       return true;
/* 39:   */     }
/* 40:   */     catch (Exception ex) {}
/* 41:50 */     return false;
/* 42:   */   }
/* 43:   */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.commands.SpectateCommand
 * JD-Core Version:    0.7.0.1
 */