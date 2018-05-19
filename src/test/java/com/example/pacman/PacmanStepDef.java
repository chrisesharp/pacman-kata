package com.example.pacman;

import static org.junit.Assert.*;
import cucumber.api.java.en.*;
import cucumber.api.DataTable;
//import cucumber.api.PendingException;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class PacmanStepDef {
    Game game;
    int lives;
    int score;
    int columns;
    GameField gameField;
    Level level;
    String output;
    ByteArrayOutputStream result = new ByteArrayOutputStream();
    Display display;
    Map<String,byte[]> ANSIcodes = new HashMap<>();
    String command = "java -cp target/pacman-kata-1.0-SNAPSHOT.jar com.example.pacman.Game";
    List<String> commandArgs = new ArrayList<>();
    String serviceResponse;
    
    // Given steps
    
    @Given("^the command arg \"([^\"]*)\"$")
    public void the_command(String arg) {
        this.commandArgs.add(arg);
    }
    
    @Given("^the screen column width is (\\d+)$")
    public void the_screen_column_width_is(int cols) {
        this.columns = cols;
    }

    @Given("^the player has (\\d+) lives$")
    public void the_player_has_lives(int lives) {
        this.lives = lives;
    }

    @Given("^the player score is (\\d+)$")
    public void the_player_score_is(int score) {
        this.score = score;
    }

    @Given("^walls at the following places:$")
    public void walls_at_the_following_places(DataTable data) {
      List<List<String>> wallList = data.cells(1);
        for (List<String> wallspec: wallList ) {
          String icon = wallspec.get(0);
          int x = Integer.parseInt(wallspec.get(1));
          int y = Integer.parseInt(wallspec.get(2));
          GameToken token = new WallToken(icon);
          token.addGameElement(game, new Location(x,y)); 
        }
    }
    
    @Given("^the game field of (\\d+) x (\\d+)$")
    public void the_game_field_of_x(int x, int y) {
      game = new Game();
      gameField = new GameField(x,y);
      gameField.clear();
      game.setGameField(gameField);
    }

    @Given("^a pacman at (\\d+) , (\\d+) facing \"([^\"]*)\"$")
    public void a_pacman_at_facing(int x, int y, String direction) {
      Location.Direction dir;
      switch (direction) {
        case "LEFT":
          dir = Location.Direction.LEFT;
          break;
        case "RIGHT":
          dir = Location.Direction.RIGHT;
          break;
        case "UP":
          dir = Location.Direction.UP;
          break;
        default:
          dir = Location.Direction.DOWN;
      }
      GameToken pacmanTok = new PacmanToken(PacmanToken.getToken(dir));
      pacmanTok.addGameElement(game, new Location(x,y));
    }
    
    @Given("^a ghost at (\\d+) , (\\d+)$")
    public void a_ghost_at(int x, int y) {
        GhostToken ghostToken = new GhostToken("M");
        ghostToken.addGameElement(game, new Location(x,y));
    }
    
    @Given("^the game state is$")
    public void theGameStateIs(String state) throws Throwable {
        level = new Level(state);
        game = new Game(level);
    }

    @Given("^the game uses animation$")
    public void the_game_uses_animation() throws Throwable {
        game.animateIcons();
    }

    @Given("^a game with (\\d+) levels$")
    public void a_game_with_levels(int levels, String levelMaps) throws Throwable {
      level = new Level(levelMaps);
      level.setMaxLevel(levels);
      game = new Game(level);
    }

    @Given("^this is the last level$")
    public void this_is_the_last_level() throws Throwable {
        game.setMaxLevels(1);
        game.setLevel(1);
    }

    @Given("^this is level (\\d+)$")
    public void this_is_level(int level) throws Throwable {
        game.setLevel(level);
    }

    @Given("^the max level  is (\\d+)$")
    public void the_max_level_is(int level) throws Throwable {
        game.setMaxLevels(level);
    }

    @Given("^the score is (\\d+)$")
    public void the_score_is(int score) throws Throwable {
        game.setScore(score);
    }

    @Given("^the lives are (\\d+)$")
    public void the_lives_are(int lives) throws Throwable {
        game.setLives(lives);
    }

    @Given("^a game is polling for a key input$")
    public void aGameIsPollingForAKeyInput() throws Throwable {
        // Can't do anything here because of Java keyboard eventing
    }

    @Given("^a display$")
    public void a_display() throws Throwable {
      display = new MonoDisplay(result);
    }

    @Given("^a colour display$")
    public void a_colour_display() throws Throwable {
      display = new ColourDisplay(result);
    }

    @Given("^the ANSI \"(.*?)\" sequence is \"(.*?)\"$")
    public void the_ANSI_sequence_is(String sequence, String hex) throws Throwable {
        ANSIcodes.put(sequence, hexStringToByteArray(hex));
    }
    
    @Given("^the user is \"([^\"]*)\"$")
    public void the_user_is(String user) {
        game.setPlayer(user);
    }
    
    // When steps
    
    @When("^I run the command with the args$")
    public void i_run_the_command_with_the_args() {
      System.setOut(new PrintStream(result));
      Game.main(commandArgs.toArray(new String[0]));
      System.setOut(System.out);
    }
    
    @When("^we render the status line$")
    public void we_render_the_status_line() {
      try {
        result.write(GameStats.renderStatus(this.lives,
                                            this.score,
                                            this.columns).getBytes());
      } catch (Exception e) {
        System.out.println("Something weird happened here!");
      }
    }
    
    @When("^we render the game field$")
    public void we_render_the_game_field() {
      GameField field = game.getGameField();
      field.generateDisplayStream();
      try {
        result.write(field.getVideoStream().getBytes());
      } catch (Exception e) {
        System.out.println("Something weird happened here!");
      }
    }
    
    @When("^we parse the state$")
    public void weParseTheState() throws Throwable {
        game.parse();
    }

    @When("^we render the game$")
    public void weRenderTheGame() throws Throwable {
        output = game.render().getVideoStream();
    }

    @When("^we play (\\d+) turn")
    public void wePlayATurn(int turns) throws Throwable {
        Pacman pacman = (Pacman) game.getGameElementByType(Pacman.class);
        if (pacman != null) {
          pacman.go();
        }
        while (turns>0) {
            game.tick();
            turns--;
        }
    }

    @When("^the player presses \"([^\"]*)\"$")
    public void thePlayerPresses(char key) throws Throwable {
        Pacman pacman = (Pacman)game.getGameElementByType(Pacman.class);
        pacman.move(Keyboard.mapKeyDirection(key));
    }

    @When("^the game refreshes the display with the buffer \"(.*?)\"$")
    public void the_game_refreshes_the_display_with_the_buffer(String buffer) throws Throwable {
        ANSIcodes.put(buffer, hexStringToByteArray(stringToHexString(buffer)));
        DisplayStream video = new DisplayStream();
        video.writeVideo(buffer);
        display.refresh(video);
    }

    @When("^the display renders the icon \"(.*?)\" in yellow and refreshes$")
    public void the_dispay_renders_the_icon_and_refreshes_the_display(String icon) throws Throwable {
        ANSIcodes.put(icon, hexStringToByteArray(stringToHexString(icon)));
    }
    
    @When("^initialize the display$")
    public void initialize_the_display() {
      int width =0;
      int height =0;
      if (game != null) {
        width = game.getGameField().width();
        height = game.getGameField().height();
      }
      
      display.init(width, height);
    }
    
    @When("^we refresh the display with the buffer \"([^\"]*)\"$")
    public void we_refresh_the_display_with_the_buffer(String buffer) {
      ANSIcodes.put(buffer, hexStringToByteArray(stringToHexString(buffer))); 
      DisplayStream video = new DisplayStream();
      video.writeVideo(buffer);
      display.refresh(video);
    }
    
    @When("^I post the score to the scoreboard$")
    public void i_add_the_score() {
      game.postScore();
    }
    
    @When("^I get the scores$")
    public void i_get_the_scores() {
        serviceResponse = game.getScores().get(0);
    }
    
    // Then steps
    
    @Then("^I should get the following output:$")
    public void i_should_get_the_following_output(String expected) {
      String received = result.toString();
      received = received.replaceAll("\u001B\\[[\\d]*m", "");
      received = received.replaceAll("\u001B\\[H", "");
      received = received.replaceAll("\u001B\\[2J", "");
      received = received.replaceAll("\u001B\\[?5[h|l]", "");
      received = received.trim();
      assertEquals(expected, received);
    }

    @Then("^the game lives should be (\\d+)$")
    public void the_game_lives_should_be(int lives) throws Throwable {
        assertEquals(lives,game.getLives());
    }

    @Then("^the game score should be (\\d+)$")
    public void the_game_score_should_be(int score) throws Throwable {
        assertEquals(score, game.getScore());
    }

    @Then("^the game field should be (\\d+) x (\\d+)$")
    public void the_game_field_should_be_x(int x, int y) throws Throwable {
        GameField gameField = game.getGameField();
        assertEquals(x,gameField.width());
        assertEquals(y,gameField.height());
    }

    @Then("^pacman should be at (\\d+) , (\\d+)$")
    public void pacmanIsAt(int x, int y) throws Throwable {
        GameElement pacman = game.getGameElementByType(Pacman.class);
        Location thisLocation = new Location(x,y);
        assertEquals(thisLocation, pacman.location());
    }

    @Then("^ghost should be at (\\d+) , (\\d+)$")
    public void ghostIsAt(int x, int y) throws Throwable {
        Location thisLocation = new Location(x,y);
        GameElement aGhost = new Ghost(thisLocation);
        assertTrue(game.getGameElement(thisLocation).equals(aGhost));
    }

    @Then("^there should be a 50 point pill at (\\d+) , (\\d+)$")
    public void powerPillIsAt(int x, int y) throws Throwable {
        Location thisLocation = new Location(x,y);
        PowerPill aPowerPill = new PowerPill(thisLocation);
        assertTrue(game.getGameElement(thisLocation).equals(aPowerPill));
    }

    @Then("^there should be a 10 point pill at (\\d+) , (\\d+)$")
    public void tenPointPillIsAt(int x, int y) throws Throwable {
        Location thisLocation = new Location(x,y);
        Pill aPill = new Pill(thisLocation);
        assertTrue(game.getGameElement(thisLocation).equals(aPill));
    }

    @Then("^there should be a wall at (\\d+) , (\\d+)$")
    public void wallIsAt(int x, int y) throws Throwable {
        Location thisLocation = new Location(x,y);
        Wall aWall = new Wall(thisLocation);
        assertTrue(game.getGameElement(thisLocation).equals(aWall));
    }

    @Then("^the player should have (\\d+) lives$")
    public void thePlayerHasLives(int lives) throws Throwable {
        assertEquals(lives, game.getLives());
    }

    @Then("^the score should be (\\d+)$")
    public void thePlayerScoreIs(int score) throws Throwable {
        assertEquals(score, game.getScore());
    }

    @Then("^pacman should be facing \"([^\"]*)\"$")
    public void pacmanIsFacing(String direction) throws Throwable {
      Pacman pacman = (Pacman)game.getGameElementByType(Pacman.class);
        assertEquals(direction.toUpperCase(), pacman.direction().toString());
    }

    @Then("^the game screen should be$")
    public void theGameScreenIs(String expected) throws Throwable {
        assertEquals(expected, output);
    }

    @Then("^then pacman should go \"([^\"]*)\"$")
    public void thenPacmanGoes(String direction) throws Throwable {
      Pacman pacman = (Pacman)game.getGameElementByType(Pacman.class);
        assertEquals(direction.toUpperCase(), pacman.direction().toString());
    }

    @Then("^there should be a gate at (\\d+) , (\\d+)$")
    public void there_is_a_gate_at(int x, int y) throws Throwable {
        Location thisLocation = new Location(x,y);
        Gate theGate = new Gate(thisLocation);
        assertEquals(theGate,game.getGameElementByType(Gate.class));
    }

    @Then("^there should be a force field at (\\d+) , (\\d+)$")
    public void there_is_a_force_field_at(int x, int y) throws Throwable {
        Location thisLocation = new Location(x,y);
        ForceField field = new ForceField(thisLocation);
        assertTrue(game.getGameElement(thisLocation).equals(field));
    }

    @Then("^the display byte stream should be$")
    public void the_display_byte_stream_should_be(List<String> sequences) throws Throwable {
        List<byte[]> expected = new ArrayList<byte[]>();
        for (String sequence: sequences) {
          expected.add(ANSIcodes.get(sequence));
        }
        byte[] expectedBytes = flattenArrayListOfBytes(expected);
        printByteArray("expected", expectedBytes);
        byte[] resultingBytes = result.toByteArray();
        printByteArray("resulting", resultingBytes);

        assertArrayEquals(expectedBytes, resultingBytes);
    }
    
    @Then("^pacman should be dead$")
    public void pacman_is_dead() throws Throwable {
        assertTrue(game.getGameElementByType(Pacman.class).isDead());
    }  
    
    @Then("^pacman should be alive$")
    public void pacman_is_alive() throws Throwable {
        assertFalse(game.getGameElementByType(Pacman.class).isDead());
    }  
    
    @Then("^ghost at (\\d+) , (\\d+) should be calm$")
    public void ghost_at_should_be_calm(int x, int y) {
      Location thisLocation = new Location(x,y);
      Ghost theGhost = (Ghost)game.getGameElement(thisLocation);
      assertFalse(theGhost.panicked());
    }

    @Then("^ghost at (\\d+) , (\\d+) should be panicked$")
    public void ghost_at_should_be_panicked(int x, int y) {
      Location thisLocation = new Location(x,y);
      Ghost theGhost = (Ghost)game.getGameElement(thisLocation);
      assertTrue(theGhost.panicked());
    }   
    

    @Then("^the game dimensions should equal the display dimensions$")
    public void the_game_dimensions_should_equal_the_display_dimensions() {
        assertEquals(game.getGameField().width(),display.width());
        assertEquals(game.getGameField().height(),display.height());
    }
    
    @Then("^I should get the following response:$")
    public void i_should_get_the_following_response(String expected) {
        assertEquals(expected, serviceResponse);
    }
    

    public static byte[] hexStringToByteArray(String input) {
      int len = input.length();
      byte[] data = new byte[len / 2];
      for (int i = 0; i < len; i += 2) {
          data[i / 2] = (byte) ((Character.digit(input.charAt(i), 16) << 4)
                               + Character.digit(input.charAt(i+1), 16));
      }
      return data;
    }
    public static String stringToHexString(String input) {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < input.length(); i++) {
        int hex = (int) input.charAt(i);
          sb.append(Integer.toHexString(hex));
      }
      return sb.toString();
    }

    public static byte[] flattenArrayListOfBytes(List<byte[]> list) {
      return  list.stream()
                  .collect(
                    () -> new ByteArrayOutputStream(),
                    (b, e) -> {
                        try {
                            b.write(e);
                        } catch (Exception e1) {
                        }
                    },
                    (a, b) -> {}).toByteArray();
    }

    public static void printByteArray(String name, byte[] bytes) {
      System.out.println(name + " bytes:");
      for (byte b: bytes) {
        System.out.print("[" + b + "]");
      }
      System.out.println();
    }

}
