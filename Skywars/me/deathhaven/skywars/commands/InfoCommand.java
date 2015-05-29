/*   1:    */ package me.deathhaven.skywars.commands;
/*   2:    */ 
/*   3:    */ import java.util.Collection;
/*   4:    */ import java.util.List;
/*   5:    */ import me.deathhaven.skywars.controllers.GameController;
/*   6:    */ import me.deathhaven.skywars.game.Game;
/*   7:    */ import me.deathhaven.skywars.player.GamePlayer;
/*   8:    */ import me.deathhaven.skywars.utilities.Messaging.MessageFormatter;
/*   9:    */ import org.bukkit.command.Command;
/*  10:    */ import org.bukkit.command.CommandExecutor;
/*  11:    */ import org.bukkit.command.CommandSender;
/*  12:    */ import org.bukkit.entity.Player;
/*  13:    */ 
/*  14:    */ @CommandDescription("You can use this command to get nodes of information")
/*  15:    */ @CommandPermissions({"skywars.command.getinfo"})
/*  16:    */ public class InfoCommand
/*  17:    */   implements CommandExecutor
/*  18:    */ {
/*  19:    */   public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
/*  20:    */   {
/*  21: 24 */     if ((sender instanceof Player)) {
/*  22: 25 */       sender.sendMessage(new Messaging.MessageFormatter().withPrefix()
/*  23: 26 */         .format("&lComing Soon!"));
/*  24:    */     }
/*  25: 88 */     return true;
/*  26:    */   }
/*  27:    */   
/*  28:    */   private List<Game> getGames()
/*  29:    */   {
/*  30: 92 */     return (List)GameController.get().getAll();
/*  31:    */   }
/*  32:    */   
/*  33:    */   private String[] getPlayers(Game g)
/*  34:    */   {
/*  35: 96 */     String[] players = new String[g.getPlayers().size()];
/*  36: 98 */     for (int i = 0; i < g.getPlayers().size(); i++) {
/*  37: 99 */       players[i] = ((GamePlayer)((List)g.getPlayers()).get(i)).getName();
/*  38:    */     }
/*  39:102 */     return players;
/*  40:    */   }
/*  41:    */   
/*  42:    */   private final boolean checkValidInt(String s)
/*  43:    */   {
/*  44:    */     try
/*  45:    */     {
/*  46:107 */       Integer.parseInt(s);
/*  47:    */     }
/*  48:    */     catch (Exception e)
/*  49:    */     {
/*  50:109 */       return false;
/*  51:    */     }
/*  52:111 */     return true;
/*  53:    */   }
/*  54:    */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.commands.InfoCommand
 * JD-Core Version:    0.7.0.1
 */