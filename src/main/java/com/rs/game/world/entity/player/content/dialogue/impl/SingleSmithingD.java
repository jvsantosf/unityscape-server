package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.WorldObject;
import com.rs.game.world.entity.player.actions.skilling.Smelting;
import com.rs.game.world.entity.player.actions.skilling.Smelting.SmeltingBar;
import com.rs.game.world.entity.player.content.SkillsDialogue;
import com.rs.game.world.entity.player.content.SkillsDialogue.ItemNameFilter;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.skills.Skills;

public class SingleSmithingD extends Dialogue {

	private WorldObject object;
	private SmeltingBar bar;

	@Override
	public void start() {
		object = (WorldObject) parameters[0];
		bar = (SmeltingBar) parameters[1];
		SkillsDialogue.sendSkillsDialogue(player, SkillsDialogue.MAKE,
				"How many bars you would like to smelt?<br>Choose a number, then click the bar to begin.", 28,
				new int[] { bar.getProducedBar().getId() }, new ItemNameFilter() {
					int count = 0;

					@Override
					public String rename(String name) {
						SmeltingBar bar = SmeltingBar.values()[count++];
						if (player.getSkills().getLevel(Skills.SMITHING) < bar.getLevelRequired())
							name = "<col=ff0000>" + name + "<br><col=ff0000>Level " + bar.getLevelRequired();
						return name;

					}
				});
	}

	@Override
	public void run(int interfaceId, int componentId) {
		player.getActionManager().setAction(new Smelting(bar, object, SkillsDialogue.getQuantity(player)));
		end();
	}

	@Override
	public void finish() {
	}

}
