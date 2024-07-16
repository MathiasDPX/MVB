package gg.gyro.mab.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Lists {
    public Set<String> privacy_scopes = Set.of("ping", "pm", "response", "pinvites");
    public Set<String> gamemodes = Set.of("creative", "survival", "adventure", "spectator");
    public HashSet<String> config_parameters = new HashSet<>(Arrays.asList("features.insomnia", "insomnia.chance", "features.joinleave", "joinleave.join", "joinleave.leave"));
    public HashSet<String> utility_blocks = new HashSet<>(Arrays.asList("anvil", "brewing_stand", "cartography_table", "crafting_table", "ender_chest", "fletching_table", "grindstone", "loom", "smithing_table", "stonecutter", "enchantment_table"));
}