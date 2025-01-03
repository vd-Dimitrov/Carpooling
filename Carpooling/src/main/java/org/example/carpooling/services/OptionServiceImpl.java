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
            findOptionByName(option.getOption());
        } catch (EntityNotFoundException e){
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("Option", "name", option.getOption());
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
    public Option findOptionByName(String name) {
        return optionRepository.findOptionByOption(name)
                .orElseThrow( () -> new EntityNotFoundException("Option", "name", name));
    }

    @Override
    public void updateOption(User requestingUser, Option updatedOption) {

    }

    private void checkPermission(User requestingUser, User updatedUser){
        if (!requestingUser.isAdmin()){
            throw new AuthorizationException(MODIFY_ERROR_MESSAGE);
        }
    }
}
