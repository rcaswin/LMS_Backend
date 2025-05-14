package com.example.LearningManagementSystem.Service;

import com.example.LearningManagementSystem.Entity.TopicEntity;
import com.example.LearningManagementSystem.Repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    public TopicEntity saveTopic(TopicEntity topic) {
        return topicRepository.save(topic);
    }

    public TopicEntity getTopicById(Long id) {
        return topicRepository.findById(id).orElse(null);
    }

    public void deleteTopic(Long id) {
        topicRepository.deleteById(id);
    }

    public TopicEntity updateTopic(Long id, TopicEntity updatedTopic) {
        if (topicRepository.existsById(id)) {
            updatedTopic.setId(id);
            return topicRepository.save(updatedTopic);
        }
        return null; // Or throw an exception
    }
}

