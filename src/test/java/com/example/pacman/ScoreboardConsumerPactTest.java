package com.example.pacman;

import com.example.pacman.utils.*;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;

import org.junit.Rule;
import org.junit.Test;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
public class ScoreboardConsumerPactTest  {

    @Rule
    public PactProviderRuleMk2 mockProvider = new PactProviderRuleMk2("scoreboard_provider", this);
    
    @Pact(provider="scoreboard_provider", consumer="scoreboard_consumer")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        Map<String, String> postReq = new HashMap<>();
        postReq.put("Content-Type", "application/json");
        postReq.put("accept", "text/plain");
        Map<String, String> postResp = new HashMap<>();
        postResp.put("Content-Type", "text/plain");
        Map<String, String> getReq = new HashMap<>();
        getReq.put("accept", "application/json");
        Map<String, String> getResp = new HashMap<>();
        getResp.put("Content-Type", "application/json");
        return builder
            .given("empty scoreboard")
              .uponReceiving("ScoreboardConsumerPactTest test add score.")
                  .path("/scores")
                  .method("POST")
                  .body("{\n\"player\": \"david\",\n\"score\": 8000\n}")
                  .headers(postReq)
              .willRespondWith()
                  .status(200)
                  .headers(postResp)
                  .body("Thanks")
            .given("scoreboard with low score")
              .uponReceiving("ScoreboardConsumerPactTest test add higher score.")
                  .path("/scores")
                  .method("POST")
                  .body("{\n\"player\": \"chris\",\n\"score\": 10000\n}")
                  .headers(postReq)
              .willRespondWith()
                  .status(200)
                  .headers(postResp)
                  .body("Thanks")
            .given("scoreboard with two scores")
              .uponReceiving("ScoreboardConsumerPactTest test get scores.")
                  .method("GET")
                  .headers(getReq)
                  .path("/scores")
                  .body("")
              .willRespondWith()
                  .status(200)
                  .headers(getResp)
                  .body("[\n{\n\"player\": \"chris\",\n\"score\": 10000\n},\n{\n\"player\": \"david\",\n\"score\": 8000\n}\n]")
            .toPact();
    }

    @Test
    @PactVerification("scoreboard_provider")
    public void runTest() throws IOException {
      String mockURL = mockProvider.getUrl();
      ScoreboardClient testClient = new ScoreboardClient("chris", mockURL);
      testClient.addScore(10000);
      testClient.setPlayer("david");
      testClient.addScore(8000);
      List<String> received = testClient.scores();
      List<String> expected = new ArrayList<>();
      expected.add("chris:10000");
      expected.add("david:8000");
      assertEquals(expected, received);
    }
}