package com.rs.game.world.entity.npc.familiar;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.skilling.Summoning.Pouches;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;

public class Honeybadger extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6059371477618091701L;

	public Honeybadger(Player owner, Pouches pouch, Position tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	   @Override
	    public String getSpecialName() {
		return "Insane ferocity";
	    }

	    @Override
	    public String getSpecialDescription() {
		return "Decreases the owner's Magic, Range, and Defence, but also increasing Strength and Attack, there is also a chance of hitting twice.";
	    }

	    @Override
	    public int getBOBSize() {
		return 0;
	    }

	    @Override
	    public int getSpecialAmount() {
		return 4;
	    }

	    @Override
	    public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	    }

	    @Override
	    public boolean submitSpecial(Object object) {
		Player player = getOwner();
		int actualLevel = player.getSkills().getLevel(Skills.ATTACK);
		int realLevel = player.getSkills().getLevelForXp(Skills.ATTACK);
		int level = actualLevel > realLevel ? realLevel : actualLevel;
		player.getSkills().set(Skills.ATTACK, (int) (level + (realLevel * 0.15)));

		actualLevel = player.getSkills().getLevel(Skills.STRENGTH);
		realLevel = player.getSkills().getLevelForXp(Skills.STRENGTH);
		level = actualLevel > realLevel ? realLevel : actualLevel;
		player.getSkills().set(Skills.STRENGTH, (int) (level + (realLevel * 0.15)));

		actualLevel = player.getSkills().getLevel(Skills.DEFENCE);
		realLevel = player.getSkills().getLevelForXp(Skills.DEFENCE);
		level = actualLevel > realLevel ? realLevel : actualLevel;
		player.getSkills().set(Skills.DEFENCE, (int) (level - (realLevel * 0.15)));

		actualLevel = player.getSkills().getLevel(Skills.MAGIC);
		realLevel = player.getSkills().getLevelForXp(Skills.MAGIC);
		level = actualLevel > realLevel ? realLevel : actualLevel;
		player.getSkills().set(Skills.MAGIC, level - 5);

		actualLevel = player.getSkills().getLevel(Skills.RANGE);
		realLevel = player.getSkills().getLevelForXp(Skills.RANGE);
		level = actualLevel > realLevel ? realLevel : actualLevel;
		player.getSkills().set(Skills.RANGE, (int) (level - (realLevel * 0.1)));
		animate(new Animation(7930));
		setNextGraphics(new Graphics(1397));
		player.animate(new Animation(7660));
		player.setNextGraphics(new Graphics(1399));
		return false;
	    }
}

