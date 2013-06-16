package test.org.korsakow.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.interf.IDomainObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.korsakow.domain.KDomainObject;
import org.korsakow.domain.proxy.KDomainObjectProxy;

/**
 * TODO: test that the clone (or its properties, for example list properties) is not == to in certain cases
 * @author d
 *
 */
public class TestDomainObjectEquality extends AbstractDomainObjectTestCase
{
	private static final long ID = 100;
	private static final long VERSION = 100;
	private static Map<Long, IDomainObject<Long>> identitiyMap = new HashMap<Long, IDomainObject<Long>>();
	
	@Override
	@Before
	public void setUp() throws Exception
	{
		super.setUp();
	}
	@Override
	@After
	public void tearDown() throws Exception
	{
		super.tearDown();
	}
	@Test public void testDOEqualsMethodPositive() throws Exception
	{
		IDomainObject<Long> do1 = new KDomainObject(ID, VERSION);
		IDomainObject<Long> do2 = new KDomainObject(ID, VERSION);
		Assert.assertTrue(do1.equals(do2));
	}
	@Test public void testDOEqualsMethodNegative() throws Exception
	{
		IDomainObject<Long> do1 = new KDomainObject(ID, VERSION);
		IDomainObject<Long> do2 = new KDomainObject(ID+1, VERSION);
		Assert.assertFalse(do1.equals(do2));
	}
	@Test public void testDOHashEquality() throws Exception
	{
		IDomainObject<Long> do1 = new KDomainObject(ID, VERSION);
		IDomainObject<Long> do2 = new KDomainObject(ID, VERSION);
		Assert.assertEquals(do1.hashCode(), do2.hashCode());
	}
	@Test public void testProxyHashEquality() throws Exception
	{
		IDomainObject<Long> do1 = new KDomainObject(ID, VERSION);
		identitiyMap.put(do1.getId(), do1);
		IDomainObject<Long> p1 = new TestProxy(ID);
		IDomainObject<Long> p2 = new TestProxy(ID);
		Assert.assertEquals(p1.hashCode(), p2.hashCode());
	}
	@Test public void testDOHashSet() throws Exception
	{
		IDomainObject<Long> do1 = new KDomainObject(ID, VERSION);
		IDomainObject<Long> do2 = new KDomainObject(ID, VERSION);
		HashSet<IDomainObject<Long>> set = new HashSet<IDomainObject<Long>>();
		set.add(do1);
		set.add(do2);
		Assert.assertEquals(set.size(), 1);
	}
	@Test public void testProxyHashSet() throws Exception
	{
		IDomainObject<Long> do1 = new KDomainObject(ID, VERSION);
		identitiyMap.put(do1.getId(), do1);
		IDomainObject<Long> p1 = new TestProxy(ID);
		IDomainObject<Long> p2 = new TestProxy(ID);
		HashSet<IDomainObject<Long>> set = new HashSet<IDomainObject<Long>>();
		set.add(p1);
		set.add(p2);
		Assert.assertEquals(set.size(), 1);
	}
	@Test public void testSetContains() throws Exception
	{
		Set<IDomainObject<Long>> set = new HashSet<IDomainObject<Long>>();
		IDomainObject<Long> do1 = new KDomainObject(ID, VERSION);
		set.add(do1);
		Assert.assertTrue(set.contains(do1));
	}
	@Test public void testMapContainsKey() throws Exception
	{
		Map<IDomainObject<Long>, Boolean> map = new HashMap<IDomainObject<Long>, Boolean>();
		IDomainObject<Long> do1 = new KDomainObject(ID, VERSION);
		map.put(do1, Boolean.TRUE);
		Assert.assertTrue(map.containsKey(do1));
	}
	@Test public void testMapContainsValue() throws Exception
	{
		Map<Boolean, IDomainObject<Long>> map = new HashMap<Boolean, IDomainObject<Long>>();
		IDomainObject<Long> do1 = new KDomainObject(ID, VERSION);
		map.put(Boolean.TRUE, do1);
		Assert.assertTrue(map.containsValue(do1));
	}
	private class TestProxy extends KDomainObjectProxy<KDomainObject>
	{
		public TestProxy(long id) {
			super(id);
		}
		@Override
		protected KDomainObject getFromMapper(Long id) throws MapperException
		{
			return (KDomainObject)identitiyMap.get(id);
		}
		@Override
		public Class<KDomainObject> getInnerClass()
		{
			return null;
		}
	}
}
