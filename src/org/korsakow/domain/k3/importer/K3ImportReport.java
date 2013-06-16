/**
 * 
 */
package org.korsakow.domain.k3.importer;

import java.util.ArrayList;
import java.util.List;

public class K3ImportReport
{
	public static enum MessageType
	{
		Warning("Warning"),
		Unsupported("Unsupported feature");
		public String getDisplayString()
		{
			return display;
		}
		private String display;
		MessageType(String display)
		{
			this.display = display;
		}
	}
	public static class Message
	{
		public final MessageType type;
		public final String message;
		public final String context;
		public Message(MessageType type, String message)
		{
			this(type, message, "");
		}
		public Message(MessageType type, String message, String context)
		{
			this.type = type;
			this.message = message;
			this.context = context;
		}
	}
	private List<Message> messages = new ArrayList<Message>();
	public void addWarning(String message)
	{
		messages.add(new Message(MessageType.Warning, message));
	}
	public void addWarning(String message, String location)
	{
		messages.add(new Message(MessageType.Warning, message, location));
	}
	public void addUnsupported(String message)
	{
		messages.add(new Message(MessageType.Unsupported, message));
	}
	public void addUnsupported(String message, String location)
	{
		messages.add(new Message(MessageType.Unsupported, message, location));
	}
	public List<Message> getMessages()
	{
		return messages;
	}
}