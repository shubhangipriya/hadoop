//
//  main.cpp
//  germany
//
//  Created by Shubhangi1 Priya on 01/02/20.
//  Copyright © 2020 Shubhangi1 Priya. All rights reserved.
//

#include <iostream>
#include <map>
//#include <pair>
#include <limits>
#include <map>
#include <limits>
#include <iostream>
#include <assert.h>
#include<tuple>
using namespace std;

template<typename K, typename V>
class interval_map {
    std::map<K,V> m_map;
    public:
    // constructor associates whole range of K with val by inserting (K_min, val)
    // into the map
    interval_map( V const& val) {
        m_map.insert(m_map.end(),std::make_pair(std::numeric_limits<K>::lowest(),val));
    }
    // Assign value val to interval [keyBegin, keyEnd).
    // Overwrite previous values in this interval.
    // Conforming to the C++ Standard Library conventions, the interval
    // includes keyBegin, but excludes keyEnd.
    // If !( keyBegin < keyEnd ), this designates an empty interval,
    // and assign must do nothing.
    void assign( K const& keyBegin, K const& keyEnd, V const& val ) {
        
        // INSERT YOUR SOLUTION HERE
               if (!(keyBegin < keyEnd))
               {
                   return;
               }
               int counter = keyBegin - m_map[1];
               for (int i = counter; i >0 ; i--)
               {
                   m_map.insert(m_map.end(), std::make_pair(2+keyBegin- i, m_map[0]));
               }
               m_map[0] =  val;
               m_map[1] = keyBegin;
               if (keyEnd == m_map[std::numeric_limits<K>::lowest()])
               {
                   for (int i = m_map.size(); i > m_map[1]; i--)
                   {
                       m_map.insert(m_map.end(), std::make_pair(i, m_map[0]));
                   }

                   m_map.erase(0);
                   m_map.erase(1);
                   m_map.erase(std::numeric_limits<K>::lowest());
               }
        
//        if(!(keyBegin < keyEnd))
//            return;
//         //int  it;
//         //std::map<std::string, int>::iterator it =  m_map.insert();
//        typename std::map<K,V>::iterator it =m_map.begin();
//        for(int j=keyBegin; j<keyEnd; j++)
//        {
//            it = m_map.find(j);
//            if(it==m_map.end())
//            {
//              m_map.insert(pair<K,V>(j,val));
//            }
//            else
//            {
//              m_map.erase(it);
//              m_map.insert(pair<K,V>(j, val));
//            }
//          }
        }

            // look-up of the value associated with key
        V const& operator[]( K const& key ) const {
                return ( --m_map.upper_bound(key) )->second;
            }
        

        // Many solutions we receive are incorrect. Consider using a randomized test
        // to discover the cases that your implementation does not handle correctly.
        // We recommend to implement a test function that tests the functionality of
        // the interval_map, for example using a map of unsigned int intervals to char.
    void IntervalMapTest()
    {

         std::map<char,unsigned int> m_map;
            char c;

        m_map ['A']=0;
        m_map ['A']=1;
        m_map ['A']=2;

        for (c='A'; c<'B'; c++)
        {
            std::cout << c;
            if (m_map.count(c)>0)
                std::cout << " is an element of mymap.\n";
            else
                std::cout << " is not an element of mymap.\n";
        }

    }
};


int main() {

// given IntervalMapTest();

// What I did starts
//
//    interval_map <char, unsigned int> test1 ('K');
//    // instantiation with float and char type
//
//    interval_map <unsigned int, char> test2 (5);
//
//    test1.IntervalMapTest();
//     test2.IntervalMapTest();
    cout << " is not an element of mymap.\n";
    
    int count = 1 << 20;
    cout<<count;
    interval_map<int, char> m(8);
       m.assign(1, 3, 'B');
       m.assign(6, 8, 'C');


       return 0;

// What I did ends

}
**
if (!(keyBegin < keyEnd)) return;

std::pair<K,V> beginExtra;
std::pair<K,V> endExtra;
bool beginHasExtra = false;
bool endHasExtra = false;

typename std::map<K,V>::iterator itBegin;
itBegin = m_map.lower_bound(keyBegin);
if ( itBegin!=m_map.end() && keyBegin < itBegin->first ) {
    if (itBegin != m_map.begin()) {
        beginHasExtra = true;
        --itBegin;
        beginExtra = std::make_pair(itBegin->first, itBegin->second);
    }
    // openRange for erase is prevIterator
    // insert (prevIterator->first, prevIterator->second) as well!
}

typename std::map<K,V>::iterator itEnd;
itEnd = m_map.lower_bound(keyEnd);
if ( itEnd!=m_map.end() && keyEnd < itEnd->first ) {
    endHasExtra = true;
    typename std::map<K,V>::iterator extraIt = itEnd;
    --extraIt;
    endExtra = std::make_pair(keyEnd, extraIt->second);
    // closeRange for erase is this iterator
    // insert (keyEnd, prevIterator->second) as well!
}

// 4 canonical conflicts:
//   beginExtra w/ mid
//   before-mid w/ mid (beginHasExtra==false)
//   mid w/ endExtra
//   mid w/ after-mid (endHasExtra==false)

bool insertMid = true;
if (beginHasExtra) {
    if (beginExtra.second == val)
        insertMid = false;
} else {
    if (itBegin != m_map.begin()) {
        typename std::map<K,V>::iterator beforeMid = itBegin;
        --beforeMid;
        if (beforeMid->second == val)
            insertMid = false;
    }
}


if (endHasExtra) {
    if ( (insertMid && endExtra.second == val) || (!insertMid && endExtra.second == beginExtra.second) )
        endHasExtra = false;
} else {
    if ( (insertMid && itEnd!=m_map.end() && itEnd->second == val) || (!insertMid && itEnd!=m_map.end() && itEnd->second == beginExtra.second) )
        itEnd = m_map.erase(itEnd);
}

itBegin = m_map.erase(itBegin, itEnd);
if (beginHasExtra)
    itBegin = m_map.insert(itBegin, beginExtra);
if (insertMid)
    itBegin = m_map.insert(itBegin, std::make_pair(keyBegin, val));
if (endHasExtra)
    m_map.insert(itBegin, endExtra);

**
2.********************************************
if (!(keyBegin < keyEnd))
           return;
       // insert keyBegin
       V valEnd;
       bool x = true;
       auto start = m_map.lower_bound(keyBegin); // insertion position for keyBegin - O(logN)
       auto delStart = start;
       if (start != m_map.end() && start->first == keyBegin) // keyBegin already exists, change the value
       {
           valEnd = start->second;
           start->second = val;
           ++delStart;
           if (delStart == m_map.end())
               x = false;
       }
       else // start's key is bigger then keyBegin, so new element (with key keyBegin) should be inserted right before start
       {
           auto prev = start;
           --prev;
           valEnd = prev->second;
           if (prev->second != val) // not redundant
           {
               auto it = m_map.insert(start, std::make_pair(keyBegin, val)); // insert with hint - O(1)
               start = it;
               delStart = ++it;
               //if ((++it) != m_map.end() && it->second == val) // next is redundant
               //    m_map.x(it);
           }
       }

       // insert keyEnd
       auto end = m_map.lower_bound(keyEnd); // insertion position for keyEnd - O(logN)
       auto delFinish = end;
       if (delFinish == delStart)
           x = false;
       if (end == m_map.end() || end->first != keyEnd) // end's key is bigger then keyEnd, so new element (with key keyEnd and value of end's previous iterator) should be inserted right before end
       {
           auto prev = end;
           --prev;
           if (prev != start)
               valEnd = prev->second;
           if (valEnd != val) // not redundant
           {
               auto it = m_map.insert(end, std::make_pair(keyEnd, valEnd)); // insert with hint - O(1)
               delFinish = it;
               if ((++it) != m_map.end() && it->second == valEnd) // next is redundant
                   m_map.erase(it);
           }
       }
       if (x && delStart != m_map.end() && delFinish != m_map.end() && delStart->first < delFinish->first)
           m_map.erase(delStart, delFinish); // remove internal elements (intervals)
   
3.************************
if (!(keyBegin < keyEnd))
    return;

typename std::map<K, V>::iterator iterBegin; /*The new begin with val, can be begin()*/
typename std::map<K, V>::iterator iterEnd;   /*the new end of val, can be end()*/

auto lowerKeyBegin = m_map.lower_bound(keyBegin); //either end() or some iter whose key is not less than keyBegin. [1st O(logN)]
auto upperKeyEnd = m_map.upper_bound(keyEnd); //some iter where keyEnd < key, or end()  [2nd O(logN)]
auto prevKeyEnd = std::prev(upperKeyEnd);

/*
The next interval of the new interval starts at keyEnd if the previous value at keyEnd differed from val
*/
if (!(prevKeyEnd->second == val))
{
    // prevKeyEnd is either less than the new end we are inserting, or the same (no update to avoid copying from erased node)
    if (!(prevKeyEnd->first < keyEnd) && !(keyEnd < prevKeyEnd->first))
        iterEnd = prevKeyEnd;
    else
        iterEnd = m_map.insert_or_assign(upperKeyEnd, keyEnd, prevKeyEnd->second);
}
else
{
    iterEnd = upperKeyEnd;
}

/*
The new interval starts at keyBegin if the would-be previous interval has a different value.
Previous interval is either a key in the map less than keyBegin, or non-existent when lower_bound is m_map.begin()
The new interval's start is merged with previous interval, if the previous interval has the same value.
*/
if (lowerKeyBegin != m_map.begin())
{
    auto prevIter = std::prev(lowerKeyBegin); //safe when end(), because we always have at least one value
    if (!(prevIter->second == val))
    {
        iterBegin = m_map.insert_or_assign(lowerKeyBegin, keyBegin, val);
    }
    else iterBegin = prevIter;
}
else
{
    iterBegin = m_map.insert_or_assign(lowerKeyBegin, keyBegin, val);
}

/*
Erase all keys between the new begin and end (excluding) so that there is only one value after iterBegin
This is fine when iterEnd is end()
*/
{
    auto nextIterOfBegin = std::next(iterBegin);//somehow msvc doesn't support if-initialization
    if (nextIterOfBegin != m_map.end())
    {
        //I would be very interested in a smarter way to get rid of this part without additional storage ...
        m_map.erase(nextIterOfBegin, iterEnd);
    }
}

////debug - check canonical
//for (auto iter = m_map.begin(); iter != m_map.end(); ++iter)
//{
//  auto next = std::next(iter);
//  if (next != m_map.end() && iter->second == next->second)
//  {
//      throw;
//  }
//}
