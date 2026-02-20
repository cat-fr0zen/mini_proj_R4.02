package com.kerware.simulateur.reusine;

import com.kerware.simulateur.ICalculateurImpot;
import com.kerware.simulateur.SituationFamiliale;

public class AdaptateurReusine implements ICalculateurImpot{
	
	SimultateurReusine adapte;
	
	public AdaptateurReusine() {
		adapte = new SimultateurReusine();
	}

	@Override
	public void setRevenusNet(int rn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSituationFamiliale(SituationFamiliale sf) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNbEnfantsACharge(int nbe) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNbEnfantsSituationHandicap(int nbesh) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setParentIsole(boolean pi) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void calculImpotSurRevenuNet() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getRevenuFiscalReference() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getAbattement() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getNbPartsFoyerFiscal() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getImpotAvantDecote() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDecote() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getImpotSurRevenuNet() {
		// TODO Auto-generated method stub
		return 0;
	}
}
