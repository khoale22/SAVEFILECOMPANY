package com.heb.pm.repository;

import com.heb.pm.entity.GenericEntity;
import com.heb.pm.entity.GenericEntityRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository to retrieve information about generic entity.
 *
 * @author vn55306
 * @since 2.12.0
 */
public interface GenericEntityRepository extends JpaRepository<GenericEntity, Long> {

    GenericEntity findTop1ByDisplayNumberAndType(Long entryId,String type);
    
    @Query("select genericEntity.displayNumber from GenericEntity genericEntity where genericEntity.id in :id and genericEntity.type=:type")
    List<Long> findDisplayNumberByIdyAndType(@Param(value = "id") List<Long> entryId, @Param("type") String type);

    /**
     * Finds a list of entities that have a given display text and type. There should be only one in the resulting
     * list.
     *
     * @param displayText The display text to look for.
     * @param type The type of entity to look for.
     * @return The list of entities with that display number and type.
     */
    List<GenericEntity> findByDisplayTextAndType(String displayText, String type);

    /**
     * Finds a list of entities that have a given display number and type. There should be only one in the resulting
     * list.
     *
     * @param displayNumber The display number to look for.
     * @param type The type of entity to look for.
     * @return The list of entities with that display number and type.
     */
    List<GenericEntity> findByDisplayNumberAndType(Long displayNumber, String type);

    /**
     *  This method to find all display number that have entity id in list entity id.
     *
     * @param entityIds the list of entity id.
     * @return list of display number.
     */
    @Query("select genericEntity.displayNumber from GenericEntity genericEntity where genericEntity.id in :ids")
    List<Long> findDisplayNumberByIds(@Param(value = "ids") List<Long> entityIds);

    /**
     * This will find the list of entities by display number.
     *
     * @param displayNumber
     * @return
     */
    List<GenericEntity> findByDisplayNumber(Long displayNumber);

    /**
     * This method to find all entity ids by display name.
     *
     * @param displayName the display name.
     * @return list of entity ids.
     */
    @Query("select genericEntity.id from GenericEntity genericEntity where genericEntity.displayText =:displayName")
    List<Long> findGenericEntityIdsByDisplayName (@Param(value = "displayName") String displayName);
}
