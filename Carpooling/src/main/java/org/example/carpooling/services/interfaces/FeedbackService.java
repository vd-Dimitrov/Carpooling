package org.example.carpooling.services.interfaces;

import org.example.carpooling.models.Feedback;
import org.example.carpooling.models.User;

import java.util.List;

public interface FeedbackService {
    Feedback createFeedback(Feedback feedback);

    Feedback getFeedbackById(int id);

    List<Feedback> getAllFeedback();

    List<Feedback> getFeedbackByAuthor(User author);

    List<Feedback> getFeedbackByReceiver(User receiver);

    void updateFeedback(Feedback updatedFeedback, User requestingUser);

    void deleteFeedback(Feedback deletedFeedback, User requestingUser);
}
