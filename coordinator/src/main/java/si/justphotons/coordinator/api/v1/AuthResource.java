package si.justphotons.coordinator.api.v1;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import si.justphotons.coordinator.api.v1.dtos.LoginEssentials;
import si.justphotons.coordinator.services.beans.CoordinatorBean;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1")
public class AuthResource {

	private final CoordinatorBean coordinatorBean;


	public AuthResource(CoordinatorBean coordinatorBean) {
		this.coordinatorBean =  coordinatorBean;
	}


	@Operation(summary = "Login to existing user account")
	@ApiResponses(value = { 
		@ApiResponse(responseCode = "200", description = "Login successful, JWT returned", 
			content = { @Content(mediaType = "application/json", 
			schema = @Schema(implementation = String.class)) 
		}),
		@ApiResponse(responseCode = "400", description = "Invalid credentials", 
			content = @Content)
	})
	@PostMapping("/login")
	public ResponseEntity<Map<String, String>> login(@RequestBody @Valid LoginEssentials entity) {
        String jwt;
        try {
			jwt = coordinatorBean.login(entity);
		} catch (HttpClientErrorException e) {
			Map<String, String> json = new HashMap<>();
			json.put("error", e.getMessage());
			return new ResponseEntity<>(e.getStatusCode());
		}
		
		Map<String, String> json = new HashMap<>();
		json.put("token", jwt);
		return new ResponseEntity<>(json, HttpStatus.OK);
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
