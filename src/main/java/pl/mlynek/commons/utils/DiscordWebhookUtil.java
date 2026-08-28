package pl.mlynek.commons.utils;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import java.awt.Color;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import javax.net.ssl.HttpsURLConnection;

/**
 * @Author: mlyn3kk_
 * @Website: https://discord.gg/swircode
 * @Date: 20.03.2026
 * @Project: mCore
 * @Description: szkidbi eszkere gigachad
 */
@RequiredArgsConstructor
@Getter
@Setter
public class DiscordWebhookUtil {
    private final String url;
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

    public void addEmbed(EmbedObject embedObject) {
        this.embeds.add(embedObject);
    }

    public void execute() {
        if (this.content == null && this.embeds.isEmpty()) {
            throw new IllegalArgumentException("Set content or add at least one EmbedObject");
        }
        CompletableFuture.runAsync(() -> {
            try {
                JSONObject json = new JSONObject();
                json.put("content", this.content);
                json.put("username", this.username);
                json.put("avatar_url", this.avatarUrl);
                json.put("tts", this.tts);

                if (!this.embeds.isEmpty()) {
                    List<JSONObject> embedObjects = new ArrayList<>();
                    for (EmbedObject embed : this.embeds) {
                        JSONObject embedJson = new JSONObject();
                        embedJson.put("title", embed.getTitle());
                        embedJson.put("description", embed.getDescription());
                        embedJson.put("url", embed.getUrl());

                        if (embed.getTimestamp() != null) {
                            embedJson.put("timestamp", embed.getTimestamp());
                        }

                        if (embed.getColor() != null) {
                            Color color = embed.getColor();
                            int rgb = (color.getRed() << 16) + (color.getGreen() << 8) + color.getBlue();
                            embedJson.put("color", rgb);
                        }

                        EmbedObject.Footer footer = embed.getFooter();
                        EmbedObject.Image image = embed.getImage();
                        EmbedObject.Thumbnail thumbnail = embed.getThumbnail();
                        EmbedObject.Author author = embed.getAuthor();
                        List<EmbedObject.Field> fields = embed.getFields();

                        if (footer != null) {
                            JSONObject footerJson = new JSONObject();
                            footerJson.put("text", footer.getText());
                            footerJson.put("icon_url", footer.getIconUrl());
                            embedJson.put("footer", footerJson);
                        }

                        if (image != null) {
                            JSONObject imageJson = new JSONObject();
                            imageJson.put("url", image.getUrl());
                            embedJson.put("image", imageJson);
                        }

                        if (thumbnail != null) {
                            JSONObject thumbnailJson = new JSONObject();
                            thumbnailJson.put("url", thumbnail.getUrl());
                            embedJson.put("thumbnail", thumbnailJson);
                        }

                        if (author != null) {
                            JSONObject authorJson = new JSONObject();
                            authorJson.put("name", author.getName());
                            authorJson.put("url", author.getUrl());
                            authorJson.put("icon_url", author.getIconUrl());
                            embedJson.put("author", authorJson);
                        }

                        if (fields != null && !fields.isEmpty()) {
                            List<JSONObject> fieldObjects = getJsonObjects(fields);
                            embedJson.put("fields", fieldObjects.toArray());
                        }

                        embedObjects.add(embedJson);
                    }
                    json.put("embeds", embedObjects.toArray());
                }

                URL siteUrl = new URL(this.url);
                HttpsURLConnection connection = (HttpsURLConnection) siteUrl.openConnection();
                connection.addRequestProperty("Content-Type", "application/json");
                connection.addRequestProperty("User-Agent", "Java-DiscordWebhook");
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                try (OutputStream outputStream = connection.getOutputStream()) {
                    outputStream.write(json.toString().getBytes("UTF-8"));
                    outputStream.flush();
                }
                connection.getInputStream().close();
                connection.disconnect();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    private static @NotNull List<JSONObject> getJsonObjects(List<EmbedObject.Field> fields) {
        List<JSONObject> fieldObjects = new ArrayList<>();
        for (EmbedObject.Field field : fields) {
            JSONObject fieldJson = new JSONObject();
            fieldJson.put("name", field.getName());
            fieldJson.put("value", field.getValue());
            fieldJson.put("inline", field.isInline());
            fieldObjects.add(fieldJson);
        }
        return fieldObjects;
    }

    private static class JSONObject {
        private final HashMap<String, Object> map = new HashMap<>();

        void put(String string, Object object) {
            if (object != null) {
                this.map.put(string, object);
            }
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            Set<Map.Entry<String, Object>> set = this.map.entrySet();
            stringBuilder.append("{");
            int n = 0;
            for (Map.Entry<String, Object> entry : set) {
                Object object = entry.getValue();
                stringBuilder.append(this.quote(entry.getKey())).append(":");
                if (object instanceof String) {
                    stringBuilder.append(this.quote(String.valueOf(object)));
                } else if (object instanceof Integer || object instanceof Boolean) {
                    stringBuilder.append(object);
                } else if (object instanceof JSONObject) {
                    stringBuilder.append(object);
                } else if (object.getClass().isArray()) {
                    stringBuilder.append("[");
                    int n2 = Array.getLength(object);
                    for (int i = 0; i < n2; ++i) {
                        stringBuilder.append(Array.get(object, i).toString()).append(i != n2 - 1 ? "," : "");
                    }
                    stringBuilder.append("]");
                }
                stringBuilder.append(++n == set.size() ? "}" : ",");
            }
            return stringBuilder.toString();
        }

        private String quote(String string) {
            return "\"" + string.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r") + "\"";
        }
    }

    public static class EmbedObject {
        private String title;
        private String description;
        private String url;
        private String timestamp;
        private Color color;
        private Footer footer;
        private Thumbnail thumbnail;
        private Image image;
        private Author author;
        private List<Field> fields = new ArrayList<>();

        public String getTitle() {
            return this.title;
        }

        public String getDescription() {
            return this.description;
        }

        public String getUrl() {
            return this.url;
        }

        public Color getColor() {
            return this.color;
        }

        public Footer getFooter() {
            return this.footer;
        }

        public Thumbnail getThumbnail() {
            return this.thumbnail;
        }

        public Image getImage() {
            return this.image;
        }

        public Author getAuthor() {
            return this.author;
        }

        public List<Field> getFields() {
            return this.fields;
        }

        public String getTimestamp() {
            return this.timestamp;
        }

        public EmbedObject setTitle(String string) {
            this.title = string;
            return this;
        }

        public EmbedObject setDescription(String string) {
            this.description = string;
            return this;
        }

        public EmbedObject setUrl(String string) {
            this.url = string;
            return this;
        }

        public EmbedObject setColor(Color color) {
            this.color = color;
            return this;
        }

        public EmbedObject setFooter(String string, String string2) {
            this.footer = new Footer(string, string2);
            return this;
        }

        public EmbedObject setThumbnail(String string) {
            this.thumbnail = new Thumbnail(string);
            return this;
        }

        public EmbedObject setImage(String string) {
            this.image = new Image(string);
            return this;
        }

        public EmbedObject setAuthor(String string, String string2, String string3) {
            this.author = new Author(string, string2, string3);
            return this;
        }

        public EmbedObject addField(String string, String string2, boolean bl) {
            this.fields.add(new Field(string, string2, bl));
            return this;
        }

        public EmbedObject setTimestamp(String string) {
            this.timestamp = string;
            return this;
        }

        public static class Footer {
            private final String text;
            private final String iconUrl;

            private Footer(String string, String string2) {
                this.text = string;
                this.iconUrl = string2;
            }

            public String getText() {
                return this.text;
            }

            public String getIconUrl() {
                return this.iconUrl;
            }
        }

        public static class Thumbnail {
            private final String url;

            private Thumbnail(String string) {
                this.url = string;
            }

            public String getUrl() {
                return this.url;
            }
        }

        public static class Image {
            private final String url;

            private Image(String string) {
                this.url = string;
            }

            public String getUrl() {
                return this.url;
            }
        }

        public static class Author {
            private final String name;
            private final String url;
            private final String iconUrl;

            private Author(String string, String string2, String string3) {
                this.name = string;
                this.url = string2;
                this.iconUrl = string3;
            }

            public String getName() {
                return this.name;
            }

            public String getUrl() {
                return this.url;
            }

            public String getIconUrl() {
                return this.iconUrl;
            }
        }

        public static class Field {
            private final String name;
            private final String value;
            private final boolean inline;

            private Field(String string, String string2, boolean bl) {
                this.name = string;
                this.value = string2;
                this.inline = bl;
            }

            public String getName() {
                return this.name;
            }

            public String getValue() {
                return this.value;
            }

            public boolean isInline() {
                return this.inline;
            }
        }
    }
}