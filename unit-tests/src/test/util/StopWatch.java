package test.util;

public class StopWatch
{
	public static StopWatch Start()
	{
		StopWatch sw = new StopWatch();
		sw.start();
		return sw;
	}
	private long begin;
	private long end;
	
	public void start()
	{
		begin = System.currentTimeMillis();
	}
	public void stop()
	{
		end = System.currentTimeMillis();
	}
	public long getTime()
	{
		return end - begin;
	}
	public void print(String label)
	{
		stop();
		System.out.println(toString(label));
	}
	@Override
	public String toString()
	{
		return toString("");
	}
	public String toString(String label)
	{
		return String.format("%s Took: %s milliseconds", label, getTime());
	}
}
