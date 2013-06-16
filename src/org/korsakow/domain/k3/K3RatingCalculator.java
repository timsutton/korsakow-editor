package org.korsakow.domain.k3;

/**
 * The rating UI is supposed to be in the range [0,100] but with 1 represented at the 50% mark.
 * This helper calculates the transformation from a number in the range [0,100] -> [0.1,10] and back,
 * where
 * 	0.0 -> 0.1
 *  50  -> 1.0
 *  100 -> 10.0
 * 
 * Note: Any similarity between this class and org.korsakow.ide.ui.resources.RatingCalculator is a coincidence. We maintain
 * 	the two classes as distinct since they could concievably diverge.
 * 
 * @author d
 *
 */
public class K3RatingCalculator
{
	/**
	 * @param x [0,100]
	 * @return [0.1,10]
	 */
	public static double calculate(double x)
	{
		x /= 100;
		
		if (x < 0.5)
			return 2*x+0.1;
		else
			return 18*(x-0.5)+1;
	}
	/**
	 * @param x [0.1,10]
	 * @return [0,100]
	 */
	public static double inverse(double x)
	{
		if (x < 1)
			return (x-0.1)/2 * 100;
		else
			return (((x-1)/18)+0.5) * 100;
	}
}