package org.korsakow.eawt;

public interface IApplication
{
	void addApplicationListener(IApplicationListener listener);
	void removeApplicationListener(IApplicationListener listener);
}
