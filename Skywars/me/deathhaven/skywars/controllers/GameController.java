/*  1:   */ package me.deathhaven.skywars.controllers;
/*  2:   */ 
/*  3:   */ import com.google.common.collect.Lists;
/*  4:   */ import com.sk89q.worldedit.CuboidClipboard;
/*  5:   */ import java.util.ArrayList;
/*  6:   */ import java.util.Collection;
/*  7:   */ import java.util.List;
/*  8:   */ import java.util.logging.Level;
/*  9:   */ import java.util.logging.Logger;
/* 10:   */ import javax.annotation.Nonnull;
/* 11:   */ import me.deathhaven.skywars.SkyWars;
/* 12:   */ import me.deathhaven.skywars.game.Game;
/* 13:   */ import me.deathhaven.skywars.game.GameState;
/* 14:   */ 
/* 15:   */ public class GameController
/* 16:   */ {
/* 17:   */   private static GameController instance;
/* 18:20 */   private List<Game> gameList = Lists.newArrayList();
/* 19:   */   
/* 20:   */   public Game findEmpty()
/* 21:   */   {
/* 22:23 */     for (Game game : this.gameList) {
/* 23:24 */       if ((game.getState() != GameState.PLAYING) && (!game.isFull())) {
/* 24:25 */         return game;
/* 25:   */       }
/* 26:   */     }
/* 27:29 */     return create();
/* 28:   */   }
/* 29:   */   
/* 30:   */   public Game create()
/* 31:   */   {
/* 32:33 */     CuboidClipboard schematic = SchematicController.get().getRandom();
/* 33:34 */     Game game = new Game(schematic);
/* 34:36 */     while (!game.isReady())
/* 35:   */     {
/* 36:37 */       String schematicName = SchematicController.get().getName(schematic);
/* 37:38 */       SkyWars.get().getLogger().log(Level.SEVERE, String.format("Schematic '%s' does not have any spawns set!", new Object[] { schematicName }));
/* 38:39 */       SchematicController.get().remove(schematicName);
/* 39:   */       
/* 40:41 */       schematic = SchematicController.get().getRandom();
/* 41:42 */       game = new Game(schematic);
/* 42:   */     }
/* 43:45 */     this.gameList.add(game);
/* 44:46 */     return game;
/* 45:   */   }
/* 46:   */   
/* 47:   */   public void remove(@Nonnull Game game)
/* 48:   */   {
/* 49:50 */     this.gameList.remove(game);
/* 50:   */   }
/* 51:   */   
/* 52:   */   public void shutdown()
/* 53:   */   {
/* 54:54 */     for (Game game : new ArrayList(this.gameList)) {
/* 55:55 */       game.onGameEnd();
/* 56:   */     }
/* 57:   */   }
/* 58:   */   
/* 59:   */   public Collection<Game> getAll()
/* 60:   */   {
/* 61:60 */     return this.gameList;
/* 62:   */   }
/* 63:   */   
/* 64:   */   public static GameController get()
/* 65:   */   {
/* 66:64 */     if (instance == null) {
/* 67:65 */       return GameController.instance = new GameController();
/* 68:   */     }
/* 69:68 */     return instance;
/* 70:   */   }
/* 71:   */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.controllers.GameController
 * JD-Core Version:    0.7.0.1
 */