package org.example.carpooling.repositories;

import org.example.carpooling.models.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<Option, Integer> {
    Option findOptionByOption(String option);
}
