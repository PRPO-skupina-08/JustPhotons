package si.justphotons.users.api.v1;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import si.justphotons.users.api.v1.dtos.UserEssentials;
import si.justphotons.users.entities.User;
import si.justphotons.users.services.beans.UsersBean;

@RestController
@RequestMapping("/v1/users")
public class UsersResource {

	private final UsersBean usersBean;

	public UsersResource(UsersBean ub) {
		this.usersBean = ub;
	}

	/* for checking the user id from jwt */
	@GetMapping("/id")
	public ResponseEntity<String> getMe(HttpServletRequest request) {
		String id = usersBean.getIdFromToken(request);
		if (id == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<>(id, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserEssentials> getOne(@PathVariable Long id) {
		UserEssentials usr = this.usersBean.getOne(id);
		if (usr != null) {
			return new ResponseEntity<>(usr, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
