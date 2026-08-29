package pl.mlynek.commons.registry;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.rollczi.litecommands.bukkit.LiteBukkitMessages;
import dev.rollczi.litecommands.bukkit.LiteBukkitSettings;
import dev.rollczi.litecommands.context.ContextProvider;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import pl.mlynek.commons.registry.entry.ArgumentEntry;
import pl.mlynek.commons.registry.entry.ContextEntry;
import pl.mlynek.commons.resolver.InvalidUsageResolver;
import pl.mlynek.commons.resolver.PermissionResolver;
import pl.mlynek.commons.utils.TimeUtil;
import pl.mlynek.commons.utils.adventure.AdventureUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Author: mlyn3kk_
 * @Website: https://discord.gg/swircode
 * @Date: 24.08.2026
 * @Project: mCore-server
 * @Description: szkidbi eszkere gigachad
 */
@Getter
@Setter
public class CommandsRegistry {
    private LiteCommands<CommandSender> liteCommands;
    private InvalidUsageResolver invalidUsageResolver = new InvalidUsageResolver();
    private PermissionResolver permissionsResolver = new PermissionResolver();
    private List<Object> commands = new ArrayList<>();
    private List<ContextEntry<?>> contexts = new ArrayList<>();
    private List<ArgumentEntry<?>> arguments = new ArrayList<>();

    public CommandsRegistry() {}

    public CommandsRegistry implementCommand(@NonNull Object o) {
        if (!this.commands.contains(o)) {
            this.commands.add(o);
        }
        return this;
    }

    public CommandsRegistry implementCommands(@NonNull Object... commands) {
        for (Object command : commands) {
            this.implementCommand(command);
        }
        return this;
    }

    public CommandsRegistry implementCommands(@NonNull Collection<Object> commands) {
        for (Object command : commands) {
            if (command != null) {
                this.implementCommand(command);
            }
        }
        return this;
    }

    public <T> CommandsRegistry implementArgument(@NonNull Class<T> type, @NonNull ArgumentResolver<CommandSender, T> argument) {
        this.arguments.add(new ArgumentEntry<>(type, null, argument));
        return this;
    }

    public <T> CommandsRegistry implementArgument(@NonNull Class<T> type, @NonNull ArgumentKey key, @NonNull ArgumentResolver<CommandSender, T> argument) {
        this.arguments.add(new ArgumentEntry<>(type, key, argument));
        return this;
    }

    public <T> CommandsRegistry implementContext(@NonNull Class<T> type, @NonNull ContextProvider<CommandSender, T> context) {
        this.contexts.add(new ContextEntry<>(type, context));
        return this;
    }

    public void build(Plugin plugin, String commandsPrefix) {
        LiteCommandsBuilder<CommandSender, LiteBukkitSettings, ?> builder = LiteBukkitFactory.builder(commandsPrefix, plugin).invalidUsage(this.invalidUsageResolver).missingPermission(this.permissionsResolver);
        builder.message(LiteBukkitMessages.PLAYER_ONLY, "&cTylko gracz może użyć tej komendy!").message(LiteBukkitMessages.LOCATION_INVALID_FORMAT, input -> AdventureUtil.translate("&#FF0000:( &cNiepoprawny format lokacji: &#FF0000" + input)).message(LiteBukkitMessages.COMMAND_COOLDOWN, input -> AdventureUtil.translate("&#FF0000:( &cNastepny raz ta komende bedziesz mogl użyć za &#FF0000" + TimeUtil.formatTimeSimple(input.getRemainingDuration().toMillis()))).message(LiteBukkitMessages.PLAYER_NOT_FOUND, input -> AdventureUtil.translate("&#FF0000:( &cGracz &#FF0000" + input + " &cnie zostal odnaleziony!")).message(LiteBukkitMessages.WORLD_NOT_EXIST, input -> AdventureUtil.translate("&#FF0000:( &cŚwiat &#FF0000" + input + " &cnie zostal odnaleziony!"));
        this.contexts.forEach(context -> this.addContext(builder, context));
        this.arguments.forEach(argument -> this.addArgument(builder, argument));
        builder.commands(this.commands.toArray());
        this.liteCommands = builder.build();
        plugin.getLogger().info("Zaladowano " + this.commands.size() + " komend, " + this.arguments.size() + " argumentow oraz " + this.contexts.size() + " kontekstow!");
    }

    private <T> void addContext(LiteCommandsBuilder<CommandSender, LiteBukkitSettings, ?> builder, ContextEntry<T> entry) {
        builder.context(entry.getType(), entry.getContext());
    }

    private <T> void addArgument(LiteCommandsBuilder<CommandSender, LiteBukkitSettings, ?> builder, ArgumentEntry<T> entry) {
        if (entry.getKey() != null) {
            builder.argument(entry.getType(), entry.getKey(), entry.getArgument());
        } else {
            builder.argument(entry.getType(), entry.getArgument());
        }
    }
}