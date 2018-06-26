package main;

public final class PlayByPlayBuilder {

  private String gameId;
  private Integer eventNumber;
  private Integer eventMessageType;
  private Integer period;
  private Integer wcTime;
  private Integer pcTime;
  private Integer actionType;
  private Integer option1;
  private Integer option2;
  private Integer option3;
  private String teamId;
  private String person1;
  private String person2;
  private Integer teamIdType;

  private PlayByPlayBuilder() {
  }

  public static PlayByPlayBuilder aPlayByPlayBuilder() {
    return new PlayByPlayBuilder();
  }

  public PlayByPlayBuilder withGameId(String gameId) {
    this.gameId = gameId;
    return this;
  }

  public PlayByPlayBuilder withEventNumber(Integer eventNumber) {
    this.eventNumber = eventNumber;
    return this;
  }

  public PlayByPlayBuilder withEventMessageType(Integer eventMessageType) {
    this.eventMessageType = eventMessageType;
    return this;
  }

  public PlayByPlayBuilder withPeriod(Integer period) {
    this.period = period;
    return this;
  }

  public PlayByPlayBuilder withWcTime(Integer wcTime) {
    this.wcTime = wcTime;
    return this;
  }

  public PlayByPlayBuilder withPcTime(Integer pcTime) {
    this.pcTime = pcTime;
    return this;
  }


  public PlayByPlayBuilder withActionType(Integer actionType) {
    this.actionType = actionType;
    return this;
  }

  public PlayByPlayBuilder withOption1(Integer option1) {
    this.option1 = option1;
    return this;
  }

  public PlayByPlayBuilder withOption2(Integer option2) {
    this.option2 = option2;
    return this;
  }

  public PlayByPlayBuilder withOption3(Integer option3) {
    this.option3 = option3;
    return this;
  }

  public PlayByPlayBuilder withTeamId(String teamId) {
    this.teamId = teamId;
    return this;
  }

  public PlayByPlayBuilder withPerson1(String person1) {
    this.person1 = person1;
    return this;
  }

  public PlayByPlayBuilder withPerson2(String person2) {
    this.person2 = person2;
    return this;
  }

  public PlayByPlayBuilder withTeamIdType(Integer teamIdType) {
    this.teamIdType = teamIdType;
    return this;
  }

  public PlayByPlay build() {
    return new PlayByPlay(gameId, eventNumber, eventMessageType, period, wcTime, pcTime, actionType,
        option1, option2, option3, teamId, person1, person2, teamIdType);
  }
}
