package com.example.demo.repositories;

import com.example.demo.model.Job;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

/**
 * Created by student on 7/6/17.
 */

public interface JobRepository extends CrudRepository<Job, Integer> {

    ArrayList<Job> findAllByTitle(String title);
    ArrayList<Job> findAllByRequirementsContaining(String skill);
    ArrayList<Job> findAllByEmployer(String username);

}
