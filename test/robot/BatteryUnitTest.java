package robot;

import static org.junit.Assert.*;
import org.junit.Test;


public class BatteryUnitTest {

    @Test
    public void testCharge() {
        Battery cell = new Battery();
        assertEquals(100f, cell.getChargeLevel(),0);
        cell.charge();
        assertEquals(111f, cell.getChargeLevel(),0);
    }

  
}