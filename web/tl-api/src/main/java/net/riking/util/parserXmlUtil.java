package net.riking.util;

import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.riking.entity.model.BigAmount;

public class parserXmlUtil {

	static {
		ConvertUtils.register(new Converter() {
			@SuppressWarnings("unchecked")
			public Object convert(Class type, Object value) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
				try {
					return simpleDateFormat.parse(value.toString());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		}, Date.class);
	}

	public static <T> T parserXml(T t, Map<String, String> names, String path) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = getDocument(db, path);
		NodeList nodeList = document.getChildNodes();
		Map<String, Node> mapNode = getElement(nodeList, names.keySet());
		for (String name : names.keySet()) {
			Node node = mapNode.get(name);
			String[] list2 = names.get(name).split(",", -1);
			for (String conlum : list2) {
				BeanUtils.setProperty(t, conlum.toLowerCase(), getTextContent(node, conlum, ""));
			}
		}
		return t;
	}

	private static Map<String, Node> getElement(NodeList nodes, Set<String> names) throws Exception {
		Map<String, Node> list = new HashMap<String, Node>();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getChildNodes().getLength() == 1) {
				String nodeName = null;
				if (node.getAttributes() != null && node.getAttributes().getNamedItem("seqno") != null) {
					nodeName = node.getNodeName() + "[@seqno=\""
							+ node.getAttributes().getNamedItem("seqno").getNodeValue() + "\"]";
				}
				Iterator<String> it = names.iterator();
				while (it.hasNext()) {
					String name = it.next();
					if (name.equals(nodeName)) {
						list.put(nodeName, node);
					}
				}
			} else {
				String nodeName = null;
				if (node.getAttributes() != null && node.getAttributes().getNamedItem("seqno") != null) {
					nodeName = node.getNodeName() + "[@seqno=\""
							+ node.getAttributes().getNamedItem("seqno").getNodeValue() + "\"]";
				}
				Iterator<String> it = names.iterator();
				while (it.hasNext()) {
					String name = it.next();
					if (name.equals(nodeName)) {
						list.put(nodeName, node);
					}
				}
				list.putAll(getElement(node.getChildNodes(), names));
			}
		}
		return list;
	}

	private static String getTextContent(Node node, String nodeName, String textContent) {
		if (node.getChildNodes().getLength() == 1) {
			if (node.getNodeName().equals(nodeName)) {
				textContent = node.getTextContent();
			}
		} else {
			NodeList nodeList = node.getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++) {
				if (StringUtils.isEmpty(textContent)) {
					textContent = getTextContent(nodeList.item(i), nodeName, textContent);
				}

			}
		}
		return textContent;
	}

	private static Document getDocument(DocumentBuilder dBuilder, String path) {
		Document document = null;
		try {
			if (path.substring(path.lastIndexOf(".")).equals(".ZIP")) {
				ZipFile zipf = new ZipFile(path);
				ZipInputStream zipIs = new ZipInputStream(new FileInputStream(path));
				ZipEntry entry;
				while ((entry = zipIs.getNextEntry()) != null) {
					if (entry.getName().substring(entry.getName().lastIndexOf(".")).equals(".XML")) {
						document = dBuilder.parse(zipf.getInputStream(entry));
						break;
					}
				}
			} else {
				FileInputStream fis = new FileInputStream(path);
				document = dBuilder.parse(fis);
			}
		} catch (Exception e) {
			System.err.println("ParseXmlUtil.getDocement ERROR");
			e.printStackTrace();
		}

		return document;
	}

	public static void main(String[] args) {
		try {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("CATI[@seqno=\"1\"]", "CSNM,HTDT");
			map.put("HTCR[@seqno=\"1\"]", "CRCD");
			map.put("TSDT[@seqno=\"1\"]", "TICD");
			BigAmount bigAmount = parserXml(new BigAmount(), map,
					"D:\\AML\\nowzip\\NBH010093101155109-20170615-00000005.ZIP");
			System.err.println(bigAmount);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
