package org.example.carpooling.repositories;

import org.example.carpooling.models.Feedback;
import org.example.carpooling.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    Feedback findFeedbackByAuthor(User author);
}
