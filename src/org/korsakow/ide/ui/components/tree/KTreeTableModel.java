package org.korsakow.ide.ui.components.tree;

import java.util.List;

import com.sun.swingx.treetable.TreeTableModel;
import com.sun.swingx.treetable.TreeTableNode;

public interface KTreeTableModel extends TreeTableModel
{
	KNode getRoot();
    void setRoot(TreeTableNode root);
    void setColumnIdentifiers(List<?> columnIdentifiers);
    List<?> getColumnIdentifiers();
    void addColumn(Object identifier);
	void prependNode(KNode newChild, KNode parent);
	/**
	 * @return the insertion index
	 */
	int appendNode(KNode newChild, KNode parent);
	void insertNodeInto(KNode newChild, KNode parent, int index);
    void removeNodeFromParent(KNode node);
    KNode getNodeById(long id);
    long getIdByNode(KNode node);
    void clear();
    void beginBatchUpdate();
    void endBatchUpdate();
    boolean isValidTreeTableNode(Object node);
}
