package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public int save(Note note){
        return noteMapper.save(note);
    }

    public void update(Note note){
        noteMapper.update(note);
    }

    public Note getNote(Integer noteId){
        return noteMapper.getNote(noteId);
    }

    public List<Note> getNotes(Integer userId){
        return noteMapper.getNotes(userId);
    }

    public void delete(Integer noteId){
        noteMapper.delete(noteId);
    }
}
