package pl.mlynek.commons.utils.adventure;

import java.util.function.UnaryOperator;

/**
 * @Author: mlyn3kk_
 * @Website: https://discord.gg/swircode
 * @Date: 29.08.2026
 * @Project: mCore-commons
 * @Description: szkidbi eszkere gigachad
 */
public class AdventureLegacyColorPreProcessor implements UnaryOperator<String> {
    @Override
    public String apply(String component) {
        return component.replace("§", "&");
    }
}