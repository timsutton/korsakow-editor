package test.util;

import java.util.Properties;

import test.util.NanoHTTPD.Response;

public interface ServiceHandler
{
	Response runService(String serviceName, Properties header, Properties params);
}
