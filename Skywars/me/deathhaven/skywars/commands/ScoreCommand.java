/*   1:    */ package me.deathhaven.skywars.commands;
/*   2:    */ 
/*   3:    */ import me.deathhaven.skywars.controllers.PlayerController;
/*   4:    */ import me.deathhaven.skywars.player.GamePlayer;
/*   5:    */ import me.deathhaven.skywars.utilities.Messaging.MessageFormatter;
/*   6:    */ import org.bukkit.Bukkit;
/*   7:    */ import org.bukkit.OfflinePlayer;
/*   8:    */ import org.bukkit.Server;
/*   9:    */ import org.bukkit.command.Command;
/*  10:    */ import org.bukkit.command.CommandExecutor;
/*  11:    */ import org.bukkit.command.CommandSender;
/*  12:    */ import org.bukkit.entity.Player;
/*  13:    */ 
/*  14:    */ @CommandDescription("Displays the score of the given user")
/*  15:    */ @CommandPermissions({"skywars.command.score"})
/*  16:    */ public class ScoreCommand
/*  17:    */   implements CommandExecutor
/*  18:    */ {
/*  19:    */   public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
/*  20:    */   {
/*  21: 36 */     if (args.length == 1)
/*  22:    */     {
/*  23: 37 */       if ((sender instanceof Player))
/*  24:    */       {
/*  25: 38 */         Player player = (Player)sender;
/*  26: 39 */         GamePlayer gamePlayer = PlayerController.get().get(player);
/*  27: 40 */         sender.sendMessage(new Messaging.MessageFormatter()
/*  28: 41 */           .setVariable("player", gamePlayer.getName())
/*  29: 42 */           .setVariable("value", String.valueOf(gamePlayer.getScore()))
/*  30: 43 */           .format("cmd.score"));
/*  31:    */       }
/*  32:    */     }
/*  33: 45 */     else if (args.length == 2)
/*  34:    */     {
/*  35: 46 */       if (!sender.hasPermission("skywars.command.score.others"))
/*  36:    */       {
/*  37: 47 */         sender.sendMessage(new Messaging.MessageFormatter().format("error.insufficient-permissions"));
/*  38: 48 */         return true;
/*  39:    */       }
/*  40: 50 */       OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(args[1]);
/*  41: 51 */       if ((!player.isOnline()) && (!player.hasPlayedBefore()))
/*  42:    */       {
/*  43: 52 */         sender.sendMessage(new Messaging.MessageFormatter()
/*  44: 53 */           .format("error.no-valid-player"));
/*  45: 54 */         return true;
/*  46:    */       }
/*  47: 56 */       GamePlayer gamePlayer = new GamePlayer(args[1]);
/*  48: 57 */       sender.sendMessage(new Messaging.MessageFormatter()
/*  49: 58 */         .setVariable("value", String.valueOf(gamePlayer.getScore()))
/*  50: 59 */         .setVariable("player", gamePlayer.getName())
/*  51: 60 */         .format("cmd.score"));
/*  52:    */     }
/*  53:    */     else
/*  54:    */     {
/*  55: 62 */       if ("set".equals(args[1]))
/*  56:    */       {
/*  57: 63 */         if (!sender.hasPermission("skywars.command.score.set"))
/*  58:    */         {
/*  59: 64 */           sender.sendMessage(new Messaging.MessageFormatter()
/*  60: 65 */             .format("error.insufficient-permissions"));
/*  61: 66 */           return true;
/*  62:    */         }
/*  63: 68 */         if (args.length < 4)
/*  64:    */         {
/*  65: 69 */           sender.sendMessage(new Messaging.MessageFormatter()
/*  66: 70 */             .setVariable("example", "/sw set player score")
/*  67: 71 */             .format("error.not-enough-arguments"));
/*  68: 72 */           return true;
/*  69:    */         }
/*  70: 74 */         OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(args[2]);
/*  71: 75 */         if ((!player.isOnline()) && (!player.hasPlayedBefore()))
/*  72:    */         {
/*  73: 76 */           sender.sendMessage(new Messaging.MessageFormatter()
/*  74: 77 */             .format("error.no-valid-player"));
/*  75: 78 */           return true;
/*  76:    */         }
/*  77: 80 */         GamePlayer gamePlayer = new GamePlayer(player.getName());
/*  78:    */         try
/*  79:    */         {
/*  80: 82 */           int amount = Integer.parseInt(args[3]);
/*  81: 83 */           gamePlayer.setScore(amount);
/*  82: 84 */           sender.sendMessage(new Messaging.MessageFormatter()
/*  83: 85 */             .setVariable("value", String.valueOf(amount))
/*  84: 86 */             .setVariable("player", gamePlayer.getName())
/*  85: 87 */             .format("success.score-set"));
/*  86: 88 */           gamePlayer.save();
/*  87: 89 */           return true;
/*  88:    */         }
/*  89:    */         catch (NumberFormatException e)
/*  90:    */         {
/*  91: 91 */           sender.sendMessage(new Messaging.MessageFormatter()
/*  92: 92 */             .format("error.no-valid-score"));
/*  93: 93 */           return true;
/*  94:    */         }
/*  95:    */       }
/*  96: 95 */       if ("give".equals(args[1]))
/*  97:    */       {
/*  98: 96 */         if (!sender.hasPermission("skywars.command.score.give"))
/*  99:    */         {
/* 100: 97 */           sender.sendMessage(new Messaging.MessageFormatter()
/* 101: 98 */             .format("error.insufficient-permissions"));
/* 102: 99 */           return true;
/* 103:    */         }
/* 104:101 */         if (args.length < 4)
/* 105:    */         {
/* 106:102 */           sender.sendMessage(new Messaging.MessageFormatter()
/* 107:103 */             .setVariable("example", "/sw give player score")
/* 108:104 */             .format("error.not-enough-arguments"));
/* 109:105 */           return true;
/* 110:    */         }
/* 111:107 */         OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(args[2]);
/* 112:108 */         if ((!player.isOnline()) && (!player.hasPlayedBefore()))
/* 113:    */         {
/* 114:109 */           sender.sendMessage(new Messaging.MessageFormatter()
/* 115:110 */             .format("error.no-valid-player"));
/* 116:111 */           return true;
/* 117:    */         }
/* 118:113 */         GamePlayer gamePlayer = new GamePlayer(player.getName());
/* 119:    */         try
/* 120:    */         {
/* 121:115 */           int amount = Integer.parseInt(args[3]);
/* 122:116 */           gamePlayer.setScore(gamePlayer.getScore() + amount);
/* 123:117 */           sender.sendMessage(new Messaging.MessageFormatter()
/* 124:118 */             .setVariable("value", String.valueOf(amount))
/* 125:119 */             .setVariable("player", gamePlayer.getName())
/* 126:120 */             .format("success.score-give"));
/* 127:121 */           gamePlayer.save();
/* 128:122 */           return true;
/* 129:    */         }
/* 130:    */         catch (NumberFormatException e)
/* 131:    */         {
/* 132:124 */           sender.sendMessage(new Messaging.MessageFormatter()
/* 133:125 */             .format("error.no-valid-score"));
/* 134:126 */           return true;
/* 135:    */         }
/* 136:    */       }
/* 137:128 */       if ("take".equals(args[1]))
/* 138:    */       {
/* 139:129 */         if (!sender.hasPermission("skywars.command.score.take"))
/* 140:    */         {
/* 141:130 */           sender.sendMessage(new Messaging.MessageFormatter()
/* 142:131 */             .format("error.insufficient-permissions"));
/* 143:132 */           return true;
/* 144:    */         }
/* 145:134 */         if (args.length < 4)
/* 146:    */         {
/* 147:135 */           sender.sendMessage(new Messaging.MessageFormatter()
/* 148:136 */             .setVariable("example", "/sw take player score")
/* 149:137 */             .format("error.not-enough-arguments"));
/* 150:138 */           return true;
/* 151:    */         }
/* 152:140 */         OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(args[2]);
/* 153:141 */         if ((!player.isOnline()) && (!player.hasPlayedBefore()))
/* 154:    */         {
/* 155:142 */           sender.sendMessage(new Messaging.MessageFormatter()
/* 156:143 */             .format("error.no-valid-player"));
/* 157:144 */           return true;
/* 158:    */         }
/* 159:146 */         GamePlayer gamePlayer = new GamePlayer(player.getName());
/* 160:    */         try
/* 161:    */         {
/* 162:148 */           int amount = Integer.parseInt(args[3]);
/* 163:149 */           gamePlayer.setScore(gamePlayer.getScore() - amount);
/* 164:150 */           sender.sendMessage(new Messaging.MessageFormatter()
/* 165:151 */             .setVariable("value", String.valueOf(amount))
/* 166:152 */             .setVariable("player", gamePlayer.getName())
/* 167:153 */             .format("success.score-take"));
/* 168:154 */           gamePlayer.save();
/* 169:155 */           return true;
/* 170:    */         }
/* 171:    */         catch (NumberFormatException e)
/* 172:    */         {
/* 173:157 */           sender.sendMessage(new Messaging.MessageFormatter()
/* 174:158 */             .format("error.no-valid-score"));
/* 175:159 */           return true;
/* 176:    */         }
/* 177:    */       }
/* 178:162 */       sender.sendMessage(new Messaging.MessageFormatter()
/* 179:163 */         .format("error.invalid-cmd"));
/* 180:    */     }
/* 181:166 */     return false;
/* 182:    */   }
/* 183:    */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.commands.ScoreCommand
 * JD-Core Version:    0.7.0.1
 */