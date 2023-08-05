package com.robertx22.age_of_exile.damage_hooks.util;

import com.robertx22.age_of_exile.saveclasses.item_classes.GearItemData;
import com.robertx22.age_of_exile.uncommon.datasaving.Gear;
import com.robertx22.age_of_exile.uncommon.datasaving.Load;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;

public class WeaponFinderUtil {

    public static ItemStack getWeapon(DamageSource source) {

        if (source.getEntity() instanceof LivingEntity == false) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = ((LivingEntity) source.getEntity()).getMainHandItem();
        GearItemData gear = Gear.Load(stack);

        if (gear == null) {

            try {
                Entity sourceEntity = source
                    .getDirectEntity();
                Entity attacker = source
                    .getEntity();

                if (sourceEntity != null && !(sourceEntity instanceof LivingEntity)) {
                    if (attacker instanceof LivingEntity) {

                        stack = getWeaponStackFromThrownEntity(sourceEntity);
                        gear = Gear.Load(stack);

                        if (gear == null) {
                            stack = ItemStack.EMPTY;
                        } else {
                            Load.Unit(attacker)
                                .setEquipsChanged(true);
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (gear != null) {
            return stack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    private static ItemStack getWeaponStackFromThrownEntity(Entity en) {

        /*
        for (Field field : en.getClass()
            .getFields()) {

            if (field.getType()
                .isAssignableFrom(ItemStack.class)) {
                try {
                    ItemStack stack = (ItemStack) field.get(en);
                    GearItemData gear = Gear.Load(stack);
                    if (gear != null) {
                        return stack;
                    }
                } catch (Exception e) {

                }

            }
        }
         */

        try {
            for (SynchedEntityData.DataItem<?> entry : en.getEntityData()
                .getAll()) {
                if (entry.getValue() instanceof ItemStack) {
                    GearItemData gear = Gear.Load((ItemStack) entry.getValue());
                    if (gear != null) {
                        return (ItemStack) entry.getValue();
                    }
                }
            }
        } catch (Exception e) {
        }

        try {
            CompoundTag nbt = new CompoundTag();
            en.saveWithoutId(nbt);

            ItemStack stack = ItemStack.EMPTY;

            for (String key : nbt.getAllKeys()) {
                if (stack == null || stack.isEmpty()) {
                    try {
                        if (nbt.get(key) instanceof CompoundTag) {
                            ItemStack s = tryGetStackFromNbt(nbt.get(key));

                            if (!s.isEmpty() && Gear.has(s)) {
                                return s;
                            }

                        } else {

                            CompoundTag nbt2 = (CompoundTag) nbt.get(key);

                            for (String key2 : nbt2.getAllKeys()) {
                                if (nbt.get(key) instanceof CompoundTag) {
                                    ItemStack s2 = tryGetStackFromNbt(nbt2.get(key2));
                                    if (!s2.isEmpty() && Gear.has(s2)) {
                                        return s2;
                                    }

                                }
                            }

                        }
                    } catch (Exception e) {
                    }
                }

            }

            ItemStack tryWholeNbt = ItemStack.of(nbt);

            if (tryWholeNbt != null) {
                GearItemData gear = Gear.Load(tryWholeNbt);
                if (gear != null) {
                    return tryWholeNbt;
                }
            }

            if (stack == null) {
                stack = ItemStack.EMPTY;
            }

            return stack;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ItemStack.EMPTY;
    }

    private static ItemStack tryGetStackFromNbt(Tag nbt) {
        if (nbt instanceof CompoundTag) {
            ItemStack s = ItemStack.of((CompoundTag) nbt);
            if (s != null && !s.isEmpty()) {
                return s;

            }
        }

        return ItemStack.EMPTY;
    }
}
