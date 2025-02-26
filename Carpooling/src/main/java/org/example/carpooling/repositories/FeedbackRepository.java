package org.example.carpooling.repositories;

import org.example.carpooling.models.Feedback;
import org.example.carpooling.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    Optional<List<Feedback>> findFeedbackByAuthor(User user);

    Optional<List<Feedback>> findFeedbackByReceiver(User user);

    Optional<Feedback> findFeedbackByFeedbackId(int feedbackId);
}
