package com.example.pacman;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class ArgParser {
  @Option(name="-f", aliases="--file", usage="Fully qualified path and name of level map txt file.")  
  private String fileName;  

  @Option(name="-c", aliases= {"--colour","--colour=true"}, usage="Use colour display")  
  private  boolean useColour;  
  
  @Option(name="-d", aliases= {"--debug","--debug=true"}, usage="Use debug mode to run only one frame tick")  
  private  boolean debug;  

  public ArgParser(String... args) {
      CmdLineParser parser = new CmdLineParser(this);
      try {
          parser.parseArgument(args);
      } catch (CmdLineException e) {
          System.err.println(e.getMessage());
          parser.printUsage(System.err);
          System.exit(-1);
      }
  }
  
  public String getMapFile() {
    return this.fileName;
  }
  
  public boolean hasMapFile() {
    return (this.fileName != null);
  }
  
  public boolean useColour() {
    return this.useColour;
  }
  
  public boolean getDebug() {
    return this.debug;
  }
}
