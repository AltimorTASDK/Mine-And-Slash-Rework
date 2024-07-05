package com.robertx22.age_of_exile.database.data.currency;

import com.google.common.collect.ImmutableList;
import com.robertx22.age_of_exile.aoe_data.datapacks.models.IAutoModel;
import com.robertx22.age_of_exile.aoe_data.datapacks.models.ItemModelManager;
import com.robertx22.age_of_exile.database.data.currency.base.Currency;
import com.robertx22.age_of_exile.database.data.currency.base.GearCurrency;
import com.robertx22.age_of_exile.database.data.currency.base.GearOutcome;
import com.robertx22.age_of_exile.gui.texts.ExileTooltips;
import com.robertx22.age_of_exile.gui.texts.textblocks.AdditionalBlock;
import com.robertx22.age_of_exile.gui.texts.textblocks.usableitemblocks.DragableBlock;
import com.robertx22.age_of_exile.gui.texts.textblocks.usableitemblocks.UsageBlock;
import com.robertx22.age_of_exile.uncommon.interfaces.IAutoLocDesc;
import com.robertx22.age_of_exile.uncommon.interfaces.IAutoLocName;
import com.robertx22.age_of_exile.uncommon.localization.Words;
import com.robertx22.library_of_exile.registry.IWeighted;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.Collections;
import java.util.List;

public class CurrencyItem extends Item implements IItemAsCurrency, IAutoLocName, IAutoModel, IAutoLocDesc {

    Currency effect;

    public CurrencyItem(Currency effect) {
        super(new Item.Properties());
        this.effect = effect;


    }


    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip,
                                TooltipFlag flagIn) {

        ExileTooltips exileTooltips = new ExileTooltips()
                .accept(new UsageBlock(ImmutableList.of(
                        locDesc().withStyle(ChatFormatting.YELLOW)
                )))
                .accept(new DragableBlock(DragableBlock.AvailableTarget.GEAR));


        if (effect instanceof GearCurrency gc) {
            for (GearOutcome o : gc.getOutcomes()) {
                exileTooltips.accept(new UsageBlock(Collections.singletonList(o.getTooltip(gc.getOutcomes().stream().mapToInt(IWeighted::Weight).sum()))));
            }
            if (gc.spendsGearPotential()) {
                exileTooltips.accept(new AdditionalBlock(Collections.singletonList(Words.POTENTIAL_COST.locName(gc.getPotentialLoss()))));
            } else {
                exileTooltips.accept(new AdditionalBlock(Collections.singletonList(Words.NOT_A_POTENTIAL_CONSUMER.locName())));
            }
        }

        tooltip.addAll(exileTooltips.release());

    }

    @Override
    public Currency currencyEffect(ItemStack stack) {
        return effect;
    }

    @Override
    public void generateModel(ItemModelManager manager) {
        manager.generated(this);
    }

    @Override
    public AutoLocGroup locNameGroup() {
        return AutoLocGroup.Currency_Items;
    }

    @Override
    public String locNameLangFileGUID() {
        return effect.locNameLangFileGUID();
    }

    @Override
    public String locNameForLangFile() {
        return effect.locNameForLangFile();
    }

    @Override
    public String GUID() {
        return effect.GUID();
    }

    @Override
    public AutoLocGroup locDescGroup() {

        return effect.locDescGroup();
    }

    @Override
    public String locDescLangFileGUID() {
        return effect.locDescLangFileGUID();
    }

    @Override
    public String locDescForLangFile() {
        return effect.locDescForLangFile();
    }
}
