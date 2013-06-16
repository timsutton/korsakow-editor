package org.korsakow.domain;

public enum MediaSource
{
	/**
	 * The media is contained inline, that is directly in the resource definition.
	 */
	INLINE("inline"),
	/**
	 * The media is contained in a file.
	 */
	FILE("file");
	
	public static MediaSource getById(String id)
	{
		for (MediaSource source : values())
			if (source.getId().equals(id))
				return source;
		throw new IllegalArgumentException(id);
	}
	
	private String id;
	MediaSource(String id)
	{
		this.id = id;
	}
	public String getId()
	{
		return id;
	}
}
