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

public class FFMpegEncoderWin32 extends FFMpegEncoder
{
	public static class FFMpegEncoderWin32Description extends FFMpegEncoder.FFMpegEncoderDescription
	{
		public Class<? extends VideoEncoder> getEncoderClass() {
			return FFMpegEncoderWin32.class;
		}
	}
	@Override
	protected Process createProcess(VideoCodec sourceFormat, File srcFile, File dstFile) throws IOException
	{
		ExternalsResourceManager.getExternalFile(ExternalsResourceManager.FFMPEG_WIN);
		List<String> cmds = createCommandLine(sourceFormat, srcFile, dstFile);
		cmds.add(0, ExternalsResourceManager.getExternalFile(ExternalsResourceManager.FFMPEG_WIN).getAbsolutePath());
		Logger.getLogger(getClass()).info(Util.join(cmds));
		return Runtime.getRuntime().exec(cmds.toArray(new String[cmds.size()]));
	}
	@Override
	protected String getNullDevice()
	{
		return "NUL";
	}
}
