package org.korsakow.services.encoders.subtitle;

public interface ISubtitle extends Comparable<ISubtitle>
{
	long getStartTime();
	long getEndTime();
	String getText();
}
