package com.kerware.simulateur.reusine;

import com.kerware.simulateur.ICalculateurImpot;
import com.kerware.simulateur.SituationFamiliale;

public class AdaptateurReusine implements ICalculateurImpot{
	
	private final SimultateurReusine adapte;
	
	public AdaptateurReusine() {
		adapte = new SimultateurReusine();
	}

	@Override
	public void setRevenusNetDeclarant1(int rn) {
		
	}

	@Override
	public void setRevenusNetDeclarant2(int rn) {
		
	}

	@Override
	public void setSituationFamiliale(SituationFamiliale sf) {
		
	}

	@Override
	public void setNbEnfantsACharge(int nbe) {
		
	}

	@Override
	public void setNbEnfantsSituationHandicap(int nbesh) {
			
	}

	@Override
	public void setParentIsole(boolean pi) {
		
	}

	@Override
	public void calculImpotSurRevenuNet() {
		
	}

	@Override
	public int getRevenuFiscalReference() {
		return 0;
	}

	@Override
	public int getAbattementDeclarant1() {
		return 0;
	}

	@Override
	public int getAbattementDeclarant2() {
		return 0;
	}

	@Override
	public double getNbPartsFoyerFiscal() {
		return 0;
	}

	@Override
	public int getImpotAvantDecote() {
		return 0;
	}

	@Override
	public int getDecote() {
		return 0;
	}

	@Override
	public int getImpotSurRevenuNet() {
		return 0;
	}

	
}
