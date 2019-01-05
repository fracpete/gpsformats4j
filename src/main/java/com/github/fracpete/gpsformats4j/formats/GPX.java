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

/*
 * GPX.java
 * Copyright (C) 2016-2019 FracPete
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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GPX format.
 *
 * @author FracPete (fracpete at gmail dot com)
 * @see Formats#allFormats()
 */
public class GPX
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
   * Create a dummy timestamp.
   *
   * @param index	the index of the item (used as seconds)
   * @return		the generated timestamp (yyyy-MM-dd'T'HH:mm:ss)
   */
  protected String dummyTimestamp(int index) {
    Calendar		cal;
    SimpleDateFormat	dformat;

    cal = new GregorianCalendar();
    cal.set(Calendar.YEAR, 2000);
    cal.set(Calendar.MONTH, 0);
    cal.set(Calendar.DAY_OF_MONTH, 1);
    cal.set(Calendar.HOUR, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.add(Calendar.SECOND, index);

    dformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    return dformat.format(cal.getTime());
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
    NodeList 			segs;
    NodeList			children;
    Element			seg;
    int				t;
    int				p;
    CSVRecord			rec;
    List<String>		values;
    Map<String,Integer> 	map;
    int				count;

    result = new ArrayList<>();
    try {
      doc   = readXML(input);
      segs  = doc.getElementsByTagName("trkseg");
      count = 0;
      map   = new HashMap<>();
      map.put(KEY_TRACK, 0);
      map.put(KEY_TIME, 1);
      map.put(KEY_LAT, 2);
      map.put(KEY_LON, 3);
      map.put(KEY_ELEVATION, 4);
      for (t = 0; t < segs.getLength(); t++) {
	segs = ((Element) segs.item(t)).getElementsByTagName("trkpt");
	for (p = 0; p < segs.getLength(); p++) {
	  count++;
	  values = new ArrayList<>();
	  seg    = (Element) segs.item(p);
	  // track
	  values.add("" + t);
	  // time
	  children = seg.getElementsByTagName("time");
	  if (children.getLength() > 0)
	    values.add(children.item(0).getTextContent().trim());
	  else
	    values.add(dummyTimestamp(p));
	  // lat
	  values.add(seg.getAttribute("lat").trim());
	  // lon
	  values.add(seg.getAttribute("lon").trim());
	  // elevation
	  children = seg.getElementsByTagName("ele");
	  if (children.getLength() > 0)
	    values.add(children.item(0).getTextContent().trim());
	  else
	    values.add("0");
	  // add record
	  rec = CSVRecordFactory.newRecord(values.toArray(new String[0]), map, null, count, -1);
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
    Document 	doc;
    Element 	child;
    Element 	gpx;
    Element 	track;
    Element 	seg;
    Element 	point;
    String	oldTrack;

    try {
      gpx = newDocument("gpx");
      doc = gpx.getOwnerDocument();
      gpx.setAttribute("xmlns", "http://www.topografix.com/GPX/1/1");
      gpx.setAttribute("xmlns:gpxx", "http://www.garmin.com/xmlschemas/GpxExtensions/v3");
      gpx.setAttribute("xmlns:gpxtpx", "\"http://www.garmin.com/xmlschemas/TrackPointExtension/v1");
      gpx.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
      gpx.setAttribute("xsi:schemaLocation", "http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd http://www.garmin.com/xmlschemas/GpxExtensions/v3 http://www.garmin.com/xmlschemas/GpxExtensionsv3.xsd http://www.garmin.com/xmlschemas/TrackPointExtension/v1 http://www.garmin.com/xmlschemas/TrackPointExtensionv1.xsd");
      gpx.setAttribute("creator", "gpsformats4j");
      oldTrack = "";
      seg      = null;
      for (CSVRecord rec: data) {
	if (!oldTrack.equals(rec.get(KEY_TRACK))) {
	  oldTrack = rec.get(KEY_TRACK);
	  track = doc.createElement("trk");
	  gpx.appendChild(track);
	  child = doc.createElement("name");
	  child.setTextContent(rec.get(KEY_TRACK));
	  track.appendChild(child);
	  seg = doc.createElement("trkseg");
	  track.appendChild(seg);
	}
	point = doc.createElement("trkpt");
	point.setAttribute("lat", rec.get(KEY_LAT));
	point.setAttribute("lon", rec.get(KEY_LON));
	seg.appendChild(point);
	child = doc.createElement("ele");
	child.setTextContent(rec.get(KEY_ELEVATION));
	point.appendChild(child);
	child = doc.createElement("time");
	child.setTextContent(rec.get(KEY_TIME));
	point.appendChild(child);
      }

      return writeXML(doc, output);
    }
    catch (Exception e) {
      m_Logger.error("Failed to write: " + output, e);
      return "Failed to write: " + output + "\n" + e;
    }
  }
}
