/*  1:   */ package me.deathhaven.skywars.controllers;
/*  2:   */ 
/*  3:   */ import com.google.common.collect.Maps;
/*  4:   */ import java.util.List;
/*  5:   */ import java.util.Map;
/*  6:   */ import me.deathhaven.skywars.SkyWars;
/*  7:   */ 
/*  8:   */ public class StatisticsController
/*  9:   */ {
/* 10:   */   private static StatisticsController statisticsController;
/* 11:13 */   private Map<String, Integer> topList = Maps.newLinkedHashMap();
/* 12:   */   private List<TopThreeStatue> topThreeStatueList;
/* 13:   */   
/* 14:   */   public void setTopList(Map<String, Integer> topList)
/* 15:   */   {
/* 16:21 */     this.topList = topList;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public void update() {}
/* 20:   */   
/* 21:   */   public static StatisticsController get()
/* 22:   */   {
/* 23:49 */     if ((statisticsController == null) && (SkyWars.getDB() != null)) {
/* 24:50 */       statisticsController = new StatisticsController();
/* 25:   */     }
/* 26:53 */     return statisticsController;
/* 27:   */   }
/* 28:   */   
/* 29:   */   public class StatisticsWall
/* 30:   */   {
/* 31:   */     private int minX;
/* 32:   */     private int minY;
/* 33:   */     private int minZ;
/* 34:   */     private int maxX;
/* 35:   */     private int maxY;
/* 36:   */     private int maxZ;
/* 37:   */     
/* 38:   */     public StatisticsWall() {}
/* 39:   */   }
/* 40:   */   
/* 41:   */   public class TopThreeStatue
/* 42:   */   {
/* 43:   */     public TopThreeStatue() {}
/* 44:   */     
/* 45:   */     public void update() {}
/* 46:   */   }
/* 47:   */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.controllers.StatisticsController
 * JD-Core Version:    0.7.0.1
 */