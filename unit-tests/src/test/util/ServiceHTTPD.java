package test.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Properties;

public class ServiceHTTPD extends NanoHTTPD
{
	public ServiceHandler serviceHandler;
	public ServiceHTTPD(int port) throws IOException {
		super(port);
	}
	public Response serve( String uri, String method, Properties header, Properties parms )
	{
		Response response = super.serve( uri, method, header, parms);
		if (NanoHTTPD.HTTP_NOTFOUND.equals(response.status) && serviceHandler!=null && uri.endsWith("Service")) {
			return serveService(uri, method, header, parms);
		} else
			return response;
	}
	public Response serveService( String uri, String method, Properties header, Properties parms )
	{
		int index = uri.lastIndexOf('/');
		String serviceName = uri.substring(index+1);
		System.out.println("Service method: " + serviceName);
		Response response = serviceHandler.runService(serviceName, header, parms);
		String responseData = "";
		if (response.data instanceof ByteArrayInputStream) {
			try {
				response.data.mark(response.data.available());
				byte[] bytes = new byte[response.data.available()];
				response.data.read(bytes);
				responseData = new String(bytes);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try { response.data.reset(); } catch (IOException e) { e.printStackTrace(); }
			}
		}
		System.out.println("Service response: " + response.status + ";" + responseData);
		return response;
	}
}



