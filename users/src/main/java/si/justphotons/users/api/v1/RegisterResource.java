package si.justphotons.users.api.v1;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import si.justphotons.users.api.v1.dtos.RegistrationEssentials;
import si.justphotons.users.entities.User;
import si.justphotons.users.services.beans.UsersBean;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;

@RestController
@RequestMapping("/v1/register")
public class RegisterResource {

	private final UsersBean usersBean;

	public RegisterResource(UsersBean ub) {
		this.usersBean = ub;
	}

	@PostMapping
	public ResponseEntity<User> register(@Valid @RequestBody RegistrationEssentials body) {
		User user = this.usersBean.register(body);
		if (user != null) {
			return new ResponseEntity<>(user, HttpStatus.CREATED);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	/*
	 * Error handlers 
	 */

	// catches validation errors and returns messages for each field in JSON
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(
	MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}


}
