/**
 * 
 */
package test.util;



public class Signal
{
	private boolean set = false;
	private long timeout;
	private int count = 0;
	private int countNeeded = 1;
	public Signal()
	{
		this(20*1000);
	}
	public Signal(long timeout)
	{
		this.timeout = timeout;
	}
	public void setCountNeeded(int countNeeded)
	{
		synchronized (this) {
			this.countNeeded = countNeeded;
		}
	}
	public void waitOrThrow() throws InterruptedException
	{
		if (!waitOrTimeout())
			throw new AssertionError("signal timed out");
	}
	public boolean waitOrTimeout() throws InterruptedException
	{
		synchronized (this) {
			// technically we should do a while loop instead of an if
			if (!set)
				this.wait(timeout);
			return set;
		}
	}
	public void fire()
	{
		if (!set) {
			synchronized (this) {
				if (++count >= countNeeded) {
					set = true;
					this.notifyAll();
				}
			}
		}
	}
}