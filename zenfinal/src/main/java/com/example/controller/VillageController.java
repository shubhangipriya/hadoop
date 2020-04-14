package com.example.controller;

import com.example.model.Article;
import com.example.model.Consumption;
import com.example.model.ConsumptionReport;
import com.example.model.Village;
import com.example.service.ArticleService;
import com.example.service.VillageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.List;

public class VillageController {

    @Autowired
    private VillageService villageService;


    @RequestMapping(value= "/consumption_report", method = RequestMethod.GET)
    @ResponseBody()
    public List<ConsumptionReport> getConsumptionReport(@QueryParam("duration") String duration) {
        return  villageService.getConsumptionReport();
    }

    @RequestMapping(value= "/village", method = RequestMethod.GET)
    @ResponseBody()
    public Village getVillageData(@QueryParam("id") int id) {
        return  villageService.getVillageData(id);
    }

    @RequestMapping(value= "/articles", method = RequestMethod.POST)
    @ResponseBody()
    public void getConsumptionData(@RequestBody Consumption consumption) {

        villageService.addConsumptionData(consumption);
    }



}
