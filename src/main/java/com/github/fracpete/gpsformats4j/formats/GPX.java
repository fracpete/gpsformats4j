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
 * GPX.java
 * Copyright (C) 2016 FracPete
 */

package com.github.fracpete.gpsformats4j.formats;

import org.apache.commons.csv.CSVRecord;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.List;

/**
 * GPX format.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
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
    DocumentBuilderFactory 	factory;
    DocumentBuilder 		builder;
    Document 			doc;
    Element 			child;
    Element 			gpx;
    Element 			track;
    Element 			seg;
    Element 			point;
    String			oldTrack;

    try {
      factory = DocumentBuilderFactory.newInstance();
      builder = factory.newDocumentBuilder();
      doc     = builder.newDocument();
      gpx     = doc.createElement("gpx");
      gpx.setAttribute("xmlns", "http://www.topografix.com/GPX/1/1");
      gpx.setAttribute("xmlns:gpxx", "http://www.garmin.com/xmlschemas/GpxExtensions/v3");
      gpx.setAttribute("xmlns:gpxtpx", "\"http://www.garmin.com/xmlschemas/TrackPointExtension/v1");
      gpx.setAttribute("creator", "gpsformats4j");
      gpx.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
      gpx.setAttribute("xsi:schemaLocation", "http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd http://www.garmin.com/xmlschemas/GpxExtensions/v3 http://www.garmin.com/xmlschemas/GpxExtensionsv3.xsd http://www.garmin.com/xmlschemas/TrackPointExtension/v1 http://www.garmin.com/xmlschemas/TrackPointExtensionv1.xsd");
      doc.appendChild(gpx);
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
