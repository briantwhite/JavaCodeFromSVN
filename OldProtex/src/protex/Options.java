// Options.java
//
// 
// Java for Programmers, Exercise 2
// Copyright 1999, Ethan Bolker and Paul English
/* 
 * License Information
 * 
 * This  program  is  free  software;  you can redistribute it and/or modify it 
 * under  the  terms  of  the GNU General Public License  as  published  by the 
 * Free  Software  Foundation; either version 2  of  the  License,  or (at your 
 * option) any later version.
 */

package protex;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Emulate options parsing, honoring Unix option syntax: command line options
 * come first, with prefix '-'.
 * 
 * Various constructors accept those command line options as an array of
 * Strings, as a List of Strings or as a StringTokenizer.
 * 
 * @author Ethan Bolker
 */

public class Options {
	// The core of the Options object is a private instance variable
	// options of type Map.
	//
	// The constructor parses the two Strings of legal flags to
	// create corresponding Sets. Then it parses the List of actual
	// arguments, as long as entries in the List begins with a '-'.
	// For each legal flag found, it creates an Entry in the options
	// Map with the flag for a key and value either "" or,
	// for options requiring an argument, the next String in the List.
	// 
	// Errors encountered are stored in another Map.
	//
	// Public methods isOpt, getOpt and getErrors provide access to
	// this information.
	//
	// Debugging relies on the toString methods of the various
	// collection classes.

	private boolean DEBUG = false;

	private Set flags; // store flags

	private Set flagsNeedingValues; // store flagsNeedingValues

	private Map defaultValues; // defaults for flagsNeedingValues

	private Map options; // store options found

	private Map errors; // store errors encountered

	private List argsLeft; // store arguments following options

	private final static String UNKNOWN_OPTION = "unknown option";

	private final static String DUPLICATE_OPTION = "duplicate option";

	private final static String MISSING_OPTARG = "required option value missing";

	private final static String NOT_INT = "integer value required";

	/**
	 * Create an Options object given Strings encoding options to look for and a
	 * List of arguments to look in.
	 * 
	 * If the Strings of legal options are
	 * 
	 * <pre>
	 * 
	 *  
	 *             flags:              &quot;e g version&quot;
	 *             flagsNeedingValues: &quot;name:mars index&quot;
	 *    
	 *  
	 * </pre>
	 * 
	 * Then the command line arguments
	 * 
	 * <pre>
	 * 
	 *  
	 *         -g -index 3 -trash more args not options
	 *    
	 *  
	 * </pre>
	 * 
	 * would be parsed to say that options <code>g</code> and
	 * <code>index</code> (with integer value <code>3</code>) and
	 * <code>name</code> (with default value <code>"mars</code> had been
	 * found along with the unknown option <code>trash</code>.
	 * 
	 * The input List command line arguments is shifted so that just
	 * 
	 * <pre>
	 * 
	 *  
	 *         more args not options
	 *    
	 *  
	 * </pre>
	 * 
	 * remain.
	 * 
	 * @param flags
	 *            options to be recognized (only)
	 * @param flagsNeedingValues
	 *            options that need values
	 * @param args
	 *            List of Strings to parse for options
	 * @param debug
	 *            turn debug on/off
	 *  
	 */

	public Options(String flags, String flagsNeedingValues, List args,
			boolean debug) {
		if (DEBUG) {
			System.out.println("---- Options debugging output ----");
		}
		this.options = new TreeMap();
		errors = new TreeMap();
		this.flags = parseString(flags);
		this.flagsNeedingValues = parseFlagsNeedingValues(flagsNeedingValues);
		if (DEBUG) {
			System.out.println("flags:\n" + flags);
			System.out.println("flags needing values:\n" + flagsNeedingValues);
			System.out.println("options input:\n" + args);
		}
		argsLeft = parseArgs(options, args);
		DEBUG = debug;

		if (DEBUG) {
			System.out.println("options found:\n" + options);
			System.out.println("args left:\n" + argsLeft);
			System.out.println("----------------------------------");
		}
	}

	/**
	 * Create an options object given sets of options to look for and an array
	 * of strings to look in.
	 * 
	 * @param flags
	 *            flags to be recognized
	 * @param flagsNeedingValues
	 * @param args
	 *            array of strings to parse for options
	 *  
	 */
	public Options(String flags, String flagsNeedingValues, String[] args) {
		this(flags, flagsNeedingValues, args, false);
	}

	public Options(String flags, String flagsNeedingValues, String[] args,
			boolean debug) {
		this(flags, flagsNeedingValues, arrayToList(args), debug);
	}

	/**
	 * Create an options object given sets of options to look for and a
	 * StringTokenizer of strings to look in.
	 * 
	 * @param flags
	 *            flags to be recognized
	 * @param flagsNeedingValues
	 * @param args
	 *            StringTokenizer to parse for options
	 *  
	 */
	public Options(String flags, String flagsNeedingValues, StringTokenizer args) {
		this(flags, flagsNeedingValues, args, false);
	}

	public Options(String flags, String flagsNeedingValues,
			StringTokenizer args, boolean debug) {
		this(flags, flagsNeedingValues, tokenizerToList(args), debug);
	}

	/**
	 * Tell whether a particular option was found.
	 * 
	 * @param opt
	 *            the option to look for
	 * @return true when opt was found
	 *  
	 */
	public boolean isOpt(String opt) {
		return options.containsKey(opt);
	}

	/**
	 * Retrieve the value for options that require them.
	 * 
	 * @param key
	 *            the option whose value we seek
	 * @return the key's value, "" if none, null if opt not an option
	 */
	public String getOpt(String key) {
		return (String) options.get(key);
	}

	/**
	 * Retrieve the integer value for options that require them.
	 * 
	 * @param key
	 *            the option whose value we seek
	 * @return the key's value,
	 * 
	 * @throws NumberFormatException.
	 */
	public int getIntOpt(String key) throws NumberFormatException {
		return Integer.parseInt((String) options.get(key));
	}

	/**
	 * Return what's left over after option parsing, in a List of Strings.
	 * 
	 * @return shifted List of Strings
	 */
	public List getShiftList() {
		return argsLeft;
	}

	/**
	 * Return what's left over after option parsing, in an array of Strings.
	 * 
	 * @return shifted array of Strings
	 */
	public String[] getShiftArray() {
		return (String[]) argsLeft.toArray(new String[0]);
	}

	/**
	 * Return what's left over after option parsing, in a StringTokenizer.
	 * 
	 * @return shifted StringTokenizer
	 */
	public StringTokenizer getShiftTokenizer() {
		String s = "";
		for (Iterator iter = argsLeft.iterator(); iter.hasNext();)
			s += " " + (String) iter.next();
		return new StringTokenizer(s);
	}

	/**
	 * Retrieve the table of option parsing errors. Table entries are
	 * (opt_found, error_message) pairs of Strings.
	 * 
	 * @return a Map of error pairs
	 */
	public Map getErrors() {
		return errors;
	}

	//--------------------- private methods ---------------------

	// Parse String s to create a Set whose elements are
	// the tokens in flags.
	//
	private Set parseString(String flags) {
		Set s = new HashSet();
		StringTokenizer st = new StringTokenizer(flags);
		while (st.hasMoreTokens()) {
			s.add(st.nextToken());
		}
		return s;
	}

	// Parse String s to create a Set whose elements are
	// the tokens in flags, with map for default values.
	//
	private Set parseFlagsNeedingValues(String flags) {
		Set s = new TreeSet();
		StringTokenizer st = new StringTokenizer(flags);
		while (st.hasMoreTokens()) {
			String next = st.nextToken();
			StringTokenizer local = new StringTokenizer(next, ": ");
			String thisOpt = local.nextToken();
			s.add(thisOpt);
			if (local.hasMoreTokens()) {
				options.put(thisOpt, local.nextToken());
			}
		}
		return s;
	}

	// Parse the arg List to create and return a Map of
	// options found, with their values when values are required.
	// Return the remainder of the arg List.
	//
	private List parseArgs(Map options, List args) {
		Iterator iter = args.iterator();
		while (iter.hasNext()) {
			String arg = (String) iter.next();
			boolean isOpt = (arg.charAt(0) == '-');
			if (!isOpt)
				break; // option parsing complete
			iter.remove();
			arg = arg.substring(1, arg.length());
			if (options.containsKey(arg)) {
				errors.put(arg, DUPLICATE_OPTION);
			}
			if (flags.contains(arg)) {
				options.put(arg, "");
				continue;
			}
			if (flagsNeedingValues.contains(arg)) {
				if (!iter.hasNext()) {
					errors.put(arg, MISSING_OPTARG);
				} else {
					options.put(arg, (String) iter.next());
					iter.remove();
				}
				continue;
			}
			errors.put(arg, UNKNOWN_OPTION);
		}
		return args;
	}

	// Construct a List whose elements are the Strings in array args
	private static List arrayToList(String[] args) {
		List stringList = new ArrayList(args.length);
		for (int i = 0; i < args.length; i++) {
			stringList.add(args[i]);
		}
		return stringList;
	}

	// Construct a List whose elements are the String tokens in
	// StringTokenizer args
	private static List tokenizerToList(StringTokenizer args) {
		List stringList = new ArrayList(args.countTokens());
		while (args.hasMoreTokens())
			stringList.add(args.nextToken());
		return stringList;
	}

	/**
	 * 
	 * Method main for unit testing.
	 *  
	 */
	public static void main(String[] argv) {
		System.out
				.println("Look for options -e, -g, -name (default mars), -index");
		Options opts = new Options("e g", "name:mars index", argv, true);
		if (opts.isOpt("index")) {
			System.out.println("index + 1 " + (1 + opts.getIntOpt("index")));
		}
		System.out.println("test getShiftTokenizer");
		for (StringTokenizer st = opts.getShiftTokenizer(); st.hasMoreTokens();)
			System.out.println(st.nextToken());
		System.out.println("----- found errors -----");
		System.out.println(opts.getErrors());
	}
}

