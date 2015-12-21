/*
*    Copyright [2015] [wisemapping]
*
*   Licensed under WiseMapping Public License, Version 1.0 (the "License").
*   It is basically the Apache License, Version 2.0 (the "License") plus the
*   "powered by wisemapping" text requirement on every single page;
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the license at
*
*       http://www.wisemapping.org/license
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
*/

package com.wisemapping.model;

public class MindmapIcon implements Comparable{
    private String name;
    private IconFamily family;

    MindmapIcon(IconFamily family, String name) {
        this.name = name;
        this.family = family;
    }

    public String getName() {
        return name;
    }

    public IconFamily getFamily() {
        return family;
    }

    public String getId() {
        return family.name().toLowerCase() + "_" + name;
    }

    public int compareTo(Object o) {
        return ((MindmapIcon)o).getId().compareTo(this.getId());
    }
}
