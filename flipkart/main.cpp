#include<bits/stdc++.h>
using namespace std;
#include "./header/user.h"
#include "./header/orders.h"
#include "./header/order.h"
int main()
{
    user u;
    orders o;
    int n;string email,pname;
    cin>>n;
    switch(n)
    {
       /* case 1:
        {
            cout<<"add product in catalog";
             cin>>s;
             c.addproduct(s);
        }
        case 2:
        {
            cin>>s;
            cout<<"remove from catalog";
            c.removee(s);
        } */
        case 3:
        {
            cout<<"purchase product";
            cin>>email>>pname;
            o.purchase(email,pname,u);
        }
        case 4:
        {
            cout<<"return product";
            cin>>email>>pname;
            o.returnn(email,pname);
        }
        case 5:
        {
            cout<<"blacklist user";
            cin>>email;
            u.blist(email);
        }
        case 6:
        {
            cout<<"display orders";
            o.display();
        }
        /*case 7:
        {
            cout<<"display catalog";
            c.display();
        }*/
    }
    return 0;

}
