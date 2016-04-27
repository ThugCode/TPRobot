package robot;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

/***
 * Classe de test de la classe LandSensor
 * @author LETOURNEUR - GERLAND
 *
 */
public class LandSensorUnitTest {

	/**
	 * Test de la fonction distance(Coordinates coordinate1, Coordinates coordinate2)
	 */
    @Test
    public void testDistance() {
        Random rand = new Random();
        LandSensor landS = new LandSensor(rand);
        double distance = landS.distance(new Coordinates(1, 1), new Coordinates(8, 8));
        assertEquals(9899f, Math.round(distance*1000), 0);
    }
}