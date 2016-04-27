package robot;

import static org.junit.Assert.*;

import org.junit.Test;

/***
 * Classe de test de la classe Battery
 * @author LETOURNEUR - GERLAND
 *
 */
public class BatteryUnitTest {

	/**
	 * Test de la fonction charge()
	 */
    @Test
    public void testCharge() {
        Battery cell = new Battery();
        assertEquals(100f, cell.getChargeLevel(), 0);
        cell.charge();
        assertEquals(111f, cell.getChargeLevel(), 0);
    }

    /**
	 * Test de la fonction setUp() sur 3 secondes
	 */
    @Test
    public void testSetUp() {
    	
    	try {
	        Battery batterie = new Battery();
	        assertEquals(100f, batterie.getChargeLevel(), 0);
	        batterie.setUp();
			Thread.sleep(1000);
	        assertEquals(111f, batterie.getChargeLevel(), 0);
	        Thread.sleep(1000);
	        assertEquals(123f, Math.round(batterie.getChargeLevel()), 0);
	        Thread.sleep(1000);
	        assertEquals(136f, Math.round(batterie.getChargeLevel()), 0);
    	} catch (InterruptedException e) {}
    }

    /**
	 * Test de la fonction use() avec niveau de charge suffisant
	 */
    @Test
    public void testUse() {
    	try {
	        Battery batterie = new Battery();
	        batterie.use(40f);
	        assertEquals(60f, batterie.getChargeLevel(), 0);
    	} catch (InsufficientChargeException e) {}
    }
    
    /**
	 * Test de la fonction use() avec niveau de charge insuffisant
	 */
    @Test (expected = InsufficientChargeException.class)
    public void testUseInsuffisant() throws InsufficientChargeException {
        Battery batterie = new Battery();
        batterie.use(110f);
    }

    /**
	 * Test de la fonction use() pour vider la batterie
	 */
    @Test
    public void testUseMax() {
    	try {
	        Battery batterie = new Battery();
	        batterie.use(batterie.getChargeLevel());
	        assertEquals(0f, batterie.getChargeLevel(), 0);
	    } catch (InsufficientChargeException e) {}
    }
}