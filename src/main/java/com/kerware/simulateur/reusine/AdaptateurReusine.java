package com.kerware.simulateur.reusine;

import com.kerware.simulateur.ICalculateurImpot;
import com.kerware.simulateur.SituationFamiliale;
import com.kerware.simulateur.exception.DeclarantSeulException;

public class AdaptateurReusine implements ICalculateurImpot{
	
	SimultateurReusine adapte;
	
	public AdaptateurReusine() {
		adapte = new SimultateurReusine();
	}

	@Override
	public void setRevenusNetDeclarant1(int revenusNetDeclarant1) {
		adapte.setRevenusNetDeclarant1(revenusNetDeclarant1);
	}

	@Override
	public void setRevenusNetDeclarant2(int revenusNetDeclarant2) throws DeclarantSeulException {
		adapte.setRevenusNetDeclarant2(revenusNetDeclarant2);
	}

	@Override
	public void setSituationFamiliale(SituationFamiliale situationFamiliale) {
		adapte.setSituationsFamiliale(situationFamiliale);
	}

	@Override
	public void setNbEnfantsACharge(int nbEnfantsACharge) {
		adapte.setNbEnfantsACharge(nbEnfantsACharge);
	}

	@Override
	public void setNbEnfantsSituationHandicap(int nbEnfantsSituationHandicap) {
		adapte.setNbEnfantsSituationHandicap(nbEnfantsSituationHandicap);
	}

	@Override
	public void setParentIsole(boolean estParentIsole) {
		adapte.setParentIsole(estParentIsole);
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
	public int getAbattementDeclarant2() throws DeclarantSeulException {
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
