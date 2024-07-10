import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Network_Topology {
  public static void main(String[] args) {
    Store store = new Store(5, 0);
    ExecutorService es = Executors.newCachedThreadPool();

    for (int i = 0; i < 100; i++) {
      Producer p = new Producer(store, i);
      es.execute(p);
    }

    for (int i = 0; i < 100; i++) {
      Consumer c = new Consumer(store, i);
      es.execute(c);
    }
    es.shutdown();
  }
}

class Producer implements Runnable {
  int id;
  private Store store;

  public Producer(Store s, int i) {
    this.store = s;
    this.id = i;
  }

  @Override
  public void run() {
    while (true) {
      synchronized (store) {
        // producer seemaphore--
        if (store.getCurrentSize() < store.getMaxSize()) {
          System.out.println("Addition is done by Producer with id: " + id);
          store.setCurrentSize(store.getCurrentSize() + 1);
          System.out.println(
              "                                                   store after the poll: " + store.getCurrentSize());
        }
        // consumer seemaphore++
      }

    }
  }
}

class Consumer implements Runnable {
  private Store store;
  int id;

  public Consumer(Store s, int i) {
    this.store = s;
    this.id = i;
  }

  @Override
  public void run() {
    while (true) {
      synchronized (store) {
        // Consumer seemaphore--
        if (store.getCurrentSize() > 0) {
          System.out.println("Substraction is done by Consumer with id: " + id);
          store.setCurrentSize(store.getCurrentSize() - 1);
          System.out.println(
              "                                                   store after the poll: " + store.getCurrentSize());
        }
        // producer seemaphore++
      }

    }
  }
}

class Store {
  private int maxSize;
  private int currentSize;

  public Store(int m, int c) {
    this.currentSize = c;
    this.maxSize = m;
  }

  public synchronized int getMaxSize() {
    return maxSize;
  }

  public synchronized void setMaxSize(int maxSize) {
    this.maxSize = maxSize;
  }

  public synchronized int getCurrentSize() {
    return currentSize;
  }

  public synchronized void setCurrentSize(int currentSize) {
    this.currentSize = currentSize;
  }
}