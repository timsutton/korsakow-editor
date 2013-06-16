/**
 * 
 */
package org.korsakow.domain.k3.code;

public class K3Code
{
	private String rawCode;
	private boolean isValid;
	public K3Code(String rawCode)
	{
		this.rawCode = rawCode;
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