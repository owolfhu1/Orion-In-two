package com.example.demo.repositories;

import com.example.demo.model.Notification;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

/**
 * Created by student on 7/6/17.
 */
public interface NotificationRepository extends CrudRepository<Notification, Integer> {

    ArrayList<Notification> findAllByUsername(String username);
}
