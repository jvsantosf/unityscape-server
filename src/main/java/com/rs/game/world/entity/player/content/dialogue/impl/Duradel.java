/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.world.entity.player.actions.skilling.slayer.Slayer;
import com.rs.game.world.entity.player.actions.skilling.slayer.SlayerMaster;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class Duradel extends Dialogue {

    /**
     * Starts the dialogue
     */
    public Duradel() {
    }

    @Override
    public void start() {
        npcId = ((Integer) parameters[0]).intValue();
        sendEntityDialogue((short) 241, new String[]{NPCDefinitions.getNPCDefinitions(npcId).name, "Hello, brave warrior. What would you like?"}, (byte) 1, npcId, 9827);
    }

    /**
     * Runs the dialogue
     */
    @Override
    public void run(int interfaceId, int componentId) {
        if (stage == -1) {
            stage = 0;
            sendOptionsDialogue("What would you like to say?", "I would like a slayer task.", "What is my current slayer task?", "Can I cancel my current task?");
        } else if (stage == 0) {
            if (componentId == OPTION_1) {
                if (player.getSkills().getCombatLevel() >= 100 && player.getSkills().getLevel(18) >= 50) {
                    if (player.hasTask == false) {
                        Slayer.assignTask(player, SlayerMaster.DURADEL);
                        sendEntityDialogue((short) 241, new String[]{NPCDefinitions.getNPCDefinitions(8275).name, "Your slayer task is to kill " + player.slayerTask.getTaskMonstersLeft() + " " + player.slayerTask.getTask().simpleName}, (byte) 1, 8275, 9827);
                    } else {
                        sendEntityDialogue((short) 243, new String[]{NPCDefinitions.getNPCDefinitions(8275).name, "You already have a slayer task! ", "You need to kill " + player.slayerTask.getTaskMonstersLeft() + " " + player.slayerTask.getTask().simpleName}, (byte) 1, 8275, 9827);
                    }
                } else if (player.getSkills().getCombatLevel() < 100 && player.getSkills().getLevel(18) < 50){
                    sendEntityDialogue((short) 241, new String[]{NPCDefinitions.getNPCDefinitions(npcId).name, "Sorry, you need atleast level 100 combat and 50 Slayer to use me."}, (byte) 1, npcId, 9827);
                } else if (player.getSkills().getCombatLevel() >= 100 && player.getSkills().getLevel(18) < 50) {
                    sendEntityDialogue((short) 241, new String[]{NPCDefinitions.getNPCDefinitions(npcId).name, "Sorry, you need atleast 50 Slayer to use me."}, (byte) 1, npcId, 9827);
                } else if (player.getSkills().getCombatLevel() < 100 && player.getSkills().getLevel(18) >= 50) {
                    sendEntityDialogue((short) 241, new String[]{NPCDefinitions.getNPCDefinitions(npcId).name, "Sorry, you need atleast 100 combat to use me."}, (byte) 1, npcId, 9827);
                }
            } else if (componentId == OPTION_2) {
                if (player.hasTask == true) {
                    sendEntityDialogue((short) 242, new String[]{NPCDefinitions.getNPCDefinitions(8275).name, "You have a short memory, don't you?", "You need to kill " + player.slayerTask.getTaskMonstersLeft() + " " + player.slayerTask.getTask().simpleName}, (byte) 1, 8275, 9827);
                } else {
                    sendEntityDialogue((short) 241, new String[]{NPCDefinitions.getNPCDefinitions(8275).name, "Foolish warrior. You don't have a slayer task!"}, (byte) 1, 8275, 9827);
                }
                stage = -1;

            } else if (componentId == OPTION_3) {
                Slayer.displayPoints(player, 1);
                end();
            } else {
                end();
            }
        }
    }

    @Override
    public void finish() {
    }
    /**
     * Declares the npc ID
     */
    private int npcId;
}
