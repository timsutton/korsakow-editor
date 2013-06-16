package org.korsakow.domain.mapper.exception;

import org.dsrg.soenea.domain.MapperException;

public class LostUpdateException extends MapperException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4972448192048874989L;
	

	public LostUpdateException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
	public LostUpdateException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public LostUpdateException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
