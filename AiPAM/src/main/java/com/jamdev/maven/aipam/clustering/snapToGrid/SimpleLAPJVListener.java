package com.jamdev.maven.aipam.clustering.snapToGrid;

public class SimpleLAPJVListener implements AssignmentListener {

	@Override
	public void columnReduction() {
		System.out.println("columnReduction");
		
	}

	@Override
	public void reductionTransfer() {
		System.out.println("reductionTransfer");

		
	}

	@Override
	public void augmenting() {
		System.out.println("augmenting");
	}

	@Override
	public void augmentingRowReduction(int f0, int k, int count) {
		System.out.println("augmentingRowReduction: k " + k + " f0: " + f0);
		
	}

}
