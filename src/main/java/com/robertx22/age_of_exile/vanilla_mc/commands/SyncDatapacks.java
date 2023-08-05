package com.robertx22.age_of_exile.vanilla_mc.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.robertx22.age_of_exile.event_hooks.player.OnLogin;
import com.robertx22.age_of_exile.mmorpg.MMORPG;
import net.minecraft.commands.CommandSourceStack;

import static net.minecraft.command.Commands.literal;

public clasnet.minecraft.commands.Commandsstatic void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(
            literal(CommandRefs.ID)
                .then(literal("sync_datapacks").requires(e -> e.hasPermission(2))
                    .executes(
                        ctx -> run())));
    }

    private static int run() {

        try {
            MMORPG.server.getPlayerList()
                .getPlayers()
                .forEach(x -> {
                    OnLogin.onLoad(x);
                });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 1;
    }
}

