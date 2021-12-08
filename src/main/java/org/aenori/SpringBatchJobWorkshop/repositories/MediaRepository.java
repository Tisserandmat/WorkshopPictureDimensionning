package org.aenori.SpringBatchJobWorkshop.repositories;

import org.aenori.SpringBatchJobWorkshop.entities.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface MediaRepository extends JpaRepository<Media, Integer> {
    @Transactional
    @Modifying
    @Query("UPDATE Media c SET c.status = 0")
    void updateAllProcessedStatus();
}
