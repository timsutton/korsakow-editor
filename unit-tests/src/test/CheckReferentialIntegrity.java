package test;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.korsakow.domain.mapper.input.MapperHelper;
import org.korsakow.ide.XPathHelper;
import org.korsakow.ide.util.DomUtil;
import org.korsakow.ide.util.Util;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class CheckReferentialIntegrity
{
	static class Meta {
		public String type;
		public long id;
		public Meta(String type, long id)
		{
			this.type = type;
			this.id = id;
		}
		
	}
// ? is this
//	@Test
//	public void test() throws Exception {
//		File file = new File("/Users/d/Library/Mail Downloads/php4PHzxc/MyProject/Korsakow.krw");
//		Document doc = DomUtil.parseXML(file);
//		Map<Long, Meta> meta = new HashMap<Long, Meta>();
//		for (Node node : XPathHelper.xpathAsList(doc, "//id")) {
//			Meta m = new Meta(((Element)node.getParentNode()).getTagName(), Long.parseLong(node.getTextContent()));
//			meta.put(m.id, m);
//		}
//		for (Node node : XPathHelper.xpathAsList(doc, "//" + Util.join(Arrays.asList(MapperHelper.REFERENCING_ELEMENTS), "| //"))) {
//			String tagName = ((Element)node).getTagName();
//			long id = Long.parseLong(node.getTextContent());
//			System.out.println(tagName + " = " + meta.get(id).type);
//		}
		
//		for (Meta m : meta.values()) {
//			for (Node node : XPathHelper.xpathAsList(doc, "//*[text()=?]", m.id)) {
//				final String tagName = ((Element)node).getTagName();
//				if ("id".equals(tagName))
//					continue;
//				System.out.println(tagName + " / " + m.id);
//			}
//		}
//	}
}
