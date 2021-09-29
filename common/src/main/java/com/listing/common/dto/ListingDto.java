package com.listing.common.dto;

import com.listing.common.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListingDto {
    private int id;
    private String title;
    private String description;
    private double price;
    private Category category;
    private UserDto userDto;
}
