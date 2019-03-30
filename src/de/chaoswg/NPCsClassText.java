/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.chaoswg;

import java.util.ArrayList;

/**
 *
 * @author Schmull
 */
public class NPCsClassText extends SprachAPI{

    @Override
    protected void setDatenFunktion(){
        Sprache = new ArrayList();
        Sprache.add("en");
        Sprache.add("de");
        setSprache(Sprache);
        
        Daten = new String[][] { 
            //Name                  en                                  de
            {"npc_Info"             ,"Info"                             ,"Info"},
            {"npc_Behaviour"        ,"Behaviour"                        ,"Verhalten"},
            {"npc_Alerted"          ,"&Alerted"                          ,"&Alarmiert"},
            {"npc_Reaction"         ,"Reaction"                         ,"Reaktion"},
            {"npc_Distance"         ,"Distance"                         ,"Entfernung"},
            
            {"npc_isDead"           ,"dead"                             ,"Tod"},
            {"npc_isInLava"         ,"in Lava"                          ,"in Lava"},
            {"npc_isInWater"        ,"in Water"                         ,"im Wasser"},
            {"npc_isInvincible"     ,"&Invincible"                       ,"&Unbesiegbar"},
            {"npc_isLocked"         ,"&Locked"                           ,"Fi&xiert"},
            {"npc_isSleeping"       ,"Sleeping"                         ,"Schläft"},
            {"npc_isTransient"      ,"Transient"                        ,"Vorübergehend"},
            {"npc_isUnderWater"     ,"under Water"                      ,"unter Wasser"},
            {"npc_name"             ,"name"                             ,"Name"},
            {"npc_health"           ,"Health"                           ,"Gesundheit"},
            {"npc_hunger"           ,"Hunger"                           ,"Hunger"},
            {"npc_thirst"           ,"Thirst"                           ,"Durst"},
            {"npc_age"              ,"Age"                              ,"Alter"},
            {"npc_follow"           ,"&Follow"                          ,"&Folgen"},
            {"npc_dummy"            ,"&Dummy"                           ,"&Dummy"},
            {"npc_access_denied"    ,"access denied"                    ,"Zugriff verweigert"},
            //{"npc_"               ,""                                 ,""},
          
            
            {"KEY_noNPC"            ,"no NPC found."                    ,"kein NPC gefunden."},
            {"KEY_noRAD"            ,"NPC out of reach."                ,"NPC außerhal der Reichweite."},
            {"KEY_loRAD"            ,"NPC is too close."                ,"NPC ist zu nahe."},
            
            //### Spiel Übersetzung
            //Alerted
            {"true"                 ,"YES"                              ,"JA"},
            {"false"                ,"NO"                               ,"NEIN"},
            {"on"                   ,"on"                               ,"an"},
            {"off"                  ,"off"                              ,"aus"},
            //### Behaviour
            {"AGGRESSIVE"           ,"AGGRESSIVE"                       ,"AGGRESSIVE"},
            {"DEFAULT"              ,"DEFAULT"                          ,"STANDARD"},
            {"SHY"                  ,"SHY"                              ,"SCHÜCHTERN"},
            //Reaction
            {"ESCAPE"               ,"ESCAPE"                           ,"FLUCHT"},
            {"ATTACK"               ,"ATTACK"                           ,"ANGRIFF"},
            {"DUMMY"                ,"DUMMY"                            ,"DUMMY"},

            //### Tierliste
            {"goat"         ,"goat"         ,"Ziege"},
            {"goatwhite"    ,"goat white"   ,"Ziege Weiß"},
            {"cow"          ,"cow"          ,"Kuh"},
            {"cowbrown"     ,"cow brown"    ,"Kuh Braun"},
            {"chicken"      ,"chicken"      ,"Huhn"},
            {"rhinoceros"   ,"rhinoceros"   ,"Nashorn"},
            {"elephant"     ,"elephant"     ,"Elefant"},
            {"pig"          ,"pig"          ,"Schwein"},
            {"minipig"      ,"minipig"      ,"Minischwein"},
            {"sheep"        ,"sheep"        ,"Schaf"},
            {"sheepshorn"   ,"sheepshorn"   ,"Geschorenes Schafe"},
            {"bear"         ,"bear"         ,"Braunbär"},
            {"polarbear"    ,"polarbear"    ,"Eisbär"},
            {"bearblack"    ,"bearblack"    ,"Schwarzbär"},
            {"moose"        ,"moose"        ,"Elch"},
            {"rabbit"       ,"rabbit"       ,"Hase"},
            {"fox"          ,"fox"          ,"Fuchs"},
            {"penguin"      ,"penguin"      ,"Pinguin"},
            {"giraffe"      ,"giraffe"      ,"Giraffe"},
            {"tiger"        ,"tiger"        ,"Tiger"},
            {"jaguar"       ,"jaguar"       ,"Jaguar"},
            {"donkey"       ,"donkey"       ,"Esel"},
            {"horse"        ,"horse"        ,"Pferd"},
            {"camel"        ,"camel"        ,"Kamel"},
            {"rat"          ,"rat"          ,"Ratte"},
            {"spider"       ,"spider"       ,"Spinne"},
            {"spiderling"   ,"spiderling"   ,"kleine Spinne"},
            {"deer"         ,"deer"         ,"Reh"},
            {"snake"        ,"snake"        ,"Schlange"},
            {"boar"         ,"boar wild"    ,"Wildschwein"},
            {"shark"        ,"shark"        ,"Hai"},
            {"dummy"        ,"dummy"        ,"dummy"},

            //### Werte
            {"TEXT_cal"     ,"called"       ,"genannt"},
            {"MSG_reloadplugins"     ,"NPCs has been deactivated. Probably the plugins will be restarted. \nYou can reconnect immediately!"       ,"NPCs wurde deaktiviert. Wahrscheinlich werden die Plugins neu gestartet.\nDu kannst dich umgehend wieder verbinden!"},
            {"MSG_load"     ,"%s loaded."       ,"%s geladen."},
            {"MSG_isFollow"     ,"The NPC follows %s."       ,"Der NPC folgt %s."},
            {"MSG_isAlreadyFollow"     ,"The NPC is already following %s."       ,"Der NPC folgt bereits %s."},
            {"MSG_you"     ,"to you"       ,"dir"},
            {"MSG_followMax"     ,"You can not let more than %d, follow you."       ,"Du kannst nicht mehr als %d, dir folgen lassen."},
            
            
            //### Beschreibungen
            {"command_author"       ,"author"                           ,"Autor"},
            {"command_Description"  ,"Description"                      ,"Beschreibung"},
            {"command_help"         ,"If you need help, then '/%s help'","Wenn du hilfe brauchst, dann schreibe '/%s help'"},
            {"continue"             ,"Continue"                         ,"Weiter"},
            {"agree"                ,"Agree"                            ,"Zustimmen"},
            {"disagree"             ,"Disagree"                         ,"nicht Zustimmen"}
        };
        setDaten(Daten);        
        
    }
}
