package org.korsakow.domain.k3;

import java.util.ArrayList;
import java.util.List;

public class K3Snu
{
	public String filename;
	public String foldername;
	/**
	 * 999 taken to mean infinite in database file. here represented as null;.
	 */
	public Long lives;
	public boolean backgroundSoundEnabled;
	/**
	 * In the database file, this is the position on the slider in the K3 UI! [0,100]
	 * The value here is already transformed to the functional value via K3RatingCalculator
	 */
	public float movieRating = 1.0f;
	/**
	 * This gets stored in the database file, we don't determine it at runtime.
	 */
	public long duration;
	public boolean looping;
	public String previewText;
	public String insertText;
	public List<K3Rule> rules = new ArrayList<K3Rule>();
}
