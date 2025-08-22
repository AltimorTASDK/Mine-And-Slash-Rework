package com.robertx22.mine_and_slash.vanilla_mc.packets;

import com.robertx22.mine_and_slash.database.data.spells.components.Spell;
import com.robertx22.mine_and_slash.database.registry.ExileDB;
import com.robertx22.mine_and_slash.mmorpg.SlashRef;
import com.robertx22.library_of_exile.main.MyPacket;
import com.robertx22.library_of_exile.main.Packets;
import com.robertx22.library_of_exile.packets.ExilePacketContext;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class GetSpellStatsRequestPacket extends MyPacket<GetSpellStatsRequestPacket> {

    public int id;
    public String spellid;

    public GetSpellStatsRequestPacket() {

    }

    public GetSpellStatsRequestPacket(Entity entity, Spell spell) {
        this.id = entity.getId();
        this.spellid = spell.GUID();
    }

    @Override
    public ResourceLocation getIdentifier() {
        return new ResourceLocation(SlashRef.MODID, "getspellstatsrequest");
    }

    @Override
    public void loadFromData(FriendlyByteBuf tag) {
        id = tag.readInt();
        spellid = tag.readUtf();
    }

    @Override
    public void saveToData(FriendlyByteBuf tag) {
        tag.writeInt(id);
        tag.writeUtf(spellid);
    }

    @Override
    public void onReceived(ExilePacketContext ctx) {
        if (!ExileDB.Spells().isRegistered(spellid)) {
            return;
        }

        Entity entity = ctx.getPlayer().level().getEntity(id);

        if (entity instanceof Player player) {
            Spell spell = ExileDB.Spells().get(spellid);
            Packets.sendToClient(ctx.getPlayer(), new GetSpellStatsReplyPacket(player, spell));
        }
    }

    @Override
    public MyPacket<GetSpellStatsRequestPacket> newInstance() {
        return new GetSpellStatsRequestPacket();
    }
}
