/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.chaoswg;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;
import net.risingworld.api.Timer;
import net.risingworld.api.events.EventMethod;
import net.risingworld.api.events.Listener;
import net.risingworld.api.events.player.PlayerChangeBlockPositionEvent;
import net.risingworld.api.events.player.PlayerChangePositionEvent;
import net.risingworld.api.events.player.PlayerNpcInteractionEvent;
import net.risingworld.api.gui.GuiLabel;
import net.risingworld.api.objects.Npc;
import net.risingworld.api.objects.Player;
import net.risingworld.api.utils.CollisionType;
import net.risingworld.api.utils.Quaternion;
import net.risingworld.api.utils.RayCastResult;
import net.risingworld.api.utils.Utils;
import net.risingworld.api.utils.Vector3f;
import net.risingworld.api.utils.Vector3i;

/**
 *
 * @author Schmull
 */
public class NPCsRayCast implements Listener {

    private final NPCs plugin;

    public NPCsRayCast(NPCs plugin) {
        this.plugin = plugin;
    }

    @EventMethod
    public void onPlayerChangePosition(PlayerChangePositionEvent event){
        Player player = event.getPlayer();
        if(plugin.getDebug()>2){System.out.println("[" + plugin.getDescription("name") + "] "+"PlayerChangePosition " + "Folow["+player.getAttribute(plugin.plA.npcFollow)+"]" + "Spwan["+player.getAttribute(plugin.plA.isSPAWN)+"] " );}
        if (player.getAttribute(plugin.plA.npcFollow)!=null) if((boolean)player.getAttribute(plugin.plA.npcFollow)){
            if (player.getAttribute(plugin.plA.isSPAWN)!=null) if((boolean)player.getAttribute(plugin.plA.isSPAWN)){
                if(plugin.getDebug()>2){System.out.println("[" + plugin.getDescription("name") + "] "+"Npc found: " + System.currentTimeMillis()+" -|- "+((String) player.getAttribute(plugin.plA.timerWaitRaycast)) );}
                if (System.currentTimeMillis()> Long.parseLong((String) player.getAttribute(plugin.plA.timerWaitRaycast)) ){
                    //perform raycast (collision type: npcs only)
                    player.raycast(CollisionType.NPCS, (RayCastResult result) -> {
                        if(result != null){
                            Object obj = result.getCollisionObject();
                            if(obj != null && obj instanceof Npc){
                                Npc npc = (Npc) obj;

                                float distance = player.getPosition().distance(npc.getPosition());
                                String infoStrg = "";
                                if (((MenüMain) player.getAttribute(plugin.plA.npcMainGui))==null){
                                    infoStrg += " Name: "+(!npc.getName().equals("")?""+npc.getName()+" ":plugin.sprachApiDaten.getText(player, npc.getDefinition().getName())+"-"+npc.getGlobalID())+" "; // Name
                                    //if (((ArrayList)player.getAttribute(plugin.plA.followListe)).indexOf(npc)>=0){//Fix Me### auf getFollow
                                    if (plugin.followListe.get(npc)!=null /*&& ABM.player!=null*/){//ToDo### +ABM Rechte
                                        infoStrg += "\n "+String.format(plugin.sprachApiDaten.getText(player, "MSG_isFollow"), (plugin.followListe.get(npc)==player?"dir":plugin.followListe.get(npc).getName()));
                                    }
                                    if(plugin.getDebug()>3){System.out.println("[" + plugin.getDescription("name") + "] "+"Npc found: " + infoStrg );}
                                    ((GuiLabel) player.getAttribute(plugin.plA.labelNpcScreen)).setText(infoStrg);
                                    ((GuiLabel) player.getAttribute(plugin.plA.labelNpcScreen)).setVisible(true);

                                    ((Timer)player.getAttribute(plugin.plA.timerLabelScreen)).setInterval(plugin.TIMER_Screen);
                                    if (((Timer)player.getAttribute(plugin.plA.timerLabelScreen)).isActive()){
                                        ((Timer)player.getAttribute(plugin.plA.timerLabelScreen)).setTick(0.0f);
                                        if(plugin.getDebug()>3){System.out.println("[" + plugin.getDescription("name") + "] "+"Timer Tick: " + ((Timer)player.getAttribute(plugin.plA.timerLabelScreen)).getTick() );}
                                    }
                                    ((Timer)player.getAttribute(plugin.plA.timerLabelScreen)).start();
                                }
                                infoStrg = "";
                                infoStrg += " "+plugin.getDescription("name")+"-"+plugin.sprachApiDaten.getText(player, "npc_Info")+": ";
                                infoStrg += plugin.sprachApiDaten.getText(player, npc.getDefinition().getName())+"-"+npc.getGlobalID();
                                infoStrg += (!npc.getName().equals("")?",\n "+plugin.sprachApiDaten.getText(player,"TEXT_cal")+" [#80ffff]"+npc.getName()+"[#ffffff].":".\n ");
                                infoStrg += "\n ";
                                infoStrg += "\n "+plugin.sprachApiDaten.getText(player, "npc_Behaviour")+"["+plugin.sprachApiDaten.getText(player, npc.getDefinition().getBehaviour())+"] ";
                                infoStrg += "\n "+plugin.sprachApiDaten.getText(player, "npc_Reaction")+"["+plugin.sprachApiDaten.getText(player, npc.getDefinition().getAttackReaction())+"] ";
                                //infoStrg += "\n "+String.format("%s: %s%.3f[#ffFFff] ",plugin.sprachApiDaten.getText(player, "npc_Distance"),(distance<=plugin.KEY_interakt_Radius?"[#c8ff00]":(distance<=plugin.KEY_follow_Radius||plugin.KEY_follow_Radius<0?"[#ffc800]":"[#ffFFff]")),distance);
                                infoStrg += "\n "+String.format("%s: %s%.3f[#ffFFff] ",plugin.sprachApiDaten.getText(player, "npc_Distance"),(distance<=plugin.KEY_interakt_Radius?"[#"+String.format("%08x", plugin.GUI_TEXT_Color_distance_low & 0xffffffff).substring(0,6)+"]":(distance<=plugin.KEY_follow_Radius||plugin.KEY_follow_Radius<0?"[#"+String.format("%08x", plugin.GUI_TEXT_Color_distance_ok & 0xffffffff).substring(0,6)+"]":"[#"+String.format("%08x", plugin.GUI_TEXT_Color_distance_high & 0xffffffff).substring(0,6)+"]")),distance);
                                infoStrg += "\n ";

                                infoStrg += "\n "+(npc.isAlerted()?"[#"+String.format("%08x", plugin.GUI_TEXT_Color_true & 0xffffffff).substring(0,6)+"]":"[#"+String.format("%08x", plugin.GUI_TEXT_Color_false & 0xffffffff).substring(0,6)+"]")+plugin.sprachApiDaten.getText(player, "npc_Alerted").replace("&", "")+"[#ffFFff] ";
                                infoStrg += "\n "+(npc.isLocked()?"[#"+String.format("%08x", plugin.GUI_TEXT_Color_true & 0xffffffff).substring(0,6)+"]":"[#"+String.format("%08x", plugin.GUI_TEXT_Color_false & 0xffffffff).substring(0,6)+"]")+plugin.sprachApiDaten.getText(player, "npc_isLocked").replace("&", "")+"[#ffFFff] ";
                                infoStrg += "\n "+(npc.isSleeping()?"[#"+String.format("%08x", plugin.GUI_TEXT_Color_true & 0xffffffff).substring(0,6)+"]":"[#"+String.format("%08x", plugin.GUI_TEXT_Color_false & 0xffffffff).substring(0,6)+"]")+plugin.sprachApiDaten.getText(player, "npc_isSleeping")+"[#ffFFff] ";
                                infoStrg += "\n "+(npc.isInvincible()?"[#"+String.format("%08x", plugin.GUI_TEXT_Color_true & 0xffffffff).substring(0,6)+"]":"[#"+String.format("%08x", plugin.GUI_TEXT_Color_false & 0xffffffff).substring(0,6)+"]")+plugin.sprachApiDaten.getText(player, "npc_isInvincible").replace("&", "")+"[#ffFFff] ";
                                if(plugin.getDebug()>3){
                                    infoStrg += "\n "+(npc.isInWater()?"[#"+String.format("%08x", plugin.GUI_TEXT_Color_true & 0xffffffff).substring(0,6)+"]":"[#"+String.format("%08x", plugin.GUI_TEXT_Color_false & 0xffffffff).substring(0,6)+"]")+plugin.sprachApiDaten.getText(player, "npc_isInWater")+"[#ffFFff] ";
                                    infoStrg += "\n "+(npc.isUnderWater()?"[#"+String.format("%08x", plugin.GUI_TEXT_Color_true & 0xffffffff).substring(0,6)+"]":"[#"+String.format("%08x", plugin.GUI_TEXT_Color_false & 0xffffffff).substring(0,6)+"]")+plugin.sprachApiDaten.getText(player, "npc_isUnderWater")+"[#ffFFff] ";
                                    infoStrg += "\n "+(npc.isInLava()?"[#"+String.format("%08x", plugin.GUI_TEXT_Color_true & 0xffffffff).substring(0,6)+"]":"[#"+String.format("%08x", plugin.GUI_TEXT_Color_false & 0xffffffff).substring(0,6)+"]")+plugin.sprachApiDaten.getText(player, "npc_isInLava")+"[#ffFFff] ";
                                    infoStrg += "\n "+(npc.isTransient()?"[#"+String.format("%08x", plugin.GUI_TEXT_Color_true & 0xffffffff).substring(0,6)+"]":"[#"+String.format("%08x", plugin.GUI_TEXT_Color_false & 0xffffffff).substring(0,6)+"]")+plugin.sprachApiDaten.getText(player, "npc_isTransient")+"[#ffFFff] ";
                                    infoStrg += "\n "+(npc.isDead()?"[#"+String.format("%08x", plugin.GUI_TEXT_Color_true & 0xffffffff).substring(0,6)+"]":"[#"+String.format("%08x", plugin.GUI_TEXT_Color_false & 0xffffffff).substring(0,6)+"]")+plugin.sprachApiDaten.getText(player, "npc_isDead")+"[#ffFFff] ";
                                }
                                
                                infoStrg += "\n ";
                                infoStrg += "\n "+plugin.sprachApiDaten.getText(player, "npc_age")+": "+npc.getAge()+" ";
                                infoStrg += "\n "+plugin.sprachApiDaten.getText(player, "npc_health")+": "+npc.getHealth()+" ";
                                infoStrg += "\n "+plugin.sprachApiDaten.getText(player, "npc_hunger")+": "+npc.getHunger()+" ";
                                infoStrg += "\n "+plugin.sprachApiDaten.getText(player, "npc_thirst")+": "+npc.getThirst()+" ";

                                if(plugin.getDebug()>2){
                                    infoStrg += "\n ";
                                    infoStrg += "\n "+"Type "+npc.getType()+" ";
                                    infoStrg += "\n "+"NearestPlayer "+(npc.getNearestPlayer()!=null?npc.getNearestPlayer().getName():"null")+" ";
                                    infoStrg += "\n "+"HostilePlayer "+(npc.getHostilePlayer()!=null?npc.getHostilePlayer().getName():"null")+" ";
                                    infoStrg += "\n "+"Rider "+(((Player)npc.getRider())!=null?((Player)npc.getRider()).getName():"null")+" ";
                                    infoStrg += "\n "+"InfoID "+npc.getInfoID()+" ";
                                    infoStrg += "\n "+"Skin "+(npc.getSkin()!=null?npc.getSkin().toString():"null")+" ";
                                    infoStrg += "\n "+"TypeID "+npc.getTypeID()+" ";
                                    infoStrg += "\n "+"Variation "+npc.getVariation()+" ";
                                    if(plugin.getDebug()>3){
                                        infoStrg += "\n "+"Position "+npc.getPosition().toString()+" ";
                                        infoStrg += "\n "+"Rotation "+npc.getRotation().toString()+" ";
                                        infoStrg += "\n "+"ViewDirection "+npc.getViewDirection().toString()+" ";
                                    }
                                    infoStrg += "\n\n "+npc.getDefinition().getName()+"-"+npc.getDefinition().getBehaviour()+"-"+ npc.getDefinition().getAttackReaction()+" ";
                                }
                                //infoStrg += "\n "+""+npc.;
                                
                                if(plugin.getDebug()>3){System.out.println("[" + plugin.getDescription("name") + "] "+"Npc found: " + infoStrg );}
                                ((GuiLabel) player.getAttribute(plugin.plA.labelNpcInfo)).setText(infoStrg);

                                ((Timer)player.getAttribute(plugin.plA.timerLabelInfo)).setInterval(plugin.TIMER_Info);
                                if (((Timer)player.getAttribute(plugin.plA.timerLabelInfo)).isActive()){
                                    ((Timer)player.getAttribute(plugin.plA.timerLabelInfo)).setTick(0.0f);
                                    if(plugin.getDebug()>3){System.out.println("[" + plugin.getDescription("name") + "] "+"Timer Tick: " + ((Timer)player.getAttribute(plugin.plA.timerLabelInfo)).getTick() );}
                                }
                                ((Timer)player.getAttribute(plugin.plA.timerLabelInfo)).start();

                            }
                        }
                    });        
                }
            }
        }
        
        //### Player Infos
        if(plugin.getDebug()>4){System.out.println("[" + plugin.getDescription("name") + "] "+"Player Info: " + String.format("ViewDirection[%.6f|%.6f|%.6f] ",player.getViewDirection().x,player.getViewDirection().y,player.getViewDirection().z) );}
        if(plugin.getDebug()>4){System.out.println("[" + plugin.getDescription("name") + "] "+"Player Info: " + String.format("Position[%.6f|%.6f|%.6f] ",player.getPosition().x,player.getPosition().y,player.getPosition().z) );}
        //### Folgen ###
        if (((ArrayList)player.getAttribute(plugin.plA.followListe))!=null){
            if (((ArrayList)player.getAttribute(plugin.plA.followListe)).size()>0 ){
                //### Positions Berechnung
                final Vector3f position = new Vector3f(player.getViewDirection().negate());

                Vector3f playerPos = player.getPosition();
                position.setY(0f).normalizeLocal();
                position.multLocal(plugin.followBlockNewPos);
                if (player.isFlying()){
                    if(plugin.getDebug()>3){System.out.println("[" + plugin.getDescription("name") + "] "+"Result Info: " + "isFly" );}
                    CRT.ClassLambadHelper l = new CRT.ClassLambadHelper();
                    l.obj = playerPos;
                    player.raycast( new Vector3f(0, -1, 0),CollisionType.getBitmask(CollisionType.TERRAIN, CollisionType.CONSTRUCTIONS, CollisionType.OBJECTS), (result) -> {
                        if(plugin.getDebug()>3){System.out.println("[" + plugin.getDescription("name") + "] "+"Result Info: " + String.format("Position[%.6f|%.6f|%.6f] ",result.getCollisionPoint().x,result.getCollisionPoint().y,result.getCollisionPoint().z) + "\n" );}
                        l.obj = new Vector3f(result.getCollisionPoint().x, result.getCollisionPoint().y+2.0f, result.getCollisionPoint().z);
                        doRaycastFollow(player, position, (Vector3f) l.obj);
                    });
                }else{
                    doRaycastFollow(player, position, playerPos);
                }
            }
        }
    }

    @EventMethod
    public void onPlayerNpcInteraction(PlayerNpcInteractionEvent event){
        Player player = event.getPlayer();
        Npc npc = event.getNpc();
        String infoStrg = "";
        infoStrg += plugin.chatDTF.format(LocalTime.now())+" ";
        infoStrg += "["+npc.getDefinition().getNpcType()+"|"+ npc.getDefinition().getName()+"|"+ npc.getGlobalID()+"]";
        infoStrg += (!npc.getName().equals("")?": "+npc.getName():"");
        if(plugin.getDebug()>2){System.out.println("[" + plugin.getDescription("name") + "] "+"Npc touch: " + infoStrg );}
    }

    @EventMethod
    public void onPlayerChangeBlockPosition(PlayerChangeBlockPositionEvent event){
        Player player = event.getPlayer();
        if (((Vector3i)player.getAttribute(plugin.plA.blockPos))!=null){
            if(plugin.getDebug()>4){System.out.println("[" + plugin.getDescription("name") + "] "+"ChangeBlockPosition: " + "Block "+((Vector3i)player.getAttribute(plugin.plA.blockPos)) );}
            if(plugin.getDebug()>4){System.out.println("[" + plugin.getDescription("name") + "] "+"ChangeBlockPosition: " + "Player "+(player.getBlockPosition()) );}
            if(plugin.getDebug()>4){System.out.println("[" + plugin.getDescription("name") + "] "+"ChangeBlockPosition: " + "Distance "+((Vector3i)player.getAttribute(plugin.plA.blockPos)).distance(player.getBlockPosition()) );}
            if (((Vector3i)player.getAttribute(plugin.plA.blockPos)).distance(player.getBlockPosition())>plugin.followBlockNoNewPos+1){
                if(plugin.getDebug()>4){System.out.println("[" + plugin.getDescription("name") + "] "+"ChangeBlockPosition: " + "SET " + ((Vector3i)player.getAttribute(plugin.plA.blockPos)).distance(player.getBlockPosition()) );}
                player.setAttribute(plugin.plA.blockPos,new Vector3i(player.getBlockPosition()));
            }
        }
    }

    private void doRaycastFollow(Player player, Vector3f position, Vector3f playerPos) {
        //### Prüfe Abstand
        if (((Npc)((ArrayList)player.getAttribute(plugin.plA.followListe)).get(0)).getPosition().distance(position)>=plugin.followBlockNewPosTrigger && (((Vector3i)player.getAttribute(plugin.plA.blockPos)).distance(player.getBlockPosition())>=plugin.followBlockNoNewPos)){
            player.setAttribute(plugin.plA.blockPos,new Vector3i(player.getBlockPosition()));
            //### NPCs Ausführen
            CRT.ClassLambadHelper l = new CRT.ClassLambadHelper();
            ((ArrayList)player.getAttribute(plugin.plA.followListe)).forEach(lokalNpc->{
                //### Folgen Pos und so
                Vector3f posi = new Vector3f(player.getViewDirection().setY(0f).negate());                      
                Random randomno = new Random();
                posi.interpolateLocal(new Vector3f((randomno.nextBoolean()?plugin.followBlockNpcRandom:(plugin.followBlockNpcRandom*-1)), 0, (randomno.nextBoolean()?plugin.followBlockNpcRandom:(plugin.followBlockNpcRandom*-1))), 0.01f+(float)+(Math.random()*(0.11f - 0.001f))).normalizeLocal();
                posi.setY(0f).normalizeLocal();
                posi.multLocal(plugin.followBlockNewPos+(plugin.followBlockNpcDistance*l.n));
                posi.addLocal(playerPos);
                if(((Npc)lokalNpc).isAlerted()) ((Npc)lokalNpc).setAlerted(false);
                plugin.followListePos.put(((Npc)lokalNpc), posi);
                if (((Npc)lokalNpc).isLocked() && plugin.followListeIsLocked.get(((Npc)lokalNpc))==null){
                    ((Npc)lokalNpc).setLocked(false);
                    ((Npc)lokalNpc).moveTo(posi);
                    plugin.followListeIsLocked.put(((Npc)lokalNpc), true);
                }else{
                    ((Npc)lokalNpc).moveTo(posi);
                }

                if (((Npc)lokalNpc).getPosition().distance(posi)>plugin.followBlockNewPosRun && !((Npc)lokalNpc).isAlerted()){
                    ((Npc)lokalNpc).setAlerted(true);
                }else if(((Npc)lokalNpc).isAlerted()){
                    ((Npc)lokalNpc).setAlerted(false);
                }

                l.n++;
            });
            if (((Timer)player.getAttribute(plugin.plA.timerNPC)).isActive()){
                ((Timer)player.getAttribute(plugin.plA.timerNPC)).setTick(0.0f);
            }
            ((Timer)player.getAttribute(plugin.plA.timerNPC)).start();

        }/*else ToDo###* / if (((Npc)((ArrayList)player.getAttribute(plugin.plA.followListe)).get(0)).getPosition().distance(position)<=0.5f){
            if(plugin.getDebug()>4){System.out.println("[" + plugin.getDescription("name") + "] "+"doRaycasFollow: " + "Aktiv " );}
            ((ArrayList)player.getAttribute(plugin.plA.followListe)).forEach(lokalNpc->{
                Quaternion rotation = new Quaternion();
                rotation.lookAt(new Vector3f(playerPos).negateLocal());
                ((Npc)lokalNpc).setRotation(rotation);
            });
        }/**/
    }
}
