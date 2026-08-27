package pl.mlynek.commons.utils.serializer;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import pl.mlynek.commons.Main;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Author: mlyn3kk_
 * @Website: https://discord.gg/swircode
 * @Date: 15.08.2026
 * @Project: mCore-anarchia-recode
 * @Description: szkidbi eszkere gigachad
 */
public final class EffectSerializer {

    private EffectSerializer() {
    }

    public static String serializePotionEffects(final Collection<PotionEffect> effects) {
        if (effects == null || effects.isEmpty()) {
            return "";
        }
        final StringBuilder serialized = new StringBuilder();
        for (final PotionEffect effect : effects) {
            serialized.append(effect.getType().getKey().getKey()).append(";").append(effect.getDuration()).append(";").append(effect.getAmplifier()).append("|");
        }
        return serialized.toString();
    }

    public static List<PotionEffect> deserializePotionEffects(final String serialized) {
        if (serialized == null || serialized.isEmpty()) {
            return List.of();
        }

        List<PotionEffect> effects = new ArrayList<>();
        for (String part : serialized.split("\\|")) {
            String[] data = part.split(";");
            if (data.length < 3) {
                continue;
            }
            PotionEffectType type = Registry.EFFECT.get(NamespacedKey.minecraft(data[0].toLowerCase()));
            if (type == null) {
                continue;
            }
            try {
                int duration = Integer.parseInt(data[1]);
                int amplifier = Integer.parseInt(data[2]);
                if (duration > 0) {
                    effects.add(new PotionEffect(type, duration, amplifier));
                }
            } catch (NumberFormatException ignored) {
                Main.getInstance().getLogger().warning("Failed to parse potion effect duration/amplifier: " + part);
            }
        }
        return effects;
    }
}