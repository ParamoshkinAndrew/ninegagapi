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


public class Channel {
    private String name;

    private Channel(String name) {
        this.name = name;
    }

    /**
     * Create channel by name
     *
     * @param name - channel name
     * @return - channel instance
     */
    public static Channel fromString(String name) {
        return new Channel(name);
    }

    public String getName() {
        return name;
    }
}
