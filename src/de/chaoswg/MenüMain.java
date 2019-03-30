/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.chaoswg;

import java.util.ArrayList;
import net.risingworld.api.Timer;
import net.risingworld.api.events.EventMethod;
import net.risingworld.api.events.Listener;
import net.risingworld.api.events.player.PlayerKeyEvent;
import net.risingworld.api.events.player.gui.PlayerGuiElementClickEvent;
import net.risingworld.api.events.player.gui.PlayerGuiInputEvent;
import net.risingworld.api.gui.GuiLabel;
import net.risingworld.api.gui.GuiPanel;
import net.risingworld.api.gui.GuiTextField;
import net.risingworld.api.gui.PivotPosition;
import net.risingworld.api.objects.Npc;
import net.risingworld.api.objects.Player;

/**
 *
 * @author Schmull
 */
public class MenüMain extends GuiPanel implements Listener{
    private final NPCs plugin;
    private final Player player;
    private final GuiLabel TietelLabel, bntExit, bntLocked;
    private final GuiPanel TietelPanel;
    private final int[] useKeys;
    int[] myKey = {};
    private final CRT.ShortcutClass keyLocked;
    private final CRT.ShortcutClass keyAlerted;
    private final CRT.ShortcutClass keyInvincible;
    private final CRT.ShortcutClass keyFollow;
    private final CRT.ShortcutClass keyDummy;
    private final GuiLabel bntFollow;
    private final GuiLabel bntDummy;
    private final GuiLabel bntAlerted;
    private final GuiLabel bntInvincible;
    static Npc npc;
    private final GuiLabel NameLabel;
    private final GuiTextField NameInput;
    private final GuiLabel AgeLabel;
    private final GuiTextField AgeInput;
    private final GuiLabel HealthLabel;
    private final GuiTextField HealthInput;
    private final GuiLabel ThirstLabel;
    private final GuiTextField ThirstInput;
    private final GuiLabel HungerLabel;
    private final GuiTextField HungerInput;

    public MenüMain(String text, float x, float y, boolean relativeposition, NPCs plugin, Player player, Npc npc) {
        super(x, y, relativeposition, 0.1f, 0.1f, true);
        this.player = player;
        this.npc = npc;
        useKeys = this.player.getRegisteredKeys();
        this.player.unregisterKeys(useKeys);
        this.player.setMouseCursorVisible(true);
        this.player.setMouseCursorCoordinates(0.55f, 0.5f, true);
        this.plugin = plugin;
        ((GuiLabel) player.getAttribute(this.plugin.plA.labelNpcScreen)).setVisible(false);
        
        float Fac=10.0f,ZeilenMax=(npc.getDefinition().getBehaviour().toUpperCase().equals("DUMMY")?11.0f:10.0f);
        float hZeile =((float)(Fac/ZeilenMax)*0.06f),h=hZeile/2;
        setSize(0.2f, ((float)((0.25*ZeilenMax)/Fac))*plugin.PHI, true);
        setPivot(PivotPosition.Center);
        setColor((int)plugin.GUI_HG_Color );
        setBorderColor((int)plugin.GUI_HG_ColorBorder );
        setBorderThickness(plugin.GUI_Border, false);
        if(plugin.getDebug()>2){System.out.println("[" + plugin.getDescription("name") + "] "+"MenüMain " + "TextColor["+String.format("%08x", plugin.GUI_HG_Color & 0xffffffff)+"] ");}
        if(plugin.getDebug()>2){System.out.println("[" + plugin.getDescription("name") + "] "+"MenüMain " + "TextColorBorder["+String.format("%08x", plugin.GUI_HG_ColorBorder & 0xffffffff)+"] ");}
        
        myKey = CRT.addElement(myKey, CRT.getKeyInputValue(plugin.KEY_cancle_Aktion));
        keyLocked = new CRT.ShortcutClass();
        keyLocked.convert(plugin.sprachApiDaten.getText(player, "npc_isLocked"),"[#"+String.format("%08x", plugin.GUI_TEXT_Color_hot & 0xffffffff).substring(0,6)+"]",(npc.isLocked()?"[#"+String.format("%08x", plugin.GUI_TEXT_Color_true & 0xffffffff).substring(0,6)+"]":"[#"+String.format("%08x", plugin.GUI_TEXT_Color_false & 0xffffffff).substring(0,6)+"]") );
        if (keyLocked.keyCode>0) myKey = CRT.addElement(myKey, keyLocked.keyCode);
        
        keyAlerted = new CRT.ShortcutClass();
        keyAlerted.convert(plugin.sprachApiDaten.getText(player, "npc_Alerted"),"[#"+String.format("%08x", plugin.GUI_TEXT_Color_hot & 0xffffffff).substring(0,6)+"]",(npc.isAlerted()?"[#"+String.format("%08x", plugin.GUI_TEXT_Color_true & 0xffffffff).substring(0,6)+"]":"[#"+String.format("%08x", plugin.GUI_TEXT_Color_false & 0xffffffff).substring(0,6)+"]") );
        if (keyAlerted.keyCode>0) myKey = CRT.addElement(myKey, keyAlerted.keyCode);
        
        keyInvincible = new CRT.ShortcutClass();
        keyInvincible.convert(plugin.sprachApiDaten.getText(player, "npc_isInvincible"),"[#"+String.format("%08x", plugin.GUI_TEXT_Color_hot & 0xffffffff).substring(0,6)+"]",(npc.isInvincible()?"[#"+String.format("%08x", plugin.GUI_TEXT_Color_true & 0xffffffff).substring(0,6)+"]":"[#"+String.format("%08x", plugin.GUI_TEXT_Color_false & 0xffffffff).substring(0,6)+"]") );
        if (keyInvincible.keyCode>0) myKey = CRT.addElement(myKey, keyInvincible.keyCode);
        
        keyFollow = new CRT.ShortcutClass();
        keyFollow.convert(plugin.sprachApiDaten.getText(player, "npc_follow"),"[#"+String.format("%08x", plugin.GUI_TEXT_Color_hot & 0xffffffff).substring(0,6)+"]", (((ArrayList)player.getAttribute(plugin.plA.followListe)).indexOf(npc)<0?"[#"+String.format("%08x", plugin.GUI_TEXT_Color_act & 0xffffffff).substring(0,6)+"]":"[#"+String.format("%08x", plugin.GUI_TEXT_Color_follow & 0xffffffff).substring(0,6)+"]") );
        if (keyFollow.keyCode>0) myKey = CRT.addElement(myKey, keyFollow.keyCode);
        
        keyDummy = new CRT.ShortcutClass();
        keyDummy.convert(plugin.sprachApiDaten.getText(player, "npc_dummy"),"[#"+String.format("%08x", plugin.GUI_TEXT_Color_hot & 0xffffffff).substring(0,6)+"]", "[#"+String.format("%08x", plugin.GUI_TEXT_Color_act & 0xffffffff).substring(0,6)+"]" );
        if (keyDummy.keyCode>0) myKey = CRT.addElement(myKey, keyDummy.keyCode);
        
        if(plugin.getDebug()>2){System.out.println("[" + plugin.getDescription("name") + "] "+"MenüMain " + "TextScann["+keyLocked.strColor+"|"+keyLocked.keyCode+"] ");}
        if(plugin.getDebug()>2){System.out.println("[" + plugin.getDescription("name") + "] "+"MenüMain " + "TextScann["+keyAlerted.strColor+"|"+keyAlerted.keyCode+"] ");}
        if(plugin.getDebug()>2){System.out.println("[" + plugin.getDescription("name") + "] "+"MenüMain " + "TextScann["+keyInvincible.strColor+"|"+keyInvincible.keyCode+"] ");}
        if(plugin.getDebug()>2){System.out.println("[" + plugin.getDescription("name") + "] "+"MenüMain " + "TextScann["+keyFollow.strColor+"|"+keyFollow.keyCode+"] ");}
        
        this.player.disableClientsideKeys(myKey);
        this.player.registerKeys(myKey);

        
        TietelPanel = new GuiPanel(0f, 1f, true, 1f, (hZeile-0.02f)*plugin.PHI, true);
        TietelPanel.setPivot(PivotPosition.TopLeft);
        TietelPanel.setBorderThickness(plugin.GUI_Border, false);
        TietelPanel.setBorderColor((int)plugin.GUI_BNT_ColorBorder);
        TietelPanel.setColor((int)plugin.GUI_BNT_Color);
        addChild(TietelPanel);
        
        TietelLabel = new GuiLabel(text, 0.5f, 1f, true);
        TietelLabel.setPivot(PivotPosition.CenterTop);
        TietelLabel.setBorderThickness(plugin.GUI_Border, false);
        TietelLabel.setBorderColor(0xffffff00);
        TietelLabel.setFontSize(26);
        TietelLabel.setClickable(true);
        TietelPanel.addChild(TietelLabel);
        if(plugin.getDebug()>1){System.out.println("[" + plugin.getDescription("name") + "] "+"MenüMain " + "FontSize["+TietelLabel.getFontSize()+"]" );}
        
        bntExit = new GuiLabel(" X ", 1f, 1f, true);
        bntExit.setPivot(PivotPosition.TopRight);
        bntExit.setBorderThickness(plugin.GUI_Border, false);
        bntExit.setBorderColor((int)plugin.GUI_BNT_ColorBorder);
        bntExit.setFontColor(0xff0000ff);
        bntExit.setColor((int)plugin.GUI_BNT_Color);
        bntExit.setFontSize(26);
        bntExit.setClickable(true);
        TietelPanel.addChild(bntExit);
        
        int ZeilenCound=1;
        bntFollow = new GuiLabel(" "+keyFollow.strColor+" ", 0.5f, 1f-((hZeile*ZeilenCound)*plugin.PHI)-h, true);
        bntFollow.setPivot(PivotPosition.CenterTop);
        bntFollow.setBorderThickness(plugin.GUI_Border, false);
        bntFollow.setBorderColor((int)plugin.GUI_BNT_ColorBorder);
        bntFollow.setColor((int)plugin.GUI_BNT_Color);
        bntFollow.setFontSize(26);
        bntFollow.setClickable(true);
        addChild(bntFollow);
        
        if (npc.getDefinition().getBehaviour().toUpperCase().equals("DUMMY")){
            ZeilenCound++;
            bntDummy = new GuiLabel(" "+keyDummy.strColor+" ", 0.5f, 1f-((hZeile*ZeilenCound)*plugin.PHI)-h, true);
            bntDummy.setPivot(PivotPosition.CenterTop);
            bntDummy.setBorderThickness(plugin.GUI_Border, false);
            bntDummy.setBorderColor((int)plugin.GUI_BNT_ColorBorder);
            bntDummy.setColor((int)plugin.GUI_BNT_Color);
            bntDummy.setFontSize(26);
            bntDummy.setClickable(true);
            addChild(bntDummy);
        }else{
            bntDummy = null;
        }
        
        ZeilenCound++;
        bntAlerted = new GuiLabel(" "+keyAlerted.strColor+" ", 0.5f, 1f-((hZeile*ZeilenCound)*plugin.PHI)-h, true);
        bntAlerted.setPivot(PivotPosition.CenterTop);
        bntAlerted.setBorderThickness(plugin.GUI_Border, false);
        bntAlerted.setBorderColor((int)plugin.GUI_BNT_ColorBorder);
        bntAlerted.setColor((int)plugin.GUI_BNT_Color);
        bntAlerted.setFontSize(26);
        bntAlerted.setClickable(true);
        addChild(bntAlerted);
        
        ZeilenCound++;
        bntLocked = new GuiLabel(" "+keyLocked.strColor+" ", 0.5f, 1f-((hZeile*ZeilenCound)*plugin.PHI)-h, true);
        bntLocked.setPivot(PivotPosition.CenterTop);
        bntLocked.setBorderThickness(plugin.GUI_Border, false);
        bntLocked.setBorderColor((int)plugin.GUI_BNT_ColorBorder);
        bntLocked.setColor((int)plugin.GUI_BNT_Color);
        bntLocked.setFontSize(26);
        bntLocked.setClickable(true);
        addChild(bntLocked);
        
        ZeilenCound++;
        bntInvincible = new GuiLabel(" "+keyInvincible.strColor+" ", 0.5f, 1f-((hZeile*ZeilenCound)*plugin.PHI)-h, true);
        bntInvincible.setPivot(PivotPosition.CenterTop);
        bntInvincible.setBorderThickness(plugin.GUI_Border, false);
        bntInvincible.setBorderColor((int)plugin.GUI_BNT_ColorBorder);
        bntInvincible.setColor((int)plugin.GUI_BNT_Color);
        bntInvincible.setFontSize(26);
        bntInvincible.setClickable(true);
        addChild(bntInvincible);
        

        ZeilenCound++;
        float sx = 0.25f;
        NameLabel = new GuiLabel(" "+plugin.sprachApiDaten.getText(player, "npc_name")+" ", sx-0.01f, 1f-((hZeile*ZeilenCound)*plugin.PHI)-h, true);
        NameLabel.setPivot(PivotPosition.TopRight);
        NameLabel.setBorderThickness(plugin.GUI_Border, false);
        NameLabel.setBorderColor((int)plugin.GUI_BNT_ColorBorder);
        NameLabel.setColor((int)plugin.GUI_BNT_Color);
        NameLabel.setFontSize(26);
        addChild(NameLabel);

        NameInput = new GuiTextField(sx+0.01f, 1f-((hZeile*ZeilenCound)*plugin.PHI)-h+0.01f, true, 1f-sx-0.03f, 0.08f, true);
        NameInput.setPivot(PivotPosition.TopLeft);
        NameInput.setBorderThickness(plugin.GUI_Border, false);
        NameInput.setBorderColor((int)plugin.GUI_BNT_ColorBorder);
        NameInput.setColor((int)plugin.GUI_BNT_Color);
        NameInput.setClickable(true);
        
        NameInput.setText(npc.getName());
        NameInput.setListenForInput(true);
        addChild(NameInput);
        
        ZeilenCound++;
        sx = 0.5f;
        AgeLabel = new GuiLabel(" "+plugin.sprachApiDaten.getText(player, "npc_age")+" ", sx-0.01f, 1f-((hZeile*ZeilenCound)*plugin.PHI)-h, true);
        AgeLabel.setPivot(PivotPosition.TopRight);
        AgeLabel.setBorderThickness(plugin.GUI_Border, false);
        AgeLabel.setBorderColor((int)plugin.GUI_BNT_ColorBorder);
        AgeLabel.setColor((int)plugin.GUI_BNT_Color);
        AgeLabel.setFontSize(26);
        addChild(AgeLabel);

        AgeInput = new GuiTextField(sx+0.01f, 1f-((hZeile*ZeilenCound)*plugin.PHI)-h+0.01f, true, /*1f-sx-0.03f*/0.1f*plugin.PHI, 0.08f, true);
        AgeInput.setPivot(PivotPosition.TopLeft);
        AgeInput.setBorderThickness(plugin.GUI_Border, false);
        AgeInput.setBorderColor((int)plugin.GUI_BNT_ColorBorder);
        AgeInput.setColor((int)plugin.GUI_BNT_Color);
        AgeInput.setMaxCharacters(3);
        AgeInput.setClickable(true);
        
        AgeInput.setText(String.valueOf(npc.getAge()));
        AgeInput.setListenForInput(true);
        addChild(AgeInput);
        
        ZeilenCound++;
        sx = 0.5f;
        HealthLabel = new GuiLabel(" "+plugin.sprachApiDaten.getText(player, "npc_health")+" ", sx-0.01f, 1f-((hZeile*ZeilenCound)*plugin.PHI)-h, true);
        HealthLabel.setPivot(PivotPosition.TopRight);
        HealthLabel.setBorderThickness(plugin.GUI_Border, false);
        HealthLabel.setBorderColor((int)plugin.GUI_BNT_ColorBorder);
        HealthLabel.setColor((int)plugin.GUI_BNT_Color);
        HealthLabel.setFontSize(26);
        addChild(HealthLabel);

        HealthInput = new GuiTextField(sx+0.01f, 1f-((hZeile*ZeilenCound)*plugin.PHI)-h+0.01f, true, /*1f-sx-0.03f*/0.1f*plugin.PHI, 0.08f, true);
        HealthInput.setPivot(PivotPosition.TopLeft);
        HealthInput.setBorderThickness(plugin.GUI_Border, false);
        HealthInput.setBorderColor((int)plugin.GUI_BNT_ColorBorder);
        HealthInput.setColor((int)plugin.GUI_BNT_Color);
        HealthInput.setMaxCharacters(3);
        HealthInput.setClickable(true);
        
        HealthInput.setText(String.valueOf(npc.getHealth()));
        HealthInput.setListenForInput(true);
        addChild(HealthInput);
        
        ZeilenCound++;
        sx = 0.5f;
        HungerLabel = new GuiLabel(" "+plugin.sprachApiDaten.getText(player, "npc_hunger")+" ", sx-0.01f, 1f-((hZeile*ZeilenCound)*plugin.PHI)-h, true);
        HungerLabel.setPivot(PivotPosition.TopRight);
        HungerLabel.setBorderThickness(plugin.GUI_Border, false);
        HungerLabel.setBorderColor((int)plugin.GUI_BNT_ColorBorder);
        HungerLabel.setColor((int)plugin.GUI_BNT_Color);
        HungerLabel.setFontSize(26);
        addChild(HungerLabel);

        HungerInput = new GuiTextField(sx+0.01f, 1f-((hZeile*ZeilenCound)*plugin.PHI)-h+0.01f, true, /*1f-sx-0.03f*/0.1f*plugin.PHI, 0.08f, true);
        HungerInput.setPivot(PivotPosition.TopLeft);
        HungerInput.setBorderThickness(plugin.GUI_Border, false);
        HungerInput.setBorderColor((int)plugin.GUI_BNT_ColorBorder);
        HungerInput.setColor((int)plugin.GUI_BNT_Color);
        HungerInput.setMaxCharacters(3);
        HungerInput.setClickable(true);
        
        HungerInput.setText(String.valueOf(npc.getHunger()));
        HungerInput.setListenForInput(true);
        addChild(HungerInput);
        
        ZeilenCound++;
        sx = 0.5f;
        ThirstLabel = new GuiLabel(" "+plugin.sprachApiDaten.getText(player, "npc_thirst")+" ", sx-0.01f, 1f-((hZeile*ZeilenCound)*plugin.PHI)-h, true);
        ThirstLabel.setPivot(PivotPosition.TopRight);
        ThirstLabel.setBorderThickness(plugin.GUI_Border, false);
        ThirstLabel.setBorderColor((int)plugin.GUI_BNT_ColorBorder);
        ThirstLabel.setColor((int)plugin.GUI_BNT_Color);
        ThirstLabel.setFontSize(26);
        addChild(ThirstLabel);

        ThirstInput = new GuiTextField(sx+0.01f, 1f-((hZeile*ZeilenCound)*plugin.PHI)-h+0.01f, true, /*1f-sx-0.03f*/0.1f*plugin.PHI, 0.08f, true);
        ThirstInput.setPivot(PivotPosition.TopLeft);
        ThirstInput.setBorderThickness(plugin.GUI_Border, false);
        ThirstInput.setBorderColor((int)plugin.GUI_BNT_ColorBorder);
        ThirstInput.setColor((int)plugin.GUI_BNT_Color);
        ThirstInput.setMaxCharacters(3);
        ThirstInput.setClickable(true);
        
        ThirstInput.setText(String.valueOf(npc.getThirst()));
        ThirstInput.setListenForInput(true);
        addChild(ThirstInput);
        
        //### Destroy Timer
        final CRT.ClassLambadHelper lambTimerMenüDestroy = new CRT.ClassLambadHelper();
        lambTimerMenüDestroy.obj = player;
        Timer timerMenüDestroy = new Timer(60f, 0f, -1, () -> {  //lambda expression
            ((Timer) ((Player) lambTimerMenüDestroy.obj).getAttribute(plugin.plA.timerMenüDestroy)).pause();
            ((GuiLabel) player.getAttribute(plugin.plA.labelNpcScreen)).setVisible(false);
            ((MenüMain) player.getAttribute(plugin.plA.npcMainGui)).destroy();
	});
        player.setAttribute(plugin.plA.timerMenüDestroy,timerMenüDestroy);
        
        
        this.player.addGuiElement(TietelPanel);
        this.player.addGuiElement(TietelLabel);
        this.player.addGuiElement(bntExit);
        this.player.addGuiElement(bntFollow);
        if (npc.getDefinition().getBehaviour().toUpperCase().equals("DUMMY")){
            this.player.addGuiElement(bntDummy);
        }
        this.player.addGuiElement(bntAlerted);
        this.player.addGuiElement(bntLocked);
        this.player.addGuiElement(bntInvincible);
        this.player.addGuiElement(NameLabel);
        this.player.addGuiElement(NameInput);
        this.player.addGuiElement(AgeLabel);
        this.player.addGuiElement(AgeInput);
        this.player.addGuiElement(HealthLabel);
        this.player.addGuiElement(HealthInput);
        this.player.addGuiElement(HealthLabel);
        this.player.addGuiElement(HealthInput);
        this.player.addGuiElement(HungerLabel);
        this.player.addGuiElement(HungerInput);
        this.player.addGuiElement(ThirstLabel);
        this.player.addGuiElement(ThirstInput);
        this.player.addGuiElement(this);
        plugin.registerEventListener(this);
    }
    

    @Override
    public void destroy() {
        if(plugin.getDebug()>2){System.out.println("[" + plugin.getDescription("name") + "] "+"GuiDestroy " );}
        ((Timer) player.getAttribute(plugin.plA.timerMenüDestroy)).kill();
        player.setMouseCursorVisible(false);
        player.setAttribute(NPCs.plugin.plA.npcMainGui,null);
        ThirstInput.destroy();
        ThirstLabel.destroy();
        HungerInput.destroy();
        HungerLabel.destroy();
        HealthInput.destroy();
        HealthLabel.destroy();
        HealthInput.destroy();
        HealthLabel.destroy();
        AgeInput.destroy();
        AgeLabel.destroy();
        NameInput.destroy();
        NameLabel.destroy();
        bntInvincible.destroy();
        bntLocked.destroy();
        bntAlerted.destroy();
        if (bntDummy!=null) bntDummy.destroy();
        bntFollow.destroy();
        bntExit.destroy();
        TietelLabel.destroy();
        TietelPanel.destroy();
        player.enableClientsideKeys(CRT.getKeyInputValue(plugin.KEY_cancle_Aktion));
        player.enableClientsideKeys(myKey);
        player.unregisterKeys(myKey);
        player.registerKeys(useKeys);
        plugin.unregisterEventListener(this);
        super.destroy();
    }

    @EventMethod
    public void onPlayerGuiElementClick(PlayerGuiElementClickEvent event){
        if(plugin.getDebug()>1){System.out.println("[" + plugin.getDescription("name") + "] "+"GuiClick " + "ID["+event.getGuiElement().getID()+"]" + " MB"+event.getMouseButton());}
        if (event.getGuiElement()==bntExit) destroy();
        if (event.getGuiElement()==bntFollow) doFollow();
        if (event.getGuiElement()==bntDummy) doDummy();
        if (event.getGuiElement()==bntAlerted) doAlerted();
        if (event.getGuiElement()==bntLocked) doLocked();
        if (event.getGuiElement()==bntInvincible) doInvincible();
        //if (event.getGuiElement()==NameInput) doNameInput();
    }
    
    @EventMethod
    public void onPlayerKey(PlayerKeyEvent event) {
        if ((event.getKeyCode() == CRT.getKeyInputValue(plugin.KEY_cancle_Aktion)) && !event.isPressed() && ((MenüMain) player.getAttribute(plugin.plA.npcMainGui))!=null  && player == this.player ){
            ((MenüMain) player.getAttribute(plugin.plA.npcMainGui)).destroy();
        }

        if(plugin.getDebug()>1){System.out.println("[" + plugin.getDescription("name") + "] "+"MenüKeyEvent " + event.getKeyCode());}
        
        if (player == event.getPlayer() && (event.getKeyCode() == keyFollow.keyCode)    && !event.isPressed() && ((MenüMain) player.getAttribute(plugin.plA.npcMainGui))!=null) doFollow();
        if (player == event.getPlayer() && (event.getKeyCode() == keyDummy.keyCode)     && !event.isPressed() && ((MenüMain) player.getAttribute(plugin.plA.npcMainGui))!=null) doDummy();
        if (player == event.getPlayer() && (event.getKeyCode() == keyAlerted.keyCode)   && !event.isPressed() && ((MenüMain) player.getAttribute(plugin.plA.npcMainGui))!=null) doAlerted();
        if (player == event.getPlayer() && (event.getKeyCode() == keyLocked.keyCode)    && !event.isPressed() && ((MenüMain) player.getAttribute(plugin.plA.npcMainGui))!=null) doLocked();
        if (player == event.getPlayer() && (event.getKeyCode() == keyInvincible.keyCode)&& !event.isPressed() && ((MenüMain) player.getAttribute(plugin.plA.npcMainGui))!=null) doInvincible();
    }

    private void doFollow() {
        if(plugin.getDebug()>1){System.out.println("[" + plugin.getDescription("name") + "] "+"MenüKeyEvent " + "Follow "+keyFollow.keyCode);}
        if (((ArrayList)player.getAttribute(plugin.plA.followListe)).indexOf(npc)<0 && plugin.followListe.get(npc)==null){
            //### NPC noch nicht vorhanden, 
            if (((ArrayList)player.getAttribute(plugin.plA.followListe)).size()<plugin.maxNPCs){
                ((MenüMain) player.getAttribute(plugin.plA.npcMainGui)).destroy();
                //### hinzufügen
                ((ArrayList)player.getAttribute(plugin.plA.followListe)).add(npc);
                plugin.followListe.put(npc, player);
                plugin.followListePos.put(npc, npc.getPosition());
            }else{
                //### oder Limit Anzeige
                ((MenüMain) player.getAttribute(plugin.plA.npcMainGui)).setVisible(false);
                player.setMouseCursorVisible(false);
                ((GuiLabel) player.getAttribute(plugin.plA.labelNpcScreen)).setText(String.format(plugin.sprachApiDaten.getText(player, "MSG_followMax"), plugin.maxNPCs));
                ((GuiLabel) player.getAttribute(plugin.plA.labelNpcScreen)).setVisible(true);
                
                ((Timer)player.getAttribute(plugin.plA.timerMenüDestroy)).setInterval(plugin.TIMER_MenüMSG);
                if (((Timer)player.getAttribute(plugin.plA.timerMenüDestroy)).isActive()){
                    ((Timer)player.getAttribute(plugin.plA.timerMenüDestroy)).setTick(0.0f);
                    if(plugin.getDebug()>3){System.out.println("[" + plugin.getDescription("name") + "] "+"Timer Tick: " + ((Timer)player.getAttribute(plugin.plA.timerLabelScreen)).getTick() );}
                }
                ((Timer)player.getAttribute(plugin.plA.timerMenüDestroy)).start();
                
            }
        }else{
            if(plugin.followListe.get(npc)==player){
                ((MenüMain) player.getAttribute(plugin.plA.npcMainGui)).destroy();
                ((ArrayList)player.getAttribute(plugin.plA.followListe)).remove(npc);
                plugin.followListe.remove(npc);
                plugin.followListePos.remove(npc);
            }else{
                String infoStrg = "";
                infoStrg += " Name: "+(!npc.getName().equals("")?""+npc.getName()+" ":plugin.sprachApiDaten.getText(player, npc.getDefinition().getName())+"-"+npc.getGlobalID())+" "; // Name
                infoStrg += "\n "+String.format(plugin.sprachApiDaten.getText(player, "MSG_isAlreadyFollow"), plugin.followListe.get(npc).getName());
                if(plugin.getDebug()>3){System.out.println("[" + plugin.getDescription("name") + "] "+"Npc found: " + infoStrg );}
                
                ((MenüMain) player.getAttribute(plugin.plA.npcMainGui)).setVisible(false);
                player.setMouseCursorVisible(false);
                ((GuiLabel) player.getAttribute(plugin.plA.labelNpcScreen)).setText(infoStrg);
                ((GuiLabel) player.getAttribute(plugin.plA.labelNpcScreen)).setVisible(true);

                ((Timer)player.getAttribute(plugin.plA.timerMenüDestroy)).setInterval(plugin.TIMER_Info);
                if (((Timer)player.getAttribute(plugin.plA.timerMenüDestroy)).isActive()){
                    ((Timer)player.getAttribute(plugin.plA.timerMenüDestroy)).setTick(0.0f);
                    if(plugin.getDebug()>3){System.out.println("[" + plugin.getDescription("name") + "] "+"Timer Tick: " + ((Timer)player.getAttribute(plugin.plA.timerLabelScreen)).getTick() );}
                }
                ((Timer)player.getAttribute(plugin.plA.timerMenüDestroy)).start();
            }
        }
        plugin.npcAnzahl(player);
    }

    private void doDummy() {
        //FixMe###editnpc
        if(plugin.getDebug()>0){System.out.println("[" + plugin.getDescription("name") + "] "+"do Dummy " + ""+player.getName() );}        
//        player.setMouseCursorVisible(false);
        //player.executeConsoleCommand("wireframe");
        ((MenüMain) player.getAttribute(plugin.plA.npcMainGui)).destroy();
        player.executeConsoleCommand(String.format("editnpc %d",npc.getGlobalID()));
    }
    
    private void doAlerted() {
        if(plugin.getDebug()>1){System.out.println("[" + plugin.getDescription("name") + "] "+"MenüKeyEvent " + "Alerted "+keyAlerted.keyCode);}
        npc.setAlerted(!npc.isAlerted());
        ((MenüMain) player.getAttribute(plugin.plA.npcMainGui)).destroy();
    }

    private void doLocked() {
        if(plugin.getDebug()>1){System.out.println("[" + plugin.getDescription("name") + "] "+"MenüKeyEvent " + "Locked "+keyLocked.keyCode);}
        npc.setLocked(!npc.isLocked());
        ((MenüMain) player.getAttribute(plugin.plA.npcMainGui)).destroy();
    }

    private void doInvincible() {
        if(plugin.getDebug()>1){System.out.println("[" + plugin.getDescription("name") + "] "+"MenüKeyEvent " + "Invincible "+keyInvincible.keyCode);}
        npc.setInvincible(!npc.isInvincible());
        ((MenüMain) player.getAttribute(plugin.plA.npcMainGui)).destroy();
    }

//    private void doNameInput() {
//        if(plugin.getDebug()>1){System.out.println("[" + plugin.getDescription("name") + "] "+"MenüKeyEvent " + "NameInput "+keyInvincible.keyCode);}
//        player.disableClientsideKeys(CRT.getKeyInputValue(plugin.KEY_cancle_Aktion));
//    }
    
    
    @EventMethod
    public void onGuiInput(PlayerGuiInputEvent event) {
        if(plugin.getDebug()>2){System.out.println("[" + plugin.getDescription("name") + "] "+"GuiInput " + ""+event.getGuiElement().getID());}
        if (event.getGuiElement() == NameInput && player == event.getPlayer()){
            if(plugin.getDebug()>1){System.out.println("[" + plugin.getDescription("name") + "] "+"GuiInput " + "NameInput "+event.getInput()+" ");}
            npc.setName(event.getInput());
        }
        if (event.getGuiElement() == AgeInput && player == event.getPlayer()){
            if(plugin.getDebug()>1){System.out.println("[" + plugin.getDescription("name") + "] "+"GuiInput " + "AgeInput "+event.getInput()+" ");}
            npc.setAge(Integer.parseInt(event.getInput().replace(",", ".")));
        }
        if (event.getGuiElement() == AgeInput && player == event.getPlayer()){
            if(plugin.getDebug()>1){System.out.println("[" + plugin.getDescription("name") + "] "+"GuiInput " + "AgeInput "+event.getInput()+" ");}
            npc.setAge(Integer.parseInt(event.getInput().replace(",", ".")));
        }
        if (event.getGuiElement() == HealthInput && player == event.getPlayer()){
            if(plugin.getDebug()>1){System.out.println("[" + plugin.getDescription("name") + "] "+"GuiInput " + "HealthInput "+event.getInput()+" ");}
            npc.setHealth(Integer.parseInt(event.getInput().replace(",", ".")));
        }
        if (event.getGuiElement() == ThirstInput && player == event.getPlayer()){
            if(plugin.getDebug()>1){System.out.println("[" + plugin.getDescription("name") + "] "+"GuiInput " + "ThirstInput "+event.getInput()+" ");}
            npc.setThirst(Integer.parseInt(event.getInput().replace(",", ".")));
        }
    }


}
