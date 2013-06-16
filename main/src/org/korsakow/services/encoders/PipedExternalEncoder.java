package org.korsakow.services.encoders;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.korsakow.ide.io.AsyncStreamPipe;

public abstract class PipedExternalEncoder implements IEncoder
{
	public void encode(Process process, InputStream sourceStream, OutputStream destStream)
			throws EncoderException
	{
		AsyncStreamPipe<InputStream, OutputStream> inPiper = new AsyncStreamPipe<InputStream, OutputStream>(sourceStream, process.getOutputStream(), "PipedExternalEncoder.inpipe");
		AsyncStreamPipe<InputStream, OutputStream> outPiper = new AsyncStreamPipe<InputStream, OutputStream>(process.getInputStream(), destStream, "PipedExternalEncoder.outpipe");
		AsyncStreamPipe<InputStream, ByteArrayOutputStream> errPiper = new AsyncStreamPipe<InputStream, ByteArrayOutputStream>(process.getErrorStream(), new ByteArrayOutputStream(), "PipedExternalEncoder.errpipe");
		inPiper.start();
		outPiper.start();
		errPiper.start();
		// we avoid thread issues by basically not doing anything until all pipers have joined.
		try {
			if (0 != process.waitFor()) {
				errPiper.join();
				throw new EncoderException("unknown error: " + errPiper.getOutputStream().toString(), errPiper.getOutputStream().toString(), null);
			}
			inPiper.join();
			outPiper.join();
			errPiper.join();
			process.destroy();
		} catch (InterruptedException e) {
			throw new EncoderException(e);
		}
		if (null != inPiper.getException()) throw new EncoderException(inPiper.getException());
		if (null != outPiper.getException()) throw new EncoderException(outPiper.getException());
	}
}
