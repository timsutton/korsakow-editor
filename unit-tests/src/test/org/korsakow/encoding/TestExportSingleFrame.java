package test.org.korsakow.encoding;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.korsakow.ide.Main;
import org.korsakow.ide.util.FileUtil;
import org.korsakow.ide.util.Platform;
import org.korsakow.services.encoders.EncoderException;
import org.korsakow.services.encoders.video.AudioCodec;
import org.korsakow.services.encoders.video.ContainerFormat;
import org.korsakow.services.encoders.video.VideoCodec;
import org.korsakow.services.encoders.video.VideoEncoder;
import org.korsakow.services.encoders.video.VideoEncoderFactory;
import org.korsakow.services.encoders.video.ffmpeg.FFMpegEncoder;
import org.korsakow.services.encoders.video.ffmpeg.plaf.FFMpegEncoderOSX;
import org.korsakow.services.encoders.video.ffmpeg.plaf.FFMpegEncoderWin32;

import test.util.BaseTestCase;


public class TestExportSingleFrame extends BaseTestCase
{
	private File parentDir;
	private File dest;

	public TestExportSingleFrame()
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
		parentDir = FileUtil.createTempDirectory("test", "");
		parentDir.deleteOnExit();
	}
	@Override
	@After
	public void tearDown()
	{
		parentDir = null;
		dest = null;
	}
	private void doPre(File src, File dest) throws Exception
	{
//		dest.deleteOnExit();
		Assert.assertTrue("Can read source file", src.canRead());
		Assert.assertTrue("Can write destination file", dest.canWrite());
	}
	private void doPost(File dest) throws Exception
	{
		Assert.assertNotSame(dest.length(), 0);
		BufferedImage image = ImageIO.read(dest);
		Assert.assertNotNull(image);
	}
	private void test(String source) throws Exception
	{
		VideoEncoderFactory factory = VideoEncoderFactory.getNewFactory();
		factory.addRequiredOutputFormat(VideoCodec.JPG);
		VideoEncoder encoder = factory.createVideoEncoder();
		encoder.setVideoCodec(VideoCodec.JPG);
		encoder.setAudioCodec(AudioCodec.NONE);
		encoder.setFrameCount(1L);
		
		dest = File.createTempFile("TestVide", "." + encoder.getFileExtension(ContainerFormat.JPG), parentDir);
		File src = new File("resources/" + source);
		doPre(src, dest);
		
		encoder.encode(null, src, dest);
		
		doPost(dest);
	}
	@Test public void testCircleOfLife() throws Exception
	{
		test("CircleOfLife/boy.mov");
		test("CircleOfLife/girl.mov");
		test("CircleOfLife/couple.mov");
		test("CircleOfLife/couple_naked.mov");
		test("CircleOfLife/family.mov");
		test("CircleOfLife/man01.mov");
		test("CircleOfLife/man02.mov");
		test("CircleOfLife/man03.mov");
		test("CircleOfLife/man04.mov");
		test("CircleOfLife/woman01.mov");
		test("CircleOfLife/woman02.mov");
		test("CircleOfLife/woman03.mov");
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
	@Test(expected=EncoderException.class) public void testKorsakow3DefaultMovie2() throws Exception
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
	@Test(expected=EncoderException.class) public void testAppleProRes422() throws Exception
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
