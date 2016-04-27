package robot;

import java.util.ArrayList;

import static robot.Direction.*;
import static robot.Instruction.*;

/***
 * Classe du robot
 * @author LETOURNEUR - GERLAND
 *
 */
public class Robot {

	private Coordinates position;				//Position du robot
	private Direction direction;				//Direction du robot
	private boolean isLanded;					//Etat du robot (posé/non posé)
	private RoadBook roadBook;					//Carnet de route du robot
	private Battery battery;					//Batterie du robot
	private LandSensor sensor;					//Capteur du robot
	private final double energyConsumption;
	
	/***
	 * Constructeur
	 */
	public Robot() {
		this(1.0, new Battery(), new LandSensor());
	}

	/***
	 * Constructeur
	 */
	public Robot(double energyConsumption, Battery battery, LandSensor sensor) {
		this.isLanded = false;
		this.energyConsumption = energyConsumption;
		this.battery = battery;
		this.sensor = sensor;
	}

	/***
	 * Positionner le robot à la position donnée
	 * @param landPosition
	 */
	public void land(Coordinates landPosition) {
		position = landPosition;
		direction = NORTH;
		isLanded = true;
	}

	/***
	 * Getter du la position en X
	 * @return int
	 * @throws UnlandedRobotException
	 */
	public int getXposition() throws UnlandedRobotException {
		if (!isLanded)
			throw new UnlandedRobotException();
		return position.getX();
	}

	/***
	 * Getter du la position en Y
	 * @return int
	 * @throws UnlandedRobotException
	 */
	public int getYposition() throws UnlandedRobotException {
		if (!isLanded)
			throw new UnlandedRobotException();
		return position.getY();
	}

	/***
	 * Getter du la direction
	 * @return Direction
	 * @throws UnlandedRobotException
	 */
	public Direction getDirection() throws UnlandedRobotException {
		if (!isLanded)
			throw new UnlandedRobotException();
		return direction;
	}

	/***
	 * Mouvement en avant du robot
	 * @throws UnlandedRobotException
	 */
	public void moveForward() throws UnlandedRobotException {
		
        if (!isLanded) throw new UnlandedRobotException();
        
        Coordinates oldPosition = this.position;
        Coordinates futurPosition = MapTools.nextForwardPosition(position, direction);
        double spent = this.sensor.getPointToPointEnergyCoefficient(oldPosition, futurPosition);
        
    	try {
    		//Si la batterie a assez d'énergie, le robot avance
    		this.battery.use(spent);
    		//La position est mise à jour
    		this.position = MapTools.nextForwardPosition(position, direction);
        } 
    	catch (InsufficientChargeException e) {
    		//Sinon le robot se charge
    		this.battery.charge();
    	}
    }

	/***
	 * Mouvement en arriere du robot
	 * @throws UnlandedRobotException
	 */
	public void moveBackward() throws UnlandedRobotException {
		if (!isLanded)
			throw new UnlandedRobotException();
		this.position = MapTools.nextBackwardPosition(position, direction);
	}

	/***
	 * Rotation du robot vers la gauche
	 * @throws UnlandedRobotException
	 */
	public void turnLeft() throws UnlandedRobotException {
		if (!isLanded)
			throw new UnlandedRobotException();
		direction = MapTools.counterclockwise(direction);
	}

	/***
	 * Rotation du robot vers la droite
	 * @throws UnlandedRobotException
	 */
	public void turnRight() throws UnlandedRobotException {
		if (!isLanded)
			throw new UnlandedRobotException();
		direction = MapTools.clockwise(direction);
	}

	/***
	 * Setter du carnet de route
	 * @param roadBook
	 */
	public void setRoadBook(RoadBook roadBook) {
		this.roadBook = roadBook;
	}

	/***
	 * Lancer le robot en suivant le carnet de route
	 * @throws UnlandedRobotException
	 */
	public void letsGo() throws UnlandedRobotException {
		while (roadBook.hasInstruction()) {
			Instruction nextInstruction = roadBook.next();
			if (nextInstruction == FORWARD)
				moveForward();
			else if (nextInstruction == BACKWARD)
				moveBackward();
			else if (nextInstruction == TURNLEFT)
				turnLeft();
			else if (nextInstruction == TURNRIGHT)
				turnRight();
		}
	}

	/***
	 * Bouger le robot à la position donnée
	 * @param destination
	 * @throws UnlandedRobotException
	 */
	public void moveTo(Coordinates destination) throws UnlandedRobotException {
		if (!isLanded)
			throw new UnlandedRobotException();
		RoadBook book = RoadBookCalculator.calculateRoadBook(direction, position, destination,
				new ArrayList<Instruction>());
		setRoadBook(book);
		letsGo();
	}
	
	public Battery getbattery() { 
		return this.battery; 
	}
}
