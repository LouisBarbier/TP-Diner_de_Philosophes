package diningphilosophers;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ChopStick {

    private static int stickCount = 0;

    private boolean iAmFree = true;
    private final int myNumber;

    private final Lock verrou = new ReentrantLock();
    private final Condition libre = verrou.newCondition();
	private final Condition pasLibre = verrou.newCondition();

    public ChopStick() {
        myNumber = ++stickCount;
    }

    synchronized public void take() throws InterruptedException {
        while (!iAmFree) {
            wait();
        }
        // assert iAmFree;
        iAmFree = false;
        System.out.println("Stick " + myNumber + " Taken");
        // Pas utile de faire notifyAll ici, personne n'attend qu'elle soit occupée
    }

    synchronized public boolean tryTake() throws InterruptedException {
        if (!iAmFree){
            wait(500);
            if (!iAmFree) {
                return false;
            }
        }
        iAmFree = false;
        System.out.println("Stick " + myNumber + " Taken");
        return true;
    }

    public boolean newTake() throws InterruptedException {
        verrou.lock();
        try {
			while (!iAmFree) {
				libre.await();  // J'attends qu'on me signale que la baguette est libre
			}
			if (!iAmFree) {
                return false;
            }
            iAmFree = false;
            System.out.println("Stick " + myNumber + " Taken");
			pasLibre.signalAll(); // Je signale que la baguette n'est pas libre
            return true;
		} finally {
			verrou.unlock();
		}
    }

    synchronized public void release() {
        // assert !iAmFree;
        System.out.println("Stick " + myNumber + " Released");
        iAmFree = true;
        notifyAll(); // On prévient ceux qui attendent que la baguette soit libre
    }

    public void newRelease() throws InterruptedException {
        verrou.lock();
		try {
			while (iAmFree) {
				pasLibre.await();
			} // J'attends qu'on me signale que la pile n'est pas vide
			System.out.println("Stick " + myNumber + " Released");
            iAmFree = true;
			libre.signalAll();
		} finally {
			verrou.unlock();
		}
    }

    @Override
    public String toString() {
        return "Stick#" + myNumber;
    }
}
