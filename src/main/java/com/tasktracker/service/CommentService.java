package com.tasktracker.service;

import com.tasktracker.dto.CommentDto;
import com.tasktracker.model.Comment;
import com.tasktracker.model.Task;
import com.tasktracker.model.User;
import com.tasktracker.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Transactional
    public Comment createComment(User author, Task task, CommentDto commentDto){
        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setTask(task);
        comment.setText(commentDto.getText());
        comment.setPostedAt(new Timestamp(System.currentTimeMillis()));
        return commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<Comment> getAllComments(Task task){
        return commentRepository.findByTask(task);
    }

    @Transactional
    public Comment saveComment(CommentDto commentDto){
        Comment comment = commentRepository.findById(commentDto.getId()).get();
        comment.setText(commentDto.getText());
        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long id){
        commentRepository.deleteById(id);
    }
}
