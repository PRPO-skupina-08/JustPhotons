package si.justphotons.users.api.v1;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import si.justphotons.users.api.v1.dtos.LoginEssentials;
import si.justphotons.users.services.beans.UsersBean;

@RestController
@RequestMapping("/v1/login")
public class LoginResource {

    private final UsersBean usersBean;

	public LoginResource(UsersBean ub) {
        this.usersBean = ub;
	}

	@PostMapping
	public ResponseEntity<String> login(@Valid @RequestBody LoginEssentials body) {
		String jwt = this.usersBean.login(body);
		if (jwt != null) {
			return new ResponseEntity<>(jwt, HttpStatus.CREATED);
		}
		return new ResponseEntity<>("Invalid credentials", HttpStatus.BAD_REQUEST);
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
