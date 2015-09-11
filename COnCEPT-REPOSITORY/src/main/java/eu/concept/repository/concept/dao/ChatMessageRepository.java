/*
 *  Copyright 2015 Medusa project, http://athina.cs.unipi.gr/medusa
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package eu.concept.repository.concept.dao;


import javax.transaction.Transactional;

import eu.concept.repository.concept.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by nikos on 7/9/2015.
 */
@Repository
@Transactional
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    public List<ChatMessage> findByPid(int pid);
    public List<ChatMessage> findTop100ByPidOrderByCreatedDateDesc(int pid);
    public List<ChatMessage> findByPidOrderByCreatedDateDesc(int pid);
}//EoI
