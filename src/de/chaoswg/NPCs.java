/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.chaoswg;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.risingworld.api.Plugin;
import net.risingworld.api.Server;
import net.risingworld.api.Timer;
import net.risingworld.api.World;
import net.risingworld.api.events.EventMethod;
import net.risingworld.api.events.Listener;
import net.risingworld.api.events.player.PlayerDisconnectEvent;
import net.risingworld.api.events.player.PlayerSpawnEvent;
import net.risingworld.api.events.player.gui.PlayerGuiElementClickEvent;
import net.risingworld.api.events.player.gui.PlayerGuiInputEvent;
import net.risingworld.api.gui.Font;
import net.risingworld.api.gui.GuiElement;
import net.risingworld.api.gui.GuiLabel;
import net.risingworld.api.gui.GuiPanel;
import net.risingworld.api.gui.PivotPosition;
import net.risingworld.api.objects.Npc;
import net.risingworld.api.objects.Player;
import net.risingworld.api.utils.Vector3f;
import net.risingworld.api.utils.Vector3i;

/**
 *
 * @author Schmull
 */
public class NPCs extends Plugin implements Listener {

    static NPCs plugin;
    private String conf;
    Server server;
    private World world;
    private int debug;
    DateTimeFormatter dtf;
    DateTimeFormatter chatDTF;
    PlA     plA;
    String  KEY_follow_Aktion;
    float   KEY_follow_Radius;
    //String  KEY_interakt_Aktion;
    float   KEY_interakt_Radius;
    String KEY_toggle_Follow;
    String KEY_cancle_Aktion;
    int GUI_HG_Color,GUI_HG_ColorBorder,GUI_TEXT_Color_true,GUI_TEXT_Color_false,GUI_TEXT_Color_hot,GUI_TEXT_Color_act,GUI_BNT_Color,GUI_BNT_ColorBorder,GUI_TEXT_Color_follow;
    int HUD_HG_Color,HUD_HG_ColorBorder,HUD_FREE_Color,HUD_FREE_ColorBorder,HUD_FULL_Color,HUD_FULL_ColorBorder,GUI_Border,HUD_Border;
    
    float PHI,TIMER_Screen,TIMER_Info,TIMER_WelkomMSG,HUD_NPC_Size,TIMER_MenüMSG;
    float POS_NPC_X,POS_NPC_Y,POS_NPC_Y_GM,POS_INFO_X,POS_INFO_Y,POS_INFO_Y_GM,POS_SCREEN_X,POS_SCREEN_Y;
    String POS_NPC_PIVOT,POS_INFO_PIVOT,POS_SCREEN_PIVOT;

    int maxNPCs;
    final Map<Npc,Player> followListe;
    final HashMap<Npc, Vector3f> followListePos;
    final HashMap<Npc, Boolean> followListeIsLocked;
    float followBlockNoNewPos,followBlockNewPos,followBlockNewPosTrigger,followBlockNewPosRun,followBlockNpcDistance,followBlockNpcRun,followBlockNpcRandom;
    int GUI_TEXT_Color_distance_high,GUI_TEXT_Color_distance_low,GUI_TEXT_Color_distance_ok;
    ArrayList<String> Access_Groups;
    boolean Access_Admins,AllowOthers;
    public  int getDebug(){ return debug; }
    void setDebug(int deb) { this.debug = deb; }


    NPCsClassText sprachApiDaten;
    GetConfigDaten sysConfig,hudConfig;

    private SprachAPI sprachApiPlugin;
    String requireSprachApiVersion;
    //private RWGui rwGuiPlugin;
    //String requireRwGuiVersion;
    private CRT crt;
    
    public NPCs() { 
        this.followListe = new HashMap<>(); 
        this.followListePos = new HashMap<>(); 
        this.followListeIsLocked = new HashMap<>(); 
        
    }

    @Override
    public void onEnable() {
        plugin                          = this;
        server                          = getServer();
        world                           = getWorld();
        requireSprachApiVersion         = "1.1.4";
        //requireRwGuiVersion           = "0.5.1";
        debug                           = 1;
        String pattern                  = "yyyy-MM-dd HH:mm:ss";
        dtf                             = DateTimeFormatter.ofPattern(pattern);
        plA                             = new PlA();
        
        KEY_follow_Aktion               = "KEY_F";
        KEY_follow_Radius               = -1f;
        //KEY_interakt_Aktion           = "KEY_F";
        KEY_interakt_Radius             = 4.5f;
        KEY_toggle_Follow               = "";
        KEY_cancle_Aktion               = "KEY_ESCAPE";
        
        GUI_Border                      = 3;
        GUI_TEXT_Color_false            = 0xff0000ff;
        GUI_TEXT_Color_hot              = 0xffff00ff;
        GUI_TEXT_Color_true             = 0x00ff00ff;
        GUI_TEXT_Color_act              = 0x00c8ffff;
        GUI_TEXT_Color_follow           = 0xff40ffff;

        GUI_TEXT_Color_distance_high    = 0xffffffff;
        GUI_TEXT_Color_distance_low     = 0xffaaaaff;
        GUI_TEXT_Color_distance_ok      = 0xaaffaaff;
        GUI_HG_Color                    = 0x0000ff80;
        GUI_HG_ColorBorder              = 0xc1c1ff64;
        GUI_BNT_Color                   = 0x00000080;
        GUI_BNT_ColorBorder             = 0x00000064;
        
        HUD_Border                      = 2;
        HUD_NPC_Size                    = 0.015f;
        POS_NPC_X                       = 0.5f;
        POS_NPC_Y                       = 1.0f;
        POS_NPC_Y_GM                    = 0.0f;
        POS_INFO_X                      = 0.99f;
        POS_INFO_Y                      = 0.875f;
        POS_INFO_Y_GM                   = 0.125f;
        POS_SCREEN_X                    = 0.5f;
        POS_SCREEN_Y                    = 0.5725f;
        POS_NPC_PIVOT                   = "CenterTop";
        POS_INFO_PIVOT                  = "TopRight";
        POS_SCREEN_PIVOT                = "Center";

        HUD_HG_Color                    = 0x00000080;
        HUD_HG_ColorBorder              = 0x00000064;
        HUD_FREE_Color                  = 0x00000064;
        HUD_FREE_ColorBorder            = 0x00ff00c0;
        HUD_FULL_Color                  = 0x0000ffc0;
        HUD_FULL_ColorBorder            = 0x00c8ff64;
        
        PHI                             = 1.61803399f;
        
        TIMER_Screen                    = 0.75f;
        TIMER_Info                      = 4.25f;
        TIMER_WelkomMSG                 = 0.0f;
        TIMER_MenüMSG                   = 2.75f;
        
        followBlockNoNewPos             = 3.0f;
        followBlockNewPos               = 5.0f;
        followBlockNewPosTrigger        = 10.0f;
        followBlockNewPosRun            = 15.0f;
        followBlockNpcDistance          = 7.5f;
        followBlockNpcRandom            = 2.5f;
        followBlockNpcRun               = 4.0f;

        maxNPCs                         = 6;
        Access_Groups                   = new ArrayList<>();
        Access_Admins                   = true;
        AllowOthers                     = false;
                
        System.out.println("[" + plugin.getDescription("name") + "] Enabled");

        if(plugin.getPluginByName("SprachAPI") != null /*&& plugin.getPluginByName("com.vms.RWGUI") != null*/ ){
            // Lade Plugin
            sprachApiPlugin = (SprachAPI) plugin.getPluginByName("SprachAPI");
            //rwGuiPlugin = (RWGui) plugin.getPluginByName("com.vms.RWGUI");
            crt             = new CRT();
            conf            = CRT.getSpec()+"config";
            // Prüfe SprachAPI Version
            if (crt.isSameVersion(requireSprachApiVersion,sprachApiPlugin.getDescription("version"),new CRT.ClassDebug(plugin,debug))){    
                //if (crt.isSameVersion(requireRwGuiVersion,rwGuiPlugin.getDescription("version"),new CRT.ClassDebug(plugin,debug))){
                    // Lade Eigene Text Klasse
                    sprachApiDaten = new NPCsClassText();
                    sprachApiDaten.setDebug(debug);   //### gebe Debug weiter
                    sprachApiDaten.setDir(conf+sprachApiDaten.getDir());
                    sprachApiDaten.INI(plugin);

                    if(debug>0){System.out.println("[" + plugin.getDescription("name") + "] "+"Enabled "+"SprachAPI("+sprachApiPlugin.getDescription("version")+") OK");}
                    //#########################
                    //### CONFIG für System ###
                    //### Definiere Variablen
                    String[][] sysConfigArray = {
                        //Name         Wert
                        {"command", "NPC"},
                        {"KEY_follow_akt", KEY_follow_Aktion},
                        {"KEY_follow_rad", String.valueOf(KEY_follow_Radius)},
                        {"KEY_follow_tog", KEY_toggle_Follow},
                        //{"KEY_interakt_akt", KEY_interakt_Aktion},
                        {"KEY_interakt_rad", String.valueOf(KEY_interakt_Radius)},
                        {"KEY_cancle_akt", KEY_cancle_Aktion},

                        {"TIMER_Info", String.valueOf(TIMER_Info)},
                        {"TIMER_Screen", String.valueOf(TIMER_Screen)},
                        //{"TIMER_WekomMSG", String.valueOf(TIMER_WelkomMSG)},
                        {"TIMER_MenuMSG", String.valueOf(TIMER_MenüMSG)},

                        {"FOLLOW_NoNewPos", String.valueOf(followBlockNoNewPos)},
                        {"FOLLOW_NewPos", String.valueOf(followBlockNewPos)},
                        {"FOLLOW_NewPosTrigger", String.valueOf(followBlockNewPosTrigger)},
                        {"FOLLOW_NewPosRun", String.valueOf(followBlockNewPosRun)},
                        {"FOLLOW_NpcDistance", String.valueOf(followBlockNpcDistance)},
                        {"FOLLOW_NpcRandom", String.valueOf(followBlockNpcRandom)},
                        {"FOLLOW_NpcRun", String.valueOf(followBlockNpcRun)},

                        {"FOLLOW_MaxNPCs", String.valueOf(maxNPCs)},
                        //{"", String.valueOf()},

                        //{"MySqlDatabase", ""},
                        //{"MySqlPrefix", ""},
                        //{"MySqlIP", ""},
                        //{"MySqlPort", ""},
                        //{"MySqlUser", ""},
                        //{"MySqlPW", ""},
                        {"Access_Groups", ""},
                        {"Access_Admins", (Access_Admins?"true":"false")},
                        {"Allow_Others", (AllowOthers?"true":"false")},
                        {"Debug", String.valueOf(debug)}
                    };
                    //################################
                    //### Initialiesiere System Config
                    sysConfig                   = new GetConfigDaten("Plugin", sysConfigArray, this, debug,conf);
                    debug                       = Integer.parseInt(sysConfig.getValue("Debug"));
                    KEY_follow_Aktion           = sysConfig.getValue("KEY_follow_akt");
                    KEY_follow_Radius           = Float.parseFloat(sysConfig.getValue("KEY_follow_rad"));
                    KEY_toggle_Follow           = sysConfig.getValue("KEY_follow_tog");
                    //KEY_interakt_Aktion = sysConfig.getValue("KEY_interakt_akt");
                    KEY_interakt_Radius = Float.parseFloat(sysConfig.getValue("KEY_interakt_rad"));
                    TIMER_Info                  = Float.parseFloat(sysConfig.getValue("TIMER_Info").replace(",", "."));
                    TIMER_Screen                = Float.parseFloat(sysConfig.getValue("TIMER_Screen").replace(",", "."));
                    //TIMER_WelkomMSG  = Float.parseFloat(sysConfig.getValue("TIMER_WekomMSG").replace(",", "."));
                    TIMER_MenüMSG               = Float.parseFloat(sysConfig.getValue("TIMER_MenuMSG").replace(",", "."));
                    followBlockNoNewPos         = Float.parseFloat(sysConfig.getValue("FOLLOW_NoNewPos").replace(",", "."));
                    followBlockNewPos           = Float.parseFloat(sysConfig.getValue("FOLLOW_NewPos").replace(",", "."));
                    followBlockNewPosTrigger    = Float.parseFloat(sysConfig.getValue("FOLLOW_NewPosTrigger").replace(",", "."));
                    followBlockNewPosRun        = Float.parseFloat(sysConfig.getValue("FOLLOW_NewPosRun").replace(",", "."));
                    followBlockNpcDistance      = Float.parseFloat(sysConfig.getValue("FOLLOW_NpcDistance").replace(",", "."));
                    followBlockNpcRandom        = Float.parseFloat(sysConfig.getValue("FOLLOW_NpcRandom").replace(",", "."));
                    followBlockNpcRun           = Float.parseFloat(sysConfig.getValue("FOLLOW_NpcRun").replace(",", "."));
                    maxNPCs                     = Integer.parseInt(sysConfig.getValue("FOLLOW_MaxNPCs"));
                    String Access_Group         = sysConfig.getValue("Access_Groups");
                    Access_Admins               = sysConfig.getValue("Access_Admins").toLowerCase().equals("true");
                    AllowOthers                 = sysConfig.getValue("Allow_Others").toLowerCase().equals("true");
                    for(String line:Access_Group.split(" ")){
                        if(debug>1){System.out.println("[" + plugin.getDescription("name") + "] "+"Line["+line+"]" );}
                        if (!line.equals("")) Access_Groups.add(line.toLowerCase());
                    }
                    if(debug>0){System.out.println("[" + plugin.getDescription("name") + "] "+"Access_Groups["+Access_Groups.size()+"]" +(debug>1?"\n\t"+Access_Groups:"")  );}
                    
                    String[][] hudConfigArray = {
                        {"GUI_HG_Color", String.format("%8s",Integer.toHexString(GUI_HG_Color & 0xffffffff)).replaceAll(" ", "0")},
                        {"GUI_HG_ColorBorder", String.format("%8s",Integer.toHexString(GUI_HG_ColorBorder & 0xffffffff)).replaceAll(" ", "0")},
                        {"GUI_BNT_Color", String.format("%8s",Integer.toHexString(GUI_BNT_Color & 0xffffffff)).replaceAll(" ", "0")},
                        {"GUI_BNT_ColorBorder", String.format("%8s",Integer.toHexString(GUI_BNT_ColorBorder & 0xffffffff)).replaceAll(" ", "0")},
                        
                        {"HUD_HG_Color", String.format("%8s",Integer.toHexString(HUD_HG_Color & 0xffffffff)).replaceAll(" ", "0")},
                        {"HUD_HG_ColorBorder", String.format("%8s",Integer.toHexString(HUD_HG_ColorBorder & 0xffffffff)).replaceAll(" ", "0")},
                        {"HUD_FREE_Color", String.format("%8s",Integer.toHexString(HUD_FREE_Color & 0xffffffff)).replaceAll(" ", "0")},
                        {"HUD_FREE_ColorBorder", String.format("%8s",Integer.toHexString(HUD_FREE_ColorBorder & 0xffffffff)).replaceAll(" ", "0")},
                        {"HUD_FULL_Color", String.format("%8s",Integer.toHexString(HUD_FULL_Color & 0xffffffff)).replaceAll(" ", "0")},
                        {"HUD_FULL_ColorBorder", String.format("%8s",Integer.toHexString(HUD_FULL_ColorBorder & 0xffffffff)).replaceAll(" ", "0")},

                        {"GUI_TEXT_Color_distance_high", String.format("%8s",Integer.toHexString(GUI_TEXT_Color_distance_high & 0xffffffff)).replaceAll(" ", "0")},
                        {"GUI_TEXT_Color_distance_low", String.format("%8s",Integer.toHexString(GUI_TEXT_Color_distance_low & 0xffffffff)).replaceAll(" ", "0")},
                        {"GUI_TEXT_Color_distance_ok", String.format("%8s",Integer.toHexString(GUI_TEXT_Color_distance_ok & 0xffffffff)).replaceAll(" ", "0")},
                            
                        {"GUI_TEXT_Color_True", String.format("%8s",Integer.toHexString(GUI_TEXT_Color_true & 0xffffffff)).replaceAll(" ", "0")},
                        {"GUI_TEXT_Color_False", String.format("%8s",Integer.toHexString(GUI_TEXT_Color_false & 0xffffffff)).replaceAll(" ", "0")},
                        {"GUI_TEXT_Color_Hot", String.format("%8s",Integer.toHexString(GUI_TEXT_Color_hot & 0xffffffff)).replaceAll(" ", "0")},
                        {"GUI_TEXT_Color_act", String.format("%8s",Integer.toHexString(GUI_TEXT_Color_act & 0xffffffff)).replaceAll(" ", "0")},
                        {"GUI_TEXT_Color_follow", String.format("%8s",Integer.toHexString(GUI_TEXT_Color_follow & 0xffffffff)).replaceAll(" ", "0")},
                        {"HUD_NPC_Size", String.valueOf(HUD_NPC_Size)},
                        {"POS_NPC_X", String.valueOf(POS_NPC_X)},
                        {"POS_NPC_Y", String.valueOf(POS_NPC_Y)},
                        {"POS_NPC_Y_GM", String.valueOf(POS_NPC_Y_GM)},
                        {"POS_INFO_X", String.valueOf(POS_INFO_X)},
                        {"POS_INFO_Y", String.valueOf(POS_INFO_Y)},
                        {"POS_INFO_Y_GM", String.valueOf(POS_INFO_Y_GM)},
                        {"POS_SCREEN_X", String.valueOf(POS_SCREEN_X)},
                        {"POS_SCREEN_Y", String.valueOf(POS_SCREEN_Y)},
                        {"POS_NPC_PIVOT", POS_NPC_PIVOT},
                        {"POS_INFO_PIVOT", POS_INFO_PIVOT},
                        {"POS_SCREEN_PIVOT", POS_SCREEN_PIVOT},

                        {"HUD_Border", String.valueOf(HUD_Border)},
                        {"GUI_Border", String.valueOf(GUI_Border)}
                    };
                    hudConfig = new GetConfigDaten("Hud", hudConfigArray, this, debug,conf);
                    GUI_HG_Color = (int) Long.parseLong(hudConfig.getValue("GUI_HG_Color"), 16); //Integer.parseInt(hudConfig.getValue("GUI_HG_Color"));
                    GUI_HG_ColorBorder = (int) Long.parseLong(hudConfig.getValue("GUI_HG_ColorBorder"), 16);
                    GUI_BNT_Color = (int) Long.parseLong(hudConfig.getValue("GUI_BNT_Color"), 16);
                    GUI_BNT_ColorBorder = (int) Long.parseLong(hudConfig.getValue("GUI_BNT_ColorBorder"), 16);

                    GUI_TEXT_Color_distance_high = (int) Long.parseLong(hudConfig.getValue("GUI_TEXT_Color_distance_high"), 16);                    
                    GUI_TEXT_Color_distance_low = (int) Long.parseLong(hudConfig.getValue("GUI_TEXT_Color_distance_low"), 16);
                    GUI_TEXT_Color_distance_ok = (int) Long.parseLong(hudConfig.getValue("GUI_TEXT_Color_distance_ok"), 16);

                    GUI_TEXT_Color_true = (int) Long.parseLong(hudConfig.getValue("GUI_TEXT_Color_True"), 16); //
                    GUI_TEXT_Color_false = (int) Long.parseLong(hudConfig.getValue("GUI_TEXT_Color_False"), 16); //Integer.parseInt(hudConfig.getValue("GUI_TEXT_Color_False"));
                    GUI_TEXT_Color_hot = (int) Long.parseLong(hudConfig.getValue("GUI_TEXT_Color_Hot"), 16); //Integer.parseInt(hudConfig.getValue("GUI_TEXT_Color_Hot"));
                    GUI_TEXT_Color_act = (int) Long.parseLong(hudConfig.getValue("GUI_TEXT_Color_act"), 16);
                    GUI_TEXT_Color_follow = (int) Long.parseLong(hudConfig.getValue("GUI_TEXT_Color_follow"), 16);
                    HUD_HG_Color = (int) Long.parseLong(hudConfig.getValue("HUD_HG_Color"), 16);
                    HUD_HG_ColorBorder = (int) Long.parseLong(hudConfig.getValue("HUD_HG_ColorBorder"), 16);
                    HUD_FREE_Color = (int) Long.parseLong(hudConfig.getValue("HUD_FREE_Color"), 16);
                    HUD_FREE_ColorBorder = (int) Long.parseLong(hudConfig.getValue("HUD_FREE_ColorBorder"), 16);
                    HUD_FULL_Color = (int) Long.parseLong(hudConfig.getValue("HUD_FULL_Color"), 16);
                    HUD_FULL_ColorBorder = (int) Long.parseLong(hudConfig.getValue("HUD_FULL_ColorBorder"), 16);
                    GUI_Border = Integer.parseInt(hudConfig.getValue("GUI_Border"));
                    HUD_Border = Integer.parseInt(hudConfig.getValue("HUD_Border"));
                    HUD_NPC_Size = Float.parseFloat(hudConfig.getValue("HUD_NPC_Size").replace(",", "."));

                    POS_NPC_X = Float.parseFloat(hudConfig.getValue("POS_NPC_X").replace(",", "."));
                    POS_NPC_Y = Float.parseFloat(hudConfig.getValue("POS_NPC_Y").replace(",", "."));
                    POS_NPC_Y_GM = Float.parseFloat(hudConfig.getValue("POS_NPC_Y_GM").replace(",", "."));
                    POS_INFO_X = Float.parseFloat(hudConfig.getValue("POS_INFO_X").replace(",", "."));
                    POS_INFO_Y = Float.parseFloat(hudConfig.getValue("POS_INFO_Y").replace(",", "."));
                    POS_INFO_Y_GM = Float.parseFloat(hudConfig.getValue("POS_INFO_Y_GM").replace(",", "."));
                    POS_SCREEN_X = Float.parseFloat(hudConfig.getValue("POS_SCREEN_X").replace(",", "."));
                    POS_SCREEN_Y = Float.parseFloat(hudConfig.getValue("POS_SCREEN_Y").replace(",", "."));
                    POS_NPC_PIVOT = hudConfig.getValue("POS_NPC_PIVOT");
                    POS_INFO_PIVOT = hudConfig.getValue("POS_INFO_PIVOT");
                    POS_SCREEN_PIVOT = hudConfig.getValue("POS_SCREEN_PIVOT");
                    
                    //... onEnable stuff
                    chatDTF = DateTimeFormatter.ofPattern("HH:mm:ss");
                    registerEventListener(this);
                    registerEventListener(new NPCsCommand(this));
                    registerEventListener(new NPCsRayCast(this));
                    registerEventListener(new NPCsKEYs(this));
                    ////### SQL Stuff
                    //String MySqlDatabase = sysConfig.getValue("MySqlDatabase");
                    //String MySqlPrefix = sysConfig.getValue("MySqlPrefix");
                    //String MySqlIP = sysConfig.getValue("MySqlIP");
                    //String MySqlUser = sysConfig.getValue("MySqlUser");
                    //String MySqlPW = sysConfig.getValue("MySqlPW");
                    //int MySqlPort; try{ MySqlPort = Integer.parseInt(sysConfig.getValue("MySqlPort")); }catch(NumberFormatException e){ MySqlPort = 0; }
                    //dbDaten = new CRT.classMySqlLockIn(MySqlPrefix, MySqlDatabase, MySqlIP, MySqlPort, MySqlUser, MySqlPW);
                    ////sSQLite = getPath() + "/database/" + getDescription("name") + "-" + world.getName() + ".db";
                    //
                    //if(debug==0){System.out.println("[" + plugin.getDescription("name") + "] "+"Datenbank Auswahl ");}
                    //if(debug>1){System.out.println("[" + plugin.getDescription("name") + "] "+"Datenbank Auswahl "+"MySqlDatabase["+MySqlDatabase+"] "+"MySqlIP["+MySqlIP+"] "+"MySqlPort["+MySqlPort+"] "+"MySqlUser["+MySqlUser+"] "+"MySqlPW[\"+MySqlPW+\"] ");}
                    ////if (!MySqlDatabase.equals("")&&!MySqlIP.equals("")&&MySqlPort>0&&!MySqlUser.equals("")&&!MySqlPW.equals("")){
                    //if (!((CRT.classMySqlLockIn)dbDaten).Datenbank.equals("")&&!((CRT.classMySqlLockIn)dbDaten).IP.equals("")&&((CRT.classMySqlLockIn)dbDaten).Port>0&&!((CRT.classMySqlLockIn)dbDaten).User.equals("")&&!((CRT.classMySqlLockIn)dbDaten).PW.equals("")){
                    //    SQL = CRT.getDatabase(plugin,dbDaten,debug);
                    //    //sqlSimi = "`";
                    //}else{
                    //    dbDaten = getPath() + "/database/" + getDescription("name") + "-" + world.getName() + ".db";
                    //    SQL = CRT.getDatabase(plugin,dbDaten,debug);
                    //    //sqlSimi = "'";
                    //}
                    //if(debug>0){System.out.println("[" + plugin.getDescription("name") + "] "+"Verbindung zur "+SQL.getType().name()+" Datenbank wurde hergestellt! ");}
                    //npcBD.iniDB();
                    //if (SQL!=null) SQL.close();
                
                
                //}else{
                //    //RW GUI Version zu klein
                //    System.err.println("[" + plugin.getDescription("name") + "-ERR] Die Version vom Plugin 'RWGui("+sprachApiPlugin.getDescription("version")+")' ist zu klein! Bitte Aktualisieren auf Version("+requireSprachApiVersion+")!");
                //    registerEventListener(new NPCsErrorRwGui(this));
                //}
            }else{
                //Sprach API Version zu klein
                System.err.println("[" + plugin.getDescription("name") + "-ERR] Die Version vom Plugin 'SprachAPI("+sprachApiPlugin.getDescription("version")+")' ist zu klein! Bitte Aktualisieren auf Version("+requireSprachApiVersion+")!");
                registerEventListener(new NPCsErrorSprachAPI(this));
            }
        }else{
            plugin.getAllPlugins().forEach((t) -> {
                if(debug>0){System.out.println("[" + plugin.getDescription("name") + "] "+"Plugin: " + t.getName() );}
            });
            if(debug>0){System.out.println("[" + plugin.getDescription("name") + "] "+"Plugin-_-: " + plugin.getPluginByName("SprachAPI") +(plugin.getPluginByName("SprachAPI")!= null) );}
            if(plugin.getPluginByName("SprachAPI") == null){
                //keine Sprach API vorhanden
                System.err.println("[" + plugin.getDescription("name") + "-ERR] Das Plugin 'SprachAPI' ist nicht installiert! Bitte installieren!");
                registerEventListener(new NPCsErrorSprachAPI(this));
            }
            //if(plugin.getPluginByName("com.vms.RWGUI") == null ){
            //    //kein RwGui vorhanden
            //    System.err.println("[" + plugin.getDescription("name") + "-ERR] Das Plugin 'RWGui' ist nicht installiert! Bitte installieren!");
            //    registerEventListener(new NPCsErrorRwGui(this));
            //}
        }
    }

    @Override
    public void onDisable() {
//        server.getAllPlayers().forEach((player) -> {
//            if (player!=null)
//                player.kick(sprachApiDaten.getText(player, "MSG_reloadplugins"));
//        });
        unregisterEventListener(this);
        unregisterEventListener(new NPCsCommand(this));
        unregisterEventListener(new NPCsRayCast(this));
        unregisterEventListener(new NPCsKEYs(this));
        System.out.println("[" + plugin.getDescription("name") + "] Disabled");
    }

    @EventMethod
    public void onPlayerSpawn(PlayerSpawnEvent event){
        Player player = event.getPlayer();
        player.registerKeys(CRT.getKeyInputValue(KEY_follow_Aktion)/*,CRT.getKeyInputValue(KEY_interakt_Aktion)*/,CRT.getKeyInputValue(KEY_cancle_Aktion));
        if (!KEY_toggle_Follow.equals("")){
            player.registerKeys(CRT.getKeyInputValue(KEY_toggle_Follow));
        }
        player.setListenForKeyInput(true);
        
        player.setAttribute(plA.isSPAWN,true);
        
        //### MSG/LABEL
        GuiLabel label = new GuiLabel(POS_SCREEN_X, POS_SCREEN_Y, true);
	label.setText(String.format(sprachApiDaten.getText(player, "MSG_load"), plugin.getDescription("name") ));
	label.setPivot(PivotPosition.valueOf(POS_SCREEN_PIVOT));
	label.setFont(Font.DefaultMono_Bold);
	label.setFontSize(24);
        label.setColor((int)GUI_BNT_Color);
        player.setAttribute(plA.labelNpcScreen,label);
        player.addGuiElement((GuiLabel) player.getAttribute(plA.labelNpcScreen));
        
        final CRT.ClassLambadHelper lambTimerLabelInfo = new CRT.ClassLambadHelper();
        lambTimerLabelInfo.obj = player;
        Timer timerLabelInfo = new Timer(60f, 0f, -1, () -> {  //lambda expression
            ((GuiLabel) ((Player) lambTimerLabelInfo.obj).getAttribute(plA.labelNpcScreen)).setVisible(false);
            ((Timer)player.getAttribute(plA.timerLabelScreen)).pause();
	});
        player.setAttribute(plA.timerLabelScreen,timerLabelInfo);
        if (TIMER_WelkomMSG>0){
            ((Timer)player.getAttribute(plA.timerLabelScreen)).setInterval(TIMER_WelkomMSG);
            ((Timer)player.getAttribute(plA.timerLabelScreen)).start();
        }else{
            ((GuiLabel) player.getAttribute(plA.labelNpcScreen)).setVisible(false);
        }
        
        
        label = new GuiLabel(POS_INFO_X, POS_INFO_Y-(player.isCreativeModeEnabled()?POS_INFO_Y_GM:0), true);
        if(plugin.getDebug()>1){System.out.println("[" + plugin.getDescription("name") + "] "+"Spawn " + sprachApiDaten.getText(player, "MSG_load") );}
        if(plugin.getDebug()>1){System.out.println("[" + plugin.getDescription("name") + "] "+"Spawn " + plugin.getDescription("name") );}
	label.setText("INFO");
	label.setPivot(PivotPosition.valueOf(POS_INFO_PIVOT));
	label.setFont(Font.DefaultMono_Bold);
	label.setFontSize(20);
        label.setColor((int)GUI_BNT_Color);
        player.setAttribute(plA.labelNpcInfo,label);
        player.addGuiElement((GuiLabel) player.getAttribute(plA.labelNpcInfo));
        ((GuiLabel) ((Player) lambTimerLabelInfo.obj).getAttribute(plA.labelNpcInfo)).setVisible(false);

        timerLabelInfo = new Timer(60f, 0f, -1, () -> {  //lambda expression
            ((GuiLabel) ((Player) lambTimerLabelInfo.obj).getAttribute(plA.labelNpcInfo)).setText(" "+plugin.getDescription("name")+"-"+plugin.sprachApiDaten.getText(player, "npc_Info")+": ");
            ((Timer)player.getAttribute(plA.timerLabelInfo)).pause();
	});
        player.setAttribute(plA.timerLabelInfo,timerLabelInfo);
        
        //### Fogen anzahl.
        float panelNPCx1=HUD_NPC_Size,panelNPCy1=HUD_NPC_Size*PHI;
        GuiPanel npcPanel = new GuiPanel(POS_NPC_X, POS_NPC_Y-(player.isCreativeModeEnabled()?POS_NPC_Y_GM:0), true, ((panelNPCx1+0.0f)*(maxNPCs+1)), panelNPCy1*1.5f, true);
        npcPanel.setColor(HUD_HG_Color);
        npcPanel.setBorderColor(HUD_HG_ColorBorder);
        npcPanel.setBorderThickness(GUI_Border, false);
        npcPanel.setPivot(PivotPosition.valueOf(POS_NPC_PIVOT));
        player.addGuiElement(npcPanel);
        player.setAttribute(plA.panelNPCAnzahl,npcPanel);
        ((GuiPanel) player.getAttribute(plugin.plA.panelNPCAnzahl)).setVisible(false);
        
        player.setAttribute(plA.panelNPCListe,new ArrayList<>());

        float panelNPCx2=1.0f/(maxNPCs+1),panelNPCy2=0.7f;
        if(plugin.getDebug()>0){System.out.println("[" + plugin.getDescription("name") + "] "+"Pannel: "+"maxNPCs[" + maxNPCs + "] "  );}
        for (int n=0;n<maxNPCs;n++){
            if(plugin.getDebug()>1){System.out.println("[" + plugin.getDescription("name") + "] "+"Pannel: "+"n[" + n + "] "  );}
            GuiPanel npcPanelN = new GuiPanel((panelNPCx2/(maxNPCs+1))+((panelNPCx2+(panelNPCx2/(maxNPCs+1)))*(n)), 0.54f, true, panelNPCx2, panelNPCy2, true);
            npcPanelN.setColor(HUD_FREE_Color);
            npcPanelN.setBorderColor(HUD_FREE_ColorBorder);
            npcPanelN.setBorderThickness(HUD_Border, false);
            npcPanelN.setPivot(PivotPosition.CenterLeft);
            npcPanelN.setClickable(true);
            
            player.addGuiElement(npcPanelN);
            ((GuiPanel) player.getAttribute(plugin.plA.panelNPCAnzahl)).addChild(npcPanelN);
            
            ((ArrayList) player.getAttribute(plugin.plA.panelNPCListe)).add(npcPanelN);
        }
        
        //### Follow
        player.setAttribute(plA.npcFollowGui,null);
        player.setAttribute(plA.npcMainGui,null);
        player.setAttribute(plA.followListe,new ArrayList());
        player.setAttribute(plA.blockPos,new Vector3i(player.getBlockPosition()));
        player.setAttribute(plugin.plA.timerKey,System.currentTimeMillis());
        
        //### NPC-RUN
        final CRT.ClassLambadHelper lambTimerNPC = new CRT.ClassLambadHelper();
        lambTimerNPC.obj = player;
        lambTimerNPC.n=0;
        Timer timerNPC = new Timer(0.75f, 0f, -1, () -> {  //lambda expression
            if (((ArrayList)player.getAttribute(plugin.plA.followListe))!=null)
            if(((ArrayList)player.getAttribute(plugin.plA.followListe)).size()>0){
                lambTimerNPC.n=0;
                ((ArrayList)player.getAttribute(plugin.plA.followListe)).forEach(npc->{
                    //Distance
                    float distance = ((Npc)npc).getPosition().distance((Vector3f)followListePos.get((Npc)npc));
                    if(plugin.getDebug()>2){System.out.println("[" + plugin.getDescription("name") + "] "+"Npc follow "+((Player)lambTimerNPC.obj).getName()+" Liste: " + ((Npc)npc).getGlobalID() +" "+((Vector3f)followListePos.get((Npc)npc)) +" "+distance );}
                    if (distance>followBlockNpcRun){
                        lambTimerNPC.n=1;
                        if(!((Npc)npc).isAlerted()) 
                            ((Npc)npc).setAlerted(true);
                    }else{
                        //if(((Npc)npc).isAlerted()) 
                        ((Npc)npc).setAlerted(false);
                        if(plugin.followListeIsLocked.get(((Npc)npc))!=null){
                            if(distance<=followBlockNpcRun-0.50f){
                                ((Npc)npc).setLocked(true);
                                plugin.followListeIsLocked.remove(((Npc)npc));
                            }else{
                                lambTimerNPC.n=1;
                            }
                        }
                    }
                });
            }
            if(lambTimerNPC.n==0){
                ((Timer)((Player)lambTimerNPC.obj).getAttribute(plA.timerNPC)).pause();
            }
	});
        player.setAttribute(plA.timerNPC,timerNPC);
        player.setAttribute(plA.timerWaitRaycast,String.format("%013d",System.currentTimeMillis()));
        
    }

    @EventMethod
    public void onDisconnect(PlayerDisconnectEvent event){
        Player player = event.getPlayer();
        if (player!=null){
            ((Timer)player.getAttribute(plA.timerLabelScreen)).kill();
            ((Timer)player.getAttribute(plA.timerLabelInfo)).kill();
            ((Timer)player.getAttribute(plA.timerNPC)).kill();
            ((ArrayList)player.getAttribute(plugin.plA.followListe)).forEach((key) -> {followListe.remove(key); followListePos.remove(key); followListeIsLocked.remove(key);});
        }
    }    
    
    void followToggle(Player player) {
        if ( plugin.Access_Groups.indexOf(player.getPermissionGroup().toLowerCase())>=0 || (plugin.Access_Admins&&player.isAdmin())){
            if (player.getAttribute(plugin.plA.npcFollow) != null){
                player.setAttribute(plugin.plA.npcFollow,!((boolean)player.getAttribute(plugin.plA.npcFollow)));
            }else{
                player.setAttribute(plugin.plA.npcFollow,true);
            }
        }
    }

    void followShow(Player player) {
        if (player.getAttribute(plugin.plA.npcFollow)!=null) 
            if ((boolean)player.getAttribute(plugin.plA.npcFollow)){
                String infoStrg = "";
                infoStrg += " "+plugin.sprachApiDaten.getText(player, "npc_Info")+": ";
                if(debug>3){System.out.println("[" + plugin.getDescription("name") + "] "+"Npc found: " + infoStrg );}

                ((GuiLabel) player.getAttribute(plugin.plA.labelNpcInfo)).setText(infoStrg);
                ((GuiLabel) player.getAttribute(plugin.plA.labelNpcInfo)).setVisible(true);
                //((GuiLabel) player.getAttribute(plugin.plA.labelNpcScreen)).setVisible(true);
                ((GuiPanel) player.getAttribute(plugin.plA.panelNPCAnzahl)).setVisible(true);
            }else{
                ((GuiLabel) player.getAttribute(plugin.plA.labelNpcInfo)).setVisible(false);
                ((GuiLabel) player.getAttribute(plugin.plA.labelNpcScreen)).setVisible(false);
                ((GuiPanel) player.getAttribute(plugin.plA.panelNPCAnzahl)).setVisible(false);
            }
    }

    void npcAnzahl(Player player) {
        for (int n=0;n<maxNPCs;n++){
            if(plugin.getDebug()>1){System.out.println("[" + plugin.getDescription("name") + "] "+"Pannel: "+"n[" + n +"|"+(((ArrayList) player.getAttribute(plugin.plA.followListe)).size()-1) + "] " + ((GuiPanel)((ArrayList) player.getAttribute(plugin.plA.panelNPCListe)).get(n)).getID() +" " );}

            if (((ArrayList) player.getAttribute(plugin.plA.followListe)).size()>n){    
                ((GuiPanel)((ArrayList) player.getAttribute(plugin.plA.panelNPCListe)).get(n)).setColor(HUD_FULL_Color);
                ((GuiPanel)((ArrayList) player.getAttribute(plugin.plA.panelNPCListe)).get(n)).setBorderColor(HUD_FULL_ColorBorder);
            }else{
                ((GuiPanel)((ArrayList) player.getAttribute(plugin.plA.panelNPCListe)).get(n)).setColor(HUD_FREE_Color);
                ((GuiPanel)((ArrayList) player.getAttribute(plugin.plA.panelNPCListe)).get(n)).setBorderColor(HUD_FREE_ColorBorder);
            }
        }
    }

    static class helferClass {
        static HashMap<Npc, Player> followListe;
        public helferClass() {
            followListe = new HashMap<>();
        }
    }

    class PlA {
        final String isSPAWN,labelNpcInfo,labelNpcScreen,timerLabelInfo,timerLabelScreen,npcFollow,npcFollowGui,npcMainGui,followListe,blockPos,
                timerKey,timerNPC,panelNPCAnzahl,panelNPCListe,timerMenüDestroy,timerWaitRaycast;
        public PlA() {
            isSPAWN = getDescription("name") + "-" + "isSPAWN";
            labelNpcInfo = getDescription("name") + "-" + "npcInfo";
            labelNpcScreen = getDescription("name") + "-" + "npcScreen";
            timerLabelInfo = getDescription("name") + "-" + "timerLabelInfo";
            timerLabelScreen = getDescription("name") + "-" + "timerLabelScreen";
            npcFollow = getDescription("name") + "-" + "npcFollow";
            npcFollowGui = getDescription("name") + "-" + "npcFollowGui";
            npcMainGui = getDescription("name") + "-" + "npcMainGui";
            followListe = getDescription("name") + "-" + "followListe";
            blockPos = getDescription("name") + "-" + "blockPos";
            timerKey = getDescription("name") + "-" + "timerKey";
            timerNPC = getDescription("name") + "-" + "timerNPC";
            panelNPCAnzahl = getDescription("name") + "-" + "panelNPCAnzahl";
            panelNPCListe = getDescription("name") + "-" + "panelNPCListe";
            timerMenüDestroy = getDescription("name") + "-" + "timerMenüDestroy";
            timerWaitRaycast = getDescription("name") + "-" + "timerWaitRaycast";
        }
    }
    
    @EventMethod
    public void onGuiElementClick(PlayerGuiElementClickEvent event) {
        Player player = event.getPlayer();
        GuiElement element = event.getGuiElement();
        for (int n=0;n<maxNPCs;n++){
            if(plugin.getDebug()>1){System.out.println("[" + plugin.getDescription("name") + "] "+"Pannel: "+"n[" + n +"|"+(((ArrayList) player.getAttribute(plugin.plA.followListe)).size()-1) + "] " + ((GuiPanel)((ArrayList) player.getAttribute(plugin.plA.panelNPCListe)).get(n)).getID() +" "+element.getID() );}
            if (((GuiPanel)((ArrayList) player.getAttribute(plugin.plA.panelNPCListe)).get(n))!=null){
                if ((GuiPanel)((ArrayList) player.getAttribute(plugin.plA.panelNPCListe)).get(n)==element){
                    if(plugin.getDebug()>1){System.out.println("[" + plugin.getDescription("name") + "] "+"Pannel-Finde: "+"n[" + n +"|"+(((ArrayList) player.getAttribute(plugin.plA.followListe)).size()-1) + "] " + ((GuiPanel)((ArrayList) player.getAttribute(plugin.plA.panelNPCListe)).get(n)).getID() +" " );}
                    if (player.getAttribute(plugin.plA.npcMainGui)!=null){
                        ((GuiPanel)player.getAttribute(plugin.plA.npcMainGui)).destroy();
                    }
                    if (n<((ArrayList) player.getAttribute(plugin.plA.followListe)).size()){
                        Npc npc = (Npc)((ArrayList)player.getAttribute(plugin.plA.followListe)).get(n);
                        String titel =" "+plugin.getDescription("name") +" ["+plugin.sprachApiDaten.getText(player, npc.getDefinition().getName())+"-"+npc.getGlobalID()+"] ";        
                        player.setAttribute(plugin.plA.npcMainGui,new MenüMain(titel, 0.5f, 0.5f, true, plugin, player, npc));                        
                    }
                }
            }
        }        
    }
    
}
