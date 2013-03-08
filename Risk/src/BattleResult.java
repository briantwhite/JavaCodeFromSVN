
public class BattleResult {
	public int attackerLoses;
	public int defenderLoses;

	public BattleResult(int attackerLoses, int defenderLoses) {
		this.attackerLoses = attackerLoses;
		this.defenderLoses = defenderLoses;
	}
	
	public String toString() {
		return "A loses: " + attackerLoses + " D loses: " + defenderLoses;
	}
}
