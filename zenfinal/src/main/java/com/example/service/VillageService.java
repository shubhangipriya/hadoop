package com.example.service;

import com.example.model.Article;
import com.example.model.Consumption;
import com.example.model.ConsumptionReport;
import com.example.model.Village;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VillageService {

    List<Consumption> consumptionList = new ArrayList<Consumption>(Arrays.asList(
            new Consumption(1,10000.123),
            new Consumption(2,12345.123),
            new Consumption(3,23456.123)
    ));

    List<Village> villageList = new ArrayList<Village>(Arrays.asList(
            new Village(1,"Villarriba"),
            new Village(2,"Villabajo"),
            new Village(3,"Villarriba")
    ));

    public  List<Consumption> getAllConsumptionWithIds()
    {
        return consumptionList;
    }

    public Village getVillageData(int id)
    {
        for(int i=0;i<villageList.size();i++)
        {
            if(villageList.get(i).getId()== id)
            {
                return villageList.get(i);
            }
        }
        Village x = new Village(0, "null");
        return x;
    }

    public void addConsumptionData(Consumption article)
    {
        consumptionList.add(article);
    }

    public List<ConsumptionReport> getConsumptionReport()
    {
        List<ConsumptionReport> cc = new ArrayList<ConsumptionReport>();

        for(int i=0;i<consumptionList.size();i++)
        {
            for(int j=0;j<villageList.size();j++)
            {
                if(consumptionList.get(i).getId() == villageList.get(j).getId())
                {
                    ConsumptionReport x = new ConsumptionReport("0",0.0);
                    x.setAmount(consumptionList.get(i).getAmount());
                    x.setVillageName(villageList.get(j).getVillage_name());
                    cc.add(x);
                }
            }
        }


        return cc;
    }




}
