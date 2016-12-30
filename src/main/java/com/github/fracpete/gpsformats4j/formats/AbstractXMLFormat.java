/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * AbstractXMLFormat.java
 * Copyright (C) 2016 FracPete
 */

package com.github.fracpete.gpsformats4j.formats;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;

/**
 * Ancestor for XML-based formats.
 *
 * @author FracPete (fracpete at gmail dot com)
 */
public abstract class AbstractXMLFormat
  extends AbstractFormat {

  /**
   * Reads the file.
   *
   * @param input	the input file
   * @return		the XML document, null in case of an error
   */
  protected Document readXML(File input) {
    DocumentBuilderFactory 	factory;
    DocumentBuilder 		builder;

    try {
      m_Logger.info("Reading: " + input);
      factory = DocumentBuilderFactory.newInstance();
      factory.setValidating(true);
      factory.setNamespaceAware(true);
      factory.setIgnoringComments(true);
      factory.setIgnoringElementContentWhitespace(true);
      builder = factory.newDocumentBuilder();
      return builder.parse(input);
    }
    catch (Exception e) {
      m_Logger.error("Failed to read: " + input, e);
      return null;
    }
  }

  /**
   * Creates a new document.
   *
   * @return		the new document, null if failed to create
   */
  protected Document newDocument() {
    DocumentBuilderFactory 	factory;
    DocumentBuilder 		builder;

    try {
      factory = DocumentBuilderFactory.newInstance();
      builder = factory.newDocumentBuilder();
      return builder.newDocument();
    }
    catch (Exception e) {
      m_Logger.error("Failed to create new XML document!", e);
      return null;
    }
  }

  /**
   * Creates a new document, adds the specified root element and returns it.
   *
   * @return		the root element, null if failed to create
   */
  protected Element newDocument(String root) {
    Document 	doc;
    Element	result;

    doc = newDocument();
    if (doc == null)
      return null;

    result = doc.createElement(root);
    doc.appendChild(result);

    return result;
  }

  /**
   * Writes to a file.
   *
   * @param doc		the document to write
   * @param output	the output file
   * @return		null if successful, otherwise error message
   */
  protected String writeXML(Document doc, File output) {
    TransformerFactory 	tfactory;
    Transformer 	transformer;
    StringWriter 	swriter;
    FileWriter 		fwriter;

    fwriter  = null;
    try {
      m_Logger.info("Writing: " + output);
      tfactory = TransformerFactory.newInstance();
      tfactory.setAttribute("indent-number", 2);
      transformer = tfactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.ENCODING, Charset.defaultCharset().toString());
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      swriter = new StringWriter();
      transformer.transform(new DOMSource(doc), new StreamResult(swriter));
      fwriter = new FileWriter(output);
      IOUtils.write(swriter.toString(), fwriter);
    }
    catch (Exception e) {
      m_Logger.error("Failed to write: " + output, e);
      return "Failed to write: " + output + "\n" + e;
    }
    finally {
      IOUtils.closeQuietly(fwriter);
    }

    return null;
  }

}
