/**
 * 
 */
package org.korsakow.ide.ui.interfacebuilder;

import java.util.EventListener;

import org.w3c.dom.Document;

public interface DomChangeListener extends EventListener
{
	/**
	 * Unspecified changes to the document
	 * @param doc
	 * @param an identifier for the change. not meant to be relied on in terms of specific values, only compared against previous such values.
	 */
	void domDocumentChange(Document doc, String changeName, boolean coalescable);
}