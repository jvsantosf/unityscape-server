package com.rs.network.decoders;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import com.hyze.Engine;
import com.hyze.event.player.PlayerCommandEvent;
import com.rs.Constants;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.familiar.Familiar;
import com.rs.game.world.entity.npc.familiar.Familiar.SpecialAttack;
import com.rs.game.world.entity.npc.instances.EliteDungeon.EliteDungeon;
import com.rs.game.world.entity.npc.instances.EliteDungeon.EliteDungeonController;
import com.rs.game.world.entity.npc.instances.EliteDungeon.EliteDungeonOne;
import com.rs.game.world.entity.player.CoordsEvent;
import com.rs.game.world.entity.player.Inventory;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.PlayerCombat;
import com.rs.game.world.entity.player.actions.PlayerFollow;
import com.rs.game.world.entity.player.actions.skilling.Summoning;
import com.rs.game.world.entity.player.content.*;
import com.rs.game.world.entity.player.content.CustomisedShop.MyShopItem;
import com.rs.game.world.entity.player.content.Notes.Note;
import com.rs.game.world.entity.player.content.activities.StealingCreation;
import com.rs.game.world.entity.player.content.activities.clanwars.ClanWars;
import com.rs.game.world.entity.player.content.dialogue.impl.EliteDungeonEnter;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.player.content.skills.construction.House;
import com.rs.game.world.entity.player.content.skills.magic.Magic;
import com.rs.game.world.entity.player.content.social.FriendChatsManager;
import com.rs.game.world.entity.player.content.social.clanchat.ClansManager;
import com.rs.game.world.entity.player.controller.ControlerHandler;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.network.LogicPacket;
import com.rs.network.Session;
import com.rs.network.decoders.handlers.ButtonHandler;
import com.rs.network.decoders.handlers.InventoryOptionsHandler;
import com.rs.network.decoders.handlers.NPCHandler;
import com.rs.network.decoders.handlers.ObjectHandler;
import com.rs.network.encoders.impl.ChatMessage;
import com.rs.network.encoders.impl.PublicChatMessage;
import com.rs.network.encoders.impl.QuickChatMessage;
import com.rs.network.io.InputStream;
import com.rs.network.io.OutputStream;
import com.rs.utility.DisplayNames;
import com.rs.utility.IPMute;
import com.rs.utility.Logger;
import com.rs.utility.Misc;
import com.rs.utility.SerializableFilesManager;
import com.rs.utility.Utils;
import com.rs.utility.huffman.Huffman;

public final class WorldPacketsDecoder extends Decoder {

	private static final byte[] PACKET_SIZES = new byte[164];
	private final static int ENTER_LONGSTRING_PACKET = 48;
	private final static int WALKING_PACKET = 8;
	private final static int MINI_WALKING_PACKET = 58;
	private final static int AFK_PACKET = -1;
	public final static int ACTION_BUTTON1_PACKET = 14;
	public final static int ACTION_BUTTON2_PACKET = 67;
	public final static int ACTION_BUTTON3_PACKET = 5;
	public final static int ACTION_BUTTON4_PACKET = 55;
	public final static int ACTION_BUTTON5_PACKET = 68;
	public final static int ACTION_BUTTON6_PACKET = 90;
	public final static int DROP_ITEM = 6;
	public final static int GROUND_ITEM_ACTION = 62;
	public final static int ACTION_BUTTON8_PACKET = 32;
	public final static int ACTION_BUTTON9_PACKET = 27;
	public final static int WORLD_MAP_CLICK = 38;
	private final static int WORLD_LIST_UPDATE = 87;
	public final static int ACTION_BUTTON10_PACKET = 96;
	public final static int RECEIVE_PACKET_COUNT_PACKET = 33;
	private final static int MAGIC_ON_ITEM_PACKET = -1;
	private final static int PLAYER_OPTION_4_PACKET = 17;
	private final static int MOVE_CAMERA_PACKET = 103;
	private final static int LOBBY_MAIN_CLICK_PACKET = 91;
	private final static int LOBBY_FRIEND_CHAT_SETTINGS = 79;
	private final static int INTERFACE_ON_OBJECT = 37;
	private final static int CLICK_PACKET = -1;
	private final static int MOUVE_MOUSE_PACKET = -1;
	private final static int KEY_TYPED_PACKET = 1;
	private final static int CLOSE_INTERFACE_PACKET = 54;
	private final static int COMMANDS_PACKET = 60;
	private final static int ITEM_ON_ITEM_PACKET = 3;
	private final static int IN_OUT_SCREEN_PACKET = -1;
	private final static int DONE_LOADING_REGION_PACKET = 30;
	private final static int PING_PACKET = 21;
	private final static int SCREEN_PACKET = 98;
	private final static int CHAT_TYPE_PACKET = 83;
	private final static int CHAT_PACKET = 53;
	private final static int PUBLIC_QUICK_CHAT_PACKET = 86;
	private final static int ADD_FRIEND_PACKET = 89;
	private final static int ADD_IGNORE_PACKET = 4;
	private final static int REMOVE_IGNORE_PACKET = 73;
	private final static int JOIN_FRIEND_CHAT_PACKET = 36;
	private final static int CHANGE_FRIEND_CHAT_PACKET = 22;
	private final static int KICK_FRIEND_CHAT_PACKET = 74;
	private final static int REMOVE_FRIEND_PACKET = 24;
	private final static int SEND_FRIEND_MESSAGE_PACKET = 82;
	private final static int SEND_FRIEND_QUICK_CHAT_PACKET = 0;
	private final static int OBJECT_CLICK1_PACKET = 26;
	private final static int OBJECT_CLICK2_PACKET = 59;
	private final static int OBJECT_CLICK3_PACKET = 40;
	private final static int OBJECT_CLICK4_PACKET = 23;
	private final static int OBJECT_CLICK5_PACKET = 80;
	private final static int OBJECT_EXAMINE_PACKET = 25;
	private final static int NPC_CLICK1_PACKET = 31;
	private final static int NPC_CLICK2_PACKET = 101;
	private final static int NPC_CLICK3_PACKET = 34;
	private final static int NPC_CLICK4_PACKET = 65;
	private final static int ATTACK_NPC = 20;
	private final static int PLAYER_OPTION_1_PACKET = 42;
	private final static int PLAYER_OPTION_2_PACKET = 46;
	private final static int ITEM_TAKE_PACKET = 57;
	private final static int DIALOGUE_CONTINUE_PACKET = 72;
	private final static int ENTER_INTEGER_PACKET = 81;
	private final static int ENTER_NAME_PACKET = 29;
	private final static int ENTER_STRING_PACKET = -1;
	private final static int SWITCH_INTERFACE_ITEM_PACKET = 76;
	private final static int INTERFACE_ON_PLAYER = 50;
	private final static int INTERFACE_ON_NPC = 66;
	private final static int COLOR_ID_PACKET = 97;
	private static final int NPC_EXAMINE_PACKET = 9;
	private final static int REPORT_ABUSE_PACKET = 11;
	private final static int PLAYER_OPTION_6_PACKET = 49;
	private final static int PLAYER_OPTION_5_PACKET = 77;
	private final static int ENTER_LONG_TEXT_PACKET = 48;
	private final static int PLAYER_OPTION_7_PACKET = 51;
	private final static int PLAYER_OPTION_9_PACKET = 56;
	private final static int KICK_CLAN_CHAT_PACKET = 92;
	private final static int GRAND_EXCHANGE_ITEM_SELECT_PACKET = 71;

	public static final int UNKNOWN = 15;

	public static final int CLICK = 13;

	public static final int PRESENCE_STATE_UPDATE_PACKET = 161;

	static {
		loadPacketSizes();
	}

	public static void loadPacketSizes() {
		PACKET_SIZES[0] = -1;
		PACKET_SIZES[1] = -2;
		PACKET_SIZES[2] = -1;
		PACKET_SIZES[3] = 16;
		PACKET_SIZES[4] = -1;
		PACKET_SIZES[5] = 8;
		PACKET_SIZES[6] = 8;
		PACKET_SIZES[7] = 3;
		PACKET_SIZES[8] = -1;
		PACKET_SIZES[9] = 3;
		PACKET_SIZES[10] = -1;
		PACKET_SIZES[11] = -1;
		PACKET_SIZES[12] = -1;
		PACKET_SIZES[13] = 7;
		PACKET_SIZES[14] = 9;
		PACKET_SIZES[15] = 6;
		PACKET_SIZES[16] = 2;
		PACKET_SIZES[17] = 3;
		PACKET_SIZES[18] = -1;
		PACKET_SIZES[19] = -2;
		PACKET_SIZES[20] = 3;
		PACKET_SIZES[21] = 0;
		PACKET_SIZES[22] = -1;
		PACKET_SIZES[23] = 9;
		PACKET_SIZES[24] = -1;
		PACKET_SIZES[25] = 9;
		PACKET_SIZES[26] = 9;
		PACKET_SIZES[27] = 8;
		PACKET_SIZES[28] = 4;
		PACKET_SIZES[29] = -1;
		PACKET_SIZES[30] = 0;
		PACKET_SIZES[31] = 3;
		PACKET_SIZES[32] = 8;
		PACKET_SIZES[33] = 4;
		PACKET_SIZES[34] = 3;
		PACKET_SIZES[35] = -1;
		PACKET_SIZES[36] = -1;
		PACKET_SIZES[37] = 19;
		PACKET_SIZES[38] = 4;
		PACKET_SIZES[39] = 4;
		PACKET_SIZES[40] = 9;
		PACKET_SIZES[41] = -1;
		PACKET_SIZES[42] = 3;
		PACKET_SIZES[43] = 7;
		PACKET_SIZES[44] = -2;
		PACKET_SIZES[45] = 7;
		PACKET_SIZES[46] = 3;
		PACKET_SIZES[47] = 4;
		PACKET_SIZES[48] = -1;
		PACKET_SIZES[49] = 3;
		PACKET_SIZES[50] = 11;
		PACKET_SIZES[51] = 3;
		PACKET_SIZES[52] = -1;
		PACKET_SIZES[53] = -1;
		PACKET_SIZES[54] = 0;
		PACKET_SIZES[55] = 8;
		PACKET_SIZES[56] = 3;
		PACKET_SIZES[57] = 7;
		PACKET_SIZES[58] = -1;
		PACKET_SIZES[59] = 9;
		PACKET_SIZES[60] = -1;
		PACKET_SIZES[61] = 7;
		PACKET_SIZES[62] = 7;
		PACKET_SIZES[63] = 12;
		PACKET_SIZES[64] = 4;
		PACKET_SIZES[65] = 3;
		PACKET_SIZES[66] = 11;
		PACKET_SIZES[67] = 9;
		PACKET_SIZES[68] = 8;
		PACKET_SIZES[69] = 15;
		PACKET_SIZES[70] = 1;
		PACKET_SIZES[71] = 2;
		PACKET_SIZES[72] = 6;
		PACKET_SIZES[73] = -1;
		PACKET_SIZES[74] = -1;
		PACKET_SIZES[75] = -2;
		PACKET_SIZES[76] = 16;
		PACKET_SIZES[77] = 3;
		PACKET_SIZES[78] = 1;
		PACKET_SIZES[79] = 3;
		PACKET_SIZES[80] = 9;
		PACKET_SIZES[81] = 4;
		PACKET_SIZES[82] = -2;
		PACKET_SIZES[83] = 1;
		PACKET_SIZES[84] = 1;
		PACKET_SIZES[85] = 3;
		PACKET_SIZES[86] = -1;
		PACKET_SIZES[87] = 4;
		PACKET_SIZES[88] = 3;
		PACKET_SIZES[89] = -1;
		PACKET_SIZES[90] = 8;
		PACKET_SIZES[91] = -2;
		PACKET_SIZES[92] = -1;
		PACKET_SIZES[93] = -1;
		PACKET_SIZES[94] = 9;
		PACKET_SIZES[95] = -2;
		PACKET_SIZES[96] = 8;
		PACKET_SIZES[97] = 2;
		PACKET_SIZES[98] = 6;
		PACKET_SIZES[99] = 2;
		PACKET_SIZES[100] = -2;
		PACKET_SIZES[101] = 3;
		PACKET_SIZES[102] = 7;
		PACKET_SIZES[103] = 4;
		PACKET_SIZES[104] = -2;
	}

	private Player player;
	private int chatType;

	public WorldPacketsDecoder(Session session, Player player) {
		super(session);
		this.player = player;
	}

	@Override
	public void decode(InputStream stream) {
		while (stream.getRemaining() > 0 && session.getChannel().isConnected()
				&& !player.isFinished()) {
			int packetId = stream.readPacket(player);
			if (packetId >= PACKET_SIZES.length || packetId < 0) {
				String message = "PacketId " + packetId + " has fake packet id.";
				Logger.logMessage(message);
				if (Constants.DEBUG) {
					System.out.println(message);
				}
				break;
			}
			int length = PACKET_SIZES[packetId];
			if (length == -1) {
				length = stream.readUnsignedByte();
			} else if (length == -2) {
				length = stream.readUnsignedShort();
			} else if (length == -3) {
				length = stream.readInt();
			} else if (length == -4) {
				length = stream.getRemaining();
				String message = "Invalid size for PacketId " + packetId
						+ ". Size guessed to be " + length +"";
				Logger.logMessage(message);
				if (Constants.DEBUG) {
					System.out.println(message);
				}
			}
			if (length > stream.getRemaining()) {
				length = stream.getRemaining();
				String message = "PacketId " + packetId
						+ " has fake size. - expected size " + length;
				Logger.logMessage(message);
				if (Constants.DEBUG)
				{
					System.out.println(message);
					// break;
				}

			}
			/*
			 * System.out.println("PacketId " +packetId+
			 * " has . - expected size " +length);
			 */
			int startOffset = stream.getOffset();
			processPackets(packetId, stream, length);
			stream.setOffset(startOffset + length);
		}
	}

	public static void decodeLogicPacket(final Player player, LogicPacket packet) {
		int packetId = packet.getId();
		InputStream stream = new InputStream(packet.getData());
		if (packetId == PLAYER_OPTION_5_PACKET) {
			int playerIndex = stream.readUnsignedShortLE128();
			Player other = World.getPlayers().get(playerIndex);
			if (other == null || other.isDead() || other.isFinished() || !player.getMapRegionsIds().contains(other.getRegionId())) {
				return;
			}
			if (!other.withinDistance(player, 14)) {
				player.getPackets().sendGameMessage("Unable to find target "+other.getDisplayName());
				return;
			}
			//player.getDialogueManager().startDialogue("ModPanel", playerIndex);
		}
		if (packetId == WALKING_PACKET || packetId == MINI_WALKING_PACKET) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead() || !player.finishedStarter || player.isLocked()) {
				return;
			}
			player.getInterfaceManager().closeReplacedRealChatBoxInterface();
			if (player.getFreezeDelay() >= Utils.currentTimeMillis()) {
				player.getPackets().sendGameMessage(
						"A magical force prevents you from moving.");
				return;
			}

			if (player.getTemporaryAttributtes().get("JAGGED") == Boolean.TRUE) {
				player.getDialogueManager().startDialogue("JAGDialogue");
				return;
			}
			int length = stream.getLength();
			/*if (packetId == MINI_WALKING_PACKET)
				length -= 13;*/
			int baseX = stream.readUnsignedShort128();
			boolean forceRun = stream.readUnsigned128Byte() == 1;
			int baseY = stream.readUnsignedShort128();
			int steps = (length - 5) / 2;
			if (steps > 25) {
				steps = 25;
			}
			player.stopAll();
			if(forceRun) {
				player.setRun(forceRun);
			}
			for (int step = 0; step < steps; step++) {
				if (!player.addWalkSteps(baseX + stream.readUnsignedByte(),
						baseY + stream.readUnsignedByte(), 25,
						true)) {
					break;
				}
			}
		} else if (packetId == PLAYER_OPTION_7_PACKET) {
			stream.readByte();
			int playerIndex = stream.readUnsignedShortLE128();
			Player other = World.getPlayers().get(playerIndex);
			if (other == null || other.isDead() || other.isFinished()
					|| !player.getMapRegionsIds().contains(other.getRegionId())) {
				return;
			}
			if (!other.withinDistance(player, 14)) {
				player.getPackets().sendGameMessage(
						"Unable to find target "+other.getDisplayName());
				return;
			}
			//player.getDialogueManager().startDialogue("ModPanel", playerIndex);
			return;
		} else if (packetId == PLAYER_OPTION_6_PACKET) {
			stream.readByte();
			int playerIndex = stream.readUnsignedShortLE128();
			Player other = World.getPlayers().get(playerIndex);
			if (other == null || other.isDead() || other.isFinished()
					|| !player.getMapRegionsIds().contains(other.getRegionId())) {
				return;
			}
			if (player.getInterfaceManager().containsScreenInter()) {
				player.getPackets().sendGameMessage("The other player is busy.");
				return;
			}
			if (!other.withinDistance(player, 14)) {
				player.getPackets().sendGameMessage(
						"Unable to find target "+other.getDisplayName());
				return;
			}
			if (player.getAttackedByDelay() + 10000 > Utils.currentTimeMillis()) {
				player.getPackets()
				.sendGameMessage(
						"<col=B00000>You view "+other.getDisplayName() +" Stats's until 10 seconds after the end of combat.");
				return;
			}
			player.getInterfaceManager().sendInterface(1314);
			player.getPackets().sendIComponentText(1314, 91, ""+other.getDisplayName() +"'s stats");
			player.getPackets().sendIComponentText(1314, 90, "");
			player.getPackets().sendIComponentText(1314, 61, ""+other.getSkills().getLevel(0));//attack
			player.getPackets().sendIComponentText(1314, 62, ""+other.getSkills().getLevel(2)); //str
			player.getPackets().sendIComponentText(1314, 63, ""+other.getSkills().getLevel(1)); //def
			player.getPackets().sendIComponentText(1314, 65, ""+other.getSkills().getLevel(4)); //range
			player.getPackets().sendIComponentText(1314, 66, ""+other.getSkills().getLevel(5)); //prayer
			player.getPackets().sendIComponentText(1314, 64, ""+other.getSkills().getLevel(6)); //mage
			player.getPackets().sendIComponentText(1314, 78, ""+other.getSkills().getLevel(20)); //rc
			player.getPackets().sendIComponentText(1314, 81, ""+other.getSkills().getLevel(22)); //construction
			player.getPackets().sendIComponentText(1314, 76, ""+other.getSkills().getLevel(24)); //dung
			player.getPackets().sendIComponentText(1314, 82, ""+other.getSkills().getLevel(3)); //hitpoints
			player.getPackets().sendIComponentText(1314, 83, ""+other.getSkills().getLevel(16)); //agiality
			player.getPackets().sendIComponentText(1314, 84, ""+other.getSkills().getLevel(15)); //herblore
			player.getPackets().sendIComponentText(1314, 80, ""+other.getSkills().getLevel(17)); //thiving
			player.getPackets().sendIComponentText(1314, 70, ""+other.getSkills().getLevel(12)); //crafting
			player.getPackets().sendIComponentText(1314, 85, ""+other.getSkills().getLevel(9)); //fletching
			player.getPackets().sendIComponentText(1314, 77, ""+other.getSkills().getLevel(18)); //slayer
			player.getPackets().sendIComponentText(1314, 79, ""+other.getSkills().getLevel(21)); //hunter
			player.getPackets().sendIComponentText(1314, 68, ""+other.getSkills().getLevel(14)); //mining
			player.getPackets().sendIComponentText(1314, 69, ""+other.getSkills().getLevel(13)); //smithing
			player.getPackets().sendIComponentText(1314, 74, ""+other.getSkills().getLevel(10)); //fishing
			player.getPackets().sendIComponentText(1314, 75, ""+other.getSkills().getLevel(7)); //cooking
			player.getPackets().sendIComponentText(1314, 73, ""+other.getSkills().getLevel(11)); //firemaking
			player.getPackets().sendIComponentText(1314, 71, ""+other.getSkills().getLevel(8)); //wc
			player.getPackets().sendIComponentText(1314, 72, ""+other.getSkills().getLevel(19)); //farming
			player.getPackets().sendIComponentText(1314, 67, ""+other.getSkills().getLevel(23)); //summining
			player.getPackets().sendIComponentText(1314, 30, "Domion tower KC:"); //boss
			player.getPackets().sendIComponentText(1314, 60, "" +other.getDominionTower().getKilledBossesCount()); //boss
			player.getPackets().sendIComponentText(1314, 87, ""+other.getMaxHitpoints()); //hitpoints
			player.getPackets().sendIComponentText(1314, 86, ""+other.getSkills().getCombatLevelWithSummoning()); //combatlevel
			player.getPackets().sendIComponentText(1314, 88, ""+other.getSkills().getTotalLevel(other)); //total level
			player.getPackets().sendIComponentText(1314, 89, ""+other.getSkills().getTotalXp(other)); //total level
			player.getTemporaryAttributtes().put("finding_player", Boolean.FALSE);
		} else if (packetId == LOBBY_MAIN_CLICK_PACKET) {//add these near the bottom
			stream.readShort();
			stream.readString();
			String idk3 = stream.readString();
			stream.readString();
			stream.readByte();
			if (idk3.equalsIgnoreCase("account_settings.ws?mod=recoveries")) {
				// open recover question link
			} else if (idk3.equalsIgnoreCase("account_settings.ws?mod=email")) {
				//opens players email
			} else if (idk3.equalsIgnoreCase("account_settings.ws?mod=messages")) {
				//opens players messages
			} else if (idk3.equalsIgnoreCase("purchasepopup.ws?externalName=rs")) {
				// open donation page
			}
		} else if (packetId == GROUND_ITEM_ACTION) {
			int y = stream.readUnsignedShort();
			int x = stream.readUnsignedShortLE();
			int id = stream.readShort();

			stream.readByte128();

			Position tile = new Position(x, y, player.getZ());
			FloorItem item = World.getRegion(player.getRegionId()).getGroundItem(id, tile, player);
			//int packageBox = 15246;
			if (item == null) {
				return;
			}

			if (item.getId() == 15246) {
				if (packetId == GROUND_ITEM_ACTION && RessourceBox.randomLocation > 9) {
					if (Player.isSunday() && !player.finishedTask) {
						World.sendWorldMessage("<col=7f0000>[Resource - Box]</col> - <col=7f0000>"+player.getUsername()+"</col> just found the Resource box!", false);
						player.sm("<col=8B000>You've found the Resource box for todays task!");
						player.foundBox = true;
					}
					RessourceBox.handleGroundPick(player, stream);
					player.sm("You claimed the Ressource Box content!");

				}
			}


			// code here
		} else if (packetId == LOBBY_FRIEND_CHAT_SETTINGS) {
			stream.readByte();
			int status = stream.readByte();
			stream.readByte();
			player.getFriendsIgnores().setPrivateStatus(status);

		} else if (packetId == INTERFACE_ON_OBJECT) {
			boolean forceRun = stream.readByte128() == 1;
			int itemId = stream.readInt();
			int y = stream.readShortLE128();
			int objectId = stream.readIntV2();
			int interfaceHash = stream.readInt();
			final int interfaceId = interfaceHash >> 16;
			int slot = stream.readShortLE();
			int x = stream.readShort128();
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
					|| player.isDead() || player.isLocked()) {
				return;
			}
			long currentTime = Utils.currentTimeMillis();
			if (player.getEmotesManager().getNextEmoteEnd() >= currentTime) {
				return;
			}
			final Position tile = new Position(x, y, player.getZ());
			int regionId = tile.getRegionId();
			if (!player.getMapRegionsIds().contains(regionId)) {
				return;
			}
			WorldObject mapObject = World.getObjectWithId(tile, objectId);
			if (mapObject == null || mapObject.getId() != objectId) {
				return;
			}
			final WorldObject object = !player.isAtDynamicRegion() ? mapObject : new WorldObject(objectId, mapObject.getType(), mapObject.getRotation(), x, y, player.getZ());
			final Item item = player.getInventory().getItem(slot);
			if (player.isDead() || Utils.getInterfaceDefinitionsSize() <= interfaceId) {
				return;
			}
			if (!player.getInterfaceManager().containsInterface(interfaceId)) {
				return;
			}
			if (item == null || item.getId() != itemId) {
				return;
			}
			player.stopAll(false); // false
			if(forceRun) {
				player.setRun(forceRun);
			}
			switch (interfaceId) {
			case Inventory.INVENTORY_INTERFACE: // inventory
				ObjectHandler.handleItemOnObject(player, object, interfaceId, item);
				break;
			}
		} else if (packetId == PLAYER_OPTION_2_PACKET) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
					|| player.isDead() || player.isLocked()) {
				return;
			}
			stream.readByte();
			int playerIndex = stream.readUnsignedShortLE128();
			Player p2 = World.getPlayers().get(playerIndex);
			if (p2 == null || p2.isDead() || p2.isFinished()
					|| !player.getMapRegionsIds().contains(p2.getRegionId())) {
				return;
			}
			player.stopAll(false);
			player.getActionManager().setAction(new PlayerFollow(p2));
		}else if (packetId == PLAYER_OPTION_4_PACKET) {
			stream.readByte();
			int playerIndex = stream.readUnsignedShortLE128();
			Player p2 = World.getPlayers().get(playerIndex);
			if (p2 == null || p2.isDead() || p2.isFinished() || player.isLocked()
					|| !player.getMapRegionsIds().contains(p2.getRegionId())) {
				return;
			}
			player.stopAll(false);
			if(player.isInDung()) {
				player.getPackets().sendGameMessage("No.");
				return;
			}
			if(player.isCantTrade()) {
				player.getPackets().sendGameMessage("You are busy.");
				return;
			}
			if (player.isTradeLocked() || p2.isTradeLocked()) {	player.getPackets().sendGameMessage("<col=FF0000>Trade request has been denied.");
			return;
			}
			if (player.getGameMode().isIronman() && !p2.getGameMode().isIronman() || !player.getGameMode().isIronman() && p2.getGameMode().isIronman()) {
				player.getPackets().sendGameMessage("You can't trade other players as an Ironman.");
				return;
			}
			if (player.getSkills().getTotalLevel() < 150) {
				player.getPackets().sendGameMessage("You must have a total level of <shad=000000>[150]</shad> to trade.");
				return;
			}
			if (player.getGameMode().isIronman()) {
				player.getPackets().sendGameMessage("You can't trade other players as an Ironman.");
				return;
			}
			if (p2.getGameMode().isIronman()) {
				player.getPackets().sendGameMessage("You can't trade other players as an Ironman.");
				return;
			}
			if (p2.getInterfaceManager().containsScreenInter() || p2.isCantTrade()) {
				player.getPackets().sendGameMessage("The other player is busy.");
				return;
			}
			if (!p2.withinDistance(player, 14)) {
				player.getPackets().sendGameMessage(
						"Unable to find target "+p2.getDisplayName());
				return;
			}

			if (p2.getTemporaryAttributtes().get("TradeTarget") == player) {
				p2.getTemporaryAttributtes().remove("TradeTarget");
				if (player.getDialogueManager().hasDialogue() || p2.getDialogueManager().hasDialogue()){
					player.sm("You cant have a dialogue open while trading");
					p2.sm("You cant have a dialogue open while trading");
					return;
				}
				player.getTrade().openTrade(p2);
				p2.getTrade().openTrade(player);
				return;
			}
			player.getTemporaryAttributtes().put("TradeTarget", p2);
			player.getPackets().sendGameMessage("Sending " + p2.getDisplayName() + " a request...");
			p2.getPackets().sendTradeRequestMessage(player);

		}else if (packetId == PLAYER_OPTION_1_PACKET) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead() || player.isLocked()) {
				return;
			}
			stream.readByte();
			int playerIndex = stream.readUnsignedShortLE128();
			Player other = World.getPlayers().get(playerIndex);
			if (other == null || other.isDead() || other.isFinished()
					|| !player.getMapRegionsIds().contains(other.getRegionId())) {
				return;
			}
			if (!player.getControlerManager().canPlayerOption1(other)) {
				return;
			}
			if (!player.isCanPvp()) {
				return;
			}
			if (!player.getControlerManager().canAttack(other)) {
				return;
			}
			if (!player.isCanPvp() || !other.isCanPvp()) {
				player.getPackets()
				.sendGameMessage(
						"You can only attack players in a player-vs-player area.");
				return;
			}
			if (player.getGameMode().isIronman() && !other.getGameMode().isIronman() || !player.getGameMode().isIronman() && other.getGameMode().isIronman()) {
				player.getPackets().sendGameMessage("As an Ironman, you can't attack other players.");
				return;
			}
			if (!other.isAtMultiArea() || !player.isAtMultiArea()) {
				if (player.getAttackedBy() != other
						&& player.getAttackedByDelay() > Utils
						.currentTimeMillis()) {
					player.getPackets().sendGameMessage(
							"You are already in combat.");
					return;
				}
				if (other.getAttackedBy() != player
						&& other.getAttackedByDelay() > Utils.currentTimeMillis()) {
					if (other.getAttackedBy() instanceof NPC) {
						other.setAttackedBy(player); // changes enemy to player,
						// player has priority over
						// npc on single areas
					} else {
						player.getPackets().sendGameMessage(
								"That player is already in combat.");
						return;
					}
				}
			}
			player.stopAll(false);
			player.getActionManager().setAction(new PlayerCombat(other));
		} else if (packetId == PLAYER_OPTION_9_PACKET) {
			boolean forceRun = stream.readUnsignedByte() == 1;
			int playerIndex = stream.readUnsignedShortLE128();
			Player p2 = World.getPlayers().get(playerIndex);
			if (p2 == null || p2 == player || p2.isDead() || p2.isFinished() || !player.getMapRegionsIds().contains(p2.getRegionId())) {
				return;
			}
			if (player.isLocked()) {
				return;
			}
			if (forceRun) {
				player.setRun(forceRun);
			}
			player.stopAll();
			player.getSlayerManager().invitePlayer(p2);
			ClansManager.viewInvite(player, p2);
		} else if (packetId == ATTACK_NPC) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead() || player.isLocked()) {
				return;
			}
			int npcIndex = stream.readUnsignedShort128();
			boolean forceRun = stream.read128Byte() == 1;
			if(forceRun) {
				player.setRun(forceRun);
			}
			NPC npc = World.getNPCs().get(npcIndex);
			if (npc == null || npc.isDead() || npc.isFinished()
					|| !player.getMapRegionsIds().contains(npc.getRegionId())
					|| !npc.getDefinitions().hasAttackOption()) {
				return;
			}
			if (!player.getControlerManager().canAttack(npc)) {
				return;
			}
			if (npc instanceof Familiar) {
				Familiar familiar = (Familiar) npc;
				if (familiar == player.getFamiliar()) {
					player.getPackets().sendGameMessage(
							"You can't attack your own familiar.");
					return;
				}
				if (!familiar.canAttack(player)) {
					player.getPackets().sendGameMessage(
							"You can't attack this npc.");
					return;
				}
			} else if (!npc.isForceMultiAttacked() && player.getPet() == null) {
				if (!npc.isAtMultiArea() || !player.isAtMultiArea()) {
					if (player.getAttackedBy() != npc
							&& player.getAttackedByDelay() > Utils
							.currentTimeMillis()) {
						player.getPackets().sendGameMessage(
								"You are already in combat.");
						return;
					}
					if (player.getPet() == null) {
						if ((npc.getAttackedBy() != player)
								&& npc.getAttackedByDelay() > Utils
								.currentTimeMillis()) {
							player.getPackets().sendGameMessage(
									"This npc is already in combat.");
							return;
						}
					}
				}
			}
			player.stopAll(false);
			player.getActionManager().setAction(new PlayerCombat(npc));
		} else if (packetId == INTERFACE_ON_PLAYER) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead() || player.isLocked()) {
				return;
			}
			@SuppressWarnings("unused")
			int junk1 = stream.readUnsignedShort();
			int playerIndex = stream.readUnsignedShort();
			int interfaceHash = stream.readIntV2();
			stream.readUnsignedShortLE128();
			stream.read128Byte();
			int interfaceId = interfaceHash >> 16;
			int componentId = interfaceHash - (interfaceId << 16);
			stream.readUnsignedShortLE128();
			if (Utils.getInterfaceDefinitionsSize() <= interfaceId) {
				return;
			}
			if (!player.getInterfaceManager().containsInterface(interfaceId)) {
				return;
			}
			if (componentId == 65535) {
				componentId = -1;
			}
			if (componentId != -1
					&& Utils.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId) {
				return;
			}
			Player other = World.getPlayers().get(playerIndex);
			if (other == null || other.isDead() || other.isFinished()
					|| !player.getMapRegionsIds().contains(other.getRegionId())) {
				return;
			}
			player.stopAll(false);
			switch (interfaceId) {
			case 1110:
				if (componentId == 87) {
					ClansManager.invite(player, other);
				}
				break;


			case Inventory.INVENTORY_INTERFACE:// Item on player
				if (!player.getControlerManager()
						.processItemOnPlayer(other, junk1)) {
					return;
				}
				InventoryOptionsHandler.handleItemOnPlayer(player, other, junk1);
				break;
			case 662:
			case 747:
				if (player.getFamiliar() == null) {
					return;
				}
				player.resetWalkSteps();
				if (interfaceId == 747 && componentId == 15
						|| interfaceId == 662 && componentId == 65
						|| interfaceId == 662 && componentId == 74
						|| interfaceId == 747 && componentId == 18) {
					if (interfaceId == 662 && componentId == 74
							|| interfaceId == 747 && componentId == 24 || interfaceId == 747
							&& componentId == 18) {
						if (player.getFamiliar().getSpecialAttack() != SpecialAttack.ENTITY) {
							return;
						}
					}
					if (player.getGameMode().isIronman() && !other.getGameMode().isIronman() || !player.getGameMode().isIronman() && other.getGameMode().isIronman()) {
						player.getPackets().sendGameMessage("As an Ironman, you can't attack other players.");
						return;
					}
					if (!player.isCanPvp() || !other.isCanPvp()) {
						player.getPackets()
						.sendGameMessage(
								"You can only attack players in a player-vs-player area.");
						return;
					}
					if (!player.getFamiliar().canAttack(other)) {
						player.getPackets()
						.sendGameMessage(
								"You can only use your familiar in a multi-zone area.");
						return;
					} else {
						player.getFamiliar().setSpecial(
								interfaceId == 662 && componentId == 74
								|| interfaceId == 747
								&& componentId == 18);
						player.getFamiliar().setTarget(other);
					}
				}
				break;
			case 193:
				switch (componentId) {
				case 28:
				case 32:
				case 24:
				case 20:
				case 30:
				case 34:
				case 26:
				case 22:
				case 29:
				case 33:
				case 25:
				case 21:
				case 31:
				case 35:
				case 27:
				case 23:
				case 56: // magic dart
				case 47:
					if (Magic.checkCombatSpell(player, componentId, 1, false)) {
						player.setNextFacePosition(new Position(other
								.getCoordFaceX(other.getSize()), other
								.getCoordFaceY(other.getSize()), other.getZ()));
						if (!player.getControlerManager().canAttack(other)) {
							return;
						}
						if (!player.isCanPvp() || !other.isCanPvp()) {
							player.getPackets()
							.sendGameMessage(
									"You can only attack players in a player-vs-player area.");
							return;
						}
						if (!other.isAtMultiArea() || !player.isAtMultiArea()) {
							if (player.getAttackedBy() != other
									&& player.getAttackedByDelay() > Utils
									.currentTimeMillis()) {
								player.getPackets()
								.sendGameMessage(
										"That "
												+ (player
														.getAttackedBy() instanceof Player ? "player"
																: "npc")
																+ " is already in combat.");
								return;
							}
							if (other.getAttackedBy() != player
									&& other.getAttackedByDelay() > Utils
									.currentTimeMillis()) {
								if (other.getAttackedBy() instanceof NPC) {
									other.setAttackedBy(player); // changes enemy
									// to player,
									// player has
									// priority over
									// npc on single
									// areas
								} else {
									player.getPackets()
									.sendGameMessage(
											"That player is already in combat.");
									return;
								}
							}
						}
						player.getActionManager()
						.setAction(new PlayerCombat(other));
					}
					break;
				}
			case 192:
				switch (componentId) {
				case 25: // air strike
				case 28: // water strike
				case 30: // earth strike
				case 32: // fire strike
				case 34: // air bolt
				case 39: // water bolt
				case 42: // earth bolt
				case 45: // fire bolt
				case 49: // air blast
				case 52: // water blast
				case 58: // earth blast
				case 63: // fire blast
				case 70: // air wave
				case 73: // water wave
				case 77: // earth wave
				case 80: // fire wave
				case 86: // teleblock
				case 84: // air surge
				case 87: // water surge
				case 89: // earth surge
				case 91: // fire surge
				case 99: // storm of armadyl
				case 47: // crumble undead
				case 56: // magic dart
				case 36: // bind
				case 66: // Sara Strike
				case 67: // Guthix Claws
				case 68: // Flame of Zammy
				case 55: // snare
				case 81: // entangle
					if (Magic.checkCombatSpell(player, componentId, 1, false)) {
						player.setNextFacePosition(new Position(other
								.getCoordFaceX(other.getSize()), other
								.getCoordFaceY(other.getSize()), other.getZ()));
						if (!player.getControlerManager().canAttack(other)) {
							return;
						}
						if (!player.isCanPvp() || !other.isCanPvp()) {
							player.getPackets()
							.sendGameMessage(
									"You can only attack players in a player-vs-player area.");
							return;
						}
						if (!other.isAtMultiArea() || !player.isAtMultiArea()) {
							if (player.getAttackedBy() != other
									&& player.getAttackedByDelay() > Utils
									.currentTimeMillis()) {
								player.getPackets()
								.sendGameMessage(
										"That "
												+ (player
														.getAttackedBy() instanceof Player ? "player"
																: "npc")
																+ " is already in combat.");
								return;
							}
							if (other.getAttackedBy() != player
									&& other.getAttackedByDelay() > Utils
									.currentTimeMillis()) {
								if (other.getAttackedBy() instanceof NPC) {
									other.setAttackedBy(player); // changes enemy
									// to player,
									// player has
									// priority over
									// npc on single
									// areas
								} else {
									player.getPackets()
									.sendGameMessage(
											"That player is already in combat.");
									return;
								}
							}
						}
						player.getActionManager()
						.setAction(new PlayerCombat(other));
					}
					break;
				}
				break;
			}
			String message = "Spell:" + componentId;
			Logger.logMessage(message);
			if (Constants.DEBUG) {
				System.out.println(message);
			}
		} else if (packetId == INTERFACE_ON_NPC) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead() || player.isLocked()) {
				return;
			}
			stream.readByte();
			int interfaceHash = stream.readInt();
			int npcIndex = stream.readUnsignedShortLE();
			int interfaceSlot = stream.readUnsignedShortLE128();
			stream.readUnsignedShortLE();
			int interfaceId = interfaceHash >> 16;
			int componentId = interfaceHash - (interfaceId << 16);
			if (Utils.getInterfaceDefinitionsSize() <= interfaceId) {
				return;
			}
			if (!player.getInterfaceManager().containsInterface(interfaceId)) {
				return;
			}
			if (componentId == 65535) {
				componentId = -1;
			}
			if (componentId != -1
					&& Utils.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId) {
				return;
			}
			NPC npc = World.getNPCs().get(npcIndex);
			if (npc == null || npc.isDead() || npc.isFinished()
					|| !player.getMapRegionsIds().contains(npc.getRegionId())) {
				return;
			}
			player.stopAll(false);
			if (interfaceId != Inventory.INVENTORY_INTERFACE) {
				if (!npc.getDefinitions().hasAttackOption()) {
					player.getPackets().sendGameMessage(
							"You can't attack this npc.");
					return;
				}
			}
			switch (interfaceId) {
			case Inventory.INVENTORY_INTERFACE:
				Item item = player.getInventory().getItem(interfaceSlot);
				if (item == null || !player.getControlerManager().processItemOnNPC(npc, item)) {
					return;
				}
				InventoryOptionsHandler.handleItemOnNPC(player, npc, item);
				break;
			case 1165:
				Summoning.attackDreadnipTarget(npc, player);
				break;
			case 662:
			case 747:
				if (player.getFamiliar() == null) {
					return;
				}
				player.resetWalkSteps();
				if (interfaceId == 747 && componentId == 15
						|| interfaceId == 662 && componentId == 65
						|| interfaceId == 662 && componentId == 74
						|| interfaceId == 747 && componentId == 18
						|| interfaceId == 747 && componentId == 24) {
					if (interfaceId == 662 && componentId == 74 || interfaceId == 747
							&& componentId == 18) {
						if (player.getFamiliar().getSpecialAttack() != SpecialAttack.ENTITY) {
							return;
						}
					}
					if(npc instanceof Familiar) {
						Familiar familiar = (Familiar) npc;
						if (familiar == player.getFamiliar()) {
							player.getPackets().sendGameMessage("You can't attack your own familiar.");
							return;
						}
						if (!player.getFamiliar().canAttack(familiar.getOwner())) {
							player.getPackets().sendGameMessage("You can only attack players in a player-vs-player area.");
							return;
						}
					}
					if (!player.getFamiliar().canAttack(npc)) {
						player.getPackets()
						.sendGameMessage(
								"You can only use your familiar in a multi-zone area.");
						return;
					} else {
						player.getFamiliar().setSpecial(
								interfaceId == 662 && componentId == 74
								|| interfaceId == 747
								&& componentId == 18);
						player.getFamiliar().setTarget(npc);
					}
				}
				break;


			case 193:
				switch (componentId) {
				case 28:
				case 32:
				case 24:
				case 20:
				case 30:
				case 34:
				case 26:
				case 22:
				case 29:
				case 33:
				case 25:
				case 21:
				case 31:
				case 35:
				case 27:
				case 23:
					if (Magic.checkCombatSpell(player, componentId, 1, false)) {
						player.setNextFacePosition(new Position(npc
								.getCoordFaceX(npc.getSize()), npc
								.getCoordFaceY(npc.getSize()), npc.getZ()));
						if (!player.getControlerManager().canAttack(npc)) {
							return;
						}
						if (npc instanceof Familiar) {
							Familiar familiar = (Familiar) npc;
							if (familiar == player.getFamiliar()) {
								player.getPackets().sendGameMessage(
										"You can't attack your own familiar.");
								return;
							}
							if (!familiar.canAttack(player)) {
								player.getPackets().sendGameMessage(
										"You can't attack this npc.");
								return;
							}
						} else if (!npc.isForceMultiAttacked()) {
							if (!npc.isAtMultiArea() || !player.isAtMultiArea()) {
								if (player.getAttackedBy() != npc
										&& player.getAttackedByDelay() > Utils
										.currentTimeMillis()) {
									player.getPackets().sendGameMessage(
											"You are already in combat.");
									return;
								}
								if (npc.getAttackedBy() != player
										&& npc.getAttackedByDelay() > Utils
										.currentTimeMillis()) {
									player.getPackets().sendGameMessage(
											"This npc is already in combat.");
									return;
								}
							}
						}
						player.getActionManager().setAction(
								new PlayerCombat(npc));
					}
					break;
				}
			case 192:
				switch (componentId) {
				case 25: // air strike
				case 28: // water strike
				case 30: // earth strike
				case 32: // fire strike
				case 34: // air bolt
				case 39: // water bolt
				case 42: // earth bolt
				case 45: // fire bolt
				case 49: // air blast
				case 52: // water blast
				case 58: // earth blast
				case 63: // fire blast
				case 70: // air wave
				case 73: // water wave
				case 77: // earth wave
				case 80: // fire wave
				case 84: // air surge
				case 87: // water surge
				case 89: // earth surge
				case 66: // Sara Strike
				case 67: // Guthix Claws
				case 68: // Flame of Zammy
				case 93:
				case 47:
				case 56:
				case 91: // fire surge
				case 99: // storm of Armadyl
				case 36: // bind
				case 55: // snare
				case 81: // entangle
					if (Magic.checkCombatSpell(player, componentId, 1, false)) {
						player.setNextFacePosition(new Position(npc
								.getCoordFaceX(npc.getSize()), npc
								.getCoordFaceY(npc.getSize()), npc.getZ()));
						if (!player.getControlerManager().canAttack(npc)) {
							return;
						}
						if (npc instanceof Familiar) {
							Familiar familiar = (Familiar) npc;
							if (familiar == player.getFamiliar()) {
								player.getPackets().sendGameMessage(
										"You can't attack your own familiar.");
								return;
							}
							if (!familiar.canAttack(player)) {
								player.getPackets().sendGameMessage(
										"You can't attack this npc.");
								return;
							}
						} else if (!npc.isForceMultiAttacked()) {
							if (!npc.isAtMultiArea() || !player.isAtMultiArea()) {
								if (player.getAttackedBy() != npc
										&& player.getAttackedByDelay() > Utils
										.currentTimeMillis()) {
									player.getPackets().sendGameMessage(
											"You are already in combat.");
									return;
								}
								if (npc.getAttackedBy() != player
										&& npc.getAttackedByDelay() > Utils
										.currentTimeMillis()) {
									player.getPackets().sendGameMessage(
											"This npc is already in combat.");
									return;
								}
							}
						}
						player.getActionManager().setAction(
								new PlayerCombat(npc));
					}
					break;
				}
				break;
			}
			String message = "Spell:" + componentId;
			Logger.logMessage(message);
			if (Constants.DEBUG) {
				System.out.println(message);
			}
		} else if (packetId == NPC_CLICK1_PACKET) {
			NPCHandler.handleOption1(player, stream);
		} else if (packetId == NPC_CLICK2_PACKET) {
			NPCHandler.handleOption2(player, stream);
		} else if (packetId == NPC_CLICK3_PACKET) {
			NPCHandler.handleOption3(player, stream);
		} else if (packetId == NPC_CLICK4_PACKET) {
			NPCHandler.handleOption4(player, stream);
		} else if (packetId == OBJECT_CLICK1_PACKET) {
			ObjectHandler.handleOption(player, stream, 1);
		} else if (packetId == OBJECT_CLICK2_PACKET) {
			ObjectHandler.handleOption(player, stream, 2);
		} else if (packetId == OBJECT_CLICK3_PACKET) {
			ObjectHandler.handleOption(player, stream, 3);
		} else if (packetId == OBJECT_CLICK4_PACKET) {
			ObjectHandler.handleOption(player, stream, 4);
		} else if (packetId == OBJECT_CLICK5_PACKET) {
			ObjectHandler.handleOption(player, stream, 5);
		} else if (packetId == ITEM_TAKE_PACKET) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead() || player.isLocked()) {
				return;
			}
			int y = stream.readUnsignedShort();
			int x = stream.readUnsignedShortLE();
			final int id = stream.readUnsignedShort();
			boolean forceRun = stream.read128Byte() == 1;
			final Position tile = new Position(x, y, player.getZ());
			final int regionId = tile.getRegionId();
			if (!player.getMapRegionsIds().contains(regionId)) {
				return;
			}
			final FloorItem item = World.getRegion(regionId).getGroundItem(id,
					tile, player);
			if (item == null) {
				return;
			}
			player.stopAll(false);
			if(forceRun) {
				player.setRun(forceRun);
			}
			player.setCoordsEvent(new CoordsEvent(tile, () -> {

				final FloorItem item1 = World.getRegion(regionId).getGroundItem(id, tile, player);
				if  (item1 == null)
					return;
				if (player.getGameMode().isIronman() && item1.hasOwner() && item1.getOwner() != player.getUsername()) {
					player.getPackets().sendGameMessage("This is not your item.");
					return;
				}
				if(item1.getId() == 15246 && RessourceBox.isBoxSpawned && item1.getOwner() != player.getUsername() && RessourceBox.randomLocation > 0) {
					RessourceBox.randomLocation = 0;
					if (Player.isSunday() && !player.finishedTask) {
						World.sendWorldMessage("<col=7f0000>[Resource - Box]</col> - <col=7f0000>"+player.getUsername()+"</col> just found the Resource box!", false);
						player.sm("<col=8B000>You've found the Resource box for todays task!");
						player.foundBox = true;
						RessourceBox.randomLocation = 0;
						RessourceBox.isBoxSpawned = false;
					} else if (player.oresMined >= 50 && player.answerTrivia >= 10) {
						player.sm("<col=8B000>You have finished todays task and claimed your reward!");
						player.foundBox = true;
						player.setSkillPoints(player.getSkillPoints() + 100);
						player.getSquealOfFortune().giveEarnedSpins(5);
						player.finishedTask = true;
						player.startedTask = false;
						World.sendWorldMessage("<col=7f0000>[Resource - Box]</col> - <col=7f0000>"+player.getUsername()+"</col> just found the Resource box!", false);
						RessourceBox.isBoxSpawned = false;
						RessourceBox.randomLocation = 0;
					}
				} else if(item1.getId() == 15246 && RessourceBox.isBoxSpawned && item1.getOwner() == player.getUsername()) {
					return;
				}
				player.setNextFacePosition(tile);
				player.addWalkSteps(tile.getX(), tile.getY(), 1);
				World.removeGroundItem(player, item1);
			}, 1, 1));
		}
	}

	public Player getPlayer() {
		return player;
	}
	public void sendInterface(int i, int windowId,
			int windowComponentId, int interfaceId) {
		// currently fixes the inter engine.. not ready for same component
		// ids(tabs), different inters
		if (!(windowId == 752 && (windowComponentId == 9 || windowComponentId == 12))) { // if
			// chatbox
			if (player.getInterfaceManager().containsInterface(
					windowComponentId, interfaceId)) {
				closeInterface(windowComponentId);
			}
			if (!player.getInterfaceManager().addInterface(windowId,
					windowComponentId, interfaceId)) {
				Logger.log(this, "Error adding interface: " + windowId + " , "
						+ windowComponentId + " , " + interfaceId);
				return;
			}
		}
	}
	public void closeInterface(int windowComponentId) {
		closeInterface(
				player.getInterfaceManager().getTabWindow(windowComponentId));
		player.getInterfaceManager().removeTab(windowComponentId);
	}

	public void processPackets(final int packetId, InputStream stream,
			int length) {
		
		player.setPacketsDecoderPing(Utils.currentTimeMillis());
		if (packetId == PING_PACKET) {
			// kk we ping :) NO ALEX
			OutputStream packet = new OutputStream(0);
			packet.writePacket(player, 153);
			player.getSession().write(packet);
		} else if (packetId == MOUVE_MOUSE_PACKET) {
			// USELESS PACKET
		} else if (packetId == KEY_TYPED_PACKET) {

			int keyPressed = stream.readByte();
			switch (keyPressed) {
			case 27:
				System.out.println("lel??");
				player.getInterfaceManager().closeScreenInterface();
				return;

			}
		} else if (packetId == RECEIVE_PACKET_COUNT_PACKET) {
			// interface packets
			stream.readInt();

		} else if (packetId == UNKNOWN) {
			System.err.println("unknown");
			int sd = stream.readInt();
			int delay = stream.readShort128();
			System.err.println(" "+sd+" - "+delay);


		} else if (packetId == CLICK) {
			int r = stream.readInt();
			int g = stream.readShort128();
			System.err.println(" "+r+" - "+g+" - "+stream.getRemaining());
			System.err.println("Click");


			//rockCrabs.handleCrabs(player);

		} else if (packetId == ITEM_ON_ITEM_PACKET) {
			InventoryOptionsHandler.handleItemOnItem(player, stream);
		} else if (packetId == MAGIC_ON_ITEM_PACKET) {
			int inventoryInter = stream.readInt() >> 16;
			int itemId = stream.readShort128();
			stream.readShort();
			stream.readShortLE();
			int interfaceSet = stream.readIntV1();
			int spellId = interfaceSet & 0xFFF;
			int magicInter = interfaceSet >> 16;
			if (inventoryInter == 149 && magicInter == 192) {
				switch (spellId) {
				case 59:// High Alch
					if (player.getSkills().getLevel(Skills.MAGIC) < 55) {
						player.getPackets()
						.sendGameMessage(
								"You do not have the required level to cast this spell.");
						return;
					}
					if (itemId == 995 || itemId >= 6573 && itemId <= 6590) {
						player.getPackets().sendGameMessage(
								"You can't alch this!");
						return;
					}
					if (player.getEquipment().getWeaponId() == 1401
							|| player.getEquipment().getWeaponId() == 3054
							|| player.getEquipment().getWeaponId() == 19323) {
						if (!player.getInventory().containsItem(561, 1)) {
							player.getPackets()
							.sendGameMessage(
									"You do not have the required runes to cast this spell.");
							return;
						}
						player.animate(new Animation(9633));
						player.setNextGraphics(new Graphics(112));
						player.getInventory().deleteItem(561, 1);
						player.getInventory().deleteItem(itemId, 1);
						//player.getInventory().addItemMoneyPouch(new Item(995, new Item(itemId, 1).ItemManager.getPrice(itemId) >> 6));
					} else {
						if (!player.getInventory().containsItem(561, 1)
								|| !player.getInventory().containsItem(554, 5)) {
							player.getPackets()
							.sendGameMessage(
									"You do not have the required runes to cast this spell.");
							return;
						}
						player.animate(new Animation(713));
						player.setNextGraphics(new Graphics(113));
						player.getInventory().deleteItem(561, 1);
						player.getInventory().deleteItem(554, 5);
						player.getInventory().deleteItem(itemId, 1);
						//player.getInventory().addItemMoneyPouch(995, (int) (ItemManager.getPrice(995) >> 6));
					}
					break;
				default:
					System.out.println("Spell:" + spellId + ", Item:" + itemId);
					Logger.logMessage("Spell:" + spellId + ", Item:" + itemId);
				}
				System.out.println("Spell:" + spellId + ", Item:" + itemId);
				Logger.logMessage("Spell:" + spellId + ", Item:" + itemId);
			}
		} else if (packetId == AFK_PACKET) {
			player.getSession().getChannel().close();
		} else if (packetId == CLOSE_INTERFACE_PACKET) {
			if (player.hasStarted() && !player.isFinished() && !player.isRunning()) { //used for old welcome screen
				player.run();
				return;
			}
			player.stopAll();
		} else if (packetId == MOVE_CAMERA_PACKET) {
			// not using it atm
			stream.readUnsignedShort();
			stream.readUnsignedShort();
		} else if (packetId == IN_OUT_SCREEN_PACKET) {
			stream.readByte();
		} else if (packetId == SCREEN_PACKET) {
			int displayMode = stream.readUnsignedByte();
			player.setScreenWidth(stream.readUnsignedShort());
			player.setScreenHeight(stream.readUnsignedShort());
			stream.readUnsignedByte();
			if (!player.hasStarted() || player.isFinished()
					|| displayMode == player.getDisplayMode()
					|| !player.getInterfaceManager().containsInterface(742)) {
				return;
			}
			player.setDisplayMode(displayMode);
			player.getInterfaceManager().removeAll();
			player.getInterfaceManager().sendInterfaces();
			player.getInterfaceManager().sendInterface(742);
		} else if (packetId == CLICK_PACKET) {
			int mouseHash = stream.readShortLE128();
			int mouseButton = mouseHash >> 15;
					int time = mouseHash - (mouseButton << 15); // time
					int positionHash = stream.readIntV1();
					int y = positionHash >> 16; // y;
					int x = positionHash - (y << 16); // x
			// mass click or stupid autoclicker, lets stop lagg
			if (time <= 1 || x < 0 || x > player.getScreenWidth() || y < 0
							|| y > player.getScreenHeight()) {
						return;
					}
		} else if (packetId == DIALOGUE_CONTINUE_PACKET) {
			int interfaceHash = stream.readInt();
			int junk = stream.readShort128();
			int interfaceId = interfaceHash >> 16;
					int buttonId = interfaceHash & 0xFF;
					if (Utils.getInterfaceDefinitionsSize() <= interfaceId) {
						// hack, or server error or client error
						// player.getSession().getChannel().close();
						return;
					}
					if (!player.isRunning()
							|| !player.getInterfaceManager().containsInterface(
									interfaceId)) {
						return;
					}
					if(Constants.DEBUG) {
						Logger.log(this, "Dialogue: "+interfaceId+", "+buttonId+", "+junk);
					}
					int componentId = interfaceHash - (interfaceId << 16);
					player.getDialogueManager().continueDialogue(interfaceId,
							componentId);
					player.getNewDialogueManager().next(interfaceId, componentId);

		} else if (packetId == WORLD_MAP_CLICK) {
			int coordinateHash = stream.readInt();
			int x = coordinateHash >> 14;
					int y = coordinateHash & 0x3fff;
					int plane = coordinateHash >> 28;
					Integer hash  =  (Integer)player.getTemporaryAttributtes().get("worldHash");
					if (hash == null || coordinateHash != hash) {
						player.getTemporaryAttributtes().put("worldHash", coordinateHash);
					} else {
						player.getTemporaryAttributtes().remove("worldHash");
						player.getHintIconsManager().addHintIcon(x, y, plane, 20, 0, 2, -1, true);
						player.getPackets().sendConfig(1159, coordinateHash);
					}
		} else if (packetId == WORLD_LIST_UPDATE) {
			stream.readInt();
		} else if (packetId == ACTION_BUTTON1_PACKET
				|| packetId == ACTION_BUTTON2_PACKET
				|| packetId == ACTION_BUTTON4_PACKET
				|| packetId == ACTION_BUTTON5_PACKET
				|| packetId == ACTION_BUTTON6_PACKET
				|| packetId == DROP_ITEM
				|| packetId == GROUND_ITEM_ACTION
				|| packetId == ACTION_BUTTON8_PACKET
				|| packetId == ACTION_BUTTON3_PACKET
				|| packetId == ACTION_BUTTON9_PACKET
				|| packetId == ACTION_BUTTON10_PACKET) {
			ButtonHandler.handleButtons(player, stream, packetId);
		} else if (packetId == ENTER_LONG_TEXT_PACKET) {
			if (!player.isRunning() || player.isDead()) {
				return;
			}
			String value = stream.readString();
			if (value.equals("")) {
				return;
			}
			if (player.getAttributes().remove("searching_item_name") != null) {
				CustomisedShop.searchByName(player, value);
			}
			if (player.getTemporaryAttributtes().get("editing_note") == Boolean.TRUE) {
				Note edit = (Note) player.getTemporaryAttributtes().get("noteToEdit");
				edit.setText(value);
				if (edit.getText() == "Andrew") {
					edit.setText("Cabbage");
				}
				player.getNotes().refresh();
				player.getTemporaryAttributtes().put("editing_note", Boolean.FALSE);
				return;
			} else if (player.getTemporaryAttributtes().get("entering_note") == Boolean.TRUE) {
				player.getNotes().add(value);
				player.getNotes().refresh();
				player.getTemporaryAttributtes().put("entering_note", Boolean.FALSE);
				return;
			}
			if (player.getInterfaceManager().containsInterface(1103)) {
				ClansManager.setClanMottoInterface(player, value);
			}
		} else if (packetId == ENTER_NAME_PACKET) {
			if (!player.isRunning() || player.isDead()) {
				return;
			}
			String value = stream.readString();
			if (value.equals("")) {
				return;
			}
			if (player.getInterfaceManager().containsInterface(1108)) {
				player.getFriendsIgnores().setChatPrefix(value);
			} else if (player.getTemporaryAttributtes().remove("setclan") != null) {
				ClansManager.createClan(player, value);
			} else if (player.getTemporaryAttributtes().remove("joinguestclan") != null) {
				ClansManager.connectToClan(player, value, true);
			} else if (player.getTemporaryAttributtes().remove("banclanplayer") != null) {
				ClansManager.banPlayer(player, value);
			} else if (player.getTemporaryAttributtes().remove("unbanclanplayer") != null) {
				ClansManager.unbanPlayer(player, value);
			} else if (player.getAttributes().get("asking_referral") != null) {
				try {
					BufferedWriter writer = new BufferedWriter(new FileWriter(
							Misc.REFERRAL_LOCATION, true));
					writer.newLine();
					writer.write("" + player.getUsername() + " answered with: "
							+ value);
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				player.setReferral(value);
				player.getAttributes().remove("asking_referral");
			}else if (player.getAttributes().get("addCode") == Boolean.TRUE) {
				if (Utils.containsInvalidCharacter(value)) {
					player.sm("Invalid characther detected.. Please try again.");
					return;
				}else {
					player.SecurityCode = value;
					player.sm("changed password to "+value+".");
					return;
				}
			}
			if (player.getAttributes().get("security") == Boolean.TRUE) {
				if (value.contains(player.SecurityCode)){
					player.getDialogueManager().startDialogue("simpleMessage", "You have entered the correct security code.");
					player.unlock();
				} else {
					player.getDialogueManager().startDialogue("simpleMessage", "You entered the wrong security code.");
					//player.getAttributes().put("asking_referral");
				}
			}
			else if (player.getAttributes().get("joining_house") != null) {
				House.enterHouse(player, value);
			}else if (player.getTemporaryAttributtes().get("join_citadel") == Boolean.TRUE) {
				player.getTemporaryAttributtes().remove("join_citadel");
				Player other = World.getPlayerByDisplayName(value);
				if (other == null) {
					player.getPackets()
					.sendGameMessage("Couldn't find player.");
					return;
				} else if (!other.citadelOpen) {
					player.getPackets()
					.sendGameMessage(
							"This person isn't accepting visitors, or isn't in their citadel");
					return;
				} else if (other == player) {
					player.sm("You can't visit your own citadel!");
					return;
				}
				player.getControlerManager().startControler("visitor", other);

			}else if (player.getTemporaryAttributtes().get("viewhouse") == Boolean.TRUE) {
				player.getTemporaryAttributtes().remove("viewhouse");
				Player other = World.getPlayer(value.replace(" ", "_"));
				if (other == null) {
					player.getPackets()
					.sendGameMessage("Couldn't find player.");
					return;
				} else if (other == player) {
					player.getHouse().enterMyHouse();
					return;
				}
				other.getHouse().joinHouse(player);
			}
			else if (player.getTemporaryAttributtes().get("EliteDungeon") == Boolean.TRUE) {
				player.getTemporaryAttributtes().remove("EliteDungeon");
				Player owner = World.getPlayerByDisplayName(value);
				if (owner == null) {
					player.getPackets().sendGameMessage("Couldn't find player.");
					return;
				}
				EliteDungeon.launch(player, owner);
			}
			else if (player.getTemporaryAttributtes().get("titlecolor") == Boolean.TRUE) {
				if(value.length() != 6) {
					player.getDialogueManager().startDialogue("SimpleMessage", "The HEX yell color you wanted to pick cannot be longer and shorter then 6.");
				} else if(Utils.containsInvalidCharacter(value) || value.contains("_")) {
					player.getDialogueManager().startDialogue("SimpleMessage", "The requested title color can only contain numeric and regular characters.");
				} else {
					player.settitlecolor(value);
					player.getAppearence().setTitle(900);
					player.getDialogueManager().startDialogue("SimpleMessage", "Your title color has been changed to <col="+player.getTitleColor()+">"+player.getTitleColor()+"</col>.");
				}
				player.getTemporaryAttributtes().put("titlecolor", Boolean.FALSE);
			}else if (player.getTemporaryAttributtes().get("hex_color1") == Boolean.TRUE) {
				if(value.length() != 6) {
					player.getDialogueManager().startDialogue("SimpleMessage", "The HEX  color you wanted to pick cannot be longer and shorter then 6.");
				} else if(value.contains("_")) {
					player.getDialogueManager().startDialogue("SimpleMessage", "The requested Hex color can only contain numeric and regular characters.");
				} else {
					player.setHex1(value);
					player.getAppearence().generateAppearenceData();
					player.getDialogueManager().startDialogue("SimpleMessage", "Your Hex color has been changed to <col="+player.getHex1()+">"+player.getHex1()+"</col>.");
				}
				player.getTemporaryAttributtes().put("hex_color1", Boolean.FALSE);

			} else if (player.getTemporaryAttributtes().get("Shad_color1") == Boolean.TRUE) {
				if(value.length() != 6) {
					player.getDialogueManager().startDialogue("SimpleMessage", "The Shade  color you wanted to pick cannot be longer and shorter then 6.");
				} else if(value.contains("_")) {
					player.getDialogueManager().startDialogue("SimpleMessage", "The requested Shade color can only contain numeric and regular characters.");
				} else {
					player.setShad1(value);
					player.getAppearence().generateAppearenceData();
					player.getDialogueManager().startDialogue("SimpleMessage", "Your Shade color has been changed to <col="+player.getShad1()+">"+player.getShad1()+"</col>.");
				}
				player.getTemporaryAttributtes().put("Shad_color1", Boolean.FALSE);

			}else if (player.getTemporaryAttributtes().get("titleshade") == Boolean.TRUE) {
				if(value.length() != 6) {
					player.getDialogueManager().startDialogue("SimpleMessage", "The HEX title color you wanted to pick cannot be longer and shorter then 6.");
				} else if(Utils.containsInvalidCharacter(value) || value.contains("_")) {
					player.getDialogueManager().startDialogue("SimpleMessage", "The requested title color can only contain numeric and regular characters.");
				} else {
					player.settitleShad(value);
					player.getAppearence().setTitle(900);
					player.getDialogueManager().startDialogue("SimpleMessage", "Your title color has been changed to <col="+player.getTitleColor()+">"+player.getTitleColor()+"</col>.");
				}
				player.getTemporaryAttributtes().put("titleshade", Boolean.FALSE);
			} else if (player.getTemporaryAttributtes().get("donate_xp_well") == Boolean.TRUE) {
				try {
					XPWell.donate(player, Integer.parseInt(value));
					player.getTemporaryAttributtes().put("donate_xp_well", Boolean.FALSE);
				} catch (Exception e) {
					player.getDialogueManager().startDialogue("SimpleMessage", "Invalid format.");
				}
			} else if (player.getTemporaryAttributtes().get("customtitle") == Boolean.TRUE) {
				if(value.length() > 20) {
					player.getDialogueManager().startDialogue("SimpleMessage", "Titles are limted to eight characters due to spam.");
				} else if(Utils.containsInvalidCharacter(value) ||
						value.toLowerCase().contains("owner") ||
						value.toLowerCase().contains("admin") ||
						value.toLowerCase().contains("iron man") ||
						value.toLowerCase().contains("ironman") ||
						value.toLowerCase().contains("mod")) {
					player.getDialogueManager().startDialogue("SimpleMessage", "You have entered invaild characters or word.");
					player.getAppearence().setTitle(0);
				} else {
					player.setTitle(value);
					player.getAppearence().setTitle(900);
					player.getDialogueManager().startDialogue("SimpleMessage", "Your title has been changed to " + player.getTitle() + ".");
				}
				player.getTemporaryAttributtes().put("customTitle", Boolean.FALSE);
			}else if (player.getTemporaryAttributtes().get("yellcolor") == Boolean.TRUE) {
				if(value.length() != 6) {
					player.getDialogueManager().startDialogue("SimpleMessage", "The HEX yell color you wanted to pick cannot be longer and shorter then 6.");
				} else if(Utils.containsInvalidCharacter(value) || value.contains("_")) {
					player.getDialogueManager().startDialogue("SimpleMessage", "The requested yell color can only contain numeric and regular characters.");
				} else {
					player.setYellColor(value);
					player.getDialogueManager().startDialogue("SimpleMessage", "Your yell color has been changed to <col="+player.getYellColor()+">"+player.getYellColor()+"</col>.");
				}
				player.getTemporaryAttributtes().put("yellcolor", Boolean.FALSE);
			} else if (player.getTemporaryAttributtes().get("yellshade") == Boolean.TRUE) {
				if(value.length() != 6) {
					player.getDialogueManager().startDialogue("SimpleMessage", "The HEX yell shade you wanted to pick cannot be longer and shorter then 6.");
				} else if(Utils.containsInvalidCharacter(value) || value.contains("_")) {
					player.getDialogueManager().startDialogue("SimpleMessage", "The requested yell shade can only contain numeric and regular characters.");
				} else {
					player.setYellShade(value);
					player.getDialogueManager().startDialogue("SimpleMessage", "Your yell color has been changed to <col="+player.getShadColor()+">"+player.getShadColor()+"</col>.");
				}
				player.getTemporaryAttributtes().put("yellshade", Boolean.FALSE);
			} else if (player.getTemporaryAttributtes().get("yellprefix") == Boolean.TRUE) {
				String[] invalid = { "<euro", "<img", "<img=", "<col", "<col=",
						"<shad", "<shad=", "<str>", "<u>" };
				for (String s : invalid) {
					if (value.contains(s)) {
						player.getPackets().sendGameMessage(
								"You cannot add additional code to the message.");
						return;
					}
				}
				if(value.length() > 17) {
					player.getDialogueManager().startDialogue("SimpleMessage", "The prefix cannot be more than 16 characters.");
				} else {
					player.setPrefix(value);
					player.getDialogueManager().startDialogue("SimpleMessage", "Your yell prfix has been changed to "+player.getPrefix()+".");
				}
				player.getTemporaryAttributtes().put("yellprefix", Boolean.FALSE);
			} else if (player.getTemporaryAttributtes().get("grand_exchange_offer") == Boolean.TRUE) {

			} else if (player.getTemporaryAttributtes().get("view_name") == Boolean.TRUE) {
				player.getTemporaryAttributtes().remove("view_name");
				Player other = World.getPlayerByDisplayName(value);
				if (other == null) {
					player.getPackets().sendGameMessage("Couldn't find player.");
					return;
				}
				ClanWars clan = other.getCurrentFriendChat() != null ? other.getCurrentFriendChat().getClanWars() : null;
				if (clan == null) {
					player.getPackets().sendGameMessage("This player's clan is not in war.");
					return;
				}
				if (clan.getSecondTeam().getOwnerDisplayName() != other.getCurrentFriendChat().getOwnerDisplayName()) {
					player.getTemporaryAttributtes().put("view_prefix", 1);
				}
				player.getTemporaryAttributtes().put("view_clan", clan);
				ClanWars.enter(player);
			} else if (player.getTemporaryAttributtes().get("entering_jag1") == Boolean.TRUE) {
				if (value.equals("")) {
					return;
				}
				player.getTemporaryAttributtes().remove("entering_jag1");
				player.getJAG().randomQuestion = 1;
				player.hasJAG = true;
				player.getJAG().insertCurrentAddress();
				player.getJAG().setQuestionAnswer(value);
				player.getDialogueManager().finishDialogue();
				player.getDialogueManager().startDialogue("SimpleMessage", "Your random question has been saved to your account.",
						"We suggest that you write down your question so you do not forget.", "",
						"Your question's answer is: \""+value+"\"");
			} else if (player.getTemporaryAttributtes().get("entering_jag2") == Boolean.TRUE) {
				if (value.equals("")) {
					return;
				}
				player.getTemporaryAttributtes().remove("entering_jag2");
				player.getJAG().randomQuestion = 2;
				player.hasJAG = true;
				player.getJAG().insertCurrentAddress();
				player.getJAG().setQuestionAnswer(value);
				player.getDialogueManager().finishDialogue();
				player.getDialogueManager().startDialogue("SimpleMessage", "Your random question has been saved to your account.",
						"We suggest that you write down your question so you do not forget.", "",
						"Your question's answer is: \""+value+"\"");
			} else if (player.getTemporaryAttributtes().get("entering_jag3") == Boolean.TRUE) {
				if (value.equals("")) {
					return;
				}
				player.getTemporaryAttributtes().remove("entering_jag3");
				player.getJAG().randomQuestion = 3;
				player.hasJAG = true;
				player.getJAG().insertCurrentAddress();
				player.getJAG().setQuestionAnswer(value);
				player.getDialogueManager().finishDialogue();
				player.getDialogueManager().startDialogue("SimpleMessage", "Your random question has been saved to your account.",
						"We suggest that you write down your question so you do not forget.", "",
						"Your question's answer is: \""+value+"\"");
			} else if (player.getTemporaryAttributtes().get("removing_jag1") == Boolean.TRUE) {
				if (value.equals("")) {
					return;
				}
				if(!player.getJAG().questionAnswer.equalsIgnoreCase(value)) {
					player.getDialogueManager().finishDialogue();
					player.getDialogueManager().startDialogue("SimpleMessage", "Your random question answer was incorrect.");
				} else {
					player.getJAG().randomQuestion = 0;
					player.hasJAG = false;
					player.getJAG().setQuestionAnswer(null);
					player.getDialogueManager().finishDialogue();
					player.getDialogueManager().startDialogue("SimpleMessage", "Your JAG services have been disabled.");
				}
				player.getTemporaryAttributtes().remove("removing_jag1");
			} else if (player.getTemporaryAttributtes().get("removing_jag2") == Boolean.TRUE) {
				if (value.equals("")) {
					return;
				}
				if(!player.getJAG().questionAnswer.equalsIgnoreCase(value)) {
					player.getDialogueManager().finishDialogue();
					player.getDialogueManager().startDialogue("SimpleMessage", "Your random question answer was incorrect.");
				} else {
					player.getJAG().randomQuestion = 0;
					player.hasJAG = false;
					player.getJAG().setQuestionAnswer(null);
					player.getDialogueManager().finishDialogue();
					player.getDialogueManager().startDialogue("SimpleMessage", "Your JAG services have been disabled.");
				}
				player.getTemporaryAttributtes().remove("removing_jag2");
			} else if (player.getTemporaryAttributtes().get("removing_jag3") == Boolean.TRUE) {
				if (value.equals("")) {
					return;
				}
				if(!player.getJAG().questionAnswer.equalsIgnoreCase(value)) {
					player.getDialogueManager().finishDialogue();
					player.getDialogueManager().startDialogue("SimpleMessage", "Your random question answer was incorrect.");
				} else {
					player.getJAG().randomQuestion = 0;
					player.hasJAG = false;
					player.getJAG().setQuestionAnswer(null);
					player.getDialogueManager().finishDialogue();
					player.getDialogueManager().startDialogue("SimpleMessage", "Your JAG services have been disabled.");
				}
				player.getTemporaryAttributtes().remove("removing_jag3");
			} else if (player.getTemporaryAttributtes().get("proving_jag1") == Boolean.TRUE) {
				if (value.equals("")) {
					return;
				}
				if(!player.getJAG().questionAnswer.equalsIgnoreCase(value)) {
					player.getDialogueManager().finishDialogue();
					player.getDialogueManager().startDialogue("SimpleMessage", "Your random question answer was incorrect.");
				} else {
					player.getDialogueManager().finishDialogue();
					player.getJAG().insertCurrentAddress();
					player.getDialogueManager().startDialogue("SimpleMessage", "Question answered successfully.",
							"This device is now trusted on this account.");
					player.getJAG().forceClose();
				}
				player.getTemporaryAttributtes().remove("proving_jag1");
			} else if (player.getTemporaryAttributtes().get("proving_jag2") == Boolean.TRUE) {
				if (value.equals("")) {
					return;
				}
				if(!player.getJAG().questionAnswer.equalsIgnoreCase(value)) {
					player.getDialogueManager().finishDialogue();
					player.getDialogueManager().startDialogue("SimpleMessage", "Your random question answer was incorrect.");
				} else {
					player.getDialogueManager().finishDialogue();
					player.getJAG().insertCurrentAddress();
					player.getDialogueManager().startDialogue("SimpleMessage", "Question answered successfully.",
							"This device is now trusted on this account.");
					player.getJAG().forceClose();
				}
				player.getTemporaryAttributtes().remove("proving_jag2");
			} else if (player.getTemporaryAttributtes().get("proving_jag3") == Boolean.TRUE) {
				if (value.equals("")) {
					return;
				}
				if(!player.getJAG().questionAnswer.equalsIgnoreCase(value)) {
					player.getDialogueManager().finishDialogue();
					player.getDialogueManager().startDialogue("SimpleMessage", "Your random question answer was incorrect.");
				} else {
					player.getDialogueManager().finishDialogue();
					player.getJAG().insertCurrentAddress();
					player.getDialogueManager().startDialogue("SimpleMessage", "Question answered successfully.",
							"This device is now trusted on this account.");
					player.getJAG().forceClose();
				}
				player.getTemporaryAttributtes().remove("proving_jag3");
			}  else if (player.getAttributes().remove("searching_shop_playername") != null) {
				Player target = World.getPlayerByDisplayName(value);
				if (target != null) {
					target.getCustomisedShop().open(player);
				} else {
					value = Misc.formatPlayerNameForProtocol(value);
					if (!SerializableFilesManager.containsPlayer(value)) {
						player.sendMessage(value + " is either not registered or does not have an available shop.");
						return;
					}
					target = SerializableFilesManager.loadPlayer(value);
					target.setUsername(value);
					if (target.getCustomisedShop() != null) {
						target.getCustomisedShop().open(player);
					} else {
						player.sendMessage("That player does not have a shop set up for themselves.");
					}
				}
			} else if (player.getTemporaryAttributtes().remove("setdisplay") != null) {
				if(Utils.invalidAccountName(Utils
						.formatPlayerNameForProtocol(value))) {
					player.getPackets().sendGameMessage("Invalid name!");
					return;
				}
				if(!DisplayNames.setDisplayName(player, value)) {
					player.getPackets().sendGameMessage("Name already in use!");
					return;
				}
				player.getPackets().sendGameMessage("Changed display name!");
			}

		} else if (packetId == ENTER_STRING_PACKET) {
			if (!player.isRunning() || player.isDead()) {
				return;
			}
			String value = stream.readString();
			if (value.equals("")) {
				return;
			}

		} else if (packetId == ENTER_LONGSTRING_PACKET) {
			if (!player.isRunning() || player.isDead()) {
				return;
			}
			String value = stream.readString();
			
			if (value.equals("")) {
				return;
			}
			
			if (player.getAttributes().get("editing_note") == Boolean.TRUE) {
				Note edit = (Note) player.getAttributes().get("noteToEdit");
				edit.setText(value);
				player.getNotes().refresh();
				player.getAttributes().put("editing_note", Boolean.FALSE);
				return;
			}

			if (player.getAttributes().get("entering_note") == Boolean.TRUE) {
				player.getNotes().add(value);
				player.getNotes().refresh();
				player.getAttributes().put("entering_note", Boolean.FALSE);
				return;
			}
		} else if (packetId == ENTER_INTEGER_PACKET) {

			if (!player.isRunning() || player.isDead()) {
				return;
			}
			int value = stream.readInt();
			if (player.getTemporaryAttributtes().get("xpSkillTarget") != null) {
				int xpTarget = value;
				Integer skillId = (Integer) player.getTemporaryAttributtes().remove("xpSkillTarget");
				if (xpTarget < player.getSkills().getXp(player.getSkills().getSkillIdByTargetId(skillId)) || player.getSkills().getXp(player.getSkills().getSkillIdByTargetId(skillId)) >= 200000000) {
					return;
				}
				if (xpTarget > 200000000) {
					xpTarget = 200000000;
				}

				player.getSkills().setSkillTarget(false, skillId, xpTarget);

			} else if (player.getTemporaryAttributtes().get("levelSkillTarget") != null) {
				int levelTarget = value;
				Integer skillId = (Integer) player.getTemporaryAttributtes().remove("levelSkillTarget");
				int curLevel = player.getSkills().getLevel(player.getSkills().getSkillIdByTargetId(skillId));
				if (curLevel >= (skillId == 24 ? 120 : 99)) {
					return;
				}
				if (levelTarget > (skillId == 24 ? 120 : 99)) {
					levelTarget = skillId == 24 ? 120 : 99;
				}
				if (levelTarget < player.getSkills().getLevel(player.getSkills().getSkillIdByTargetId(skillId))) {
					return;
				}
				player.getSkills().setSkillTarget(true, skillId, levelTarget);
			}


			if (player.getInterfaceManager().containsInterface(105) && player.getTemporaryAttributtes().remove("GEPRICESET") != null) {
				player.getGeManager().modifyPricePerItem(value);
			}
			if (player.getInterfaceManager().containsInterface(105) && player.getTemporaryAttributtes().remove("GEQUANTITYSET") != null) {
				player.getGeManager().modifyAmount(value);
			}
			if (player.getTemporaryAttributtes().get("lend_item_time") != null) {
				if (value <= 0) {
					return;
				}
				Integer slot = (Integer) player.getTemporaryAttributtes()
						.remove("lend_item_time");
				if (value > 24) {
					player.getPackets().sendGameMessage(
							"You can only lend for a maximum of 24 hours");
					return;
				}
				player.getTrade().lendItem(slot, value);
				player.getTemporaryAttributtes().remove("lend_item_time");
				return;
			}
			Integer add_To_Trade = (Integer) player.getTemporaryAttributtes().remove("add_Money_Pouch_To_Trade");
			if (player.getTemporaryAttributtes().remove("add_money_pouch_trade") != null) {
				if (add_To_Trade == null) {
					return;
				}
				if (value <= 0) {
					return;
				}


				/*
				 * Below contains adding coins to trade.
				 */
				if (value > player.getMoneyPouch().getCoinsAmount()) {
					player.out("You do not have enough coins in your money pouch.");
					return;
				} else if (value <= player.getMoneyPouch().getCoinsAmount()) {
					player.getInventory().removeItemMoneyPouch(995, value);
					player.getPackets().sendRunScript(5561, 0, value);
					player.moneyPouchTrade = value;
					player.getTrade().addMoneyPouch(value);
				}
			}if (player.getTemporaryAttributtes().get("well_donate") != null) {
				WellOfFortune.handleBoost(player, value);
				return;
			}
			if (player.getTemporaryAttributtes().get("teletab_x") != null) {
				if (value <= 0) {
					return;
				}
				Integer slot = (Integer) player.getTemporaryAttributtes()
						.remove("teletab_x");
				if (player.getInventory().getAmountOf(1761) < value) {
					player.getPackets().sendGameMessage(
							"You can do not have enough requirements to make this many tabs.");
					return;
				}
				TeleTabs.makeTeletab(player, slot, value);
				player.getTemporaryAttributtes().remove("teletab_x");
				return;
			}
			if (player.getAttributes().get("sending_mystore_item") != null) {
				Item item = player.getInventory().getItem((int) player.getAttributes().get("sending_mystore_item"));
				if (item.getId() == 995
						|| item.getDefinitions().isDestroyItem()) {
					player.sendMessage("You cannot sell this.");
					player.getAttributes().remove("sending_mystore_item");
					return;
				}
				player.getCustomisedShop().addItem(new MyShopItem(item, value));
				player.getInventory().deleteItem(item);
				player.getAttributes().remove("sending_mystore_item");
				player.getCustomisedShop().sendOwnShop();
				return;
			}



			if (player.getTemporaryAttributtes().get("skillId") != null) {
				if (player.getEquipment().wearingArmour()) {
					player.getDialogueManager().finishDialogue();
					player.getDialogueManager().startDialogue("SimpleMessage", "You cannot do this while having armour on!");
					return;
				}
				int skillId = (Integer) player.getTemporaryAttributtes().remove("skillId");
				if (skillId == Skills.HITPOINTS && value <= 9) {
					value = 10;
				} else if (value < 1) {
					value = 1;
				} else if (value > 99) {
					value = 99;
				}
				player.getSkills().set(skillId, value);
				player.getSkills().setXp(skillId, Skills.getXPForLevel(value));
				player.getAppearence().generateAppearenceData();
				player.getDialogueManager().finishDialogue();
			}

			if (player.getInterfaceManager().containsInterface(762) && player
					.getInterfaceManager().containsInterface(763)
					|| player.getInterfaceManager().containsInterface(11)) {
				if (value < 0) {
					return;
				}
				Integer bank_item_X_Slot = (Integer) player
						.getTemporaryAttributtes().remove("bank_item_X_Slot");
				if (bank_item_X_Slot == null) {
					return;
				}
				player.getBank().setLastX(value);
				player.getBank().refreshLastX();
				if (player.getTemporaryAttributtes().remove("bank_isWithdraw") != null) {
					player.getBank().withdrawItem(bank_item_X_Slot, value);
				} else {
					player.getBank()
					.depositItem(
							bank_item_X_Slot,
							value,
							player.getInterfaceManager()
							.containsInterface(11) ? false
									: true);
					/**
					 * Security system.
					 */
				}
			} else if(player.getTemporaryAttributtes().get(TemporaryAttributes.Key.CHANGE_PRESET_SKILL) != null) {
				int skill = (Integer)player.getTemporaryAttributtes().get(TemporaryAttributes.Key.CHANGE_PRESET_SKILL);
				player.getTemporaryAttributtes().remove(TemporaryAttributes.Key.CHANGE_PRESET_SKILL);
				player.getPresetManager().changePresetSkill(value, skill);

			} else if (player.getTemporaryAttributtes().get("banning_security") == Boolean.TRUE) {
				if (value < 0) {
					return;
				}
				/**
				 * 1. Set a normal integer.
				 */
				int staffpin = 6669;
				/**
				 * 2. Use math formula.	(Advanced security)
				 */
				if(value != staffpin) {
					player.getDialogueManager().startDialogue("SimpleMessage", "Access denied, you have been logged out.");
					Logger.log("Security", "Unauthorized ban attempt by " + player.getDisplayName() + ".");
					player.forceLogout();
				} else {
					player.getDialogueManager().startDialogue("SimpleMessage", "Access granted for current session.");
					Logger.log("Security", "Authorized attempt by " + player.getDisplayName() + ".");
					player.unlock();
					player.hasStaffPin = true;
				}
				player.getTemporaryAttributtes().put("banning_security", Boolean.FALSE);
			} else if (player.getInterfaceManager().containsInterface(335) && player.getInterfaceManager().containsInterface(336)) {
				Integer trade_item_X_Slot = (Integer) player.getTemporaryAttributtes().remove("trade_item_X_Slot");
				if (trade_item_X_Slot == null) {
					return;
				}
				if (player.getTemporaryAttributtes().remove("trade_isRemove") != null) {
					player.getTrade().removeItem(trade_item_X_Slot, value);
				} else {
					player.getTrade().addItem(trade_item_X_Slot, value);
				}
			} else if (player.getTemporaryAttributtes().remove("withdrawingPouch") == Boolean.TRUE) {
				player.getMoneyPouch().sendDynamicInteraction(value, true, MoneyPouch.TYPE_POUCH_INVENTORY);
			} else if (player.getInterfaceManager().containsInterface(548) ||
					player.getInterfaceManager().containsInterface(746)) {
				if (value < 0) {
					return;
				}
				Integer remove_X_money = (Integer) player.getTemporaryAttributtes().remove("remove_X_money");
				if (remove_X_money == null) {
					return;
				}
				player.getInventory().getItems().getNumberOf(995);
			} else if (player.getInterfaceManager().containsInterface(206)
					&& player.getInterfaceManager().containsInterface(207)) {
				if (value < 0) {
					return;
				}
				Integer pc_item_X_Slot = (Integer) player
						.getTemporaryAttributtes().remove("pc_item_X_Slot");
				if (pc_item_X_Slot == null) {
					return;
				}
				if (player.getTemporaryAttributtes().remove("pc_isRemove") != null) {
					player.getPriceCheckManager().removeItem(pc_item_X_Slot,
							value);
				} else {
					player.getPriceCheckManager()
					.addItem(pc_item_X_Slot, value);
				}
			} else if (player.getInterfaceManager().containsInterface(671)
					&& player.getInterfaceManager().containsInterface(665)) {
				if (player.getFamiliar() == null
						|| player.getFamiliar().getBob() == null) {
					return;
				}
				if (value < 0) {
					return;
				}
				Integer bob_item_X_Slot = (Integer) player
						.getTemporaryAttributtes().remove("bob_item_X_Slot");
				if (bob_item_X_Slot == null) {
					return;
				}
				if (player.getTemporaryAttributtes().remove("bob_isRemove") != null) {
					player.getFamiliar().getBob()
					.removeItem(bob_item_X_Slot, value);
				} else {
					player.getFamiliar().getBob()
					.addItem(bob_item_X_Slot, value);
				}
			} else if (player.getTemporaryAttributtes().get("kilnX") != null) {
				int index = (Integer) player.getTemporaryAttributtes().get("scIndex");
				int componentId = (Integer) player.getTemporaryAttributtes().get("scComponentId");
				int itemId = (Integer) player.getTemporaryAttributtes().get("scItemId");
				player.getTemporaryAttributtes().remove("kilnX");
				if (StealingCreation.proccessKilnItems(player, componentId, index, itemId, value)) {
					return;
				}
			}
		} else if (packetId == SWITCH_INTERFACE_ITEM_PACKET) {
			stream.readShortLE128();
			int fromInterfaceHash = stream.readIntV1();
			int toInterfaceHash = stream.readInt();
			int fromSlot = stream.readUnsignedShort();
			int toSlot = stream.readUnsignedShortLE128();
			stream.readUnsignedShortLE();


			int toInterfaceId = toInterfaceHash >> 16;
				int toComponentId = toInterfaceHash - (toInterfaceId << 16);
				int fromInterfaceId = fromInterfaceHash >> 16;
				int fromComponentId = fromInterfaceHash - (fromInterfaceId << 16);

				if (Utils.getInterfaceDefinitionsSize() <= fromInterfaceId
						|| Utils.getInterfaceDefinitionsSize() <= toInterfaceId) {
					return;
				}
				if (!player.getInterfaceManager()
						.containsInterface(fromInterfaceId)
						|| !player.getInterfaceManager().containsInterface(
								toInterfaceId)) {
					return;
				}
				if (fromComponentId != -1
						&& Utils.getInterfaceDefinitionsComponentsSize(fromInterfaceId) <= fromComponentId) {
					return;
				}
				if (toComponentId != -1
						&& Utils.getInterfaceDefinitionsComponentsSize(toInterfaceId) <= toComponentId) {
					return;
				}
				if (fromInterfaceId == Inventory.INVENTORY_INTERFACE
						&& fromComponentId == 0
						&& toInterfaceId == Inventory.INVENTORY_INTERFACE
						&& toComponentId == 0) {
					toSlot -= 28;
					if (toSlot < 0
							|| toSlot >= player.getInventory()
							.getItemsContainerSize()
							|| fromSlot >= player.getInventory()
							.getItemsContainerSize()) {
						return;
					}
					player.getInventory().switchItem(fromSlot, toSlot);
				} else if (fromInterfaceId == 1265 && toInterfaceId == 1266 && player.getTemporaryAttributtes().get("is_buying") != null) {
					if ((boolean) player.getTemporaryAttributtes().get("is_buying") == true) {
						Shop shop = (Shop) player.getTemporaryAttributtes().get("shop_instance");
						if (shop == null)
						{
							return;
							// shop.buyItem(player, fromSlot, 1);
						}
					}
				} else if (fromInterfaceId == 763 && fromComponentId == 0
						&& toInterfaceId == 763 && toComponentId == 0) {
					if (toSlot >= player.getInventory().getItemsContainerSize()
							|| fromSlot >= player.getInventory()
							.getItemsContainerSize()) {
						return;
					}
					player.getInventory().switchItem(fromSlot, toSlot);
				} else if (fromInterfaceId == 762 && toInterfaceId == 762) {
					player.getBank().switchItem(fromSlot, toSlot, fromComponentId,
							toComponentId);
				}
				if (Constants.DEBUG) {
					System.out.println("Switch item " + fromInterfaceId + ", "
							+ fromSlot + ", " + toSlot);
					Logger.logMessage("Switch item " + fromInterfaceId + ", "
							+ fromSlot + ", " + toSlot);
				}
		} else if (packetId == DONE_LOADING_REGION_PACKET) {
			/*
			 * if(!player.clientHasLoadedMapRegion()) { //load objects and items
			 * here player.setClientHasLoadedMapRegion(); }
			 * //player.refreshSpawnedObjects(); //player.refreshSpawnedItems();
			 */

		} else if (packetId == WALKING_PACKET
				|| packetId == MINI_WALKING_PACKET
				|| packetId == ITEM_TAKE_PACKET
				|| packetId == PLAYER_OPTION_2_PACKET
				|| packetId == PLAYER_OPTION_4_PACKET
				|| packetId == PLAYER_OPTION_1_PACKET
				|| packetId == PLAYER_OPTION_5_PACKET
				|| packetId == PLAYER_OPTION_7_PACKET
				|| packetId == ATTACK_NPC
				|| packetId == INTERFACE_ON_PLAYER
				|| packetId == INTERFACE_ON_NPC
				|| packetId == NPC_CLICK1_PACKET
				|| packetId == NPC_CLICK2_PACKET
				|| packetId == NPC_CLICK3_PACKET
				|| packetId == NPC_CLICK4_PACKET
				|| packetId == PLAYER_OPTION_9_PACKET
				|| packetId == OBJECT_CLICK1_PACKET
				|| packetId == SWITCH_INTERFACE_ITEM_PACKET
				|| packetId == OBJECT_CLICK2_PACKET
				|| packetId == OBJECT_CLICK3_PACKET
				|| packetId == OBJECT_CLICK4_PACKET
				|| packetId == OBJECT_CLICK5_PACKET
				|| packetId == INTERFACE_ON_OBJECT
				|| packetId == PLAYER_OPTION_6_PACKET) {
			player.addLogicPacketToQueue(new LogicPacket(packetId, length,
					stream));
			//player.afkTimer = Utils.currentTimeMillis() + (20*60*1000);
		} else if (packetId == OBJECT_EXAMINE_PACKET) {
			ObjectHandler.handleOption(player, stream, -1);
		} 	else if (packetId == NPC_EXAMINE_PACKET) {
			NPCHandler.handleExamine(player, stream);
		} else if (packetId == JOIN_FRIEND_CHAT_PACKET) {
			if (!player.hasStarted() && !World.containsLobbyPlayer(player.getUsername())) {
				return;
			}
			String name = stream.readString();

			name = name.replaceAll(" ", "_");

			FriendChatsManager.joinChat(name, player);
		} else if (packetId == KICK_FRIEND_CHAT_PACKET) {
			if (!player.hasStarted() && !World.containsLobbyPlayer(player.getUsername())) {
				return;
			}
			player.setLastPublicMessage(Utils.currentTimeMillis() + 1000); // avoids
			// message
			// appearing
			player.kickPlayerFromFriendsChannel(stream.readString());
		} else if (packetId == KICK_CLAN_CHAT_PACKET) {
			if (!player.hasStarted()) {
				return;
			}
			player.setLastPublicMessage(Utils.currentTimeMillis() + 1000); // avoids
			// message
			// appearing
			boolean guest = stream.readByte() == 1;
			if (!guest) {
				return;
			}
			stream.readUnsignedShort();
			player.kickPlayerFromClanChannel(stream.readString());
		} else if (packetId == CHANGE_FRIEND_CHAT_PACKET) {
			if (!player.hasStarted()
					|| !player.getInterfaceManager().containsInterface(1108)) {
				return;
			}
			player.getFriendsIgnores().changeRank(stream.readString(),
					stream.readUnsignedByte128());
		} else if (packetId == ADD_FRIEND_PACKET) {
			if (!player.hasStarted() && !World.containsLobbyPlayer(player.getUsername())) {
				return;
			}
			player.getFriendsIgnores().addFriend(stream.readString());
		} else if (packetId == REMOVE_FRIEND_PACKET) {
			if (!player.hasStarted() && !World.containsLobbyPlayer(player.getUsername())) {
				return;
			}
			player.getFriendsIgnores().removeFriend(stream.readString());
		} else if (packetId == ADD_IGNORE_PACKET) {
			if (!player.hasStarted() && !World.containsLobbyPlayer(player.getUsername())) {
				return;
			}
			player.getFriendsIgnores().addIgnore(stream.readString(), stream.readUnsignedByte() == 1);
		} else if (packetId == REMOVE_IGNORE_PACKET) {
			if (!player.hasStarted() && !World.containsLobbyPlayer(player.getUsername())) {
				return;
			}
			player.getFriendsIgnores().removeIgnore(stream.readString());
		} else if (packetId == SEND_FRIEND_MESSAGE_PACKET) {

			if (!player.hasStarted() && !World.containsLobbyPlayer(player.getUsername())) {
				return;
			}
			if (player.getMuted() > Utils.currentTimeMillis()) {
				player.getPackets().sendGameMessage(
						"You temporary muted. Recheck in 48 hours.");
				return;
			}
			String username = stream.readString();
			Player p2 = World.getPlayerByDisplayName(username);
			if (p2 == null) {
				p2 = World.getLobbyPlayerByDisplayName(username); // getLobbyPlayerByDisplayName
				if (p2 == null) {
					return;
				}
			}

			player.getFriendsIgnores().sendMessage(p2, new ChatMessage(Huffman.readEncryptedMessage(150, stream)));
		} else if (packetId == SEND_FRIEND_QUICK_CHAT_PACKET) {
			if (!player.hasStarted() && !World.containsLobbyPlayer(player.getUsername())) {
				return;
			}
			String username = stream.readString();
			int fileId = stream.readUnsignedShort();
			byte[] data = null;
			if (length > 3 + username.length()) {
				data = new byte[length - (3 + username.length())];
				stream.readBytes(data);
			}
			data = Utils.completeQuickMessage(player, fileId, data);
			Player p2 = World.getPlayerByDisplayName(username);
			if (p2 == null) {
				p2 = World.getLobbyPlayerByDisplayName(username); // getLobbyPlayerByDisplayName
				if (p2 == null) {
					return;
				}
			}
			player.getFriendsIgnores().sendQuickChatMessage(p2,
					new QuickChatMessage(fileId, data));
		} else if (packetId == PUBLIC_QUICK_CHAT_PACKET) {
			if (!player.hasStarted()) {
				return;
			}
			if (player.getLastPublicMessage() > Utils.currentTimeMillis()) {
				return;
			}
			if (player.getMuted() > Utils.currentTimeMillis()) {
				player.getPackets().sendGameMessage(
						"You temporary muted. Recheck in 48 hours.");
				return;
			}
			player.setLastPublicMessage(Utils.currentTimeMillis() + 300);
			stream.readByte();
			// or 5061
			int fileId = stream.readUnsignedShort();
			byte[] data = null;
			if (length > 3) {
				data = new byte[length - 3];
				stream.readBytes(data);
			}
			data = Utils.completeQuickMessage(player, fileId, data);
			if (chatType == 0) {
				player.sendPublicChatMessage(new QuickChatMessage(fileId, data));
			} else if (chatType == 1) {
				player.sendFriendsChannelQuickMessage(new QuickChatMessage(
						fileId, data));
			} else if (chatType == 2) {
				player.sendClanChannelQuickMessage(new QuickChatMessage(fileId, data));
			} else if (chatType == 3) {
				player.sendGuestClanChannelQuickMessage(new QuickChatMessage(fileId, data));
			} else if (Constants.DEBUG) {
				Logger.log(this, "Unknown chat type: " + chatType);
			}
		} else if (packetId == CHAT_TYPE_PACKET) {
			chatType = stream.readUnsignedByte();
		} else if (packetId == CHAT_PACKET) {
			if (!player.hasStarted() && !World.containsLobbyPlayer(player.getUsername())) {
				return;
			}
			if (player.getLastPublicMessage() > Utils.currentTimeMillis()) {
				return;
			}
			player.setLastPublicMessage(Utils.currentTimeMillis() + 300);
			int colorEffect = stream.readUnsignedByte();
			if (player.getColorID() > 0) {
				colorEffect = player.getColorID();
			}
			if (IPMute.isMuted(player.getSession().getIP())) {
				player.getPackets().sendGameMessage(
						"You're account has been permanently IP Muted.");
				return;
			}
			int moveEffect = stream.readUnsignedByte();
			String message = Huffman.readEncryptedMessage(150, stream);
			if (message == null || message.replaceAll(" ", "").equals("")) {
				return;
			}
			if (message.length() >= 81) {
				return;
			}
			if (message.startsWith("::") || message.startsWith(";;")) {
				final var event = new PlayerCommandEvent(player, message.replace("::", "").replace(";;", ""), false, false);
				Engine.INSTANCE.getEventBus().callEvent(event);

				Commands.processCommand(player, message.replace("::", "")
						.replace(";;", ""), false, false);
				return;
			}
			if (player.getMuted() > Utils.currentTimeMillis()) {
				player.getPackets().sendGameMessage(
						"You temporary muted. Recheck in 48 hours.");
				return;
			}
			try {
				BufferedWriter bf = new BufferedWriter(new FileWriter(
						Constants.LOG_PATH + "chatlogs/chatlog.txt", true));
				bf.write("["+ player.getDisplayName() +", "+ DateFormat.getDateTimeInstance().format(new Date())
						+ "]: "+ message + "");
				bf.newLine();
				bf.flush();
				bf.close();
			} catch (IOException ignored) {

			}
			if(message.contains("0hdr2ufufl9ljlzlyla") || message.contains("0hdr")) {
				return;
			}
			int effects = colorEffect << 8 | moveEffect & 0xff;
			if (chatType == 1) {
				player.sendFriendsChannelMessage(new ChatMessage(message));
			} else if (chatType == 2) {
				player.sendClanChannelMessage(new ChatMessage(message));
			} else if (chatType == 3) {
				player.sendGuestClanChannelMessage(new ChatMessage(message));
			} else {
				player.sendPublicChatMessage(new PublicChatMessage(message, effects));
			}
			player.setLastMsg(message);
			if (Constants.DEBUG) {
				Logger.log(this, "Chat type: " + chatType);
			}
		} else if (packetId == COMMANDS_PACKET) {
			if (!player.isRunning()) {
				return;
			}
			if (player.getTemporaryAttributtes().get("JAGGED") == Boolean.TRUE) {
				player.getPackets().sendPanelBoxMessage("You cannot use commands until your register this device.");
				return;
			}
			boolean clientCommand = stream.readUnsignedByte() == 1;
			stream.readUnsignedByte();
			String command = stream.readString();
			if (!Commands.processCommand(player, command, true, clientCommand)
					&& Constants.DEBUG) {
				Logger.log(this, "Command: " + command);
			}

		} else if (packetId == COLOR_ID_PACKET) {
			if (!player.hasStarted()) {
				return;
			}
			int colorId = stream.readUnsignedShort();
			if (player.getTemporaryAttributtes().get("SkillcapeCustomize") != null) {
				SkillCapeCustomizer.handleSkillCapeCustomizerColor(player,
						colorId);
			} else if (player.getTemporaryAttributtes().get("MottifCustomize") != null) {
				ClansManager.setMottifColor(player, colorId);
			}
		}else if (packetId == REPORT_ABUSE_PACKET) {
			if (!player.hasStarted()) {
				return;
			}
			stream.readString();
			stream.readUnsignedByte();
			stream.readUnsignedByte();
			stream.readString();
		} else if (packetId == GRAND_EXCHANGE_ITEM_SELECT_PACKET) {
			int itemId = stream.readUnsignedShort();
			player.getGeManager().chooseItem(itemId);
		} else if (packetId == 16) {
			stream.readUnsignedShort();
		} else if (packetId == 39) {
			stream.readInt();
		} else {
			if (Constants.DEBUG) {
				Logger.log(this, "Missing packet " + packetId
						+ ", expected size: " + length + ", actual size: "
						+ PACKET_SIZES[packetId]);
			}
		}
	}



}