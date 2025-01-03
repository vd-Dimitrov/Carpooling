package org.example.carpooling.services.interfaces;

import org.example.carpooling.models.Feedback;
import org.example.carpooling.models.User;

import java.util.List;

public interface FeedbackService {
    Feedback createFeedback(Feedback feedback);

    Feedback findById(int id);
    List<Feedback> getAllFeedback();
    List<Feedback> getFeedbackByAuthor(int authorId);
    void updateFeedback(Feedback updatedFeedback, User author);
}
