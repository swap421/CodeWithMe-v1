package com.connectify.connectify.Service;

import com.connectify.connectify.Entity.Rating;
import com.connectify.connectify.Repository.RatingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    public Rating findRatingByUser(Long userId){
        return ratingRepository.findByUserId(userId);
    }
    public void saveRating(Rating rating){
        ratingRepository.save(rating);
    }
    public List<Rating> findRelatableUsers(Long userId) {
        return ratingRepository.findAllRelatableUsers(userId);
    }

    public List<Rating> findPendingRequest(Long userId) {
        return ratingRepository.findAllPendingRequest(userId);
    }

    public List<Rating> findAllActiveUsers(Long userId) {
        return ratingRepository.findAllActiveUsers(userId);
    }

}
