package org.example.carpooling.repositories;

import org.example.carpooling.models.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OptionRepository extends JpaRepository<Option, Integer> {
    Optional<Option> findOptionByOptionType(String optionType);

    Optional<Option> findOptionByOptionId(int optionId);
}
