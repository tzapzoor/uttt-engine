.PHONY: clean cleanest

CC      = g++
CFLAGS  = -std=c++11 -g -Wall -Wextra
OBJS = GlobalSettings.o Move.o Field.o BotParser.o BotFactory.o AbstractMinimax.o Minimax_Winning.o Bot_Winning.o Main.o

all: bot

bot: $(OBJS)
	$(CC) -o $@ $^ $(LDFLAGS)

BotFactory.o: BotFactory.cpp BotFactory.h
	$(CC) -c $(CFLAGS) $<
BotParser.o: BotParser.cpp BotParser.h
	$(CC) -c $(CFLAGS) $<
Field.o: Field.cpp Field.h
	$(CC) -c $(CFLAGS) $<
GlobalSettings.o: GlobalSettings.cpp GlobalSettings.h
	$(CC) -c $(CFLAGS) $<
Move.o: Move.cpp Move.h
	$(CC) -c $(CFLAGS) $<
AbstractMinimax.o: AbstractMinimax.cpp AbstractMinimax.h
	$(CC) -c $(CFLAGS) $<
Minimax_Winning.o: Minimax_Winning.cpp Minimax_Winning.h
	$(CC) -c $(CFLAGS) $<
Bot_Winning.o: Bot_Winning.cpp Bot_Winning.h
	$(CC) -c $(CFLAGS) $<
Main.o: Main.cpp
	$(CC) -c $(CFLAGS) $<

clean:
	rm *.o
	rm bot
