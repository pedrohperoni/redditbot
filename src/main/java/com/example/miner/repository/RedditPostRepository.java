package com.example.miner.repository;

import com.example.miner.model.RedditPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RedditPostRepository extends JpaRepository<RedditPost, Long> {

    Optional<RedditPost> findByRedditId(String redditId);

    List<RedditPost> findTop20ByDateAfterOrderByScoreDesc(LocalDateTime date);

}