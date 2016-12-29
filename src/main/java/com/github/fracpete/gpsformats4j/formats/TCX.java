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
 * TCX.java
 * Copyright (C) 2016 FracPete
 */

package com.github.fracpete.gpsformats4j.formats;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVRecordFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TCX format (proprietary Garmin XML format).
 *
 * @author FracPete (fracpete at gmail dot com)
 */
public class TCX
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
    List<CSVRecord>		result;
    Document 			doc;
    NodeList			tracks;
    NodeList			points;
    NodeList			children;
    int				t;
    int				p;
    CSVRecord			rec;
    List<String>		values;
    Map<String,Integer> 	map;
    int				count;

    result = new ArrayList<>();
    try {
      doc     = readXML(input);
      tracks  = doc.getElementsByTagName("Track");
      count   = 0;
      map     = new HashMap<>();
      map.put(KEY_TRACK, 0);
      map.put(KEY_TIME, 1);
      map.put(KEY_LAT, 2);
      map.put(KEY_LON, 3);
      map.put(KEY_ELEVATION, 4);
      for (t = 0; t < tracks.getLength(); t++) {
	points = ((Element) tracks.item(t)).getElementsByTagName("Trackpoint");
	for (p = 0; p < points.getLength(); p++) {
	  count++;
	  values = new ArrayList<>();
	  // track
	  values.add("" + t);
	  // time
	  children = ((Element) points.item(p)).getElementsByTagName("Time");
	  values.add(children.item(0).getTextContent().trim());
	  // lat
	  children = ((Element) points.item(p)).getElementsByTagName("LatitudeDegrees");
	  values.add(children.item(0).getTextContent().trim());
	  // lon
	  children = ((Element) points.item(p)).getElementsByTagName("LongitudeDegrees");
	  values.add(children.item(0).getTextContent().trim());
	  // elevation
	  children = ((Element) points.item(p)).getElementsByTagName("AltitudeMeters");
	  values.add(children.item(0).getTextContent().trim());
	  // add record
	  rec = CSVRecordFactory.newRecord(values.toArray(new String[values.size()]), map, null, count, -1);
	  result.add(rec);
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
    return false;
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
    return "Not supported";
  }
}
