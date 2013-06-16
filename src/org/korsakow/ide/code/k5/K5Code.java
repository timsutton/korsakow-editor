/**
 * 
 */
package org.korsakow.ide.code.k5;

public class K5Code
{
	private String rawCode;
	private boolean isValid;
	public K5Code(String rawCode)
	{
		this.rawCode = rawCode;
	}
	public void setRawCode(String code)
	{
		this.rawCode = code;
	}
	public String getRawCode()
	{
		return rawCode;
	}
	public void setValid(boolean valid)
	{
		this.isValid = valid;
	}
	public boolean isValid()
	{
		return isValid;
	}
}