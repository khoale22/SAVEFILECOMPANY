package com.heb.pm.repository;

import com.heb.pm.entity.TagType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The JPA repository for the TagType class.
 *
 * @author d116773
 * @since 2.17.0
 */
public interface TagTypeRepository extends JpaRepository<TagType, String> {

    /**
     * Find tag type by tag type description
     *
     * @param tagTypeDescription the tag type description.
     * @return TagType
     */
    TagType findFirstByTagTypeDescription(String tagTypeDescription);
}
