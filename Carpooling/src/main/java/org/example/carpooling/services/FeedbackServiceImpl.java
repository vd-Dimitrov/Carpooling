package org.example.carpooling.services;

import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.models.Feedback;
import org.example.carpooling.models.User;
import org.example.carpooling.repositories.FeedbackRepository;
import org.example.carpooling.services.interfaces.FeedbackService;

import java.util.List;

public class FeedbackServiceImpl implements FeedbackService {
    public static final String MODIFY_ERROR_MESSAGE = "Only owner can make changes to the feedback!";
    private final FeedbackRepository feedbackRepository;

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public Feedback createFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    @Override
    public Feedback findById(int id) {
        return feedbackRepository.findFeedbackByFeedbackId(id)
                .orElseThrow( () -> new EntityNotFoundException("Feedback", id));
    }

    @Override
    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }

    @Override
    public List<Feedback> getFeedbackByAuthor(User author) {
        return feedbackRepository.findFeedbackByAuthor(author)
                .orElseThrow( () -> new EntityNotFoundException("User", author.getUserId()));
    }

    @Override
    public void updateFeedback(Feedback updatedFeedback, User requestingUser) {
        checkPermission(requestingUser, updatedFeedback);
        feedbackRepository.save(updatedFeedback);
    }

    @Override
    public void deleteFeedback(Feedback deletedFeedback, User requestingUser) {
        checkPermission(requestingUser, deletedFeedback);
        feedbackRepository.delete(deletedFeedback);
    }
    
    private void checkPermission(User requestingUser, Feedback updatedFeedback){
        if (requestingUser.getUserId()!= updatedFeedback.getAuthor().getUserId()){
            throw new AuthorizationException(MODIFY_ERROR_MESSAGE);
        }
    }
}
