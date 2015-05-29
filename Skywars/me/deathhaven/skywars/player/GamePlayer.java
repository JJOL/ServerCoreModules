/*   1:    */ package me.deathhaven.skywars.player;
/*   2:    */ 
/*   3:    */ import me.deathhaven.skywars.SkyWars;
/*   4:    */ import me.deathhaven.skywars.config.PluginConfig;
/*   5:    */ import me.deathhaven.skywars.game.Game;
/*   6:    */ import me.deathhaven.skywars.storage.DataStorage;
/*   7:    */ import net.milkbowl.vault.economy.Economy;
/*   8:    */ import org.bukkit.entity.Player;
/*   9:    */ import org.bukkit.inventory.ItemStack;
/*  10:    */ import org.bukkit.inventory.PlayerInventory;
/*  11:    */ 
/*  12:    */ public class GamePlayer
/*  13:    */ {
/*  14:    */   private final Player bukkitPlayer;
/*  15:    */   private final String playerName;
/*  16:    */   private Game game;
/*  17:    */   private boolean chosenKit;
/*  18:    */   private boolean spectating;
/*  19:    */   private int score;
/*  20:    */   private int gamesPlayed;
/*  21:    */   private int gamesWon;
/*  22:    */   private int kills;
/*  23:    */   private int deaths;
/*  24:    */   private boolean skipFallDamage;
/*  25:    */   private boolean skipFireTicks;
/*  26: 25 */   private ItemStack[] savedInventoryContents = null;
/*  27: 26 */   private ItemStack[] savedArmorContents = null;
/*  28:    */   
/*  29:    */   public GamePlayer(Player bukkitPlayer)
/*  30:    */   {
/*  31: 29 */     this.bukkitPlayer = bukkitPlayer;
/*  32: 30 */     this.playerName = bukkitPlayer.getName();
/*  33: 31 */     this.spectating = false;
/*  34: 32 */     DataStorage.get().loadPlayer(this);
/*  35:    */   }
/*  36:    */   
/*  37:    */   public GamePlayer(String playerName)
/*  38:    */   {
/*  39: 36 */     this.bukkitPlayer = null;
/*  40: 37 */     this.playerName = playerName;
/*  41: 38 */     DataStorage.get().loadPlayer(this);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void save()
/*  45:    */   {
/*  46: 42 */     DataStorage.get().savePlayer(this);
/*  47:    */   }
/*  48:    */   
/*  49:    */   public Player getBukkitPlayer()
/*  50:    */   {
/*  51: 46 */     return this.bukkitPlayer;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public boolean isPlaying()
/*  55:    */   {
/*  56: 50 */     return this.game != null;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public boolean isSpectating()
/*  60:    */   {
/*  61: 54 */     return this.spectating;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public void setGame(Game game)
/*  65:    */   {
/*  66: 58 */     this.game = game;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public Game getGame()
/*  70:    */   {
/*  71: 62 */     return this.game;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public boolean hasChosenKit()
/*  75:    */   {
/*  76: 66 */     return this.chosenKit;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public void setChosenKit(boolean yes)
/*  80:    */   {
/*  81: 70 */     this.chosenKit = yes;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public void setSpectating(boolean s)
/*  85:    */   {
/*  86: 74 */     this.spectating = s;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public int getScore()
/*  90:    */   {
/*  91: 78 */     if ((PluginConfig.useEconomy()) && (SkyWars.getEconomy() != null)) {
/*  92: 79 */       return (int)SkyWars.getEconomy().getBalance(this.playerName);
/*  93:    */     }
/*  94: 82 */     return this.score;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public void setScore(int score)
/*  98:    */   {
/*  99: 86 */     if ((PluginConfig.useEconomy()) && (SkyWars.getEconomy() != null))
/* 100:    */     {
/* 101: 87 */       double balance = SkyWars.getEconomy().getBalance(this.playerName);
/* 102: 88 */       if (balance < 0.0D) {
/* 103: 89 */         SkyWars.getEconomy().depositPlayer(this.playerName, -balance);
/* 104:    */       } else {
/* 105: 91 */         SkyWars.getEconomy().withdrawPlayer(this.playerName, balance);
/* 106:    */       }
/* 107: 93 */       addScore(score);
/* 108:    */     }
/* 109:    */     else
/* 110:    */     {
/* 111: 96 */       this.score = score;
/* 112:    */     }
/* 113:    */   }
/* 114:    */   
/* 115:    */   public void addScore(int score)
/* 116:    */   {
/* 117:101 */     if ((PluginConfig.useEconomy()) && (SkyWars.getEconomy() != null))
/* 118:    */     {
/* 119:102 */       if (score < 0) {
/* 120:103 */         SkyWars.getEconomy().withdrawPlayer(this.playerName, -score);
/* 121:    */       } else {
/* 122:105 */         SkyWars.getEconomy().depositPlayer(this.playerName, score);
/* 123:    */       }
/* 124:    */     }
/* 125:    */     else {
/* 126:109 */       this.score += score;
/* 127:    */     }
/* 128:    */   }
/* 129:    */   
/* 130:    */   public String toString()
/* 131:    */   {
/* 132:115 */     return this.playerName;
/* 133:    */   }
/* 134:    */   
/* 135:    */   public String getName()
/* 136:    */   {
/* 137:119 */     return this.playerName;
/* 138:    */   }
/* 139:    */   
/* 140:    */   public int getGamesPlayed()
/* 141:    */   {
/* 142:123 */     return this.gamesPlayed;
/* 143:    */   }
/* 144:    */   
/* 145:    */   public void setGamesPlayed(int gamesPlayed)
/* 146:    */   {
/* 147:127 */     this.gamesPlayed = gamesPlayed;
/* 148:    */   }
/* 149:    */   
/* 150:    */   public int getGamesWon()
/* 151:    */   {
/* 152:131 */     return this.gamesWon;
/* 153:    */   }
/* 154:    */   
/* 155:    */   public void setGamesWon(int gamesWon)
/* 156:    */   {
/* 157:135 */     this.gamesWon = gamesWon;
/* 158:    */   }
/* 159:    */   
/* 160:    */   public int getKills()
/* 161:    */   {
/* 162:139 */     return this.kills;
/* 163:    */   }
/* 164:    */   
/* 165:    */   public void setKills(int kills)
/* 166:    */   {
/* 167:143 */     this.kills = kills;
/* 168:    */   }
/* 169:    */   
/* 170:    */   public int getDeaths()
/* 171:    */   {
/* 172:147 */     return this.deaths;
/* 173:    */   }
/* 174:    */   
/* 175:    */   public void setDeaths(int deaths)
/* 176:    */   {
/* 177:151 */     this.deaths = deaths;
/* 178:    */   }
/* 179:    */   
/* 180:    */   public void setSkipFallDamage(boolean skipFallDamage)
/* 181:    */   {
/* 182:155 */     this.skipFallDamage = skipFallDamage;
/* 183:    */   }
/* 184:    */   
/* 185:    */   public void setSkipFireTicks(boolean skipFireTicks)
/* 186:    */   {
/* 187:159 */     this.skipFireTicks = skipFireTicks;
/* 188:    */   }
/* 189:    */   
/* 190:    */   public boolean shouldSkipFallDamage()
/* 191:    */   {
/* 192:163 */     return this.skipFallDamage;
/* 193:    */   }
/* 194:    */   
/* 195:    */   public boolean shouldSkipFireTicks()
/* 196:    */   {
/* 197:167 */     return this.skipFireTicks;
/* 198:    */   }
/* 199:    */   
/* 200:    */   public void saveCurrentState()
/* 201:    */   {
/* 202:171 */     this.savedArmorContents = ((ItemStack[])this.bukkitPlayer.getInventory().getArmorContents().clone());
/* 203:172 */     this.savedInventoryContents = ((ItemStack[])this.bukkitPlayer.getInventory().getContents().clone());
/* 204:    */   }
/* 205:    */   
/* 206:    */   public void restoreState()
/* 207:    */   {
/* 208:177 */     boolean shouldUpdateInventory = false;
/* 209:179 */     if (this.savedArmorContents != null)
/* 210:    */     {
/* 211:180 */       this.bukkitPlayer.getInventory().setArmorContents(this.savedArmorContents);
/* 212:181 */       this.savedArmorContents = null;
/* 213:182 */       shouldUpdateInventory = true;
/* 214:    */     }
/* 215:185 */     if (this.savedInventoryContents != null)
/* 216:    */     {
/* 217:186 */       this.bukkitPlayer.getInventory().setContents(this.savedInventoryContents);
/* 218:187 */       this.savedInventoryContents = null;
/* 219:188 */       shouldUpdateInventory = true;
/* 220:    */     }
/* 221:191 */     if (shouldUpdateInventory) {
/* 222:192 */       this.bukkitPlayer.updateInventory();
/* 223:    */     }
/* 224:    */   }
/* 225:    */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.player.GamePlayer
 * JD-Core Version:    0.7.0.1
 */