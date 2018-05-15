package net.softesco.neonasa;

public class NeoException extends Exception {

	private static final long serialVersionUID = 1L;

	public NeoException() {
		super();
	}

	public NeoException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public NeoException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public NeoException(String arg0) {
		super(arg0);
	}

	public NeoException(Throwable arg0) {
		super(arg0);
	}

}
