import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class Network_Topology {
  public static void main(String[] args) {
    Semaphore pSemaphore = new Semaphore(3);
    Semaphore cSemaphore = new Semaphore(0);

    Store store = new Store(5, 0);
    ExecutorService es = Executors.newCachedThreadPool();

    for (int i = 0; i < 100; i++) {
      Producer p = new Producer(store, i, pSemaphore, cSemaphore);
      es.execute(p);
    }

    for (int i = 0; i < 100; i++) {
      Consumer c = new Consumer(store, i, pSemaphore, cSemaphore);
      es.execute(c);
    }
    es.shutdown();
  }
}

class Producer implements Runnable {
  int id;
  private Store store;
  Semaphore pSemaphore;
  Semaphore cSemaphore;

  public Producer(Store s, int i, Semaphore pSemaphore, Semaphore cSemaphore) {
    this.store = s;
    this.id = i;
    this.pSemaphore = pSemaphore;
    this.cSemaphore = cSemaphore;
  }

  @Override
  public void run() {
    while (true) {

      // producer seemaphore--
      try {
        pSemaphore.acquire();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      if (store.getCurrentSize() < store.getMaxSize()) {
        System.out.println("Addition is done by Producer with id: " + id);
        store.setCurrentSize(store.getCurrentSize() + 1);
        System.out.println(
            "                                                   store after the poll: " + store.getCurrentSize());
      }
      // consumer seemaphore++
      cSemaphore.release();

    }
  }
}

class Consumer implements Runnable {
  private Store store;
  int id;
  Semaphore pSemaphore;
  Semaphore cSemaphore;

  public Consumer(Store s, int i, Semaphore pSemaphore, Semaphore cSemaphore) {
    this.store = s;
    this.id = i;
    this.pSemaphore = pSemaphore;
    this.cSemaphore = cSemaphore;
  }

  @Override
  public void run() {
    while (true) {

      // Consumer seemaphore--
      try {
        cSemaphore.acquire();
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      if (store.getCurrentSize() > 0) {
        System.out.println("Substraction is done by Consumer with id: " + id);
        store.setCurrentSize(store.getCurrentSize() - 1);
        System.out.println(
            "                                                   store after the poll: " + store.getCurrentSize());
      }
      // producer seemaphore++
      pSemaphore.release();

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