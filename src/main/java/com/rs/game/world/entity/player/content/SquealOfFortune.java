package com.rs.game.world.entity.player.content;

import java.io.Serializable;

import com.rs.Constants;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.DonatorRanks;
import com.rs.game.world.entity.player.Player;
import com.rs.utility.Utils;

public class SquealOfFortune implements Serializable {

	private static final long serialVersionUID = -5330047553089876572L;

	private static final int RARITY_COMMON = 0;
	private static final int RARITY_UNCOMMON = 1;
	private static final int RARITY_RARE = 2;
	private static final int RARITY_JACKPOT = 3;

	private static final int SPIN_TYPE_DAILY = 0;
	private static final int SPIN_TYPE_EARNED = 1;
	private static final int SPIN_TYPE_BOUGHT = 2;

	private static final int SOF_STATUS_CLAIMINVOK = 1;
	private static final int SOF_STATUS_CLAIMINVBAD = 2;
	private static final int SOF_STATUS_CLAIMBANKOK = 3;
	private static final int SOF_STATUS_CLAIMBANKBAD = 4;
	private static final int SOF_STATUS_CLAIMPOUCHOK = 5;
	private static final int SOF_STATUS_CLAIMPOUCHBAD = 6;
	private static final int SOF_STATUS_DISABLED = 7;

	private transient Player player;

	private long lastDailySpinsGiveaway;

	private int dailySpins;
	private int earnedSpins;
	private int boughtSpins;

	private Item item;

	private int rewardSlot;
	private int jackpotSlot;
	private Item[] rewards;

	public SquealOfFortune() {
		// 0s are defaults anyway ^.^
		/*
		 * lastDailySpinsGiveaway = 0; dailySpins = 0; earnedSpins = 0; boughtSpins = 0;
		 */
		rewardSlot = -1;
		jackpotSlot = -1;
	}

	public void setPlayer(Player player) {
		this.player = player;

	}

	public void processClick(int packetId, int interfaceId, int componentId, int e1, int e2) {
		if (interfaceId == 1139) { // squeal tab
			if (componentId == 18) {
				openSpinInterface();
			} else {
				player.getPackets().sendOpenURL(Constants.BUY_SPINS_LINK);
			}
		} else if (interfaceId == 1252) { // squeal overlay
			if (componentId == 5) {
				player.getPackets().sendGameMessage(
						"<col=8B0000>[SoF]</col> - You can access the Squeal of Fortune from the side panel. You can show the button again by re-logging.");
				player.getInterfaceManager().closeSquealOverlay();
			} else {
				openSpinInterface();
			}
		} else if (interfaceId == 1253) { // squeal main
			if (componentId == 106 || componentId == 258) { // hide/close button
				player.getInterfaceManager().sendWindowPane();
			} else if (componentId == 7 || componentId == 321) { // buy spins on main/reward
				player.getPackets().sendOpenURL(Constants.BUY_SPINS_LINK);
			} else if (componentId == 93 && jackpotSlot != -1 && rewardSlot == -1) { // spin button
				player.spinsCount++;
				pickReward();
			}
			// else if (componentId == 93 && rewardSlot != -1) { // they double clicked spin
			// button
			// in rs, it was made that if you double click the spin button it will show
			// rewards instantly
			// player.getPackets().sendGlobalConfig(1781, -1); // disable spinning
			// player.getPackets().sendRunScript(5906); // force call to sof_displayPrize();
			// }
			else if ((componentId == 192 || componentId == 239) && rewardSlot != -1) { // picking reward
				obtainReward(componentId == 239);
			} else if (componentId == 273 && jackpotSlot == -1 && getTotalSpins() > 0) { // play again
				generateRewards(getNextSpinType());
				player.getVarsManager().forceSendVarBit(11155, jackpotSlot + 1);
				player.getPackets().sendItems(665, rewards);
				player.getVarsManager().forceSendVarBit(10861, 0);
				player.getPackets().sendRunScript(5879); // sof_setupHooks();
				sendSpinCounts();
			}
		}
	}

	public void processItemClick(int slotId, int itemId, Item item) {
		if (itemId == 24154 || itemId == 24155) { // spin ticket and double spin ticket
			player.getInventory().deleteItem(item);
			giveEarnedSpins(itemId == 24154 ? 1 : 2);
		}
	}

	private void pickReward() {
		if (!useSpin()) {
			return;
		}
		int rewardRarity = RARITY_COMMON;
		double roll = Utils.randomDouble();
		if (roll <= Constants.SOF_CHANCES[RARITY_JACKPOT]) {
			rewardRarity = RARITY_JACKPOT; // we have a winner here...
		} else if (roll <= Constants.SOF_CHANCES[RARITY_RARE]) {
			rewardRarity = RARITY_RARE;
		} else if (roll <= Constants.SOF_CHANCES[RARITY_UNCOMMON]) {
			rewardRarity = RARITY_UNCOMMON;
		}

		int[] possibleSlots = new int[13];
		int possibleSlotsCount = 0;
		for (int i = 0; i < 13; i++) {
			if (getSlotRarity(i, jackpotSlot) == rewardRarity) {
				possibleSlots[possibleSlotsCount++] = i;
			}
		}

		rewardSlot = possibleSlots[Utils.random(possibleSlotsCount)];

		if (rewardRarity >= RARITY_RARE) {
			announceWin();
		}

		player.getVarsManager().forceSendVarBit(10860, rewardSlot);
		player.getVarsManager().forceSendVarBit(10861, 1); // block spin & set reward
		player.getPackets().sendGlobalConfig(1790, getRewardStatusType());
		player.getPackets().sendGlobalConfig(1781, getBestRewardSpoofSlot());

	}

	private void obtainReward(boolean discard) {
		int type = getRewardStatusType();

		if (discard && type == SOF_STATUS_DISABLED || !discard && type != SOF_STATUS_CLAIMINVOK
				&& type != SOF_STATUS_CLAIMPOUCHOK && type != SOF_STATUS_CLAIMBANKOK) {
			return;
		}

		player.getVarsManager().forceSendVarBit(10861, 0); // prepare for next spin
		player.getPackets().sendGlobalConfig(1790, 0);
		player.getPackets().sendItems(665, new Item[13]);

		if (!discard) {
			if (type == SOF_STATUS_CLAIMPOUCHOK) {
				player.getInventory().addItemMoneyPouch(rewards[rewardSlot]);
			} else if (type == SOF_STATUS_CLAIMINVOK) {
				player.getInventory().addItem(rewards[rewardSlot]);
			} else if (type == SOF_STATUS_CLAIMBANKOK) {
				player.getBank().addItem(rewards[rewardSlot].getId(), rewards[rewardSlot].getAmount(), false);
			}
		}

		rewards = null;
		jackpotSlot = -1;
		rewardSlot = -1;
	}

	private void announceWin() {
		Item item = rewards[rewardSlot];
		if (item.getDefinitions().isStackable() || item.getDefinitions().isNoted() || item.getAmount() > 1) {
			World.sendWorldMessage("<col=8B0000>[SoF - News]</col> - <col=8B0000>" + player.getDisplayName()
					+ "</col> has just won " + item.getAmount() + " x " + item.getName() + " on Squeal of Fortune!",
					false);
		} else {
			World.sendWorldMessage("<col=8B0000>[SoF - News]</col> - <col=8B0000>" + player.getDisplayName()
					+ "</col> has just won " + item.getName() + " on Squeal of Fortune!", false);
		}
	}

	public void openSpinInterface() {
		if (player.getInterfaceManager().containsInventoryInter()
				|| player.getInterfaceManager().containsScreenInter()) {
			player.getPackets().sendGameMessage("Please finish what you are doing before opening Squeal of Fortune.");
			return;
		}

		player.stopAll();

		sendSpinCounts();
		if (rewardSlot != -1) {
			openExistingReward();
		} else if (getTotalSpins() < 1) {
			openNoSpinsLeft();
		} else {
			openSpin();
		}
	}

	private void openExistingReward() {
		player.getVarsManager().forceSendVarBit(11155, jackpotSlot + 1); // need to send all items because otherwise it
		// will set wrong color for rarity etc
		player.getPackets().sendItems(665, rewards);
		player.getPackets().sendRootInterface(1253, 0);
		player.getVarsManager().forceSendVarBit(10860, rewardSlot);
		player.getVarsManager().forceSendVarBit(10861, 1); // block spin & set reward
		player.getPackets().sendGlobalConfig(1790, getRewardStatusType());
		player.getPackets().sendRunScript(5906); // force call to sof_displayPrize();

	}

	private void openNoSpinsLeft() {
		player.getVarsManager().forceSendVarBit(11155, Utils.random(13) + 1);
		player.getPackets().sendItems(665, new Item[13]);
		player.getPackets().sendRootInterface(1253, 0);
		player.getVarsManager().forceSendVarBit(10861, 0);
		player.getPackets().sendGlobalConfig(1790, 0);
		player.getPackets().sendRunScript(5906); // force call to sof_displayPrize();
	}

	private void openSpin() {
		if (rewards == null) {
			generateRewards(getNextSpinType());
		}

		player.getVarsManager().forceSendVarBit(11155, jackpotSlot + 1);
		player.getPackets().sendItems(665, rewards);
		player.getPackets().sendRootInterface(1253, 0);
		player.getVarsManager().forceSendVarBit(10861, 0); // force allow spin
	}

	public void sendSpinCounts() {
		player.getVarsManager().forceSendVarBit(10862, dailySpins);
		player.getVarsManager().forceSendVarBit(11026, earnedSpins);
		player.getPackets().sendGlobalConfig(1800, boughtSpins);
		// must send all three otherwise it wont trigger refresh code @ cs2
	}

	private void generateRewards(int spinType) {
		jackpotSlot = Utils.random(13);
		rewards = new Item[13];
		for (int i = 0; i < rewards.length; i++) {
			rewards[i] = generateReward(spinType, getSlotRarity(i, jackpotSlot));
		}
	}

	private Item generateReward(int spinType, int rarityType) {
		// TODO different rewards depending on spin type.
		boolean isLamp = Utils.random(3) == 0; // lamp 1/3 others 2/3
		if (isLamp) {
			int[] lamps = Constants.SOF_COMMON_LAMPS;
			if (rarityType == RARITY_JACKPOT) {
				lamps = Constants.SOF_JACKPOT_LAMPS;
			} else if (rarityType == RARITY_RARE) {
				lamps = Constants.SOF_RARE_LAMPS;
			} else if (rarityType == RARITY_UNCOMMON) {
				lamps = Constants.SOF_UNCOMMON_LAMPS;
			}

			return new Item(lamps[Utils.random(lamps.length)], 1);
		} else {
			int[] items = Constants.SOF_COMMON_OTHERS;
			if (rarityType == RARITY_JACKPOT) {
				items = Constants.SOF_JACKPOT_OTHERS;
			} else if (rarityType == RARITY_RARE) {
				items = Constants.SOF_RARE_OTHERS;
			} else if (rarityType == RARITY_UNCOMMON) {
				items = Constants.SOF_UNCOMMON_OTHERS;
			}

			int itemId = items[Utils.random(items.length)];
			int amount;

			if (itemId == 995) {
				int[] amounts = Constants.SOF_COMMON_CASH_AMOUNTS;
				if (rarityType == RARITY_JACKPOT) {
					amounts = Constants.SOF_JACKPOT_CASH_AMOUNTS;
				} else if (rarityType == RARITY_RARE) {
					amounts = Constants.SOF_RARE_CASH_AMOUNTS;
				} else if (rarityType == RARITY_UNCOMMON) {
					amounts = Constants.SOF_UNCOMMON_CASH_AMOUNTS;
				}
				amount = amounts[Utils.random(amounts.length)];
			} else {
				ItemDefinitions defs = ItemDefinitions.getItemDefinitions(itemId);
				amount = rarityType > RARITY_COMMON || !defs.isStackable() && !defs.isNoted() ? 1
						: Utils.random(40) + 10;
			}

			return new Item(itemId, amount);
		}
	}

	private int getRewardStatusType() {
		if (!Constants.SQUEAL_OF_FORTUNE_ENABLED) {
			return SOF_STATUS_DISABLED;
		}

		Item reward = rewards[rewardSlot];
		if (reward.getId() == 995) { // coins go to pouch
			long amt = player.getMoneyPouch().getCoinsAmount() + reward.getAmount();
			return amt > Integer.MAX_VALUE || amt <= 0 ? SOF_STATUS_CLAIMPOUCHBAD : SOF_STATUS_CLAIMPOUCHOK;
		} else if (!reward.getDefinitions().isStackable() && !reward.getDefinitions().isNoted()
				&& reward.getAmount() == 1) { // non stackable items to inv
			return player.getInventory().hasFreeSlots() ? SOF_STATUS_CLAIMINVOK : SOF_STATUS_CLAIMINVBAD;
		} else { // other items go to bank
			if (player.getBank().getItem(reward.getId()) != null) {
				long amt = player.getBank().getItem(reward.getId()).getAmount() + reward.getAmount();
				return amt > Integer.MAX_VALUE || amt <= 0 ? SOF_STATUS_CLAIMBANKBAD : SOF_STATUS_CLAIMBANKOK;
			} else {
				return player.getBank().hasBankSpace() ? SOF_STATUS_CLAIMBANKOK : SOF_STATUS_CLAIMBANKBAD;
			}
		}
	}

	private int getBestRewardSpoofSlot() {
		int wonRarity = getSlotRarity(rewardSlot, jackpotSlot);
		if (wonRarity == RARITY_JACKPOT)
		{
			return rewardSlot; // nothing to spoof
		}
		int spoofMinType = wonRarity == RARITY_RARE ? RARITY_JACKPOT : RARITY_RARE;
		int bestSlot = -1;
		int bestDistance = Integer.MAX_VALUE;
		for (int i = 0; i < 13; i++) {
			if (i == rewardSlot || getSlotRarity(i, jackpotSlot) < spoofMinType)
			{
				continue; // skip self & not rare
			}
			int distance = distanceTo(i, rewardSlot);
			if (bestSlot == -1 || distance < bestDistance) {
				bestSlot = i;
				bestDistance = distance;
			}
		}

		return bestSlot;
	}

	private int distanceTo(int from, int to) {
		if (from == to) {
			return 0;
		} else if (from > to) {
			return 13 - from + to;
		} else {
			return to - from;
		}
	}

	private int getSlotRarity(int slot, int jackpotSlot) {
		if (slot == jackpotSlot) {
			return RARITY_JACKPOT;
		}
		switch (slot) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 10:
		case 12:
			return RARITY_COMMON;
		case 2:
		case 6:
		case 9:
		case 11:
			return RARITY_UNCOMMON;
		case 0:
		case 4:
		case 8:
			return RARITY_RARE;
		default: // default case added so compiler can add tableswitch instruction instead of
			// lookupswitch
			throw new RuntimeException("Bad slot");
		}
	}

	private int getTotalSpins() {
		return dailySpins + earnedSpins + boughtSpins;
	}

	private int getNextSpinType() {
		if (dailySpins > 0) {
			return SPIN_TYPE_DAILY;
		} else if (earnedSpins > 0) {
			return SPIN_TYPE_EARNED;
		} else if (boughtSpins > 0) {
			return SPIN_TYPE_BOUGHT;
		} else {
			return -1;
		}
	}

	private boolean useSpin() {
		int type = getNextSpinType();
		if (type == -1) {
			return false;
		}

		if (type == SPIN_TYPE_DAILY) {
			setDailySpins(dailySpins - 1);
		} else if (type == SPIN_TYPE_EARNED) {
			setEarnedSpins(earnedSpins - 1);
		} else if (type == SPIN_TYPE_BOUGHT) {
			setBoughtSpins(boughtSpins - 1);
		}

		return true;
	}

	/**
	 * Give's daily spins for donators, only if daily spins < 2.
	 */
	public void giveDailySpins() {
		if (Utils.currentTimeMillis() - lastDailySpinsGiveaway < 12 * 60 * 60 * 1000) {
			return;
		}
		lastDailySpinsGiveaway = Utils.currentTimeMillis();
		int previous = dailySpins;
		dailySpins += player.getGameMode().getExtraSpins();
		if (player.getDonationManager().hasRank(DonatorRanks.DRAGON)) {
			dailySpins += 8;
		}
		else if (player.getDonationManager().hasRank(DonatorRanks.RUNE)) {
			dailySpins += 7;
		}
		else if (player.getDonationManager().hasRank(DonatorRanks.ADAMANT)) {
			dailySpins += 6;
		}
		else if (player.getDonationManager().hasRank(DonatorRanks.MITHRIL)) {
			dailySpins += 5;
		}
		else if (player.getDonationManager().hasRank(DonatorRanks.STEEL)) {
			dailySpins += 4;
		}
		else if (player.getDonationManager().hasRank(DonatorRanks.IRON)) {
			dailySpins += 3;
		}
		else if (player.getDonationManager().hasRank(DonatorRanks.BRONZE)) {
			dailySpins += 2;
		}
		if (dailySpins > previous) {
			player.sendMessage("<col=FF0000>You have been given your daily spins!");
			player.sendMessage("<col=FF0000>Click on the squeal of fortune tab to use them.");
		}

		if (dailySpins != previous) {
			sendSpinCounts();
		}
	}

	public void giveEarnedSpins(int amount) {
		int previous = earnedSpins;
		earnedSpins += amount;
		if (earnedSpins > previous) {
			player.getPackets().sendGameMessage("[<col=8B0000>Daily - Squeal</col>] - You have earned <col=8B0000>"
					+ (earnedSpins - previous) + "</col> squeal of fortune spins.");
			player.getPackets().sendGameMessage(
					"[<col=8B0000>Daily - Squeal</col>] - Click on the squeal of fortune tab to use them.");
		}

		if (earnedSpins != previous) {
			sendSpinCounts();
		}
	}

	public void giveBoughtSpins(int amount) {
		int previous = boughtSpins;
		boughtSpins += amount;
		if (boughtSpins > previous) {
			player.getPackets().sendGameMessage("[<col=8B0000>Daily - Squeal</col>] - You have bought <col=8B0000>"
					+ (boughtSpins - previous) + "</col> squeal of fortune spins.");
			player.getPackets().sendGameMessage(
					"[<col=8B0000>Daily - Squeal</col>] - Click on the squeal of fortune tab to use them.");
		}

		if (boughtSpins != previous) {
			sendSpinCounts();
		}
	}

	public void resetSpins() {
		dailySpins = 0;
		earnedSpins = 0;
		boughtSpins = 0;
		sendSpinCounts();
	}

	public int getDailySpins() {
		return dailySpins;
	}

	public void setDailySpins(int dailySpins) {
		int previous = this.dailySpins;
		this.dailySpins = dailySpins;
		if (this.dailySpins != previous) {
			sendSpinCounts();
		}
	}

	public int getEarnedSpins() {
		return earnedSpins;
	}

	public void setEarnedSpins(int earnedSpins) {
		int previous = this.earnedSpins;
		this.earnedSpins = earnedSpins;
		if (this.earnedSpins != previous) {
			sendSpinCounts();
		}
	}

	public int getBoughtSpins() {
		return boughtSpins;
	}

	public void setBoughtSpins(int boughtSpins) {
		int previous = this.boughtSpins;
		this.boughtSpins = boughtSpins;
		if (this.boughtSpins != previous) {
			sendSpinCounts();
		}
	}

}
