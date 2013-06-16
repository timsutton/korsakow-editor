/**
 * 
 */
package org.korsakow.ide.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A thread for piping an InputStream to an OutputStream.
 * Absolutely no thread-safety is assumed or enforced, caller assumes all responsibility.
 * 
 * @author d
 *
 * @param <Input>
 * @param <Output>
 */
public class AsyncStreamPipe<Input extends InputStream, Output extends OutputStream> extends Thread
{
	private Input source;
	private Output dest;
	private Throwable exception = null;
	private byte[] buffer;
	public AsyncStreamPipe(Input source, Output dest, int buffersize, String name)
	{
		super(name);
		this.source = source;
		this.dest = dest;
		this.buffer = new byte[buffersize];
	}
	public AsyncStreamPipe(Input source, Output dest, int buffersize)
	{
		this(source, dest, buffersize, "Piper");
	}
	public AsyncStreamPipe(Input source, Output dest, String name)
	{
		this(source, dest, 1024, name);
	}
	public AsyncStreamPipe(Input source, Output dest)	
	{
		this(source, dest, 1024, "Piper");
	}
	public Input getInputStream()
	{
		return source;
	}
	public Output getOutputStream()
	{
		return dest;
	}
	public Throwable getException()
	{
		return exception;
	}
	@Override
	public void run()
	{
		int len = -1;
//		System.out.println(getName() + ".enter");
		try {
			while (-1 != (len=source.read(buffer))) {
				dest.write(buffer, 0, len);
			}
		} catch (IOException ioe) {
			exception = ioe;
		} finally {
			try { dest.flush(); } catch (IOException e) {}
			try { if (!System.in.equals(source)) source.close(); } catch (IOException e) {}
			try { if (!System.out.equals(dest) && !System.err.equals(dest)) dest.close(); } catch (IOException e) {}
//			System.out.println(getName() + ".exit");
		}
	}
}