package org.example.carpooling.services;

import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.EntityDuplicateException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.models.Option;
import org.example.carpooling.models.User;
import org.example.carpooling.repositories.OptionRepository;
import org.example.carpooling.services.interfaces.OptionService;

public class OptionServiceImpl implements OptionService {
    private final OptionRepository optionRepository;
    public static final String MODIFY_ERROR_MESSAGE = "Only an admin can make changes to the options!";


    public OptionServiceImpl(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    @Override
    public Option createOption(Option option) {
        boolean duplicateExists = true;
        try {
            findOptionByOptionType(option.getOptionType());
        } catch (EntityNotFoundException e){
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("Option", "name", option.getOptionType());
        } else {
            return optionRepository.save(option);
        }
    }

    @Override
    public Option findOptionById(int id) {
        return optionRepository.findOptionByOptionId(id)
                .orElseThrow( () -> new EntityNotFoundException("Option", id));
    }

    @Override
    public Option findOptionByOptionType(String optionType) {
        return optionRepository.findOptionByOptionType(optionType)
                .orElseThrow( () -> new EntityNotFoundException("Option", "name", optionType));
    }

    @Override
    public void updateOption(User requestingUser, Option updatedOption) {
        checkPermission(requestingUser);
        optionRepository.save(updatedOption);
    }

    private void checkPermission(User requestingUser){
        if (!requestingUser.isAdmin()){
            throw new AuthorizationException(MODIFY_ERROR_MESSAGE);
        }
    }
}
