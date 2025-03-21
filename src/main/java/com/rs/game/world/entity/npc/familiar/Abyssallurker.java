package com.rs.game.world.entity.npc.familiar;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.skilling.Summoning.Pouches;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;

public class Abyssallurker extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6059371477618091701L;

	public Abyssallurker(Player owner, Pouches pouch, Position tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

    @Override
    public String getSpecialName() {
	return "Abyssal Stealth";
    }

    @Override
    public String getSpecialDescription() {
	return "Temporarily increases a player's Agility and Thieving by 4 levels.";
    }

    @Override
    public int getBOBSize() {
	return 7;
    }

    @Override
    public int getSpecialAmount() {
	return 3;
    }

    @Override
    public SpecialAttack getSpecialAttack() {
	return SpecialAttack.CLICK;
    }

    @Override
    public boolean submitSpecial(Object object) {
	final Player player = (Player) object;
	int newTheiving = player.getSkills().getLevel(Skills.THIEVING) + 4;
	if (newTheiving > player.getSkills().getLevelForXp(Skills.THIEVING) + 4)
	    newTheiving = player.getSkills().getLevelForXp(Skills.THIEVING) + 4;
	int newAgility = player.getSkills().getLevel(Skills.AGILITY) + 4;
	if (newAgility > player.getSkills().getLevelForXp(Skills.AGILITY) + 4)
	    newAgility = player.getSkills().getLevelForXp(Skills.AGILITY) + 4;
	setNextGraphics(new Graphics(1336));
	animate(new Animation(7682));
	player.animate(new Animation(7660));
	WorldTasksManager.schedule(new WorldTask() {

	    @Override
	    public void run() {
		player.setNextGraphics(new Graphics(1300));
	    }
	}, 3);
	player.getSkills().set(Skills.THIEVING, newTheiving);
	player.getSkills().set(Skills.AGILITY, newAgility);
	return false;
    }
}