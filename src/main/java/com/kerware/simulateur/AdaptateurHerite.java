package com.kerware.simulateur;

public class AdaptateurHerite implements ICalculateurImpot {
	
	SimulateurHerite adapte;
	
	public AdaptateurHerite() {
		adapte = new SimulateurHerite();
	}

	@Override
	public void setRevenusNet(int rn) {
		adapte.setRevenusNet(rn);
	}

	@Override
	public void setSituationFamiliale(SituationFamiliale sf) {
		adapte.setSituationsFamiliale(sf);
	}

	@Override
	public void setNbEnfantsACharge(int nbe) {
		adapte.setNbEnfantsACharge(nbe);
	}

	@Override
	public void setNbEnfantsSituationHandicap(int nbesh) {
		adapte.setNbEnfantsSituationHandicap(nbesh);
	}

	@Override
	public void setParentIsole(boolean pi) {
		adapte.setParentIsole(pi);
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
	public int getAbattement() {
		return adapte.getAbattement();
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
