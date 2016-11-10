package com.example.a2048;

import java.util.Random;

/**
 * Created by Hades on 16/10/31.
 */
public class Utils {
    private static Random random = new Random();
    public  static int getRandomInt(int area) {
        return (random.nextInt(area));
    }
}
