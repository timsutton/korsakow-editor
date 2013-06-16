package test.org.korsakow.ui;

import org.junit.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.korsakow.ide.XPathHelper;
import org.korsakow.ide.ui.components.NewMediaPanel;
import org.korsakow.ide.util.DomUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import test.util.BaseTestCase;

/**
 * @author d
 *
 */
public class TestUI extends BaseTestCase
{
	@Test
	public void testNewMediaPanelTimeFormat() {
		Assert.assertEquals((Long)(9123*1000*60 + 45*1000 + 678L), NewMediaPanel.parseTime("9123:45.678"));
		Assert.assertEquals((Long)(123*1000*60 + 45*1000 + 678L), NewMediaPanel.parseTime("123:45.678"));
		Assert.assertEquals((Long)(23*1000*60 + 45*1000 + 678L), NewMediaPanel.parseTime("23:45.678"));
		Assert.assertEquals((Long)(3*1000*60 + 45*1000 + 678L), NewMediaPanel.parseTime("3:45.678"));
		Assert.assertEquals((Long)(3*1000*60 + 5*1000 + 678L), NewMediaPanel.parseTime("3:5.678"));
		Assert.assertEquals((Long)(3*1000*60 + 5*1000 + 78L), NewMediaPanel.parseTime("3:5.78"));
		Assert.assertEquals((Long)(3*1000*60 + 5*1000 + 8L), NewMediaPanel.parseTime("3:5.8"));
		Assert.assertEquals(null, NewMediaPanel.parseTime("1:345:8"));
		Assert.assertEquals(null, NewMediaPanel.parseTime("1:2:3678"));
	}
}
