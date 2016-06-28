MIT License

Copyright (c) 2016 Morten Bendiksen

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

package com.alisk.xml;

import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

interface XmlNode {
}

class Attribute implements XmlNode {
    String name;
    String value;

    Attribute(String name, String value) {
        this.name = name;
        this.value = value;
    }
}

class Empty implements XmlNode {

}

class Value implements XmlNode {
    String val;

    Value(String val) {
        this.val = val;
    }
}

public class Tag implements XmlNode {
    String name;
    ArrayList<XmlNode> parts = new ArrayList<>();

    Tag(String name) {
        this.name = name;
    }

    static Tag tag(String name) {
        return new Tag(name);
    }

    static Value val(String value) {
        return new Value(value);
    }

    Tag with(XmlNode... es) {
        for (XmlNode e : es) {
            parts.add(e);
        }
        return this;
    }

    static Attribute atr(String name, String value) {
        return new Attribute(name, value);
    }

    static Element createElement(Document doc, Tag tag) {
        Element el = doc.createElement(tag.name);
        for (XmlNode e : tag.parts) {
            if (e instanceof Tag) {
                el.appendChild(createElement(doc, (Tag) e));
            } else if (e instanceof Attribute) {
                Attribute a = (Attribute) e;
                el.setAttribute(a.name, a.value);
            } else if (e instanceof Value) {
                el.appendChild(doc.createTextNode(((Value) e).val));
            }
        }
        return el;
    }

    Document toDoc() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = null;
            docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            doc.appendChild(createElement(doc, this));
            return doc;
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public String toString() {
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer;

            transformer = tf.newTransformer();

            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();

            transformer.transform(new DOMSource(toDoc()), new StreamResult(writer));
            String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
            return output;
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String xml =
                tag("foo").with(
                    atr("bar", "baz"),
                    tag("hello"),
                    val("world"),
                    tag("life").with(
                       atr("isgood", "yes")))
                .toString();
        System.out.println(xml);
    }

}
