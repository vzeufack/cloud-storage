package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/home")
public class HomeController {
    private final UserService userService;

    private final NoteService noteService;

    private final CredentialService credentialService;

    private final EncryptionService encryptionService;

    private final FileService fileService;

    public HomeController(UserService userService, NoteService noteService, CredentialService credentialService, EncryptionService encryptionService, FileService fileService) {
        this.userService = userService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
        this.fileService = fileService;
    }

    @GetMapping()
    public String homeView(Authentication authentication, Model model, Note note, Credential credential) {
        Integer userId = userService.getUser(authentication.getName()).getUserId();
        model.addAttribute("notes", noteService.getNotes(userId));
        model.addAttribute("credentials", credentialService.getCredentials(userId));
        model.addAttribute("encryption", encryptionService);
        model.addAttribute("files", fileService.getFiles(userId));
        return "home";
    }

    @PostMapping("/note")
    public String createNote(Authentication authentication, ModelMap model, Note note, RedirectAttributes redirectAttributes){
        try {
            Integer userId = userService.getUser(authentication.getName()).getUserId();
            note.setUserId(userId);
            if (note.getNoteId() == null) {
                noteService.save(note);
                redirectAttributes.addFlashAttribute("noteSuccessMsg", "Note successfully created");
            } else {
                noteService.update(note);
                redirectAttributes.addFlashAttribute("noteSuccessMsg", "Note successfully edited");
            }
            model.addAttribute("notes", noteService.getNotes(userId));
        }
        catch (Exception e){
            redirectAttributes.addFlashAttribute("noteErrorMsg", "There was an error was creating or editing the note");
        }

        return "redirect:/home#nav-notes";
    }

    @RequestMapping("/note/delete/{noteId}")
    public String deleteNote(@PathVariable Integer noteId, RedirectAttributes redirectAttributes){
        noteService.delete(noteId);
        redirectAttributes.addFlashAttribute("noteDeleteMsg", "Note was successfully deleted");
        return "redirect:/home#nav-notes";
    }

    @PostMapping("/credential")
    public String createCredential(Authentication authentication, Model model, Credential credential, RedirectAttributes redirectAttributes){
        try {
            Integer userId = userService.getUser(authentication.getName()).getUserId();
            credential.setUserId(userId);
            if (credential.getCredentialId() == null) {
                credentialService.save(credential);
                redirectAttributes.addFlashAttribute("credentialSuccessMsg", "Credential successfully created");
            } else {
                credentialService.update(credential);
                redirectAttributes.addFlashAttribute("credentialSuccessMsg", "Credential successfully edited");
            }
            model.addAttribute("credentials", credentialService.getCredentials(userId));
        }
        catch (Exception e){
            redirectAttributes.addFlashAttribute("credentialErrorMsg", "There was an error was creating or editing the credential");
            redirectAttributes.addFlashAttribute("credentialErrorMsg", "There was an error was creating or editing the credential");
        }

        return "redirect:/home#nav-credentials";
    }

    @RequestMapping("/credential/delete/{credentialId}")
    public String deleteCredential(@PathVariable Integer credentialId, RedirectAttributes redirectAttributes){
        credentialService.delete(credentialId);
        redirectAttributes.addFlashAttribute("credentialDeleteMsg", "Credential was successfully deleted");
        return "redirect:/home#nav-credentials";
    }

    @PostMapping("/file/upload")
    public String uploadFile(Authentication authentication, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes){
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:/home";
        }

        try{
            Integer userId = userService.getUser(authentication.getName()).getUserId();
            fileService.save(file, userId);
            redirectAttributes.addFlashAttribute("fileSuccessMsg", "File uploaded successfully");
        }
        catch(IOException e){
            redirectAttributes.addFlashAttribute("fileErrorMsg", e.getMessage());
        }

        return "redirect:/home";
    }

    @GetMapping("/file/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Integer id) {
        File file = fileService.getFileById(id);
        if (file != null) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                    .body(file.getFileData());
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping("/file/delete/{id}")
    public String deleteFile(@PathVariable Integer id, RedirectAttributes redirectAttributes){
        fileService.delete(id);
        redirectAttributes.addFlashAttribute("fileDeleteMsg", "File deleted successfully");
        return "redirect:/home#nav-files";
    }
}
