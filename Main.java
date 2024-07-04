package OS;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Write code to return a list having 2x the number of each element in the original list, returned by a new thread
// <1,2,3> => <2,4,6>

class ListModifier implements Runnable{
    int n;
    int[] newArr;
    int[] arr;
    public ListModifier(int[] arr){
        this.n = arr.length;
        this.newArr = new int[n];
        this.arr = arr;
    }
    public void run(){
        for(int i=0; i<n; i++){
            newArr[i] = 2*arr[i];
        }
        getNewArr();
    }

    public int[] getNewArr(){
        System.out.println(Thread.currentThread().getName() );
        return newArr;
    }
}   

class Student extends Thread{
    @Override
    public void run(){
        System.out.println("Thread is running");
    }
}

class SingleNumberPrinter implements Runnable{
    int n;
    public SingleNumberPrinter(int n){
        this.n = n;
    }
    public void run(){
        System.out.println(n + " from thread-" + Thread.currentThread().getName());
    }
}

class PrintANumber extends Thread{
    int n;
    public PrintANumber(int n){
        this.n = n;
    }
    public void run(){
        System.out.println(n + " from thread: " + Thread.currentThread().getName());
    }
}

class PrintNumbersFrom1to100 extends Thread{
    public void run(){
        for(int i = 1; i <= 100; i++){
            Thread thread = new Thread(new SingleNumberPrinter(i));
            thread.start();
        }
    }
}

class PrintNumbers extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println(i + " from thread: " + Thread.currentThread().getName());
        }
    }
}


class HelloThread extends Thread {
    @Override
    public void run() {
        System.out.println("Hello from a thread! with name: " + Thread.currentThread().getName());
    }
}

public class Main {
    public static void main(String[] args) {
        // Get the current thread
        // Thread currentThread = Thread.currentThread();
        
        // // Print the name of the current thread
        // // System.out.println("The name of the current thread is: " + currentThread.getName());

        // PrintNumbersFrom1to100 printNumbersFrom1to100 = new PrintNumbersFrom1to100();
        // printNumbersFrom1to100.start();

        ExecutorService es = Executors.newFixedThreadPool(1);
        int[] arr = {1,2,3};
        es.execute(new ListModifier(arr));
        es.shutdown();
    }
}
