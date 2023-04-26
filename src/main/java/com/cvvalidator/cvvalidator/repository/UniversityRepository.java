package com.cvvalidator.cvvalidator.repository;

import com.cvvalidator.cvvalidator.model.Profile;
import com.cvvalidator.cvvalidator.model.University;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversityRepository extends CrudRepository<University,Long> {

    University findByIdAndProfile(long id, Profile profile);
    University findAllByProfile(Profile profile);
}
