package pl.mlynek.commons.utils;

import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONObject;
import pl.mlynek.commons.Main;
import java.awt.*;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

/**
 * @Author: mlyn3kk_
 * @Website: https://discord.gg/swircode
 * @Date: 20.03.2026
 * @Project: mCore
 * @Description: szkidbi eszkere gigachad
 */
@RequiredArgsConstructor
public class DiscordWebhookUtil {
    private final String webhookUrl;
    private final Plugin plugin;
    private String content;
    private String username;
    private String avatarUrl;
    private boolean tts;
    private final List<EmbedObject> embeds = new ArrayList<>();

    public DiscordWebhookUtil setContent(String content) {
        this.content = content;
        return this;
    }

    public DiscordWebhookUtil setUsername(String username) {
        this.username = username;
        return this;
    }

    public DiscordWebhookUtil setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    public DiscordWebhookUtil setTts(boolean tts) {
        this.tts = tts;
        return this;
    }

    public DiscordWebhookUtil addEmbed(EmbedObject embed) {
        this.embeds.add(embed);
        return this;
    }

    public void send() {
        if (this.content == null && this.embeds.isEmpty()) {
            throw new IllegalArgumentException("Set content or add at least one EmbedObject");
        } else {
            CompletableFuture.runAsync(() -> {
                try {
                    JSONObject json = new JSONObject();
                    json.put("content", this.content);
                    json.put("username", this.username);
                    json.put("avatar_url", this.avatarUrl);
                    json.put("tts", this.tts);
                    if (!this.embeds.isEmpty()) {
                        ArrayList<JSONObject> embedObjects = new ArrayList<>();
                        Iterator<EmbedObject> var3 = this.embeds.iterator();
                        while (true) {
                            if (!var3.hasNext()) {
                                json.put("embeds", embedObjects);
                                break;
                            }
                            EmbedObject embed =  var3.next();
                            embedObjects.add(embed.toJson());
                        }
                    }
                    URL url = new URL(this.webhookUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);
                    OutputStream os = connection.getOutputStream();
                    try {
                        os.write(json.toString().getBytes());
                        os.flush();
                    } catch (Throwable var8) {
                        if (os != null) {
                            try {
                                os.close();
                            } catch (Throwable var7) {
                                var8.addSuppressed(var7);
                            }
                        }
                        throw var8;
                    }
                    os.close();
                    int responseCode = connection.getResponseCode();
                    if (responseCode != 204) {
                        throw new RuntimeException("Failed to send message: HTTP error code : " + responseCode);
                    }
                } catch (Exception var9) {
                    this.plugin.getLogger().log(Level.SEVERE, "Error sending webhook", var9);
                }
            });
        }
    }

    public static class EmbedObject {
        private String title;
        private String description;
        private Color color;
        private OffsetDateTime timestamp;
        private String thumbnailUrl;
        private String imageUrl;
        private Footer footer;
        private Author author;
        private List<Field> fields = new ArrayList<>();

        public EmbedObject setTitle(String title) {
            this.title = title;
            return this;
        }

        public EmbedObject setDescription(String description) {
            this.description = description;
            return this;
        }

        public EmbedObject setColor(Color color) {
            this.color = color;
            return this;
        }

        public EmbedObject setTimestamp(OffsetDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public EmbedObject setThumbnail(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
            return this;
        }

        public EmbedObject setImage(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public EmbedObject setFooter(String text, String iconUrl) {
            this.footer = new Footer(text, iconUrl);
            return this;
        }

        public EmbedObject setAuthor(String name, String url, String iconUrl) {
            this.author = new Author(name, url, iconUrl);
            return this;
        }

        public EmbedObject addField(String name, String value, boolean inline) {
            this.fields.add(new Field(name, value, inline));
            return this;
        }

        private JSONObject toJson() {
            JSONObject json = new JSONObject();
            json.put("title", this.title);
            json.put("description", this.description);
            if (this.color != null) {
                json.put("color", this.color.getRGB() & 16777215);
            }

            if (this.timestamp != null) {
                json.put("timestamp", this.timestamp.toString());
            }

            JSONObject image;
            if (this.thumbnailUrl != null) {
                image = new JSONObject();
                image.put("url", this.thumbnailUrl);
                json.put("thumbnail", image);
            }

            if (this.imageUrl != null) {
                image = new JSONObject();
                image.put("url", this.imageUrl);
                json.put("image", image);
            }

            if (this.footer != null) {
                json.put("footer", this.footer.toJson());
            }

            if (this.author != null) {
                json.put("author", this.author.toJson());
            }

            if (!this.fields.isEmpty()) {
                ArrayList<JSONObject> fieldObjects = new ArrayList<>();
                for (Field field : this.fields) {
                    fieldObjects.add(field.toJson());
                }
                json.put("fields", fieldObjects);
            }

            return json;
        }
    }

    private static class Footer {
        private final String text;
        private final String iconUrl;

        private Footer(String text, String iconUrl) {
            this.text = text;
            this.iconUrl = iconUrl;
        }

        private JSONObject toJson() {
            JSONObject json = new JSONObject();
            json.put("text", this.text);
            json.put("icon_url", this.iconUrl);
            return json;
        }
    }

    private static class Author {
        private final String name;
        private final String url;
        private final String iconUrl;

        private Author(String name, String url, String iconUrl) {
            this.name = name;
            this.url = url;
            this.iconUrl = iconUrl;
        }

        private JSONObject toJson() {
            JSONObject json = new JSONObject();
            json.put("name", this.name);
            json.put("url", this.url);
            json.put("icon_url", this.iconUrl);
            return json;
        }
    }

    private static class Field {
        private final String name;
        private final String value;
        private final boolean inline;

        private Field(String name, String value, boolean inline) {
            this.name = name;
            this.value = value;
            this.inline = inline;
        }

        private JSONObject toJson() {
            JSONObject json = new JSONObject();
            json.put("name", this.name);
            json.put("value", this.value);
            json.put("inline", this.inline);
            return json;
        }
    }
}