package com.listing.common.repositories;

import com.listing.common.model.Category;
import com.listing.common.model.Listing;
import com.listing.common.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface ListingRepository extends JpaRepository<Listing, Integer> {

    List<Listing> findAllByUser(User user);

    List<Listing> findAllByCategory(Category category);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE listing SET category_id =:after WHERE category_id =:before", nativeQuery = true)
    void changeAllListingsCategory(@Param("before") Integer before, @Param("after") Integer after);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE listing SET user_id =:after WHERE user_id =:before", nativeQuery = true)
    void changeAllListingsUser(@Param("before") Integer before, @Param("after") Integer after);
}
