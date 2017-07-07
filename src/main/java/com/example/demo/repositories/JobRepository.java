package com.example.demo.repositories;

import com.example.demo.models.Job;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

/**
 * Created by student on 7/6/17.
 */

public interface JobRepository extends CrudRepository<Job, Integer> {

    ArrayList<Job> findAllByTitle(String title);
    ArrayList<Job> findAllByRequirementsContaining(String skill);
    ArrayList<Job> findAllByEmployer(String username);
    ArrayList<Job> findAllByDescriptionContaining(String key);

}
