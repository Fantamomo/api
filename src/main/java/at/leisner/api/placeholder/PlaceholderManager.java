package at.leisner.api.placeholder;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.entity.Player;
import org.bukkit.World;

public class PlaceholderManager {
    private Map<String, Placeholder> placeholders;

    public PlaceholderManager() {
        this.placeholders = new HashMap<>();
    }

    public void addPlaceholder(Placeholder placeholder) {
        this.placeholders.put(placeholder.key, placeholder);
    }

    public void removePlaceholder(String key) {
        this.placeholders.remove(key);
    }

    public String replacePlaceholders(String text, Player player, World world) {
        // Verarbeitung für bedingte Platzhalter
        text = processConditionalPlaceholders(text, player, world);

        // Verarbeitung für normale Platzhalter
        for (Map.Entry<String, Placeholder> entry : this.placeholders.entrySet()) {
            text = text.replace("%" + entry.getKey() + "%", entry.getValue().getValue(player, world));
        }

        return text;
    }

    private String processConditionalPlaceholders(String text, Player player, World world) {
        Pattern pattern = Pattern.compile("%(\\w+)\\?(.*?):(.*?)%");
        Matcher matcher = pattern.matcher(text);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String key = matcher.group(1);
            String trueValue = matcher.group(2);
            String falseValue = matcher.group(3);

            Placeholder placeholder = this.placeholders.get(key);
            String replacement;
            if (placeholder instanceof BooleanPlaceholder) {
                boolean condition = ((BooleanPlaceholder) placeholder).getBooleanValue(player, world);
                replacement = condition ? trueValue : falseValue;
            } else {
                replacement = matcher.group(0); // unverändert zurückgeben, wenn der Platzhalter nicht gefunden wird
            }

            matcher.appendReplacement(sb, replacement);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
