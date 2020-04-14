#include<bits/stdc++.h>
#include "../header/user.h"
#include "../header/orders.h"
#include "../header/order.h"
using namespace std;

void orders::returnn(string email,string pname)
{
        user u;
        //if(u.isblist("kkk") )
        {
            cout<<"kkkk";
        }
        if(u.z.find(email)== u.z.end())
        {
            cout<<"No user";
        }
       else if(u.z.find(email)!= u.z.end()  && u.z[email]==true)
        {
            cout<<"user is blacklisted";
        }
        vector<order> tt= plist[email];
        order ord;
        for(int i = 0;i<tt.size();i++)
        {
           /* ord = (*i);
            if(ord.pname==pname)
            {
                index=i;
            }*/
        }
        ord.orderstatus="Cancelled";
}

void orders::purchase(string email,string pname, user &uu)
{
        user u;
        bool d = false;
       // string d;
        if(u.z.find(email)!= u.z.end() && u.z[email]==true )
        {
            cout<<"user is blacklisted";
        }
        else if(u.z.find(email)== u.z.end())
        {
            uu.add(email,d);
        }
        order ord;
        ord.email=email;
        ord.orderstatus="Booked";
        ord.pname=pname;
        ord.quantity=1;
        ord.ustatus=false;
        if(plist.find(email)!= plist.end())
        {
           // plist.insert(make_pair(email,ord));
           plist[email].push_back(ord);
        }
        else
        {
           //plist.insert(make_pair(email,ord));
           plist[email].push_back(ord);
        }
}

void orders::display()
{
    for(int i=0;i<plist.size();i++)
        {
            /*for(i=0;plist->second.begin();i++)
            {

            }*/
        }
}

/*
class orders
{
    map<string,vector<order> > plist;
    void return(string email,string pname)
    {
        user u;
        if(u.z[email]==NULL)
        {
            cout<<"No user";
        }
        else if(u.z[email]==true)
        {
            cout<<"user is blacklisted";
        }
        vector<order> tt= plist[email];
        order ord;
        for(int i = 0;i<tt.size();i++)
        {
            ord = (*i);
            if(ord.pname==pname)
            {
                index=i;
            }
        }
        ord.orderstatus="Cancelled";

    }
    void purchase(string email,string pname)
    {
        user u;
        if(u.z[email]==true)
        {
            cout<<"user is blacklisted";
        }
        else if(u.z[email]==NULL)
        {
            u.add(email,false);
        }
        order ord;
        ord.email=email;
        ord.orderstatus="Booked";
        ord.pname=pname;
        ord.quantity=1;
        ord.ustatus=false;
        if(plist[email]!=NULL)
        {
            plist[email].push_back(ord);
        }
        else
        {
           plist.insert(email,ord);
        }
    }
    void display()
    {
        for(i=0;i<plist.size();i++)
        {
            for(i=0;plist->second.begin();i++)
            {

            }
        }
    }
};
*/
