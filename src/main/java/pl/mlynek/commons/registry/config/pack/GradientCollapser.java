package pl.mlynek.commons.registry.config.pack;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: mlyn3kk_
 * @Website: https://discord.gg/swircode
 * @Date: 29.08.2026
 * @Project: mCore-commons
 * @Description: szkidbi eszkere gigachad
 */
public final class GradientCollapser {

    private static final Pattern OPEN_TAG = Pattern.compile("<#([0-9a-fA-F]{6})>");
    private static final Pattern ANY_TAG = Pattern.compile("<[^>]+>");
    private static final int TOLERANCE = 2;
    private static final int MAX_STOPS = 5;

    private GradientCollapser() {
    }

    public static String collapse(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        StringBuilder result = new StringBuilder();
        int n = input.length();
        int i = 0;
        List<int[]> runColors = new ArrayList<>();
        List<String> runChars = new ArrayList<>();

        while (i < n) {
            Matcher m = OPEN_TAG.matcher(input);
            m.region(i, n);

            if (!m.lookingAt()) {
                flushRun(result, runColors, runChars);
                int nextOpen = findNextOpenTagStart(input, i);
                if (nextOpen == -1) {
                    result.append(input, i, n);
                    i = n;
                } else {
                    result.append(input, i, nextOpen);
                    i = nextOpen;
                }
                continue;
            }

            String hex = m.group(1);
            int contentStart = m.end();
            String closeTag = "</#" + hex + ">";

            Boundary boundary = scanContent(input, contentStart, closeTag);
            runColors.add(hexToRgb(hex));
            runChars.add(boundary.content());
            i = boundary.nextIndex();
        }

        flushRun(result, runColors, runChars);
        return result.toString();
    }

    private static int findNextOpenTagStart(String input, int from) {
        Matcher m = OPEN_TAG.matcher(input);
        return m.find(from) ? m.start() : -1;
    }

    private record Boundary(String content, int nextIndex) {
    }

    private static Boundary scanContent(String input, int contentStart, String closeTag) {
        int n = input.length();
        int i = contentStart;
        StringBuilder content = new StringBuilder();
        while (i < n) {
            if (input.startsWith(closeTag, i)) {
                return new Boundary(content.toString(), i + closeTag.length());
            }
            char c = input.charAt(i);
            if (c == '\\' && i + 1 < n) {
                content.append(c).append(input.charAt(i + 1));
                i += 2;
                continue;
            }
            if (c == '<') {
                Matcher m = ANY_TAG.matcher(input);
                m.region(i, n);
                if (m.lookingAt()) {
                    return new Boundary(content.toString(), i);
                }
            }
            content.append(c);
            i++;
        }
        return new Boundary(content.toString(), n);
    }

    private static void flushRun(StringBuilder result, List<int[]> colors, List<String> chars) {
        if (colors.isEmpty()) {
            return;
        }
        String gradientTag = tryBuildGradientTag(colors, chars);
        if (gradientTag != null) {
            result.append(gradientTag);
        } else {
            for (int i = 0; i < colors.size(); i++) {
                String hex = rgbToHex(colors.get(i));
                result.append("<#").append(hex).append('>').append(chars.get(i));
            }
        }
        colors.clear();
        chars.clear();
    }

    private static String tryBuildGradientTag(List<int[]> colors, List<String> chars) {
        if (colors.size() < 2) {
            return null;
        }

        int runCount = colors.size();
        int[] visLen = new int[runCount];
        int[] start = new int[runCount];
        int total = 0;
        for (int r = 0; r < runCount; r++) {
            visLen[r] = visualLength(chars.get(r));
            start[r] = total;
            total += visLen[r];
        }
        if (total == 0) {
            return null;
        }

        List<int[]> stopColors = detectStops(colors, start, visLen, total);
        if (stopColors == null) {
            return null;
        }

        StringBuilder tag = new StringBuilder("<gradient");
        for (int[] c : stopColors) {
            tag.append(':').append('#').append(rgbToHex(c));
        }
        tag.append('>');
        for (String c : chars) {
            tag.append(c);
        }
        tag.append("</gradient>");
        return tag.toString();
    }

    private static int visualLength(String s) {
        String clean = s.replaceAll("<[^>]+>", "");
        int len = 0;
        int i = 0;
        int n = clean.length();
        while (i < n) {
            if (clean.charAt(i) == '\\' && i + 1 < n) {
                i += 2;
            } else {
                i += 1;
            }
            len++;
        }
        return len;
    }

    private static List<int[]> detectStops(List<int[]> colors, int[] start, int[] visLen, int total) {
        int n = colors.size();
        int cap = Math.min(MAX_STOPS, n);
        for (int k = 2; k <= cap; k++) {
            List<int[]> candidateStops = new ArrayList<>(k);
            for (int m = 0; m < k; m++) {
                int pos = total == 1 ? 0 : Math.round((m * (float) (total - 1)) / (k - 1));
                int run = findRunForPosition(start, visLen, pos);
                candidateStops.add(colors.get(run));
            }
            if (matchesGradient(colors, start, visLen, total, candidateStops)) {
                return candidateStops;
            }
        }
        return null;
    }

    private static int findRunForPosition(int[] start, int[] visLen, int pos) {
        for (int r = 0; r < start.length; r++) {
            if (visLen[r] == 0) {
                continue;
            }
            int end = start[r] + visLen[r] - 1;
            if (pos >= start[r] && pos <= end) {
                return r;
            }
        }
        return start.length - 1;
    }

    private static boolean matchesGradient(List<int[]> colors, int[] start, int[] visLen, int total, List<int[]> stopColors) {
        int k = stopColors.size();
        for (int r = 0; r < colors.size(); r++) {
            if (visLen[r] == 0) {
                continue;
            }
            int[] actual = colors.get(r);
            int rangeStart = start[r];
            int rangeEnd = start[r] + visLen[r] - 1;
            for (int pos = rangeStart; pos <= rangeEnd; pos++) {
                float t = total == 1 ? 0 : pos / (float) (total - 1);
                float segFloat = t * (k - 1);
                int seg = Math.min(k - 2, (int) Math.floor(segFloat));
                float localT = segFloat - seg;
                int[] c0 = stopColors.get(seg);
                int[] c1 = stopColors.get(seg + 1);
                for (int ch = 0; ch < 3; ch++) {
                    int expected = Math.round(c0[ch] + (c1[ch] - c0[ch]) * localT);
                    if (Math.abs(expected - actual[ch]) > TOLERANCE) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static int[] hexToRgb(String hex) {
        return new int[]{Integer.parseInt(hex.substring(0, 2), 16), Integer.parseInt(hex.substring(2, 4), 16), Integer.parseInt(hex.substring(4, 6), 16)};
    }

    private static String rgbToHex(int[] rgb) {
        return String.format("%02X%02X%02X", rgb[0], rgb[1], rgb[2]);
    }
}