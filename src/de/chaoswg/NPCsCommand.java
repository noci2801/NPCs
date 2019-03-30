/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.chaoswg;

import static de.chaoswg.NPCs.plugin;
import net.risingworld.api.Timer;
import net.risingworld.api.events.EventMethod;
import net.risingworld.api.events.Listener;
import net.risingworld.api.events.player.PlayerChangeGamemodeEvent;
import net.risingworld.api.events.player.PlayerCommandEvent;
import net.risingworld.api.gui.GuiLabel;
import net.risingworld.api.gui.GuiPanel;
import net.risingworld.api.objects.Player;

/**
 *
 * @author Schmull
 */
public class NPCsCommand implements Listener {

    private final NPCs plugin;

    public NPCsCommand(NPCs plugin) {
        this.plugin = plugin;
    }
    @EventMethod
    public void onPlayerCommand(PlayerCommandEvent event){
        Player player = event.getPlayer();
        //Command abholen und ggf. Leerzeichen auf eines reduzieren
        String command = event.getCommand();
        String[] cmd = command.split(" ");
        if (cmd[0].toLowerCase().equals("/"+plugin.sysConfig.getValue("command")) || cmd[0].toLowerCase().equals("/"+plugin.sysConfig.getValue("command").toLowerCase())) {
            if (cmd.length > 1){
                //### Zeigt dem Spieler den Eingegebenen Befehl an
                player.sendTextMessage("");
                player.sendTextMessage(("#> "+cmd[0].substring(1)+" "+command.substring(1+cmd[0].length()))); //+1 für jedes Leerzeichen
            }
            if (cmd.length > 1) {
                if (cmd[1].toLowerCase().equals("debug")) {
                    int deb = plugin.getDebug();
                    if (cmd.length > 2) 
                    try{deb=Integer.parseInt(cmd[2]);plugin.setDebug(deb);}catch(NumberFormatException e){
                        System.err.println("[" + plugin.getDescription("name") + "] "+"Fehler "+e.getMessage());
                    }
                }
                if (cmd[1].toLowerCase().equals("msg")) {
                    String msg = command.substring(cmd[0].length()+1+cmd[1].length()+1);
                    CRT.ClassLambadHelper l = new CRT.ClassLambadHelper();
                    l.n = 0;
                    if (msg!=null)
                    plugin.server.getAllPlayers().forEach((Player playerBroadcast) -> {
                        if (player!=null){
                            playerBroadcast.sendYellMessage(msg);
                            l.n++;
                        }
                    });
                }
                
                if (cmd[1].toLowerCase().equals("follow")&& (plugin.Access_Groups.indexOf(player.getPermissionGroup().toLowerCase())>=0||(plugin.Access_Admins&&player.isAdmin()))) {
                    //### Param 2 verarbeiten
                    if (cmd.length > 2){
                        int value;
                        boolean bol = (player.getAttribute(plugin.plA.npcFollow)!=null?(boolean)player.getAttribute(plugin.plA.npcFollow):false);
                        //### Int prüfung
                        try{ value = Integer.parseInt(cmd[2]); }catch(NumberFormatException e){ value = -1; }
                        //### Boolean aus Text auswerten
                        if (value>=0){ bol = (value>0); }else{
                            if (cmd[2].toLowerCase().equals("true")||cmd[2].toLowerCase().equals("false")){
                                bol = cmd[2].toLowerCase().equals("true")||!cmd[2].toLowerCase().equals("false");
                            }
                        }
                        String info = value+" "+bol;
                        if(plugin.getDebug()>3){System.out.println("[" + plugin.getDescription("name") + "] "+"Command " + cmd[2] + " " + info );}
                        player.setAttribute(plugin.plA.npcFollow,bol);

                    }else{
                        plugin.followToggle(player);
                            
                    }
                    if (player.getAttribute(plugin.plA.npcFollow)!=null) player.sendTextMessage("   "+"follow "+((boolean)player.getAttribute(plugin.plA.npcFollow)?plugin.sprachApiDaten.getText(player, "on"):plugin.sprachApiDaten.getText(player, "off")));
                    //if (plugin.Access_Groups.indexOf(player.getPermissionGroup().toLowerCase())==-1){player.sendTextMessage("   "+plugin.sprachApiDaten.getText(player, "npc_access_denied"));}
                    plugin.followShow(player);

                }else if (cmd[1].toLowerCase().equals("follow")&& plugin.Access_Groups.indexOf(player.getPermissionGroup().toLowerCase())==-1){
                    player.sendTextMessage("   "+plugin.sprachApiDaten.getText(player, "npc_access_denied"));
                }
                //Fehler behebung
                if (cmd[1].toLowerCase().equals("err")) {
                    if (cmd[2].toLowerCase().equals("gui")) {
                        if (cmd[3].toLowerCase().equals("mouse")) {
                            int value;
                            boolean bol = false;
                            //### Int prüfung
                            try{ value = Integer.parseInt(cmd[2]); }catch(NumberFormatException e){ value = -1; }
                            //### Boolean aus Text auswerten
                            if (value>=0){ bol = (value>0); }else{
                                if (cmd[4].toLowerCase().equals("true")||cmd[4].toLowerCase().equals("false")){
                                    bol = cmd[4].toLowerCase().equals("true")||!cmd[4].toLowerCase().equals("false");
                                }
                            }
                            String info = value+" "+bol;
                            if(plugin.getDebug()>3){System.out.println("[" + plugin.getDescription("name") + "] "+"Command " + cmd[2] + " " + info );}
                            player.setMouseCursorVisible(bol);
                        }
                        
                    }
                }
            }
        } else if (cmd[0].toLowerCase().equals("/")) {
            // Ausgabe für die Chat Eingabe "/" anzeigen. Das Komando aus der Konfigurations Datei
            player.sendTextMessage("" + "/"+plugin.sysConfig.getValue("command"));
        }
    }

    @EventMethod
    public void onPlayerChangeGamemode(PlayerChangeGamemodeEvent event){
        final Player player = event.getPlayer();
        Timer timerGM = new Timer(0.1f, 0f, 0, () -> {  //lambda expression
            ((GuiLabel) player.getAttribute(plugin.plA.labelNpcInfo)).setPosition(plugin.POS_INFO_X, plugin.POS_INFO_Y-((player.isCreativeModeEnabled()?plugin.POS_INFO_Y_GM:0)),true);
            ((GuiPanel) player.getAttribute(plugin.plA.panelNPCAnzahl)).setPosition(plugin.POS_NPC_X, plugin.POS_NPC_Y-((player.isCreativeModeEnabled()?plugin.POS_NPC_Y_GM:0)),true);
            if(plugin.getDebug()>1){System.out.println("[" + plugin.getDescription("name") + "] "+"ChangeGamemode " + player.getName() + " "+player.isCreativeModeEnabled()+ " " + ((GuiLabel) player.getAttribute(plugin.plA.labelNpcInfo)).getPositionX()+"-"+((GuiLabel) player.getAttribute(plugin.plA.labelNpcInfo)).getPositionY());}
	});
        timerGM.start();
    }    
}
