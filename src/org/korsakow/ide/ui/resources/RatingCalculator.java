package org.korsakow.ide.ui.resources;

/**
 * The rating UI is supposed to be in the range [0.1,10] but with 1 represented at the 50% mark.
 * This helper calculates the transformation from a number in the range [0,1] -> [0.1,10] and back,
 * where
 * 	0.0 -> 0.1
 *  0.5 -> 1.0
 *  1.0 -> 10.0
 * 
 * @author d
 *
 */
public class RatingCalculator
{
	/**
	 * @param x [0,1]
	 * @return [0.1,10]
	 */
	public static float calculate(float x)
	{
		if (x < 0.5)
			return 2f*x+0.1f;
		else
			return 18f*(x-0.5f)+1f;
	}
	/**
	 * @param x [0.1,10]
	 * @return [0,1]
	 */
	public static float inverse(float x)
	{
		if (x < 1)
			return (x-0.1f)/2f;
		else
			return ((x-1f)/18f)+0.5f;
	}
}