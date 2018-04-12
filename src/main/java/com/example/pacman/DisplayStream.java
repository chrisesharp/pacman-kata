package com.example.pacman;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import com.example.pacman.Colours.Colour;

public class DisplayStream {
  private String videoBuffer = "";
  private List<Colour> colourBuffer = new ArrayList<>();
  
  public String getVideoStream() {
    return videoBuffer;
  }
  
  public Colour[] getColourStream() {
    return colourBuffer.toArray(new Colour[colourBuffer.size()]);
  }
  
  public void writeVideo(String data) {
    videoBuffer += data;
  }
  
  public void writeColour(Colour data) {
    colourBuffer.add(data);
  }
  
  public void writeColour(Colour[] data) {
    colourBuffer.addAll(Arrays.asList(data));
  }
}
