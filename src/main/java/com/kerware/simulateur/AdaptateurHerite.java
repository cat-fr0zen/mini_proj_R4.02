package com.kerware.simulateur;

public class AdaptateurHerite implements ICalculateurImpot {
	
	SimulateurHerite adapte;
	
	public AdaptateurHerite() {
		adapte = new SimulateurHerite();
	}

	@Override
	public void setRevenusNetDeclarant1(int rn) {
		//TODO
		adapte.setRevenusNet(rn);
	}
	
	@Override
	public void setRevenusNetDeclarant2(int rn) {
		//TODO 
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
	public int getAbattementDeclarant1() {
		//TODO
		return adapte.getAbattement();
	}
	
	@Override
	public int getAbattementDeclarant2() {
		//TODO
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
