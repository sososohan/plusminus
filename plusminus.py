'''
keep track of the five players on the court for each team
every time a bucket is scored, add/substract to their +/-
'''

import collections

MADE_SHOT=1
MADE_FT=3
SUB=8
START_PERIOD=12
END_PERIOD=13

SubstitutionEvent = collections.namedtuple('SubstitutionEvent', ['person1', 'person2', 'game'])

class Game:
  def __init__(self, id, teams={}):
    self.id = id
    self.teams = {team.id: team for team in teams}

  def __repr__(self):
    s = ""
    for team in self.teams.itervalues():
      for player in team.players:
        s += "{},{},{}\n".format(self.id, team.players[player].id, team.players[player].plus_minus)
    return s


class Team:
  def __init__(self, id, players={}):
    self.id = id
    self.players = {player.id: player for player in players}

  def set_active(self, player_ids):
    for player_id in self.players:
      self.players[player_id].active = True if player_id in player_ids else False


class Player:
  def __init__(self, id, team_id):
    self.team_id = team_id
    self.id = id
    self.plus_minus = 0
    self.active = True # player is on the floor

def made_bucket(offensive_team, defensive_team, value):
  for player in offensive_team.players.itervalues():
    if player.active:
      player.plus_minus += value
  for player in defensive_team.players.itervalues():
    if player.active:
      player.plus_minus -= value

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
    game.teams[team].set_active(active_players[team])
  return game

def proccess_buffered_substitution_events(substitution_event):
  for team_id_iter, team in substitution_event.game.teams.iteritems():
    if team.players.get(substitution_event.person1) != None:
      team_id = team_id_iter  # override team_id because it's set incorrectly on the SUB events
  substitution_event.game.teams[team_id].players[substitution_event.person1].active = False
  if substitution_event.game.teams[team_id].players.get(substitution_event.person2) == None:
    substitution_event.game.teams[team_id].players[substitution_event.person2] = Player(substitution_event.person2, team_id)
  substitution_event.game.teams[team_id].players[substitution_event.person2].active = True

def main():
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

  pbp_data = [item for item in pbp_data if item[2] in [MADE_SHOT, MADE_FT, SUB, START_PERIOD, END_PERIOD]] # we only care about made shots, made free throws, substitutions, and starts and ends of periods

  game = None
  buffered_substitution_events = []
  for idx, row in enumerate(pbp_data):
    game_id = row[0]
    event_type = row[2]
    period = row[3]
    option1 = row[7]
    team_id = row[10]
    person1 = row[11]
    person2 = row[12]

    if event_type == SUB: # person1 is subbing out, person2 is subbing in. person1 should already be registered as part of his team because he's already on the floor. only process sub before next event
      buffered_substitution_events.append(SubstitutionEvent(person1, person2, game))
    elif event_type != MADE_FT:
      for substitution_event in buffered_substitution_events:
        proccess_buffered_substitution_events(substitution_event)
      buffered_substitution_events = []

    # on "end period" events when the next event is for period 1 (or there are no more events), write the existing game object to disk because this game is over
    if event_type == END_PERIOD and (idx+1 == len(pbp_data) or pbp_data[idx+1][3] == 1):
      f = open('results2.csv', 'a')
      f.write(str(game))
      game = None # reset the game object that gets passed to init_period

    # on "start period" events, call init_period to set up new players
    if event_type == START_PERIOD:
      game = init_period(lineup_data, game_id, period, game)

    if event_type == MADE_FT or event_type == MADE_SHOT:
      offensive_team = team_id
      for team in game.teams:
        if team != team_id:
          defensive_team = team
      made_bucket(game.teams[offensive_team], game.teams[defensive_team], option1)
main()
