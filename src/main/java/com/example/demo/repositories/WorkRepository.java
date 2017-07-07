package com.example.demo.repositories;

import com.example.demo.models.Work;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

/**
 * Created by student on 7/6/17.
 */
public interface WorkRepository extends CrudRepository<Work, Integer> {
    ArrayList<Work> findAllByUsername(String username);
    ArrayList<Work> findAllByCompany(String company);
}
