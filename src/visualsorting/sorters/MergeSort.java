package visualsorting.sorters;

import java.util.List;
import java.util.ArrayList;
import visualsorting.SteppableSorter;

/**
 * Credit https://en.wikipedia.org/wiki/Merge_sort#Top-down_implementation
 * @author Calvin
 */
public class MergeSort 
    extends SteppableSorter {

    private int[] copyArr;
    private List<Pair> intervals;
    private int state;
    private Pair currentInterval;
    private int currentMiddle;
    private int i;
    private int j;
    private int k;
    
    @Override
    public void setArray(int[] arr) {
        super.setArray(arr);
        
        this.state = 0;
        this.copyArr = new int[arr.length];
        for (int i = 0; i < arr.length; i++)
            this.copyArr[i] = arr[i];
        
        //make all split calls
        intervals = new ArrayList<>();
        addIntervals(intervals, 0, arr.length);
        //for (Pair p : intervals)
        //    System.out.println(p);
        
        this.state = 0;
    }
    
    private void addIntervals(List<Pair> collect, int left, int right) {
        if (right - left < 2)
            return;
        
        int middle = (left + right) / 2;
        addIntervals(collect, left, middle);
        addIntervals(collect, middle, right);
        
        collect.add(new Pair(left, right));
    }
    
    @Override
    protected void step() {
        switch (state) {
            case 0:
                if (!this.intervals.isEmpty() || this.intervals == null) {
                    this.currentInterval = this.intervals.remove(0);
                    this.currentMiddle = (currentInterval.left + currentInterval.right) / 2; 
                    this.i = currentInterval.left;
                    this.j = currentMiddle;
                    this.k = currentInterval.left;
                    this.state = 1;
                    //copy array into copy so we can merge successfully
                    this.copyArr = new int[array.length];
                    for (int i = 0; i < array.length; i++) {
                        this.numArrayAccesses++;
                        this.copyArr[i] = array[i];
                    }
                }
                else {
                    return; //should be done with algorithm
                }
                //return; fall thru
            case 1:
                if (k < currentInterval.right) {
                    if (i < currentMiddle && (j >= currentInterval.right || lteCheck(arrayAccessCheck(copyArr, i), arrayAccessCheck(copyArr, j))) ) {
                        array[k] = copyArr[i];
                        this.numArrayAccesses++;
                        this.removeAllColoredIndiciesOf(this.SWAP_COLOR_1);
                        this.addColoredIndex(k, this.SWAP_COLOR_1);
                        this.addColoredIndex(i, this.SWAP_COLOR_1);
                        //this.lastSwappedIndicies = new int[]{k,i};
                        i++;
                    }
                    else {
                        array[k] = copyArr[j];
                        this.numArrayAccesses++;
                        this.removeAllColoredIndiciesOf(this.SWAP_COLOR_1);
                        this.addColoredIndex(k, this.SWAP_COLOR_1);
                        this.addColoredIndex(j, this.SWAP_COLOR_1);
                        //this.lastSwappedIndicies = new int[]{k,j};
                        j++;
                    }
                    this.removeAllColoredIndiciesOf(this.SELECTED_COLOR);
                    this.addColoredIndex(k, this.SELECTED_COLOR, true);
                    //this.selectedIndicies = new int[]{k};
                    k++;
                }
                else {
                    state = 0;
                }
                return;
        }   
    }
    
    private boolean lteCheck(int a, int b) {
        this.numComparisons++;
        return a <= b;
    }
    
    private int arrayAccessCheck(int[] array, int index) {
        this.numArrayAccesses++;
        return array[index];
    }

    @Override
    protected String getSorterName() {
        return "MergeSort";
    }
    
    private class Pair {
        int left;
        int right;
        
        public Pair(int left, int right) {
            this.left = left;
            this.right = right;
        }
        
        @Override
        public String toString() {
            return "(" + left + ", " + right + ")";
        }
    }
    
    private static void printArr(int[] a) {
        for (int n : a) {
            System.out.print(n + " ");
        }
        System.out.println();
    }
    
    public static void main(String[] args) {
        int[] arr = new int[]{8,7,6,5,4,3,2,1};
        MergeSort ms = new MergeSort();
        ms.setArray(arr);
        
        while (!ms.isFinished()) {
            printArr(ms.array);
            printArr(ms.copyArr);
            ms.step();
            
            System.out.println();
        }
        printArr(ms.array);
            printArr(ms.copyArr);
    }

}