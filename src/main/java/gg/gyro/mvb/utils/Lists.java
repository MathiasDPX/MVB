package gg.gyro.mvb.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Lists {
    public Set<String> privacy_scopes = Set.of("ping", "pm", "response", "pinvites", "party_coords", "tpa_requests");
    public Set<String> gamemodes = Set.of("creative", "survival", "adventure", "spectator");
    public HashSet<String> config_parameters = new HashSet<>(Arrays.asList("features.insomnia", "insomnia.chance", "features.joinleave", "joinleave.join", "joinleave.leave", "features.food_poisoning", "food_poisoning.chance", "food_poisoning.duration", "food_poisoning.amplifier", "features.durability_ping"));
    public HashSet<String> utility_blocks = new HashSet<>(Arrays.asList("anvil", "brewing_stand", "cartography_table", "crafting_table", "ender_chest", "fletching_table", "grindstone", "loom", "smithing_table", "stonecutter", "enchantment_table"));

    public static boolean isRaw(String namespace){
        return switch (namespace) {
            case "porkchop" -> true;
            case "mutton" -> true;
            case "chicken" -> true;
            case "beef" -> true;
            case "cod" -> true;
            case "salmon" -> true;
            case "tropical_fish" -> true;
            default -> false;
        };
    }
}