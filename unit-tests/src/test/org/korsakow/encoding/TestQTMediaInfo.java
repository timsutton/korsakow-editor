package test.org.korsakow.encoding;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.korsakow.ide.resources.media.MediaInfo;
import org.korsakow.ide.resources.media.QTMediaInfoFactory;

import quicktime.QTSession;


public class TestQTMediaInfo extends AbstractTestMediaInfo
{
	@Override
	@Before
	public void setUp() throws Exception
	{
		QTSession.open();
	}
	@Override
	@After
	public void tearDown()
	{
		QTSession.close();
	}
	@Override
	protected MediaInfo getMediaInfo(File source) throws IOException, InterruptedException
	{
		return QTMediaInfoFactory.getInfo(source);
	}
}
