package test.org.korsakow.domain;

import org.dsrg.soenea.domain.DomainObject;
import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.GenericOutputMapper;
import org.dsrg.soenea.domain.proxy.DomainObjectProxy;
import org.dsrg.soenea.uow.MapperFactory;
import org.dsrg.soenea.uow.UoW;
import org.junit.Test;

public class TestProxy extends AbstractDomainObjectTestCase {
	@Test public void testUoWRegisterProxyDirtyAndCommit()
	{
		final long ID = 0;
		ProxyBaseClass proxy = new ProxyBaseClass(ID);
		MapperFactory fac = new MapperFactory();
		fac.addMapping(MyDomainObject.class, MyOutputMapper.class);
		UoW.initMapperFactory(fac);
		UoW.getCurrent().registerDirty(proxy);
	}
	@Test public void testUoWRegisterProxySubclassDirtyAndCommit()
	{
		final long ID = 0;
		ProxySubClass proxy = new ProxySubClass(ID);
		MapperFactory fac = new MapperFactory();
		fac.addMapping(MyDomainObject.class, MyOutputMapper.class);
		UoW.initMapperFactory(fac);
		UoW.getCurrent().registerDirty(proxy);
	}
	private static class MyOutputMapper implements GenericOutputMapper<Long, MyDomainObject> {
		@Override
		public void delete(MyDomainObject d) throws MapperException {
		}
		@Override
		public void insert(MyDomainObject d) throws MapperException {
		}
		@Override
		public void update(MyDomainObject d) throws MapperException {
		}
	}
	private static class ProxyBaseClass extends DomainObjectProxy<Long, MyDomainObject>
	{
		protected ProxyBaseClass(Long id) {
			super(id);
			
		}
		@Override
		protected MyDomainObject getFromMapper(Long id) throws MapperException {
			return new MyDomainObject( id );
		}
	}
	private static class ProxySubClass extends ProxyBaseClass
	{
		protected ProxySubClass(Long id) {
			super(id);
			
		}
	}
	private static class MyDomainObject extends DomainObject<Long>
	{
		protected MyDomainObject(Long id) {
			super(id);
		}
		
	}
}
