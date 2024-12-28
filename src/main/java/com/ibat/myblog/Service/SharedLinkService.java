package com.ibat.myblog.Service;

import com.ibat.myblog.Model.SharedLink;
import com.ibat.myblog.Repository.SharedLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SharedLinkService {
    
    @Autowired
    private SharedLinkRepository sharedLinkRepository;

    public List<SharedLink> getLatestSharedLinks(int limit) {
        return sharedLinkRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, limit));
    }
}
