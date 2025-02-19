package org.example.carpooling.services.interfaces;

import org.example.carpooling.models.Option;
import org.example.carpooling.models.User;

public interface OptionService {
    Option createOption(Option option);

    Option findOptionById(int id);

    Option findOptionByOptionType(String name);

    void updateOption(User requestingUser, Option updatedOption);
}
