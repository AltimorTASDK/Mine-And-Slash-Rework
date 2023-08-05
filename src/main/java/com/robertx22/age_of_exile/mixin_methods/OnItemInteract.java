package com.robertx22.age_of_exile.mixin_methods;

import com.robertx22.age_of_exile.database.data.currency.base.ICurrencyItemEffect;
import com.robertx22.age_of_exile.database.data.currency.loc_reqs.LocReqContext;
import com.robertx22.age_of_exile.mmorpg.registers.common.items.SlashItems;
import com.robertx22.age_of_exile.saveclasses.item_classes.GearItemData;
import com.robertx22.age_of_exile.saveclasses.stat_soul.StatSoulData;
import com.robertx22.age_of_exile.saveclasses.stat_soul.StatSoulItem;
import com.robertx22.age_of_exile.uncommon.datasaving.Gear;
import com.robertx22.age_of_exile.uncommon.datasaving.StackSaving;
import com.robertx22.age_of_exile.uncommon.interfaces.data_items.ISalvagable;
import com.robertx22.age_of_exile.uncommon.utilityclasses.PlayerUtils;
import com.robertx22.age_of_exile.vanilla_mc.items.misc.SalvagedDustItem;
import com.robertx22.library_of_exile.utils.SoundUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class OnItemInteract {

    public static void on(AbstractContainerMenu screen, int i, int j, ClickType slotActionType, Player player, CallbackInfo ci) {

        if (slotActionType != ClickType.PICKUP) {
            return;
        }

        ItemStack cursor = player.getInventory().getSelected(); // this was getcarried, test

        if (!cursor.isEmpty()) {

            Slot slot = null;
            try {
                slot = screen.slots.get(i);
            } catch (Exception e) {
            }
            if (slot == null) {
                return;
            }

            ItemStack stack = slot.getItem();

            boolean success = false;

            if (stack.isDamaged() && cursor.getItem() instanceof SalvagedDustItem) {

                GearItemData gear = Gear.Load(stack);

                if (gear == null) {
                    return;
                }

                SalvagedDustItem essence = (SalvagedDustItem) cursor.getItem();

                SoundUtils.playSound(player, SoundEvents.ANVIL_USE, 1, 1);

                int repair = essence.durabilityRepair;

                stack.setDamageValue(stack.getDamageValue() - repair);
                success = true;

            } else if (cursor.getItem() instanceof StatSoulItem) {
                StatSoulData data = StackSaving.STAT_SOULS.loadFrom(cursor);

                if (data != null) {
                    if (data.canInsertIntoStack(stack)) {
                        data.insertAsUnidentifiedOn(stack);
                        success = true;
                    }
                }
            } else if (cursor.getItem() instanceof ICurrencyItemEffect) {
                LocReqContext ctx = new LocReqContext(player, stack, cursor);
                if (ctx.effect.canItemBeModified(ctx)) {
                    ItemStack result = ctx.effect.modifyItem(ctx).stack;
                    stack.shrink(1);
                    slot.set(result);
                    success = true;
                }
            } else if (cursor.getItem() == SlashItems.SALVAGE_HAMMER.get()) {
                ISalvagable data = ISalvagable.load(stack);

                if (data == null && stack.getItem() instanceof ISalvagable) {
                    data = (ISalvagable) stack.getItem();
                }

                if (data != null) {
                    SoundUtils.playSound(player, SoundEvents.ANVIL_USE, 1, 1);

                    stack.shrink(1);
                    data.getSalvageResult(stack)
                            .forEach(x -> PlayerUtils.giveItem(x, player));
                    //ci.setReturnValue(ItemStack.EMPTY);
                    stack.shrink(1000);
                    ci.cancel();
                    return;
                }
            } else if (cursor.getItem() == SlashItems.SOCKET_EXTRACTOR.get()) {

                GearItemData gear = Gear.Load(stack);

                if (gear != null) {
                    if (gear.sockets != null && gear.sockets.sockets.size() > 0) {
                        try {
                            ItemStack gem = new ItemStack(gear.sockets.sockets.get(0)
                                    .getGem()
                                    .getItem());
                            gear.sockets.sockets.remove(0);
                            Gear.Save(stack, gear);
                            PlayerUtils.giveItem(gem, player);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        stack.shrink(1000);
                        // ci.setReturnValue(ItemStack.EMPTY);
                        ci.cancel();
                        return;
                    }
                }
            }

            if (success) {
                SoundUtils.ding(player.level(), player.blockPosition());
                SoundUtils.playSound(player.level(), player.blockPosition(), SoundEvents.ANVIL_USE, 1, 1);

                cursor.shrink(1);
                stack.shrink(1000);
                //ci.setReturnValue(ItemStack.EMPTY);
                ci.cancel();
            }

        }
    }
}
