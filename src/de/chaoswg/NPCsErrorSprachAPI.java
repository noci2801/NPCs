package de.chaoswg;

import net.risingworld.api.events.EventMethod;
import net.risingworld.api.events.Listener;
import net.risingworld.api.events.player.PlayerSpawnEvent;
import net.risingworld.api.objects.Player;

public class NPCsErrorSprachAPI implements Listener {

    private final NPCs plugin;

    public NPCsErrorSprachAPI(NPCs plugin) {
        this.plugin = plugin;
    }
    @EventMethod
    public void onPlayerSpawn(PlayerSpawnEvent event) {
        if(plugin.getDebug()>0){System.out.println("[" + plugin.getDescription("name") + "] "+"Spawn-ERR ");}
        Player player = event.getPlayer();
        boolean notFound = (plugin.getPluginByName("SprachAPI") == null);
        if (player.isAdmin()){
            if (player.getLanguage().equals("de")){
                player.sendYellMessage("[" + plugin.getDescription("name") + "]"+"\nPlugin angehalten"+"\n\n"+"\"SprachAPI\" "+(notFound?"nicht gefunden.":"zu Alte Version. "+"\nBenötiege Version("+plugin.requireSprachApiVersion+") "));
            }else{
                player.sendYellMessage("[" + plugin.getDescription("name") + "]"+"\nPlugin stopt"+"\n\n"+"\"SprachAPI\" "+(notFound?"not found.":"to Old Version. "+"\nRequires version("+plugin.requireSprachApiVersion+") "));
            }
//            // Das Plugin kann auch, nachdem der erste Admin informiert wurde nach einer gewissen Zeit Komplet abgeschaltet werden.
//            // Das Hilft um Konflikte zu Vermeiden.
//            // Hier nach 1 Minute und 30 Sekunden.
//            Timer closePlugin = new Timer(60f*1.5f, 0f, -1, () -> {  // lambda expression
//                // This will be executed when the timer triggers
//                plugin.onDisable(); // Plugin Beenden
//            });
//            closePlugin.start(); // Timer Starten
        }
    }
    
}
