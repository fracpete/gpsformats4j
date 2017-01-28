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
 * KML.java
 * Copyright (C) 2017 FracPete
 */

package com.github.fracpete.gpsformats4j.formats;

import org.apache.commons.csv.CSVRecord;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.util.List;

/**
 * KML format (Keyhole Markup Language).
 * https://developers.google.com/kml/documentation/kmlreference
 *
 * @author FracPete (fracpete at gmail dot com)
 * @see Formats#allFormats()
 */
public class KML
  extends AbstractXMLFormat {

  /**
   * Returns whether reading is supported.
   *
   * @return		true if supported
   */
  @Override
  public boolean canRead() {
    return false;
  }

  /**
   * Reads the file.
   *
   * @param input	the input file
   * @return		the collected data, null in case of an error
   */
  @Override
  public List<CSVRecord> read(File input) {
    return null;
  }

  /**
   * Returns whether writing is supported.
   *
   * @return		true if supported
   */
  @Override
  public boolean canWrite() {
    return true;
  }

  /**
   * Writes to a file.
   *
   * @param data	the data to write
   * @param output	the output file
   * @return		null if successful, otherwise error message
   */
  @Override
  public String write(List<CSVRecord> data, File output) {
    Document 		doc;
    Element		root;
    Element 		child;
    Element 		kml;
    Element		pm;
    Element		coordinates;
    StringBuilder	coords;
    String		oldTrack;

    try {
      kml = newDocument("kml");
      kml.setAttribute("xmlns", "http://www.opengis.net/kml/2.2");
      doc  = kml.getOwnerDocument();
      root = doc.createElement("Document");
      kml.appendChild(root);
      oldTrack    = "";
      pm          = null;
      coordinates = null;
      coords      = new StringBuilder();
      for (CSVRecord rec: data) {
	if (!oldTrack.equals(rec.get(KEY_TRACK))) {
	  if (coordinates != null)
	    coordinates.setTextContent(coords.toString());
	  coords = new StringBuilder();

	  oldTrack = rec.get(KEY_TRACK);
	  pm = doc.createElement("Placemark");
	  root.appendChild(pm);

	  // name
	  child = doc.createElement("name");
	  child.setTextContent(rec.get(KEY_TRACK));
	  pm.appendChild(child);

	  // extrude
	  child = doc.createElement("extrude");
	  child.setTextContent("1");
	  pm.appendChild(child);

	  // tessellate
	  child = doc.createElement("tessellate");
	  child.setTextContent("1");
	  pm.appendChild(child);

	  // altitudeMode
	  child = doc.createElement("altitudeMode");
	  child.setTextContent("absolute");
	  pm.appendChild(child);

	  // coordinates
	  coordinates = doc.createElement("coordinates");
	  pm.appendChild(coordinates);
	}

	if (coords.length() > 0)
	  coords.append(" ");

	coords.append(rec.get(KEY_LAT));
	coords.append(",");
	coords.append(rec.get(KEY_LON));
	coords.append(",");
	coords.append(rec.get(KEY_ELEVATION));
      }

      // trailing coordinates?
      if ((coordinates != null) && (coords.length() > 0))
	coordinates.setTextContent(coords.toString());

      return writeXML(doc, output);
    }
    catch (Exception e) {
      m_Logger.error("Failed to write: " + output, e);
      return "Failed to write: " + output + "\n" + e;
    }
  }
}
