package test.java.bgu.spl.mics;


import main.java.bgu.spl.mics.application.passiveObjects.Ewok;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;


public class EwokTest {
    private Ewok ewok;

    @BeforeEach
    public void setup(){
        ewok=new Ewok(1);
    }

    @Test
    public void testAcquire(){
        ewok.acquire();
        assertFalse(ewok.isAvailable());
    }
    @Test
    public void testRelease(){
        ewok.release();
        assertTrue(ewok.isAvailable());
    }

}
