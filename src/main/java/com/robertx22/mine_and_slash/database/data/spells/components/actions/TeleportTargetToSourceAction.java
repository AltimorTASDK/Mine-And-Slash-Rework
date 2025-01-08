package com.robertx22.mine_and_slash.database.data.spells.components.actions;

import com.robertx22.mine_and_slash.database.data.spells.components.MapHolder;
import com.robertx22.mine_and_slash.database.data.spells.spell_classes.SpellCtx;
import com.google.common.collect.Lists;
import com.robertx22.library_of_exile.utils.EntityUtils;
import com.robertx22.library_of_exile.utils.geometry.MyPosition;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class TeleportTargetToSourceAction extends SpellAction {

    // nudge a single increment in each direction when trying to find the correct side of a block to unclip from
    private static final double NUDGE_SCALE = 1e-6;
    private static final List<Vec3i> NUDGE_OFFSETS = createSearchOffsets(1);

    // check up to two half-block increment in each direction to unclip
    private static final double UNCLIP_SCALE = 0.5;
    private static final List<Vec3i> UNCLIP_OFFSETS = createSearchOffsets(2);

    // create offsets to search in a cube around a position
    private static List<Vec3i> createSearchOffsets(int radius)
    {
        List<Vec3i> offsets = Lists.newArrayList();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    offsets.add(new Vec3i(x, y, z));
                }
            }
        }

        offsets.sort((a, b) -> {
            // sort by horizontal distance, then by vertical distance
            int aDistance = Math.abs(a.getX()) + Math.abs(a.getZ());
            int bDistance = Math.abs(b.getX()) + Math.abs(b.getZ());

            if (aDistance == bDistance) {
                return Math.abs(a.getY()) - Math.abs(b.getY());
            } else {
                return aDistance - bDistance;
            }
        });

        return offsets;
    }

    public TeleportTargetToSourceAction() {
        super(Arrays.asList());
    }

    @Override
    public void tryActivate(Collection<LivingEntity> targets, SpellCtx ctx, MapHolder data) {

        targets.forEach(x -> {
            if (x.level() == ctx.sourceEntity.level()) { // don't allow teleport in wrong dimension
                teleportEntitySafe(x, ctx.sourceEntity.position());
            }
        });

    }

    private void teleportEntitySafe(LivingEntity entity, Vec3 destination) {

        Vec3 tpPosition = findSafeTeleportPosition(entity, destination).add(0.0, -entity.getBbHeight() / 2.0, 0.0);
        EntityUtils.setLoc(entity, new MyPosition(tpPosition).asVector3D(), entity.getYRot(), entity.getXRot());

    }

    // find a safe position to teleport to, getting as close to the destination as possible
    private Vec3 findSafeTeleportPosition(Entity entity, Vec3 destination) {

        Vec3 safePosition = findNearbySafeTeleportPosition(entity, destination);

        if (safePosition == null) {
            return destination;
        }

        // attempt to move the entity to the destination similar to Entity.collide
        AABB aabb = makeAabbForPosition(entity, safePosition);
        Vec3 delta = destination.subtract(safePosition);
        List<VoxelShape> entityCollisions = entity.level().getEntityCollisions(entity, aabb.expandTowards(delta));
        Vec3 clippedDelta = Entity.collideBoundingBox(entity, delta, aabb, entity.level(), entityCollisions);
        return safePosition.add(clippedDelta);

    }

    // only accurate to a block
    private Vec3 findNearbySafeTeleportPosition(Entity entity, Vec3 destination) {

        // search for a valid position in a cube surrounding the destination block
        Vec3 adjustedDestination = nudgePositionOutOfBlock(entity, destination);
        BlockPos blockPos = BlockPos.containing(adjustedDestination);

        for (Vec3i offset : UNCLIP_OFFSETS) {
            Vec3 offsetVec = new Vec3(offset.getX(), offset.getY(), offset.getZ());
            Vec3 testPosition = blockPos.getCenter().add(offsetVec.scale(UNCLIP_SCALE));
            if (canEntityFit(entity, testPosition)) {
                return testPosition;
            }
        }

        return null;

    }

    // teleport projectiles end up at the exact edge of the block they hit, nudge into free space
    private Vec3 nudgePositionOutOfBlock(Entity entity, Vec3 destination) {

        for (Vec3i offset : NUDGE_OFFSETS) {
            Vec3 offsetVec = new Vec3(offset.getX(), offset.getY(), offset.getZ());
            Vec3 testPosition = destination.add(offsetVec.scale(NUDGE_SCALE));
            if (canPointFit(entity, testPosition, NUDGE_SCALE)) {
                return testPosition;
            }
        }

        return destination;

    }

    private boolean canPointFit(Entity entity, Vec3 destination, double size) {
        return entity.level().noCollision(entity, AABB.ofSize(destination, size, size, size));
    }

    private boolean canEntityFit(Entity entity, Vec3 destination) {
        return entity.level().noCollision(entity, makeAabbForPosition(entity, destination));
    }

    private AABB makeAabbForPosition(Entity entity, Vec3 position) {
        return AABB.ofSize(position, entity.getBbWidth(), entity.getBbHeight(), entity.getBbWidth());
    }

    public MapHolder create() {
        MapHolder c = new MapHolder();
        c.type = GUID();
        this.validate(c);
        return c;
    }

    @Override
    public String GUID() {
        return "tp_target_to_self";
    }

}

