package avt;

public class BubbleSort {
    private long[] sortedArray;
    private int currentLength;
    public BubbleSort(int max){
        sortedArray = new long[max];
        currentLength = 0;
    }
    public void insert(long value) {
        if(currentLength == sortedArray.length){
            System.out.println("Cannot insert - Array is full");
            return;
        }
        sortedArray[currentLength] = value;
        currentLength++;
        System.out.println("  * * * New Array * * * ");
        display();
    }
    public void display() {
        System.out.println(" - - - - - - - - - - - - ");
        for(int i = 0 ; i<currentLength; i++){
            System.out.print(" [" + sortedArray[i] + "] ");
        }
        System.out.println("");
        for(int i = 0 ; i<currentLength; i++){
            System.out.print(" [" + i + "] ");
        }
        System.out.println();
        System.out.println(" - - - - - - - - - - - - ");
    }
    private void swap(int a, int b){
        long temp = sortedArray[a];
        sortedArray[a] = sortedArray[b];
        sortedArray[b] = temp;
        System.out.println("Swapping values " + sortedArray[a] + " and " + sortedArray[b] + ".");
        System.out.println("  * * * New Array * * * ");
        display();
        
    }
    public void bubbleSort(){
        int out, in;
        System.out.println("Running Bubble Sort...");
        for(out = currentLength-1 ; out>0 ; out--){
            for(in = 0 ; in < out; in++){
                System.out.println("Current in/out indexes: out = " + out + " " + "in = " + in);
                display();
                if(sortedArray[in] > sortedArray[in + 1]){
                    swap(in, in + 1);
                }
            }
        }

    }
}
