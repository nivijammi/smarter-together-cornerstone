package com.kenzie.appserver.repositories;
import com.kenzie.appserver.repositories.model.NoteRecord;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface NoteRepository extends CrudRepository<NoteRecord, String> {

    Optional<NoteRecord> findById(String noteId);
}
