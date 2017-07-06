package com.example.demo.repositories;

import com.example.demo.model.Edu;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

/**
 * Created by student on 7/6/17.
 */

public interface EduRepository extends CrudRepository<Edu, Integer> {
    ArrayList<Edu> findAllByUsername(String username);
}
