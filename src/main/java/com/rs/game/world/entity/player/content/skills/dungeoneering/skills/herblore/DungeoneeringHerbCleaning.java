package com.rs.game.world.entity.player.content.skills.dungeoneering.skills.herblore;


import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.item.Item;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.Skills;

public class DungeoneeringHerbCleaning {

	public static boolean clean(final Player player, Item item) {
		final DungeoneeringHerbs herb = getHerb(item.getId());
		if (herb == null)
			return false;
		if (player.getSkills().getLevel(Skills.HERBLORE) < herb.getLevel()) {
			player.sendMessage("You do not have the required level to clean this.");
			return true;
		}
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				if (item.getId() != herb.getHerbId())
					return;
				if (!player.getInventory().hasFreeSlots() && item.getAmount() > 1) {
					player.sendMessage("You don't have enough free inventory space to clean this herb.");
					return;
				}
				player.getInventory().deleteItem(item.getId(), 1);
				player.getInventory().addItem(new Item(herb.getCleanId(), 1));
				player.getSkills().addXp(Skills.HERBLORE, herb.getExperience());
				player.sendMessage("You clean the herb.");
				this.stop();
			}
		});
		return true;
	}

	public static DungeoneeringHerbs getHerb(int id) {
		for (final DungeoneeringHerbs herb : DungeoneeringHerbs.values())
			if (herb.getHerbId() == id)
				return herb;
		return null;
	}
}