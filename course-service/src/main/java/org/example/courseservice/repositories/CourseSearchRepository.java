package org.example.courseservice.repositories;

import org.example.courseservice.indexies.CourseIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.util.List;
@EnableElasticsearchRepositories
public interface CourseSearchRepository extends ElasticsearchRepository<CourseIndex, String> {
    List<CourseIndex> findByNameContainingIgnoreCase(String name);
}
