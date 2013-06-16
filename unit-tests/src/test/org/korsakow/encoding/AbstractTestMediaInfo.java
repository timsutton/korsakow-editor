package test.org.korsakow.encoding;

import java.io.File;

import org.junit.Assert;

import org.junit.Test;
import org.korsakow.ide.resources.media.MediaInfo;
import org.korsakow.ide.util.ExternalsResourceManager;
import org.korsakow.ide.util.FileUtil;

import test.util.BaseTestCase;



public abstract class AbstractTestMediaInfo extends BaseTestCase
{
	private static final int TOLERANCE = 1000;
	private File parentDir;
	private File dest;

	@Override
	public void setUp() throws Exception
	{
		parentDir = FileUtil.createTempDirectory("test", "");
		parentDir.deleteOnExit();
		dest = File.createTempFile("test", ".flv", parentDir);
		dest.deleteOnExit();
	}
	@Override
	public void tearDown()
	{
		parentDir = null;
		dest = null;
	}
	protected abstract MediaInfo getMediaInfo(File source) throws Exception;
	
	private void pre(File src, File dest) throws Exception
	{
		Assert.assertTrue("Can read source file", src.canRead());
	}
	private void test(String source, MediaInfo expected) throws Exception
	{
		File src = new File("resources/" + source);
		pre(src, dest);
		MediaInfo info = getMediaInfo(src);
		Assert.assertTrue("Math.abs(" + expected.duration + " - " + info.duration + " ) < " + TOLERANCE, Math.abs(expected.duration - info.duration) < TOLERANCE);
//		Assert.assertEquals(expected.codec, info.codec);
		Assert.assertEquals(expected.width, info.width);
		Assert.assertEquals(expected.height, info.height);
	}
	/**
	 * This test has always passed.
	 */
	@Test public void testDIVX_MP2() throws Exception
	{
		MediaInfo expected = new MediaInfo();
		expected.duration = 7541;
//		expected.codec = "msmpeg4";
		expected.width = 640;
		expected.height = 480;
		test("v_divx_a_mp2.mov", expected);
	}
	@Test public void testFLV_H263_MP3() throws Exception
	{
		MediaInfo expected = new MediaInfo();
		expected.duration = 11075;
//		expected.codec = "flv";
		expected.width = 320;
		expected.height = 176;
		test("v_flv_h263_a_mp3.flv", expected);
	}
	@Test public void testM4V_H264_AAC() throws Exception
	{
		MediaInfo expected = new MediaInfo();
		expected.duration = 5573;
//		expected.codec = "h264";
		expected.width = 640;
		expected.height = 480;
		test("v_h264_a_aac.m4v", expected);
	}
	@Test public void testMJPEG_PCM8() throws Exception
	{
		MediaInfo expected = new MediaInfo();
		expected.duration = 5411;
//		expected.codec = "Motion JPEG OpenDML";
		expected.width = 640;
		expected.height = 480;
		test("v_mjpeg_a_pcm8.mov", expected);
	}
	@Test public void testH264_ADPCM_IMA_QT_48000HZ() throws Exception
	{
		MediaInfo expected = new MediaInfo();
		expected.duration = 46880;
//		expected.codec = "h264";
		expected.width = 1024;
		expected.height = 576;
		test("v_h264_a_adpcm_ima_qt_48000.mov", expected);
	}
	@Test public void testKorsakow3DefaultMovie1() throws Exception
	{
		MediaInfo expected = new MediaInfo();
		expected.duration = 6863;
//		expected.codec = "h264";
		expected.width = 720;
		expected.height = 405;
		test("man01.mov", expected);
	}
	@Test public void testKorsakow3DefaultMovie2() throws Exception
	{
		MediaInfo expected = new MediaInfo();
		expected.duration = 6850;
//		expected.codec = "h264";
		expected.width = 720;
		expected.height = 405;
		test("couple_naked.mov", expected);
	}
	@Test public void testForgottenFlagsMovie1() throws Exception
	{
		MediaInfo expected = new MediaInfo();
		expected.duration = 6000;
//		expected.codec = "h264";
		expected.width = 384;
		expected.height = 192;
		test("TV-sandra_p.mov", expected);
	}

	
}
