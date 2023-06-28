package com.kenzie.appserver.repositories;
import com.kenzie.appserver.repositories.model.NoteRecord;
import jdk.jfr.Enabled;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@EnableScan
public interface NoteRepository extends CrudRepository<NoteRecord, String> {

    Optional<NoteRecord> findById(String noteId);
}
