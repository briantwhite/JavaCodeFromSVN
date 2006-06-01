/////////////////////////////////////////////////////////////////
// package declaration here
/////////////////////////////////////////////////////////////////
package protex;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

/////////////////////////////////////////////////////////////////
// import libraries here
/////////////////////////////////////////////////////////////////
import java.util.ArrayList;


/////////////////////////////////////////////////////////////////
// class starts here
/////////////////////////////////////////////////////////////////

public class LCS {


/////////////////////////////////////////////////////////////////
// variable declaration here
// These are "constants" which indicate a direction in the backtracking array.
/////////////////////////////////////////////////////////////////

  private static final int NEITHER = 0;
  private static final int UP = 1;
  private static final int LEFT = 2;
  private static final int UP_AND_LEFT = 3;


/////////////////////////////////////////////////////////////////
// constructor definition here
// Construct the application
/////////////////////////////////////////////////////////////////

  public static String LCSAlgorithm(ArrayList a, ArrayList b) {

    int n = a.size();
    int m = b.size();

    int S[][] = new int[n + 1][m + 1];
    int R[][] = new int[n + 1][m + 1];

    int ii, jj;

// It is important to use <=, not <.  The next two for-loops are initialization

    for (ii = 0; ii <= n; ++ii) {

      S[ii][0] = 0;
      R[ii][0] = UP;
    }
    for (jj = 0;jj <= m;++jj) {
      S[0][jj] = 0;
      R[0][jj] = LEFT;
    }
// This is the main dynamic programming loop that computes the score and
// backtracking arrays.
    for(ii = 1;ii <= n;++ii) {
      for (jj = 1;jj <= m;++jj) {
        if (((String)(a.get(ii - 1))).equals((String)(b.get(jj - 1)))){
          S[ii][jj] = S[ii - 1][jj - 1] +1;

          R[ii][jj] = UP_AND_LEFT;
        }
        else {
          S[ii][jj] = S[ii - 1][jj - 1] + 0;
          R[ii][jj] = NEITHER;
        }
        if (S[ii - 1][jj] >= S[ii][jj]) {
          S[ii][jj] = S[ii - 1][jj];
          R[ii][jj] = UP;
        }
        if (S[ii][jj - 1] >= S[ii][jj]) {
          S[ii][jj] = S[ii][jj - 1];
          R[ii][jj] = LEFT;
        }
      }
    }
// The length of the longest substring is S[n][m]
    ii = n;
    jj = m;
    int pos = S[ii][jj] - 1;
    int list1 = n+m-pos;

    ArrayList lcs1 = new ArrayList(list1);
    ArrayList lcs2 = new ArrayList(list1);

    for(int x = 0;x < list1;x++) {
    lcs1.add(x,new String (""));
    lcs2.add(x,new String (""));
    }
// Trace the backtracking matrix.


while( ii > 0 || jj > 0 ) {
    if (R[ii][jj] == UP_AND_LEFT) {

      ii--;
      jj--;
      list1--;

      lcs1.remove(list1);
      lcs2.remove(list1);
      lcs1.add(list1,(String)(a.get (ii)));
      lcs2.add(list1,(String)(b.get (jj)));

    }
    else if (R[ii][jj] == UP) {
      ii--;
      list1--;

      lcs1.remove(list1);
      lcs2.remove(list1);
      lcs1.add(list1,(String)(a.get (ii)));
      lcs2.add(list1, new String("***"));
    }
    else if (R[ii][jj] == LEFT) {
      jj--;
      list1--;

      lcs1.remove(list1);
      lcs2.remove(list1);
      lcs1.add(list1, new String("***"));
      lcs2.add(list1,(String)(b.get (jj)));
    }
  }

  return new String("TOP Panel: " + lcs1.toString() + "\n" + "BOT Panel: " +
                    lcs2.toString());
}


/////////////////////////////////////////////////////////////////
// method name: main
//        type: public, static
//   called by: none.
//    comments: to test LCS class
/////////////////////////////////////////////////////////////////

public static void main(String args[]) {
  try {
//    String s = LCSAlgorithm(args[0], args[1]);

    ArrayList a = new ArrayList();
    a.add("p");
    a.add("q");
    a.add("a");
    a.add("b");
    a.add("c");
    a.add("s");
    a.add("t");
    a.add("a");
    a.add("y");

    ArrayList b = new ArrayList();
    b.add("p");
    b.add("x");
    b.add("a");
    b.add("b");
    b.add("c");
    b.add("y");

    String s = LCSAlgorithm(a,b);
    System.out.println(s);
  }
  catch (Exception e) {
    e.printStackTrace();
  }
}

/////////////////////////////////////////////////////////////////
//end of class here
/////////////////////////////////////////////////////////////////
}
