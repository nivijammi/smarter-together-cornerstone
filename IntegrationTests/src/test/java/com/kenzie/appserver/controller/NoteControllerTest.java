package com.kenzie.appserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.NoteRequest;
import com.kenzie.appserver.controller.model.NoteResponse;
import com.kenzie.appserver.repositories.converter.ZonedDateTimeConverter;
import com.kenzie.appserver.service.NoteService;
import com.kenzie.appserver.service.model.Note;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
public class NoteControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private NoteService noteService;
    @Autowired
    private NoteController noteController;
    private final MockNeat mockNeat = MockNeat.threadLocal();
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void createNote_createsANote() throws Exception {

        String noteId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();
        String content = "note content";
        ZonedDateTime date = ZonedDateTime.now();

        NoteRequest noteRequest = new NoteRequest();
        noteRequest.setNoteId(noteId);
        noteRequest.setUserId(userId);
        noteRequest.setContent(content);
        noteRequest.setCreatedDateTime(new ZonedDateTimeConverter().convert(date));
        noteRequest.setUpdatedDateTime(new ZonedDateTimeConverter().convert(date));

        ResultActions actions = mvc.perform(post("/v1/notes/create")
                        .content(mapper.writeValueAsString(noteRequest))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        String responseBody = actions.andReturn().getResponse().getContentAsString();
        NoteResponse response = mapper.readValue(responseBody, NoteResponse.class);
        assertThat(response.getNoteId()).isNotEmpty().as("The note id is populated");
        assertThat(response.getUserId()).isNotEmpty().as("The user id is populated");
        assertThat(response.getContent()).isEqualTo(noteRequest.getContent()).as("The content is correct");
    }

    @Test
    public void createNote_creatingNoteFails() throws Exception {
        String noteId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();
        String content = "noteContent";
        ZonedDateTime date = ZonedDateTime.now();

        NoteRequest noteRequest = new NoteRequest();
        noteRequest.setNoteId(noteId);
        noteRequest.setUserId(userId);
        noteRequest.setContent(content);
        noteRequest.setCreatedDateTime(new ZonedDateTimeConverter().convert(date));
        noteRequest.setUpdatedDateTime(new ZonedDateTimeConverter().convert(date));

        mvc.perform(post("/v1/notes/create")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());// action
    }

    @Test
    public void getNoteById_successful() throws Exception {
        String noteId = "1";
        String userId = UUID.randomUUID().toString();
        String content = "noteContent";
        ZonedDateTime date = ZonedDateTime.now();

        Note note = new Note(noteId,userId,content,date,date);

        // Add Note
        noteService.createNote(note);

        //THEN
        ResultActions actions = mvc.perform(get("/v1/notes/{noteId}", noteId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        // THEN
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        NoteResponse response = mapper.readValue(responseBody, NoteResponse.class);
        assertThat(response.getNoteId()).isNotEmpty().as("The note id is populated");
        assertThat(response.getUserId()).isNotEmpty().as("The user id is populated");
        assertThat(response.getContent()).isNotEmpty().as("The content is populated");

        noteService.deleteNote(noteId);
    }
    @Test
    public void getNoteById_unsuccessful() throws Exception {
        String noteId = "1";

        //THEN
        mvc.perform(get("/v1/notes/{noteId}", noteId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void updateNote_success() throws Exception{
        String noteId = "1";
        String userId = UUID.randomUUID().toString();
        String content = "noteContent";
        ZonedDateTime date = ZonedDateTime.now();

        Note note = new Note(noteId, userId, content, date, date);

        noteService.createNote(note);

        NoteRequest noteRequest = new NoteRequest();
        noteRequest.setNoteId(noteId);
        noteRequest.setUserId(userId);
        noteRequest.setContent(content);
        noteRequest.setCreatedDateTime(new ZonedDateTimeConverter().convert(date));
        noteRequest.setUpdatedDateTime(new ZonedDateTimeConverter().convert(date));

        ResultActions actions = mvc.perform(put("/v1/notes/{noteId}",noteId)
                        .content(mapper.writeValueAsString(noteRequest))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        String responseBody = actions.andReturn().getResponse().getContentAsString();
        NoteResponse response = mapper.readValue(responseBody, NoteResponse.class);
        assertThat(response.getNoteId()).isEqualTo(noteId).as("The note id is populated");
        assertThat(response.getUserId()).isEqualTo(userId).as("The user id is populated");
        assertThat(response.getContent()).isEqualTo(noteRequest.getContent()).as("The content is correct");

        noteService.deleteNote(noteId);
    }

    @Test
    public void updateNote_unsuccessful() throws Exception{

        NoteRequest noteRequest = new NoteRequest();
        noteRequest.setUserId("userID");
        noteRequest.setContent("noteContent");
        noteRequest.setCreatedDateTime(new ZonedDateTimeConverter().convert(ZonedDateTime.now()));
        noteRequest.setUpdatedDateTime(noteRequest.getCreatedDateTime());

        String noteId = "1";
        mvc.perform(put("/v1/notes/{noteId}",noteId)
                        .content(mapper.writeValueAsString(noteRequest))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

    }

    @Test
    public void deleteNote_success() throws Exception {
        String noteId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();
        String content = "noteContent";
        ZonedDateTime date = ZonedDateTime.now();

        Note note = new Note(noteId, userId, content, date, date);

        noteService.createNote(note);


        // WHEN
        mvc.perform(delete("/v1/notes/{noteId}",noteId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        // THEN
        mvc.perform(get("/v1/notes/{noteId}", noteId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    public void deleteCustomer_unsuccessful() throws Exception {

        String noteId = "1";

        mvc.perform(delete("/v1/notes/{noteId}", noteId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

    }

}
