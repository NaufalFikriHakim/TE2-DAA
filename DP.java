import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

class DP {
    boolean[] bestAssignment;
    int bestErr;

    static boolean isSubsetSum(int arr[], int n, int sum)
    {
        // Base Cases
        if (sum == 0)
            return true;
        if (n == 0 && sum != 0)
            return false;
 
        // If last element is greater than sum, then ignore
        // it
        if (arr[n - 1] > sum)
            return isSubsetSum(arr, n - 1, sum);
 
        /* else, check if sum can be obtained by any of
           the following
        (a) including the last element
        (b) excluding the last element
        */
        return isSubsetSum(arr, n - 1, sum)
            || isSubsetSum(arr, n - 1, sum - arr[n - 1]);
    }
 
    // Returns true if arr[] can be partitioned in two
    // subsets of equal sum, otherwise false
    static boolean findPartition(int arr[], int n)
    {
        // Calculate sum of the elements in array
        int sum = 0;
        for (int i = 0; i < n; i++)
            sum += arr[i];
 
        // If sum is odd, there cannot be two subsets
        // with equal sum
        if (sum % 2 != 0)
            return false;
 
        // Find if there is subset with sum equal to half
        // of total sum
        return isSubsetSum(arr, n, sum / 2);
    }

    private static class PartitionData {
        boolean[] bestAssignment;
        int bestErr;

        PartitionData(boolean[] bestAssignment, int bestErr) {
            this.bestAssignment = bestAssignment;
            this.bestErr = bestErr;
        }
    }

    private static void partitionValuesFromIndex(int[] values, int startIndex, int totalValue, int unassignedValue,
                                                 boolean[] testAssignment, int testValue,
                                                 PartitionData partitionData) {
        // If startIndex is beyond the end of the array,
        // then all entries have been assigned.
        if (partitionData.bestErr <= 1) return;
        if (startIndex >= values.length) {
            // We're done. See if this assignment is better than
            // what we have so far.
            int testErr = Math.abs(2 * testValue - totalValue);
            if (testErr < partitionData.bestErr) {
                // This is an improvement. Save it.
                partitionData.bestErr = testErr;
                partitionData.bestAssignment = Arrays.copyOf(testAssignment, testAssignment.length);

                // System.out.println(partitionData.bestErr);
            }
        } else {
            // See if there's any way we can assign
            // the remaining items to improve the solution.
            int testErr = Math.abs(2 * testValue - totalValue);
            if (testErr - unassignedValue < partitionData.bestErr) {
                // There's a chance we can make an improvement.
                // We will now assign the next item.
                unassignedValue -= values[startIndex];

                // Try adding values[startIndex] to set 1.
                testAssignment[startIndex] = true;
                partitionValuesFromIndex(values, startIndex + 1,
                        totalValue, unassignedValue,
                        testAssignment, testValue + values[startIndex],
                        partitionData);

                // Try adding values[startIndex] to set 2.
                testAssignment[startIndex] = false;
                partitionValuesFromIndex(values, startIndex + 1,
                        totalValue, unassignedValue,
                        testAssignment, testValue,
                        partitionData);
            }
        }
    }

    public static int[] readFile(String filePath, int size)throws Exception {
        int[] array = new int[size];
        Scanner sc = new Scanner(new BufferedReader(new FileReader(new File(filePath))));
        for(int i=0;sc.hasNextLine();i++) 
        {
        array[i]=Integer.parseInt(sc.nextLine());
        }
        return array;
    }

    public static void calculate(String filePath, String sortFunc, int size){
        try{
            int[] arr = readFile(filePath, size);
            double startTime;

            if(sortFunc.equals("DP")){
                startTime = System.nanoTime();
                
                if(findPartition(arr, size)){
                    System.out.println("Can be divided into two subsets of equal sum");
                } else {
                    System.out.println("Can not be divided into two subsets of equal sum");
                }

            }else{
            int startIndex = 0;
            int totalValue = Arrays.stream(arr).sum();
            int unassignedValue = totalValue;
            boolean[] testAssignment = new boolean[arr.length];
            int testValue = 0;
            boolean[] bestAssignment = new boolean[arr.length];
            int bestErr = Integer.MAX_VALUE;
    
            PartitionData partitionData = new PartitionData(bestAssignment, bestErr);

            startTime = System.nanoTime();
    
            partitionValuesFromIndex(arr, startIndex, totalValue, unassignedValue, testAssignment, testValue, partitionData);
    
            // Access updated values after the method call
            bestAssignment = partitionData.bestAssignment;
            bestErr = partitionData.bestErr;

            }

            double endTime = System.nanoTime();
            double duration = (endTime - startTime) / 1000000;
            System.out.println("Time taken to find partition in " + filePath + " using " + sortFunc + " is " + duration + " ms");
            System.out.println();
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public static void comaperAlgs(String fileName, int size){
        calculate(fileName, "DP", size);
        calculate(fileName, "B&B", size);
    }

    // Driver code
    public static void main(String[] args)
    {
 
        comaperAlgs("10elemen.txt", 10);
        comaperAlgs("40elemen.txt", 40);
        comaperAlgs("80elemen.txt", 80);
    }
}