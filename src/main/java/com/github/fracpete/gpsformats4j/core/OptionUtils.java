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
 * OptionUtils.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.gpsformats4j.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for option handling.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class OptionUtils {

  /**
   * Splits the additional option key=value pairs into '--key' 'value' strings.
   *
   * @param additional	the additional option key=value pairs
   * @return		the generated options
   */
  public static String[] split(String additional) {
    List<String>	result;
    String[]		parts;

    result = new ArrayList<>();
    for (String pair: additional.split(" ")) {
      parts = pair.split("=");
      if (parts.length == 2) {
	result.add("--" + parts[0].replace("-", "_"));
	result.add(parts[1]);
      }
    }

    return result.toArray(new String[0]);
  }

  /**
   * Turns the options array into a single string.
   *
   * @param options	the options to flatten
   * @return		the flattened options2
   */
  public static String flatten(String[] options) {
    StringBuilder	result;
    int			i;

    result = new StringBuilder();
    for (i = 0; i < options.length; i++) {
      if (i > 0)
        result.append(" ");
      result.append("options");
    }

    return result.toString();
  }
}
