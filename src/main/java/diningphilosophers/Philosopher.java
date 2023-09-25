package diningphilosophers;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Philosopher
        extends Thread {

    private static int seed = 1;
    private final Random myRandom = new Random(System.currentTimeMillis() + seed++);
    private final static int DELAY = 1000;
    private final ChopStick myLeftStick;
    private final ChopStick myRightStick;
    private boolean running = true;

    public Philosopher(String name, ChopStick left, ChopStick right) {
        super(name);
        myLeftStick = left;
        myRightStick = right;
    }

    @Override
    public void run() {
        while (running) {
            try {
                think();
                /*if (myLeftStick.tryTake()){
                    if(myRightStick.tryTake()){
                        // success : process
                        eat();
                        // release resources
                        myLeftStick.release();
                        myRightStick.release();
                    }
                }*/
                if (myLeftStick.newTake()){
                    if(myRightStick.newTake()){
                        // success : process
                        eat();
                        // release resources
                        myLeftStick.newRelease();
                        myRightStick.newRelease();
                    }
                }
            } catch (InterruptedException ex) {
                Logger.getLogger("Table").log(Level.SEVERE, "{0} Interrupted", this.getName());
            }
        }
    }

    // Permet d'interrompre le philosophe "proprement" :
    // Il relachera ses baguettes avant de s'arrêter
    public void leaveTable() {
        running = false;
    }

    private void think() throws InterruptedException {
        int delay = myRandom.nextInt(500 + DELAY);
        System.out.println(this.getName() + " Starts Thinking for: " + delay + " ms");
        sleep(delay); // Le thread peut être interrompu ici
        System.out.println(this.getName() + " Stops Thinking");
    }

    private void eat() throws InterruptedException {
        int delay = myRandom.nextInt(100 + DELAY);
        System.out.println(this.getName() + " Starts Eating for:" + delay + " ms");
        sleep(delay); // Le thread peut être interrompu ici
        System.out.println(this.getName() + " Stops Eating");
    }
}
