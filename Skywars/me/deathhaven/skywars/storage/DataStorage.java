/*  1:   */ package me.deathhaven.skywars.storage;
/*  2:   */ 
/*  3:   */ import javax.annotation.Nonnull;
/*  4:   */ import me.deathhaven.skywars.player.GamePlayer;
/*  5:   */ 
/*  6:   */ public abstract class DataStorage
/*  7:   */ {
/*  8:   */   private static DataStorage instance;
/*  9:   */   public abstract void loadPlayer(@Nonnull GamePlayer paramGamePlayer);
/* 10:   */   
/* 11:   */   public abstract void savePlayer(@Nonnull GamePlayer paramGamePlayer);
/* 12:   */   
/* 13:   */   public static enum DataStorageType
/* 14:   */   {
/* 15:14 */     FILE,  SQL;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public static void setInstance(DataStorageType dataStorageType)
/* 19:   */   {
/* 20:21 */     switch (dataStorageType)
/* 21:   */     {
/* 22:   */     case FILE: 
/* 23:23 */       instance = new FlatFileStorage();
/* 24:24 */       break;
/* 25:   */     case SQL: 
/* 26:26 */       instance = new SQLStorage();
/* 27:   */     }
/* 28:   */   }
/* 29:   */   
/* 30:   */   public static DataStorage get()
/* 31:   */   {
/* 32:32 */     if (instance == null) {
/* 33:33 */       instance = new FlatFileStorage();
/* 34:   */     }
/* 35:36 */     return instance;
/* 36:   */   }
/* 37:   */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.storage.DataStorage
 * JD-Core Version:    0.7.0.1
 */