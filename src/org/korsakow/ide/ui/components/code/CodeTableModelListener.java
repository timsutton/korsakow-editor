package org.korsakow.ide.ui.components.code;

import javax.swing.event.TableModelListener;

public interface CodeTableModelListener extends TableModelListener
{
    public void tableSorted(TableModelSortEvent e);
}
