NineGagAPI
===================

This library allow java application to access [9gag.com](http://9gag.com) posts. 

Features
--------
 - Get random post
 - Get post by id
 - Get 10 posts from main page feed or any channels you want
 - Continue feeds (getting next 10)

Accessible post attributes
--------------------------

 - **id** - post identifier
 - **title** - post caption
 - **url** - absolute path to post
 - **images** - list of images urls (in 4 sizes)
 - **isContainsVideo** - is post contains video
 - **videos** - list of videos urls (web and mp4)
 - **votes** - count of votes
 - **comments** - count of comments
 - **nextId** - next post id

-------

## Usage

Initialisation
```
NineGagApi nineGagApi = new NineGagApi();
```

Getting random post
```
Post randomPost = nineGagApi.getRandom();
```

Get post by id
```
Post postById = nineGagApi.getById("SomeId");
```

Get list of posts from default channel (10 posts)
```
Post[] tenPosts = nineGagApi.getPosts();
```

Get list of next posts from default channel (another 10 posts)
```
Post[] tenPosts = nineGagApi.getPosts("eighthPostId", "ninthPostId", "tenthPostId");
```

Get list of posts from specific channel (10 posts)
```
Post[] tenPosts = nineGagApi.getPosts(Channel.fromString("hot"));
```

------
## License
This software is licensed under the Apache 2 license