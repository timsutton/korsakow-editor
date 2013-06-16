package test.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

import test.util.NanoHTTPD.Response;

public abstract class ReflectionServiceHandler implements ServiceHandler
{
	public Response runService(String serviceName, Properties header, Properties params)
	{
		Method method;
		try {
			method = getClass().getMethod(serviceName, Properties.class, Properties.class);
			Response response = (Response)method.invoke(this, header, params);
			return response;
		} catch (SecurityException e) {
			e.printStackTrace();
			return new Response(NanoHTTPD.HTTP_INTERNALERROR, NanoHTTPD.MIME_PLAINTEXT, e.getMessage());
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return new Response(NanoHTTPD.HTTP_NOTFOUND, NanoHTTPD.MIME_PLAINTEXT, e.getMessage());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return new Response(NanoHTTPD.HTTP_INTERNALERROR, NanoHTTPD.MIME_PLAINTEXT, e.getMessage());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return new Response(NanoHTTPD.HTTP_INTERNALERROR, NanoHTTPD.MIME_PLAINTEXT, e.getMessage());
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return new Response(NanoHTTPD.HTTP_INTERNALERROR, NanoHTTPD.MIME_PLAINTEXT, e.getMessage());
		}
	}
}
