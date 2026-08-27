package pl.mlynek.commons.resolver;

import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.permission.MissingPermissionsHandler;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.mlynek.commons.utils.message.MessageType;
import pl.mlynek.commons.utils.message.PlayerMessage;

public class PermissionResolver implements MissingPermissionsHandler<CommandSender> {
    @Override
    public void handle(Invocation<CommandSender> invocation, MissingPermissions missingPermissions, ResultHandlerChain<CommandSender> chain) {
        Player p = (Player) invocation.sender();
        String msg = "<red>Nie posiadasz permisji <#FF0000>" + missingPermissions.asJoinedText();
        PlayerMessage.message(p, MessageType.CHAT, msg);
        PlayerMessage.message(p, MessageType.SUBTITLE, msg);
        PlayerMessage.message(p, MessageType.ACTIONBAR, msg);
        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
    }
}
