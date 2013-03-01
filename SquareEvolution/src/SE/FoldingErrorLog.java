package SE;
import java.util.HashMap;
import java.util.Iterator;


public class FoldingErrorLog {

	private static FoldingErrorLog instance;
	private HashMap<String, Integer> errors;

	private FoldingErrorLog() {
		errors = new HashMap<String, Integer>();
	}

	public static FoldingErrorLog getInstance() {
		if (instance == null) {
			instance = new FoldingErrorLog();
		}
		return instance;
	}

	public void clearAll() {
		errors = new HashMap<String, Integer>();
	}

	public void addError(String error) {
		if (!errors.containsKey(error)) {
			errors.put(error, new Integer(0));
		}
		Integer old = errors.get(error);
		errors.put(error, new Integer(old.intValue() + 1));

	}

	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("Folding Errors:\n");
		if (errors.keySet() == null) {
			b.append("\tNone\n");
		} else {
			Iterator<String> it = errors.keySet().iterator();
			while (it.hasNext()) {
				String err = it.next();
				b.append("\t" + err + ": " + errors.get(err).intValue() + "\n");
			}
		}
		return b.toString();
	}

}
