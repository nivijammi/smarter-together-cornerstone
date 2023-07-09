package com.kenzie.appserver.service;

import com.kenzie.appserver.config.CacheStore;
import com.kenzie.appserver.exception.NoteNotFoundException;
import com.kenzie.appserver.exception.StudyGroupNotFoundException;
import com.kenzie.appserver.repositories.MemberRepository;
import com.kenzie.appserver.repositories.NoteRepository;
import com.kenzie.appserver.repositories.model.MemberRecord;
import com.kenzie.appserver.repositories.model.NoteRecord;
import com.kenzie.appserver.service.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private MemberRepository memberRepository;

    public NoteService(NoteRepository noteRepository, MemberRepository memberRepository) {
        this.noteRepository = noteRepository;
        this.memberRepository = memberRepository;
    }

    public boolean doesNoteExist(String noteId) {
        Optional<NoteRecord> noteById = noteRepository.findById(noteId);
        return noteById.isPresent();
    }

//    public boolean isValidUserId(String userId) {
//        Optional<MemberRecord> userById = memberRepository.findById(userId);
//        return userById.isPresent();
//    }

    public Note createNote(Note note) {

        // Check if the study group already exists
        Note existingNote = getExistingNote(note);
        if (existingNote != null) {
            // If the study group exists, return it
            return existingNote;
        }
        // add to the repo
        NoteRecord record = buildNoteRecord(note);
        noteRepository.save(record);
        return buildNote(record);
    }

    public Note getExistingNote(Note note) {
        Iterable<NoteRecord> records = noteRepository.findAll();
        List<Note> allNotes = new ArrayList<>();
        if (records != null) {
            for (NoteRecord record : records) {
                Note studyNote = buildNote(record);
                allNotes.add(studyNote);
            }
        }
        for (Note existingNote : allNotes) {
            if (existingNote.getNoteId().equals(note.getNoteId())
                    && existingNote.getUserId().equals(note.getUserId())) {
                return existingNote;
            }
        }
        return null;
    }

    private NoteRecord buildNoteRecord(Note note) {        ;
        NoteRecord record = new NoteRecord();
        record.setNoteId(note.getNoteId());
        record.setUserId(note.getUserId());
        record.setContent(note.getContent());
        record.setCreatedDateTime(note.getCreatedDateTime());
        record.setUpdatedDateTime(note.getUpdatedDateTime());
        return record;
    }

    private Note buildNote(NoteRecord record) {
        Note note = new Note(record.getNoteId(), record.getUserId(),
                record.getContent(), record.getCreatedDateTime(),
                record.getUpdatedDateTime());
        return note;
    }

    public Note findByNoteId(String noteId) {
        Optional<NoteRecord> noteById = noteRepository.findById(noteId);
        if(noteById.isEmpty()) {
            return null;
        }
        NoteRecord noteRecord = noteById.get();
        // convert from record to study group(domain object)
        return buildNote(noteRecord);
    }

    public void updateNote(Note note) {
        Optional<NoteRecord> existingNote = noteRepository.findById(note.getNoteId());
        if (existingNote.isEmpty()) {
            throw new NoteNotFoundException("Note not found for noteId: " + note.getNoteId());
        }
        // Update the properties of the existing note with the new values
        NoteRecord noteRecord = existingNote.get();
        noteRecord.setNoteId(note.getNoteId());
        noteRecord.setUserId(note.getUserId());
        noteRecord.setContent(note.getContent());
        noteRecord.setCreatedDateTime(note.getCreatedDateTime());
        noteRecord.setUpdatedDateTime(note.getUpdatedDateTime());
        noteRepository.save(noteRecord);
    }

    public void deleteNote(String noteId) {
        noteRepository.deleteById(noteId);
    }
}
