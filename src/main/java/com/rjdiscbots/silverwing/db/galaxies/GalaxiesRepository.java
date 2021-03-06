package com.rjdiscbots.silverwing.db.galaxies;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GalaxiesRepository extends JpaRepository<GalaxyEntity, Long> {

    GalaxyEntity findOneByName(String name);
}
