package com.kenzie.appserver.service;

import com.kenzie.appserver.exception.NoteNotFoundException;
import com.kenzie.appserver.exception.StudyGroupNotFoundException;
import com.kenzie.appserver.repositories.MemberRepository;
import com.kenzie.appserver.repositories.NoteRepository;
import com.kenzie.appserver.repositories.model.NoteRecord;
import com.kenzie.appserver.service.model.Note;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NoteServiceTest {
    private NoteService subject;
    private NoteRepository noteRepository;
    private MemberRepository memberRepository;

    @BeforeEach
    void setup(){
        noteRepository = mock(NoteRepository.class);
        memberRepository = mock(MemberRepository.class);
        subject = new NoteService(noteRepository,memberRepository);
    }

    @Test
    void createNote_successful(){

        String noteId = UUID.randomUUID().toString();
        String userId = "person1@aol.com";
        String content = "ZonedDateTime is an immutable representation of a date-time with a time-zone. " +
                "This class stores all date and time fields, " +
                "to a precision of nanoseconds, and a time-zone, " +
                "with a zone offset used to handle ambiguous local date-times.";
        ZonedDateTime createdDate = ZonedDateTime.now();
        ZonedDateTime updatedDate = ZonedDateTime.now();

        NoteRecord record = new NoteRecord();
        record.setNoteId(noteId);
        record.setUserId(userId);
        record.setContent(content);
        record.setCreatedDateTime(createdDate);
        record.setUpdatedDateTime(updatedDate);

        Note note = new Note(noteId,userId,content,createdDate,updatedDate);
        Note newNote = subject.createNote(note);

        assertNotNull(newNote);

        verify(noteRepository, times(1)).save(record);

        assertEquals(record.getNoteId(), newNote.getNoteId());
        assertEquals(record.getUserId(), newNote.getUserId());
        assertEquals(record.getContent(),newNote.getContent());
        assertEquals(record.getCreatedDateTime(),newNote.getCreatedDateTime());
        assertEquals(record.getUpdatedDateTime(),newNote.getUpdatedDateTime());

        noteRepository.deleteById(noteId);


    }

    @Test
    void createNote_unsuccessful(){
        String noteId = UUID.randomUUID().toString();
        String userId = "person1@aol.com";
        String content = "ZonedDateTime is an immutable representation of a date-time with a time-zone. " +
                "This class stores all date and time fields, " +
                "to a precision of nanoseconds, and a time-zone, " +
                "with a zone offset used to handle ambiguous local date-times.";
        ZonedDateTime createdDate = ZonedDateTime.now();
        ZonedDateTime updatedDate = ZonedDateTime.now();

        Note note = new Note(noteId,userId,content,createdDate,updatedDate);

        when(noteRepository.save(any())).thenThrow(new NoteNotFoundException("Failed to save the note"));

        assertThrows(NoteNotFoundException.class, () -> subject.createNote(note));

        verify(noteRepository, times(1)).save(any());

    }

    // source: https://stackoverflow.com/questions/4801794/use-of-javas-collections-singletonlist

    @Test
    void getExistingNote_existingNoteExists_returnNote() {

        String noteId = UUID.randomUUID().toString();
        String userId = "person2@aol.com";
        String content = "Sample content";
        ZonedDateTime createdDate = ZonedDateTime.now();
        ZonedDateTime updatedDate = ZonedDateTime.now();

        Note note = new Note(noteId, userId, content, createdDate, updatedDate);

        NoteRecord record = new NoteRecord();
        record.setNoteId(noteId);
        record.setUserId(userId);
        record.setContent(content);
        record.setCreatedDateTime(createdDate);
        record.setUpdatedDateTime(updatedDate);

        when(noteRepository.findAll()).thenReturn(Collections.singletonList(record));

        Note existingNote = subject.getExistingNote(note);

        assertNotNull(existingNote);
        assertEquals(note.getNoteId(), existingNote.getNoteId());
        assertEquals(note.getUserId(), existingNote.getUserId());
        assertEquals(note.getContent(), existingNote.getContent());
        assertEquals(note.getCreatedDateTime(), existingNote.getCreatedDateTime());
        assertEquals(note.getUpdatedDateTime(), existingNote.getUpdatedDateTime());

        verify(noteRepository, times(1)).findAll();
        noteRepository.deleteById(noteId);
    }

    @Test
    void getExistingNote_noMatchingNote_returnNull() {
        String noteId = UUID.randomUUID().toString();
        String userId = "person1@aol.com";
        String content = "Sample content";
        ZonedDateTime createdDate = ZonedDateTime.now();
        ZonedDateTime updatedDate = ZonedDateTime.now();

        Note note = new Note(noteId, userId, content, createdDate, updatedDate);

        NoteRecord record = new NoteRecord();
        record.setNoteId(UUID.randomUUID().toString());
        record.setUserId("person2@aol.com");
        record.setContent("Different content");
        record.setCreatedDateTime(ZonedDateTime.now());
        record.setUpdatedDateTime(ZonedDateTime.now());

        when(noteRepository.findAll()).thenReturn(Collections.singletonList(record));

        Note existingNote = subject.getExistingNote(note);

        assertNull(existingNote);

        verify(noteRepository, times(1)).findAll();
    }

    @Test
    void findByNoteId_existingNoteId_returnNote() {
        String noteId = UUID.randomUUID().toString();
        String userId = "person3@aol.com";
        String content = "Sample content";
        ZonedDateTime createdDate = ZonedDateTime.now();
        ZonedDateTime updatedDate = ZonedDateTime.now();

        NoteRecord record = new NoteRecord();
        record.setNoteId(noteId);
        record.setUserId(userId);
        record.setContent(content);
        record.setCreatedDateTime(createdDate);
        record.setUpdatedDateTime(updatedDate);

        when(noteRepository.findById(noteId)).thenReturn(Optional.of(record));

        Note result = subject.findByNoteId(noteId);

        assertNotNull(result);
        assertEquals(noteId, result.getNoteId());
        assertEquals(userId, result.getUserId());
        assertEquals(content, result.getContent());
        assertEquals(createdDate, result.getCreatedDateTime());
        assertEquals(updatedDate, result.getUpdatedDateTime());

        verify(noteRepository, times(1)).findById(noteId);
        noteRepository.deleteById(noteId);
    }

    @Test
    void findByNoteId_nonExistingNoteId_returnNull() {
        String nonExistingNoteId = UUID.randomUUID().toString();

        when(noteRepository.findById(nonExistingNoteId)).thenReturn(Optional.empty());

        Note result = subject.findByNoteId(nonExistingNoteId);

        assertNull(result);

        verify(noteRepository, times(1)).findById(nonExistingNoteId);
    }

    @Test
    void updateNote_existingNote_updatePropertiesAndSave() {
        String noteId = UUID.randomUUID().toString();
        String userId = "person4@aol.com";
        String content = "Sample content";
        ZonedDateTime createdDate = ZonedDateTime.now();
        ZonedDateTime updatedDate = ZonedDateTime.now();

        Note note = new Note(noteId, userId, content, createdDate, updatedDate);

        NoteRecord existingNoteRecord = new NoteRecord();
        existingNoteRecord.setNoteId(noteId);
        existingNoteRecord.setUserId(userId);
        existingNoteRecord.setContent("Old content");
        existingNoteRecord.setCreatedDateTime(ZonedDateTime.now());
        existingNoteRecord.setUpdatedDateTime(ZonedDateTime.now());

        when(noteRepository.findById(noteId)).thenReturn(Optional.of(existingNoteRecord));

        subject.updateNote(note);

        verify(noteRepository, times(1)).findById(noteId);

        assertEquals(noteId, existingNoteRecord.getNoteId());
        assertEquals(userId, existingNoteRecord.getUserId());
        assertEquals(content, existingNoteRecord.getContent());
        assertEquals(createdDate, existingNoteRecord.getCreatedDateTime());
        assertEquals(updatedDate, existingNoteRecord.getUpdatedDateTime());

        verify(noteRepository, times(1)).save(existingNoteRecord);
    }

    @Test
    void updateNote_nonExistingNote_throwNoteNotFoundException() {
        String nonExistingNoteId = UUID.randomUUID().toString();
        String userId = "person1@aol.com";
        String content = "Sample content";
        ZonedDateTime createdDate = ZonedDateTime.now();
        ZonedDateTime updatedDate = ZonedDateTime.now();

        Note note = new Note(nonExistingNoteId, userId, content, createdDate, updatedDate);

        when(noteRepository.findById(nonExistingNoteId)).thenReturn(Optional.empty());

        assertThrows(NoteNotFoundException.class, () -> subject.updateNote(note));

        verify(noteRepository, times(1)).findById(nonExistingNoteId);

        verify(noteRepository, never()).save(any(NoteRecord.class));
    }

    @Test
    void deleteNote_existingNoteId_deleteById() {
        String noteId = UUID.randomUUID().toString();

        subject.deleteNote(noteId);

        verify(noteRepository, times(1)).deleteById(noteId);
    }







}
