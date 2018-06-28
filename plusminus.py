'''
keep track of the five players on the court for each team
every time a bucket is scored, add/substract to their +/-
'''

MADE_SHOT=1
MADE_FT=3
SUB=8
START_PERIOD=12
END_PERIOD=13

data_folder = "basketball_analytics"

class Game:
  def __init__(self, id, teams={}):
    self.id = id
    self.teams = dict()
    for team in teams:
      self.teams[team.id] = team

  def __repr__(self):
    s = ""
    for team in self.teams:
      for player in self.teams[team].players:
        s += "{},{},{}\n".format(self.id, self.teams[team].players[player].id, self.teams[team].players[player].plus_minus)
    return s


class Team:
  def __init__(self, id, players={}):
    self.id = id
    self.players = dict()
    for player in players:
      self.players[player.id] = player

  def setActive(self, playerIds):
    for player in self.players:
      self.players[player].active = False
    for playerId in playerIds:
      self.players[playerId].active = True

class Player:
  def __init__(self, id, team_id):
    self.team_id = team_id
    self.id = id
    self.plus_minus = 0
    self.active = True # player is on the floor

def made_bucket(offensive_team, defensive_team, value):
  for player_id, player in offensive_team.players.iteritems():
    if player.active:
      player.plus_minus += value
  for player_id, player in defensive_team.players.iteritems():
    if player.active:
      player.plus_minus-= value

def convert_to_int(i):
  if i.isdigit():
    return int(i)
  return i

def init_period(lineup_data, game_id, period, game):
  active_players = dict() # team -> players
  for row in lineup_data:
    if row[0] != game_id or row[1] != period:
      continue
    person_id = row[2]
    team_id = row[3]
    if game == None:
      game = Game(game_id)
    if game.teams.get(team_id) == None:
      game.teams[team_id] = Team(team_id)
    if game.teams[team_id].players.get(person_id) == None:
      game.teams[team_id].players[person_id] = Player(person_id, team_id)
    if active_players.get(team_id) == None:
      active_players[team_id] = []
    active_players[team_id].append(person_id)
  for team in game.teams:
    game.teams[team].setActive(active_players[team])
  return game

def main():
  # first iterate through game lineup data to get the initial set of players on the floor
  lineup_data = []
  pbp_data = []
  with open("basketball_analytics/lineup_data.txt") as f:
    next(f)
    for line in f:
      lineup_data.append(map(convert_to_int, line.strip().split("\t")))
  with open("basketball_analytics/play_by_play.txt") as f:
    next(f)
    for line in f:
      pbp_data.append(map(convert_to_int, line.strip().split("\t")))

  pbp_data = [item for item in pbp_data if item[2] in [MADE_SHOT, MADE_FT, SUB, START_PERIOD, END_PERIOD]] # we only care about made shots, made free throws, substitutions and start of period

  game = None
  for row in pbp_data:
    game_id = row[0]
    event_type = row[2]
    period = row[3]
    option1 = row[7]
    team_id = row[10]
    person1 = row[11]
    person2 = row[12]

    # on "end period" events for period 4, write the existing game object to disk
    if event_type == END_PERIOD and period == 4:
      f = open('results.csv', 'w')
      f.write(str(game))

    # on "start period" events, call init_period to set up new players
    if event_type == START_PERIOD:
      game = init_period(lineup_data, game_id, period, game)

    if event_type == SUB: # person1 is subbing out, person2 is subbing in
      game.teams[team_id].players[person1].active = False
      if game.teams[team_id].players.get(person2) == None:
        game.teams[team_id].players[person2] = Player(person2, team_id)
      game.teams[team_id].players[person2].active = True

    if event_type == MADE_FT or event_type == MADE_SHOT:
      offensive_team = team_id
      for team in game.teams:
        if team != team_id:
          defensive_team = team
      made_bucket(game.teams[offensive_team], game.teams[defensive_team], option1)
main()