#include<bits/stdc++.h>
#ifndef USER_H_
#define USER_H_
using namespace std;
class user
{
    public:
        string name;
        //vector<int> kk;
        bool ustatus;
        map<string ,bool> z;
        //vector<string > z;
        void add(string email,bool blist);
        //void add(string email);
        void blist(string email);
        bool isblist(string email);
};
#endif
