package org.korsakow.domain.k3;

import java.util.ArrayList;
import java.util.Collection;

public class K3Project
{
	public K3ProjectSettings settings;
	public Collection<K3Snu> snus = new ArrayList<K3Snu>();
//	public Collection<Me> snus = new ArrayList<K3Snu>();
	public Collection<K3Interface> interfaces = new ArrayList<K3Interface>();
}
