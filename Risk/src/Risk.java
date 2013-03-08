import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class Risk {
	
	static int minD = 1;
	static int maxD = 10;
	static int minA = 2;
	static int maxA = 10;
	
	public Random r;
	public int reps;
	
	public Risk() {
		reps = 10000;
		r = new Random();
	}	

	private BattleResult battle(ArrayList<Integer> attackerDice, ArrayList<Integer> defenderDice) {
		int attackerLoses = 0;
		int defenderLoses = 0;
		for (int i = 0; i < defenderDice.size(); i++) {
			if (i > (attackerDice.size() - 1)) break;
			if (defenderDice.get(i).intValue() >= attackerDice.get(i).intValue()) {
				attackerLoses++;
			} else {
				defenderLoses++;
			}
		}
		return new BattleResult(attackerLoses, defenderLoses);
	}

	// true = attacker wins
	public CampaignResult campaign(int attackerNum, int defenderNum) {
		int atNum = attackerNum;
		int deNum = defenderNum;
		
		while (atNum > 1) {

			ArrayList<Integer> ar = null;
			if (atNum > 3) ar = rollDice(3);
			if (atNum == 3) ar = rollDice(2);
			if (atNum == 2) ar = rollDice(1);

			ArrayList<Integer> dr = null;
			if (deNum > 1) {
				dr = rollDice(2);
			} else {
				dr = rollDice(1);
			}

			BattleResult br = battle(ar,dr);
//			System.out.println("A=" + ar.toString() + " D=" + dr.toString() + " result=" + br.toString());
			atNum = atNum - br.attackerLoses;
			deNum = deNum - br.defenderLoses;
//			System.out.println("     A=" + atNum + " D=" + deNum);
			
			if (atNum < 1) return new CampaignResult(false, 0);
			if (deNum < 1) return new CampaignResult(true, atNum);
		}
		return new CampaignResult(false, 0);
	}

	public ArrayList<Integer> rollDice(int n) {
		ArrayList<Integer> dice = new ArrayList<Integer>();
		for (int i = 0; i < n; i++) {
			dice.add(new Integer(r.nextInt(6) + 1));
		}
		Collections.sort(dice, Collections.reverseOrder());
		return dice;
	}

	public void run() {
		int[][] attackerPrevails = new int[maxA + 1][maxD + 1];
		float[][] survivingAttackers = new float[maxA + 1][maxD + 1];
		for (int a = minA; a < (maxA + 1); a++) {
			for (int d = minD; d < (maxD + 1); d++) {
				
				int totalSurvivingAttackers = 0;
				for (int i = 0; i < reps; i++) {
					CampaignResult cr = campaign(a,d);
					if (cr.attackerWins) {
						attackerPrevails[a][d]++;
						totalSurvivingAttackers += cr.survivingAttackers;
					}
				}
				survivingAttackers[a][d] = (float)totalSurvivingAttackers/attackerPrevails[a][d];
			}
		}
		
		System.out.println("Number of times the attacker prevails in " + reps + " trials. {avg surviving attackers - if attacker prevails}");

		System.out.print("\t");
		for (int d = minD; d < (maxD + 1); d++) {
			System.out.print("D=" + d + "\t\t");
		}
		System.out.println();
		
		for (int a = minA; a < (maxA + 1); a++) {
			System.out.print("A=" + a + "\t");
			for (int d = minD; d < (maxD + 1); d++) {
				System.out.print(attackerPrevails[a][d] + " {"); 
				System.out.printf("%3.2f", survivingAttackers[a][d]); 
				System.out.print("}\t");	
			}
			System.out.println();
		}


	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Risk risk = new Risk();
		risk.run();
	}

}
