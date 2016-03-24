#!/usr/bin/python2.7
###############################################################################
#                            pub.theaigames.com
#                    Ultimate Tic Tac Toe Auto Challenge
#
# Requirements:
#               * python2.7
#               * robobrowser (sudo easy_install robobrowser)
###############################################################################

from getpass import getpass
from robobrowser import RoboBrowser
import os
import requests
import random
import time
import sys

SITE_URL = "http://pub.theaigames.com/"
CHALLENGE_API = SITE_URL + 'competitions/ultimate-tic-tac-toe/game/challenge/<bot_name>/'

print("TheAiGames Bot Auto-Challenger")
print("")

username = raw_input("Username: ")
password = getpass()
challenge_count = 1

while (True):
    browser = RoboBrowser(parser='lxml')
    browser.open(SITE_URL)

    # loop forever
    #try catch this
    signin_form = browser.get_forms()[0]
    signin_form['login'].value = username
    signin_form['password'].value = password
    browser.submit_form(signin_form)

    #get the leaderboard list
    browser.follow_link(browser.get_link(text='Leaderboard'))
    bot_name_tags = browser.find_all('div', {'class': 'bot-name'});
    bot_name_extracter = lambda tag: tag.string.replace('\t', '').replace('\n', '').lower()
    bot_names = map(bot_name_extracter, bot_name_tags)
    no_bots = len(bot_names)

    our_rank = bot_names.index('cbteamname') + 1
    print("[INFO] CBTeamName is ranked " + str(our_rank))

    random.seed(os.urandom(8))
    opponent_queue = []
    #three bots with lower rank
    opponent_queue += ([bot_names[random.randint(our_rank + 1, no_bots - 1)],
                        bot_names[random.randint(our_rank + 1, no_bots - 1)],
                        bot_names[random.randint(our_rank + 1, no_bots - 1)]])
    #one bot with a higher rank
    opponent_queue += ([bot_names[random.randint(0, our_rank - 1)]])

    for bot_name in opponent_queue:
        #challenge the opponent
        print("[" + str(challenge_count) + "] Challenging \"" + bot_name + "\"... "),
        try:
            gotoUrl = CHALLENGE_API.replace('<bot_name>', bot_name)+'new'
            browser.open(gotoUrl)
            time.sleep(5)
            challenge_count += 1
            print("DONE")
        except requests.exceptions.ConnectionError:
            print("ERROR")

        #loop wait for the game to finish and get the result
        #not working for now
        #for i in range(0, 10):
        #    try:
        #        browser.open(CHALLENGE_API.replace('<bot_name>', bot_name)+'refresh')
        #        print(browser.url)
        #        if(browser.url != CHALLENGE_API.replace('<bot_name>', bot_name)+'refresh'):
        #            print("Game finished!")
        #            break
        #    except requests.exceptions.ConnectionError:
        #        print("Could not connect!")
        #    time.sleep(5)

        #get the json data of the match and the IO dumps

        #do stuff with the data

        #sleep for 6 minutes
        print("Sleeping for 6 minutes.")
        time.sleep(6*60)

    print("Done with the first batch. Sleeping for 15 minutes")
    time.sleep(15*60)
