package org.korsakow.eawt;

import java.util.EventListener;

public interface IApplicationListener extends EventListener
{
	void handleAbout(ApplicationEvent event);
//	void handleOpenApplication(ApplicationEvent event);
	void handleOpenFile(ApplicationEvent event);
//	void handlePreferences(ApplicationEvent event);
//	void handlePrintFile(ApplicationEvent event);
	void handleQuit(ApplicationEvent event);
//	void handleReOpenApplication(ApplicationEvent event);
}
