package com.example.miner.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.miner.model.RedditPost;
import com.example.miner.repository.RedditPostRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RedditMinerService {

    private static final String URL_DOMAIN = "https://www.reddit.com";
    private static final String SEARCH_PARAMS = "/r/memes/top/?sort=top&t=day";
   //  https://www.reddit.com/svc/shreddit/community-more-posts/top/?sort=top&t=DAY&name=memes

   @Autowired
    private RedditPostRepository redditPostRepository;
    
    public List<RedditPost> getTopMemes() throws IOException {
        String redditUrl = URL_DOMAIN + SEARCH_PARAMS;
        Document document = Jsoup.connect(redditUrl).get();
        Elements posts = document.select("#main-content > div:nth-child(3) > shreddit-feed > article");

        int index = 0;
        List<RedditPost> topMemes = new ArrayList<>();

        for (Element post : posts) {
            if (index >= 20) {
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
            topMemes.add(redditPost);
            saveOrUpdateRedditPost(redditPost);
            index++;
        }
        return topMemes;
    }

    public void saveOrUpdateRedditPost(RedditPost redditPost) {
        Optional<RedditPost> existingPost = redditPostRepository.findByRedditId(redditPost.getRedditId());
        if (existingPost.isPresent()) {
            RedditPost existing = existingPost.get();
            existing.setScore(redditPost.getScore());
            redditPostRepository.save(existing);
        } else {
            redditPostRepository.save(redditPost);
        }
    }
}
