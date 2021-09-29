package com.listing.common.servicies;

import com.listing.common.model.Category;
import com.listing.common.model.Listing;
import com.listing.common.model.User;
import com.listing.common.repositories.ListingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ListingService {
    private final ListingRepository listingRepository;
    private final CategoryService categoryService;

    public List<Listing> findAll() {
        return listingRepository.findAll();
    }

    public List<Listing> findByUser(User user) {
        List<Listing> allByUser = listingRepository.findAllByUser(user);
        if (allByUser.isEmpty()) {
            return null;
        }
        return allByUser;
    }

    public List<Listing> findAllByCategory(Category category) {
        return listingRepository.findAllByCategory(category);
    }

    public Optional<Listing> findById(int id) {
        return listingRepository.findById(id);
    }

    public Listing save(Listing listing) {
        return listingRepository.save(listing);
    }

    public boolean update(Listing listing) {
        Optional<Listing> byId = listingRepository.findById(listing.getId());
        if (byId.isEmpty()) {
            log.info("don't update: wrong listing's ID");
            return false;
        }
        listingRepository.save(listing);
        return true;
    }

    public boolean deleteById(int id) {
        Optional<Listing> byId = listingRepository.findById(id);
        if (byId.isEmpty()) {
            log.info("don't delete: wrong listing's ID");
            return false;
        }
        listingRepository.deleteById(id);
        return true;
    }

    //unused
    public void changeAllListingsCategory(Integer before, Integer after) {
        if (after == null) {
            listingRepository.changeAllListingsCategory(before, after);
        } else {
            Optional<Category> byId = categoryService.findById(after);
            if (byId.isPresent()) {
                listingRepository.changeAllListingsCategory(before, after);
            } else {
                return;
            }
        }
    }
}
