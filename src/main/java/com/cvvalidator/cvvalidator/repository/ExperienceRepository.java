package com.cvvalidator.cvvalidator.repository;

import com.cvvalidator.cvvalidator.model.Experience;
import com.cvvalidator.cvvalidator.model.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperienceRepository extends CrudRepository<Experience,Long> {

    void deleteByIdAndProfile(long id, Profile profile);

    void deleteByIdAndProfile_Id(long id, long profile_id);
}
