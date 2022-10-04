package engine;

public interface GameListener {
	
	public void onMove();
	public void onAttack();
	public void onCastAbility1();
	public void onCastAbility2();
	public void onCastAbility3();
	public void onEndTurn();
	public void onLoadChampions();
	

}
