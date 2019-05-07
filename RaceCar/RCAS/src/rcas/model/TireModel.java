package rcas.model;

/**
 * This interface defines the methods to be supported in a class representing
 * and implementing a race car tire model.<br>
 * <br>
 *
 * @author suy
 *
 */

public interface TireModel {

	/**
	 * Calculates the cornering (lateral) force of a tire based on it's slip
	 * angle and vertical load.<br>
	 * <br>
	 *
	 * @param slipAngle
	 *            - slip angle of the tire in degrees (°).
	 * @param load
	 *            - load on the tire in N.
	 * @return - lateral tire force in N.
	 */
	public Double getLateralTireForce(Double slipAngle, Double load);

	/*
	 * TO DO: In future a method with the declaration
	 * "getLongitudinalTireForce(Double slip, Double load)" for longitudinal
	 * tire forces should be implemented.
	 */

}
