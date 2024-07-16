package com.imc.sqlclient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.imc.sqlclient.model.Connection;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Integer> {

}
