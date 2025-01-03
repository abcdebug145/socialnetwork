package com.project.socialnetwork.service;

import com.project.socialnetwork.entity.Account;
import com.project.socialnetwork.entity.BanRequest;
import com.project.socialnetwork.entity.Post;
import com.project.socialnetwork.repository.BanRequestRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BanRequestService {
    private final BanRequestRepository banRequestRepository;
    
    public List<BanRequest> getAllBanRequests() {
        return banRequestRepository.findAll();
    }
    public BanRequest findById(Long id) {
        return banRequestRepository.findById(id).orElse(null);
    }
    public BanRequest saveBanRequest(BanRequest banRequest) {
        return banRequestRepository.save(banRequest);
    }
    public long getTotalPages() {
        return banRequestRepository.count() / 10;
    }
    public Page<BanRequest> findAll(Pageable pageable) {
        return banRequestRepository.findAll(pageable);
    }
    public BanRequest createBanRequest(Account account, Post post , String reason) {
        BanRequest banRequest = new BanRequest();
        banRequest.setAccount(account);
        banRequest.setPost(post);
        banRequest.setReason(reason);
        return banRequestRepository.save(banRequest);
    }
    public void deleteBanRequestById(Long id) {
        banRequestRepository.deleteById(id);
    }
}
