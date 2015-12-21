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

package com.wisemapping.service;

import com.wisemapping.exceptions.AccessDeniedSecurityException;
import com.wisemapping.exceptions.LockException;
import com.wisemapping.exceptions.WiseMappingException;
import com.wisemapping.model.Mindmap;
import com.wisemapping.model.User;
import org.jetbrains.annotations.NotNull;

public interface LockManager {
    boolean isLocked(@NotNull Mindmap mindmap);

    LockInfo getLockInfo(@NotNull Mindmap mindmap);

    LockInfo updateExpirationTimeout(@NotNull Mindmap mindmap, @NotNull User user);

    void unlock(@NotNull Mindmap mindmap, @NotNull User user) throws LockException, AccessDeniedSecurityException;

    void unlockAll(@NotNull User user) throws LockException, AccessDeniedSecurityException;

    boolean isLockedBy(@NotNull Mindmap mindmap, @NotNull User collaborator);

    @NotNull
    LockInfo lock(@NotNull Mindmap mindmap, @NotNull User user, long session) throws WiseMappingException;

    long generateSession();
}
