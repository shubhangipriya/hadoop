//
//  main.cpp
//  transactionOverflow
//
//  Created by Shubhangi1 Priya on 27/03/20.
//  Copyright © 2020 Shubhangi1 Priya. All rights reserved.
//


#include <iostream>
#include <map>
//#include <pair>
//#include <multiset>
#include <set>
#include <limits>
#include <iostream>
#include <assert.h>
 //#include<unordered_multiset>
#include <set>
#include <iterator>
#include<tuple> // for tuple

using namespace std;
const int N = 1e6;
int balance[N];
int main()
{
    int i, n, m, u, v, w, poor, x, rich, y;
    cin >> n >> m;
    for(i=0; i<m; i++) {
        cin >> u >> v >> w;
        //u has to pay v an amount w
        balance[u] -= w;
        balance[v] += w;
    }
    
    multiset<tuple<int,int>> S;
    for(i=0; i<n; i++)
        if(balance[i] != 0) S.insert(make_tuple(balance[i], i));
       
    int count = 0;
    while(!S.empty()) {
        tie(poor, x) = *S.begin(); S.erase(S.begin());
        tie(rich, y) = *S.rbegin(); S.erase(prev(S.end()));
        int amount = min(-poor, rich);
        
        count++; //poor pays amount "amount" to rich
        printf("%d pays %d amount to %d\n", x, amount, y);
        poor += amount;
        rich -= amount;
        
        if (poor) S.insert(make_tuple(poor, x));
        if (rich) S.insert(make_tuple(rich, y));
    }
    
    cout << count << endl;
    
    
}
//© 2020 GitHub, Inc.
