package main;

import java.util.HashSet;

public class Game {   // I was just gonna iterate through and keep separate state per game...havent put any thought into it tho
  String gameId;
  HashSet<String> currentPlayersA;
  HashSet<String> currentPlayersB;

  HashSet<Player> teamA;
  HashSet<Player> teamB;

  String teamIdA;
  String teamIdB;

  public Game(String gameId) {
    this.gameId = gameId;
    this.currentPlayersA = new HashSet<>();
    this.currentPlayersB = new HashSet<>();
    this.teamA = new HashSet<>();
    this.teamB = new HashSet<>();
  }
}
