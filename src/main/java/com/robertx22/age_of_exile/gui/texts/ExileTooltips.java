package com.robertx22.age_of_exile.gui.texts;

import com.robertx22.age_of_exile.gui.texts.textblocks.*;
import com.robertx22.age_of_exile.gui.texts.textblocks.usableitemblocks.DragableBlock;
import com.robertx22.age_of_exile.uncommon.utilityclasses.TooltipUtils;
import com.robertx22.library_of_exile.wrappers.ExileText;
import lombok.NoArgsConstructor;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor
public class ExileTooltips {
    public static Component EMPTY_LINE = Component.literal("");

    private final List<AbstractTextBlock> blockContainer = new ArrayList<>();
    //use this map to standardize the same blocks name in different tooltips.


    public ExileTooltips accept(AbstractTextBlock block) {
        blockContainer.add(block);
        return this;
    }


    public List<Component> release() {
        IgnoreNullList<Component> list = new IgnoreNullList<>();

        MutableComponent emptyLine = ExileText.emptyLine().get();
        Map<BlockCategories, List<AbstractTextBlock>> collect = blockContainer.stream().collect(Collectors.groupingBy(AbstractTextBlock::getCategory));


        //handle the req, stat. Should be noticed that the order of these blocks are fixed, and thats the point in order to maintain the style consistency of tooltips.
        Optional.ofNullable(collect.get(BlockCategories.NAME))
                .map(x -> x.get(0))
                .map(AbstractTextBlock::getAvailableComponents)
                .ifPresent(x -> {
                    List<AbstractTextBlock> rarity = collect.get(BlockCategories.RARITY);
                    if (rarity != null) {
                        for (Component component : x) {
                            list.add(component.copy().withStyle(((RarityBlock) rarity.get(0)).rarity.textFormatting()));
                        }
                    } else {
                        for (Component component : x) {
                            list.add(component.copy().withStyle(ChatFormatting.WHITE));
                        }
                    }
                });

        list.add(emptyLine);

        Stream.of(
                        collect.get(BlockCategories.REQUIREMENT),
                        collect.get(BlockCategories.STAT),
                        collect.get(BlockCategories.USAGE),
                        collect.get(BlockCategories.LEVELED_ITEM_LEVEL)
                        )
                .filter(Objects::nonNull)
                .map(x -> x.get(0))
                .filter(x -> !x.getAvailableComponents().isEmpty())
                .forEachOrdered(x -> {
                    list.addAll(x.getAvailableComponents());
                    list.add(emptyLine);
                });

        //handle additional blocks, the order of aBs is the putting order.
        List<AbstractTextBlock> additions = collect.get(BlockCategories.ADDITIONAL);
        if (additions != null){
            additions.sort(Comparator.comparing(x -> x instanceof DragableBlock));
            for (AbstractTextBlock abstractTextBlock : additions) {
                if (abstractTextBlock != null && !abstractTextBlock.getAvailableComponents().isEmpty()) {
                    list.addAll(abstractTextBlock.getAvailableComponents());
                    list.add(emptyLine);
                }
            }
        }


        Stream.of(
                        collect.get(BlockCategories.RARITY),
                        collect.get(BlockCategories.DURABILITY)
                )
                .filter(Objects::nonNull)
                .map(x -> x.get(0))
                .filter(x -> !x.getAvailableComponents().isEmpty())
                .forEachOrdered(x -> {
                    list.addAll(x.getAvailableComponents());
                });

        list.add(emptyLine);
        Optional.ofNullable(collect.get(BlockCategories.OPERATION))
                .map(x -> x.get(0))
                .map(AbstractTextBlock::getAvailableComponents)
                .ifPresent(list::addAll);


        if (list.get(list.size() - 1).getString().isBlank()) {
            list.remove(list.size() - 1);
        }

        List<Component> postEditList = TooltipUtils.splitLongText(list);

        return postEditList;

    }

    public enum BlockCategories {
        NAME,
        RARITY,
        REQUIREMENT,
        STAT,
        DURABILITY,
        USAGE,
        LEVELED_ITEM_LEVEL,
        OPERATION,
        ADDITIONAL

    }

}
