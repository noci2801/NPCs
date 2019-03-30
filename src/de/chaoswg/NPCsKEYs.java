/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.chaoswg;

import static de.chaoswg.NPCs.plugin;
import java.time.LocalTime;
import java.util.ArrayList;
import net.risingworld.api.Timer;
import net.risingworld.api.events.EventMethod;
import net.risingworld.api.events.Listener;
import net.risingworld.api.events.player.PlayerKeyEvent;
import net.risingworld.api.gui.GuiLabel;
import net.risingworld.api.objects.Npc;
import net.risingworld.api.objects.Player;
import net.risingworld.api.utils.CollisionType;
import net.risingworld.api.utils.RayCastResult;

/**
 *
 * @author Schmull
 */
public class NPCsKEYs implements Listener {

    private final NPCs plugin;

    public NPCsKEYs(NPCs plugin) {
        this.plugin = plugin;
    }
    
    @EventMethod
    public void onPlayerKeyEvent(PlayerKeyEvent event) {
        Player player = event.getPlayer();
        int key = event.getKeyCode();
        if(plugin.getDebug()>3){System.out.println("[" + plugin.getDescription("name") + "] "+"KeyEvent " + ((String)player.getPermissionValue("input_interaction")) );}
        final int key_follow            = CRT.getKeyInputValue((plugin.KEY_follow_Aktion.toUpperCase().equals("AKTION")  ?((String)player.getPermissionValue("input_interaction")):plugin.KEY_follow_Aktion));//FixMe###player.getOption
        //final int key_interakt          = CRT.getKeyInputValue((plugin.KEY_interakt_Aktion.toUpperCase().equals("AKTION")?((String)player.getPermissionValue("input_interaction")):plugin.KEY_interakt_Aktion));//FixMe###player.getOption
        final int key_toggle_follow   = CRT.getKeyInputValue(plugin.KEY_toggle_Follow);
        final int key_cancle            = CRT.getKeyInputValue(plugin.KEY_cancle_Aktion);
        if(plugin.getDebug()>2){System.out.println("[" + plugin.getDescription("name") + "] "+"PlayerKeyEvent FollowKey[" + key_toggle_follow +"] " + "Key["+key+"] ");}
        boolean press = event.isPressed();
        long tNow = System.currentTimeMillis();
        if (player.getAttribute(plugin.plA.isSPAWN)!=null) 
            if((boolean)player.getAttribute(plugin.plA.isSPAWN))
        if (player.getAttribute(plugin.plA.npcFollow)!=null) if((boolean)player.getAttribute(plugin.plA.npcFollow)){
            if ( key == key_follow && press && player.getAttribute(plugin.plA.npcMainGui)==null ){
                player.setAttribute(plugin.plA.timerKey,tNow+(250));
            }else if(key == key_follow &&((long)player.getAttribute(plugin.plA.timerKey))<=tNow &&  !press && player.getAttribute(plugin.plA.npcMainGui)==null){
                if(plugin.getDebug()>0){System.out.println("[" + plugin.getDescription("name") + "] "+"Npc KEY follow hold: " + "" );}
                //ToDo#-# Formations Menü/Optionen
            }else if ( (key == key_follow ) && !press && player.getAttribute(plugin.plA.npcMainGui)==null && (plugin.Access_Groups.indexOf(player.getPermissionGroup().toLowerCase())>=0 || (player.isAdmin()&&plugin.Access_Admins))){
                player.setAttribute(plugin.plA.timerKey,tNow-1);
                final CRT.ClassLambadHelper l = new CRT.ClassLambadHelper();
                l.n = key;
                player.raycast(CollisionType.NPCS, (RayCastResult result) -> {
                    if(result != null){
                        Object obj = result.getCollisionObject();
                        if(obj != null && obj instanceof Npc){
                            Npc npc = (Npc) obj;
                            float distance = player.getPosition().distance(npc.getPosition());
                            String infoStrg = "";
                            infoStrg += plugin.chatDTF.format(LocalTime.now())+" ";
                            infoStrg += "Dist: "+distance;
                            if(plugin.getDebug()>3){System.out.println("[" + plugin.getDescription("name") + "] "+"Npc KEY found: " + infoStrg );}

                            if (l.n == key_follow && (distance <= plugin.KEY_follow_Radius || plugin.KEY_follow_Radius < 0 ) && distance >= plugin.KEY_interakt_Radius){
                                if(plugin.getDebug()>2){System.out.println("[" + plugin.getDescription("name") + "] "+"Npc KEY Follow: " + infoStrg );}
                                if(plugin.getDebug()>2){System.out.println("[" + plugin.getDescription("name") + "] "+"Npc KEY Follow: " + "KEY["+key_cancle+"] " );}
                                String titel =" "+plugin.getDescription("name") +" ["+plugin.sprachApiDaten.getText(player, npc.getDefinition().getName())+"-"+npc.getGlobalID()+"] ";
                                //### Nummer der Folge Liste
                                String infStrIndex = ""+((ArrayList)player.getAttribute(plugin.plA.followListe)).indexOf(npc);
                                String infStrCound = "|"+((ArrayList)player.getAttribute(plugin.plA.followListe)).size();
                                if(plugin.getDebug()>0){System.out.println("[" + plugin.getDescription("name") + "] "+"Npc KEY Follow: " + "followCound["+infStrCound+"] "+ "followIndex["+infStrIndex+"] " );}
                                //ToDo### +ABM Rechte
                                if ( (plugin.followListe.get(npc)==null /*&& ABM.player==null*/) || (player.equals(plugin.followListe.get(npc))/*|| player.equals(ABM.player)*/) || plugin.AllowOthers || (player.isAdmin()&&plugin.Access_Admins)){
                                    //### Menü Öffnen
                                    player.setAttribute(plugin.plA.npcMainGui,new MenüMain(titel, 0.5f, 0.5f, true, plugin, player, npc));
                                }
                                
                            }else if (l.n == key_follow && distance > plugin.KEY_follow_Radius && distance >= plugin.KEY_interakt_Radius){
                                if(plugin.getDebug()>2){System.out.println("[" + plugin.getDescription("name") + "] "+"Npc KEY no Follow Radius: " + infoStrg );}
                                //### NPC zu weit weck
                                player.setAttribute(plugin.plA.timerWaitRaycast,String.format("%013d", ((long)(System.currentTimeMillis()+((long)plugin.TIMER_Info*100))) ));
                                ((GuiLabel) player.getAttribute(plugin.plA.labelNpcScreen)).setText(" "+plugin.sprachApiDaten.getText(player, "KEY_noRAD")+" ");
                                ((GuiLabel) player.getAttribute(plugin.plA.labelNpcScreen)).setVisible(true);
                                
                                ((Timer)player.getAttribute(plugin.plA.timerLabelScreen)).setInterval(plugin.TIMER_Screen);
                                if (((Timer)player.getAttribute(plugin.plA.timerLabelScreen)).isActive()){
                                    ((Timer)player.getAttribute(plugin.plA.timerLabelScreen)).setTick(0.0f);
                                    if(plugin.getDebug()>3){System.out.println("[" + plugin.getDescription("name") + "] "+"Timer Tick: " + ((Timer)player.getAttribute(plugin.plA.timerLabelScreen)).getTick() );}
                                }
                                ((Timer)player.getAttribute(plugin.plA.timerLabelScreen)).start();
                            }else if (l.n == key_follow && distance <= plugin.KEY_interakt_Radius){
                                if(plugin.getDebug()>2){System.out.println("[" + plugin.getDescription("name") + "] "+"Npc KEY Interact Radius: " + infoStrg );}
                                //### NPC zu nahe
                                player.setAttribute(plugin.plA.timerWaitRaycast,String.format("%013d", ((long)(System.currentTimeMillis()+((long)plugin.TIMER_Info*100))) ));
                                ((GuiLabel) player.getAttribute(plugin.plA.labelNpcScreen)).setText(" "+plugin.sprachApiDaten.getText(player, "KEY_loRAD")+" ");
                                ((GuiLabel) player.getAttribute(plugin.plA.labelNpcScreen)).setVisible(true);
                                
                                ((Timer)player.getAttribute(plugin.plA.timerLabelScreen)).setInterval(plugin.TIMER_Info);
                                if (((Timer)player.getAttribute(plugin.plA.timerLabelScreen)).isActive()){
                                    ((Timer)player.getAttribute(plugin.plA.timerLabelScreen)).setTick(0.0f);
                                    if(plugin.getDebug()>3){System.out.println("[" + plugin.getDescription("name") + "] "+"Timer Tick: " + ((Timer)player.getAttribute(plugin.plA.timerLabelScreen)).getTick() );}
                                }
                                ((Timer)player.getAttribute(plugin.plA.timerLabelScreen)).start();
                            }
                        }
                    }else{
                        if(plugin.getDebug()>2){System.out.println("[" + plugin.getDescription("name") + "] "+"Npc KEY not found: "  );}
                        player.setAttribute(plugin.plA.timerWaitRaycast,String.format("%013d", ((long)(System.currentTimeMillis()+((long)plugin.TIMER_Info*100))) ));
                        ((GuiLabel) player.getAttribute(plugin.plA.labelNpcScreen)).setText(plugin.sprachApiDaten.getText(player, "KEY_noNPC"));
                        ((GuiLabel) player.getAttribute(plugin.plA.labelNpcScreen)).setVisible(true);

                        ((Timer)player.getAttribute(plugin.plA.timerLabelScreen)).setInterval(plugin.TIMER_Screen);
                        if (((Timer)player.getAttribute(plugin.plA.timerLabelScreen)).isActive()){
                            ((Timer)player.getAttribute(plugin.plA.timerLabelScreen)).setTick(0.0f);
                            if(plugin.getDebug()>3){System.out.println("[" + plugin.getDescription("name") + "] "+"Timer Tick: " + ((Timer)player.getAttribute(plugin.plA.timerLabelScreen)).getTick() );}
                        }
                        ((Timer)player.getAttribute(plugin.plA.timerLabelScreen)).start();
                    }
                });
            }else if ((key == key_follow && !press && plugin.Access_Groups.indexOf(player.getPermissionGroup().toLowerCase())==-1 && player.getAttribute(plugin.plA.npcMainGui)==null) /*|| (player.isAdmin()&&plugin.Access_Admins)*/){
                if(plugin.getDebug()>0){System.out.println("[" + plugin.getDescription("name") + "] "+"Doch hier das Problem?!! "  );}
                player.setAttribute(plugin.plA.timerWaitRaycast,String.format("%013d", ((long)(System.currentTimeMillis()+((long)plugin.TIMER_Info*100))) ));
                ((GuiLabel) player.getAttribute(plugin.plA.labelNpcScreen)).setText(plugin.sprachApiDaten.getText(player, "npc_access_denied"));
                ((GuiLabel) player.getAttribute(plugin.plA.labelNpcScreen)).setVisible(true);

                ((Timer)player.getAttribute(plugin.plA.timerLabelScreen)).setInterval(plugin.TIMER_Screen);
                if (((Timer)player.getAttribute(plugin.plA.timerLabelScreen)).isActive()){
                    ((Timer)player.getAttribute(plugin.plA.timerLabelScreen)).setTick(0.0f);
                    if(plugin.getDebug()>3){System.out.println("[" + plugin.getDescription("name") + "] "+"Timer Tick: " + ((Timer)player.getAttribute(plugin.plA.timerLabelScreen)).getTick() );}
                }
                ((Timer)player.getAttribute(plugin.plA.timerLabelScreen)).start();
            }
        }
        
        if (key == key_toggle_follow && !press && (plugin.Access_Groups.indexOf(player.getPermissionGroup().toLowerCase())>=0||(plugin.Access_Admins&&player.isAdmin()))){
            plugin.followToggle(player);
            plugin.followShow(player);
        }else if (key == key_toggle_follow && !press && (plugin.Access_Groups.indexOf(player.getPermissionGroup().toLowerCase())==-1 /*|| (player.isAdmin()&&(plugin.Access_Admins&&player.isAdmin()))*/)){
            player.setAttribute(plugin.plA.timerWaitRaycast,String.format("%013d", ((long)(System.currentTimeMillis()+((long)plugin.TIMER_Info*100))) ));
            ((GuiLabel) player.getAttribute(plugin.plA.labelNpcScreen)).setText(plugin.sprachApiDaten.getText(player, "npc_access_denied"));
            ((GuiLabel) player.getAttribute(plugin.plA.labelNpcScreen)).setVisible(true);

            ((Timer)player.getAttribute(plugin.plA.timerLabelScreen)).setInterval(plugin.TIMER_Screen);
            if (((Timer)player.getAttribute(plugin.plA.timerLabelScreen)).isActive()){
                ((Timer)player.getAttribute(plugin.plA.timerLabelScreen)).setTick(0.0f);
                if(plugin.getDebug()>3){System.out.println("[" + plugin.getDescription("name") + "] "+"Timer Tick: " + ((Timer)player.getAttribute(plugin.plA.timerLabelScreen)).getTick() );}
            }
            ((Timer)player.getAttribute(plugin.plA.timerLabelScreen)).start();
        }

    }
}
