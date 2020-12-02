package main.java.bgu.spl.mics.application.passiveObjects;


import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class Ewoks {
     private static  Ewoks ewoks;

     private Ewok[] ewokArray;

     private Ewoks(int count)
     {
         ewokArray=new Ewok[count];
         for (int i=0;i<count;i++){
             ewokArray[i]=new Ewok(i);
         }
     }

     public synchronized Ewoks getEwoks(int count){
         if(ewoks==null){
             ewoks=new Ewoks(count);
         }
         return ewoks;
     }



     public synchronized void fetchEwok(int[] count){
        for (int i=0;i<count.length;i++){
            while (!ewokArray[count[i]].isAvailable()){
               try {
                   this.wait();
                   i=0;
               }catch (InterruptedException e){}
            }
        }
        for (int i=0;i<count.length;i++) {
            ewokArray[count[i]].acquire();
        }



     }
}
