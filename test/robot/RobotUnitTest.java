package robot;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Arrays;

import static robot.Direction.WEST;
import static robot.Direction.EAST;

/***
 * Classe de test de la classe Robot
 * @author LETOURNEUR - GERLAND
 *
 */
public class RobotUnitTest {

	@Mock
    private LandSensor landsensor;
	@Mock
	private Battery battery;
	
	@Before
	public void setUp() throws Exception {
		
		battery = mock(Battery.class);
		landsensor = mock(LandSensor.class);
		
		//Retourne 17 pour l'energie nécessaire au déplacement
        Mockito.when(landsensor.getPointToPointEnergyCoefficient(Mockito.any(), Mockito.any())).thenReturn((double) 17.0);
    }
	
	/**
	 * Test de la fonction testLand()
	 */
    @Test
    public void testLand() throws UnlandedRobotException {
        Robot robot = new Robot();
        robot.land(new Coordinates(3,0));
        assertEquals(3, robot.getXposition());
        assertEquals(0, robot.getYposition());
    }

    /**
	 * Test de la fonction testRobotMustBeLandedBeforeAnyMove()
	 */
    @Test (expected=UnlandedRobotException.class)
    public void testRobotMustBeLandedBeforeAnyMove() throws Exception {
        Robot robot = new Robot();
        robot.moveForward();
    }

    /**
	 * Test de la fonction testMoveForward()
	 */
    @Test
    public void testMoveForward() throws UnlandedRobotException {
        Robot robot = new Robot();
        robot.land(new Coordinates(3, 0));
        int currentXposition = robot.getXposition();
        int currentYposition = robot.getYposition();
        double batteryLevel = robot.getbattery().getChargeLevel();
        robot.moveForward();
        assertEquals(currentXposition, robot.getXposition());
        assertEquals(currentYposition+1, robot.getYposition());
        assertTrue(batteryLevel>robot.getbattery().getChargeLevel());
    }

    /**
	 * Test de la fonction testMoveBackward()
	 */
    @Test
    public void testMoveBackward() throws UnlandedRobotException {
        Robot robot = new Robot();
        robot.land(new Coordinates(3,0));
        int currentXposition = robot.getXposition();
        int currentYposition = robot.getYposition();
        robot.moveBackward();
        assertEquals(currentXposition, robot.getXposition());
        assertEquals(currentYposition - 1, robot.getYposition());
    }

    /**
	 * Test de la fonction testTurnLeft()
	 */
    @Test
    public void testTurnLeft() throws UnlandedRobotException {
        Robot robot = new Robot();
        robot.land(new Coordinates(3, 0));
        robot.turnLeft();
        assertEquals(WEST, robot.getDirection());
    }

    /**
	 * Test de la fonction testTurnRight()
	 */
    @Test
    public void testTurnRight() throws UnlandedRobotException {
        Robot robot = new Robot();
        robot.land(new Coordinates(3, 0));
        robot.turnRight();
        assertEquals(EAST, robot.getDirection());
    }

    /**
	 * Test de la fonction testFollowInstruction()
	 */
    @Test
    public void testFollowInstruction() throws UnlandedRobotException {
        Robot robot = new Robot();
        double batteryLevel = robot.getbattery().getChargeLevel();
        robot.land(new Coordinates(5, 7));
        robot.setRoadBook(new RoadBook(Arrays.asList(Instruction.FORWARD, Instruction.FORWARD, Instruction.TURNLEFT, Instruction.FORWARD)));
        robot.letsGo();
        assertEquals(4, robot.getXposition());
        assertEquals(9, robot.getYposition());
        assertTrue(batteryLevel>robot.getbattery().getChargeLevel());
    }
    
    /********
     * 
     * Les tests ci-dessous sont utilisés avec les Mocks
     * 
     *******/
    
    /**
	 * Test des mocks Battery et Landsensor
	 * Batterie rechargé à 10 avec un déplacement à 17
	 * donc le robot n'avance pas
	 */
    @Test
    public void testRobotNotEnoughtBattery() throws Exception {
    	
    	//Retourne 10 pour l'energie de la batterie donc < au déplacement
        Mockito.when(battery.getChargeLevel()).thenReturn((float) 10.0);
        //Retourne une exception sur la méthode use de la batterie
        Mockito.doThrow(new InsufficientChargeException()).when(battery).use(Mockito.anyDouble());
        
        Robot robot = new Robot(1.0, battery, landsensor);
        robot.land(new Coordinates(3, 0));
        int positionX = robot.getXposition();
        int positionY = robot.getYposition();
        robot.moveForward();
        
        assertEquals(positionX, robot.getXposition());
        assertEquals(positionY, robot.getYposition());
    }
    
    /**
	 * Test des mocks Battery et Landsensor
	 * Batterie rechargé à 20 avec un déplacement à 17
	 * donc le robot avance en Y
	 */
    @Test
    public void testRobotEnoughtBattery() throws Exception {
    	
    	//Retourne 20 pour l'energie de la batterie donc > au déplacement
        Mockito.when(battery.getChargeLevel()).thenReturn((float) 20.0);
        
        Robot robot = new Robot(1.0, battery, landsensor);
        robot.land(new Coordinates(3, 0));
        int positionX = robot.getXposition();
        int positionY = robot.getYposition();
        robot.moveForward();
        
        assertEquals(positionX, robot.getXposition());
        assertEquals(positionY+1, robot.getYposition());
    }
}