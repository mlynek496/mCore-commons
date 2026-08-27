package pl.mlynek.commons.registry.entry;

import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolverBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.command.CommandSender;

/**
 * @Author: mlyn3kk_
 * @Website: https://discord.gg/swircode
 * @Date: 24.08.2026
 * @Project: mCore-server
 * @Description: szkidbi eszkere gigachad
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArgumentEntry<T> {
    private Class<T> type;
    private ArgumentKey key;
    private ArgumentResolverBase<CommandSender, T> argument;
}