package com.example.demo.repositories;

import com.example.demo.models.Skill;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

/**
 * Created by student on 7/6/17.
 */
public interface SkillRepository extends CrudRepository<Skill, Integer> {
    ArrayList<Skill> findAllByUsername(String username);

    ArrayList<Skill> findAllByArea(String skill);
}
