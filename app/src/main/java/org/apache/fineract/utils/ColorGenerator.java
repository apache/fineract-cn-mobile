package org.apache.fineract.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ColorGenerator {

    public static final ColorGenerator DEFAULT;

    public static final ColorGenerator MATERIAL;

    static {
        DEFAULT = create(Arrays.asList(
                0xfff16364,
                0xfff58559,
                0xfff9a43e,
                0xffe4c62e,
                0xff67bf74,
                0xff59a2be,
                0xff2093cd,
                0xffad62a7,
                0xff805781
        ));
        MATERIAL = create(Arrays.asList(
                0xffe57373,
                0xfff06292,
                0xffba68c8,
                0xff9575cd,
                0xff7986cb,
                0xff64b5f6,
                0xff4fc3f7,
                0xff4dd0e1,
                0xff4db6ac,
                0xff81c784,
                0xffaed581,
                0xffff8a65,
                0xffd4e157,
                0xffffd54f,
                0xffffb74d,
                0xffa1887f,
                0xff90a4ae
        ));
    }

    private final List<Integer> colors;
    private final Random random;

    public static ColorGenerator create(List<Integer> colorList) {
        return new ColorGenerator(colorList);
    }

    private ColorGenerator(List<Integer> colorList) {
        colors = colorList;
        random = new Random(System.currentTimeMillis());
    }

    public int getRandomColor() {
        return colors.get(random.nextInt(colors.size()));
    }

    public int getColor(Object key) {
        return colors.get(Math.abs(key.hashCode()) % colors.size());
    }
}