/*  1:   */ package me.deathhaven.skywars.utilities;
/*  2:   */ 
/*  3:   */ import com.google.common.base.Preconditions;
/*  4:   */ import java.lang.reflect.Method;
/*  5:   */ import javax.annotation.Nonnull;
/*  6:   */ import org.bukkit.Bukkit;
/*  7:   */ import org.bukkit.entity.Player;
/*  8:   */ 
/*  9:   */ public class CraftBukkitUtil
/* 10:   */ {
/* 11:   */   public static final String BUKKIT_PACKAGE;
/* 12:   */   public static final String MINECRAFT_PACKAGE;
/* 13:   */   
/* 14:   */   public static Object getHandle(@Nonnull Object target)
/* 15:   */   {
/* 16:16 */     return getMethod(target, "getHandle", new Class[0], new Object[0]);
/* 17:   */   }
/* 18:   */   
/* 19:   */   public static Object getMethod(@Nonnull Object target, @Nonnull String methodName)
/* 20:   */   {
/* 21:20 */     return getMethod(target, methodName, new Class[0], new Object[0]);
/* 22:   */   }
/* 23:   */   
/* 24:   */   public static Object getMethod(@Nonnull Object target, @Nonnull String methodName, @Nonnull Class<?>[] paramTypes, @Nonnull Object[] params)
/* 25:   */   {
/* 26:25 */     Preconditions.checkNotNull(target, "Target is null");
/* 27:26 */     Preconditions.checkNotNull(methodName, "Method name is null");
/* 28:   */     
/* 29:28 */     Class<?> currentClazz = target.getClass();
/* 30:29 */     Object returnValue = null;
/* 31:   */     do
/* 32:   */     {
/* 33:   */       try
/* 34:   */       {
/* 35:33 */         Method method = currentClazz.getDeclaredMethod(methodName, paramTypes);
/* 36:34 */         returnValue = method.invoke(target, params);
/* 37:   */       }
/* 38:   */       catch (Exception exception)
/* 39:   */       {
/* 40:37 */         currentClazz = currentClazz.getSuperclass();
/* 41:   */       }
/* 42:40 */     } while ((currentClazz != null) && (currentClazz.getSuperclass() != null) && (returnValue == null));
/* 43:42 */     return returnValue;
/* 44:   */   }
/* 45:   */   
/* 46:   */   public static void forceRespawn(Player player)
/* 47:   */   {
/* 48:46 */     Preconditions.checkNotNull(player, "Player is null");
/* 49:48 */     if (!player.isDead()) {
/* 50:49 */       return;
/* 51:   */     }
/* 52:52 */     Object playerHandle = getHandle(player);
/* 53:53 */     if (playerHandle == null) {
/* 54:53 */       return;
/* 55:   */     }
/* 56:55 */     Object serverHandle = getHandle(Bukkit.getServer());
/* 57:56 */     if (serverHandle == null) {
/* 58:56 */       return;
/* 59:   */     }
/* 60:58 */     serverHandle = getMethod(serverHandle, "getServer", new Class[0], new Object[0]);
/* 61:59 */     if (serverHandle == null) {
/* 62:59 */       return;
/* 63:   */     }
/* 64:61 */     Object playerListHandle = getMethod(serverHandle, "getPlayerList", new Class[0], new Object[0]);
/* 65:62 */     if (playerListHandle == null) {
/* 66:62 */       return;
/* 67:   */     }
/* 68:64 */     getMethod(playerListHandle, "moveToWorld", new Class[] { playerHandle.getClass(), Integer.TYPE, Boolean.TYPE }, new Object[] { playerHandle, Integer.valueOf(0), Boolean.valueOf(false) });
/* 69:   */   }
/* 70:   */   
/* 71:   */   public static boolean isRunning()
/* 72:   */   {
/* 73:68 */     Object minecraftServer = getMethod(Bukkit.getServer(), "getServer");
/* 74:69 */     if (minecraftServer == null) {
/* 75:70 */       return false;
/* 76:   */     }
/* 77:73 */     Object isRunning = getMethod(minecraftServer, "isRunning");
/* 78:74 */     return ((isRunning instanceof Boolean)) && (((Boolean)isRunning).booleanValue());
/* 79:   */   }
/* 80:   */   
/* 81:   */   static
/* 82:   */   {
/* 83:78 */     String packageName = Bukkit.getServer().getClass().getPackage().getName();
/* 84:79 */     String bukkitVersion = packageName.substring(packageName.lastIndexOf('.') + 1);
/* 85:   */     
/* 86:81 */     BUKKIT_PACKAGE = "org.bukkit.craftbukkit." + bukkitVersion;
/* 87:82 */     MINECRAFT_PACKAGE = "net.minecraft.server." + bukkitVersion;
/* 88:   */   }
/* 89:   */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.utilities.CraftBukkitUtil
 * JD-Core Version:    0.7.0.1
 */