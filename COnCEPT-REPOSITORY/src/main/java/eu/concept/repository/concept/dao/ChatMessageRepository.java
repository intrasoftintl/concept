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
