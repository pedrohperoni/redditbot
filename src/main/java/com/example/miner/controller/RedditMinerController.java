package com.example.miner.controller;

import com.example.miner.model.RedditPost;
import com.example.miner.service.RedditMinerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

   @GetMapping("/top-20-report-today")
   public ResponseEntity<ByteArrayResource> getTop20Report() throws IOException {
      ByteArrayResource resource = redditMinerService.generateTop20Report();

      return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=top20.csv")
            .contentType(MediaType.parseMediaType("application/csv"))
            .contentLength(resource.contentLength())
            .body(resource);
   }
}