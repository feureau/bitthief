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

package ws.moor.bt.dht;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import ws.moor.bt.BitThiefConfiguration;
import ws.moor.bt.Environment;
import ws.moor.bt.util.LoggingUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * TODO(pmoor): Javadoc
 */
public class StandaloneTracker {

  private Environment environment;
  private DHTracker tracker;

  private static final File STATE_FILE = new File("/tmp/TrackerState");

  private static final Logger logger = LoggingUtil.getLogger(StandaloneTracker.class);

  private TrackerState getTrackerState() throws IOException {
    if (STATE_FILE.canRead()) {
      return TrackerState.createForFile(STATE_FILE);
    }
    return TrackerState.createNew(8031);
  }

  private class TrackerStateSaver implements Runnable {
    public void run() {
      try {
        logger.info("saving tracker state");
        TrackerState state = TrackerState.createForTracker(tracker);
        state.saveToFile(STATE_FILE);
        logger.info("saving done");
      } catch (IOException e) {
        logger.error("exception during state file saving", e);
      }
    }
  }

  private static void configureStaticStuff(BitThiefConfiguration configuration) throws IOException {
    String resourceName = configuration.getLoggingPropertyFile();
    URL resource = ClassLoader.getSystemResource(resourceName);
    if (resource == null) {
      throw new IOException("unable to load logging property file: " + resourceName);
    }

    LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
    JoranConfigurator configurator = new JoranConfigurator();
    configurator.setContext(context);
    context.reset();
    try {
      configurator.doConfigure(resource);
    } catch (JoranException e) {
      throw new IOException("unable to configure logging", e);
    }
  }

  public void run(String[] args) throws Exception {
    BitThiefConfiguration config = BitThiefConfiguration.fromPropertiesFile();
    configureStaticStuff(config);

    environment = new Environment(config);
    tracker = new DHTracker(environment, getTrackerState());
    environment.getScheduledExecutor().scheduleWithFixedDelay(new TrackerStateSaver(), 300, 600, TimeUnit.SECONDS);
  }

  public static void main(String[] args) throws Exception {
    new StandaloneTracker().run(args);
  }
}
