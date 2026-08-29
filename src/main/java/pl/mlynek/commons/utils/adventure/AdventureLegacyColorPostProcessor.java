package pl.mlynek.commons.utils.adventure;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

/**
 * @Author: mlyn3kk_
 * @Website: https://discord.gg/swircode
 * @Date: 29.08.2026
 * @Project: mCore-commons
 * @Description: szkidbi eszkere gigachad
 */
public class AdventureLegacyColorPostProcessor implements UnaryOperator<Component> {
    private static final TextReplacementConfig LEGACY_REPLACEMENT_CONFIG = TextReplacementConfig.builder().match(Pattern.compile(".*")).replacement((matchResult, build) -> AdventureUtil.component(matchResult.group())).build();

    @Override
    public Component apply(Component component) {
        return component.replaceText(LEGACY_REPLACEMENT_CONFIG);
    }
}