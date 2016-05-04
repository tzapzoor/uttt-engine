//============================================================================
// Name        : Utils.h
// Author      : CBTeamName
//============================================================================

#ifndef UTILS_H_
#define UTILS_H_

#include <chrono>
#include <cstdint>
#include <sstream>
#include <string>
#include <vector>
using namespace std;

inline vector<string> &split(const string &s, char delim,
                             vector<string> &elems) {
    stringstream ss(s);
    string item;
    elems.clear();
    while (getline(ss, item, delim)) {
        elems.push_back(item);
    }
    return elems;
}

inline int stringToInt(const string &s) {
    istringstream ss(s);
    int result;
    ss >> result;
    return result;
}

inline long timestamp() {
    chrono::milliseconds ms = chrono::duration_cast<chrono::milliseconds>(
        chrono::system_clock::now().time_since_epoch());
    return ms.count();
}

#endif /* UTILS_H_ */
