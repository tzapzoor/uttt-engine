//============================================================================
// Name        : GlobalSettings.h
// Author      : CBTeamName
//============================================================================

#ifndef GLOBAL_SETTINGS_H_
#define GLOBAL_SETTINGS_H_
#include <cstdint>

class GlobalSettings {
   public:
    static GlobalSettings& getInstance();
    int getBotId();
    void setBotId(int botId);
    int getOpponentId();
    void setTimebank(int timebank);
    int getTimebank();

   private:
    int botId;
    int opponentId;
    int timebank;

    GlobalSettings(){};
    GlobalSettings(GlobalSettings const&);
    void operator=(GlobalSettings const&);
};

#endif /* GLOBAL_SETTINGS_H_ */
