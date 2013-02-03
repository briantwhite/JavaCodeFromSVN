package protex;

public class PasswordVault {
	
	private static PasswordVault pv;
	public String email;
	public String password;
	
	public static PasswordVault getInstance() {
		if (pv == null) {
			pv = new PasswordVault();
		}
		return pv;
	}
	
	private PasswordVault() {
		email = null;
		password = null;
	}

}
