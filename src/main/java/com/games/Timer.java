package com.games;

import javafx.scene.control.Label;

public class Timer implements Runnable{

    private int seconds = 0;
    private int minutes = 0;
    private Label clock; 
    
    public Timer(Label clock){
        this.clock = clock;
    }

    @Override
    public void run() {
        try{
            while(true){
                seconds++;
                if(seconds == 60){
                    seconds = 0;
                    minutes++;
                }
                clock.setText(this.toString());
                Thread.sleep(1000);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public String toString(){
        String secondsString = seconds<10?"0"+seconds:seconds+""; 
        String minutesString = minutes<10?"0"+minutes:minutes+""; 
        return minutesString + ":" + secondsString;
    } 
}
