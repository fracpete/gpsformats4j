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
 * Format.java
 * Copyright (C) 2016 FracPete
 */
package com.github.fracpete.gpsformats4j.formats;

import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.util.List;

/**
 * Interface for formats.
 *
 * @author FracPete (fracpete at gmail dot com)
 */
public interface Format {

  /** the key for the track. */
  public final static String KEY_TRACK = "Track";

  /** the key for the time. */
  public final static String KEY_TIME = "Time";

  /** the key for the longitude. */
  public final static String KEY_LON = "Longitude";

  /** the key for the latitiude. */
  public final static String KEY_LAT = "Latitude";

  /** the key for the elevation. */
  public final static String KEY_ELEVATION = "Elevation";

  /**
   * Returns whether reading is supported.
   *
   * @return		true if supported
   */
  public boolean canRead();

  /**
   * Reads the file.
   *
   * @param input	the input file
   * @return		the collected data, null in case of an error
   */
  public List<CSVRecord> read(File input);

  /**
   * Returns whether writing is supported.
   *
   * @return		true if supported
   */
  public boolean canWrite();

  /**
   * Writes to a file.
   *
   * @param data	the data to write
   * @param output	the output file
   * @return		null if successful, otherwise error message
   */
  public String write(List<CSVRecord> data, File output);
}
