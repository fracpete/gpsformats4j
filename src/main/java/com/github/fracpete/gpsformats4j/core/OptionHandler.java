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
 * OptionHandler.java
 * Copyright (C) 2016 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.gpsformats4j.core;

/**
 * Interface for classes that handle options.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public interface OptionHandler {

  /**
   * For setting options.
   *
   * @param options	the options
   */
  public void setOptions(String[] options) throws Exception;

  /**
   * Returns the help string.
   *
   * @return		the help
   */
  public String toHelp();
}
