package com.github.paramoshkinandrew.ninegagapi.classes;
/*
 * This file is a part of com.github.paramoshkinandrew.ninegagapi.classes as part of ninegagapi
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


import com.github.paramoshkinandrew.ninegagapi.NineGagApi;
import com.github.paramoshkinandrew.ninegagapi.models.Post;
import com.github.paramoshkinandrew.ninegagapi.types.ApiExceptionType;
import com.github.paramoshkinandrew.ninegagapi.types.ImageType;
import com.github.paramoshkinandrew.ninegagapi.types.VideoType;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostParser {
    // Map with association image type - url size string
    private static final HashMap<ImageType, String> imageSizeDefiner;

    static {
        imageSizeDefiner = new HashMap<ImageType, String>();
        imageSizeDefiner.put(ImageType.SMALL, "_220x145");
        imageSizeDefiner.put(ImageType.COVER, "_460c");
        imageSizeDefiner.put(ImageType.NORMAL, "_460s");
        imageSizeDefiner.put(ImageType.LARGE, "_700b");
    }

    // Map with association video type - mime type
    private static final HashMap<VideoType, String> videoMimes;

    static {
        videoMimes = new HashMap<VideoType, String>();
        videoMimes.put(VideoType.MP4, "video/mp4");
        videoMimes.put(VideoType.WEBM, "video/webm");
    }

    /**
     * Parsing one 9GAG post
     *
     * @param doc - Element node
     * @return Post instance
     * @throws NineGagApiException
     */
    public static Post parseOnePost(Element doc) throws NineGagApiException {
        // Parameters
        String title = "";
        boolean isContainsVideo = false;
        String id = "";
        // Setting basic content
        Element contentContainer = doc.getElementsByClass("badge-entry-container").first();
        HashMap<ImageType, String> imageUrls = new HashMap<ImageType, String>();
        HashMap<VideoType, String> videoUrls = new HashMap<VideoType, String>();
        int votes, comments;
        String nextId = "";
        if (contentContainer != null) {
            // Setting title
            Element titleElement = contentContainer.getElementsByClass("badge-item-title").first();
            if (titleElement != null) {
                title = titleElement.text();
            }
            // Image element
            Element imageElement = contentContainer.getElementsByClass("badge-item-img").first();
            // Video element
            Element videoElement = contentContainer.getElementsByTag("video").first();
            // Setting up video
            if (videoElement != null) {
                imageElement = new Element(Tag.valueOf("img"), "");
                imageElement.attr("src", videoElement.attr("poster"));
                videoUrls = generateVideoUrls(videoElement);
                isContainsVideo = true;
            }
            // Setting up images
            if (imageElement != null) {
                imageUrls = generateImageUrls(imageElement);
            }
            id = contentContainer.attr("data-entry-id");
            votes = Integer.parseInt(contentContainer.attr("data-entry-votes"));
            comments = Integer.parseInt(contentContainer.attr("data-entry-comments"));
            nextId = getNextId(doc);

        } else {
            throw new NineGagApiException("9GAG structure changed", ApiExceptionType.ParsingError);
        }
        return new Post(id, title, String.format(NineGagApi.POST_PATH_TEMPLATE, id), imageUrls, videoUrls, isContainsVideo, votes, comments, nextId);
    }

    /**
     * Parse post page
     *
     * @param doc - Document instance
     * @return - list of Post instances
     */
    public static List<Post> parsePostPage(Document doc) {
        List<Post> out = new ArrayList<Post>();
        Elements list = doc.select("article[id^=jsid-entry-entity-]");
        for (Element postElement : list) {
            try {
                Post post = parseOnePost(postElement);
                out.add(post);
            } catch (NineGagApiException e) {
                e.printStackTrace();
            }
        }
        return out;
    }

    /**
     * Parse next post ID
     *
     * @param doc - Element instance
     * @return - next post ID
     */
    private static String getNextId(Element doc) {
        Element nextPostIdElement = doc.select(".badge-fast-entry.badge-next-post-entry.next").first();
        if (nextPostIdElement != null)
            return nextPostIdElement.attr("data-entry-key");
        else
            return "";
    }

    /**
     * Generating HashMap with video types
     *
     * @param videoElement - video node
     * @return - HashMap with video urls
     */
    private static HashMap<VideoType, String> generateVideoUrls(Element videoElement) {
        HashMap<VideoType, String> out = new HashMap<VideoType, String>();
        for (VideoType type : VideoType.values()) {
            Element sourceElement = videoElement.select(String.format("source[type=%s]", videoMimes.get(type))).first();
            String videoUrl = "";
            if (sourceElement != null)
                videoUrl = sourceElement.attr("src");
            out.put(type, videoUrl);
        }
        return out;
    }

    /**
     * Generating HashMap with different types of images
     *
     * @param imageElement - image node
     * @return - HashMap with images
     */
    private static HashMap<ImageType, String> generateImageUrls(Element imageElement) {
        HashMap<ImageType, String> out = new HashMap<ImageType, String>();
        String initialUrl = imageElement.attr("src");
        // Define initial type
        ImageType initialType = null;
        for (ImageType type : ImageType.values()) {
            if (initialUrl.contains(imageSizeDefiner.get(type))) {
                initialType = type;
                break;
            }
        }
        for (ImageType type : ImageType.values()) {
            if (initialType == null) {
                out.put(type, initialUrl);
            } else {
                if (type != initialType) {
                    int index = initialUrl.lastIndexOf(imageSizeDefiner.get(initialType));
                    out.put(type, new StringBuilder(initialUrl)
                            .replace(index, index + imageSizeDefiner.get(initialType).length(), imageSizeDefiner.get(type))
                            .toString());
                } else
                    out.put(type, initialUrl);
            }
        }
        return out;
    }
}
