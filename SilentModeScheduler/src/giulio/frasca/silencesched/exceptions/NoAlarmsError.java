package giulio.frasca.silencesched.exceptions;
/**
 * Exception thrown if no blocks are set in the prefs file
 * @author Giulio Frasca
 *
 */
public class NoAlarmsError extends Exception {

	private String message;
	
	/**
	 * Standard Constructor
	 * @param m - the message this exception contains
	 */
	public NoAlarmsError(String m) {
		this.message=m;
	}
	
	/**
	 * Gets the message this exception contains
	 * 
	 * @return this exception's message
	 */
	public String getMessage(){
		return message;
	}

	
	private static final long serialVersionUID = 1L;

}
