package com.modular.rest.endpoints;

import com.listing.common.dto.ListingDto;
import com.listing.common.dto.ListingSaveDto;
import com.listing.common.dto.UserDto;
import com.listing.common.model.Category;
import com.listing.common.model.Listing;
import com.listing.common.model.User;
import com.listing.common.servicies.CategoryService;
import com.listing.common.servicies.ListingService;
import com.listing.common.servicies.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ListingEndpoint {

    private final ListingService listingService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ModelMapper mapper;

    @GetMapping("/listings")
    public ResponseEntity<List<ListingDto>> listings() {
        List<Listing> listings = listingService.findAll();
        List<ListingDto> allListings = new LinkedList<>();
        for (Listing listing : listings) {
            allListings.add(parseListingToListingDto(listing));
        }
        return ResponseEntity.ok(allListings);
    }

    @GetMapping("/listings/byUser/{email}")
    public ResponseEntity<List<ListingDto>> byUserEmail(@PathVariable("email") String email) {
        User userByEmail = userService.findByEmail(email);
        if (userByEmail == null) {
            return ResponseEntity.notFound().build();
        }
        List<Listing> listings = listingService.findByUser(userByEmail);
        List<ListingDto> byUser = new LinkedList<>();
        for (Listing listing : listings) {
            byUser.add(parseListingToListingDto(listing));
        }
        return ResponseEntity.ok(byUser);
    }

    @GetMapping("/listings/byCategory/{categoryId}")
    public ResponseEntity<List<ListingDto>> byCategoryId(@PathVariable("categoryId") int id) {
        Optional<Category> byId = categoryService.findById(id);
        if (byId.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<Listing> byCategory = listingService.findAllByCategory(byId.get());
        List<ListingDto> allByCategory = new LinkedList<>();
        for (Listing listing : byCategory) {
            allByCategory.add(parseListingToListingDto(listing));
        }
        return ResponseEntity.ok(allByCategory);
    }

    @GetMapping("/listings/{id}")
    public ResponseEntity<ListingDto> listingById(@PathVariable("id") int id) {
        Optional<Listing> byId = listingService.findById(id);
        if (byId.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(parseListingToListingDto(byId.get()));
    }

    @PostMapping("/listings")
    public ResponseEntity<ListingDto> addListing(@RequestBody ListingSaveDto listingSaveDto) {
        Listing listing = mapper.map(listingSaveDto, Listing.class);
        listing.setUser(mapper.map(listingSaveDto.getUserDto(), User.class));
        Listing save = listingService.save(listing);
        return ResponseEntity.ok(parseListingToListingDto(save));
    }

    @PutMapping("/listings")
    public ResponseEntity<ListingDto> updateListing(@RequestBody ListingDto listingDto) {
        Listing listing = mapper.map(listingDto, Listing.class);
        listing.setUser(mapper.map(listingDto.getUserDto(), User.class));
        if (listingService.update(listing)) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(parseListingToListingDto(listing));
    }

    @DeleteMapping("/listings/{id}")
    public ResponseEntity deleteListing(@PathVariable("id") int id) {
        if (!listingService.deleteById(id)) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    private ListingDto parseListingToListingDto(Listing listing) {
        ListingDto listingDto = mapper.map(listing, ListingDto.class);
        listingDto.setUserDto(mapper.map(listing.getUser(), UserDto.class));
        return listingDto;
    }

}
