/**
 * 
 */
package org.korsakow.ide.ui.components.linkpool;

import java.util.Collection;
import java.util.Collections;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.interf.IResource;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.mapper.input.SnuInputMapper;
import org.korsakow.ide.Application;
import org.korsakow.ide.controller.ApplicationAdapter;
import org.korsakow.ide.resources.ResourceType;

public class LinkPoolUpdateListener extends ApplicationAdapter implements Runnable
	{
		private final LinkPool pool;
		public LinkPoolUpdateListener(LinkPool pool)
		{
			this.pool = pool;
		}
		public void run()
		{
			doUpdate();
		}
		private void update()
		{
			Application.getInstance().enqueueCommonTask(this);
		}
		private void doUpdate()
		{
			Application app = Application.getInstance();
			Collection<ISnu> snus;
			try {
				snus = SnuInputMapper.findAll();
			} catch (MapperException e) {
				Application.getInstance().showUnhandledErrorDialog(e);
				snus = Collections.EMPTY_LIST;
			}
			
			LinkPoolModel model = new LinkPoolModel();
			model.beginBatchUpdate();
			for (ISnu snu : snus)
			{
				model.add(new SnuHeaderEntry( snu.getId(), snu.getName() ));
			}
			model.endBatchUpdate();
			
			pool.setModel(model);
		}
		@Override
		public void onKeywordsChanged() {
			update();
		}
		@Override
		public void onProjectLoaded(IProject project) {
			update();
		}
		@Override
		public void onResourceAdded(IResource resource) {
			if (ResourceType.forId(resource.getType()) == ResourceType.SNU)
				update();
		}
		@Override
		public void onResourceDeleted(IResource resource) {
			if (ResourceType.forId(resource.getType()) == ResourceType.SNU)
				update();
		}
		@Override
		public void onResourceModified(IResource resource) {
			if (ResourceType.forId(resource.getType()) == ResourceType.SNU)
				update();
		}
		@Override
		public void onResourcesCleared() {
			update();
		}
	}