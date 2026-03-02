package com.kerware.simulateur.reusine;

import com.kerware.simulateur.SituationFamiliale;
import com.kerware.simulateur.exception.DeclarantSeulException;

public class SimultateurReusine {
	
	private int revenusNetDeclarant1;
	private int revenusNetDeclarant2;
	private SituationFamiliale situationFamiliale;
	private int nbEnfantsACharge;
	private int nbEnfantsSituationHandicap;
	private boolean estParentIsole;

	/**
     * Changer le revenus net du premier déclarant
     * @param revenusNetDeclarant1 le nouveu revenus net
     * @author picots
     */
	public void setRevenusNetDeclarant1(int revenusNetDeclarant1) {
		this.revenusNetDeclarant1 = revenusNetDeclarant1;
	}
	
	/**
	 * Changer le revenus net du second déclarant
	 * @param revenusNetDeclarant2 le nouveau revenus net
	 * @throws DeclarantSeulException si le premier déclarant est un parent isolé
	 * @author picots
	 */
	public void setRevenusNetDeclarant2(int revenusNetDeclarant2) throws DeclarantSeulException {
		switch(situationFamiliale) {
			case PACSE, MARIE -> this.revenusNetDeclarant2 = revenusNetDeclarant2;
			default -> throw new DeclarantSeulException();
		}
	}

	/**
	 * Changer la situation familiale
	 * @param situationFamiliale la nouvelle situation familiale
	 * @author picots
	 */
	public void setSituationsFamiliale(SituationFamiliale situationFamiliale) {
		this.situationFamiliale = situationFamiliale;
	}
	
	/**
	 * Changer le nombre d'enfants à charge
	 * @param nbEnfantsACharge le nouveau nombre d'enfants à charge
	 * @author picots
	 */
	public void setNbEnfantsACharge(int nbEnfantsACharge) {
		this.nbEnfantsACharge = nbEnfantsACharge;
	}
	
	/**
	 * Changer le nombre d'enfants en situation de handicap à charge
	 * @param nbEnfantsSituationHandicap le nouveau nombre d'enfants en situation de handicap à charge
	 * @author picots
	 */
	public void setNbEnfantsSituationHandicap(int nbEnfantsSituationHandicap) {
		this.nbEnfantsSituationHandicap = nbEnfantsSituationHandicap;
	}
	
	/**
	 * Changer le statut de parent isolé
	 * @param estParentIsole le nouveau statut
	 * @author picots
	 */
	public void setParentIsole(boolean estParentIsole) {
		this.estParentIsole = estParentIsole;
	}
}
