package me.deathhaven.skywars.tasks;

import me.deathhaven.skywars.controllers.GameController;
import me.deathhaven.skywars.game.Game;

public class SyncTask implements Runnable {

    //private int tickCounter;

    @Override
    public void run() {
        for (Game game : GameController.get().getAll()) {
            game.onTick();
        }
        
        
        // Updates All Necesarry Things
       /* Iterator<GameChangeEvent> iterator = CustomController.get().getEventsQueue().iterator();
        while(iterator.hasNext()) {
        	CustomController.get().onGameChange(iterator.next());
        	iterator.remove();
        }*/

        // @TODO
        /*if (tickCounter++ == 10) {
            if (SkyWars.getDB() != null) {
                SkyWars.getDB().checkConnection();
            }
            tickCounter = 0;
        }*/
    }
}
