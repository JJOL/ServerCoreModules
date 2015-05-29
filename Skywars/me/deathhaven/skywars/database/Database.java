/*   1:    */ package me.deathhaven.skywars.database;
/*   2:    */ 
/*   3:    */ import com.google.common.base.Charsets;
/*   4:    */ import com.google.common.collect.Maps;
/*   5:    */ import com.google.common.io.Resources;
/*   6:    */ import java.io.IOException;
/*   7:    */ import java.net.URL;
/*   8:    */ import java.sql.Connection;
/*   9:    */ import java.sql.DriverManager;
/*  10:    */ import java.sql.PreparedStatement;
/*  11:    */ import java.sql.ResultSet;
/*  12:    */ import java.sql.SQLException;
/*  13:    */ import java.sql.Statement;
/*  14:    */ import java.util.Map;
/*  15:    */ import me.deathhaven.skywars.SkyWars;
/*  16:    */ import org.bukkit.configuration.ConfigurationSection;
/*  17:    */ 
/*  18:    */ public class Database
/*  19:    */ {
/*  20:    */   private final String connectionUri;
/*  21:    */   private final String username;
/*  22:    */   private final String password;
/*  23:    */   private Connection connection;
/*  24:    */   
/*  25:    */   public Database(ConfigurationSection config)
/*  26:    */     throws ClassNotFoundException, SQLException
/*  27:    */   {
/*  28: 24 */     String hostname = config.getString("hostname", "localhost");
/*  29: 25 */     int port = config.getInt("port", 3306);
/*  30: 26 */     String database = config.getString("database", "");
/*  31:    */     
/*  32: 28 */     this.connectionUri = String.format("jdbc:mysql://%s:%d/%s", new Object[] { hostname, Integer.valueOf(port), database });
/*  33: 29 */     this.username = config.getString("username", "");
/*  34: 30 */     this.password = config.getString("password", "");
/*  35:    */     try
/*  36:    */     {
/*  37: 33 */       Class.forName("com.mysql.jdbc.Driver");
/*  38: 34 */       connect();
/*  39:    */     }
/*  40:    */     catch (SQLException sqlException)
/*  41:    */     {
/*  42: 37 */       close();
/*  43: 38 */       throw sqlException;
/*  44:    */     }
/*  45:    */   }
/*  46:    */   
/*  47:    */   private void connect()
/*  48:    */     throws SQLException
/*  49:    */   {
/*  50: 43 */     if (this.connection != null) {
/*  51:    */       try
/*  52:    */       {
/*  53: 45 */         this.connection.createStatement().execute("SELECT 1;");
/*  54:    */       }
/*  55:    */       catch (SQLException sqlException)
/*  56:    */       {
/*  57: 48 */         if (sqlException.getSQLState().equals("08S01")) {
/*  58:    */           try
/*  59:    */           {
/*  60: 50 */             this.connection.close();
/*  61:    */           }
/*  62:    */           catch (SQLException localSQLException1) {}
/*  63:    */         }
/*  64:    */       }
/*  65:    */     }
/*  66: 58 */     if ((this.connection == null) || (this.connection.isClosed())) {
/*  67: 59 */       this.connection = DriverManager.getConnection(this.connectionUri, this.username, this.password);
/*  68:    */     }
/*  69:    */   }
/*  70:    */   
/*  71:    */   public Connection getConnection()
/*  72:    */   {
/*  73: 64 */     return this.connection;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public void close()
/*  77:    */   {
/*  78:    */     try
/*  79:    */     {
/*  80: 69 */       if ((this.connection != null) && (!this.connection.isClosed())) {
/*  81: 70 */         this.connection.close();
/*  82:    */       }
/*  83:    */     }
/*  84:    */     catch (SQLException localSQLException) {}
/*  85: 77 */     this.connection = null;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public boolean checkConnection()
/*  89:    */   {
/*  90:    */     try
/*  91:    */     {
/*  92: 82 */       connect();
/*  93:    */     }
/*  94:    */     catch (SQLException sqlException)
/*  95:    */     {
/*  96: 85 */       close();
/*  97: 86 */       sqlException.printStackTrace();
/*  98:    */       
/*  99: 88 */       return false;
/* 100:    */     }
/* 101: 91 */     return true;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public void createTables()
/* 105:    */     throws IOException, SQLException
/* 106:    */   {
/* 107: 95 */     URL resource = Resources.getResource(SkyWars.class, "/tables.sql");
/* 108: 96 */     String[] databaseStructure = Resources.toString(resource, Charsets.UTF_8).split(";");
/* 109: 98 */     if (databaseStructure.length == 0) {
/* 110: 99 */       return;
/* 111:    */     }
/* 112:102 */     Statement statement = null;
/* 113:    */     try
/* 114:    */     {
/* 115:105 */       this.connection.setAutoCommit(false);
/* 116:106 */       statement = this.connection.createStatement();
/* 117:108 */       for (String query : databaseStructure)
/* 118:    */       {
/* 119:109 */         query = query.trim();
/* 120:111 */         if (!query.isEmpty()) {
/* 121:115 */           statement.execute(query);
/* 122:    */         }
/* 123:    */       }
/* 124:118 */       this.connection.commit();
/* 125:    */     }
/* 126:    */     finally
/* 127:    */     {
/* 128:121 */       this.connection.setAutoCommit(true);
/* 129:123 */       if ((statement != null) && (!statement.isClosed())) {
/* 130:124 */         statement.close();
/* 131:    */       }
/* 132:    */     }
/* 133:    */   }
/* 134:    */   
/* 135:    */   public boolean doesPlayerExist(String player)
/* 136:    */   {
/* 137:130 */     if (!checkConnection()) {
/* 138:131 */       return false;
/* 139:    */     }
/* 140:134 */     int count = 0;
/* 141:135 */     PreparedStatement preparedStatement = null;
/* 142:136 */     ResultSet resultSet = null;
/* 143:    */     try
/* 144:    */     {
/* 145:139 */       StringBuilder queryBuilder = new StringBuilder();
/* 146:140 */       queryBuilder.append("SELECT Count(`player_id`) ");
/* 147:141 */       queryBuilder.append("FROM `skywars_player` ");
/* 148:142 */       queryBuilder.append("WHERE `player_name` = ? ");
/* 149:143 */       queryBuilder.append("LIMIT 1;");
/* 150:    */       
/* 151:145 */       preparedStatement = this.connection.prepareStatement(queryBuilder.toString());
/* 152:146 */       preparedStatement.setString(1, player);
/* 153:147 */       resultSet = preparedStatement.executeQuery();
/* 154:149 */       if (resultSet.next()) {
/* 155:150 */         count = resultSet.getInt(1);
/* 156:    */       }
/* 157:    */     }
/* 158:    */     catch (SQLException sqlException)
/* 159:    */     {
/* 160:154 */       sqlException.printStackTrace();
/* 161:157 */       if (resultSet != null) {
/* 162:    */         try
/* 163:    */         {
/* 164:159 */           resultSet.close();
/* 165:    */         }
/* 166:    */         catch (SQLException localSQLException1) {}
/* 167:    */       }
/* 168:164 */       if (preparedStatement != null) {
/* 169:    */         try
/* 170:    */         {
/* 171:166 */           preparedStatement.close();
/* 172:    */         }
/* 173:    */         catch (SQLException localSQLException2) {}
/* 174:    */       }
/* 175:    */     }
/* 176:    */     finally
/* 177:    */     {
/* 178:157 */       if (resultSet != null) {
/* 179:    */         try
/* 180:    */         {
/* 181:159 */           resultSet.close();
/* 182:    */         }
/* 183:    */         catch (SQLException localSQLException3) {}
/* 184:    */       }
/* 185:164 */       if (preparedStatement != null) {
/* 186:    */         try
/* 187:    */         {
/* 188:166 */           preparedStatement.close();
/* 189:    */         }
/* 190:    */         catch (SQLException localSQLException4) {}
/* 191:    */       }
/* 192:    */     }
/* 193:172 */     return count > 0;
/* 194:    */   }
/* 195:    */   
/* 196:    */   public void createNewPlayer(String player)
/* 197:    */   {
/* 198:176 */     if (!checkConnection()) {
/* 199:177 */       return;
/* 200:    */     }
/* 201:180 */     PreparedStatement preparedStatement = null;
/* 202:    */     try
/* 203:    */     {
/* 204:183 */       StringBuilder queryBuilder = new StringBuilder();
/* 205:184 */       queryBuilder.append("INSERT INTO `skywars_player` ");
/* 206:185 */       queryBuilder.append("(`player_id`, `player_name`, `first_seen`, `last_seen`) ");
/* 207:186 */       queryBuilder.append("VALUES ");
/* 208:187 */       queryBuilder.append("(NULL, ?, NOW(), NOW());");
/* 209:    */       
/* 210:189 */       preparedStatement = this.connection.prepareStatement(queryBuilder.toString());
/* 211:190 */       preparedStatement.setString(1, player);
/* 212:191 */       preparedStatement.executeUpdate();
/* 213:    */     }
/* 214:    */     catch (SQLException sqlException)
/* 215:    */     {
/* 216:194 */       sqlException.printStackTrace();
/* 217:197 */       if (preparedStatement != null) {
/* 218:    */         try
/* 219:    */         {
/* 220:199 */           preparedStatement.close();
/* 221:    */         }
/* 222:    */         catch (SQLException localSQLException1) {}
/* 223:    */       }
/* 224:    */     }
/* 225:    */     finally
/* 226:    */     {
/* 227:197 */       if (preparedStatement != null) {
/* 228:    */         try
/* 229:    */         {
/* 230:199 */           preparedStatement.close();
/* 231:    */         }
/* 232:    */         catch (SQLException localSQLException2) {}
/* 233:    */       }
/* 234:    */     }
/* 235:    */   }
/* 236:    */   
/* 237:    */   public Map<String, Integer> getTopScore(int limit)
/* 238:    */   {
/* 239:207 */     Map<String, Integer> topScore = Maps.newLinkedHashMap();
/* 240:209 */     if (!checkConnection()) {
/* 241:210 */       return topScore;
/* 242:    */     }
/* 243:213 */     PreparedStatement preparedStatement = null;
/* 244:214 */     ResultSet resultSet = null;
/* 245:    */     try
/* 246:    */     {
/* 247:217 */       StringBuilder queryBuilder = new StringBuilder();
/* 248:218 */       queryBuilder.append("SELECT ");
/* 249:219 */       queryBuilder.append("`player_name`, `score` ");
/* 250:220 */       queryBuilder.append("FROM ");
/* 251:221 */       queryBuilder.append("`skywars_player` ");
/* 252:222 */       queryBuilder.append("ORDER BY `score` DESC LIMIT ?;");
/* 253:    */       
/* 254:224 */       preparedStatement = this.connection.prepareStatement(queryBuilder.toString());
/* 255:225 */       preparedStatement.setInt(1, limit);
/* 256:226 */       resultSet = preparedStatement.executeQuery();
/* 257:    */       do
/* 258:    */       {
/* 259:229 */         topScore.put(resultSet.getString("player_name"), Integer.valueOf(resultSet.getInt("score")));
/* 260:228 */         if (resultSet == null) {
/* 261:    */           break;
/* 262:    */         }
/* 263:228 */       } while (resultSet.next());
/* 264:    */     }
/* 265:    */     catch (SQLException sqlException)
/* 266:    */     {
/* 267:233 */       sqlException.printStackTrace();
/* 268:236 */       if (resultSet != null) {
/* 269:    */         try
/* 270:    */         {
/* 271:238 */           resultSet.close();
/* 272:    */         }
/* 273:    */         catch (SQLException localSQLException1) {}
/* 274:    */       }
/* 275:243 */       if (preparedStatement != null) {
/* 276:    */         try
/* 277:    */         {
/* 278:245 */           preparedStatement.close();
/* 279:    */         }
/* 280:    */         catch (SQLException localSQLException2) {}
/* 281:    */       }
/* 282:    */     }
/* 283:    */     finally
/* 284:    */     {
/* 285:236 */       if (resultSet != null) {
/* 286:    */         try
/* 287:    */         {
/* 288:238 */           resultSet.close();
/* 289:    */         }
/* 290:    */         catch (SQLException localSQLException3) {}
/* 291:    */       }
/* 292:243 */       if (preparedStatement != null) {
/* 293:    */         try
/* 294:    */         {
/* 295:245 */           preparedStatement.close();
/* 296:    */         }
/* 297:    */         catch (SQLException localSQLException4) {}
/* 298:    */       }
/* 299:    */     }
/* 300:251 */     return topScore;
/* 301:    */   }
/* 302:    */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.database.Database
 * JD-Core Version:    0.7.0.1
 */