package com.rs.game.world.entity.player.controller.impl;

import java.util.ArrayList;
import java.util.List;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.item.Item;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.others.BarrowsBrother;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.player.controller.Controller;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

public final class Barrows extends Controller {
	
	private BarrowsBrother target;
	
	
	//they have to be handled by correct order
	//AHRIM
	//DHAROK
	//GUTHAN
	//KARIL
	//TORAG
	//VERAC
	private static enum Hills {
		AHRIM_HILL(new Position(3564, 3287, 0), new Position(3557, 9703, 3)),
		DHAROK_HILL(new Position(3573, 3296, 0), new Position(3556, 9718, 3)),
		GUTHAN_HILL(new Position(3574, 3279, 0), new Position(3534, 9704, 3)),
		KARIL_HILL(new Position(3563, 3276, 0), new Position(3546, 9684, 3)),
		TORAG_HILL(new Position(3553, 3281, 0), new Position(3568, 9683, 3)),
		//VERAC_HILL(new WorldTile(3556, 3296, 0), new WorldTile(3578, 9706, 3));
		VERAC_HILL(new Position(3556, 3296, 0), new Position(4077, 5710, 0));

		private Position outBound;
		private Position inside;
		
		//out bound since it not a full circle
		
		private Hills(Position outBound, Position in) {
			this.outBound = outBound;
			inside = in;
		}
	}
	
	public static boolean digIntoGrave(final Player player) {
		for(Hills hill : Hills.values()) {
			if(player.getZ() == hill.outBound.getZ()
					&& player.getX() >= hill.outBound.getX()
					&& player.getY() >= hill.outBound.getY()
					&& player.getX() <= hill.outBound.getX()+3
					&& player.getY() <= hill.outBound.getY()+3) {
				player.useStairs(-1, hill.inside, 1, 2, "You've broken into a crypt.");
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						player.getControlerManager().startControler("Barrows");
					}
				});
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public boolean canAttack(Entity target) {
		if (target instanceof BarrowsBrother && target != this.target) {
			player.getPackets().sendGameMessage("This isn't your target.");
			return false;
		}
		return true;
	}
	
	private void exit(Position outside) {
		player.setNextPosition(outside);
		leave(false);
	}
	
	private void leave(boolean logout) {
		if(target != null)
			target.finish(); //target also calls removing hint icon at remove
		if(!logout) {
			player.getPackets().sendBlackOut(0); //unblacks minimap
			if (player.getHiddenBrother() == -1)
				player.getPackets().sendStopCameraShake();
			else
				player.getPackets().closeInterface(player.getInterfaceManager().hasRezizableScreen() ? 11 : 0); //removes inter
			removeControler();
		}
	}
	
	@Override
	public boolean sendDeath() {
		leave(false);
		return true;
	}
	

	@Override
	public void magicTeleported(int type) {
		leave(false);
	}
	
	public int getRandomBrother() {
		List<Integer> bros = new ArrayList<Integer>();
		for(int i = 0; i < Hills.values().length; i++) {
			if(player.getKilledBarrowBrothers()[i] || player.getHiddenBrother() == i)
				continue;
			bros.add(i);
		}
		if(bros.isEmpty())
			return -1;
		return bros.get(Utils.random(bros.size()));
		
	}
	
	private static final Item[] COMMON_REWARDS = {new Item(558, 1795),
		new Item(562, 773),
		new Item(560, 391),
		new Item(565, 164),
		new Item(4740, 188)
	};
	
	private static final Item[] RARE_REWARDS = { 
			new Item(1149, 1), new Item(987, 1), new Item(985, 1)
	};

	Item[] BARROW_REWARDS = { 
			new Item(4708, 1), new Item(4710, 1), new Item(4712, 1), new Item(4714, 1),
			new Item(4716, 1), new Item(4718, 1), new Item(4720, 1), new Item(4722, 1), new Item(4724, 1),
			new Item(4726, 1), new Item(4728, 1), new Item(4730, 1), new Item(4732, 1), new Item(4734, 1),
			new Item(4736, 1), new Item(4738, 1), new Item(4745, 1), new Item(4747, 1), new Item(4749, 1),
			new Item(4751, 1), new Item(4753, 1), new Item(4755, 1), new Item(4757, 1), 
	};

	public void sendReward() {
		double percentage = 0;
		for(boolean died : player.getKilledBarrowBrothers()) {
			if(died)
				percentage += 2.5;
		}
		percentage += (player.getBarrowsKillCount() / 40d);
		if (percentage > 90)
			percentage = 90;
		if(percentage >= Math.random() * 100) {
			//reard barrows
			drop(BARROW_REWARDS[Utils.random(BARROW_REWARDS.length)]);
		}
		if (player.getEquipment().getRingId() == 2572)//ring of wealth
			percentage += 1;
		if (percentage / 2 >= Math.random() * 100) {
			drop(RARE_REWARDS[Utils.random(RARE_REWARDS.length)]);
			player.getPackets().sendGameMessage("<col=ff7000>Your ring of wealth shines more brightly!", true);
		}
		for (int i = 0; i < 10; i++)
			if (percentage >= Math.random() * 100)
				drop(COMMON_REWARDS[Utils.random(COMMON_REWARDS.length)]);
		drop(new Item(995, Utils.random(5307))); // money reward at least always
	}
	
	public void drop(Item item) {
		player.getInventory().addItemDrop(item.getId(), item.getAmount());
	}
	
	@Override
	public boolean processObjectClick1( WorldObject object) {
		if(object.getId() >= 6702 && object.getId() <= 6707) {
			Position out = Hills.values()[object.getId()-6702].outBound;
			 //cant make a perfect middle since 3/ 2 wont make a real integer number or wahtever u call it.. 
			exit(new Position(out.getX() + 1, out.getY() + 1, out.getZ()));
			return false;
		}else if (object.getId() == 10284) {
			if(player.getHiddenBrother() == -1) {//reached chest
				player.getPackets().sendGameMessage("You found nothing.");
				return false;
			}
			if(!player.getKilledBarrowBrothers()[player.getHiddenBrother()]) 
				if (player.getHiddenBrother() < 6)
					sendTarget(2025 + player.getHiddenBrother(), player);
				else
					sendTarget(14297, player);
			if (target != null) {
				player.getPackets().sendGameMessage("You found nothing.");
				return false;
			}
			sendReward();
			player.barrowsLoot++;
			player.getPackets().sendCameraShake(3, 12, 25, 12, 25);
			player.getPackets().closeInterface(player.getInterfaceManager().hasRezizableScreen() ? 11 : 0); //removes inter
			player.getPackets().sendSpawnedObject(new WorldObject(6775, 10, 0, 3551, 9695, 0));
			player.resetBarrows();
			return false;
		}else if (object.getId() >= 6716 && object.getId() <= 6749) {
			Position walkTo;
			if(object.getRotation() == 0) 
				walkTo = new Position(object.getX() + 5, object.getY(), 0);
			else if(object.getRotation() == 1)
				walkTo = new Position(object.getX(), object.getY() - 5, 0);
			else if(object.getRotation() == 2) 
				walkTo = new Position(object.getX() - 5, object.getY(), 0);
			else
				walkTo = new Position(object.getX(), object.getY() + 5, 0);
			if(!World.isNotCliped(walkTo.getZ(), walkTo.getX(), walkTo.getY(), 1))
				return false;
			player.addWalkSteps(walkTo.getX(), walkTo.getY(), -1, false);
			player.lock(6);
			if(player.getHiddenBrother() != -1) {
				int brother = getRandomBrother();
				if(brother != -1)
					if (brother < 6)
						sendTarget(2025 + brother, walkTo);
					else
						sendTarget(14297, walkTo);
			}
			return false;
		}else {
			int sarcoId = getSarcophagusId(object.getId());
			if(sarcoId != -1) {
				if(sarcoId == player.getHiddenBrother()) 
					player.getDialogueManager().startDialogue("BarrowsD");
				else if(target != null || player.getKilledBarrowBrothers()[sarcoId]) 
					player.getPackets().sendGameMessage("You found nothing.");
				else
					if (sarcoId != 6)
						sendTarget(2025 + sarcoId, player);
					else
						sendTarget(14297, player);
				return false;
			}
		}
		return true;
	};
	
	public int getSarcophagusId(int objectId) {
		switch(objectId) {
		case 66017: return 0;
		case 63177: return 1;
		case 66020: return 2;
		case 66018: return 3;
		case 66019: return 4;
		case 66016: return 5;
		case 61189: return 6;
		default : return -1;
		}
	}
	
	public void targetDied() {
		player.getHintIconsManager().removeUnsavedHintIcon();
		if (player.getHiddenBrother() < 6)
			setBrotherSlained(target.getId() - 2025);
		else
			setBrotherSlained(6);
		target = null;
		
	}
	
	public void targetFinishedWithoutDie() {
		player.getHintIconsManager().removeUnsavedHintIcon();
		target = null;
	}
	
	public void setBrotherSlained(int index) {
		player.getKilledBarrowBrothers()[index] = true;
		sendBrotherSlain(index, true);
	}
	
	public void sendTarget(int id, Position tile) {
		if(target != null) 
			target.disapear();
		target = new BarrowsBrother(id, tile, this);
		target.setTarget(player);
		target.setNextForceTalk(new ForceTalk("You dare disturb my rest!"));
		player.getHintIconsManager().addHintIcon(target, 1, -1, false);
	}
	
	public Barrows() {
		
	}
	
	//component 9, 10, 11
	
	private int headComponentId;
	private int timer;
	
	
	public int getAndIncreaseHeadIndex() {
		Integer head = (Integer) player.getTemporaryAttributtes().remove("BarrowsHead");
		if(head == null || head == player.getKilledBarrowBrothers().length-1)
			head = 0;
		player.getTemporaryAttributtes().put("BarrowsHead", head+1);
		return player.getKilledBarrowBrothers()[head] ? head : -1;
	}
	
	@Override
	public void process() {
		if(timer > 0) {
			timer--;
			return;
		}
		if(headComponentId == 0) {
			if(player.getHiddenBrother() == -1) {
				player.applyHit(new Hit(player, Utils.random(50)+1, HitLook.REGULAR_DAMAGE));
				resetHeadTimer();
				return;
			}
			int headIndex = getAndIncreaseHeadIndex();
			if(headIndex == -1) {
				resetHeadTimer();
				return;
			}
			headComponentId = 9 + Utils.random(2);
			player.getPackets().sendItemOnIComponent(24, headComponentId, 4761 + headIndex, 0);
			player.getPackets().sendIComponentAnimation(9810, 24, headComponentId);
			int activeLevel = player.getPrayer().getPrayerpoints();
			if (activeLevel > 0) {
				int level = player.getSkills().getLevelForXp(Skills.PRAYER) * 10;
				player.getPrayer().drainPrayer(level / 6);
			}
			timer = 3;
		}else{
			player.getPackets().sendItemOnIComponent(24, headComponentId, -1, 0);
			headComponentId = 0;
			resetHeadTimer();
		}
	}
	
	public void resetHeadTimer() {
		timer = 20 + Utils.random(6);
	}
	
	
	@Override
	public void sendInterfaces() {
		if(player.getHiddenBrother() != -1)
			player.getInterfaceManager().sendTab(player.getInterfaceManager().hasRezizableScreen() ? 11 : 0, 24);
	}
	
	public void loadData() {
		resetHeadTimer();
		for(int i = 0; i < player.getKilledBarrowBrothers().length; i++) 
			sendBrotherSlain(i, player.getKilledBarrowBrothers()[i]);
		sendCreaturesSlainCount(player.getBarrowsKillCount());
		player.getPackets().sendBlackOut(2); //blacks minimap
	}
	
	public void sendBrotherSlain(int index, boolean slain) {
		player.getPackets().sendConfigByFile(457+index, slain ? 1 : 0);
	}
	
	public void sendCreaturesSlainCount(int count) {
		player.getPackets().sendConfigByFile(464, count);
	}
	
	
	@Override
	public void start() {
		if(player.getHiddenBrother() == -1)
			player.setHiddenBrother(Utils.random(Hills.values().length));
		loadData();
		sendInterfaces();
	}
	
	@Override
	public boolean login() {
		if(player.getHiddenBrother() == -1)
			player.getPackets().sendCameraShake(3, 25, 50, 25, 50);
		loadData();
		sendInterfaces();
		return false;
	}
	
	@Override
	public boolean logout() {
		leave(true);
		return false;
	}
	
	@Override
	public void forceClose() {
		leave(true);
	}
	

}
