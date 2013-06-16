package org.korsakow.ide.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * OutputStream equivalent to /dev/null
 *
 */
public class NullOutputStream extends OutputStream
{
	@Override
	public void write(int b) throws IOException {
	}
}
