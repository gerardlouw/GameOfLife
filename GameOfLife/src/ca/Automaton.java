package ca;
public interface Automaton<StateType> {
	public StateType getState();
	public void setState(StateType state);
	public StateType accept(AutomatonVisitor visitor);
}
