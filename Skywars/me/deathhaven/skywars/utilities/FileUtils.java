/*  1:   */ package me.deathhaven.skywars.utilities;
/*  2:   */ 
/*  3:   */ import java.io.File;
/*  4:   */ import java.io.FileOutputStream;
/*  5:   */ import java.io.IOException;
/*  6:   */ import java.io.InputStream;
/*  7:   */ import java.io.OutputStream;
/*  8:   */ import java.util.logging.Level;
/*  9:   */ import java.util.logging.Logger;
/* 10:   */ import javax.annotation.Nonnull;
/* 11:   */ import org.bukkit.plugin.Plugin;
/* 12:   */ 
/* 13:   */ public class FileUtils
/* 14:   */ {
/* 15:   */   public static boolean deleteFolder(@Nonnull File file)
/* 16:   */   {
/* 17:12 */     if (file.exists())
/* 18:   */     {
/* 19:13 */       boolean result = true;
/* 20:15 */       if (file.isDirectory())
/* 21:   */       {
/* 22:16 */         File[] contents = file.listFiles();
/* 23:18 */         if (contents != null) {
/* 24:19 */           for (File f : contents) {
/* 25:20 */             result = (result) && (deleteFolder(f));
/* 26:   */           }
/* 27:   */         }
/* 28:   */       }
/* 29:25 */       return (result) && (file.delete());
/* 30:   */     }
/* 31:28 */     return false;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public static void saveResource(Plugin plugin, String resourcePath, File outFile, boolean replace)
/* 35:   */   {
/* 36:32 */     if ((resourcePath == null) || (resourcePath.equals(""))) {
/* 37:33 */       throw new IllegalArgumentException("ResourcePath cannot be null or empty");
/* 38:   */     }
/* 39:36 */     resourcePath = resourcePath.replace('\\', '/');
/* 40:37 */     InputStream in = plugin.getResource(resourcePath);
/* 41:38 */     if (in == null) {
/* 42:39 */       throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found.");
/* 43:   */     }
/* 44:42 */     int lastIndex = resourcePath.lastIndexOf('/');
/* 45:43 */     File outDir = new File(plugin.getDataFolder(), resourcePath.substring(0, lastIndex >= 0 ? lastIndex : 0));
/* 46:45 */     if ((!outDir.exists()) && (!outDir.mkdirs())) {
/* 47:46 */       return;
/* 48:   */     }
/* 49:   */     try
/* 50:   */     {
/* 51:50 */       if ((!outFile.exists()) || (replace))
/* 52:   */       {
/* 53:51 */         OutputStream out = new FileOutputStream(outFile);
/* 54:52 */         byte[] buf = new byte[1024];
/* 55:   */         int len;
/* 56:54 */         while ((len = in.read(buf)) > 0)
/* 57:   */         {
/* 58:   */           int len;
/* 59:55 */           out.write(buf, 0, len);
/* 60:   */         }
/* 61:57 */         out.close();
/* 62:58 */         in.close();
/* 63:   */       }
/* 64:   */       else
/* 65:   */       {
/* 66:60 */         plugin.getLogger().log(Level.WARNING, "Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
/* 67:   */       }
/* 68:   */     }
/* 69:   */     catch (IOException ex)
/* 70:   */     {
/* 71:64 */       plugin.getLogger().log(Level.SEVERE, "Could not save " + outFile.getName() + " to " + outFile, ex);
/* 72:   */     }
/* 73:   */   }
/* 74:   */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.utilities.FileUtils
 * JD-Core Version:    0.7.0.1
 */