import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SimultaneousAddedSubtracter {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Value value = new Value(0);
        ExecutorService es = Executors.newFixedThreadPool(10);
        // Lock lock = new ReentrantLock();
        // We dont need the lock object as we can use the object itself as a lock of the shared resource

        AdderWithSynchronisation adder = new AdderWithSynchronisation(value);
        SubtracterWithSynchronisation subtracter = new SubtracterWithSynchronisation(value);

        Future<Void> adderFuture = es.submit(adder);
        Future<Void> subtracterFuture = es.submit(subtracter);
        adderFuture.get();
        subtracterFuture.get();
        
        System.out.println(value.val);
        es.shutdown();
    }
}

class AdderWithSynchronisation implements Callable<Void>{
    Value value;
    public AdderWithSynchronisation(Value value){
        this.value = value;
    }
    @Override
    public Void call() {
        for(int i = 1; i <= 100; i++){
            synchronized(value){
                value.val += i;
            }
        }
        return null;
    }

}

class SubtracterWithSynchronisation implements Callable<Void>{
    Value value;
    public SubtracterWithSynchronisation(Value value){
        this.value = value;
    }
    
    @Override
    public Void call() {
        for(int i = 1; i <= 100; i++){
            synchronized(value){
                value.val -= i;
            }
        }
        return null;
    }
}

class Value{
    int val;
    public Value(int value){
        this.val = value;
    }
}