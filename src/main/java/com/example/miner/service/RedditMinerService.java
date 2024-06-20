package com.example.miner.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import com.example.miner.model.RedditPost;
import com.example.miner.repository.RedditPostRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RedditMinerService {

   private static final String URL_DOMAIN = "https://www.reddit.com";
   private static final String SEARCH_PARAMS = "/r/memes/top/?sort=top&t=day";

   @Autowired
   private RedditPostRepository redditPostRepository;

   public List<RedditPost> getTopMemes() throws IOException {
      List<RedditPost> topMemes = new ArrayList<>();
      try {
         String redditUrl = URL_DOMAIN + SEARCH_PARAMS;
         Document document = Jsoup.connect(redditUrl).get();
         Elements posts = document.select("#main-content > div:nth-child(3) > shreddit-feed > article");

         String srcMorePosts = document.select("#main-content > div:nth-child(3) > shreddit-feed > faceplate-partial")
               .attr("src");
         String urlMorePosts = URL_DOMAIN + srcMorePosts;
         if (!srcMorePosts.isEmpty()) {
            document = Jsoup.connect(urlMorePosts).get();
            Elements morePosts = document.select("shreddit-app > faceplate-batch > article");
            posts.addAll(morePosts);
         }

         int index = 0;
         for (Element post : posts) {
            if (index == 20) {
               break;
            }
            RedditPost redditPost = new RedditPost();
            String redditId = post.select("shreddit-post").attr("id");
            String title = post.select("shreddit-post > a.absolute.inset-0 > faceplate-screen-reader-content").text();
            String href = post.select("shreddit-post > a.absolute").attr("href");
            String url = URL_DOMAIN + href;
            String image = post.select("shreddit-post").attr("content-href");
            int score = Integer.parseInt(post.select("shreddit-post").attr("score"));
            String author = post.select("shreddit-post").attr("author");

            redditPost.setRedditId(redditId);
            redditPost.setTitle(title);
            redditPost.setUrl(url);
            redditPost.setImage(image);
            redditPost.setScore(score);
            redditPost.setAuthor(author);
            redditPost.setDate(LocalDateTime.now());
            topMemes.add(redditPost);
            saveOrUpdateRedditPost(redditPost);
            index++;
         }

      } catch (IOException e) {
         e.printStackTrace();
      }
      return topMemes;
   }

   public void saveOrUpdateRedditPost(RedditPost redditPost) {
      Optional<RedditPost> existingPost = redditPostRepository.findByRedditId(redditPost.getRedditId());
      if (existingPost.isPresent()) {
         RedditPost existing = existingPost.get();
         existing.setScore(redditPost.getScore());
         existing.setDate(LocalDateTime.now());
         redditPostRepository.save(existing);
      } else {
         redditPostRepository.save(redditPost);
      }
   }

   public ByteArrayResource generateTop20Report() throws IOException {
      LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
      List<RedditPost> topPosts = redditPostRepository.findByDate(yesterday);

      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      PrintWriter writer = getPrintWriter(outputStream, topPosts);
      writer.flush();
      writer.close();

      return new ByteArrayResource(outputStream.toByteArray());
   }

   private static PrintWriter getPrintWriter(ByteArrayOutputStream outputStream, List<RedditPost> topPosts) {
      PrintWriter writer = new PrintWriter(outputStream);

      writer.println("Reddit ID, Title, Score, URL, Author, Image");

      for (RedditPost post : topPosts) {
         writer.printf("%s, %s, %d, %s, %s, %s%n",
               post.getRedditId(),
               post.getTitle(),
               post.getScore(),
               post.getUrl(),
               post.getAuthor(),
               post.getImage());
      }
      return writer;
   }
}
