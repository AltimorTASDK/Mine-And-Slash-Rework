package com.robertx22.mine_and_slash.maps;

import com.robertx22.mine_and_slash.mmorpg.MMORPG;
import com.robertx22.mine_and_slash.uncommon.datasaving.Load;
import com.robertx22.mine_and_slash.uncommon.utilityclasses.WorldUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

// todo this isnt working super correctly
public class MobUnloading {

    public static void onUnloadMob(LivingEntity en) {
        try {
            if (WorldUtils.isMapWorldClass(en.level())) {
                var cp = new ChunkPos(en.blockPosition());
                if (en.level().hasChunk(cp.x, cp.z)) {
                    var chunk = en.level().getChunkAt(en.blockPosition());

                    if (MMORPG.RUN_DEV_TOOLS) {
                        var nearestPlayer = en.level().getNearestPlayer(en, -1);
                        if (nearestPlayer != null) {
                            nearestPlayer.sendSystemMessage(Component.literal("Mob Despawned: ").withStyle(ChatFormatting.RED).append(en.getDisplayName()));
                        }
                    }

                    Load.chunkData(chunk).trySaveMob(en);


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadBackMobs(Level level, ChunkPos cp) {
        try {
            if (WorldUtils.isMapWorldClass(level)) {
                // we dont want to force load chunks
                if (level.hasChunk(cp.x, cp.z)) {
                    var chunk = level.getChunk(cp.x, cp.z);
                    Load.chunkData(chunk).tryLoadMobs(level);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
