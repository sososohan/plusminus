package main;

public class PlayByPlay {

  public PlayByPlay(String gameId, Integer eventNumber, Integer eventMessageType,
      Integer period, Integer wcTime, Integer pcTime, Integer actionType, Integer option1, Integer option2,
      Integer option3, String teamId, String person1, String person2, Integer teamIdType) {
    this.gameId = gameId;
    this.eventNumber = eventNumber;
    this.eventMessageType = eventMessageType;
    this.period = period;
    this.wcTime = wcTime;
    this.pcTime = pcTime;
    this.actionType = actionType;
    this.option1 = option1;
    this.option2 = option2;
    this.option3 = option3;
    this.teamId = teamId;
    this.person1 = person1;
    this.person2 = person2;
    this.teamIdType = teamIdType;
  }

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

  public String getGameId() {
    return gameId;
  }

  public Integer getEventNumber() {
    return eventNumber;
  }

  public Integer getEventMessageType() {
    return eventMessageType;
  }

  public Integer getPeriod() {
    return period;
  }

  public Integer getWcTime() {
    return wcTime;
  }

  public Integer getPcTime() {
    return pcTime;
  }

  public Integer getActionType() {
    return actionType;
  }

  public Integer getOption1() {
    return option1;
  }

  public Integer getOption2() {
    return option2;
  }

  public Integer getOption3() {
    return option3;
  }

  public String getTeamId() {
    return teamId;
  }

  public String getPerson1() {
    return person1;
  }

  public String getPerson2() {
    return person2;
  }

  public Integer getTeamIdType() {
    return teamIdType;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("PlayByPlay{");
    sb.append("gameId='").append(gameId).append('\'');
    sb.append(", eventNumber='").append(eventNumber).append('\'');
    sb.append(", eventMessageType='").append(eventMessageType).append('\'');
    sb.append(", period='").append(period).append('\'');
    sb.append(", wcTime='").append(wcTime).append('\'');
    sb.append(", pcTime='").append(pcTime).append('\'');
    sb.append(", actionType='").append(actionType).append('\'');
    sb.append(", option1='").append(option1).append('\'');
    sb.append(", option2='").append(option2).append('\'');
    sb.append(", option3='").append(option3).append('\'');
    sb.append(", teamId='").append(teamId).append('\'');
    sb.append(", person1='").append(person1).append('\'');
    sb.append(", person2='").append(person2).append('\'');
    sb.append(", teamIdType='").append(teamIdType).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
