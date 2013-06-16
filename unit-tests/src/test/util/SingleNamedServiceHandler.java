package test.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

import test.util.NanoHTTPD.Response;

public abstract class SingleNamedServiceHandler implements ServiceHandler
{
	private final String serviceName;
	public SingleNamedServiceHandler(String serviceName)
	{
		this.serviceName = serviceName;
	}
	public abstract Response handleResponse(Properties header, Properties params);
	public Response runService(String serviceName, Properties header, Properties params)
	{
		if (!this.serviceName.equals(serviceName)) {
			return new Response(NanoHTTPD.HTTP_NOTFOUND, NanoHTTPD.MIME_PLAINTEXT, "not found: " + serviceName);
		}
		return handleResponse(header, params);
	}
}
