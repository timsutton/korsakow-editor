/**
 * 
 */
package org.korsakow.ide.ui.components.model;

import java.util.HashSet;
import java.util.Set;

public class KeywordEntry
{
	// these are used by the snulist
	public static enum State
	{
		INDETERMINATE,
		UNUSED,
		USED_BY_ALL,
		USED_BY_SOME,
	}
	public String keyword;
	public State state = State.INDETERMINATE;
	
	// these are used bythe snueditor (yes bad design mixing these and the snulist states)
	public boolean usedByThisSnu = false;
	public boolean usedByOtherSnu = false;
	public boolean inForThisSnu = false;
	public boolean inForOtherSnu = false;
	
	public Set<Long> resources = new HashSet<Long>();
	public KeywordEntry(String keyword)
	{
		this.keyword = keyword;
	}
}