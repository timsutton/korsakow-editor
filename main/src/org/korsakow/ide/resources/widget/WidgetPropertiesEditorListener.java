package org.korsakow.ide.resources.widget;

import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;

public interface WidgetPropertiesEditorListener extends CellEditorListener
{
    void propertyEditingStopped(ChangeEvent e);
    void propertyEditingCanceled(ChangeEvent e);
}
