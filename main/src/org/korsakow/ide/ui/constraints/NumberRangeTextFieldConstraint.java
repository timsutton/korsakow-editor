package org.korsakow.ide.ui.constraints;

import javax.swing.JTextField;

/**
 * Constrains a textfield to numbers in the given range. 
 * @author d
 *
 */
public class NumberRangeTextFieldConstraint extends TextFieldConstraint
{
	private final Class<? extends Number> numberClass;
	private Number minimum = null;
	private Number maximum = null;
	public NumberRangeTextFieldConstraint(Class<? extends Number> numberClass, Number minimum, Number maximum)
	{
		this.numberClass = numberClass;
		this.minimum = minimum;
		this.maximum = maximum;
		parseNumber(numberClass, "0"); // if we can't parse due to illegal number type throw now so the exception is more likely to get spotted
	}
	private Number parseNumber(Class<? extends Number> numberClass, String text) throws NumberFormatException
	{
		if (Double.class.isAssignableFrom(numberClass))
			return Double.parseDouble(text);
		if (Float.class.isAssignableFrom(numberClass))
			return Float.parseFloat(text);
		if (Long.class.isAssignableFrom(numberClass))
			return Long.parseLong(text);
		if (Integer.class.isAssignableFrom(numberClass))
			return Integer.parseInt(text);
		if (Short.class.isAssignableFrom(numberClass))
			return Short.parseShort(text);
		if (Byte.class.isAssignableFrom(numberClass))
			return Byte.parseByte(text);
		throw new IllegalArgumentException("can't parse this type of number: " + numberClass);
	}
	@Override
	protected void validate(JTextField textField) {
		String text = textField.getText();
		Object value;
		try {
			value = parseNumber(numberClass, text);
		} catch (NumberFormatException nfe) {
			value = valueOnFocus.get(textField);
		}
		if (value instanceof Number) {
			Number numberValue = (Number)value;
			if (minimum != null)
				if (numberValue.doubleValue() < minimum.doubleValue())
					value = minimum;
			if (maximum != null)
				if (numberValue.doubleValue() > maximum.doubleValue())
					value = maximum;
		}
		textField.setText(value.toString());
	}
}
