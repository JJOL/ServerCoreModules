/*  1:   */ package me.deathhaven.skywars.utilities;
/*  2:   */ 
/*  3:   */ import java.util.List;
/*  4:   */ 
/*  5:   */ public class StringUtils
/*  6:   */ {
/*  7:   */   public static String toString(String[] args, char color1, char color2)
/*  8:   */   {
/*  9: 8 */     StringBuilder stringBuilder = new StringBuilder();
/* 10:10 */     if (args.length > 1) {
/* 11:11 */       for (int iii = 0; iii < args.length - 1; iii++)
/* 12:   */       {
/* 13:12 */         stringBuilder.append("§");
/* 14:13 */         stringBuilder.append(color1);
/* 15:14 */         stringBuilder.append(args[iii]);
/* 16:15 */         stringBuilder.append("§");
/* 17:16 */         stringBuilder.append(color2);
/* 18:17 */         stringBuilder.append(", ");
/* 19:   */       }
/* 20:   */     }
/* 21:21 */     stringBuilder.append("§");
/* 22:22 */     stringBuilder.append(color1);
/* 23:23 */     stringBuilder.append(args[(args.length - 1)]);
/* 24:   */     
/* 25:25 */     return stringBuilder.toString();
/* 26:   */   }
/* 27:   */   
/* 28:   */   public static String toString(List<String> args, char color1, char color2)
/* 29:   */   {
/* 30:29 */     return toString((String[])args.toArray(new String[args.size()]), color1, color2);
/* 31:   */   }
/* 32:   */   
/* 33:   */   public static String formatScore(int score)
/* 34:   */   {
/* 35:33 */     return formatScore(score, "");
/* 36:   */   }
/* 37:   */   
/* 38:   */   public static String formatScore(int score, String note)
/* 39:   */   {
/* 40:37 */     char color = '7';
/* 41:39 */     if (score > 0) {
/* 42:40 */       color = 'a';
/* 43:41 */     } else if (score < 0) {
/* 44:42 */       color = 'c';
/* 45:   */     }
/* 46:45 */     return "§" + color + "(" + (score > 0 ? "+" : "") + score + note + ")";
/* 47:   */   }
/* 48:   */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.utilities.StringUtils
 * JD-Core Version:    0.7.0.1
 */