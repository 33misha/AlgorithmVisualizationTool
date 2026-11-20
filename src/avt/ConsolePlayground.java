package avt;
import java.util.Scanner;
public class ConsolePlayground {
    public static void main(String[] args) {
        start();
    }

    public static void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Console Playground!");
        breakLine();
        System.out.println("Choose alorithim to visualize : \n " + " - - - - - - - - - - - - \n" +
                           "[0] Bubble Sort \n" +
                           "[1] Selection Sort \n" +
                           "[2] Stack \n" +
                           "[3] Queue \n" +
                           "[4] Linked List \n" +
                           "[5] Merge Sort \n" +
                           "[6] Quick Sort \n");

        // This method runs in the app's console page and can also run directly.
        // Use System.out.print/println for text and new Scanner(System.in) for input.
        // TODO: Write your welcome text and instructions here.
        // TODO: Add your algorithm choices and input prompts when you are ready.
    }
    public static void breakLine(){
        System.out.println(" - - - - - - - - - - - - ");
    }
}
