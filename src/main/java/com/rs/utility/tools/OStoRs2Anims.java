package com.rs.utility.tools;

import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

import com.alex.io.InputStream;
import com.alex.io.OutputStream;
import com.alex.store.Index;
import com.alex.store.Store;
import com.alex.utils.Constants;
import com.alex.utils.Utils;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class OStoRs2Anims {

    private static final DecimalFormat format = new DecimalFormat("#.##");
    private static int currentBas = 4000;

    private static final Set<Integer> underwaterMaps = new HashSet<>();

    public static final int OSRS_MODELS_OFFSET = 300_000;
    public static final int OLD_MODEL_OFFSET = 600_000;
    public static final int OVERLAY_OFFSET = 1_000;
    public static final int UNDERLAY_OFFSET = 1_000;
    public static final int OSRS_BASES_OFFSET = 8_000;
    public static final int OSRS_FRAMES_OFFSET = 8_000;
    public static final int OSRS_SEQ_OFFSET = 20_000; //34223
    public static final int OSRS_OBJECTS_OFFSET = 200_000;
    public static final int OSRS_ITEMS_OFFSET = 30000;
    public static final int OSRS_TEXTURES_OFFSET = 2611;
    public static final int OSRS_NPC_OFFSET = 20_000;
    public static final int OSRS_BAS_OFFSET = 20_000;
    public static final int OSRS_SPOT_OFFSET = 5000;

    static {
        // zulrah map.
        underwaterMaps.add(9007);
        underwaterMaps.add(9008);
    }

    public static void main(String[] args) throws IOException {
        // if (false) {
        // // this should be only executed to transfer your customs from your old cache
        // to
        // // new cache. How do i pack latest OSRS npcs?
        // Store old_cache = new Store("C:\\Users\\Connor\\Documents\\cache\\");
        // Store new_cache = new Store(Settings.CACHE_PATH + "\\");
        // for (int id = 235339; id <= 235531; id++) {// THESE IDS NEED REPLACING BELOW
        // 200K (80-120K?)
        // byte[] data = old_cache.getIndexes()[7].getFile(id, 0);
        // if (data == null) {
        // continue;
        // }
        // new_cache.getIndexes()[7].putFile(id, 0, Constants.GZIP_COMPRESSION, data,
        // null, false, false, -1, -1);
        // System.out.println("Repacked custom model " + id + "..");
        // }
        // new_cache.getIndexes()[7].rewriteTable();
        // return;
        // }
        Store osrs_cache = new Store("G:\\[Learning] RSPS\\caches\\204\\");
        Store nsrs_cache = new Store("G:\\[Clients] RSPS\\[Sources]\\unityscape-server\\data\\cache_202\\");
        /**
         * When packing npcs use the following lines; models and npcs, and animations
         */
        //transport_animations(osrs_cache, nsrs_cache);
        // /**3264, 4288
        // * Maps
        // */
        //initializeJSONXteas();
        //transport_models(osrs_cache, nsrs_cache, OSRS_MODELS_OFFSET);
        //transport_group(osrs_cache, nsrs_cache, 2, 19, OSRS_ITEMS_OFFSET);

//        transport_index(osrs_cache, 7, nsrs_cache, 7, OSRS_MODELS_OFFSET, true);
   	    transport_models(osrs_cache, nsrs_cache, OSRS_MODELS_OFFSET);
//		transport_overlays(osrs_cache, nsrs_cache, OVERLAY_OFFSET);
//		transport_underlays(osrs_cache, nsrs_cache, UNDERLAY_OFFSET);
//        transport_animations(osrs_cache, nsrs_cache);
		transport_index(osrs_cache, 9, nsrs_cache, 9, OSRS_TEXTURES_OFFSET);
		//transport_maps(osrs_cache, nsrs_cache, collect_regionids(com.rs.Constants.OSRS_REGIONS));
//		transport_group(osrs_cache, nsrs_cache, 6, 16, OSRS_OBJECTS_OFFSET);
        transport_group(osrs_cache, nsrs_cache, 6, 16, OSRS_OBJECTS_OFFSET);
    }

    private static void transport_archive(Store source_cache, int source_id, Store target_cache, int target_id, int group_id) throws IOException {
        transport_archive(source_cache, source_id, target_cache, target_id, group_id, true);
    }

    private static void transport_archive(Store source_cache, int source_id, Store target_cache, int target_id, int group_id, boolean rewrite) throws IOException {
        Index target_index = target_cache.getIndexes()[target_id];
        System.out.println("Attempting to transport group of id " + group_id + "..");
        if (source_cache.getIndexes()[source_id].archiveExists(group_id)) {
            target_index.putArchive(source_id, group_id, source_cache, false, true);
        }
        if (rewrite) {
            System.out.println("\t ^ Rewriting table..");
            target_index.rewriteTable();
        }
        System.out.println("\t ^ Finished!");
        System.out.println();

    }

    private static void transport_locations(Store osrs_cache, Store nsrs_cache, int offset) {
        final int num_locations = osrs_cache.getIndexes()[2].getLastFileId(6) + 1;
        double last = 0.0D;
        System.out.println("Attempting to transport locations from osrs..");
        for (int locationid = 0; locationid < num_locations; locationid++) {
            byte[] data = osrs_cache.getIndexes()[2].getFile(6, locationid);
            if (data != null) {
                int newid = locationid + offset;
                int groupid = newid >>> 8;
                int fileid = newid & 0xff;
                nsrs_cache.getIndexes()[16].putFile(groupid, fileid, Constants.GZIP_COMPRESSION, data, null, false, false, -1, -1);
            }
            double percentage = (double) locationid / (double) num_locations * 100D;
            if (locationid == num_locations - 1 || percentage - last >= 1.0D) {
                System.out.println("\t ^ Percentage Completed: " + format.format(percentage) + "%");
                last = percentage;
            }
        }
        System.out.println("\t ^ Rewriting table..");
        nsrs_cache.getIndexes()[16].rewriteTable();
        System.out.println("\t ^ Finished!");
        System.out.println();
    }

    private static void transport_maps(Store osrs_cache, Store nsrs_cache, Set<Integer> regionids) throws IOException {
        System.out.println("Attempts to transport " + regionids.size() + " map(s) from osrs..");
        int index = 0;
        for (int regionid : regionids) {
            System.out.println("\t ^ Attempts to transport map " + regionid + " from osrs.. (" + ++index + " / " + regionids.size() + ") to coordinates: " + (regionid >> 8 & 0xff) * 64 + ", " + (regionid & 0xff) * 64);
            transport_map(osrs_cache, nsrs_cache, regionid, false);
        }
        System.out.println("\t ^ Rewriting table..");
        nsrs_cache.getIndexes()[5].rewriteTable();
        System.out.println("\t ^ Finished!");
        System.out.println();
    }

    private static void transport_map(Store osrs_cache, Store nsrs_cache, int regionid, boolean rewrite) throws IOException {
        if (XTEAS.isEmpty()) {
            initializeJSONXteas();
        }
        int regionx = regionid >> 8;
        int regiony = regionid & 0xff;
        {
            int map_namehash = Utils.getNameHash("m" + regionx + "_" + regiony);
            int map_groupid = osrs_cache.getIndexes()[5].getArchiveId(map_namehash);
            if (map_groupid == -1) {
                return;
            }
            byte[] map_data = osrs_cache.getIndexes()[5].getFile(map_groupid, 0);
            if (map_data == null) {
                return;
            }
            final boolean generateUnderwater = underwaterMaps.contains(regionid);
            byte[] umap_data = null;
            if (generateUnderwater) {
                if (true) {
                    umap_data = nsrs_cache.getIndexes()[5].getFile(nsrs_cache.getIndexes()[5].getArchiveId("um32_47"), 0);
                } else {
                    byte[][] beep_data = generate_underwater_map(map_data);
                    if (beep_data != null) {
                        map_data = beep_data[0];
                        umap_data = beep_data[1];
                    }
                }
            }
            map_groupid = nsrs_cache.getIndexes()[5].getArchiveId(map_namehash);
            if (map_groupid == -1) {
                map_groupid = nsrs_cache.getIndexes()[5].getLastArchiveId() + 1;
            }
            nsrs_cache.getIndexes()[5].putFile(map_groupid, 0, Constants.GZIP_COMPRESSION, map_data, null, rewrite, true, map_namehash, -1);
            if (umap_data != null) {
                map_namehash = Utils.getNameHash("um" + regionx + "_" + regiony);
                map_groupid = nsrs_cache.getIndexes()[5].getArchiveId(map_namehash);
                if (map_groupid == -1) {
                    map_groupid = nsrs_cache.getIndexes()[5].getLastArchiveId() + 1;
                }
                nsrs_cache.getIndexes()[5].putFile(map_groupid, 0, Constants.GZIP_COMPRESSION, umap_data, null, rewrite, true, map_namehash, -1);
            }
        }
        {
            int loc_namehash = Utils.getNameHash("l" + regionx + "_" + regiony);
            int loc_groupid = osrs_cache.getIndexes()[5].getArchiveId(loc_namehash);
            if (loc_groupid == -1) {
                return;
            }
            //XTEAS.put(loc_namehash, Set.of(new int[]{-902940818,2021125853,-1994893323,383226982}));
            Set<int[]> possibleXteas = XTEAS.get(loc_namehash);
            if (possibleXteas == null) {
                System.err.println("Missing xteas for: " + regionid + " >> regionHash: " + loc_namehash);
                return;
            }
            byte[] loc_data = null;
            for (int[] xteas : possibleXteas) {
                loc_data = osrs_cache.getIndexes()[5].getFile(loc_groupid, 0, xteas);
                if (loc_data != null && loc_data.length > 2) {
                    break;
                }
                osrs_cache.getIndexes()[5].resetCachedFiles();
                loc_data = null;
            }
            if (loc_data == null) {
                return;
            }
            loc_groupid = nsrs_cache.getIndexes()[5].getArchiveId(loc_namehash);
            if (loc_groupid == -1) {
                loc_groupid = nsrs_cache.getIndexes()[5].getLastArchiveId() + 1;
            }
            nsrs_cache.getIndexes()[5].putFile(loc_groupid, 0, Constants.GZIP_COMPRESSION, loc_data, null, rewrite, false, loc_namehash, -1);
        }
    }

    private static byte[][] generate_underwater_map(byte[] overwater_map) {
        // store hte map data.
        int[][][] heights = new int[4][64][64];
        byte[][][] underlays = new byte[4][64][64];
        byte[][][] overlays = new byte[4][64][64];
        byte[][][] settings = new byte[4][64][64];
        byte[][][] rotations = new byte[4][64][64];
        byte[][][] shapes = new byte[4][64][64];
        boolean[][][] waterises = new boolean[4][64][64];
        boolean has_water = false;
        // read the map data.
        InputStream stream = new InputStream(overwater_map);
        for (int level = 0; level < 4; level++) {
            for (int x = 0; x < 64; x++) {
                for (int y = 0; y < 64; y++) {
                    for (;;) {
                        int opcode = stream.readUnsignedByte();
                        if (opcode == 0) {
                            heights[level][x][y] = Integer.MIN_VALUE;
                            break;
                        } else if (opcode == 1) {
                            heights[level][x][y] = stream.readUnsignedByte();
                            break;
                        } else if (opcode <= 49) {
                            int overlay = overlays[level][x][y] = (byte) stream.readByte();
                            shapes[level][x][y] = (byte) ((opcode - 2) / 4);
                            rotations[level][x][y] = (byte) (opcode - 2 & 0x3);
                            if (level == 0) {
                                waterises[level][x][y] = true;
                                has_water = true;
                            }
                        } else if (opcode <= 81) {
                            settings[level][x][y] = (byte) (opcode - 49);
                        } else {
                            int underlay = underlays[level][x][y] = (byte) (opcode - 81);
                            if (underlay == 103) {
                                waterises[level][x][y] = true;
                                has_water = true;
                            }
                        }
                    }
                }
            }
        }
        if (!has_water) {
            return null;
        }
        OutputStream map_stream = new OutputStream();
        OutputStream umap_stream = new OutputStream();
        for (int level = 0; level < 4; level++) {
            for (int x = 0; x < 64; x++) {
                for (int y = 0; y < 64; y++) {
                    int height = heights[level][x][y];
                    int underlay = underlays[level][x][y] & 0xff;
                    int overlay = overlays[level][x][y] & 0xff;
                    int setting = settings[level][x][y];
                    int rotation = rotations[level][x][y];
                    int shape = shapes[level][x][y];
                    boolean waterise = waterises[level][x][y];
                    if (overlay > 0) {
                        // always at surface
                        map_stream.writeByte(shape * 4 + (rotation & 0x3) + 2);
                        map_stream.writeByte(overlays[level][x][y]);
                        if (waterise) {
                            umap_stream.writeByte(shape * 4 + (rotation & 0x3) + 2);
                            umap_stream.writeByte(overlays[level][x][y]);

                        }
                    }
                    if (setting > 0) {
                        // both share settings
                        map_stream.writeByte(49 + setting);
                        if (waterise) {
                            umap_stream.writeByte(49 + setting);
                        }
                    }
                    if (underlay > 0) {
                        // underlay only at underwater if there is water.
                        if (waterise) {
                            umap_stream.writeByte(81 + (underlay & 0xff));
                        }
                        map_stream.writeByte(81 + (underlay & 0xff));
                    }
                    if (height == Integer.MIN_VALUE) {
                        map_stream.writeByte(0);
                    } else {
                        map_stream.writeByte(1);
                        map_stream.writeByte(height);
                    }
                    if (!waterise || height == Integer.MIN_VALUE) {
                        umap_stream.writeByte(0);
                    } else {
                        umap_stream.writeByte(1);
                        umap_stream.writeByte(3);
                    }
                }
            }
        }
        return new byte[][] { Arrays.copyOfRange(map_stream.getBuffer(), 0, map_stream.getOffset()), Arrays.copyOfRange(umap_stream.getBuffer(), 0, umap_stream.getOffset()) };

    }

    private static Set<Integer> collect_regionids(int... borders) {
        Set<Integer> regionids = new HashSet<>();
        int minx = 255;
        int miny = 255;
        int maxx = 0;
        int maxy = 0;
        for (int border : borders) {
            int borderx = border >> 8;
            int bordery = border & 0xff;
            if (borderx < minx) {
                minx = borderx;
            }
            if (borderx > maxx) {
                maxx = borderx;
            }
            if (bordery < miny) {
                miny = bordery;
            }
            if (bordery > maxy) {
                maxy = bordery;
            }
        }
        for (int x = minx; x <= maxx; x++) {
            for (int y = miny; y <= maxy; y++) {
                regionids.add(x << 8 | y);
            }
        }
        return regionids;
    }

    private static void transport_overlays(Store osrs_cache, Store nsrs_cache, int offset) {
        final int num_overlays = osrs_cache.getIndexes()[2].getLastFileId(4) + 1;
        double last = 0.0D;
        System.out.println("Attempting to transport overlays from osrs..");
        for (int overlayid = 0; overlayid < num_overlays; overlayid++) {
            byte[] data = osrs_cache.getIndexes()[2].getFile(4, overlayid);
            if (data != null) {
                nsrs_cache.getIndexes()[2].putFile(4, overlayid + offset, Constants.GZIP_COMPRESSION, data, null, false, false, -1, -1);
            }
            double percentage = (double) overlayid / (double) num_overlays * 100D;
            if (overlayid == num_overlays - 1 || percentage - last >= 1.0D) {
                System.out.println("\t ^ Percentage Completed: " + format.format(percentage) + "%");
                last = percentage;
            }
        }
        System.out.println("\t ^ Rewriting table..");
        nsrs_cache.getIndexes()[2].rewriteTable();
        System.out.println("\t ^ Finished!");
        System.out.println();
    }

    private static void transport_underlays(Store osrs_cache, Store nsrs_cache, int offset) {
        final int num_underlays = osrs_cache.getIndexes()[2].getLastFileId(1) + 1;
        double last = 0.0D;
        System.out.println("Attempting to transport underlays from osrs..");
        for (int underlayid = 0; underlayid < num_underlays; underlayid++) {
            byte[] data = osrs_cache.getIndexes()[2].getFile(1, underlayid);
            if (data != null) {
                nsrs_cache.getIndexes()[2].putFile(1, underlayid + offset, Constants.GZIP_COMPRESSION, data, null, false, false, -1, -1);
            }
            double percentage = (double) underlayid / (double) num_underlays * 100D;
            if (underlayid == num_underlays - 1 || percentage - last >= 1.0D) {
                System.out.println("\t ^ Percentage Completed: " + format.format(percentage) + "%");
                last = percentage;
            }
        }
        System.out.println("\t ^ Rewriting table..");
        nsrs_cache.getIndexes()[2].rewriteTable();
        System.out.println("\t ^ Finished!");
        System.out.println();
    }

    private static void transport_animations(Store osrs_cache, Store nsrs_cache) throws IOException {
        transport_index(osrs_cache, 0, nsrs_cache, 0, 8000);
        transport_index(osrs_cache, 1, nsrs_cache, 1, 8000);
        transport_seqs(osrs_cache, nsrs_cache, 30000);
    }

    private static void transport_models(Store osrs_cache, Store nsrs_cache, int offset) {
        final int numModels = osrs_cache.getIndexes()[7].getLastArchiveId() + 1;
        double last = 0.0D;
        System.out.println("Attempting to transport models from osrs..");
        for (int modelid = 0; modelid < numModels; modelid++) {
            byte[] data = osrs_cache.getIndexes()[7].getFile(modelid, 0);
            if (data != null) {
                nsrs_cache.getIndexes()[7].putFile(offset + modelid, 0, Constants.GZIP_COMPRESSION, data, null, false, false, -1, -1);
            }
            double percentage = (double) modelid / (double) numModels * 100D;
            if (modelid == numModels - 1 || percentage - last >= 1.0D) {
                System.out.println("\t ^ Percentage Completed: " + format.format(percentage) + "%");
                last = percentage;
            }
        }
        System.out.println("\t ^ Rewriting table..");
        nsrs_cache.getIndexes()[7].rewriteTable();
        System.out.println("\t ^ Finished!");
        System.out.println();
    }

    static void transport_npcs(Store osrs_cache, Store nsrs_cache, int offset) {
        final int num_npcs = osrs_cache.getIndexes()[2].getLastFileId(9) + 1;
        double last = 0.0D;
        System.out.println("Attempting to transport npcs from osrs..");
        for (int npcid = 0; npcid < num_npcs; npcid++) {
            byte[] data = osrs_cache.getIndexes()[2].getFile(9, npcid);
            if (data != null) {
                int newid = npcid + offset;
                int groupid = newid >>> 7;
                int fileid = newid & 0x7f;
                data = transform_npc_data(newid, nsrs_cache, data);
                nsrs_cache.getIndexes()[18].putFile(groupid, fileid, Constants.GZIP_COMPRESSION, data, null, false, false, -1, -1);
            }
            double percentage = (double) npcid / (double) num_npcs * 100D;
            if (npcid == num_npcs - 1 || percentage - last >= 1.0D) {
                System.out.println("\t ^ Percentage Completed: " + format.format(percentage) + "%");
                last = percentage;
            }
        }
        System.out.println("\t ^ Rewriting table..");
        nsrs_cache.getIndexes()[2].rewriteTable();
        nsrs_cache.getIndexes()[18].rewriteTable();
        System.out.println("\t ^ Finished!");
        System.out.println();
    }

    private static byte[] transform_npc_data(int id, Store nsrs_cache, byte[] data) {
        InputStream buffer = new InputStream(data);
        OutputStream output = new OutputStream();
        int ready_animation = -1, anim2 = -1, anim3 = -1, anim4 = -1, walk_forward_animation = -1, walk_backward_animation = -1, walk_left_animation = -1, walk_right_animation = -1;
        for (;;) {
            int opcode = buffer.readUnsignedByte();
            if (opcode == 0) {
                break;
            }
            if (opcode == 1) {
                output.writeByte(opcode);
                int num_models = buffer.readUnsignedByte();
                output.writeByte(num_models);
                for (int i_55_ = 0; i_55_ < num_models; i_55_++) {
                    output.writeShort(buffer.readUnsignedShort());
                }
            } else if (opcode == 2) {
                output.writeByte(opcode);
                output.writeString(buffer.readString());
            } else if (opcode == 12) {
                output.writeByte(opcode);
                output.writeByte(buffer.readUnsignedByte());
            } else if (opcode == 13) {
                ready_animation = buffer.readUnsignedShort();
            } else if (opcode == 14) {
                anim2 = buffer.readUnsignedShort();
            } else if (opcode == 15) {
                anim3 = buffer.readUnsignedShort();
            } else if (opcode == 16) {
                anim4 = buffer.readUnsignedShort();
            } else if (17 == opcode) {
                walk_forward_animation = buffer.readUnsignedShort();
                walk_backward_animation = buffer.readUnsignedShort();
                walk_left_animation = buffer.readUnsignedShort();
                walk_right_animation = buffer.readUnsignedShort();
            } else if (opcode >= 30 && opcode < 35) {
                output.writeByte(opcode);
                output.writeString(buffer.readString());
            } else if (opcode == 40) {
                output.writeByte(opcode);
                int numRecols = buffer.readUnsignedByte();
                output.writeByte(numRecols);
                for (int index = 0; index < numRecols; index++) {
                    output.writeShort(buffer.readUnsignedShort());
                    output.writeShort(buffer.readUnsignedShort());
                }
            } else if (opcode == 41) {
                output.writeByte(opcode);
                int numRetexs = buffer.readUnsignedByte();
                output.writeByte(numRetexs);
                for (int i_59_ = 0; i_59_ < numRetexs; i_59_++) {
                    output.writeShort(buffer.readUnsignedShort());
                    output.writeShort(buffer.readUnsignedShort());
                }
            } else if (opcode == 60) {
                output.writeByte(opcode);
                int i_62_ = buffer.readUnsignedByte();
                output.writeByte(i_62_);
                for (int i_63_ = 0; i_63_ < i_62_; i_63_++) {
                    output.writeShort(buffer.readUnsignedShort());
                }
            } else if (opcode == 93) {
                output.writeByte(opcode);
            } else if (opcode == 95) {
                output.writeByte(opcode);
                output.writeShort(buffer.readUnsignedShort());
            } else if (opcode == 97) {
                output.writeByte(opcode);
                output.writeShort(buffer.readUnsignedShort());
            } else if (98 == opcode) {
                output.writeByte(opcode);
                output.writeShort(buffer.readUnsignedShort());
            } else if (opcode == 99) {
                output.writeByte(opcode);
            } else if (opcode == 100) {
                output.writeByte(opcode);
                output.writeByte(buffer.readUnsignedByte());
            } else if (101 == opcode) {
                output.writeByte(opcode);
                output.writeByte(buffer.readUnsignedByte());
            } else if (opcode == 102) {
                output.writeByte(opcode);
                output.writeShort(buffer.readUnsignedShort());
            } else if (opcode == 103) {
                output.writeByte(opcode);
                output.writeShort(buffer.readUnsignedShort());
            } else if (opcode == 106 || 118 == opcode) {
                output.writeByte(opcode);
                output.writeShort(buffer.readUnsignedShort());
                output.writeShort(buffer.readUnsignedShort());
                if (opcode == 118) {
                    output.writeShort(buffer.readUnsignedShort());
                }
                int i_65_ = buffer.readUnsignedByte();
                output.writeByte(i_65_);
                for (int i_66_ = 0; i_66_ <= i_65_; i_66_++) {
                    output.writeShort(buffer.readUnsignedShort());
                }
            } else if (opcode == 107) {
                output.writeByte(opcode);
            } else if (opcode == 109) {
                output.writeByte(opcode);
            } else if (111 == opcode) {
                output.writeByte(opcode);
            } else if (opcode == 249) {
                output.writeByte(opcode);
                int size = buffer.readUnsignedByte();
                output.writeByte(size);
                for (int index = 0; index < size; index++) {
                    boolean test = buffer.readUnsignedByte() == 1;
                    output.writeByte(test ? 1 : 0);
                    int key = buffer.read24BitInt();
                    output.write24BitInteger(key);
                    if (test) {
                        output.writeString(buffer.readString());
                    } else {
                        output.writeInt(buffer.readInt());
                    }
                }
            } else {
                throw new RuntimeException("Unhandled opcode:" + opcode);
            }
        }
        if (ready_animation != -1 || anim2 != -1 || anim3 != -1 || anim4 != -1 || anim4 != -1 || walk_forward_animation != -1 || walk_backward_animation != -1 || walk_left_animation != -1 || walk_right_animation != -1) {
            output.writeByte(127);// anim2 != -1 || anim3 != -1 || anim4 != -1 || anim4 != -1 || prob run
            output.writeShort(create_bas_type(nsrs_cache, ready_animation, anim2, walk_backward_animation, walk_left_animation, walk_right_animation));
        }
        output.writeByte(0);
        return Arrays.copyOf(output.getBuffer(), output.getOffset());
    }

    private static int create_bas_type(Store nsrs_cache, int ready_animation, int walk_forward_animation, int walk_backward_animation, int walk_left_animation, int walk_right_animation) {
        OutputStream output = new OutputStream();
        if (ready_animation != -1) {
            output.writeByte(1);
            output.writeBigSmart(ready_animation);
            output.writeBigSmart(walk_forward_animation);
        }
        if (walk_forward_animation != -1) {
            final int[] opcodes = { 2, 6 };
            for (int opcode : opcodes) {
                output.writeByte(opcode);
                output.writeBigSmart(walk_forward_animation);
            }
        }
        if (walk_backward_animation != -1) {
            final int[] opcodes = { 3, 7, 40 };
            for (int opcode : opcodes) {
                output.writeByte(opcode);
                output.writeBigSmart(walk_backward_animation);
            }
        }
        if (walk_left_animation != -1) {
            final int[] opcodes = { 4, 8, 41 };
            for (int opcode : opcodes) {
                output.writeByte(opcode);
                output.writeBigSmart(walk_left_animation);
            }
        }
        if (walk_right_animation != -1) {
            final int[] opcodes = { 5, 9, 42 };
            for (int opcode : opcodes) {
                output.writeByte(opcode);
                output.writeBigSmart(walk_right_animation);
            }
        }
        output.writeByte(0);
        byte[] data = Arrays.copyOf(output.getBuffer(), output.getOffset());
        int basid = 4000 + currentBas++;
        nsrs_cache.getIndexes()[2].putFile(32, basid, Constants.GZIP_COMPRESSION, data, null, false, false, -1, -1);
        return basid;
    }

    private static void transport_group(Store osrs_cache, Store nsrs_cache, int group, int index, int offset) {
        final int num_npcs = osrs_cache.getIndexes()[2].getLastFileId(group) + 1;
        double last = 0.0D;
        System.out.println("Attempting to transport group (" + group + ") from osrs..");
        for (int seqid = 0; seqid < num_npcs; seqid++) {
            byte[] data = osrs_cache.getIndexes()[2].getFile(group, seqid);
            if (data != null) {
                int newid = seqid + offset;
                int groupid = newid >>> 8;
                int fileid = newid & 0xff;
                nsrs_cache.getIndexes()[index].putFile(groupid, fileid, Constants.GZIP_COMPRESSION, data, null, false, false, -1, -1);
            }
            double percentage = (double) seqid / (double) num_npcs * 100D;
            if (seqid == num_npcs - 1 || percentage - last >= 1.0D) {
                System.out.println("\t ^ Percentage Completed: " + format.format(percentage) + "%");
                last = percentage;
            }
        }
        System.out.println("\t ^ Rewriting table..");
        nsrs_cache.getIndexes()[index].rewriteTable();
        System.out.println("\t ^ Finished!");
        System.out.println();
    }

    private static void transport_seqs(Store osrs_cache, Store nsrs_cache, int offset) {
        final int num_npcs = osrs_cache.getIndexes()[2].getLastFileId(12) + 1;
        double last = 0.0D;
        System.out.println("Attempting to transport sequences from osrs..");
        for (int seqid = 0; seqid < num_npcs; seqid++) {
            byte[] data = osrs_cache.getIndexes()[2].getFile(12, seqid);
            if (data != null) {
                int newid = seqid + offset;
                int groupid = newid >>> 7;
                int fileid = newid & 0x7f;
                nsrs_cache.getIndexes()[20].putFile(groupid, fileid, Constants.GZIP_COMPRESSION, data, null, false, false, -1, -1);
            }
            double percentage = (double) seqid / (double) num_npcs * 100D;
            if (seqid == num_npcs - 1 || percentage - last >= 1.0D) {
                System.out.println("\t ^ Percentage Completed: " + format.format(percentage) + "%");
                last = percentage;
            }
        }
        System.out.println("\t ^ Rewriting table..");
        nsrs_cache.getIndexes()[20].rewriteTable();
        System.out.println("\t ^ Finished!");
        System.out.println();
    }

    private static void transport_index(Store source_cache, int source_id, Store target_cache, int target_id, int offset) throws IOException {
        System.out.println("Attempting to transport index from source id of " + source_id + " and target id of " + target_id);
        Index source_index = source_cache.getIndexes()[source_id];
        if (target_cache.getIndexes().length <= target_id) {
            if (target_cache.getIndexes().length != target_id) {
                throw new IllegalStateException("The cache has more than one gap between the source_index and the target_index!");
            }
            target_cache.addIndex(source_index.getTable().isNamed(), source_index.getTable().usesWhirpool(), Constants.GZIP_COMPRESSION);
            System.out.println("\t ^ Index was created!");
        }

        Index target_index = target_cache.getIndexes()[target_id];
        int num_groups = source_index.getLastArchiveId() + 1;
        System.out.println("\t ^ Attempting to pack " + num_groups + " group(s)..");

        double last = 0.0D;
        for (int group_id = 0; group_id < num_groups; group_id++) {
            if (source_index.archiveExists(group_id)) {
                target_index.putArchive(source_id, group_id, group_id + offset, source_cache, false, true);
            }
            double percentage = (double) group_id / (double) num_groups * 100D;
            if (group_id == num_groups - 1 || percentage - last >= 1.0D) {
                System.out.println("\t ^ Percentage Completed: " + format.format(percentage) + "%");
                last = percentage;
            }
        }
        System.out.println("\t ^ Rewriting table..");
        target_index.rewriteTable();
        System.out.println("\t ^ Finished!");
        System.out.println();

    }

    public static void transport_index(Store source_cache, int source_id, Store target_cache, int target_id, int offset,
                                       boolean newOnly) throws IOException {
        System.out.println(
                "Attempting to transport index from source id of " + source_id + " and target id of " + target_id);
        Index source_index = source_cache.getIndexes()[source_id];
        if (target_cache.getIndexes().length <= target_id) {
            if (target_cache.getIndexes().length != target_id) {
                throw new IllegalStateException(
                        "The cache has more than one gap between the source_index and the target_index!");
            }
            target_cache.addIndex(source_index.getTable().isNamed(), source_index.getTable().usesWhirpool(),
                    Constants.GZIP_COMPRESSION);
            System.out.println("\t ^ Index was created!");
        }

        Index target_index = target_cache.getIndexes()[target_id];
        int source_num_groups = source_index.getLastArchiveId() + 1;
        int target_num_groups = target_index.getLastArchiveId() + 1;
        int groupsToPack = source_num_groups;
        System.out.println("\t ^ Attempting to pack " + groupsToPack + (newOnly ? (" new") : "") + " group(s)..");

        double last = 0.0D;
        for (int group_id = 0; group_id < source_num_groups; group_id++) {
            if (source_index.archiveExists(group_id)) {
                byte[] data = source_index.getFile(group_id);

                if (data != null) {
                    target_index.putFile(offset + group_id, 0, Constants.GZIP_COMPRESSION, data, null, false, false, -1, -1);
                }
                double percentage = ((double) (group_id - (newOnly ? target_num_groups : 0))) / (double) groupsToPack
                        * 100D;
                if (group_id == source_num_groups - 1 || percentage - last >= 1.0D) {
                    System.out.println("\t ^ Percentage Completed: " + format.format(percentage) + "%");
                    last = percentage;
                }
            }
        }
        System.out.println("\t ^ Rewriting table..");
        target_index.rewriteTable();
        System.out.println("\t ^ Finished!");
        System.out.println();
    }

    private static final Map<Long, Set<int[]>> XTEAS = new HashMap<>();//

    private static void initializeJSONXteas() {
        System.out.println("Extracting xteas from json file");
        Gson gson = new GsonBuilder().create();
        try(FileReader reader = new FileReader("G:\\[Learning] RSPS\\xteas\\202xteas.json")) {
            ArrayList<Xtea> xteas = gson.fromJson(reader, new TypeToken<ArrayList<Xtea>>() {
            }.getType());

            for (Xtea xtea : xteas) {
                XTEAS.put(xtea.name_hash, Set.of(xtea.key));
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private static void initialiseXteas() throws IOException {
        XTEAS.put((long) -1155319887, Set.of(new int[]{-722072550,1657307114,-166459129,-811776836}));
    }

    private static final class Xtea {

        public int archive;
        public int group;
        public long name_hash;
        public String name;
        public int mapsquare;
        public int[] key;

        @Override
        public String toString() {
            return String.format("[archive=%s, group=%s, hash=%s, name='%s', mapsquare=%s, key=%s]", archive, group, name_hash, name, mapsquare, Arrays.toString(key));
        }
    }
}
