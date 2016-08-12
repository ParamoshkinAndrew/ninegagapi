package com.github.paramoshkinandrew.ninegagapi;
/*
 * This file is a part of com.github.paramoshkinandrew.ninegagapi as part of ninegagapi
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


import com.github.paramoshkinandrew.ninegagapi.classes.NineGagApiException;
import com.github.paramoshkinandrew.ninegagapi.classes.PostParser;
import com.github.paramoshkinandrew.ninegagapi.models.Channel;
import com.github.paramoshkinandrew.ninegagapi.models.Post;
import com.github.paramoshkinandrew.ninegagapi.types.ApiExceptionType;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

public class NineGagApi {
    public static final String POST_PATH_TEMPLATE = "http://9gag.com/gag/%s";
    public static final String RANDOM_PATH = "http://9gag.com/random";
    public static final String LIST_PATH_TEMPLATE = "http://9gag.com/%s";

    /**
     * Get post by id
     *
     * @param id - post identifier
     * @return - Post instance
     */
    public Post getById(String id) throws NineGagApiException {
        Document doc;
        try {
            doc = Jsoup.connect(String.format(POST_PATH_TEMPLATE, id)).get();
        } catch (IOException e) {
            // FIXME: 10/08/16 Remove stacktrace
            e.printStackTrace();
            ApiExceptionType type = ApiExceptionType.TopLevel;
            if (e.getClass() == HttpStatusException.class) {
                HttpStatusException exception = (HttpStatusException) e;
                if (exception.getStatusCode() == 404)
                    type = ApiExceptionType.NotFound;
            }
            throw new NineGagApiException(e.getMessage(), type);
        }
        return PostParser.parseOnePost(doc);
    }

    /**
     * Get random post
     *
     * @return - post instance
     * @throws NineGagApiException
     */
    public Post getRandom() throws NineGagApiException {
        Document doc;
        try {
            doc = Jsoup.connect(RANDOM_PATH).get();
        } catch (IOException e) {
            // FIXME: 10/08/16 Remove stacktrace
            e.printStackTrace();
            ApiExceptionType type = ApiExceptionType.TopLevel;
            throw new NineGagApiException(e.getMessage(), type);
        }
        return PostParser.parseOnePost(doc);
    }

    /**
     * Get post list from specific channel
     *
     * @param channel - Channel instance
     * @param ids     - list of latest ids (for continue)
     * @return - Array of Post instances
     * @throws NineGagApiException
     */
    public Post[] getPosts(Channel channel, String... ids) throws NineGagApiException {
        Document doc;
        try {
            String query = "";
            if (ids.length > 0) {
                query = "?id=" + String.join("%2C", ids);
            }
            doc = Jsoup.connect(String.format(LIST_PATH_TEMPLATE, channel.getName()) + query).get();
        } catch (IOException e) {
            // FIXME: 10/08/16 Remove stacktrace
            e.printStackTrace();
            ApiExceptionType type = ApiExceptionType.TopLevel;
            if (e.getClass() == HttpStatusException.class) {
                HttpStatusException exception = (HttpStatusException) e;
                if (exception.getStatusCode() == 404)
                    type = ApiExceptionType.NotFound;
            }
            throw new NineGagApiException(e.getMessage(), type);
        }
        List<Post> posts = PostParser.parsePostPage(doc);
        return posts.toArray(new Post[posts.size()]);
    }

    /**
     * Get post list
     *
     * @param ids - list of latest ids (for continue)
     * @return - Array of Post instances
     * @throws NineGagApiException
     */
    public Post[] getPosts(String... ids) throws NineGagApiException {
        return getPosts(Channel.fromString(""), ids);
    }

}
