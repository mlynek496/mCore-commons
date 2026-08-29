package pl.mlynek.commons.utils;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @Author: mlyn3kk_
 * @Website: https://discord.gg/swircode
 * @Date: 29.08.2026
 * @Project: mCore-commons
 * @Description: szkidbi eszkere gigachad
 */
public final class AdventureUtil {
    private static final LegacyComponentSerializer SERIALIZER = LegacyComponentSerializer.builder().character('&').hexCharacter('#').hexColors().extractUrls().useUnusualXRepeatedCharacterHexFormat().build();
    private static final LegacyComponentSerializer SECTION_SERIALIZER = LegacyComponentSerializer.builder().character('\u00a7').hexCharacter('#').hexColors().extractUrls().useUnusualXRepeatedCharacterHexFormat().build();
    private static final PlainTextComponentSerializer PLAIN_TEXT_COMPONENT_SERIALIZER = PlainTextComponentSerializer.plainText();
    private static final MiniMessage MINI = MiniMessage.miniMessage();

    private AdventureUtil() {}

    public static String legacy(String string) {
        return SECTION_SERIALIZER.serialize(SERIALIZER.deserialize(string));
    }

    private static Component stripItalics(Component component) {
        return component.decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    public static Component translate(String string) {
        Component base = SERIALIZER.deserializeOrNull(string);
        return AdventureUtil.stripItalics(base);
    }

    public static List<Component> translate(String... strings) {
        return Stream.of(strings).map(AdventureUtil::translate).toList();
    }

    public static List<Component> translate(List<String> strings) {
        return strings.stream().map(AdventureUtil::translate).toList();
    }

    public static TextComponent textComponentOf(String string) {
        Component base = SERIALIZER.deserializeOrNull(string);
        return (TextComponent) AdventureUtil.stripItalics(base);
    }

    public static List<TextComponent> textComponentsOf(String... strings) {
        return Stream.of(strings).map(AdventureUtil::textComponentOf).toList();
    }

    public static List<TextComponent> textComponentsOf(List<String> strings) {
        return strings.stream().map(AdventureUtil::textComponentOf).toList();
    }

    public static String componentToString(Component component) {
        if (component == null) return "";
        return PLAIN_TEXT_COMPONENT_SERIALIZER.serialize(component);
    }

    public static Component miniMessage(String message, Map<String, String> placeholders) {
        if (message == null) {
            return Component.empty();
        }
        String parsed = message;
        if (placeholders != null && !placeholders.isEmpty()) {
            for (Map.Entry<String, String> e : placeholders.entrySet()) {
                parsed = parsed.replace("{" + e.getKey() + "}", e.getValue());
            }
        }
        if (parsed.contains("&")) {
            parsed = legacy(parsed);
        }
        Component comp;
        if (parsed.contains("§")) {
            comp = SECTION_SERIALIZER.deserialize(parsed);
        } else {
            comp = MINI.deserialize(parsed, AdventureUtil.buildPlaceholders(placeholders));
        }
        return AdventureUtil.stripItalics(comp);
    }

    public static List<Component> miniMessage(Map<String, String> placeholders, String... messages) {
        return Stream.of(messages).map(msg -> AdventureUtil.miniMessage(msg, placeholders)).toList();
    }

    public static List<Component> miniMessage(List<String> messages, Map<String, String> placeholders) {
        if (messages == null) return new ArrayList<>();
        return messages.stream().map(msg -> AdventureUtil.miniMessage(msg, placeholders)).toList();
    }

    public static TagResolver buildPlaceholders(Map<String, String> placeholders) {
        if (placeholders != null && !placeholders.isEmpty()) {
            TagResolver.Builder builder = TagResolver.builder();
            placeholders.forEach((key, value) -> builder.resolver(Placeholder.parsed(key, value)));
            return builder.build();
        }
        return TagResolver.empty();
    }

    public static String tpsWithFormat(double tps) {
        return (tps > 20.0 ? "*" : "") + Math.min((float) Math.round(tps * 100.0) / 100.0f, 20.0f);
    }
}
