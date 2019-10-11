package com.cxel.launcher.util;

import java.util.Random;

/**
 * zhulf 20190924
 * andevele@163.com
 * 随机颜色值
 */
public class ColorUtil {

    public static String getRandomColor() {
        String[] colors = {"#555555", "#ffcc66", "#238888", "#00ccff", "#996699",
                "#9999cc", "#666699", "#ff9900", "#339933", "#0066cc"};
        Random random = new Random();
        int i = random.nextInt(colors.length);
        return colors[i];
    }
}
