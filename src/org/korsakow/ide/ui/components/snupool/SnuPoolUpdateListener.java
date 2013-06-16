/**
 * 
 */
package org.korsakow.ide.ui.components.snupool;

import java.util.Collection;
import java.util.HashSet;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.interf.IResource;
import org.korsakow.domain.interf.IRule;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.mapper.input.KeywordInputMapper;
import org.korsakow.domain.mapper.input.SnuInputMapper;
import org.korsakow.ide.Application;
import org.korsakow.ide.controller.ApplicationAdapter;
import org.korsakow.ide.resources.ResourceType;
import org.korsakow.ide.rules.RuleType;

public class SnuPoolUpdateListener extends ApplicationAdapter implements Runnable
{
	private final SnuPool pool;
	public SnuPoolUpdateListener(SnuPool pool)
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
		SnuPoolModel model;
		try {
			model = createModel();
		} catch (MapperException e) {
			Application.getInstance().showUnhandledErrorDialog(e);
			model = new SnuPoolModel();
		}
		
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
	public static SnuPoolModel createModel() throws MapperException
	{
		SnuPoolModel model = new SnuPoolModel();

		Collection<ISnu> snus = SnuInputMapper.findAll();
		
		model.beginBatchUpdate();
		for (ISnu snu : snus)
		{
			Collection<String> inKeywords = new HashSet<String>();
			for (IKeyword k : KeywordInputMapper.findByObject(snu.getId()))
				inKeywords.add(k.getValue());
			
			Collection<String> outKeywords = new HashSet<String>();
			for (IRule searchRule : snu.getRules()) {
				if (RuleType.forId(searchRule.getRuleType()) != RuleType.Search)
					continue;
				
				for (IRule rule : searchRule.getRules()) {
					switch(RuleType.forId(rule.getRuleType())) {
					case KeywordLookup:
						for (IKeyword k : KeywordInputMapper.findByObjectRecursive(rule.getId()))
							outKeywords.add(k.getValue());
						break;
					}
				}
			}
			model.add(snu.getId(), snu.getName(), inKeywords, outKeywords);
		}
		model.endBatchUpdate();
		
		return model;
	}
}
