package com.kerware.simulateur;

import com.kerware.simulateur.exception.DeclarantSeulException;

public class AdaptateurHerite implements ICalculateurImpot {

	private final SimulateurHerite adapte;

	public AdaptateurHerite() {
		adapte = new SimulateurHerite();
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
		adapte.calculImpotSurRevenuNet();
	}

	@Override
	public int getRevenuFiscalReference() {
		return adapte.getRevenuFiscalReference();
	}

	@Override
	public int getAbattementDeclarant1() {
		return adapte.getAbattementDeclarant1();
	}

	@Override
	public int getAbattementDeclarant2() throws DeclarantSeulException {
		return adapte.getAbattementDeclarant2();
	}

	@Override
	public double getNbPartsFoyerFiscal() {
		return adapte.getNbPartsFoyerFiscal();
	}

	@Override
	public int getImpotAvantDecote() {
		return adapte.getImpotSurRevenuNet() + adapte.getDecote();
	}

	@Override
	public int getDecote() {
		return adapte.getDecote();
	}

	@Override
	public int getImpotSurRevenuNet() {
		return adapte.getImpotSurRevenuNet();
	}
}
