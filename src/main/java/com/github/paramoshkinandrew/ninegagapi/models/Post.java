package com.github.paramoshkinandrew.ninegagapi.models;
/*
 * This file is a part of com.github.paramoshkinandrew.ninegagapi.models as part of ninegagapi
 * Copyright (C) 2016 Andrew Paramoshkin <paramoshkin.andrew@gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import com.github.paramoshkinandrew.ninegagapi.types.ImageType;
import com.github.paramoshkinandrew.ninegagapi.types.VideoType;

import java.util.HashMap;

public class Post {
    private String id;
    private String title;
    private String url;
    private HashMap<ImageType, String> images;
    private HashMap<VideoType, String> videos;
    private boolean isContainsVideo;
    private int votes;
    private int comments;
    private String nextId;

    public Post(String id, String title, String url, HashMap<ImageType, String> images, HashMap<VideoType, String> videos, boolean isContainsVideo,
                int votes, int comments, String nextId) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.images = images;
        this.videos = videos;
        this.isContainsVideo = isContainsVideo;
        this.votes = votes;
        this.comments = comments;
        this.nextId = nextId;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public HashMap<ImageType, String> getImages() {
        return images;
    }

    public HashMap<VideoType, String> getVideos() {
        return videos;
    }

    public boolean isContainsVideo() {
        return isContainsVideo;
    }

    public int getVotes() {
        return votes;
    }

    public int getComments() {
        return comments;
    }

    public String getNextId() {
        return nextId;
    }

    @Override
    public String toString() {
        return String.format("[Post id: %s, title: %s]", this.id, this.title);
    }
}
