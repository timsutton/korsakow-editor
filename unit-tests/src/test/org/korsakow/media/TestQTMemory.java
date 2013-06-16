package test.org.korsakow.media;

import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.lang.ref.WeakReference;

import javax.swing.JFrame;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.korsakow.ide.resources.media.QTVideo;
import org.korsakow.ide.util.StrongReference;
import org.korsakow.ide.util.UIUtil;

import quicktime.QTSession;
import test.util.BaseTestCase;
import test.util.MoreAsserts;
import test.util.Unit;

/**
 * This is a set of tests related to the performance of the QT api.
 * 
 * Particular attention is paid to memory usage & deallocation.
 * 
 * 
 * @author d
 *
 */
@Ignore(value="Not accurate")
public class TestQTMemory extends BaseTestCase
{
	private static class MemoryRecord
	{
		public final long freeMemory;
		public final long totalMemory;
		public final long maxMemory;
		public MemoryRecord(long freeMemory, long totalMemory, long maxMemory)
		{
			this.freeMemory = freeMemory;
			this.totalMemory = totalMemory;
			this.maxMemory = maxMemory;
		}
		public MemoryRecord()
		{
			this(Runtime.getRuntime().freeMemory(), Runtime.getRuntime().totalMemory(), Runtime.getRuntime().maxMemory());
		}
		public static MemoryRecord delta(MemoryRecord before, MemoryRecord after)
		{
			return new MemoryRecord(after.freeMemory-before.freeMemory, after.totalMemory-before.totalMemory, after.maxMemory-before.maxMemory);
		}
	}
	private final File base;
	private final File file;
	private final int N;
	private MemoryRecord memBefore;
	private MemoryRecord memAfter;
	private MemoryRecord memDelta;
	private final Unit unit;
	public TestQTMemory()
	{
		base = new File("resources/");
		String filename = "v_divx_a_mp2.mov";
		file = new File(base, filename);
		N = 100;
		unit = Unit.MegaBytes;
	}
	@Override
	@Before
	public void setUp() throws Exception
	{
		QTSession.open();
//		printRecord("before", memBefore, unit);
	}
	@Override
	@After
	public void tearDown() throws Exception
	{
		QTSession.close();
	}
	private static void printRecord(String prefix, MemoryRecord record, Unit unit)
	{
		System.out.printf("Memory %s%n", prefix);
		System.out.printf("\tfree: %2.2f%s%n", unit.convertBytes(record.freeMemory), unit.getPostfix());
		System.out.printf("\ttotal: %2.2f%s%n", unit.convertBytes(record.totalMemory), unit.getPostfix());
		System.out.printf("\tmax: %2.2f%s%n", unit.convertBytes(record.maxMemory), unit.getPostfix());
	}
	private void commonBefore(String name)
	{
		System.out.println("Test: " + name);
		memBefore = new MemoryRecord();
		printRecord("before", memBefore, unit);
	}
	private void commonAfter()
	{
		memAfter = new MemoryRecord();
		printRecord("after", memAfter, unit);
		memDelta = MemoryRecord.delta(memBefore, memAfter);
		printRecord("delta", memDelta, unit);
	}
	/**
	 * Tests that QT is releasing memory.
	 * 
	 * We verify in two ways:
	 * 		-first that you don't simply get an OutOfMemoryError
	 * 		-more reliable: we verify that the amount of free memory remains stable over many iterations
	 */
	@Test public void testMemoryUsage() throws Throwable
	{
		commonBefore("Memory: remove first");
		final StrongReference<Throwable> error = new StrongReference<Throwable>();
		long referenceFreeMemory = 0;
		for (int i = 0; i < N; ++i)
		{
			final QTVideo video = new QTVideo(file.getAbsolutePath());
			final WeakReference<Component> comp = new WeakReference<Component>(video.getComponent());
			final JFrame frame = new JFrame();
			UIUtil.runUITaskNow(new Runnable() {
				public void run() {
					frame.setSize(new Dimension(0, 0));
					frame.setVisible(true);
					frame.add(comp.get());
				}
			});
			if (!error.isNull()) throw error.get();
			UIUtil.runUITaskNow(new Runnable() {
				public void run() {
					try {
						frame.remove(comp.get());
					} catch (Throwable t) {
						error.set(t);
					}
				}
			});
			if (!error.isNull()) throw error.get();
			video.dispose(); // it should not matter if we dispose the video or remove first
			frame.dispose();
			long used = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
			long free = Runtime.getRuntime().maxMemory() - used;

			if (i == 0)
				referenceFreeMemory = free;
			float percentFree = free/(float)Runtime.getRuntime().maxMemory();
			// the tolerance is a twiddle value whose dependencies are the source movie and QTJava.
			// the point is basically to make sure that the amount of free memory is stable (ie no leaks)
			float tolerance = 1024*1024;
			MoreAsserts.assertEquals(free, referenceFreeMemory, tolerance);
//			System.out.println(i + "\t" + percentFree); // the free memory should remain relatively stable
		}
		commonAfter();
	}
}
