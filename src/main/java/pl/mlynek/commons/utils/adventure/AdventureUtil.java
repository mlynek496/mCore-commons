package pl.mlynek.commons.utils.adventure;

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
public class AdventureUtil {
    private static final MiniMessage MINI_MESSAGE = MiniMessage.builder().postProcessor(new AdventureLegacyColorPostProcessor()).preProcessor(new AdventureLegacyColorPreProcessor()).build();
    private static final LegacyComponentSerializer AMPERSAND_SERIALIZER = LegacyComponentSerializer.builder().character('&').hexColors().useUnusualXRepeatedCharacterHexFormat().build();
    private static final LegacyComponentSerializer SECTION_SERIALIZER = LegacyComponentSerializer.builder().character('§').hexCharacter('#').hexColors().extractUrls().useUnusualXRepeatedCharacterHexFormat().build();
    private static final PlainTextComponentSerializer PLAIN_TEXT_COMPONENT_SERIALIZER = PlainTextComponentSerializer.plainText();

    private AdventureUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Component translate(String text) {
        return translate(text, null);
    }

    public static Component translate(String text, Map<String, String> placeholders) {
        if (text == null) {
            return Component.empty();
        }
        String parsed = text;
        if (placeholders != null && !placeholders.isEmpty()) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                parsed = parsed.replace("{" + entry.getKey() + "}", entry.getValue());
            }
        }
        return MINI_MESSAGE.deserialize(parsed, buildPlaceholders(placeholders)).decoration(TextDecoration.ITALIC, false);
    }

    public static List<Component> translate(List<String> lines) {
        return AdventureUtil.translate(lines, null);
    }

    public static List<Component> translate(List<String> lines, Map<String, String> placeholders) {
        if (lines == null) {
            return new ArrayList<>();
        }
        List<Component> components = new ArrayList<>();
        for (String line : lines) {
            components.add(translate(line, placeholders));
        }
        return components;
    }

    public static List<Component> translate(Map<String, String> placeholders, String... lines) {
        return Stream.of(lines).map(line -> translate(line, placeholders)).toList();
    }

    public static Component miniMessage(String message, Map<String, String> placeholders) {
        return translate(message, placeholders);
    }

    public static List<Component> miniMessage(List<String> messages, Map<String, String> placeholders) {
        return translate(messages, placeholders);
    }

    public static String legacy(String string) {
        return SECTION_SERIALIZER.serialize(AMPERSAND_SERIALIZER.deserialize(string));
    }

    public static Component component(String text) {
        return AMPERSAND_SERIALIZER.deserialize(text);
    }

    public static TextComponent textComponentOf(String string) {
        if (string == null) return Component.empty();
        return (TextComponent) translate(string);
    }

    public static List<TextComponent> textComponentsOf(String... strings) {
        return Stream.of(strings).map(AdventureUtil::textComponentOf).toList();
    }

    public static List<TextComponent> textComponentsOf(List<String> strings) {
        return strings.stream().map(AdventureUtil::textComponentOf).toList();
    }

    public static String componentToString(Component component) {
        return PLAIN_TEXT_COMPONENT_SERIALIZER.serialize(component);
    }

    public static TagResolver buildPlaceholders(Map<String, String> placeholders) {
        if (placeholders != null && !placeholders.isEmpty()) {
            TagResolver.Builder builder = TagResolver.builder();
            placeholders.forEach((key, value) -> builder.resolver(Placeholder.parsed(key, value)));
            return builder.build();
        }
        return TagResolver.empty();
    }

    public static MiniMessage miniMessage() {
        return MINI_MESSAGE;
    }

    public static String tpsWithFormat(double tps) {
        return (tps > 20.0F ? "*" : "") + Math.min(Math.round(tps * 100.0F) / 100.0F, 20.0F);
    }
}