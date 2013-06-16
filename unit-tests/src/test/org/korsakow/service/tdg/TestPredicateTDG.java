package test.org.korsakow.service.tdg;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.korsakow.domain.interf.ISnu.BackgroundSoundMode;
import org.korsakow.ide.DataRegistry;
import org.korsakow.services.tdg.PredicateTDG;
import org.korsakow.services.tdg.SnuTDG;
import org.w3c.dom.Document;

import test.util.StopWatch;


public class TestPredicateTDG {
	@Before
	public void setUp() throws Exception
	{
		File file = File.createTempFile("korsakow", "test");
		file.deleteOnExit();
		Document document = DataRegistry.createDefaultEmptyDocument();
		DataRegistry.initialize(document, file);
	}
	@Ignore @Test public void timeInsert() throws Exception
	{
		long snuId = DataRegistry.getMaxId();
		SnuTDG.insert(snuId, 0L, "name", null, 0, null, BackgroundSoundMode.KEEP.getId(), 0, false, null, null, false, null, false, false, null, "previewText", "insertText");
		int count = 2000;
		List<Long> ids = new ArrayList<Long>();
		for (int i = 0; i < count; ++i)
			ids.add(DataRegistry.getMaxId());
		StopWatch sw = StopWatch.Start();
		for (Long id : ids)
			PredicateTDG.insert(snuId, id, 0, "");
		sw.print("testInsert");
	}
}
