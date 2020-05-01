package fii.practic.health.threads;

public class MyRunnable implements Runnable {

    private int id;

    public MyRunnable(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        System.out.println("Hello from " +  Thread.currentThread().getName());
    }

    @Override
    public String toString() {
        return String.format("MyRunnable{id = %d}", id);
    }
}
