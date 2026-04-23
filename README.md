BitThief

BitThief is an ongoing project of the Distributed Computing Group at ETH Zurich. The project deals with incentive problems in peer-to-peer filesharing systems.

The sources in this project are current as of January 2007 when I stopped coding on the project as part of my Master Thesis. ETH has since improved the client and the latest version (closed source) will always be available at http://bitthief.ethz.ch/.

BitThief is a byproduct of our HotNets-V paper "Free Riding in BitTorrent is Cheap" (http://www.disco.ethz.ch/publications/hotnets06.pdf).

Modernized build (Java 21 + Maven)

Requirements:
- Java 21
- Maven 3.9+

Common commands:
- Compile all modules: `mvn -q -DskipTests compile`
- Run all tests: `mvn test`
- Build jars: `mvn package`

Modules:
- `bitthief` -> core runtime/client (`ws.moor.bt.BitThief`)
- `simulation` -> network simulation tools (`ws.moor.bt.simulation.SimulationNetwork`)
- `grapher` -> stats charting/analysis tools (`ws.moor.bt.grapher.Grapher`)

Logging:
- Migrated from log4j 1.x to SLF4J + logback.
- Existing `logging.config` key remains and now points to `logback*.xml` resources.

Version metadata:
- Build revision is derived from git commit metadata during Maven build.
