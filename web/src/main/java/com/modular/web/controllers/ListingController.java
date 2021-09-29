package com.modular.web.controllers;

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
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ListingController {
    private final ListingService listingService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ModelMapper mapper;

    @GetMapping("/listings")
    public String listings(ModelMap modelMap) {
        List<Listing> listings = listingService.findAll();
        List<ListingDto> allListings = new LinkedList<>();
        for (Listing listing : listings) {
            allListings.add(parseListingToListingDto(listing));
        }
        modelMap.addAttribute("allListings",allListings);
        return "listings";
    }

    @GetMapping("/listings/byUser/{email}")
    public String byUserEmail(@PathVariable("email") String email, ModelMap modelMap) {
        User userByEmail = userService.findByEmail(email);
        if (userByEmail == null) {
            return "redirect:/listings";
        }
        List<Listing> listings = listingService.findByUser(userByEmail);
        List<ListingDto> byUser = new LinkedList<>();
        for (Listing listing : listings) {
            byUser.add(parseListingToListingDto(listing));
        }
        modelMap.addAttribute("byUser",byUser);
        return "userListings";
    }

    @GetMapping("/listings/byCategory/{categoryId}")
    public String byCategoryId(@PathVariable("categoryId") int id, ModelMap modelMap) {
        Optional<Category> byId = categoryService.findById(id);
        if (byId.isEmpty()) {
            return "redirect:/listings";
        }
        List<Listing> byCategory = listingService.findAllByCategory(byId.get());
        List<ListingDto> allByCategory = new LinkedList<>();
        for (Listing listing : byCategory) {
            allByCategory.add(parseListingToListingDto(listing));
        }
        modelMap.addAttribute("allByCategory",allByCategory);
        return "cotegoryListings";
    }

    @GetMapping("/listings/{id}")
    public String listingById(@PathVariable("id") int id, ModelMap modelMap) {
        Optional<Listing> byId = listingService.findById(id);
        if (byId.isEmpty()) {
            return "redirect:/listings";
        }
        modelMap.addAttribute("listing", parseListingToListingDto(byId.get()));
        return "singleListing";
    }

    @PostMapping("/listings")
    public String addListing(@RequestBody ListingSaveDto listingSaveDto) {
        Listing listing = mapper.map(listingSaveDto, Listing.class);
        listing.setUser(mapper.map(listingSaveDto.getUserDto(), User.class));
        listingService.save(listing);
        return "redirect:/listings";
    }

    @PutMapping("/listings")
    public String updateListing(@RequestBody ListingDto listingDto) {
        Listing listing = mapper.map(listingDto, Listing.class);
        listing.setUser(mapper.map(listingDto.getUserDto(), User.class));
        listingService.update(listing);
        return "redirect:/listings";
    }

    @DeleteMapping("/listings/{id}")
    public String deleteListing(@PathVariable("id") int id) {
        listingService.deleteById(id);
        return "redirect:/listings";
    }

    private ListingDto parseListingToListingDto(Listing listing){
        ListingDto listingDto = mapper.map(listing, ListingDto.class);
        listingDto.setUserDto(mapper.map(listing.getUser(), UserDto.class));
        return listingDto;
    }
}
