package com.github.fracpete.gpsformats4j;/*
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
 * Convert.java
 * Copyright (C) 2016 FracPete
 */

import com.github.fracpete.gpsformats4j.core.BaseObject;
import com.github.fracpete.gpsformats4j.core.OptionHandler;
import com.github.fracpete.gpsformats4j.formats.Format;
import com.github.fracpete.gpsformats4j.formats.Formats;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.Namespace;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.util.List;

/**
 * Conversion class.
 *
 * @author FracPete (fracpete at gmail dot com)
 */
public class Convert
  extends BaseObject
  implements OptionHandler {

  public static final String INPUT_FILE = "in_file";

  public static final String INPUT_FORMAT = "in_format";

  public static final String OUTPUT_FILE = "out_file";

  public static final String OUTPUT_FORMAT = "out_format";

  /** the argument parser. */
  protected ArgumentParser parser;

  /** the input file. */
  protected File m_InputFile;

  /** the input format. */
  protected Class m_InputFormat;

  /** the output file. */
  protected File m_OutputFile;

  /** the output format. */
  protected Class m_OutputFormat;

  /**
   * Initializes the members.
   */
  @Override
  protected void initialize() {
    String[]	formats;
    int		i;

    super.initialize();

    formats = new String[Formats.allFormats().length];
    for (i = 0; i < Formats.allFormats().length; i++)
      formats[i] = Formats.allFormats()[i].getSimpleName();

    parser = ArgumentParsers.newArgumentParser(getClass().getSimpleName());

    parser.description("Converts GPS formats.");
    parser.defaultHelp(true);

    parser.addArgument("--" + INPUT_FILE)
      .metavar(INPUT_FILE)
      .type(String.class)
      .help("The input file to convert.");
    parser.addArgument("--" + INPUT_FORMAT)
      .metavar(INPUT_FORMAT)
      .type(String.class)
      .choices(formats)
      .help("The input format.");

    parser.addArgument("--" + OUTPUT_FILE)
      .metavar(OUTPUT_FILE)
      .type(String.class)
      .help("The output file to generate.");
    parser.addArgument("--" + OUTPUT_FORMAT)
      .metavar(OUTPUT_FORMAT)
      .type(String.class)
      .choices(formats)
      .help("The output format.");
  }

  /**
   * Sets the input file.
   * 
   * @param value	the file
   */
  public void setInputFile(File value) {
    m_InputFile = value;
  }

  /**
   * Returns the input file.
   * 
   * @return		the file
   */
  public File getInputFile() {
    return m_InputFile;
  }

  /**
   * Sets the input format.
   * 
   * @param value	the format
   */
  public void setInputFormat(Class value) {
    m_InputFormat = value;
  }

  /**
   * Returns the input format.
   * 
   * @return		the format
   */
  public Class getInputFormat() {
    return m_InputFormat;
  }

  /**
   * Sets the output file.
   * 
   * @param value	the file
   */
  public void setOutputFile(File value) {
    m_OutputFile = value;
  }

  /**
   * Returns the output file.
   * 
   * @return		the file
   */
  public File getOutputFile() {
    return m_OutputFile;
  }

  /**
   * Sets the output format.
   * 
   * @param value	the format
   */
  public void setOutputFormat(Class value) {
    m_OutputFormat = value;
  }

  /**
   * Returns the output format.
   * 
   * @return		the format
   */
  public Class getOutputFormat() {
    return m_OutputFormat;
  }
  
  /**
   * Sets the options.
   *
   * @param options	the options
   */
  @Override
  public void setOptions(String[] options) throws Exception {
    Namespace	ns;

    try {
      ns = parser.parseArgs(options);
    }
    catch (Exception e) {
      parser.printHelp();
      throw e;
    }

    setInputFile(new File(ns.getString(INPUT_FILE)));
    setInputFormat(Class.forName(Format.class.getPackage().getName() + "." + ns.getString(INPUT_FORMAT)));
    setOutputFile(new File(ns.getString(OUTPUT_FILE)));
    setOutputFormat(Class.forName(Format.class.getPackage().getName() + "." + ns.getString(OUTPUT_FORMAT)));
  }

  /**
   * Returns the help.
   *
   * @return		the help
   */
  @Override
  public String toHelp() {
    return parser.formatHelp();
  }

  /**
   * Performs the conversion.
   *
   * @return		null if successful, otherwise error message
   */
  public String execute() {
    Format		formatIn;
    Format		formatOut;
    List<CSVRecord>	data;

    if (!m_InputFile.exists())
      return "Input file does not exist: " + m_InputFile;
    if (m_InputFile.isDirectory())
      return "Input file points to a directory: " + m_InputFile;

    try {
      formatIn  = (Format) m_InputFormat.newInstance();
      formatOut = (Format) m_OutputFormat.newInstance();
    }
    catch (Exception e) {
      return "Error configurating formats: " + e.toString();
    }

    if (!formatIn.canRead())
      return "Input format does not support reading!";
    if (!formatOut.canWrite())
      return "Output format does not support writing!";

    data = formatIn.read(m_InputFile);
    if (data == null)
      return "Failed to read data from: " + m_InputFile;

    return formatOut.write(data, m_OutputFile);
  }

  /**
   * Executes the conversion.
   *
   * @param args	the conversion
   * @throws Exception	if something goes wrong, eg setting the options
   */
  public static void main(String[] args) throws Exception {
    Convert convert = new Convert();
    convert.setOptions(args);
    String msg = convert.execute();
    if (msg != null)
      System.err.println(msg);
  }
}
