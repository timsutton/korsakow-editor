package org.korsakow.ide.ui.resourceexplorer;

import java.util.Collection;
import java.util.EventListener;

import org.korsakow.ide.ui.components.tree.KNode;

public interface ResourceBrowserListener extends EventListener
{
	void onDeleteNodes(Collection<? extends KNode> nodes);
}
