package test.org.korsakow.encoding;

import java.io.File;
import java.util.Properties;

import org.junit.Assert;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.korsakow.ide.Main;
import org.korsakow.ide.resources.media.FFMpegMediaInfoFactory;
import org.korsakow.ide.resources.media.MediaInfo;
import org.korsakow.ide.resources.media.QTMediaInfoFactory;
import org.korsakow.ide.util.ExternalsResourceManager;
import org.korsakow.ide.util.FileUtil;
import org.korsakow.ide.util.Platform;
import org.korsakow.ide.util.ResourceManager;
import org.korsakow.services.encoders.video.ffmpeg.FFMpegEncoder;
import org.korsakow.services.encoders.video.ffmpeg.plaf.FFMpegEncoderOSX;
import org.korsakow.services.encoders.video.ffmpeg.plaf.FFMpegEncoderWin32;
import org.korsakow.services.export.IVideoEncodingProfile;
import org.korsakow.services.export.PropertiesVideoEncodingProfile;
import org.korsakow.services.export.task.VideoExportTask;

import quicktime.QTSession;
import test.Warning;
import test.util.BaseTestCase;


public class TestVideoExport extends BaseTestCase
{
	/**
	 * Tolerance History.
	 * 
	 * 1001: testDIVX_MP2 has a delta of 1000
	 * 1002: testDIVX_MP2 has a delta of 1001 (slightly different encoding options)
	 * 
	 */
	private static final long SIZE_TOLERANCE = 1002;
	private File parentDir;
	private File dest;
	private MediaInfo beforeInfoQT;
	private MediaInfo beforeInfoFFMpeg;

	public TestVideoExport()
	{
		Main.setupPlatformEncoders();
	}
	private FFMpegEncoder getPlatformEncoder(File parentDir) throws Exception
	{
		switch (Platform.getOS())
		{
		case MAC:
			return new FFMpegEncoderOSX();
		case WIN:
			return new FFMpegEncoderWin32();
		case NIX:
		default:
			Assert.fail("No Encoder for platform: " + Platform.getOS().getCanonicalName());
			break;
		}
		throw new AssertionError();
	}
	
	@Override
	@Before
	public void setUp() throws Exception
	{
		QTSession.open();
		
		parentDir = FileUtil.createTempDirectory("test", "");
		parentDir.deleteOnExit();
	}
	@Override
	@After
	public void tearDown()
	{
		parentDir = null;
		dest = null;
		beforeInfoQT = null;
		beforeInfoFFMpeg = null;
		
		QTSession.close();
	}
	private void doPre(File src, File dest) throws Exception
	{
//		dest.deleteOnExit();
		Assert.assertTrue("Can read source file", src.canRead());
		Assert.assertTrue("Can write destination file", dest.canWrite());
		try {
			beforeInfoQT = QTMediaInfoFactory.getInfo(src);
		} catch (Throwable t) {
			beforeInfoQT = null;
		}
		try {
			beforeInfoFFMpeg = FFMpegMediaInfoFactory.getInfo(src);
		} catch (Throwable t) {
			beforeInfoFFMpeg = null;
		}
	}
	private void doPost(File dest) throws Exception
	{
		Assert.assertNotSame(dest.length(), 0);
		MediaInfo afterInfoQT = null;
		try {
			afterInfoQT = QTMediaInfoFactory.getInfo(dest);
		} catch (Throwable t) {
		}
		MediaInfo afterInfoFFMPeg = null;
		try {
			afterInfoFFMPeg = FFMpegMediaInfoFactory.getInfo(dest);
		} catch (Throwable t) {
			
		}
//		FileUtil.copyFile(dest, new File("/Users/d/Desktop/test.mov"));
		if (beforeInfoFFMpeg != null && afterInfoFFMPeg != null)
			Warning.warnTrue(String.format("FFMPEG: Math.abs(%d - %d) = %d < %d", beforeInfoFFMpeg.duration, afterInfoFFMPeg.duration, Math.abs(beforeInfoFFMpeg.duration - afterInfoFFMPeg.duration), SIZE_TOLERANCE), Math.abs(beforeInfoFFMpeg.duration - afterInfoFFMPeg.duration) < SIZE_TOLERANCE);
		else
			Warning.warnTrue("No FFMpegInfo", false);
		if (beforeInfoQT != null && afterInfoQT != null)
			Warning.warnTrue(String.format("QT: Math.abs(%d - %d) = %d < %d", beforeInfoQT.duration, afterInfoQT.duration, Math.abs(beforeInfoQT.duration - afterInfoQT.duration), SIZE_TOLERANCE), Math.abs(beforeInfoQT.duration - afterInfoQT.duration) < SIZE_TOLERANCE);
		else
			Warning.warnTrue("No QTInfo", false);
		
//		ShellExec.revealInPlatformFilesystemBrowser(dest.getAbsolutePath());
	}
	private void test(String source) throws Exception
	{
		doTestFLVLow(source);
		doTestFLVMed(source);
		doTestFLVHigh(source);
//		doTestFLVMax(source);
		doTestX264Low(source);
		doTestX264Med(source);
		doTestX264High(source);
//		doTestX264Max(source);
//		doTestX264Lossless(source);
	}
	private void doTestFLVLow(String source) throws Exception
	{
		doTestCommon(source, "flv_low");
	}
	private void doTestFLVMed(String source) throws Exception
	{
		doTestCommon(source, "flv_med");
	}
	private void doTestFLVHigh(String source) throws Exception
	{
		doTestCommon(source, "flv_high");
	}
	private void doTestX264Low(String source) throws Exception
	{
		doTestCommon(source, "x264_low");
	}
	private void doTestX264Med(String source) throws Exception
	{
		doTestCommon(source, "x264_med");
	}
	private void doTestX264High(String source) throws Exception
	{
		doTestCommon(source, "x264_high");
	}
	private void doTestCommon(String source, String profileFile) throws Exception
	{
		Properties props = new Properties();
		props.load(ResourceManager.getResourceStream("encodingprofiles/" + profileFile + ".properties"));
		IVideoEncodingProfile profile = new PropertiesVideoEncodingProfile(props);
		System.out.println(String.format("\tProfile: %s", profile.getName()));
		
		dest = File.createTempFile(profile.getName(), ".flv", parentDir);
		File src = new File("resources/" + source);
		doPre(src, dest);
		
		long timeBefore = System.currentTimeMillis();
		VideoExportTask.encodeVideo(src, dest, profile, 320, 240);
		long timeAfter = System.currentTimeMillis();
		
		System.out.println(String.format("Took %f seconds", (timeAfter-timeBefore)/1000.0));
		
		doPost(dest);
	}
	@Test public void testCircleOfLife_Boy() throws Exception
	{
		test("CircleOfLife/boy.mov");
	}
	@Test public void testCircleOfLife_Girl() throws Exception
	{
		test("CircleOfLife/girl.mov");
	}
	@Test public void testCircleOfLife_Couple() throws Exception
	{
		test("CircleOfLife/couple.mov");
	}
	@Test public void testCircleOfLife_CoupleNaked() throws Exception
	{
		test("CircleOfLife/couple_naked.mov");
	}
	@Test public void testCircleOfLife_Family() throws Exception
	{
		test("CircleOfLife/family.mov");
	}
	@Test public void testCircleOfLife_Man01() throws Exception
	{
		test("CircleOfLife/man01.mov");
	}
	@Test public void testCircleOfLife_Man02() throws Exception
	{
		test("CircleOfLife/man02.mov");
	}
	@Test public void testCircleOfLife_Man03() throws Exception
	{
		test("CircleOfLife/man03.mov");
	}
	@Test public void testCircleOfLife_Man04() throws Exception
	{
		test("CircleOfLife/man04.mov");
	}
	@Test public void testCircleOfLife_Woman01() throws Exception
	{
		test("CircleOfLife/woman01.mov");
	}
	@Test public void testCircleOfLife_Woman02() throws Exception
	{
		test("CircleOfLife/woman02.mov");
	}
	@Test public void testCircleOfLife_Woman03() throws Exception
	{
		test("CircleOfLife/woman03.mov");
	}
	@Test public void testCircleOfLife_Woman04() throws Exception
	{
		test("CircleOfLife/woman04.mov");
	}
	/**
	 * This test highlights the fact that we only capture one audio stream.
	 */
	@Test public void testGUT() throws Exception
	{
		test("gut.mov");
	}
	/**
	 * This test has always passed.
	 */
	@Test public void testDIVX_MP2() throws Exception
	{
		test("v_divx_a_mp2.mov");
	}
	/**
	 * This test has always passed.
	 */
	@Test public void testFLV_H263_MP3() throws Exception
	{
		test("v_flv_h263_a_mp3.flv");
	}
	/**
	 * This test has always passed.
	 */
	@Test public void testM4V_H264_AAC() throws Exception
	{
		test("v_h264_a_aac.m4v");
	}
	/**
	 * This test caught the fact that ffmpeg can't convert audio directly from pcmu8 to mp3
	 * 
	 * TODO: sthiel discovered a 2-pass workaround that involved processing only the audio first into pcm16
	 * 
	 * Has always and currently fails.
	 */
	@Test public void testMJPEG_PCM8() throws Exception
	{
		test("v_mjpeg_a_pcm8.mov");
	}
	/**
	 * This test caught the fact that FLV requires one of "44100, 22050, 11025" as its audio sampling rate.
	 */
	@Test public void testH264_ADPCM_IMA_QT_48000HZ() throws Exception
	{
		test("v_h264_a_adpcm_ima_qt_48000.mov");
	}
	/**
	 * This movie came with the download of Korsakow V3. It initially failed to encode on OSX but worked on WIN32
	 * due to the MJPEG encoding not being properly supported.
	 * A rebuild of FFMPEG for OSX (from unmodified source) fixed the issue (original was from FFMPEGX).
	 * 
	 * 2009-09-30: Now using x264 we have to make sure the width and height are multiples of 2 for this to work (padding didn't work, had to set the actual size)
	 */
	@Test public void testKorsakow3DefaultMovie1() throws Exception
	{
		test("man01.mov");
	}
	/**
	 * This movie came with the download of Korsakow V3.
	 * Has always failed.
	 * 
org.korsakow.ide.encoders.EncoderException: unknown error: FFmpeg version SVN-r195, Copyright (c) 2000-2009 Fabrice Bellard, et al.
  configuration: --enable-gpl --enable-postproc --enable-swscale --disable-ffplay --disable-ffserver
  libavutil     49.14. 0 / 49.14. 0
  libavcodec    52.11. 0 / 52.11. 0
  libavformat   52.25. 0 / 52.25. 0
  libavdevice   52. 1. 0 / 52. 1. 0
  libswscale     0. 6. 1 /  0. 6. 1
  libpostproc   51. 2. 0 / 51. 2. 0
  built on Mar 27 2009 09:21:34, gcc: 4.0.1 (Apple Inc. build 5490)
[mov,mp4,m4a,3gp,3g2,mj2 @ 0x1002600]edit list not starting at 0, a/v desync might occur, patch welcome
Input #0, mov,mp4,m4a,3gp,3g2,mj2, from '/Users/d/work/korsakow/workspace/ide/test/resources/couple_naked.mov':
  Duration: 00:00:06.91, start: 0.000000, bitrate: 96 kb/s
    Stream #0.0(eng): Audio: aac, 22050 Hz, mono, s16
    Stream #0.1(eng): Video: mjpeg, 720x405, 15.00 tb(r)
    Stream #0.2(eng): Video: mjpeg, yuvj444p, 720x405 [PAR 72:72 DAR 16:9], 15.00 tb(r)
swScaler: Unknown format is not supported as input pixel format
Cannot get resampling context
	 */
	@Test public void testKorsakow3DefaultMovie2() throws Exception
	{
		test("couple_naked.mov");
	}
	/**
	 * 3GP cell phone video. Has never worked.
	 * 
	 * FFMPEG doesn't seem to like the 'samr' audio codec.
	 * 
	 * @throws Exception
	 */
	@Test public void test3GP() throws Exception
	{
		test("MOV00006.3gp");
	}
	/**
	 * FFMpeg does not currently support 	 (Has never worked)
	 * http://www.mail-archive.com/libav-user@mplayerhq.hu/msg00201.html
	 * @throws Exception
	 */
	@Test public void testAppleProRes422() throws Exception
	{
		test("Harkins_FinalColor_ProResHQ2.blank.mov");
	}
	/**
	 * Illustrates that some builds of FFMpeg (notable the windows build) can't handle Data: TMCD streams.
	 * 
	 * This is from Forgotten Flags, one of the well known k3 films.
	 * @throws Exception
	 */
	@Test public void testForgottenFlagsMovie1() throws Exception
	{
		test("TV-sandra_p.mov");
	}
	/**
	 * This is only interesting in that it is from Almost Architecture, one of the well known k3 films.
	 * @throws Exception
	 */
	@Test public void testAlmostArchitechtureMovie1() throws Exception
	{
		test("opening.mov");
	}
	
}
