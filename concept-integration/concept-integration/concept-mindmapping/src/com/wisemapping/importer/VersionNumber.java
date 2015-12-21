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

package com.wisemapping.importer;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class VersionNumber
        implements Comparable<VersionNumber> {

    protected String version_d;

    //~ Constructors .........................................................................................

    public VersionNumber(final String version) {
        version_d = version;
    }

    //~ Methods ..............................................................................................

    /**
     * Answers whether the receiver is greater then the given version number.
     *
     * @param versionNumber the version number to compare to
     * @return true if the receiver has a greater version number, false otherwise
     */
    public boolean isGreaterThan(final VersionNumber versionNumber) {
        return this.compareTo(versionNumber) > 0;
    }

    /**
     * Answers whether the receiver is smaller then the given version number.
     *
     * @param versionNumber the version number to compare to
     * @return true if the receiver has a smaller version number, false otherwise
     */
    public boolean isSmallerThan(final VersionNumber versionNumber) {
        return this.compareTo(versionNumber) < 0;
    }

    public String getVersion() {
        return version_d;
    }


    public int compareTo(@NotNull final VersionNumber otherObject) {
        if (this.equals(otherObject)) {
            return 0;
        }

        final StringTokenizer ownTokenizer = this.getTokenizer();
        final StringTokenizer otherTokenizer = otherObject.getTokenizer();

        while (ownTokenizer.hasMoreTokens()) {
            final int ownNumber;
            final int otherNumber;

            try {
                ownNumber = Integer.parseInt(ownTokenizer.nextToken());
                otherNumber = Integer.parseInt(otherTokenizer.nextToken());
            } catch (NoSuchElementException nseex) {
                // only possible if we have more tokens than the other version -
                // if we get to this point then we are always greater
                return 1;
            }

            if (ownNumber > otherNumber) {
                return 1;
            } else if (ownNumber < otherNumber) {
                return -1;
            }
        }

        // if other version still has tokens then it is greater than me!
        otherTokenizer.nextToken();
        return -1;
    }


    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof VersionNumber)) {
            return false;
        }

        final VersionNumber versionNumber = (VersionNumber) o;

        return !(version_d != null ? !version_d.equals(versionNumber.version_d)
                : versionNumber.version_d != null);

    }

    public int hashCode() {
        return (version_d != null ? version_d.hashCode() : 0);
    }

    @NotNull
    private StringTokenizer getTokenizer() {
        return new StringTokenizer(this.getVersion(), ".");
    }
}
