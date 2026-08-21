package com.example.fairnessengine;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class ChoreIconUtil {
    private static final Map<String, Integer> iconMap = new TreeMap<>();

    static {
        iconMap.put("ic_sweep", R.drawable.ic_sweep);
        iconMap.put("ic_vacuum", R.drawable.ic_vacuum);
        iconMap.put("ic_bathroom", R.drawable.ic_bathroom);
        iconMap.put("ic_cooking", R.drawable.ic_cooking);
        iconMap.put("ic_dishes", R.drawable.ic_dishes);
        iconMap.put("ic_clean", R.drawable.ic_clean);
        iconMap.put("ic_laundry", R.drawable.ic_laundry);
        iconMap.put("ic_grocery", R.drawable.ic_grocery);
        iconMap.put("ic_pet", R.drawable.ic_pet);
        iconMap.put("ic_garden", R.drawable.ic_garden);
        iconMap.put("ic_trash", R.drawable.ic_trash);
        iconMap.put("ic_other", R.drawable.ic_other);
    }

    public static int getIconResId(String iconName) {
        if (iconName != null && iconMap.containsKey(iconName)) {
            return Objects.requireNonNullElse(iconMap.get(iconName), R.drawable.ic_other);
        }
        return R.drawable.ic_other;
    }

    public static int guessIconResId(String choreName) {
        if (choreName == null) return R.drawable.ic_other;
        String lower = choreName.toLowerCase();
        if (lower.contains("sweep") || lower.contains("mop") || lower.contains("floor")) return R.drawable.ic_sweep;
        if (lower.contains("vacuum")) return R.drawable.ic_vacuum;
        if (lower.contains("bath") || lower.contains("toilet")) return R.drawable.ic_bathroom;
        if (lower.contains("cook") || lower.contains("food") || lower.contains("meal")) return R.drawable.ic_cooking;
        if (lower.contains("dish") || lower.contains("plate") || lower.contains("wash")) return R.drawable.ic_dishes;
        if (lower.contains("laundry") || lower.contains("cloth")) return R.drawable.ic_laundry;
        if (lower.contains("shop") || lower.contains("grocer")) return R.drawable.ic_grocery;
        if (lower.contains("pet") || lower.contains("dog") || lower.contains("cat")) return R.drawable.ic_pet;
        if (lower.contains("garden") || lower.contains("plant") || lower.contains("yard")) return R.drawable.ic_garden;
        if (lower.contains("trash") || lower.contains("bin")) return R.drawable.ic_trash;
        if (lower.contains("clean")) return R.drawable.ic_clean;
        return R.drawable.ic_other;
    }

    public static String getDefaultName(String iconName) {
        if (iconName == null) return "Chore";
        switch (iconName) {
            case "ic_sweep": return "Sweep Floors";
            case "ic_vacuum": return "Vacuuming";
            case "ic_bathroom": return "Clean Bathroom";
            case "ic_cooking": return "Cooking";
            case "ic_dishes": return "Do Dishes";
            case "ic_clean": return "Deep Clean";
            case "ic_laundry": return "Laundry";
            case "ic_grocery": return "Grocery Shopping";
            case "ic_pet": return "Pet Care";
            case "ic_garden": return "Gardening";
            case "ic_trash": return "Take out Trash";
            default: return "New Chore";
        }
    }

    public static int getIconColor(String iconName) {
        if (iconName == null) return 0xFFA8949E; // Muted
        switch (iconName) {
            case "ic_laundry": return 0xFF56C2E0; // Sky
            case "ic_pet": return 0xFFFF6B5B; // Coral
            case "ic_cooking": return 0xFFFFB648; // Orange
            case "ic_garden": return 0xFF6FCF97; // Green
            case "ic_trash": return 0xFFC77DFF; // Purple
            case "ic_clean": return 0xFFE9D5E2; // Pinkish
            default: return 0xFFFFFFFF; // White
        }
    }

    public static int getEffortColor(double weight) {
        if (weight <= 1.5) return 0xFF6FCF97; // Green (Low)
        if (weight <= 3.5) return 0xFFFFB648; // Orange (Med)
        return 0xFFFF6B5B; // Coral (High)
    }

    public static String[] getAllIconNames() {
        return iconMap.keySet().toArray(new String[0]);
    }
}
