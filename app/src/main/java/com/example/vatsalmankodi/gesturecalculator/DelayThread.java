package com.example.vatsalmankodi.gesturecalculator;

/**
 * Created by Vatsal Mankodi on 17-12-2016.
 */
public class DelayThread extends Thread {
    public DelayThread(){
        super();
        this.start();
    }
    public void run(){
        try {
            this.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
        }
    }
}
