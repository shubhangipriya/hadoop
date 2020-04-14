#include<bits/stdc++.h>
#include "./order.h"
#include "./user.h
#ifndef ORDERS_H_
#define ORDERS_H_
using namespace std;
class orders
{
    public:
        user uu;
        map<string,vector<order> > plist;
        void returnn(string email,string pname);
        void purchase(string email,string pname,user &uu);
        void display();
};
#endif
