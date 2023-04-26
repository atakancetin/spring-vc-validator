package com.cvvalidator.cvvalidator.repository;

import com.cvvalidator.cvvalidator.model.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends CrudRepository<Profile,Long> {

}
