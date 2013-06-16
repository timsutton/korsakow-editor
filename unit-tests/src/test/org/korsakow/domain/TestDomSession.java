package test.org.korsakow.domain;

import java.util.ConcurrentModificationException;

import org.junit.Assert;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.korsakow.ide.DomSession;
import org.korsakow.ide.XPathHelper;
import org.korsakow.ide.util.DomUtil;
import org.korsakow.ide.util.StrongReference;
import org.korsakow.ide.util.UIUtil;
import org.w3c.dom.Document;

public class TestDomSession
{
	private DomSession dom;
	public TestDomSession()
	{
	}
	@Before
	public void setUp() throws Exception
	{
		Document head = DomUtil.createDocument();
		head.appendChild(head.createElement("root"));
		dom = new DomSession(head);
	}
	@After
	public void tearDown() throws Exception
	{
		
	}
	@Test public void testVersionIncrementOnCommit()
	{
		long before = dom.getVersion();
		dom.commit();
		long after = dom.getVersion();
		Assert.assertEquals(before+1, after);
	}
	@Test public void testCommitOneThread() throws Exception
	{
		Document doc = dom.getDocument();
		doc.getDocumentElement().appendChild(doc.createTextNode("hello"));
		dom.commit();
		Assert.assertEquals("hello", dom.getDocument().getDocumentElement().getTextContent());
		Assert.assertTrue(DomUtil.isEqualAsXMLString(dom.getDocument(), dom.getHeadDocument()));
	}
	@Test public void testCommitTwoThreadsDomManipulation() throws Exception
	{
		final StrongReference<Document> ref = new StrongReference<Document>();
		UIUtil.runUITaskNow(new Runnable() {
			public void run() {
				Document doc = dom.getDocument();
				doc.getDocumentElement().appendChild(doc.createTextNode("hello"));
				dom.commit();
				ref.set(doc);
			}
		});
		Assert.assertEquals("hello", dom.getDocument().getDocumentElement().getTextContent());
		Assert.assertTrue(DomUtil.isEqualAsXMLString(ref.get(), dom.getDocument()));
	}
	@Test public void testCommitTwoThreadsSetDocument() throws Exception
	{
		final Document refDoc = DomUtil.createDocument();
		refDoc.appendChild(refDoc.createElement("testme"));
		refDoc.getDocumentElement().appendChild(refDoc.createCDATASection(""+Math.random()));
		UIUtil.runUITaskNow(new Runnable() {
			public void run() {
				dom.setDocument(refDoc);
				dom.commit();
			}
		});
		Assert.assertTrue(DomUtil.isEqualAsXMLString(refDoc, dom.getDocument()));
	}
	@Test public void testRollbackOneThread() throws Exception
	{
		Document doc = dom.getDocument();
		doc.getDocumentElement().appendChild(doc.createTextNode("hello"));
		doc = null;
		dom.rollbackToHead();
		Assert.assertFalse(dom.getDocument().getDocumentElement().getTextContent().equals("hello"));
		Assert.assertTrue(DomUtil.isEqualAsXMLString(dom.getDocument(), dom.getHeadDocument()));
	}
	@Test public void testMultipleWriteFailure() throws Exception
	{
		dom.getDocument(); // get thread-local so we could potentially get out of sync
		final StrongReference<Document> ref = new StrongReference<Document>();
		UIUtil.runUITaskNow(new Runnable() {
			public void run() {
				dom.commit();
			}
		});
		try {
			dom.commit();
			throw new AssertionError("expected an exception");
		} catch (ConcurrentModificationException expected) {
			// expected failure
		}
	}
	
	@Test public void testHistoryAddedOnCommit() throws Exception
	{
		Document doc = dom.getDocument();
		Assert.assertEquals(0, dom.getHistory().size());

		DomUtil.appendTextNode(doc, doc.getDocumentElement(), "mama", "dada");
		dom.commit();
		Assert.assertEquals(1, dom.getHistory().size());
		Assert.assertEquals("dada", XPathHelper.xpathAsString(doc, "/root/mama"));
		
		// History currently limited to 1
//		DomUtil.appendTextNode(doc, doc.getDocumentElement(), "caca", "pipi");
//		dom.commit();
//		Assert.assertEquals(2, dom.getHistory().size());
//		Assert.assertEquals("dada", XPathHelper.xpathAsString(doc, "/root/mama")); // still there
//		Assert.assertEquals("pipi", XPathHelper.xpathAsString(doc, "/root/caca")); // new
		
	}
	@Test public void testHistoryRollback() throws Exception
	{
		Document doc = dom.getDocument();
		long version0 = dom.getVersion();
		Assert.assertEquals(0, dom.getHistory().size());

		DomUtil.appendTextNode(doc, doc.getDocumentElement(), "mama", "dada");
		dom.commit();
		long version1 = dom.getVersion();
		
		DomUtil.appendTextNode(doc, doc.getDocumentElement(), "caca", "pipi");
		dom.commit();
		long version2 = dom.getVersion();
		
		Assert.assertEquals("dada", XPathHelper.xpathAsString(doc, "/root/mama"));
		Assert.assertEquals("pipi", XPathHelper.xpathAsString(doc, "/root/caca"));

		dom.rollbackHeadToPreviousVersion();
		doc = dom.getDocument();
		
		Assert.assertEquals(version1, dom.getVersion());
		Assert.assertEquals("dada", XPathHelper.xpathAsString(doc, "/root/mama"));
		Assert.assertEquals(0, XPathHelper.xpathAsList(doc, "/root/caca").size());
	}
}
