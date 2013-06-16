package org.korsakow.domain.k3;


public class K3Rule implements Comparable<K3Rule>
{
	public int lineNumber;
	
	public long time;
	public String code;
	public Long maxLinks;
	
	public int compareTo(K3Rule o) {
		return (int)(time - o.time);
	}
}
