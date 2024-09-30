package org.example.forkjoin.util;

import java.util.Random;

/**
 * ClassName:Utils
 * Package:org.example.forkjoin.util
 * Description:
 *
 * @Date:2024/9/26 14:32
 * @Author:qs@1.com
 */
public class Utils {
    public static int[] buildRandomIntArray(final int size) {
        int[] arrayToCalculateSumOf = new int[size];
        Random generator = new Random();
        for (int i = 0; i < arrayToCalculateSumOf.length; i++) {
            arrayToCalculateSumOf[i] = generator.nextInt(1000);
        }
        return arrayToCalculateSumOf;
    }

    public static int[] buildDefaultIntArray(final int size) {
        int[] arrayToCalculateSumOf = new int[size];
        Random generator = new Random();
        for (int i = 0; i < arrayToCalculateSumOf.length; i++) {
            arrayToCalculateSumOf[i] = 1;
        }
        return arrayToCalculateSumOf;
    }

}
