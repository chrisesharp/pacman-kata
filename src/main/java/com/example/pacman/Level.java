package com.example.pacman;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class Level {
  private List<String> levelMap = new ArrayList<String>();
  private int lastLevel = 1;
  private int currentLevel =1;

  public Level() {
  }

  public Level(String levelMap) {
      addLevelMaps(levelMap);
  }

  private void addLevelMaps(String levelMap) {
    for (String map: levelMap.split("\nSEPARATOR\n")) {
      this.levelMap.add(map);
    }
  }

  public void readFile(String filePath) {
    StringBuilder contentBuilder = new StringBuilder();

    try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8))
    {
      stream.forEach(s -> contentBuilder.append(s).append("\n"));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    addLevelMaps(contentBuilder.toString());

  }

  public void nextLevel() {
    currentLevel = (currentLevel<lastLevel) ? currentLevel+1 : lastLevel;
  }

  public String getLevelMap() {
    return (currentLevel < levelMap.size()) ? levelMap.get(currentLevel) :  levelMap.get(levelMap.size()-1);
  }

  public void  setMaxLevel(int lastLevel) {
      this.lastLevel = lastLevel;
  }

  public void  setLevel(int currentLevel) {
      this.currentLevel = currentLevel;
  }

  public boolean last() {
    return (currentLevel == lastLevel);
  }
}
