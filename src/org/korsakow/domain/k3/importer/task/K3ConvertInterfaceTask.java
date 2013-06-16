/**
 * 
 */
package org.korsakow.domain.k3.importer.task;

import java.util.ArrayList;
import java.util.Collection;

import org.korsakow.domain.InterfaceFactory;
import org.korsakow.domain.WidgetFactory;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.domain.interf.IWidget;
import org.korsakow.domain.k3.K3Interface;
import org.korsakow.domain.k3.K3Widget;
import org.korsakow.domain.k3.importer.K3ImportException;
import org.korsakow.domain.k3.importer.K3ImportReport;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.resources.WidgetType;
import org.korsakow.ide.task.AbstractTask;
import org.korsakow.ide.task.TaskException;
import org.korsakow.ide.util.StrongReference;

public class K3ConvertInterfaceTask extends AbstractTask
{
	private final K3ImportReport report;
	private final StrongReference<K3Interface> k3Interface;
	private final StrongReference<IInterface> k5InterfaceRef;
	
	public K3ConvertInterfaceTask(StrongReference<K3Interface> k3Interface, K3ImportReport report, StrongReference<IInterface> k5Interface)
	{
		this.k3Interface = k3Interface;
		this.report = report;
		k5InterfaceRef = k5Interface;
	}
	@Override
	public String getTitleString()
	{
		return LanguageBundle.getString("import.task.convertinterface");
	}
	@Override
	public void runTask() throws TaskException
	{
		if (k3Interface.get() != null)
			try {
				importInterface(k3Interface.get());
			} catch (K3ImportException e) {
				throw new TaskException(e);
			}
	}
	private void importInterface(K3Interface k3Interface) throws K3ImportException
	{
		IInterface k5Interface = InterfaceFactory.createNew();
		k5Interface.setName("Interface");
		k5Interface.setGridWidth(20);
		k5Interface.setGridHeight(20);
		
		Collection<IWidget> k5Widgets = new ArrayList<IWidget>();
		int autoLinkCounter = 0;
		for (K3Widget k3Widget : k3Interface.widgets)
		{
			IWidget k5Widget = null;
			if (K3Widget.MAIN.equals(k3Widget.type)) {
				k5Widget = WidgetFactory.createNew(WidgetType.MainMedia.getId());
			} else
			if (K3Widget.LOADING.equals(k3Widget.type)) {
				report.addUnsupported("Video loading bar Widget", "Widget");
			} else
			if (K3Widget.PREVIEW.equals(k3Widget.type)) {
				k5Widget = WidgetFactory.createNew(WidgetType.SnuAutoLink.getId());
				k5Widget.setDynamicProperty("index", autoLinkCounter++);
			} else
			if (K3Widget.SUBTITLE.equals(k3Widget.type)) {
				k5Widget = WidgetFactory.createNew(WidgetType.Subtitles.getId());
			} else
			if (K3Widget.INSERTTEXT.equals(k3Widget.type)) {
				k5Widget = WidgetFactory.createNew(WidgetType.InsertText.getId());
			} else
				report.addUnsupported(k3Widget.type, "Widget");
			
			if (k5Widget != null) {
				k5Widgets.add(k5Widget);
				k5Widget.setX(k3Widget.left);
				k5Widget.setY(k3Widget.top);
				k5Widget.setWidth(k3Widget.right - k3Widget.left);
				k5Widget.setHeight(k3Widget.bottom - k3Widget.top);
			}
		}
		
		k5Interface.setWidgets(k5Widgets);
		
		k5InterfaceRef.set(k5Interface);
	}
}