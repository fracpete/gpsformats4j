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
 * AbstractFormat.java
 * Copyright (C) 2016 FracPete
 */
package com.github.fracpete.gpsformats4j.formats;

import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

public abstract class AbstractFormat {

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

  /** the logger instance. */
  protected Logger m_Logger;

  /**
   * Initializes the format.
   */
  protected AbstractFormat() {
    m_Logger = LoggerFactory.getLogger(getClass());
  }

  /**
   * Returns whether reading is supported.
   *
   * @return		true if supported
   */
  public abstract boolean canRead();

  /**
   * Reads the file.
   *
   * @param input	the input file
   * @return		the collected data, null in case of an error
   */
  public abstract List<CSVRecord> read(File input);

  /**
   * Returns whether writing is supported.
   *
   * @return		true if supported
   */
  public abstract boolean canWrite();

  /**
   * Writes to a file.
   *
   * @param data	the data to write
   * @param output	the output file
   * @return		null if successful, otherwise error message
   */
  public abstract String write(List<CSVRecord> data, File output);
}
