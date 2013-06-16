package test.org.korsakow.encoding;

import java.io.File;
import java.io.IOException;

import org.korsakow.ide.resources.media.FFMpegMediaInfoFactory;
import org.korsakow.ide.resources.media.MediaInfo;


public class TestFFMpegMediaInfo extends AbstractTestMediaInfo
{
	@Override
	protected MediaInfo getMediaInfo(File source) throws IOException, InterruptedException
	{
		return FFMpegMediaInfoFactory.getInfo(source);
	}
}
