// package si.justphotons.users.api.v1;

// @RestController
// @RequestMapping("/v1/login")
// public class LoginResource {

// 	public UsersResource() {

// 	}

// 	// @PostMapping
// 	// public ResponseEntity<Organisation> getOne(@PathVariable Long id) {
// 	// 	
// 	// }


// 	/*
// 	 * Error handlers 
// 	 */

// 	// catches validation errors and returns messages for each field in JSON
// 	@ResponseStatus(HttpStatus.BAD_REQUEST)
// 	@ExceptionHandler(MethodArgumentNotValidException.class)
// 	public Map<String, String> handleValidationExceptions(
// 	MethodArgumentNotValidException ex) {
// 		Map<String, String> errors = new HashMap<>();
// 		ex.getBindingResult().getAllErrors().forEach((error) -> {
// 			String fieldName = ((FieldError) error).getField();
// 			String errorMessage = error.getDefaultMessage();
// 			errors.put(fieldName, errorMessage);
// 		});
// 		return errors;
// 	}


// }
