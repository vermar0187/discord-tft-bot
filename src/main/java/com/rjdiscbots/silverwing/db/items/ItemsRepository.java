package com.rjdiscbots.silverwing.db.items;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemsRepository extends JpaRepository<ItemEntity, Long> {

    ItemEntity findOneByName(String name);

    ItemEntity findOneById(Integer id);

    List<ItemEntity> findByComponentOneIsNullAndComponentTwoIsNull();

    List<ItemEntity> findByComponentOneNameOrComponentTwoName(
        String c1Name, String c2Name);

    List<ItemEntity> findByComponentOneNameAndComponentTwoNameIsInOrComponentTwoNameAndComponentOneNameIsIn(
        String c1Name, List<String> c2List, String c2Name, List<String> c1List);
}
