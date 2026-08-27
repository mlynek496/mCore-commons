package pl.mlynek.commons.utils;


/**
 * @Author: mlyn3kk_
 * @Website: https://discord.gg/swircode
 * @Date: 20.07.2026
 * @Project: mCore-boxpvp
 * @Description: szkidbi eszkere gigachad
 */
public class ProgressUtil {
    public static String bar(long intActually, long intRequired, int numberOfBlocks) {
        int i;
        intActually = Math.min(intActually, intRequired);
        double progressPercentage = (double) intActually / (double) intRequired;
        int filledBlocks = (int) Math.round(progressPercentage * (double) numberOfBlocks);
        StringBuilder progressBar = new StringBuilder();
        for (i = 0; i < filledBlocks; ++i) {
            progressBar.append("&a⏹");
        }
        for (i = filledBlocks; i < numberOfBlocks; ++i) {
            progressBar.append("&c⏹");
        }
        return AdventureUtil.legacy(progressBar.toString());
    }

    public static String bar(int intActually, int intRequired, int numberOfBlocks) {
        int i;
        intActually = Math.min(intActually, intRequired);
        double progressPercentage = (double) intActually / (double) intRequired;
        int filledBlocks = (int) Math.round(progressPercentage * (double) numberOfBlocks);
        StringBuilder progressBar = new StringBuilder();
        for (i = 0; i < filledBlocks; ++i) {
            progressBar.append("&a⏹");
        }
        for (i = filledBlocks; i < numberOfBlocks; ++i) {
            progressBar.append("&c⏹");
        }
        return AdventureUtil.legacy(progressBar.toString());
    }

    public static String percent(int intActually, int intRequired) {
        double progressPercentage = (double) Math.min(intActually, intRequired) / (double) intRequired * 100.0;
        int roundedPercentage = (int) Math.round(progressPercentage);
        if (roundedPercentage > 100) {
            roundedPercentage = 100;
        }
        return roundedPercentage + "%";
    }

    public static float calculateBarProgress(long remainingTime, long totalTime) {
        if (totalTime <= 0L) {
            return 0.0f;
        }
        float progress = (float) remainingTime / (float) totalTime;
        return Math.max(0.0f, Math.min(1.0f, progress));
    }
}