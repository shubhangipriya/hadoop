package com.example.controller;

import com.example.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.service.ArticleService;

import java.util.List;

@RestController
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @RequestMapping(value = "/articles", method = RequestMethod.GET)
    @ResponseBody()
    public List<Article> getAllArticles()
    {
        return  articleService.getAllArticles();
    }

    @RequestMapping(value= "/articles/{id}", method = RequestMethod.GET)
    @ResponseBody()
    public Article getArticle(@PathVariable String id) {
        return  articleService.getArticle(id);
    }

    @RequestMapping(value= "/articles/", method = RequestMethod.POST)
    @ResponseBody()
    public void addArticle(@RequestBody Article article) {

        articleService.addArticle(article);
    }

    @RequestMapping(method=RequestMethod.PUT, value ="/articles/{id}")
    public void updateArticle(@RequestBody Article article,
                              @PathVariable String id  ) {

        articleService.updateArticle(id);

    }

    @RequestMapping(method=RequestMethod.DELETE, value ="/articles/{id}")
    public void updateArticle(@PathVariable String id  ) {

        articleService.deleteArticle(id);

    }



}
