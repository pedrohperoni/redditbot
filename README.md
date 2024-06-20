# Reddit Miner API
This is a Spring Boot API that scrapes /r/memes top posts for the last 24h, stores them in a MySQL database, and provides an endpoint to generate a CSV report of the top 20 memes from the past 24 hours.

## Features
* Scrape Reddit posts and save them to a MySQL database.
* Retrieve the top 20 Reddit posts from the past 24 hours, sorted by score.
* Generate and download a CSV report of the top posts.


## Endpoints

#### GET/top-memes

This endpoint returns the top posts from a subreddit and stores them in MySQL database. Currently it captures /r/memes by default but you can change the url and it will capture any subreddit. 

```
    {
        "redditId": "t3_1dk0dfa",
        "title": "Wtf did the rocks do",
        "url": "https://www.reddit.com/r/memes/comments/1dk0dfa/wtf_did_the_rocks_do/",
        "image": "https://i.redd.it/k27yvey5wm7d1.jpeg",
        "score": 32226,
        "author": "TransportationFuzzy3",
        "date": "2024-06-20T20:19:13.0291371"
    },
```

#### GET/top-20-report-today

This endpoint generates a CSV file download with the following columns: Reddit ID, Title, Score, Date, URL, Author and Image from the top 20 posts in the last 24 hours.


### Database Schema

```
CREATE TABLE reddit_post (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reddit_id VARCHAR(255) UNIQUE NOT NULL,
    title VARCHAR(255) NOT NULL,
    url VARCHAR(255) NOT NULL,
    image VARCHAR(255),
    score INT,
    author VARCHAR(255),
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```
