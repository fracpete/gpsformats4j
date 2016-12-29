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
 * CSV.java
 * Copyright (C) 2016 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.gpsformats4j.formats;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV format.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class CSV
  extends AbstractFormat {

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
    CSVParser		parser;

    result = new ArrayList<>();
    try {
      parser = CSVParser.parse(input, Charset.defaultCharset(), CSVFormat.DEFAULT);
      for (CSVRecord rec: parser)
	result.add(rec);
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
   */
  @Override
  public String write(List<CSVRecord> data, File output) {
    CSVPrinter	printer;
    FileWriter	writer;

    writer  = null;
    printer = null;
    try {
      writer = new FileWriter(output);
      printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
      for (CSVRecord rec: data)
	printer.printRecord(rec);
      printer.flush();
      printer.close();
    }
    catch (Exception e) {
      m_Logger.error("Failed to write: " + output, e);
      return "Failed to write: " + output + "\n" + e;
    }
    finally {
      IOUtils.closeQuietly(writer);
      IOUtils.closeQuietly(printer);
    }

    return null;
  }
}
