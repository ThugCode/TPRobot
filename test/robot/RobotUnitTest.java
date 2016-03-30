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


public class RobotUnitTest {

	@Mock
    private LandSensor landsensor;
	@Mock
	private Battery battery;
	
	@Before
	public void setUp() throws Exception {
		
		battery = mock(Battery.class);
		landsensor = mock(LandSensor.class);
		
        Mockito.when(battery.getChargeLevel()).thenReturn((float) 100.0);
        Mockito.when(landsensor.distance(Mockito.any(), Mockito.any())).thenReturn((double) 5);
	}
	
    @Test
    public void testLand() throws UnlandedRobotException {
        Robot robot = new Robot();
        robot.land(new Coordinates(3,0));
        assertEquals(3, robot.getXposition());
        assertEquals(0, robot.getYposition());
    }

    @Test (expected=UnlandedRobotException.class)
    public void testRobotMustBeLandedBeforeAnyMove() throws Exception {
        Robot robot = new Robot();
        robot.moveForward();
    }

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

    @Test
    public void testTurnLeft() throws UnlandedRobotException {
        Robot robot = new Robot();
        robot.land(new Coordinates(3, 0));
        robot.turnLeft();
        assertEquals(WEST, robot.getDirection());
    }

    @Test
    public void testTurnRight() throws UnlandedRobotException {
        Robot robot = new Robot();
        robot.land(new Coordinates(3, 0));
        robot.turnRight();
        assertEquals(EAST, robot.getDirection());
    }

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
}