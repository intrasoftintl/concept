/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.concept.repository.concept.dao;

import eu.concept.repository.concept.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public interface TagsRepository extends JpaRepository<Tag,Integer>{

}
