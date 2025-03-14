package com.rs.game.world.entity.player.content.dialogue.impl;

import java.util.List;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.utility.Utils;

public final class MiningGuildDwarf extends Dialogue {

	private int npcId;
	private boolean skillCape;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		skillCape = (Boolean) parameters[1];
		sendEntityDialogue(SEND_2_TEXT_CHAT,
				new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
						"Welcome to the Mining Guild.",
						"Can I help you with anything?"}, IS_NPC, npcId, 9827);

	}

	@Override
	public void run(int interfaceId, int componentId) {
		switch(stage) {
		case -1:
			stage = 0;
			if(skillCape) {
				sendOptionsDialogue("What would you like to say?",
						"What have you got in the Guild?",
						"What do you dwarves do with the ore you mine?",
						"Can you tell me about your skillcape?",
						"No thanks, I'm fine.");
			}else{
				sendOptionsDialogue("What would you like to say?",
						"What have you got in the Guild?",
						"What do you dwarves do with the ore you mine?",
						"No thanks, I'm fine.");
			}
		break;
		case 0:
			if(skillCape ? componentId == 1 : componentId == 2) {
				stage = 1;
				sendEntityDialogue(SEND_1_TEXT_CHAT,
						new String[] { player.getDisplayName(),
								"What have you got in the Guild?" }, IS_PLAYER, player.getIndex(), 9827);
			}else if (skillCape ? componentId == 2 : componentId == 3) {
				stage = 8;
				sendEntityDialogue(SEND_1_TEXT_CHAT,
						new String[] { player.getDisplayName(),
								"What do you dwarves do with the ore you mine?" }, IS_PLAYER,
						player.getIndex(), 9827);
			}else if (componentId == 3 && skillCape) {
				stage = 14;
				sendEntityDialogue(SEND_1_TEXT_CHAT,
						new String[] { player.getDisplayName(),
								"Can you tell me about your skillcape?" }, IS_PLAYER,player.getIndex(), 9827);
			}else{
				stage = -2;
				sendEntityDialogue(SEND_1_TEXT_CHAT,
						new String[] { player.getDisplayName(),
								"No thanks, I'm fine." }, IS_PLAYER,
						player.getIndex(), 9827);
			}
		break;
		case 1:
			stage = (byte) (player.getSkills().getLevelForXp(Skills.MINING) < 60 ? 2 : 4);
			sendEntityDialogue(SEND_4_TEXT_CHAT,
					new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
							"Ooh, it's WONDERFUL! There are lots of coal rocks,",
							"and even a few mithril rocks in the guild,",
							"all exclusively for people with at least level 60 mining.",
							"There's no better mining site anywhere near here."}, IS_NPC, npcId, 9827);
		break;
		case 2:
			stage = 3;
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(),
							"So you won't let me go in there?" }, IS_PLAYER,
					player.getIndex(), 9827);
		break;
		case 3:
			stage = 6;
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
							"Sorry, but rules are rules. Do some more training first.",
							"Can I help you with anything else?"}, IS_NPC, npcId, 9827);
		break;
		case 4:
			stage = 5;
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(),
							"It's a good thing I have level "+player.getSkills().getLevelForXp(Skills.MINING)+" Mining." }, IS_PLAYER,
					player.getIndex(), 9827);
		break;
		case 5:
			stage = 6;
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
							"Yes, that's amazing! Did you want anything else?"
					}, IS_NPC, npcId, 9827);
	
		break;
		case 6:
			stage = 7;
			sendOptionsDialogue("What would you like to say?",
					"What do you dwarves do with the ore you mine?",
					"No thanks, I'm fine.");
		break;
		case 7:
			if(componentId == 1) {
				stage = 8;
				sendEntityDialogue(SEND_1_TEXT_CHAT,
						new String[] { player.getDisplayName(),
								"What do you dwarves do with the ore you mine?" }, IS_PLAYER,
						player.getIndex(), 9827);
			}else{
				stage = -2;
				sendEntityDialogue(SEND_1_TEXT_CHAT,
						new String[] { player.getDisplayName(),
								"No thanks, I'm fine." }, IS_PLAYER,
						player.getIndex(), 9827);
			}
		break;
		case 8:
			stage = 9;
			sendEntityDialogue(SEND_3_TEXT_CHAT,
					new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
							"What do you think? We smelt it into bars, smith the metal",
							"to make armour and weapons, then we exchange them for",
							"goods and services."
					}, IS_NPC, npcId, 9827);
		break;
		case 9:
			stage = 10;
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { player.getDisplayName(),
							"I don't see many dwarves",
							"selling armour or weapons here."}, IS_PLAYER,
					player.getIndex(), 9827);
		break;
		case 10:
			stage = 11;
			sendEntityDialogue(SEND_4_TEXT_CHAT,
					new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
							"No this is only a mining outpost. We dwarves don't much",
							"like to settle in human cities. Most of the ore is carted off",
							"to Keldagrim, the great dwarven city. They've got a",
							"special blast furnace up there - it makes smelthing the ore"
					}, IS_NPC, npcId, 9827);
		break;
		case 11:
			stage = 12;
			sendEntityDialogue(SEND_3_TEXT_CHAT,
					new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
							"so much easier. There are plenty of dwarven traders",
							"working in Keldagrim. Anyway, can I help you with anything",
							"else?"
					}, IS_NPC, npcId, 9827);
		break;
		case 12:
			stage = 13;
			sendOptionsDialogue("What would you like to say?",
					"What have you got in the guild?",
					"No thanks, I'm fine.");
		break;
		case 13:
			if(componentId == 1) {
				stage = 1;
				sendEntityDialogue(SEND_1_TEXT_CHAT,
						new String[] { player.getDisplayName(),
								"What have you got in the Guild?" }, IS_PLAYER,
						player.getIndex(), 9827);
			}else{
				stage = -2;
				sendEntityDialogue(SEND_1_TEXT_CHAT,
						new String[] { player.getDisplayName(),
								"No thanks, I'm fine." }, IS_PLAYER,
						player.getIndex(), 9827);
			}
		break;
		case 14:
			if(player.getSkills().getLevel(Skills.MINING) < 99) {
				stage = 15;
				sendEntityDialogue(SEND_4_TEXT_CHAT,
						new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
						"Sure, this is a Skillcape of Mining. It shows my stature as",
						"a master miner! It has all sorts of uses including a skill",
						"boost to my Mining skill. When you get to level 99 com",
						"and talk to me and I'll sell you one. Is there anything else I"
						}, IS_NPC, npcId, 9827);
			}else{
				//TODO im not 99mining
			}
		break;
		case 15:
			stage = 16;
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
							"can help you with?"
					}, IS_NPC, npcId, 9827);
			break;
		case 16:
			stage = 17;
			sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE,
					"Yes",
					"No");
		break;
		case 17:
			if(componentId == 1) {
				stage = -1;
				sendEntityDialogue(SEND_1_TEXT_CHAT,
						new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
								"So what can I help you with, then?"
						}, IS_NPC, npcId, 9827);
			}else{
				stage = 18;
				sendEntityDialogue(SEND_1_TEXT_CHAT,
						new String[] { player.getDisplayName(),
								"No." }, IS_PLAYER,
						player.getIndex(), 9827);
			}
		break;
		case 18:
			stage = -2;
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
							"Okay, bye then."
					}, IS_NPC, npcId, 9827);
		break;
		default:
			end();
		break;
		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

	public static int getClosestDwarfID(Player player) {
		int npcId = 3295;
		int distance = -1;
		for(int regionId : player.getMapRegionsIds()) {
			List<Integer> npcsIndexes = World.getRegion(regionId)
					.getNPCsIndexes();
			if (npcsIndexes != null) {
				for (int npcIndex : npcsIndexes) {
					NPC npc = World.getNPCs().get(npcIndex);
					if (npc == null
							|| npc.isDead()
							|| npc.isFinished()
							|| !npc.getName().equals("Dwarf")) 
						continue;
					int newDistance = Utils.getDistance(player, npc);
					if(distance == -1 || distance > newDistance) {
						distance = newDistance;
						npcId = npc.getId();
					}
				}
			};
		}
		return npcId;
	}
}
