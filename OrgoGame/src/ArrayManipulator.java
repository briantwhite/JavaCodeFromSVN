//class to implement the problematic Vector class
//with safer material

public class ArrayManipulator {

	public static int[] append(int[] startingArray, int newLastElement){

		if (startingArray == null) {
			int[] newArray = new int[1];
			newArray[0] = newLastElement;
			return newArray;
		}

		int[] newArray = new int[startingArray.length + 1];
		int i;
		for (i = 0; i < startingArray.length; i++){
			newArray[i] = startingArray[i];
		}
		newArray[i] = newLastElement;

		return newArray;
	}

	public static int[] delete(int[] startingArray, int indexToDelete) {

		if ((indexToDelete < 0) || 
				(indexToDelete >= startingArray.length)){
			return startingArray;
		}

		int[] newArray = new int[startingArray.length - 1];
		boolean afterAddedElement = false;
		for (int i = 0; i < (startingArray.length); i++){
			if (i != indexToDelete){
				if (afterAddedElement){
					newArray[i - 1] = startingArray[i];
				} else {
					newArray[i] = startingArray[i];
				}
			} else {
				afterAddedElement = true;
			}
		}

		return newArray;
	}

	public static int[] insertBefore(int[] startingArray, 
			int indexToAddBefore,
			int newElement) {

		if (startingArray == null) {
			int[] newArray = new int[1];
			newArray[0] = newElement;
			return newArray;
		}

		if ((indexToAddBefore < 0) || 
				(indexToAddBefore >= startingArray.length)){
			return startingArray;
		}

		int[] newArray = new int[startingArray.length + 1];
		boolean afterAddedElement = false;
		for (int i = 0; i < (startingArray.length); i++){
			if (i != indexToAddBefore){
				if (afterAddedElement){
					newArray[i + 1] = startingArray[i];
				} else {
					newArray[i] = startingArray[i];
				}
			} else {
				newArray[i] = newElement;
				newArray[i + 1] = startingArray[i];
				afterAddedElement = true;
			}
		}

		return newArray;
	}
}
