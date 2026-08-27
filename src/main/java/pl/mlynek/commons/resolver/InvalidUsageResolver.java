package pl.mlynek.commons.resolver;

import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invalidusage.InvalidUsageHandler;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.schematic.Schematic;
import org.bukkit.command.CommandSender;
import pl.mlynek.commons.utils.message.MessageType;
import pl.mlynek.commons.utils.message.PlayerMessage;

public class InvalidUsageResolver implements InvalidUsageHandler<CommandSender> {

    @Override
    public void handle(Invocation<CommandSender> invocation, InvalidUsage<CommandSender> result, ResultHandlerChain<CommandSender> chain) {
        CommandSender sender = invocation.sender();
        Schematic schematic = result.getSchematic();
        if (schematic.isOnlyFirst()) {
            PlayerMessage.message(sender, MessageType.CHAT, "<red>Poprawne użycie: <#FF0000>" + schematic.first());
            return;
        }
        PlayerMessage.message(sender, MessageType.CHAT, "<red>Poprawne użycie:");
        for (String scheme : schematic.all()) {
            PlayerMessage.message(sender, MessageType.CHAT, "<dark_gray> - <#FF0000>" + scheme);
        }
    }
}