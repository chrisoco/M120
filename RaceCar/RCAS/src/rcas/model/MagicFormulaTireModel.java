package rcas.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Represents a Model for a Race Car Tire based on the Pacejka "Magic Formula".
 * Calculation of the lateral tire forces is based on this formula: <br>
 * <br>
 * Fy = f1(slipAngle) * f2(load)<br>
 * <br>
 * A normalised slip-angle reaction function (Pacejka "Magic Formula") is used
 * which returns a value between 0 and 1: <br>
 * f1 = 1 * sin ( C * atan ( B * x - E * ( B * x - atan( B * x ) ) ) )<br>
 * <br>
 * and a parabolic function for lateral force based on load:<br>
 * f2 = KA * ( 1 - KB * load ) * load<br>
 * <br>
 *
 * @author suy
 *
 */
public class MagicFormulaTireModel implements TireModel {

	private static String DEFAULT_NAME = "Magic Formula Tire Model";
	private static double DEFAULT_COEFF_C = 1.3;
	private static double DEFAULT_COEFF_B = 15.2;
	private static double DEFAULT_COEFF_E = -1.6;
	private static double DEFAULT_COEFF_KA = 2.0;
	private static double DEFAULT_COEFF_KB = 0.000055;

	// name of the tire.
	private SimpleStringProperty name = new SimpleStringProperty(DEFAULT_NAME);

	// coefficient C for the normalised slip-angle curve.
	private SimpleDoubleProperty slipAngleCoefficientC;

	// coefficient B for the normalised slip-angle curve.
	private SimpleDoubleProperty slipAngleCoefficientB;

	// coefficient E for the normalised slip-angle curve.
	private SimpleDoubleProperty slipAngleCoefficientE;

	// coefficient KA for the load function.
	private SimpleDoubleProperty loadCoefficientKA;

	// coefficient KB for the load function.
	private SimpleDoubleProperty loadCoefficientKB;

	/**
	 * Creates a Race Car Tire Model Object with the given curve coefficients
	 * and the default name "Magic Formula Tire Model".<br>
	 * Use these example coefficient values for basic tire to start with:<br>
	 * C = 1.3<br>
	 * B = 15.2<br>
	 * E = -1.6<br>
	 * KA = 2.0<br>
	 * KB = 0.000055
	 *
	 * @param slipAngleCoefficientC
	 *            - coefficient C in the normalised slip angle curve function
	 *            f1.
	 * @param slipAngleCoefficientB
	 *            - coefficient B in the normalised slip angle curve function
	 *            f1.
	 * @param slipAngleCoefficientE
	 *            - coefficient E in the normalised slip angle curve function
	 *            f1.
	 * @param loadCoefficientKA
	 *            - coefficient KA in the load curve function f2.
	 * @param loadCoefficientKB
	 *            - coefficient KB in the load curve function f2.
	 */
	public MagicFormulaTireModel(Double slipAngleCoefficientC, Double slipAngleCoefficientB,
	                             Double slipAngleCoefficientE, Double loadCoefficientKA, Double loadCoefficientKB) {
		this.slipAngleCoefficientC = new SimpleDoubleProperty(slipAngleCoefficientC);
		this.slipAngleCoefficientB = new SimpleDoubleProperty(slipAngleCoefficientB);
		this.slipAngleCoefficientE = new SimpleDoubleProperty(slipAngleCoefficientE);
		this.loadCoefficientKA     = new SimpleDoubleProperty(loadCoefficientKA);
		this.loadCoefficientKB     = new SimpleDoubleProperty(loadCoefficientKB);
	}

	/**
	 * Creates a Race Car Tire Model Object with the following defaults:<br>
	 * <br>
	 * C = 1.3<br>
	 * B = 15.2<br>
	 * E = -1.6<br>
	 * KA = 2.0<br>
	 * KB = 0.000055
	 */
	public MagicFormulaTireModel() {
		this(DEFAULT_COEFF_C, DEFAULT_COEFF_B, DEFAULT_COEFF_E, DEFAULT_COEFF_KA, DEFAULT_COEFF_KB);
	}

	private Double calcSlipAngleFactor(Double slipAngle) {
		Double x = Math.toRadians(slipAngle);
		return Math.sin(this.slipAngleCoefficientC.get()
				* Math.atan(this.slipAngleCoefficientB.get() * x - this.slipAngleCoefficientE.get()
				* (this.slipAngleCoefficientB.get() * x - Math.atan(this.slipAngleCoefficientB.get() * x))));
	}

	private Double calcLoadForce(Double load) {
		return this.loadCoefficientKA.get() * (1 - this.loadCoefficientKB.get() * load) * load;
	}

	/**
	 * Calculates the lateral cornering force for this tire in N.<br>
	 * <br>
	 * <b>CAUTION:</b> this function returns a value in Newton N!
	 *
	 * @param slipAngle
	 *            - the slip angle in degrees (°).
	 * @param load
	 *            - the load in N.
	 * @return - lateral tire force in N.
	 */
	public Double getLateralTireForce(Double slipAngle, Double load) {
		return this.calcSlipAngleFactor(slipAngle) * this.calcLoadForce(load);
	}

	public String getName() {
		return this.name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	@Override
	public String toString() {
		return String.format("%s: \"%s\" (C=%.2f, B=%.2f, E=%.2f, KA=%.2f, KB=%.6f)", this.getClass().toString(),
				this.getName(), slipAngleCoefficientC.get(), slipAngleCoefficientB.get(), slipAngleCoefficientE.get(),
				loadCoefficientKA.get(), loadCoefficientKB.get());
	}

	public double getSlipAngleCoefficientC() {
		return slipAngleCoefficientC.get();
	}

	public double getSlipAngleCoefficientB() {
		return slipAngleCoefficientB.get();
	}

	public double getSlipAngleCoefficientE() {
		return slipAngleCoefficientE.get();
	}

	public double getLoadCoefficientKA() {
		return loadCoefficientKA.get();
	}

	public double getLoadCoefficientKB() {
		return loadCoefficientKB.get() * 10000;
	}

}

