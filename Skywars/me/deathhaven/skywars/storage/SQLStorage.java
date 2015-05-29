/*   1:    */ package me.deathhaven.skywars.storage;
/*   2:    */ 
/*   3:    */ import java.sql.Connection;
/*   4:    */ import java.sql.PreparedStatement;
/*   5:    */ import java.sql.ResultSet;
/*   6:    */ import java.sql.SQLException;
/*   7:    */ import java.util.concurrent.LinkedBlockingQueue;
/*   8:    */ import javax.annotation.Nonnull;
/*   9:    */ import me.deathhaven.skywars.SkyWars;
/*  10:    */ import me.deathhaven.skywars.config.PluginConfig;
/*  11:    */ import me.deathhaven.skywars.database.Database;
/*  12:    */ import me.deathhaven.skywars.player.GamePlayer;
/*  13:    */ import org.bukkit.Bukkit;
/*  14:    */ import org.bukkit.scheduler.BukkitScheduler;
/*  15:    */ 
/*  16:    */ public class SQLStorage
/*  17:    */   extends DataStorage
/*  18:    */ {
/*  19:    */   public void loadPlayer(@Nonnull final GamePlayer gamePlayer)
/*  20:    */   {
/*  21: 21 */     Bukkit.getScheduler().runTaskAsynchronously(SkyWars.get(), new Runnable()
/*  22:    */     {
/*  23:    */       public void run()
/*  24:    */       {
/*  25: 24 */         Database database = SkyWars.getDB();
/*  26: 26 */         if (!database.checkConnection()) {
/*  27: 27 */           return;
/*  28:    */         }
/*  29: 30 */         if (!database.doesPlayerExist(gamePlayer.getName()))
/*  30:    */         {
/*  31: 31 */           database.createNewPlayer(gamePlayer.getName());
/*  32:    */         }
/*  33:    */         else
/*  34:    */         {
/*  35: 34 */           Connection connection = database.getConnection();
/*  36: 35 */           PreparedStatement preparedStatement = null;
/*  37: 36 */           ResultSet resultSet = null;
/*  38:    */           try
/*  39:    */           {
/*  40: 39 */             StringBuilder queryBuilder = new StringBuilder();
/*  41: 40 */             queryBuilder.append("SELECT `score`, `games_played`, `games_won`, `kills`, `deaths` ");
/*  42: 41 */             queryBuilder.append("FROM `skywars_player` ");
/*  43: 42 */             queryBuilder.append("WHERE `player_name` = ? ");
/*  44: 43 */             queryBuilder.append("LIMIT 1;");
/*  45:    */             
/*  46: 45 */             preparedStatement = connection.prepareStatement(queryBuilder.toString());
/*  47: 46 */             preparedStatement.setString(1, gamePlayer.getName());
/*  48: 47 */             resultSet = preparedStatement.executeQuery();
/*  49: 49 */             if ((resultSet != null) && (resultSet.next()))
/*  50:    */             {
/*  51: 50 */               if ((!PluginConfig.useEconomy()) || (SkyWars.getEconomy() == null)) {
/*  52: 51 */                 gamePlayer.setScore(resultSet.getInt("score"));
/*  53:    */               }
/*  54: 53 */               gamePlayer.setGamesPlayed(resultSet.getInt("games_played"));
/*  55: 54 */               gamePlayer.setGamesWon(resultSet.getInt("games_won"));
/*  56: 55 */               gamePlayer.setKills(resultSet.getInt("kills"));
/*  57: 56 */               gamePlayer.setDeaths(resultSet.getInt("deaths"));
/*  58:    */             }
/*  59:    */           }
/*  60:    */           catch (SQLException sqlException)
/*  61:    */           {
/*  62: 60 */             sqlException.printStackTrace();
/*  63: 63 */             if (resultSet != null) {
/*  64:    */               try
/*  65:    */               {
/*  66: 65 */                 resultSet.close();
/*  67:    */               }
/*  68:    */               catch (SQLException localSQLException1) {}
/*  69:    */             }
/*  70: 70 */             if (preparedStatement != null) {
/*  71:    */               try
/*  72:    */               {
/*  73: 72 */                 preparedStatement.close();
/*  74:    */               }
/*  75:    */               catch (SQLException localSQLException2) {}
/*  76:    */             }
/*  77:    */           }
/*  78:    */           finally
/*  79:    */           {
/*  80: 63 */             if (resultSet != null) {
/*  81:    */               try
/*  82:    */               {
/*  83: 65 */                 resultSet.close();
/*  84:    */               }
/*  85:    */               catch (SQLException localSQLException3) {}
/*  86:    */             }
/*  87: 70 */             if (preparedStatement != null) {
/*  88:    */               try
/*  89:    */               {
/*  90: 72 */                 preparedStatement.close();
/*  91:    */               }
/*  92:    */               catch (SQLException localSQLException4) {}
/*  93:    */             }
/*  94:    */           }
/*  95:    */         }
/*  96:    */       }
/*  97:    */     });
/*  98:    */   }
/*  99:    */   
/* 100: 82 */   public final SaveProcessor saveProcessor = new SaveProcessor();
/* 101:    */   
/* 102:    */   public static class SaveProcessor
/* 103:    */     implements Runnable
/* 104:    */   {
/* 105: 86 */     private final LinkedBlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue();
/* 106: 87 */     private final Thread thread = new Thread(this);
/* 107: 88 */     private boolean running = true;
/* 108:    */     
/* 109:    */     public SaveProcessor()
/* 110:    */     {
/* 111: 91 */       this.thread.start();
/* 112:    */     }
/* 113:    */     
/* 114:    */     public void submit(Runnable runnable)
/* 115:    */     {
/* 116:    */       try
/* 117:    */       {
/* 118: 96 */         this.taskQueue.put(runnable);
/* 119:    */       }
/* 120:    */       catch (InterruptedException localInterruptedException) {}
/* 121:    */     }
/* 122:    */     
/* 123:    */     public boolean isEmpty()
/* 124:    */     {
/* 125:102 */       return this.taskQueue.isEmpty();
/* 126:    */     }
/* 127:    */     
/* 128:    */     public void stop()
/* 129:    */     {
/* 130:106 */       this.running = false;
/* 131:107 */       this.thread.interrupt();
/* 132:    */     }
/* 133:    */     
/* 134:    */     public void run()
/* 135:    */     {
/* 136:112 */       while (this.running) {
/* 137:    */         try
/* 138:    */         {
/* 139:114 */           ((Runnable)this.taskQueue.take()).run();
/* 140:    */         }
/* 141:    */         catch (InterruptedException localInterruptedException) {}
/* 142:    */       }
/* 143:    */     }
/* 144:    */   }
/* 145:    */   
/* 146:    */   public void savePlayer(@Nonnull final GamePlayer gamePlayer)
/* 147:    */   {
/* 148:123 */     this.saveProcessor.submit(new Runnable()
/* 149:    */     {
/* 150:    */       public void run()
/* 151:    */       {
/* 152:126 */         Database database = SkyWars.getDB();
/* 153:128 */         if (!database.checkConnection()) {
/* 154:129 */           return;
/* 155:    */         }
/* 156:132 */         Connection connection = database.getConnection();
/* 157:133 */         PreparedStatement preparedStatement = null;
/* 158:    */         try
/* 159:    */         {
/* 160:136 */           StringBuilder queryBuilder = new StringBuilder();
/* 161:137 */           queryBuilder.append("UPDATE `skywars_player` SET ");
/* 162:138 */           queryBuilder.append("`score` = ?, `games_played` = ?, ");
/* 163:139 */           queryBuilder.append("`games_won` = ?, `kills` = ?, ");
/* 164:140 */           queryBuilder.append("`deaths` = ?, `last_seen` = NOW() ");
/* 165:141 */           queryBuilder.append("WHERE `player_name` = ?;");
/* 166:    */           
/* 167:143 */           preparedStatement = connection.prepareStatement(queryBuilder.toString());
/* 168:144 */           preparedStatement.setInt(1, gamePlayer.getScore());
/* 169:145 */           preparedStatement.setInt(2, gamePlayer.getGamesPlayed());
/* 170:146 */           preparedStatement.setInt(3, gamePlayer.getGamesWon());
/* 171:147 */           preparedStatement.setInt(4, gamePlayer.getKills());
/* 172:148 */           preparedStatement.setInt(5, gamePlayer.getDeaths());
/* 173:149 */           preparedStatement.setString(6, gamePlayer.getName());
/* 174:150 */           preparedStatement.executeUpdate();
/* 175:    */         }
/* 176:    */         catch (SQLException sqlException)
/* 177:    */         {
/* 178:153 */           sqlException.printStackTrace();
/* 179:156 */           if (preparedStatement != null) {
/* 180:    */             try
/* 181:    */             {
/* 182:158 */               preparedStatement.close();
/* 183:    */             }
/* 184:    */             catch (SQLException localSQLException1) {}
/* 185:    */           }
/* 186:    */         }
/* 187:    */         finally
/* 188:    */         {
/* 189:156 */           if (preparedStatement != null) {
/* 190:    */             try
/* 191:    */             {
/* 192:158 */               preparedStatement.close();
/* 193:    */             }
/* 194:    */             catch (SQLException localSQLException2) {}
/* 195:    */           }
/* 196:    */         }
/* 197:    */       }
/* 198:    */     });
/* 199:    */   }
/* 200:    */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.storage.SQLStorage
 * JD-Core Version:    0.7.0.1
 */