package org.example.forkjoin.recursiveaction;

import org.example.forkjoin.util.Utils;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;
import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;

/**
 * @author Fox
 *
 * 数组排序
 */
public class MergeSortMain {

    private final int[] arrayToSort;
    private final int threshold;

    public MergeSortMain(final int[] arrayToSort, final int threshold) {
        this.arrayToSort = arrayToSort;
        this.threshold = threshold;
    }

    public int[] sequentialSort() {
        return sequentialSort(arrayToSort, threshold);
    }

    public static int[] sequentialSort(final int[] arrayToSort, int threshold) {
        if (arrayToSort.length < threshold) {
            Arrays.sort(arrayToSort);
            return arrayToSort;
        }

        int midpoint = arrayToSort.length / 2;

        int[] leftArray = Arrays.copyOfRange(arrayToSort, 0, midpoint);
        int[] rightArray = Arrays.copyOfRange(arrayToSort, midpoint, arrayToSort.length);

        leftArray = sequentialSort(leftArray, threshold);
        rightArray = sequentialSort(rightArray, threshold);

        return merge(leftArray, rightArray);
    }

    public static int[] merge(final int[] leftArray, final int[] rightArray) {
        int[] mergedArray = new int[leftArray.length + rightArray.length];
        int mergedArrayPos = 0;
        int leftArrayPos = 0;
        int rightArrayPos = 0;
        while (leftArrayPos < leftArray.length && rightArrayPos < rightArray.length) {
            if (leftArray[leftArrayPos] <= rightArray[rightArrayPos]) {
                mergedArray[mergedArrayPos] = leftArray[leftArrayPos];
                leftArrayPos++;
            } else {
                mergedArray[mergedArrayPos] = rightArray[rightArrayPos];
                rightArrayPos++;
            }
            mergedArrayPos++;
        }

        while (leftArrayPos < leftArray.length) {
            mergedArray[mergedArrayPos] = leftArray[leftArrayPos];
            leftArrayPos++;
            mergedArrayPos++;
        }

        while (rightArrayPos < rightArray.length) {
            mergedArray[mergedArrayPos] = rightArray[rightArrayPos];
            rightArrayPos++;
            mergedArrayPos++;
        }

        return mergedArray;
    }

    /**
     * 注意：这里的排序后的值貌似有点问题，应该是排序的方法有点问题
     * 但这里的目的是为了展示如何使用ForkJoinPool 的 RecursiveAction、
     * 后续有需要要用的话自行修改这个排序方法吧
     */
    public static void main(String[] args) {
        // 构建数组
        int[] arrayToSort = Utils.buildRandomIntArray(200000);
        int[] expectedArray = Arrays.copyOf(arrayToSort, arrayToSort.length);

        int nofProcessors = Runtime.getRuntime().availableProcessors();

        MergeSortMain shortestPathServiceSeq = new MergeSortMain(arrayToSort, nofProcessors);
        int[] actualArray = shortestPathServiceSeq.sequentialSort();

        Arrays.sort(expectedArray);
        assertThat(actualArray, is(expectedArray));

        int[] arrayToSortSingleThread = Utils.buildRandomIntArray(200000);
        int[] arrayToSortMultiThread = Arrays.copyOf(arrayToSortSingleThread, arrayToSortSingleThread.length);

        nofProcessors = Runtime.getRuntime().availableProcessors();

        // SINGLE THREADED
        shortestPathServiceSeq = new MergeSortMain(arrayToSortSingleThread, nofProcessors);
        int[] sortSingleThreadArray = shortestPathServiceSeq.sequentialSort();

        // MULTI THREADED
        MergeSortAction mergeSortAction = new MergeSortAction(arrayToSortMultiThread, nofProcessors);

        ForkJoinPool forkJoinPool = new ForkJoinPool(nofProcessors);
        forkJoinPool.invoke(mergeSortAction);

        assertArrayEquals(sortSingleThreadArray, mergeSortAction.getSortedArray());
    }

}