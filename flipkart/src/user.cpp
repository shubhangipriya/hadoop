#include<bits/stdc++.h>
#include "../header/user.h"
#include "../header/orders.h"
#include "./header/order.h"
//#include "C:/Users/Deepak/Documents/PROGRAMS/flipkart/header/user.h"
//#include "C:/Users/Deepak/Documents/PROGRAMS/flipkart/header/orders.h"
using namespace std;
void user::add(string email,bool bb)
{
    z.insert(make_pair(email,bb));
}
void user::blist(string email)
{
   if(z.find(email)==z.end())
        {
            cout<<"user dne";
        }
        z[email]=false;
        orders o;
        //o.removee(email);
}

bool user::isblist(string email)
{
    if(z.find(email)!=z.end())
    {
        return z[email];
    }
    return false;
}

/*class user
{
    string name;
    bool ustatus;
    map<string ,bool> z;
    void add(string email,bool blist)
    {
        z.insert(email,blist);
    }
    void blist(string email)
    {
        if(z.find(email)==z.end())
        {
            cout<<"user dne";
        }
        z[email]=false;
        orders o;
        o.removee(email);
    }
    void isblist(string email)
    {
        if(z.find(email)!=z.end())
        {
            return z[email];
        }
    }
};
*/

