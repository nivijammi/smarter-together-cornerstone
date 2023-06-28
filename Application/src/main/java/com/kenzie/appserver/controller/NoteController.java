package com.kenzie.appserver.controller;


import com.kenzie.appserver.controller.model.*;
import com.kenzie.appserver.service.NoteService;
import com.kenzie.appserver.service.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.time.ZonedDateTime;

@RestController
@RequestMapping("/v1")
public class NoteController {
    @Autowired
    NoteService noteService;


    // had to change the endpoints
    @PostMapping("/notes/create")
    public ResponseEntity<NoteResponse> createNote(@RequestBody NoteRequest request) {
        // Scenario #1: Note Exists
        boolean exists = noteService.doesNoteExist(request.getNoteId());
        if (exists){
            NoteResponse noteExistsResponse = new NoteResponse(request.getNoteId(),
                    request.getUserId(),
                    request.getContent(),
                    request.getCreatedDateTime(),
                    request.getUpdatedDateTime());
            return ResponseEntity.created(URI.create("/notes/" + noteExistsResponse.getNoteId())).body(noteExistsResponse);
        }

        // Scenario #2: UserID not valid
        if (!noteService.isValidUserId(request.getUserId())) {
            NoteResponse invalidUserIdResponse = new NoteResponse(request.getNoteId(),
                    request.getUserId(),
                    request.getContent(),
                    request.getCreatedDateTime(),
                    request.getUpdatedDateTime());
            return ResponseEntity.created(URI.create("/users/" + invalidUserIdResponse.getUserId())).body(invalidUserIdResponse);
        }

        Note note = new Note(request.getNoteId(), request.getUserId(), request.getContent(), ZonedDateTime.now(), ZonedDateTime.now());
        Note newNote = noteService.createNote(note);

        NoteResponse createNoteSuccessfulResponse = new NoteResponse(newNote.getNoteId(),
                newNote.getUserId(),
                newNote.getContent(),
                newNote.getCreatedDateTime(),
                newNote.getUpdatedDateTime());
        return ResponseEntity.created(URI.create("/notes/" + createNoteSuccessfulResponse.getNoteId())).body(createNoteSuccessfulResponse);
    }

    @GetMapping("/notes/{noteId}")
    public ResponseEntity<NoteResponse> getNoteById(@PathVariable String noteId) {
        Note note = noteService.findByNoteId(noteId);

        if(note == null){
            return ResponseEntity.notFound().build();
        }
        NoteResponse noteResponse = convertToNoteResponse(note);
        return ResponseEntity.ok(noteResponse);
    }
    // get all notes

    @PutMapping("/notes/{noteId}")
    public ResponseEntity<NoteResponse> updateNote(@PathVariable String noteId, @RequestBody NoteRequest noteRequest) {
        if (noteRequest.getNoteId() == null || noteRequest.getNoteId().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Note ID is required");
        }

        Note existingNote = noteService.findByNoteId(noteId);
        if (existingNote == null) {
            return ResponseEntity.notFound().build();
        }
        existingNote.setNoteId(noteRequest.getNoteId());
        existingNote.setUserId(noteRequest.getUserId());
        existingNote.setContent(noteRequest.getContent());
        existingNote.setCreatedDateTime(noteRequest.getCreatedDateTime());
        existingNote.setUpdatedDateTime(ZonedDateTime.now());

        noteService.updateNote(existingNote);

        NoteResponse noteResponse = convertToNoteResponse(existingNote);
        return ResponseEntity.ok(noteResponse);
    }

    @DeleteMapping("/notes/{noteId}")
    public ResponseEntity deleteStudyGroup(@PathVariable String noteId) {

        Note existingNote = noteService.findByNoteId(noteId);
        if (existingNote == null) {
            return ResponseEntity.notFound().build();
        }
        noteService.deleteNote(noteId);
        return ResponseEntity.noContent().build();
    }

    private NoteResponse convertToNoteResponse(Note note) {
        return new NoteResponse(note.getNoteId(),
                note.getUserId(),
                note.getContent(),
                note.getCreatedDateTime(),
                note.getUpdatedDateTime());
    }
}
