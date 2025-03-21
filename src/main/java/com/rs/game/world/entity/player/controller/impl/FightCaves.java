package com.rs.game.world.entity.player.controller.impl;

import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.rs.Constants;
import com.rs.cores.CoresManager;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.item.Item;
import com.rs.game.map.RegionBuilder;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.fightcaves.FightCavesNPC;
import com.rs.game.world.entity.npc.fightcaves.TzKekCaves;
import com.rs.game.world.entity.npc.fightcaves.TzTok_Jad;
import com.rs.game.world.entity.player.DonatorRanks;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.skilling.Summoning;
import com.rs.game.world.entity.player.content.pets.Pets;
import com.rs.game.world.entity.player.controller.Controller;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.utility.Logger;
import com.rs.utility.Utils;

public class FightCaves extends Controller {

	public static final Position OUTSIDE = new Position(4610, 5130, 0);

	private static final int THHAAR_MEJ_JAL = 2617;

	private static final int[] MUSICS = {1088, 1082, 1086};

	public void playMusic() {
		player.getMusicsManager().playMusic(selectedMusic);
	}

	private final int[][] WAVES = {
			{2734}
			,{2734,2734}
			,{2736}
			,{2736,2734}
			,{2736,2734,2734}
			,{2736,2736}
			,{2739}
			,{2739,2734}
			,{2739,2734,2734}
			,{2739,2736}
			,{2739,2736,2734}
			,{2739,2736,2734,2734}
			,{2739,2736,2736}
			,{2739,2739}
			,{2741}
			,{2741,2734}
			,{2741,2734,2734}
			,{2741,2736}
			,{2741,2736,2734}
			,{2741,2736,2734,2734}
			,{2741,2736,2736}
			,{2741,2739}
			,{2741,2739,2734}
			,{2741,2739,2734,2734}
			,{2741,2739,2736}
			,{2741,2739,2736,2734}
			,{2741,2739,2736,2734,2734}
			,{2741,2739,2736,2736}
			,{2741,2739,2739}
			,{2741,2741}
			,{2743}
			,{2743,2734}
			,{2743,2734,2734}
			,{2743,2736}
			,{2743,2736,2734}
			,{2743,2736,2734,2734}
			,{2743,2736,2736}
			,{2743,2739}
			,{2743,2739,2734}
			,{2743,2739,2734,2734}
			,{2743,2739,2736}
			,{2743,2739,2736,2734}
			,{2743,2739,2736,2734,2734}
			,{2743,2739,2736,2736}
			,{2743,2739,2739}
			,{2743,2741}
			,{2743,2741,2734}
			,{2743,2741,2734,2734}
			,{2743,2741,2736}
			,{2743,2741,2736,2734}
			,{2743,2741,2736,2734,2734}
			,{2743,2741,2736,2736}
			,{2743,2741,2739}
			,{2743,2741,2739,2734}
			,{2743,2741,2739,2734,2734}
			,{2743,2741,2739,2736}
			,{2743,2741,2739,2736,2734}
			,{2743,2741,2739,2736,2734,2734}
			,{2743,2741,2739,2736,2736}
			,{2743,2741,2739,2739}
			,{2743,2741,2741}
			,{2743,2743}
			,{2745}
	};


	private int[] boundChuncks;
	private Stages stage;
	private boolean logoutAtEnd;
	private boolean login;
	public boolean spawned;
	public int selectedMusic;

	public static void enterFightCaves(Player player) {
		if(player.getFamiliar() != null || player.getPet() != null || Summoning.hasPouch(player) || Pets.hasPet(player)) {
			player.getDialogueManager().startDialogue("SimpleNPCMessage", THHAAR_MEJ_JAL, "No Kimit-Zil in the pits! This is a fight for YOU, not your friends!");
			return;
		}
		player.getControlerManager().startControler("FightCavesControler", getStartingWave(player)); //start at wave 1
	}
	
	
	private static int getStartingWave(Player p) {
		int wave;
		switch (p.getDonationManager().getRank()) {
		case BRONZE:
			wave = 15;
			break;
		case IRON:
			wave = 20;
			break;
		case STEEL:
			wave = 25;
			break;
		case MITHRIL:
			wave = 30;
			break;
		case ADAMANT:
			wave = 35;
			break;
		case RUNE:
			wave = 40;
			break;
		case DRAGON:
			wave = 50;
			break;
		default:
			wave = 1;
			break;
		}

		return wave;
	}

	private static enum Stages {
		LOADING,
		RUNNING,
		DESTROYING
	}

	@Override
	public void start() {
		loadCave(false);
	}

	@Override
	public boolean processButtonClick(int interfaceId, int componentId, int slotId, int packetId) {
		if(stage != Stages.RUNNING) {
			return false;
		}
		if(interfaceId == 182 && (componentId == 6 || componentId == 13)) {
			if(!logoutAtEnd) {
				logoutAtEnd = true;
				player.getPackets().sendGameMessage("<col=ff0000>You will be logged out automatically at the end of this wave.");
				player.getPackets().sendGameMessage("<col=ff0000>If you log out sooner, you will have to repeat this wave.");
			} else {
				player.forceLogout();
			}
			return false;
		}
		return true;
	}

	/**
	 * return process normaly
	 */
	@Override
	public boolean processObjectClick1(WorldObject object) {
		if(object.getId() == 9357) {
			if(stage != Stages.RUNNING) {
				return false;
			}
			exitCave(1);
			return false;
		}
		return true;
	}


	/*
	 * return false so wont remove script
	 */
	@Override
	public boolean login() {
		loadCave(true);
		return false;
	}


	public void loadCave(final boolean login) {
		this.login = login;
		stage = Stages.LOADING;
		player.lock(); //locks player
		CoresManager.slowExecutor.execute(new Runnable() {
			@Override
			public void run() {
				//finds empty map bounds
				boundChuncks = RegionBuilder.findEmptyChunkBound(8, 8);
				//copys real map into the empty map
				//552 640
				RegionBuilder.copyAllPlanesMap(552, 640, boundChuncks[0], boundChuncks[1], 8);
				//selects a music
				selectedMusic = MUSICS[Utils.random(MUSICS.length)];
				player.setNextPosition(!login ? getWorldTile(46, 61) : getWorldTile(32, 32) );
				//1delay because player cant walk while teleing :p, + possible issues avoid
				WorldTasksManager.schedule(new WorldTask()  {
					@Override
					public void run() {
						if(!login) {
							Position walkTo = getWorldTile(32, 32);
							player.addWalkSteps(walkTo.getX(), walkTo.getY());
						}
						player.getDialogueManager().startDialogue("SimpleNPCMessage", THHAAR_MEJ_JAL, "You're on your own now, JalYt.<br>Prepare to fight for your life!");
						player.setForceMultiArea(true);
						playMusic();
						player.unlock(); //unlocks player
						stage = Stages.RUNNING;
					}

				}, 1);
				if(!login) {
					/*
					 * lets stress less the worldthread, also fastexecutor used for mini stuff
					 */
					CoresManager.fastExecutor.schedule(new TimerTask() {

						@Override
						public void run() {
							if(stage != Stages.RUNNING) {
								return;
							}
							try {
								startWave();
							} catch (Throwable t) {
								Logger.handle(t);
							}
						}
					}, 6000);
				}
			}
		});
	}

	public Position getSpawnTile() {
		switch(Utils.random(5)) {
		case 0:
			return getWorldTile(11, 16);
		case 1:
			return getWorldTile(51, 25);
		case 2:
			return getWorldTile(10, 50);
		case 3:
			return getWorldTile(46, 49);
		case 4:
		default:
			return getWorldTile(32, 30);
		}
	}

	@Override
	public void moved() {
		if(stage != Stages.RUNNING || !login) {
			return;
		}
		login = false;
		setWaveEvent();
	}

	public void startWave() {
		int currentWave = getCurrentWave();
		if(currentWave > WAVES.length) {
			win();
			return;
		}
		player.getInterfaceManager().sendTab(player.getInterfaceManager().hasRezizableScreen() ? 11 : 0, 316);
		player.getPackets().sendConfig(639, currentWave);
		if(stage != Stages.RUNNING) {
			return;
		}
		for(int id : WAVES[currentWave-1]) {
			if(id == 2736) {
				new TzKekCaves(id, getSpawnTile());
			} else if (id == 2745) {
				new TzTok_Jad(id, getSpawnTile(), this);
			} else {
				new FightCavesNPC(id, getSpawnTile());
			}
		}
		spawned = true;
	}

	public void spawnHealers() {
		if(stage != Stages.RUNNING) {
			return;
		}
		for(int i = 0; i < 4; i++) {
			new FightCavesNPC(2746, getSpawnTile());
		}
	}

	public void win() {
		if(stage != Stages.RUNNING) {
			return;
		}
		exitCave(4);
	}


	public void nextWave() {
		playMusic();
		setCurrentWave(getCurrentWave()+1);
		if(logoutAtEnd) {
			player.forceLogout();
			return;
		}
		setWaveEvent();
	}

	public void setWaveEvent() {
		if(getCurrentWave() == 63) {
			player.getDialogueManager().startDialogue("SimpleNPCMessage", THHAAR_MEJ_JAL, "Look out, here comes TzTok-Jad!");
		}
		CoresManager.fastExecutor.schedule(new TimerTask() {

			@Override
			public void run() {
				try {
					if(stage != Stages.RUNNING) {
						return;
					}
					startWave();
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 600);
	}

	@Override
	public void process() {
		if(spawned) {
			List<Integer> npcs = World.getRegion(player.getRegionId()).getNPCsIndexes();
			if(npcs == null || npcs.isEmpty())  {
				spawned = false;
				nextWave();
			}
		}
	}


	@Override
	public boolean sendDeath() {
		player.lock(7);
		player.stopAll();
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.animate(new Animation(836));
				} else if (loop == 1) {
					player.getPackets().sendGameMessage("You have been defeated!");
				} else if (loop == 3) {
					player.reset();
					exitCave(1);
					player.animate(new Animation(-1));
				} else if (loop == 4) {
					player.getPackets().sendMusicEffect(90);
					stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}


	@Override
	public void magicTeleported(int type) {
		exitCave(2);
	}

	/*
	 * logout or not. if didnt logout means lost, 0 logout, 1, normal,  2 tele
	 */
	public void exitCave(int type) {
		stage = Stages.DESTROYING;
		Position outside = new Position(OUTSIDE, 2); //radomizes alil
		int tokkul = 0;
		if(type == 0 || type == 2) {
			player.setLocation(outside);
		} else {
			player.setForceMultiArea(false);
			player.getPackets().closeInterface(player.getInterfaceManager().hasRezizableScreen() ?  11 : 0);
			if(type == 1 || type == 4) {
				player.setNextPosition(outside);
				if(type == 4) {
					player.setCompletedFightCaves();
					player.reset();
					player.getDialogueManager().startDialogue("SimpleNPCMessage", THHAAR_MEJ_JAL, "You even defeated Tz Tok-Jad, I am most impressed! Please accept this gift as a reward.");
					player.getPackets().sendGameMessage("You were victorious!!");
					tokkul = 16064;
					if(!player.getInventory().addItem(6570, 1)) {
						World.addGroundItem(new Item(6570, 1), new Position(player), player, true, 180);
						World.addGroundItem(new Item(6529, tokkul * (player.getDonationManager().hasRank(DonatorRanks.DRAGON) ? 2 : 1)), new Position(player), player, true, 180);
					}else if(!player.getInventory().addItem(6529, 16064)) {
						World.addGroundItem(new Item(6529, tokkul * (player.getDonationManager().hasRank(DonatorRanks.DRAGON) ? 2 : 1)), new Position(player), player, true, 180);
					}
				}else if(getCurrentWave() == 1) {
					player.getDialogueManager().startDialogue("SimpleNPCMessage", THHAAR_MEJ_JAL, "Well I suppose you tried... better luck next time.");
				} else{
					tokkul = getCurrentWave() * 8032 / WAVES.length;
					tokkul *= Constants.DROP_RATE; //10x more
					tokkul *= (player.getDonationManager().hasRank(DonatorRanks.DRAGON) ? 2 : 1);
					if(!player.getInventory().addItem(6529, tokkul)) {
						World.addGroundItem(new Item(6529, tokkul), new Position(player), player, true, 180);
					}
					player.getDialogueManager().startDialogue("SimpleNPCMessage", THHAAR_MEJ_JAL, "Well done in the cave, here, take TokKul as reward.");
					//TODO tokens
				}
			}
			removeControler();
		}
		/*
		 * 1200 delay because of leaving
		 */
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				RegionBuilder.destroyMap(boundChuncks[0], boundChuncks[1], 8, 8);
			}
		}, 1200, TimeUnit.MILLISECONDS);
	}

	/*
	 * gets worldtile inside the map
	 */
	public Position getWorldTile(int mapX, int mapY) {
		return new Position(boundChuncks[0]*8 + mapX, boundChuncks[1]*8 + mapY, 0);
	}


	/*
	 * return false so wont remove script
	 */
	@Override
	public boolean logout() {
		/*
		 * only can happen if dungeon is loading and system update happens
		 */
		if(stage != Stages.RUNNING) {
			return false;
		}
		exitCave(0);
		return false;

	}
	@Override
	public boolean processMagicTeleport(Position toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage", "You cannot teleport out of the fight caves!");
		return false;
	}

	@Override
	public boolean processItemTeleport(Position toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage", "You cannot teleport out of the fight caves!");
		return false;
	}

	@Override
	public boolean processObjectTeleport(Position toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage", "You cannot teleport out of the fight caves!");
		return false;

	}

	public int getCurrentWave() {
		if (getArguments() == null || getArguments().length == 0) {
			return 0;
		}
		return (Integer) getArguments()[0];
	}

	public void setCurrentWave(int wave) {
		if(getArguments() == null || getArguments().length == 0) {
			setArguments(new Object[1]);
		}
		getArguments()[0] = wave;
	}

	@Override
	public void forceClose() {
		/*
		 * shouldnt happen
		 */
		if(stage != Stages.RUNNING) {
			return;
		}
		exitCave(2);
	}
}
