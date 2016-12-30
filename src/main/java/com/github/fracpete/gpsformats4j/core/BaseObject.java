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
 * BaseObject.java
 * Copyright (C) 2016 FracPete
 */

package com.github.fracpete.gpsformats4j.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Ancestor for objects.
 *
 * @author FracPete (fracpete at gmail dot com)
 */
public class BaseObject {

  /** the logger instance. */
  protected Logger m_Logger;

  /**
   * Default constructor.
   */
  public BaseObject() {
    super();
    initialize();
    finishInit();
  }

  /**
   * Initializes members.
   */
  protected void initialize() {
    m_Logger = LoggerFactory.getLogger(getClass());
  }

  /**
   * Finishes the initialization.
   */
  protected void finishInit() {
  }
}
