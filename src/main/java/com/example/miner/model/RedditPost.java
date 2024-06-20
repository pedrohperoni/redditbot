package com.example.miner.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class RedditPost {

   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Id
   private Long id;
   @Column(unique = true)
   private String redditId;
   private String title;
   private String url;
   private String image;
   private int score;
   private String author;

   @Column()
   private LocalDateTime date;

   public String getRedditId() {
      return redditId;
   }

   public void setRedditId(String redditId) {
      this.redditId = redditId;
   }

   public String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String getUrl() {
      return url;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public String getImage() {
      return image;
   }

   public void setImage(String image) {
      this.image = image;
   }

   public int getScore() {
      return score;
   }

   public void setScore(int score) {
      this.score = score;
   }

   public String getAuthor() {
      return author;
   }

   public void setAuthor(String author) {
      this.author = author;
   }

   public LocalDateTime getDate() {
      return date;
   }

   public void setDate(LocalDateTime date) {
      this.date = date;
   }

}