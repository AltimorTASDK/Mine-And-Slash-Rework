package com.robertx22.age_of_exile.maps.generator;


import com.google.common.base.Preconditions;
import com.robertx22.age_of_exile.maps.DungeonRoom;
import com.robertx22.age_of_exile.maps.MapData;
import com.robertx22.age_of_exile.maps.RoomGroup;
import com.robertx22.library_of_exile.utils.RandomUtils;
import net.minecraft.world.level.ChunkPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class DungeonBuilder {


    public DungeonBuilder(long worldSeed, ChunkPos cpos) {

        int chunkX = MapData.getStartChunk(cpos.getMiddleBlockPosition(55)).x;
        int chunkZ = MapData.getStartChunk(cpos.getMiddleBlockPosition(55)).z;

        long newSeed = (worldSeed + (long) (chunkX * chunkX * 4987142) + (long) (chunkX * 5947611) + (long) (chunkZ * chunkZ) * 4392871L + (long) (chunkZ * 389711) ^ worldSeed);
        rand = new Random(newSeed);

        this.group = RandomUtils.weightedRandom(RoomGroup.getAll()
                .stream()
                .filter(x -> x.canBeMainTheme)
                .collect(Collectors.toList()), rand.nextDouble());

        this.size = RandomUtils.RandomRange(15, 25); // todo this needs the same random if i'll use at world gen async, if i do it myself, it doesnt

        if (RandomUtils.roll(5, rand)) {
            this.maxBossRooms++;
        }
    }

    public int maxPuzzleBlockRooms = 3;

    public boolean debug = false;
    public Dungeon dungeon;
    public final Random rand;
    public int size;
    public boolean isTesting = false;
    RoomGroup group;
    public int maxBossRooms = 1;

    public void build() {
        dungeon = new Dungeon(size);

        setupEntrance();

        dungeon.setupBarriers();


        int tries = 0;

        while (!dungeon.isFinished() || tries < 500) {

            tries++;

            dungeon.getUnbuiltCopy()
                    .forEach(x -> {

                        try {
                            UnbuiltRoom unbuilt = dungeon.getUnbuiltFor(x.x, x.y);

                            Preconditions.checkNotNull(unbuilt);

                            RoomRotation rot = randomDungeonRoom(unbuilt);

                            Preconditions.checkNotNull(rot);

                            DungeonRoom dRoom = rot.type.getRandomRoom(group, this);

                            Preconditions.checkNotNull(dRoom);

                            BuiltRoom room = new BuiltRoom(rot, dRoom);

                            Preconditions.checkNotNull(room);

                            dungeon.addRoom(x.x, x.y, room);

                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                    });
        }

        dungeon.fillWithBarriers();


    }

    public RoomRotation randomDungeonRoom(UnbuiltRoom unbuilt) {

        if (dungeon.shouldStartFinishing()) {
            List<RoomRotation> pos = RoomType.END.getPossibleFor(unbuilt);
            if (pos.isEmpty()) { //its not possible to set end for all of them
                return randomRoom(unbuilt);
            } else {
                return random(pos);
            }
        } else {
            return randomRoom(unbuilt);
        }
    }

    public RoomRotation randomRoom(UnbuiltRoom unbuilt) {
        List<RoomType> types = new ArrayList<>();
        types.add(RoomType.CURVED_HALLWAY);
        types.add(RoomType.STRAIGHT_HALLWAY);
        types.add(RoomType.FOUR_WAY);
        types.add(RoomType.TRIPLE_HALLWAY);

        List<RoomRotation> possible = new ArrayList<>();

        types.forEach(x -> {
            possible.addAll(x.getPossibleFor(unbuilt));
        });

        if (possible.isEmpty()) {

            // we dont want to end things fast, but if there's nothing else that matches, add an end.
            possible.addAll(RoomType.END.getPossibleFor(unbuilt));

            if (possible.isEmpty()) {
                throw new RuntimeException("No possible rooms at all for unbuilt room, this is horrible.");
            }
        }

        return random(possible);
    }

    public RoomRotation random(List<RoomRotation> list) {
        return RandomUtils.weightedRandom(list, rand.nextDouble());
    }

    private void setupEntrance() {
        DungeonRoom entranceRoom = RoomType.ENTRANCE.getRandomRoom(group, this);

        List<RoomRotation> possible = new ArrayList<>();
        possible.addAll(RoomType.ENTRANCE.getRotations());
        RoomRotation rotation = random(possible);

        BuiltRoom entrance = new BuiltRoom(rotation, entranceRoom);

        int mid = dungeon.getMiddle();
        dungeon.addRoom(mid, mid, entrance);
    }

}