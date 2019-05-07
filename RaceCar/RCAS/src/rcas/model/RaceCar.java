package rcas.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents a Race Car.
 *
 * @author suy
 *
 */
public class RaceCar {

	private static String DEFAULT_NAME = "Race Car";
	private static double DEFAULT_TRACK = 1.6;
	private static double DEFAULT_WHEELBASE = 2.6;
	private static double DEFAULT_COGHEIGHT = 0.5;
	private static double DEFAULT_FRONTROLLDIST = 0.5;

	// the name of the car.
	private SimpleStringProperty name = new SimpleStringProperty(DEFAULT_NAME);

	// front track width of the car in m.
	private SimpleDoubleProperty frontTrack = new SimpleDoubleProperty(DEFAULT_TRACK);

	// rear track width of the car in m.
	private SimpleDoubleProperty rearTrack = new SimpleDoubleProperty(DEFAULT_TRACK);

	// wheelbase of the car in m.
	private SimpleDoubleProperty wheelbase = new SimpleDoubleProperty(DEFAULT_WHEELBASE);

	// center of gravity (cog) height in m.
	private SimpleDoubleProperty cogHeight = new SimpleDoubleProperty(DEFAULT_COGHEIGHT);

	// front roll distribution as a simplification of a suspension model.
	private SimpleDoubleProperty frontRollDist = new SimpleDoubleProperty(DEFAULT_FRONTROLLDIST);

	// front left corner weight in kg.
	private SimpleDoubleProperty cornerWeightFL;

	// front right corner weight in kg.
	private SimpleDoubleProperty cornerWeightFR;

	// rear left corner weight in kg.
	private SimpleDoubleProperty cornerWeightRL;

	// rear right corner weight in kg.
	private SimpleDoubleProperty cornerWeightRR;

	// front axle tire model of the car.
	private TireModel frontAxleTireModel;

	// rear axle tire model of the car.
	private TireModel rearAxleTireModel;

	//
	private String color;

	/**
	 * Creates a new Race Car Object with the given corner weights in kg and the
	 * following defaults: <br>
	 * <br>
	 * Name: "Race Car"<br>
	 * Front and Rear Track: 1.6m<br>
	 * Wheelbase: 2.6m<br>
	 * Center of Gravity Height: 0.5m<br>
	 * Tires: Default front and rear Tire Model
	 *
	 *
	 * @param cornerWeightFL
	 *            - Front Left corner weight in kg.
	 * @param cornerWeightFR
	 *            - Front Rear corner weight in kg.
	 * @param cornerWeightRL
	 *            - Rear Left corner weight in kg.
	 * @param cornerWeightRR
	 *            - Rear Right corner weight in kg.
	 */
	public RaceCar(String name, double cornerWeightFL, double cornerWeightFR, double cornerWeightRL, double cornerWeightRR, String color) {
		this.name           = new SimpleStringProperty(name);
		this.cornerWeightFL = new SimpleDoubleProperty(cornerWeightFL);
		this.cornerWeightFR = new SimpleDoubleProperty(cornerWeightFR);
		this.cornerWeightRL = new SimpleDoubleProperty(cornerWeightRL);
		this.cornerWeightRR = new SimpleDoubleProperty(cornerWeightRR);
		this.color          = color;

		// initialize tire models with a default tire model.
		this.frontAxleTireModel = new MagicFormulaTireModel();
		this.rearAxleTireModel  = new MagicFormulaTireModel();
	}

	public RaceCar(String name, double frontTrack, double rearTrack, double wheelbase, double cogHeight, double frontRollDist, double cornerWeightFL, double cornerWeightFR, double cornerWeightRL, double cornerWeightRR, String color) {
		this.name           = new SimpleStringProperty(name);
		this.frontTrack     = new SimpleDoubleProperty(frontTrack);
		this.rearTrack      = new SimpleDoubleProperty(rearTrack);
		this.wheelbase      = new SimpleDoubleProperty(wheelbase);
		this.cogHeight      = new SimpleDoubleProperty(cogHeight);
		this.frontRollDist  = new SimpleDoubleProperty(frontRollDist);
		this.cornerWeightFL = new SimpleDoubleProperty(cornerWeightFL);
		this.cornerWeightFR = new SimpleDoubleProperty(cornerWeightFR);
		this.cornerWeightRL = new SimpleDoubleProperty(cornerWeightRL);
		this.cornerWeightRR = new SimpleDoubleProperty(cornerWeightRR);
		this.color          = color;

		// initialize tire models with a default tire model.
		this.frontAxleTireModel = new MagicFormulaTireModel();
		this.rearAxleTireModel  = new MagicFormulaTireModel();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name.get();
	}

	/**
	 * @param name
	 *            the name of the car to be set
	 */
	public void setName(String name) {
		this.name.set(name);
	}

	/**
	 *
	 * @return - the name of this car.
	 */
	public StringProperty nameProperty() {
		return this.name;
	}

	/**
	 * @return the frontTrack in m.
	 */
	public Double getFrontTrack() {
		return this.frontTrack.get();
	}

	/**
	 * @param frontTrack
	 *            the frontTrack to set in m.
	 */
	public void setFrontTrack(Double frontTrack) {
		this.frontTrack.set(frontTrack);
	}

	/**
	 *
	 * @return - the front track in m.
	 */
	public DoubleProperty frontTrackProperty() {
		return this.frontTrack;
	}

	/**
	 * @return the rearTrack in m.
	 */
	public Double getRearTrack() {
		return this.rearTrack.get();
	}

	/**
	 * @param rearTrack
	 *            the rearTrack to set in m.
	 */
	public void setRearTrack(Double rearTrack) {
		this.rearTrack.set(rearTrack);
	}

	/**
	 *
	 * @return - the rear track in m.
	 */
	public DoubleProperty rearTrackProperty() {
		return this.rearTrack;
	}

	/**
	 * @return the wheelbase in m.
	 */
	public Double getWheelbase() {
		return this.wheelbase.get();
	}

	/**
	 * @param wheelbase
	 *            the wheelbase to set in m.
	 */
	public void setWheelbase(Double wheelbase) {
		this.wheelbase.set(wheelbase);
	}

	/**
	 *
	 * @return - wheelbase in m.
	 */
	public DoubleProperty wheelbaseProperty() {
		return this.wheelbase;
	}

	/**
	 * @return the cogHeight in m.
	 */
	public Double getCogHeight() {
		return this.cogHeight.get();
	}

	/**
	 * @param cogHeight
	 *            the cogHeight to set in m.
	 */
	public void setCogHeight(Double cogHeight) {
		this.cogHeight.set(cogHeight);
	}

	/**
	 *
	 * @return - center of gravity height in m.
	 */
	public DoubleProperty cogHeightProperty() {
		return this.cogHeight;
	}

	/**
	 *
	 * @return - front roll distribution coefficient (0-1).
	 */
	public Double getFrontRollDist() {
		return this.frontRollDist.get();
	}

	/**
	 *
	 * @param frontRollDist
	 *            - front roll distribution coefficient (0-1).
	 */
	public void setFrontRollDist(Double frontRollDist) {
		this.frontRollDist.set(frontRollDist);
	}

	/**
	 *
	 * @return - front roll distribution property.
	 */
	public DoubleProperty frontRollDistProperty() {
		return this.frontRollDist;
	}

	/**
	 * @return the cornerWeightFL in kg.
	 */
	public Double getCornerWeightFL() {
		return this.cornerWeightFL.get();
	}

	/**
	 * @param cornerWeightFL
	 *            the cornerWeightFL to set in kg.
	 */
	public void setCornerWeightFL(Double cornerWeightFL) {
		this.cornerWeightFL.set(cornerWeightFL);
	}

	/**
	 *
	 * @return - corner weight FL in kg.
	 */
	public DoubleProperty cornerWeightFLProperty() {
		return this.cornerWeightFL;
	}

	/**
	 * @return the cornerWeightFR in kg.
	 */
	public Double getCornerWeightFR() {
		return this.cornerWeightFR.get();
	}

	/**
	 * @param cornerWeightFR
	 *            the cornerWeightFR to set in kg.
	 */
	public void setCornerWeightFR(Double cornerWeightFR) {
		this.cornerWeightFR.set(cornerWeightFR);
	}

	/**
	 *
	 * @return - corner weight FR in kg.
	 */
	public DoubleProperty cornerWeightFRProperty() {
		return this.cornerWeightFR;
	}

	/**
	 * @return the cornerWeightRL in kg.
	 */
	public Double getCornerWeightRL() {
		return this.cornerWeightRL.get();
	}

	/**
	 * @param cornerWeightRL
	 *            the cornerWeightRL to set in kg.
	 */
	public void setCornerWeightRL(Double cornerWeightRL) {
		this.cornerWeightRL.set(cornerWeightRL);
	}

	public DoubleProperty cornerWeightRLProperty() {
		return this.cornerWeightRL;
	}

	/**
	 * @return the cornerWeightRR in kg.
	 */
	public Double getCornerWeightRR() {
		return this.cornerWeightRR.get();
	}

	/**
	 * @param cornerWeightRR
	 *            the cornerWeightRR to set in kg.
	 */
	public void setCornerWeightRR(Double cornerWeightRR) {
		this.cornerWeightRR.set(cornerWeightRR);
	}

	public DoubleProperty cornerWeightRRProperty() {
		return this.cornerWeightRR;
	}

	/**
	 * @return the frontAxleTireModel
	 */
	public TireModel getFrontAxleTireModel() {
		return frontAxleTireModel;
	}

	/**
	 * @param frontAxleTireModel
	 *            the frontAxleTireModel to set
	 */
	public void setFrontAxleTireModel(TireModel frontAxleTireModel) {
		this.frontAxleTireModel = frontAxleTireModel;
	}

	/**
	 * @return the rearAxleTireModel
	 */
	public TireModel getRearAxleTireModel() {
		return rearAxleTireModel;
	}

	/**
	 * @param rearAxleTireModel
	 *            the rearAxleTireModel to set
	 */
	public void setRearAxleTireModel(TireModel rearAxleTireModel) {
		this.rearAxleTireModel = rearAxleTireModel;
	}

	@Override
	public String toString() {
		return String.format(
				"Object=\t%s\n" + "Name=\t%s\n" + "Front Track=\t%.2f\n" + "Rear Track=\t%.2f\n" + "Wheelbase=\t%.2f\n"
						+ "CoG Height=\t%.2f\n" + "Front Roll Dist.=\t%.2f\n" + "Corner Weight FL=\t%.2f\n"
						+ "Corner Weight FR=\t%.2f\n" + "Corner Weight RL=\t%.2f\n" + "Corner Weight RR=\t%.2f\n"
						+ "Front Tire Model=\t%s\n" + "Rear Tire Model=\t%s\n",
				this.getClass().toString(), this.getName(), this.getFrontTrack(), this.getRearTrack(),
				this.getWheelbase(), this.getCogHeight(), this.getFrontRollDist(), this.getCornerWeightFL(),
				this.getCornerWeightFR(), this.getCornerWeightRL(), this.getCornerWeightRR(),
				this.getFrontAxleTireModel().toString(), this.getRearAxleTireModel().toString());
	}

	public String getColor() {
		return color;
	}

}
