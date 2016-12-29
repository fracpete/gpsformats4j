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
 * CSVRecordFactory.java
 * Copyright (C) 2016 FracPete
 */

package org.apache.commons.csv;

import java.util.Map;

/**
 * Factory for CSVRecord instances.
 *
 * @author FracPete (fracpete at gmail dot com)
 */
public class CSVRecordFactory {

  /**
   * Instantiates a new record.
   *
   * @param values		the values
   * @param mapping		the mapping of column name to column index
   * @return			the record
   */
  public static CSVRecord newRecord(final String[] values, final Map<String, Integer> mapping) {
    return new CSVRecord(values, mapping, null, -1, -1);
  }

  /**
   * Instantiates a new record.
   *
   * @param values		the values
   * @param mapping		the mapping of column name to column index
   * @param comment		the comment
   * @param recordNumber	the record number
   * @param characterPosition	the character position
   * @return			the record
   */
  public static CSVRecord newRecord(final String[] values, final Map<String, Integer> mapping, final String comment, final long recordNumber, final long characterPosition) {
    return new CSVRecord(values, mapping, comment, recordNumber, characterPosition);
  }
}
