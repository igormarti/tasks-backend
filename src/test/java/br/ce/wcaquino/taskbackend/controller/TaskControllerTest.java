package br.ce.wcaquino.taskbackend.controller;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.ce.wcaquino.taskbackend.model.Task;
import br.ce.wcaquino.taskbackend.repo.TaskRepo;
import br.ce.wcaquino.taskbackend.utils.ValidationException;

public class TaskControllerTest {
	
	@Mock
	private TaskRepo taskRepo;
	
	@InjectMocks
	private TaskController taskController;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void ShouldNotSaveTaskWithOutDescription() {
		
		try {
			Task todo = new Task();
			todo.setDueDate(LocalDate.now().plusDays(10));
			taskController.save(todo);
			Assert.fail("It Shouldn't get to this point.");
		} catch(ValidationException e) {
			Assert.assertEquals("Fill the task description", e.getMessage());
		}
		
	}
	
	@Test
	public void ShouldNotSaveTaskWithOutDate() {
		try {
			Task todo = new Task();
			todo.setTask("Test unit.");
			taskController.save(todo);
			Assert.fail("It Shouldn't get to this point.");
		} catch(ValidationException e) {
			Assert.assertEquals("Fill the due date", e.getMessage());
		}
	}
	
	@Test
	public void ShouldNotSaveTaskWithPastDate() {
		try {
			Task todo = new Task();
			todo.setDueDate(LocalDate.of(1996,10,3));
			todo.setTask("Test unit.");
			taskController.save(todo);
			Assert.fail("It Shouldn't get to this point.");
		} catch(ValidationException e) {
			Assert.assertEquals("Due date must not be in past", e.getMessage());
		}
	}
	
	@Test
	public void ShouldSaveTaskWithSuccess() throws ValidationException {
		Task todo = new Task();
		todo.setDueDate(LocalDate.now().plusDays(10));
		todo.setTask("Test unit.");
		taskController.save(todo);
		Mockito.verify(taskRepo).save(todo);
	}

}
