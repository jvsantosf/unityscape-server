package com.rs.game.world.entity.npc.godwars.saradomin;

import java.util.ArrayList;
import java.util.List;

import com.rs.game.item.Item;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.utility.Utils;

@SuppressWarnings("serial")
public class GodwarsSaradominFaction extends NPC {

	public GodwarsSaradominFaction(int id, Position tile, int mapAreaNameHash,
                                   boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, 0, canBeAttackFromOutOfArea, spawned);
	}

	@Override
	public ArrayList<Entity> getPossibleTargets() {
		ArrayList<Entity> possibleTarget = new ArrayList<Entity>();
		for (int regionId : getMapRegionsIds()) {
			List<Integer> playerIndexes = World.getRegion(regionId)
					.getPlayerIndexes();
			if (playerIndexes != null) {
				for (int npcIndex : playerIndexes) {
					Player player = World.getPlayers().get(npcIndex);
					if (player == null
							|| player.isDead()
							|| player.isFinished()
							|| !player.isRunning()
							|| !player
									.withinDistance(
											this,
											getCombatDefinitions()
													.getAttackStyle() == NPCCombatDefinitions.MELEE ? 4
													: getCombatDefinitions()
															.getAttackStyle() == NPCCombatDefinitions.RANGE_FOLLOW ? 16
															: 8)
							|| ((!isAtMultiArea() || !player.isAtMultiArea())
									&& player.getAttackedBy() != this && player
									.getAttackedByDelay() > Utils
									.currentTimeMillis())
							|| !clipedProjectile(player, false) || !hasGodItem(player))
						continue;
					possibleTarget.add(player);
				}
			}
			List<Integer> npcsIndexes = World.getRegion(regionId)
					.getNPCsIndexes();
			if (npcsIndexes != null) {
				for (int npcIndex : npcsIndexes) {
					NPC npc = World.getNPCs().get(npcIndex);
					if (npc == null
							|| npc == this
							|| npc instanceof GodwarsSaradominFaction
							|| npc.isDead()
							|| npc.isFinished()
							|| !npc.withinDistance(
									this,
									getCombatDefinitions().getAttackStyle() == NPCCombatDefinitions.MELEE ? 4
											: getCombatDefinitions()
													.getAttackStyle() == NPCCombatDefinitions.RANGE_FOLLOW ? 16
													: 8)
							|| !npc.getDefinitions().hasAttackOption()
							|| ((!isAtMultiArea() || !npc.isAtMultiArea())
									&& npc.getAttackedBy() != this && npc
									.getAttackedByDelay() > Utils
									.currentTimeMillis())
							|| !clipedProjectile(npc, false))
						continue;
					possibleTarget.add(npc);
				}
			}
		}
		return possibleTarget;
	}
	
	private boolean hasGodItem(Player player) {
		for (Item item : player.getEquipment().getItems().getItems()) {
			if (item == null) continue; // shouldn't happen
			String name = item.getDefinitions().getName();
			// using else as only one item should count
			if (name.contains("Saradomin coif") || name.contains("Citharede hood") || name.contains("Saradomin mitre") || name.contains("Saradomin full helm") || name.contains("Saradomin halo") || name.contains("Torva full helm") || name.contains("Pernix cowl") || name.contains("Virtus mask"))
				return true;
			else if (name.contains("Saradomin cape") || name.contains("Saradomin cloak"))
				return true;
			else if (name.contains("Holy symbol") || name.contains("Citharede symbol") || name.contains ("Saradomin stole"))
				return true;
			else if (name.contains("Saradomin arrow"))
				return true;
			else if (name.contains("Saradomin godsword") || name.contains("Saradomin sword") || name.contains("Saradomin staff") || name.contains("Saradomin crozier") || name.contains("Zaryte Bow"))
				return true;
			else if (name.contains("Saradomin robe top") || name.contains("Saradomin d'hide") || name.contains("Citharede robe top") || name.contains("Monk's robe top") || name.contains("Saradomin platebody") || name.contains("Torva platebody") || name.contains("Pernix body") || name.contains("Virtus robe top"))
				return true;
			else if (name.contains("Illuminated holy book") || name.contains("Holy book") || name.contains(" Saradomin kiteshield"))
				return true;
		}
		return false;
	}
}
