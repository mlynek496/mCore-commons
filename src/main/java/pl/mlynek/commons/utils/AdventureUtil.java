package pl.mlynek.commons.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @Author: mlyn3kk_
 * @Website: https://discord.gg/swircode
 * @Date: 22.08.2026
 * @Project: mCore-server
 * @Description: szkidbi eszkere gigachad
 */
public final class AdventureUtil {
    private static final LegacyComponentSerializer SERIALIZER = LegacyComponentSerializer.builder().character('&').hexCharacter('#').hexColors().extractUrls().useUnusualXRepeatedCharacterHexFormat().build();
    private static final LegacyComponentSerializer SECTION_SERIALIZER = LegacyComponentSerializer.builder().character('§').hexCharacter('#').hexColors().extractUrls().useUnusualXRepeatedCharacterHexFormat().build();
    private static final PlainTextComponentSerializer PLAIN_TEXT_COMPONENT_SERIALIZER = PlainTextComponentSerializer.plainText();
    private static final MiniMessage MINI = MiniMessage.miniMessage();

    private AdventureUtil() {
    }

    public static String legacy(String string) {
        return SECTION_SERIALIZER.serialize(SERIALIZER.deserialize(string));
    }

    public static Component stripItalics(Component component) {
        Style noItalic = component.style().decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);
        component = component.style(noItalic);
        List<Component> children = component.children().stream().map(AdventureUtil::stripItalics).toList();
        return component.children(children);
    }

    public static Component translate(String string) {
        Component base = SERIALIZER.deserializeOrNull(string);
        return stripItalics(base);
    }

    public static List<Component> translate(String... strings) {
        return Stream.of(strings).map(AdventureUtil::translate).toList();
    }

    public static List<Component> translate(List<String> strings) {
        return strings.stream().map(AdventureUtil::translate).toList();
    }

    public static TextComponent textComponentOf(String string) {
        Component base = SERIALIZER.deserializeOrNull(string);
        return (TextComponent) stripItalics(base);
    }

    public static List<TextComponent> textComponentsOf(String... strings) {
        return Stream.of(strings).map((s) -> (TextComponent) stripItalics(SERIALIZER.deserializeOrNull(s))).toList();
    }

    public static List<TextComponent> textComponentsOf(List<String> strings) {
        return strings.stream().map(AdventureUtil::textComponentOf).toList();
    }

    public static String componentToString(Component component) {
        return PLAIN_TEXT_COMPONENT_SERIALIZER.serialize(component);
    }

    public static Component miniMessage(String message, Map<String, String> placeholders) {
        String parsed = message;
        if (placeholders != null) {
            for (Map.Entry<String, String> e : placeholders.entrySet()) {
                parsed = parsed.replace("{" + e.getKey() + "}", e.getValue());
            }
        }
        if (parsed.contains("§")) {
            Component comp = SECTION_SERIALIZER.deserialize(parsed);
            return stripItalics(comp);
        } else if (parsed.contains("&")) {
            Component comp = translate(parsed);
            return stripItalics(comp);
        } else {
            Component comp = MINI.deserialize(parsed, buildPlaceholders(placeholders));
            return stripItalics(comp);
        }
    }

    public static List<Component> miniMessage(Map<String, String> placeholders, String... messages) {
        return Stream.of(messages).map((msg) -> miniMessage(msg, placeholders)).toList();
    }

    public static List<Component> miniMessage(List<String> messages, Map<String, String> placeholders) {
        return messages.stream().map((msg) -> miniMessage(msg, placeholders)).toList();
    }

    public static TagResolver buildPlaceholders(Map<String, String> placeholders) {
        if (placeholders != null && !placeholders.isEmpty()) {
            TagResolver.Builder builder = TagResolver.builder();
            placeholders.forEach((key, value) -> builder.resolver(Placeholder.parsed(key, value)));
            return builder.build();
        } else {
            return TagResolver.empty();
        }
    }

    public static String tpsWithFormat(double tps) {
        return (tps > 20.0F ? "*" : "") + Math.min(Math.round(tps * 100.0F) / 100.0F, 20.0F);
    }
}
