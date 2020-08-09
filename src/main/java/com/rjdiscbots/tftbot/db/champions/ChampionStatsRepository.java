package com.rjdiscbots.tftbot.db.champions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChampionStatsRepository extends JpaRepository<ChampionStatsEntity, Long> {

    ChampionStatsEntity findOneByChampionOrderByStarsAsc(String championName);
}
