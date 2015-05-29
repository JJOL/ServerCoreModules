/*  1:   */ package me.deathhaven.skywars.utilities;
/*  2:   */ 
/*  3:   */ import java.util.logging.Level;
/*  4:   */ import java.util.logging.Logger;
/*  5:   */ import me.deathhaven.skywars.SkyWars;
/*  6:   */ 
/*  7:   */ public class LogUtils
/*  8:   */ {
/*  9:   */   public static void log(Level level, String message, Object... args)
/* 10:   */   {
/* 11:10 */     if (args.length > 0) {
/* 12:11 */       message = String.format(message, args);
/* 13:   */     }
/* 14:14 */     SkyWars.get().getLogger().log(level, message);
/* 15:   */   }
/* 16:   */   
/* 17:   */   public static void log(Level level, Class<?> clazz, String message, Object... args)
/* 18:   */   {
/* 19:18 */     log(level, String.format("[%s] %s", new Object[] { clazz.getSimpleName(), message }), args);
/* 20:   */   }
/* 21:   */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.utilities.LogUtils
 * JD-Core Version:    0.7.0.1
 */