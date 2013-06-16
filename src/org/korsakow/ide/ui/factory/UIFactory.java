package org.korsakow.ide.ui.factory;



public class UIFactory
{
	private static IUIFactory factory;
	public static void setFactory(IUIFactory factory)
	{
		factory = factory;
	}
	public static IUIFactory getFactory()
	{
		if (factory == null)
			factory = new DefaultUIFactory();
		return factory;
	}
	
}
