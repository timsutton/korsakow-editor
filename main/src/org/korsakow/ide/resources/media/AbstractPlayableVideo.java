package org.korsakow.ide.resources.media;

import java.awt.Dimension;

public abstract class AbstractPlayableVideo implements PlayableVideo
{
	public boolean isTemporal()
	{
		return true;
	}
	public Dimension getAspectRespectingDimension(Dimension outter) {
		Dimension inner = getComponent().getPreferredSize();
		float aspectRatio = inner.width/(float)inner.height;
		if(outter.width/aspectRatio < outter.height) {
			return new Dimension(outter.width, (int)(outter.width/aspectRatio));
		} else {
			return new Dimension((int)(outter.height*aspectRatio), outter.height);
		}
	}
}
