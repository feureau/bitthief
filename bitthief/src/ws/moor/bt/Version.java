/*
 * BitThief - A Free Riding BitTorrent Client
 * Copyright (C) 2006 Patrick Moor <patrick@moor.ws>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package ws.moor.bt;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * TODO(pmoor): Javadoc
 */
public class Version {

  private static int major;
  private static int minor;
  private static int tiny;

  private static String revision;

  private static String shortString;
  private static String longString;

  static {
    loadProperties();
  }

  private static void loadProperties() {
    loadVersionProperties();
    loadRevisionProperties();
    fallbackToManifestRevision();
    constructVersionStrings();
  }

  private static void loadVersionProperties() {
    try (InputStream stream = ClassLoader.getSystemResourceAsStream("version.properties")) {
      if (stream != null) {
        Properties versionProperties = new Properties();
        versionProperties.load(stream);
        extractVersions(versionProperties);
      }
    } catch (IOException ignored) {
    }
  }

  private static void loadRevisionProperties() {
    try (InputStream stream = ClassLoader.getSystemResourceAsStream("revision.properties")) {
      if (stream != null) {
        Properties revisionProperties = new Properties();
        revisionProperties.load(stream);
        extractRevision(revisionProperties);
      }
    } catch (IOException ignored) {
    }
  }

  private static void fallbackToManifestRevision() {
    if (revision == null || revision.length() == 0 || "exported".equals(revision)) {
      Package versionPackage = Version.class.getPackage();
      if (versionPackage != null) {
        String build = versionPackage.getImplementationVersion();
        if (build != null && build.length() > 0) {
          revision = build;
        }
      }
    }
  }

  private static void constructVersionStrings() {
    StringBuilder builder = new StringBuilder();
    builder.append(major);
    builder.append(".");
    builder.append(minor);
    builder.append(".");
    builder.append(tiny);
    shortString = builder.toString();

    if (revision != null) {
      builder.append("-");
      builder.append(revision);
    }
    longString = builder.toString();
  }

  private static void extractRevision(Properties properties) {
    String value = properties.getProperty("version.revision");
    if (value != null) {
      revision = value.trim();
    }
  }

  private static void extractVersions(Properties properties) {
    major = Integer.parseInt(properties.getProperty("version.major"));
    minor = Integer.parseInt(properties.getProperty("version.minor"));
    tiny = Integer.parseInt(properties.getProperty("version.tiny"));
  }

  public static int getMajor() {
    return major;
  }

  public static int getMinor() {
    return minor;
  }

  public static int getTiny() {
    return tiny;
  }

  public static String getRevision() {
    return revision;
  }

  public static String getShortVersionString() {
    return shortString;
  }

  public static String getLongVersionString() {
    return longString;
  }
}
