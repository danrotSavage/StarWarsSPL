package main.java.bgu.spl.mics.application.passiveObjects;


import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing a Diary - in which the flow of the battle is recorded.
 * We are going to compare your recordings with the expected recordings, and make sure that your output makes sense.
 * <p>
 * Do not add to this class nothing but a single constructor, getters and setters.
 */
public class Diary {
    AtomicInteger totalAttack;
    long [] timeArray;


    public static Diary getInstance() {
        if(DiaryInstance==null)
        {
            DiaryInstance=new Diary();
        }
        return DiaryInstance;
    }

    public AtomicInteger getTotalAttacks(){
        return totalAttack;
    }
    public long getHanSoloFinish() {
        return HanSoloFinish;
    }

    public void setHanSoloFinish(long hanSoloFinish) {
        HanSoloFinish = hanSoloFinish;
    }

    public long getC3POFinish() {
        return C3POFinish;
    }

    public void setC3POFinish(long c3POFinish) {
        C3POFinish = c3POFinish;
    }

    public long getR2D2Deactivate() {
        return R2D2Deactivate;
    }

    public void setR2D2Deactivate(long r2D2Deactivate) {
        R2D2Deactivate = r2D2Deactivate;
    }

    public long getLeiaTerminate() {
        return LeiaTerminate;
    }

    public void setLeiaTerminate(long leiaTerminate) {
        LeiaTerminate = leiaTerminate;
    }

    public long getHanSoloTerminate() {
        return HanSoloTerminate;
    }

    public void setHanSoloTerminate(long hanSoloTerminate) {
        HanSoloTerminate = hanSoloTerminate;
    }

    public long getC3POTerminate() {
        return C3POTerminate;
    }

    public void setC3POTerminate(long c3POTerminate) {
        C3POTerminate = c3POTerminate;
    }

    public long getR2D2Terminate() {
        return R2D2Terminate;
    }

    public void setR2D2Terminate(long r2D2Terminate) {
        R2D2Terminate = r2D2Terminate;
    }

    public long getLandoTerminate() {
        return LandoTerminate;
    }

    public void setLandoTerminate(long landoTerminate) {
        LandoTerminate = landoTerminate;
    }

    long HanSoloFinish;
    long C3POFinish;
    long R2D2Deactivate;
    long LeiaTerminate;
    long HanSoloTerminate;
    long C3POTerminate;
    long R2D2Terminate;
    long LandoTerminate;


    private Diary(){
        totalAttack = new AtomicInteger();



        long HanSoloFinish=0;
        long C3POFinish=0;
        long R2D2Deactivate=0;
        long LeiaTerminate=0;
        long HanSoloTerminate=0;
        long C3POTerminate=0;
        long R2D2Terminate=0;
        long LandoTerminate=0;


    }

    private static  Diary DiaryInstance = new Diary();
    public synchronized static Diary getDiary(){
        if(DiaryInstance==null)
        {
            DiaryInstance=new Diary();
        }
        return DiaryInstance;
    }

    public void addAttack()
    {
        int val;
        do {
            val = totalAttack.get();
        }
        while (!totalAttack.compareAndSet(val,val+1));
    }

    @Override
    public String toString() {
        timeArray= new long[8];
        timeArray[0]= HanSoloFinish;
        timeArray[1]= C3POFinish;
        timeArray[2]= R2D2Deactivate;
        timeArray[3]= LeiaTerminate;
        timeArray[4]= HanSoloTerminate;
        timeArray[5]= C3POTerminate;
        timeArray[6]= R2D2Terminate;
        timeArray[7]= LandoTerminate;
        Arrays.sort(timeArray);
        for (int i = 0; i < timeArray.length; i++) {
            System.out.println(timeArray[i]);        }

        return "Diary{" +
                "totalAttack=" + totalAttack +
                ", HanSoloFinish=" + HanSoloFinish +
                ", C3POFinish=" + C3POFinish +
                ", R2D2Deactivate=" + R2D2Deactivate +
                ", LeiaTerminate=" + LeiaTerminate +
                ", HanSoloTerminate=" + HanSoloTerminate +
                ", C3POTerminate=" + C3POTerminate +
                ", R2D2Terminate=" + R2D2Terminate +
                ", LandoTerminate=" + LandoTerminate +
                '}';
    }

    public AtomicInteger getNumberOfAttacks() {
        return totalAttack;
    }

    public void resetNumberAttacks() {
        int val;
        do {
            val = totalAttack.get();
        }
        while (!totalAttack.compareAndSet(val,0));
    }
}
