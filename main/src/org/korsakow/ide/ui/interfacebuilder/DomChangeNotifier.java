/**
 * 
 */
package org.korsakow.ide.ui.interfacebuilder;

import org.korsakow.ide.util.EventDispatcher;
import org.w3c.dom.Document;

public class DomChangeNotifier extends EventDispatcher<DomChangeListener>
{
	public DomChangeNotifier()
	{
		super(DomChangeListener.class);
	}
	public void notifyDomChanged(Document doc, String changeName, boolean coalescable)
	{
		notify("domDocumentChange", doc, changeName, coalescable);
	}
}