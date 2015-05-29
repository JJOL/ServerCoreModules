/*  1:   */ package me.deathhaven.skywars.tasks;
/*  2:   */ 
/*  3:   */ import me.deathhaven.skywars.controllers.GameController;
/*  4:   */ import me.deathhaven.skywars.game.Game;
/*  5:   */ 
/*  6:   */ public class SyncTask
/*  7:   */   implements Runnable
/*  8:   */ {
/*  9:   */   private int tickCounter;
/* 10:   */   
/* 11:   */   public void run()
/* 12:   */   {
/* 13:12 */     for (Game game : GameController.get().getAll()) {
/* 14:13 */       game.onTick();
/* 15:   */     }
/* 16:   */   }
/* 17:   */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.tasks.SyncTask
 * JD-Core Version:    0.7.0.1
 */