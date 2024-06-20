package com.example.miner.controller;

import com.example.miner.model.RedditPost;
import com.example.miner.service.RedditMinerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class RedditMinerController {

    @Autowired
    private RedditMinerService redditMinerService;

    @GetMapping("/top-memes")
    public List<RedditPost> getTopMemes() throws IOException {
        return redditMinerService.getTopMemes();
    }
}