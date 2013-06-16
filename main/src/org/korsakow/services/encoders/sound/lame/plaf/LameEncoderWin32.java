package org.korsakow.services.encoders.sound.lame.plaf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.korsakow.ide.util.ExternalsResourceManager;
import org.korsakow.ide.util.Util;
import org.korsakow.services.encoders.sound.SoundEncoder;
import org.korsakow.services.encoders.sound.SoundFormat;
import org.korsakow.services.encoders.sound.lame.LameEncoder;

public class LameEncoderWin32 extends LameEncoder
{
	public static class LameEncoderWin32Description extends LameEncoder.LameEncoderDescription
	{
		public Class<? extends SoundEncoder> getEncoderClass() {
			return LameEncoderWin32.class;
		}
	}
	@Override
	protected Process createProcess(SoundFormat destFormat, File src, File dst) throws IOException
	{
		List<String> cmds = new ArrayList<String>();
		cmds.add(ExternalsResourceManager.getExternalFile(ExternalsResourceManager.LAME_WIN).getAbsolutePath());
		cmds.add("--nohist");
		cmds.add("--disptime");
		cmds.add("60");
		cmds.add("--brief");
		cmds.add(src.getAbsolutePath());
		cmds.add(dst.getAbsolutePath());
		Logger.getLogger(getClass()).info(Util.join(cmds));
		return Runtime.getRuntime().exec(cmds.toArray(new String[cmds.size()]));
	}
}
