package com.cxel.launcher.util;

import java.util.Random;

/**
 * zhulf 20190924
 * andevele@163.com
 * 随机颜色值
 */
public class ColorUtil {
    static String[] colors = {
            "#141414", //黑
            "#ffffff", //白
            "#D2691E",
            "#555555",
            "#8B0000",
            "#008B8B",
            "#6495ED",
            "#3CB371",
            "#4169E1",
            "#8B4513",
            "#228B22",
            "#808000"
    };

    public static String getRandomColor() {
        Random random = new Random();
        int i = random.nextInt(colors.length);
        return colors[i];
    }

    public static String getColors(int index) {
        return colors[index];
    }
}
