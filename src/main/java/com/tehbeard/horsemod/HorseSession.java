package com.tehbeard.horsemod;

public class HorseSession {
	
	public HorseState state;
	
	public String toOwner = null;

	public void resetState(){
		state = HorseState.NONE;
		toOwner = null;
	}
	
	public enum HorseState{
		NONE,
		TRANSFER,
		INFO
	}
}
