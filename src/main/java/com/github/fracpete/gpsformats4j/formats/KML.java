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
import org.apache.commons.csv.CSVRecordFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * KML format (Keyhole Markup Language).
 * https://developers.google.com/kml/documentation/kmlreference
 *
 * Only handles absolute altitudes.
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
    return true;
  }

  /**
   * Reads the file.
   *
   * @param input	the input file
   * @return		the collected data, null in case of an error
   */
  @Override
  public List<CSVRecord> read(File input) {
    List<CSVRecord>	result;
    Document 		doc;
    String		track;
    NodeList		pms;
    NodeList		list;
    Element		pm;
    Element 		coordinates;
    int			i;
    int			c;
    String[]		coords;
    String[]		parts;
    CSVRecord		rec;
    List<String>	values;
    Map<String,Integer> map;
    Calendar		cal;
    SimpleDateFormat	df;
    int			count;

    result = new ArrayList<>();
    df     = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    count  = 0;
    map    = new HashMap<>();
    map.put(KEY_TRACK, 0);
    map.put(KEY_TIME, 1);
    map.put(KEY_LAT, 2);
    map.put(KEY_LON, 3);
    map.put(KEY_ELEVATION, 4);
    try {
      doc  = readXML(input);
      pms  = doc.getElementsByTagName("Placemark");
      for (i = 0; i < pms.getLength(); i++) {
	pm   = (Element) pms.item(i);
	list = pm.getElementsByTagName("name");
	if (list.getLength() == 1)
	  track = list.item(0).getTextContent().trim();
	else
	  track = "" + i;
	cal = new GregorianCalendar();
	cal.set(Calendar.YEAR, 2000);
	cal.set(Calendar.MONTH, Calendar.JANUARY);
	cal.set(Calendar.DAY_OF_MONTH, 1);
	cal.set(Calendar.HOUR_OF_DAY, 0);
	cal.set(Calendar.MINUTE, 0);
	cal.set(Calendar.SECOND, 0);
	list = pm.getElementsByTagName("coordinates");
	for (c = 0; c < list.getLength(); c++) {
	  coordinates = (Element) list.item(c);
	  coords      = coordinates.getTextContent().split(" ");
	  for (String coord: coords) {
	    if (coord.trim().isEmpty())
	      continue;
	    parts = coord.trim().split(",");
	    if (parts.length == 3) {
	      count++;
	      cal.add(Calendar.SECOND, 1);
	      values = new ArrayList<>();
	      values.add(track);
	      values.add(df.format(cal.getTime()));
	      values.addAll(Arrays.asList(parts));
	      // add record
	      rec = CSVRecordFactory.newRecord(values.toArray(new String[values.size()]), map, null, count, -1);
	      result.add(rec);
	    }
	  }
	}
      }
    }
    catch (Exception e) {
      m_Logger.error("Failed to read: " + input, e);
      return null;
    }

    return result;
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
