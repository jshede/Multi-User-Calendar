package com.example.demo;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.SampleEntity;

public interface SampleEntityRepository extends CrudRepository<SampleEntity, Integer>
{

}
