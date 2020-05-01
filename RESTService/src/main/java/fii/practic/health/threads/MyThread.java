package fii.practic.health.threads;

public class MyThread extends Thread {

    private int id;

    public MyThread(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        System.out.println("Hello from " + Thread.currentThread().getName());
    }

    @Override
    public String toString() {
        return String.format("Thread{id = %d}", id);
    }
}
