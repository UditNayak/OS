import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SimultaneousAddedSubtracter {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Value value = new Value(0);
        ExecutorService es = Executors.newFixedThreadPool(10);

        AdderWithSynchronisation adder = new AdderWithSynchronisation(value);
        SubtracterWithSynchronisation subtracter = new SubtracterWithSynchronisation(value);

        Future<Void> adderFuture = es.submit(adder);
        Future<Void> subtracterFuture = es.submit(subtracter);
        adderFuture.get();
        subtracterFuture.get();

        System.out.println(value.getVal());
        es.shutdown();
    }
}

class AdderWithSynchronisation implements Callable<Void> {
    private Value value;

    public AdderWithSynchronisation(Value value) {
        this.value = value;
    }

    @Override
    public Void call() {
        for (int i = 1; i <= 100; i++) {
            this.value.add(i);
        }
        return null;
    }

}

class SubtracterWithSynchronisation implements Callable<Void> {
    private Value value;

    public SubtracterWithSynchronisation(Value value) {
        this.value = value;
    }

    @Override
    public Void call() {
        for (int i = 1; i <= 100; i++) {
            this.value.subtract(i);
        }
        return null;
    }
}

class Value {
    private int val;

    public Value(int value) {
        this.val = value;
    }

    public synchronized int getVal() {
        return val;
    }

    public synchronized void setVal(int val) {
        this.val = val;
    }

    synchronized public void add(int a) {
        this.val += a;
    }

    synchronized public void subtract(int a) {
        this.val -= a;
    }
}