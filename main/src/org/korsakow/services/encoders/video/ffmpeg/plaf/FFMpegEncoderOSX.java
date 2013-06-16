package org.korsakow.services.encoders.video.ffmpeg.plaf;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.korsakow.ide.util.ExternalsResourceManager;
import org.korsakow.ide.util.Util;
import org.korsakow.services.encoders.video.VideoCodec;
import org.korsakow.services.encoders.video.VideoEncoder;
import org.korsakow.services.encoders.video.ffmpeg.FFMpegEncoder;

public class FFMpegEncoderOSX extends FFMpegEncoder
{
	public static class FFMpegEncoderOSXDescription extends FFMpegEncoder.FFMpegEncoderDescription
	{
		public Class<? extends VideoEncoder> getEncoderClass() {
			return FFMpegEncoderOSX.class;
		}
	}
	@Override
	protected Process createProcess(VideoCodec sourceFormat, File srcFile, File dstFile) throws IOException
	{
		File file = ExternalsResourceManager.getExternalFile(ExternalsResourceManager.FFMPEG_OSX);
		List<String> cmds = createCommandLine(sourceFormat, srcFile, dstFile);
		cmds.add(0, file.getAbsolutePath());
		Logger.getLogger(getClass()).info(Util.join(cmds));
		return Runtime.getRuntime().exec(cmds.toArray(new String[cmds.size()]));
	}
	@Override
	protected String getNullDevice()
	{
		return "/dev/null";
	}
}
