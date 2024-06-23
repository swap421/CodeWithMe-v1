package com.connectify.connectify.Service;

import com.connectify.connectify.Repository.ConnectionRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConnectionService {
    @Autowired
    private ConnectionRequestRepository connectionRequestRepository;

    public void removeConnectionRequest(Long id1, Long id2) {
        connectionRequestRepository.removeConnectionRequest(id1,id2);
    }
}
