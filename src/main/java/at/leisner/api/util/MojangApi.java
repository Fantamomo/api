package at.leisner.api.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MojangApi {
    private static final String MOJANG_API_URL = "https://api.mojang.com/users/profiles/minecraft/%s";
    private static final String MOJANG_SESSION_API_URL = "https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false";
    private static final String MINESKIN_API_URL = "https://api.mineskin.org/get/id/%s";

    public static String getUUID(String playerName) {
        try {
            URL url = new URL(String.format(MOJANG_API_URL, playerName));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            JsonObject response = JsonParser.parseReader(reader).getAsJsonObject();
            reader.close();

            return response.get("id").getAsString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static SkinData getSkinData(String uuid) {
        try {
            URL url = new URL(String.format(MOJANG_SESSION_API_URL, uuid));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            JsonObject response = JsonParser.parseReader(reader).getAsJsonObject();
            reader.close();

            JsonArray properties = response.getAsJsonArray("properties");
            for (int i = 0; i < properties.size(); i++) {
                JsonObject property = properties.get(i).getAsJsonObject();
                if (property.get("name").getAsString().equals("textures")) {
                    String value = property.get("value").getAsString();
                    String signature = property.get("signature").getAsString();
                    return new SkinData(value, signature);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static CapeData getCapeData(String capeId) {
        try {
            URL url = new URL(String.format(MINESKIN_API_URL, capeId));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            JsonObject response = JsonParser.parseReader(reader).getAsJsonObject();
            reader.close();

            JsonObject data = response.getAsJsonObject("data");
            JsonObject texture = data.getAsJsonObject("texture");

            String value = texture.get("value").getAsString();
            String signature = texture.get("signature").getAsString();

            return new CapeData(value, signature);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public record CapeData(String value, String signature) {}
    public record SkinData(String value, String signature) {}
}
